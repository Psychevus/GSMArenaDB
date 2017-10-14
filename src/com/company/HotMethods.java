package com.company;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

/**
 * Created by mojtaba.zaferanloo on 2/1/2016.
 */
public class HotMethods {

    public static Document htmlDocument;
    public static String htmlPageUrl = "http://www.gsmarena.com/acer-phones-59.php";


    public static void main( String args[] ) {
        Connection c = null;
        Statement stmt = null;

        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:MobileInfo.db");
            System.out.println("Opened database successfully");
            stmt = c.createStatement();
            /*String SQL =
                    "CREATE TABLE HOTTEST " +
                    "(ID                        INT PRIMARY KEY NOT NULL," +
                    " MAKER                     TEXT," +
                    " MODEL                     TEXT," +
                    " ADDRESS                   TEXT," +
                    " IMAGE_ADDRESS             TEXT," +
                    " TYPE                      TEXT)";
            stmt.executeUpdate(SQL);
            System.out.println("DB created successfully");
*/
            ResultSet rs = stmt.executeQuery( "SELECT * FROM MAKERS;" );
            ArrayList<MakersModel> makersModelArrayList = new ArrayList<>();
            while ( rs.next() ) {
                String  address = rs.getString("address");
                String  maker = rs.getString("name");
                System.out.println(" --------> "+maker);
                MakersModel makersModel = new MakersModel();
                makersModel.setAddress(address);
                makersModel.setName(maker);
                makersModelArrayList.add(makersModel);
            }
            rs.close();

            ArrayList<ModelModel> result = htmlParser(makersModelArrayList);
/*

            System.out.println("********************** "+result.size()+" **********************");
            for (int n=0;n<result.size();n++) {
                System.out.println(result.get(n).getModel());
                PreparedStatement preState = c.prepareStatement("INSERT INTO HOTTEST VALUES (?,?,?,?,?,?)");

                preState.setInt(1,n+1);
                preState.setString(2,result.get(n).getMaker());
                preState.setString(3,result.get(n).getModel());
                preState.setString(4,result.get(n).getAddress());
                preState.setString(5,result.get(n).getImage_address());
                preState.setString(6,"LATEST");

                preState.executeUpdate();
            }
*/

            stmt.close();
            c.close();

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Table created successfully");

    }


    public static ArrayList<ModelModel> htmlParser(ArrayList<MakersModel> makersModels){
        ArrayList<ModelModel> arrayList = new ArrayList<>();
        try {
            htmlDocument = Jsoup.connect("http://www.gsmarena.com/")
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64; rv:46.0) Gecko/20100101 Firefox/46.0")
                    .get();
            Elements links = htmlDocument.select("div[class=module module-rankings s3]");
            ArrayList<String> addressLatest = new ArrayList<>();

            for (int h = 0; h < links.get(1).select("a").size(); h++) {

                System.out.println(links.get(1).select("a").text());
                System.out.println(links.get(1).select("a").get(h).absUrl("href"));
                addressLatest.add(links.get(1).select("a").get(h).absUrl("href"));
            }
        } catch (IOException e) {
            System.err.println(e.getClass().getName() + ": " + e.getLocalizedMessage() +" : "+ e.getMessage());
        }
        return arrayList;
    }

}