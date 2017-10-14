package com.company;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class DBModel {

    public static Document htmlDocument;
    public static String htmlPageUrl = "http://www.gsmarena.com/acer-phones-59.php";


    public static void main(String args[]) {
        Connection c = null;
        Statement stmt = null;

        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:MobileInfo.db");
            System.out.println("Opened database successfully");

            stmt = c.createStatement();
            /*String SQL =
                    "CREATE TABLE MODELS " +
                    "(ID                        INT PRIMARY KEY NOT NULL," +
                    " MAKER                     TEXT," +
                    " MODEL                     TEXT," +
                    " ADDRESS                   TEXT," +
                    " IMAGE_ADDRESS             TEXT)";
            stmt.executeUpdate(SQL);*/
            System.out.println("DB created successfully");

            ResultSet rs = stmt.executeQuery("SELECT * FROM MAKERS;");
            ArrayList<MakersModel> makersModelArrayList = new ArrayList<>();
            while (rs.next()) {
                String address = rs.getString("address");
                String maker = rs.getString("name");
                System.out.println(" --------> " + maker);
                MakersModel makersModel = new MakersModel();
                makersModel.setAddress(address);
                makersModel.setName(maker);
                makersModelArrayList.add(makersModel);
                htmlParser(makersModel);
            }
            rs.close();

/*
            System.out.println("********************** "+result.size()+" **********************");
            for (int n=0;n<result.size();n++) {
                System.out.println(result.get(n).getModel());
                PreparedStatement preState = c.prepareStatement("INSERT INTO MODELS VALUES (?,?,?,?,?)");

                preState.setInt(1,n+1);
                preState.setString(2,result.get(n).getMaker());
                preState.setString(3,result.get(n).getModel());
                preState.setString(4,result.get(n).getAddress());
                preState.setString(5,result.get(n).getImage_address());

                preState.executeUpdate();
            }*/

            stmt.close();
            c.close();

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Table created successfully");

    }


    public static void htmlParser(MakersModel makersModels) {


        JsonModel modelModel;
        int counter = 0;
        ArrayList<JsonModel> arrayList = new ArrayList<>();

        htmlDocument = Jsoup.parse(urlConnection(makersModels.getAddress()), "http://m.gsmarena.com/");
        Elements links = htmlDocument.select("div[class=general-menu]");

        String pages = htmlDocument.select("div[class=nav-current]").select("span[class=left]").text();
        int page = Integer.parseInt(pages.substring(5, pages.length()));
        System.out.println(page);

        for (int s = 1; s < page + 1; s++) {
            String nextPageLink = htmlDocument.select("div[class=nav-current]").select("a").get(1).absUrl("href");
            System.out.println(" ---- " + nextPageLink);
            System.out.println(htmlDocument.select("div[class=st-text]").select("td"));
            for (int h = 0; h < links.select("a").size(); h++) {
                modelModel = new JsonModel();
                String name = links.select("a").get(h).text();
                String urlAddress = links.select("a").get(h).absUrl("href");
                String image_url = links.select("a").get(h).select("img").get(0).absUrl("src");
                System.out.println(image_url);

                modelModel.setId(h);
                modelModel.setMaker(makersModels.getName());
                modelModel.setModel(name);
                modelModel.setAddress(urlAddress);
                modelModel.setImage_ADDRESS(image_url);

                arrayList.add(h, modelModel);

                System.out.println(links.select("a").get(h).text());
                System.out.println(urlAddress + "  " + links.select("a").get(h).text());

                counter++;
            }


            htmlDocument = Jsoup.parse(urlConnection(nextPageLink), "http://m.gsmarena.com/");
            links = htmlDocument.select("div[class=general-menu]");
        }

        ObjectMapper objectMapper =  new ObjectMapper();
        try {
            objectMapper.writeValue(new File("d:\\mobinfoObject.json"),arrayList);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    private static String urlConnection(String address) {
        try {
            URL url = new URL(address);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(10000);
            InputStreamReader inputStreamReader = new InputStreamReader(connection.getInputStream());
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String i;
            StringBuilder builder = new StringBuilder();
            while ((i = reader.readLine()) != null) {
                builder.append(i);
            }
            return builder.toString();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}