package com.company;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class DBDetailTimeOut {

    public static Document htmlDocument;
    public static String htmlPageUrl = "http://m.gsmarena.com/acer_jade_primo-7650.php";

    public static void main( String args[] ) {
        Connection c = null;
        Statement stmt = null;

        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:MobileInfo.db");
            System.out.println("Opened database successfully");
            stmt = c.createStatement();

            //ResultSet rs = stmt.executeQuery( "SELECT * FROM DETAILS WHERE NETWORK_Technology IS NULL" );
            ResultSet rs = stmt.executeQuery( "SELECT * FROM DETAILS WHERE BATTERY_MusicPlay IS NULL " );
            ArrayList<DetailModel> modelDetailArrayList = new ArrayList<>();
            while ( rs.next() ) {
                String  address = rs.getString("address");
                String  maker = rs.getString("maker");
                String  model = rs.getString("model");
                System.out.println(" --------> "+model);

                DetailModel detailModel = new DetailModel();
                detailModel.setAddress(address);
                detailModel.setMaker(maker);
                detailModel.setModel(model);
                modelDetailArrayList.add(detailModel);

            }
            rs.close();

            ArrayList<DetailModel> result = htmlParser(modelDetailArrayList);



            System.out.println("********************** "+result.size()+" **********************");
            for (int n=0;n<result.size();n++) {
                System.out.println(result.get(n).getModel());
                PreparedStatement preState = c.prepareStatement("UPDATE DETAILS SET " +
                        "BATTERY_MusicPlay = ? WHERE model = ?");
                //preState.setInt(1,n+1);
                preState.setString(1,result.get(n).getBATTERY_MusicPlay());

                preState.setString(2,result.get(n).getModel());

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


    public static ArrayList<DetailModel> htmlParser(ArrayList<DetailModel> modelModels){

        DetailModel detailModel;
        ArrayList<DetailModel> arrayList = new ArrayList<>();
        for (int y=0;y<modelModels.size();y++) {
            try {
                htmlDocument = Jsoup.connect(modelModels.get(y).getAddress()).get();
                Elements links = htmlDocument.select("div[class=grid_12]").select("table");
                detailModel = new DetailModel();
                detailModel.setAddress(modelModels.get(y).getAddress());
                detailModel.setModel(modelModels.get(y).getModel());
                detailModel.setMaker(modelModels.get(y).getMaker());

                for (Element table:links) {
                    if (table.select("th[scope=col]").text().equals("Features")){
                        detailModel.setFEATURES_(table.select("td[class=nfo]").get(table.select("td[class=nfo]").size()-1).text());}
                    if (table.select("th[scope=col]").text().equals("Battery")){
                        detailModel.setBATTERY_(table.select("td[class=nfo]").get(0).text());}
                    for (Element tr:table.select("tr")) {
                        if (tr.select("td[class=ttl]").text().equals("Music play")){
                            detailModel.setBATTERY_MusicPlay(tr.select("td[class=nfo]").text());
                        }
                        //System.out.println(tr.select("td[class=ttl]").text()+" : "+tr.select("td[class=nfo]").text());
                        System.out.println();
                    }
                }
                arrayList.add(detailModel);
            } catch (IOException e) {
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
            }
        }
        return arrayList;
    }

}