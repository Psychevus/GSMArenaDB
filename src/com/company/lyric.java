package com.company;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by Mojta on 3/16/2016.
 */
public class lyric {

    private static Document htmlDocument;
    public static void main(String[] args) {

        try {
            htmlDocument = Jsoup.connect("http://genius.com/Lamb-of-god-512-annotated")
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64; rv:47.0) Gecko/20100101 Firefox/47.0")
                    .get();

            Elements links = htmlDocument.select("lyrics[class=lyrics]");
            System.out.println(links.text());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
