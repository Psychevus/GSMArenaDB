package com.company;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

/**
 * Created by mojtaba.zaferanloo on 1/23/2016.
 */
public class Methodes {

    private static Document htmlDocument;
    private Statement stmt = null;
    private Connection c = null;


    public DetailModel getPhone(String phone_name, String phone_maker){
        DetailModel detailModel;
        ArrayList<ModelModel> modelModelArrayList = new ArrayList<>();
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:MobileInfo.db");
            System.out.println("Opened database successfully");
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT * FROM MODELS" );
            while ( rs.next() ) {
                String  address = rs.getString("address");
                String  maker = rs.getString("maker");
                String  model = rs.getString("model");
                System.out.println(" --------> "+model);

                ModelModel modelModel = new ModelModel();
                modelModel.setAddress(address);
                modelModel.setMaker(maker);
                modelModel.setModel(model);
                if (modelModel.getModel().equalsIgnoreCase(phone_name)){
                    modelModelArrayList.add(modelModel);
                }
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        ArrayList<DetailModel> detailModelArrayList = htmlParser(modelModelArrayList);
        detailModel = detailModelArrayList.get(0);
        return detailModel;
    }



    //add newest models to db
    public void insertPhone(ArrayList<DetailModel> detailModelArrayList){




    }

    public ArrayList<DetailModel> getPhonesFromMaker(String phone_maker){

        return null;
    }

    public ArrayList<DetailModel> searchPhone(String phone_name, String phone_maker){

        return null;
    }

    //save cached image files to object or favorite or ...
    public void updatePhone(DetailModel detailModel){

    }

    //add row to default phone models that named favorite(boolean)
    //and returns phones with true conditions.

    public ArrayList<DetailModel> getPhonesFromFavorite(){

        return null;
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
