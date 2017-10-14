package com.company;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Pattern;

/**
 * Created by Mojta on 2/20/2016.
 */
public class UpdateDBTest {


    public static void main(String[] args) {

        System.out.println("Time before "+ String.valueOf(Calendar.getInstance().get(Calendar.SECOND)));

        updateCheck();
       // getUpdateHttp();
    }


    public static ArrayList<String> updateCheck() {

        Document htmlDocument;
        System.out.println("Parsers :: updateCheck()");
        try {
                htmlDocument = Jsoup.connect("http://www.gsmarena.com/makers.php3")
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64; rv:46.0) Gecko/20100101 Firefox/46.0")
                        .get();

            Elements links = htmlDocument.select("div[class=st-text]");
            System.out.println(links.select("td").size());
            /*String[] strings = new String[2];
            String[] strings2 = new String[2];
            */
            String address;
            String name;
            String[] split;
            for (int h = 1; h < links.select("td").size(); h++) {
                name = links.select("td").get(h).text();
                address = links.select("a").get(h).absUrl("href");
                name = name.replace(" phones ", "");
                name = name.replace(")", "");
                System.out.println(name);

                split = name.split((Pattern.quote("(")));

                System.out.println(split[0]);
                //System.out.println(split[1]);
                System.out.println(address);
                //System.out.println(split[0]+" "+split[1]+" "+address);

               /* MakersModel makerModel = new MakersModel();
                makerModel.setId(h);
                makerModel.setName(split[0]);
                makerModel.setNumber(Integer.parseInt(split[1]));
                makerModel.setAddress(address);*//*




                // TODO clean old json and append new data in json file
                *//*strings = name.split("\\(");
                if (strings.length == 2) {
                    strings2[0] = strings[1].replace(")", "");
                    strings2[1] = strings[0].replace(" phones ", "");
                    System.out.println(strings2[0]);
                    System.out.println("Parsers :: updateCheck() ---> " + strings2[1] + strings2[0]);

                    makerModel.add(strings2[1] + "," + strings2[0]);
                }*//*
            }
//            return makerModel;
*/
        }
        //System.out.println(links.text());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
/*

    private static void getUpdateHttp(){

        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://www.gsmarena.com/makers.php3", new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                // called when response HTTP status is "200 OK"
                // Log.d("YAYYY", "onResponse() returned: " + new String(response));
                System.out.println(new String(response));
                System.out.println("Time after "+ String.valueOf(Calendar.getInstance().get(Calendar.SECOND)));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });


    }*/
}
