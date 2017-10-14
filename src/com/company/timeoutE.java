package com.company;

import com.google.gson.Gson;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Mojta on 3/9/2016.
 */
public class timeoutE {

    public static ArrayList<JsonModel> arrayList;


    public static void main(String[] args) {

        //"http://m.gsmarena.com/verykool-phones-f-70-0-p5.php";
        arrayList = new ArrayList<>();

        getModels("http://m.gsmarena.com/verykool-phones-f-70-0-p5.php");
        try {
        JsonModel[] hjh = arrayList.toArray(new JsonModel[arrayList.size()]);
        Gson gson = new Gson();
        FileWriter writer = null;
        writer = new FileWriter(new File("D:\\json333.json"));
        writer.write(gson.toJson(hjh, JsonModel[].class));
        writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    private static void getModels(String address) {
        Document htmlDocument;
        String finalAddress = address.replace("http://www", "http://m");

        try {
            htmlDocument = Jsoup.connect(finalAddress).timeout(5000).get();
            Elements links = htmlDocument.select("div[class=general-menu]");

            String pages = htmlDocument.select("div[class=nav-current]").select("span[class=left]").text();
            int page = Integer.parseInt(pages.substring(5, pages.length()));
            System.out.println(page);
/*

            for (int s = 1; s < page + 1; s++) {
                System.out.println(htmlDocument.select("div[class=nav-current]"));
                String nextPageLink = htmlDocument.select("div[class=nav-current]").select("a").get(1).absUrl("href");
                System.out.println(" ---- " + nextPageLink);
                System.out.println(htmlDocument.select("div[class=st-text]").select("td"));
*/


                for (int h = 0; h < links.select("a").size(); h++) {
                    JsonModel newJson = new JsonModel();
                    String name = links.select("a").get(h).text();
                    String url = links.select("a").get(h).absUrl("href");
                    String image_url = links.select("a").get(h).select("img").get(0).absUrl("src");

                    //newJson.setId(h + 1);
                    newJson.setMaker("verykool");
                    newJson.setModel(name);
                    newJson.setAddress(url);
                    newJson.setImage_ADDRESS(image_url);

                    System.out.println(name);
                    System.out.println(url);


                    arrayList.add(newJson);
                }

/*

                htmlDocument = Jsoup.connect(nextPageLink).get();
                links = htmlDocument.select("div[class=general-menu]");
            }
*/


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
