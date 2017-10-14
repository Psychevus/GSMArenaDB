package com.company;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class DBModelUpdate {

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

            ResultSet rs = stmt.executeQuery( "SELECT * FROM MODELS" );
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

            ArrayList<DetailModel> result = htmlParser(modelModelArrayList);



            System.out.println("********************** "+result.size()+" **********************");
            for (int n=0;n<result.size();n++) {
                System.out.println(result.get(n).getModel());
                PreparedStatement preState = c.prepareStatement("INSERT INTO DETAILS VALUES (" +
                        "?,?,?,?," +
                        "?,?,?,?,?,?,?," +
                        "?,?," +
                        "?,?,?," +
                        "?,?,?,?,?," +
                        "?,?,?,?," +
                        "?,?," +
                        "?,?,?,?," +
                        "?,?,?," +
                        "?,?,?,?,?," +
                        "?,?,?,?,?," +
                        "?,?,?,?," +
                        "?,?)");
                preState.setInt(1,n+1);
                preState.setString(2,result.get(n).getMaker());
                preState.setString(3,result.get(n).getModel());
                preState.setString(4,result.get(n).getAddress());

                preState.setString(5,result.get(n).getNETWORK_Technology());
                preState.setString(6,result.get(n).getNETWORK_2GBands());
                preState.setString(7,result.get(n).getNETWORK_3GBands());
                preState.setString(8,result.get(n).getNETWORK_4GBands());
                preState.setString(9,result.get(n).getNETWORK_Speed());
                preState.setString(10,result.get(n).getNETWORK_GPRS());
                preState.setString(11,result.get(n).getNETWORK_EDGE());

                preState.setString(12,result.get(n).getLAUNCH_Announced());
                preState.setString(13,result.get(n).getLAUNCH_Status());

                preState.setString(14,result.get(n).getBODY_Dimensions());
                preState.setString(15,result.get(n).getBODY_Weight());
                preState.setString(16,result.get(n).getBODY_SIM());

                preState.setString(17,result.get(n).getDISPLAY_Type());
                preState.setString(18,result.get(n).getDISPLAY_Size());
                preState.setString(19,result.get(n).getDISPLAY_Resolution());
                preState.setString(20,result.get(n).getDISPLAY_Multitouch());
                preState.setString(21,result.get(n).getDISPLAY_Protection());

                preState.setString(22,result.get(n).getPLATFORM_OS());
                preState.setString(23,result.get(n).getPLATFORM_Chipset());
                preState.setString(24,result.get(n).getPLATFORM_CPU());
                preState.setString(25,result.get(n).getPLATFORM_GPU());

                preState.setString(26,result.get(n).getMEMORY_CardSlot());
                preState.setString(27,result.get(n).getMEMORY_Internal());

                preState.setString(28,result.get(n).getCAMERA_Features());
                preState.setString(29,result.get(n).getCAMERA_Primary());
                preState.setString(30,result.get(n).getCAMERA_Video());
                preState.setString(31,result.get(n).getCAMERA_Secondary());

                preState.setString(32,result.get(n).getSOUND_AlertTypes());
                preState.setString(33,result.get(n).getSOUND_Loudspeaker());
                preState.setString(34,result.get(n).getSOUND_35mmjack());

                preState.setString(35,result.get(n).getCOMMS_WLAN());
                preState.setString(36,result.get(n).getCOMMS_Bluetooth());
                preState.setString(37,result.get(n).getCOMMS_GPS());
                preState.setString(38,result.get(n).getCOMMS_Radio());
                preState.setString(39,result.get(n).getCOMMS_USB());

                preState.setString(40,result.get(n).getFEATURES_Sensors());
                preState.setString(41,result.get(n).getFEATURES_Messaging());
                preState.setString(42,result.get(n).getFEATURES_Browser());
                preState.setString(43,result.get(n).getFEATURES_Java());
                preState.setString(44,result.get(n).getFEATURES_());

                preState.setString(45,result.get(n).getBATTERY_());
                preState.setString(46,result.get(n).getBATTERY_StandBy());
                preState.setString(47,result.get(n).getBATTERY_TalkTime());
                preState.setString(48,result.get(n).getBATTERY_MusicPlay());

                preState.setString(49,result.get(n).getMISC_Colors());
                preState.setString(50,result.get(n).getMISC_PriceGroup());

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


    public static ArrayList<DetailModel> htmlParser(ArrayList<ModelModel> modelModels){

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
                        switch (tr.select("td[class=ttl]").text()){
                            case "Technology":
                                detailModel.setNETWORK_Technology(tr.select("td[class=nfo]").text());
                                break;
                            case "2G bands":
                                detailModel.setNETWORK_2GBands(tr.select("td[class=nfo]").text());
                                break;
                            case "3G bands":
                                detailModel.setNETWORK_3GBands(tr.select("td[class=nfo]").text());
                                break;
                            case "4G bands":
                                detailModel.setNETWORK_4GBands(tr.select("td[class=nfo]").text());
                                break;
                            case "Speed":
                                detailModel.setNETWORK_Speed(tr.select("td[class=nfo]").text());
                                break;
                            case "GPRS":
                                detailModel.setNETWORK_GPRS(tr.select("td[class=nfo]").text());
                                break;
                            case "EDGE":
                                detailModel.setNETWORK_EDGE(tr.select("td[class=nfo]").text());
                                break;
                            case "Announced":
                                detailModel.setLAUNCH_Announced(tr.select("td[class=nfo]").text());
                                break;
                            case "Status":
                                detailModel.setLAUNCH_Status(tr.select("td[class=nfo]").text());
                                break;
                            case "Dimensions":
                                detailModel.setBODY_Dimensions(tr.select("td[class=nfo]").text());
                                break;
                            case "Weight":
                                detailModel.setBODY_Weight(tr.select("td[class=nfo]").text());
                                break;
                            case "SIM":
                                detailModel.setBODY_SIM(tr.select("td[class=nfo]").text());
                                break;
                            case "Type":
                                detailModel.setDISPLAY_Type(tr.select("td[class=nfo]").text());
                                break;
                            case "Size":
                                detailModel.setDISPLAY_Size(tr.select("td[class=nfo]").text());
                                break;
                            case "Resolution":
                                detailModel.setDISPLAY_Resolution(tr.select("td[class=nfo]").text());
                                break;
                            case "Multitouch":
                                detailModel.setDISPLAY_Multitouch(tr.select("td[class=nfo]").text());
                                break;
                            case "OS":
                                detailModel.setPLATFORM_OS(tr.select("td[class=nfo]").text());
                                break;
                            case "Chipset":
                                detailModel.setPLATFORM_Chipset(tr.select("td[class=nfo]").text());
                                break;
                            case "CPU":
                                detailModel.setPLATFORM_CPU(tr.select("td[class=nfo]").text());
                                break;
                            case "GPU":
                                detailModel.setPLATFORM_GPU(tr.select("td[class=nfo]").text());
                                break;
                            case "Card slot":
                                detailModel.setMEMORY_CardSlot(tr.select("td[class=nfo]").text());
                                break;
                            case "Internal":
                                detailModel.setMEMORY_Internal(tr.select("td[class=nfo]").text());
                                break;
                            case "Primary":
                                detailModel.setCAMERA_Primary(tr.select("td[class=nfo]").text());
                                break;
                            case "Features":
                                detailModel.setCAMERA_Features(tr.select("td[class=nfo]").text());
                                break;
                            case "Video":
                                detailModel.setCAMERA_Video(tr.select("td[class=nfo]").text());
                                break;
                            case "Secondary":
                                detailModel.setCAMERA_Secondary(tr.select("td[class=nfo]").text());
                                break;
                            case "Alert types":
                                detailModel.setSOUND_AlertTypes(tr.select("td[class=nfo]").text());
                                break;
                            case "Loudspeaker":
                                detailModel.setSOUND_Loudspeaker(tr.select("td[class=nfo]").text());
                                break;
                            case "3.5mm jack":
                                detailModel.setSOUND_35mmjack(tr.select("td[class=nfo]").text());
                                break;

                            case "WLAN":
                                detailModel.setCOMMS_WLAN(tr.select("td[class=nfo]").text());
                                break;
                            case "Bluetooth":
                                detailModel.setCOMMS_Bluetooth(tr.select("td[class=nfo]").text());
                                break;
                            case "GPS":
                                detailModel.setCOMMS_GPS(tr.select("td[class=nfo]").text());
                                break;
                            case "Radio":
                                detailModel.setCOMMS_Radio(tr.select("td[class=nfo]").text());
                                break;
                            case "USB":
                                detailModel.setCOMMS_USB(tr.select("td[class=nfo]").text());
                                break;

                            case "Sensors":
                                detailModel.setFEATURES_Sensors(tr.select("td[class=nfo]").text());
                                break;
                            case "Messaging":
                                detailModel.setFEATURES_Messaging(tr.select("td[class=nfo]").text());
                                break;
                            case "Browser":
                                detailModel.setFEATURES_Browser(tr.select("td[class=nfo]").text());
                                break;
                            case "Java":
                                detailModel.setFEATURES_Java(tr.select("td[class=nfo]").text());
                                break;

                            case "Stand-by":
                                detailModel.setBATTERY_StandBy(tr.select("td[class=nfo]").text());
                                break;
                            case "Talk time":
                                detailModel.setBATTERY_TalkTime(tr.select("td[class=nfo]").text());
                                break;
                            case "Music play":
                                detailModel.setBATTERY_MusicPlay(tr.select("td[class=nfo]").text());
                                break;

                            case "Colors":
                                detailModel.setMISC_Colors(tr.select("td[class=nfo]").text());
                                break;
                            case "Price group":
                                detailModel.setMISC_PriceGroup(tr.select("td[class=nfo]").text());
                                break;

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