package com.company;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Mojta on 2/20/2016.
 */
public class ImageArray {

    private static Document htmlDocument;

    public static void main(String[] args) {
        getImages("http://www.gsmarena.com/alcatel_idol_4s-7896.php");
    }


    private static ArrayList<String> getImages(String url){
        ArrayList<String> picturesArray = new ArrayList<>();
        try {
            String picturesUrl =  url.replace(url.substring(24, url.indexOf("-")),"gsmarena-pictures");
            htmlDocument = Jsoup.connect(picturesUrl)
                    .get();
            Elements links = htmlDocument.select("div[id=pictures]").select("p[align=center]");
            for (int a=0;a<links.size();a++) {
                System.out.println(links.select("img").get(a).absUrl("src"));
                picturesArray.add(links.select("img").get(a).absUrl("src"));
            }
            return picturesArray;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
