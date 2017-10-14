package com.company;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;


public class DBMakers {

    public static Document htmlDocument;
    public static String htmlPageUrl = "http://www.gsmarena.com/makers.php3";
    public static ArrayList<JsonModel> arrayList;

    public static void main(String args[]) {


        readJson();
       /* arrayList = new ArrayList<>();
        htmlParser();*/

    }

    public static void htmlParser() {

        ArrayList<MakerModel> arrayListMakerModel = getJson();
        try {
            for (int a = 0; a < arrayListMakerModel.size(); a++) {
                getModels(arrayListMakerModel.get(a), arrayListMakerModel.get(a).getNumber());
            }

            JsonModel[] hjh = arrayList.toArray(new JsonModel[arrayList.size()]);
            Gson gson = new Gson();
            FileWriter writer = new FileWriter(new File("D:\\json_shit2.json"));
            writer.write(gson.toJson(hjh, JsonModel[].class));
            writer.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static ArrayList<MakerModel> getJson() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            MakerModel[] models = objectMapper.readValue(new File("makers.json"), MakerModel[].class);
            return new ArrayList<MakerModel>(Arrays.asList(models));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String excutePost(String targetURL) {
        HttpURLConnection connection = null;
        try {
            //Create connection
            URL url = new URL(targetURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);

            //Get Response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            StringBuilder response = new StringBuilder(); // or StringBuffer if not Java 5+
            String line;
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private static void getModels(final MakerModel model, final int number) {
        Document htmlDocument;
        String finalAddress = model.getAddress().replace("http://www", "http://m");

        htmlDocument = Jsoup.parse(excutePost(finalAddress), "http://m.gsmarena.com");
        Elements links = htmlDocument.select("div[class=general-menu]");

        String pages = htmlDocument.select("div[class=nav-current]").select("span[class=left]").text();
        int page = Integer.parseInt(pages.substring(5, pages.length()));
        System.out.println(page);

        for (int s = 1; s < page + 1; s++) {
            System.out.println(htmlDocument.select("div[class=nav-current]"));
            String nextPageLink = htmlDocument.select("div[class=nav-current]").select("a").get(1).absUrl("href");
            System.out.println(" ---- " + nextPageLink);
            System.out.println(htmlDocument.select("div[class=st-text]").select("td"));


            for (int h = 0; h < links.select("a").size(); h++) {
                JsonModel newJson = new JsonModel();
                String name = links.select("a").get(h).text();
                String url = links.select("a").get(h).absUrl("href");
                String image_url = links.select("a").get(h).select("img").get(0).absUrl("src");

                //newJson.setId(h + 1);
                newJson.setMaker(model.getName());
                newJson.setModel(name);
                newJson.setAddress(url);
                newJson.setImage_ADDRESS(image_url);

                System.out.println(name);
                System.out.println(url);


                arrayList.add(newJson);
            }

            htmlDocument = Jsoup.parse(excutePost(nextPageLink), "http://m.gsmarena.com");
            links = htmlDocument.select("div[class=general-menu]");
        }

    }

    private static void readJson(){
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            JsonModel[] jsons = mapper.readValue(new File("D:\\json_shit2.json"), JsonModel[].class);
            ArrayList<JsonModel> jsonModels = new ArrayList<>(Arrays.asList(jsons));
            System.out.println("**** " +  jsonModels.size());

            Collections.sort(jsonModels, new Comparator<JsonModel>() {
                @Override
                public int compare(JsonModel o1, JsonModel o2) {
                    if(o1.getModel().compareToIgnoreCase(o2.getModel()) == 0
                            && o1.getMaker().compareToIgnoreCase(o2.getMaker()) == 0){
                        System.out.println("**** " +  o1.getMaker() + " : " + o1.getModel());
                        System.out.println("**** " +  o2.getMaker() + " : " + o2.getModel());
                    }
                    return o1.getModel().compareToIgnoreCase(o2.getModel());
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}