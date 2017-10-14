package com.company;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

public class DBImages {

    public static Document htmlDocument;
    public static Document imagePage;
    public static String htmlPageUrl = "http://m.gsmarena.com/acer_jade_primo-7650.php";

    public static void main( String args[] ) {
        Connection c = null;
        Statement stmt = null;

        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:MobileInfo.db");
            System.out.println("Opened database successfully");
            stmt = c.createStatement();
            /*String SQL =
                    "CREATE TABLE IMAGES " +
                            "(ID                    INT PRIMARY KEY NOT NULL," +
                            " MAKER                           TEXT," +
                            " MODEL                           TEXT," +
                            " ADDRESS_PAGE                    TEXT," +
                            " ADDRESS_IMAGES                  TEXT)";
            stmt.executeUpdate(SQL);
            System.out.println("TABLE created successfully");
            */
            ResultSet rs = stmt.executeQuery( "SELECT * FROM MODELS WHERE maker='iNQ'" );
            ArrayList<ModelModel> modelModelArrayList = new ArrayList<>();
            while ( rs.next() ) {
                String  address = rs.getString("address");
                String  maker = rs.getString("maker");
                String  model = rs.getString("model");
                System.out.println(" --------> "+model);

                ModelModel modelModel = new ModelModel();
                modelModel.setAddress(address);
                modelModel.setMaker(maker);
                modelModel.setModel(model);
                modelModelArrayList.add(modelModel);
            }
            rs.close();

            ArrayList<ImagesModel> result = htmlParser(modelModelArrayList);

            System.out.println("********************** "+result.size()+" **********************");
            for (int n=0;n<result.size();n++) {
                System.out.println(result.get(n).getModel());
                PreparedStatement preState = c.prepareStatement("INSERT INTO IMAGES VALUES (" +
                        "?,?,?,?,?)");
                preState.setInt(1,n+1);
                preState.setString(2,result.get(n).getMaker());
                preState.setString(3,result.get(n).getModel());
                preState.setString(4,result.get(n).getAddress_page());
                preState.setString(5,Arrays.toString(result.get(n).getAddress_images()));
                preState.executeUpdate();
            }
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Table created successfully");

    }


    public static ArrayList<ImagesModel> htmlParser(ArrayList<ModelModel> modelModels){

        ImagesModel imagesModel;
        ArrayList<ImagesModel> arrayList = new ArrayList<>();
        for (int y=0;y<modelModels.size();y++) {
            try {
                htmlDocument = Jsoup.connect(modelModels.get(y).getAddress()).get();
                Elements links = htmlDocument.select("li[id=specs-cp-pics]");
                imagesModel = new ImagesModel();
                imagesModel.setModel(modelModels.get(y).getModel());
                imagesModel.setMaker(modelModels.get(y).getMaker());
                String[] imageLinksArray = null;
                if (links.text().equals("Pictures")) {
                    String page = links.select("a").get(0).absUrl("href");
                    imagesModel.setAddress_page(page);
                    imagePage = Jsoup.connect(page).get();
                    Elements imageLinks = imagePage.select("div[id=pictures]");

                    System.out.println(imageLinks.select("img").size());
                    imageLinksArray = new String[imageLinks.select("img").size()+1];

                    for (int h=0;h<imageLinks.select("img").size();h++) {
                        imageLinksArray[h] = imageLinks.select("img").get(h).absUrl("src");
                        System.out.println(imageLinksArray[h]+"  "+imageLinks.select("img").get(h).text());
                    }
                    Elements linksoriginal = htmlDocument.select("div[class=left grid_6 specs-cp-pic-rating]");
                    imageLinksArray[imageLinks.select("img").size()] = linksoriginal.select("img").get(0).absUrl("src");

                }else {
                    imageLinksArray = new String[1];
                    Elements linksoriginal = htmlDocument.select("div[class=left grid_6 specs-cp-pic-rating]");
                    imageLinksArray[0] = linksoriginal.select("img").get(0).absUrl("src");
                }
                System.out.println(Arrays.toString(imageLinksArray));
                imagesModel.setAddress_images(imageLinksArray);
                arrayList.add(imagesModel);
            } catch (IOException e) {
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
            }

        }
        return arrayList;
    }

}