package com.company;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

/**
 * Created by mojtaba.zaferanloo on 2/3/2016.
 */
public class UpdateDB {

    public static Document htmlDocument;
    public static String htmlPageUrl = "http://www.gsmarena.com/acer-phones-59.php";
    public static String htmlContentInStringFormat;


    public static void main( String args[] ) {
        Connection c = null;
        Statement stmt = null;

        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:MobileInfo.db");
            System.out.println("Opened database successfully");
            stmt = c.createStatement();
            ArrayList<MakersModel> makersModelArrayList = new ArrayList<>();
            ResultSet rs = stmt.executeQuery( "SELECT * FROM MAKERS" );
            HashMap hashMap = new HashMap();
            while ( rs.next() ) {
                MakersModel makersModel = new MakersModel();
                makersModel.setName(rs.getString("NAME"));
                makersModel.setNumber(rs.getInt("NUMBER"));
                System.out.println(" --------> "+rs.getString("NAME")+" | "+rs.getInt("NUMBER"));
                makersModelArrayList.add(makersModel);
            }
            rs.close();

            ArrayList<String> result = htmlParser(makersModelArrayList);

            String[] myString;
            System.out.println("here 1");
            int j=0;
            for (MakersModel makersmodel :makersModelArrayList) {
                System.out.println("here 2");
                for (String string:result) {
                    System.out.println(makersmodel.getName());
                    System.out.println(string);
                    myString = string.split(",");
                    if (makersmodel.getName().equals(myString[0])){
                        System.out.println("here 4");
                        makersmodel.setNumber(Integer.parseInt(myString[1]));
                    }
                }
                makersModelArrayList.set(j,makersmodel);
                j++;
            }

            System.out.println("********************** "+result.size()+" **********************");
            for (int n=0;n<result.size();n++) {
                System.out.println(makersModelArrayList.get(n).getName());
                System.out.println(makersModelArrayList.get(n).getNumber());
                PreparedStatement preState = c.prepareStatement("INSERT INTO MAKERS VALUES (?,?,?,?)");
                preState.setString(2,makersModelArrayList.get(n).getName());
                preState.setString(3,makersModelArrayList.get(n).getAddress());
                preState.setInt(4,makersModelArrayList.get(n).getNumber());
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


    public static ArrayList<String> htmlParser(ArrayList<MakersModel> makersModelArrayList){
        try {
            htmlDocument = Jsoup.connect("http://www.gsmarena.com/makers.php3")
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64; rv:46.0) Gecko/20100101 Firefox/46.0")
                    .get();
            htmlContentInStringFormat = "";
            Elements links = htmlDocument.select("div[class=st-text]");
            System.out.println(links.select("td").size());
            String[] strings = new String[2];
            String[] strings2 = new String[2];
            ArrayList<String> makersModel = new ArrayList<>();

            for (int h=0;h<links.select("td").size();h++) {
                String name = links.select("td").get(h).text();

                strings = name.split("\\(");
                if (strings.length==2) {
                    strings2[0] = strings[1].replace(")", "");
                    strings2[1] = strings[0].replace(" phones ","");
                    System.out.println(strings2[0]);
                    makersModel.add(strings2[1]+","+strings2[0]);
                }
            }
            return makersModel;

            //System.out.println(links.text());
        } catch (IOException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        return null;
    }

}