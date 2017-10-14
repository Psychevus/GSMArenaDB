package com.company;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Arrays;

/**
 * Created by Mojta on 2/22/2016.
 */
public class PriceModel {

    private static Document htmlDocument;

    public static void main(String[] args) {
        getModel("galaxy s6");
    }


    private static String getModel(String model){
        String[] arrayPrice;
        int finalPrice;
        try {
            htmlDocument = Jsoup.connect("http://www.mobile.ir/phones/search.aspx?terms="+model+"&submit=")
                    .get();
            if (htmlDocument.select("div[class=breadcrumb]").select("li").size()==3){
                Elements links = htmlDocument.select("div[class=padd phones phonesgrid]").select("div[class=phone Available]");
                if (links.size()!=0) {
                    System.out.println(links.get(0).select("a").get(0).absUrl("href"));
                    htmlDocument = Jsoup.connect(links.get(0).select("a").get(0).absUrl("href"))
                            .get();
                    Elements links1 = htmlDocument.select("div[class=phoneheader]").select("div[class=info]").select("li");
                    if (links1.size()!=1) {
                        String priceText = links1.get(1).text();
                        arrayPrice = priceText.substring(priceText.indexOf("از ")+3,priceText.length()-6).replace("تا ","").replace(",","").split("\\s+");
                        finalPrice = (Integer.parseInt(arrayPrice[0]) + Integer.parseInt(arrayPrice[1]))/2;
                        System.out.print("Price: ");
                        System.out.println(finalPrice);
                    }
                }
            }else if (htmlDocument.select("div[class=breadcrumb]").select("li").size()==4){
                Elements links = htmlDocument.select("div[class=phoneheader]").select("div[class=info]").select("li");
                if (links.size()!=0) {
                    String priceText = links.get(1).text();
                    arrayPrice = priceText.substring(priceText.indexOf("از ")+3,priceText.length()-6).replace("تا ","").replace(",","").split("\\s+");
                    finalPrice = (Integer.parseInt(arrayPrice[0]) + Integer.parseInt(arrayPrice[1]))/2;
                    System.out.print("Price: ");
                    System.out.println(finalPrice);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return model;
    }


}
