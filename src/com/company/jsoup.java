package com.company;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Mojta on 3/8/2016.
 */
public class jsoup {


    public static Document htmlDocument;
    public static String htmlPageUrl = "http://afroid.ir:";
    public static ArrayList<String> arrayList;

    public static void main(String[] args) {

        for (int u = 1000;u<3000;u++) {
            Connection.Response response = null;
            htmlPageUrl += u;
            try {
                System.out.println(u);
                response = Jsoup.connect(htmlPageUrl)
                        .userAgent("Mozilla/5.0 (Windows NT 6.0) AppleWebKit/536.5 (KHTML, like Gecko) Chrome/19.0.1084.46 Safari/536.5")
                        .ignoreHttpErrors(true)
                        .execute();

                arrayList.add(htmlPageUrl);
                System.out.println(htmlPageUrl);
            } catch (IOException e) {
                /*System.out.println(u);
                System.out.println("io - " + e);*/
            }
            htmlPageUrl = "http://afroid.ir:";
        }

    }

}
