package com.company;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Mojta on 2/23/2016.
 */
public class AdvanceSearch {


    public static void main(String[] args) {

        AdvanceSearchModel advanceSearchModel = new AdvanceSearchModel();
        advanceSearchModel.setnCPUCoresMax("2");
        advanceSearchModel.setnCamPrimMin("15");
        getAdvanceSearch(advanceSearchModel);

    }


    private static ArrayList<String> getAdvanceSearch(AdvanceSearchModel advanceSearchModel) {
         Document htmlDocument;

        ArrayList<String> stringArrayList = new ArrayList<>();
        String targetUrl = "http://www.gsmarena.com/results.php3?" +
                "nPriceMin=" + advanceSearchModel.getnPriceMin() +
                "&nPriceMax=" + advanceSearchModel.getnCamPrimMax() +
                "&nWidthMin=" + advanceSearchModel.getnWidthMin() +
                "&nWidthMax=" + advanceSearchModel.getnWidthMax() +
                "&nCPUCoresMin=" + advanceSearchModel.getnCPUCoresMin() +
                "&nCPUCoresMax=" + advanceSearchModel.getnCPUCoresMax() +
                "&nRamMin=" + advanceSearchModel.getnRamMin() +
                "&nRamMax=" + advanceSearchModel.getnRamMax() +
                "&fDisplayInchesMin=" + advanceSearchModel.getfDisplayInchesMin() +
                "&fDisplayInchesMax=" + advanceSearchModel.getfDisplayInchesMax() +
                "&nCamPrimMin=" + advanceSearchModel.getnCamPrimMin() +
                "&nCamPrimMax=" + advanceSearchModel.getnCamPrimMax() +
                "&sMakers=" + advanceSearchModel.getsMakers() +
                "&sAvailabilities=" + advanceSearchModel.getsAvailabilities() +
                "&sNumberSIMs=" + advanceSearchModel.getsNumberSIMs() +
                "&sOSes=" + advanceSearchModel.getsOSes();

        try {
            htmlDocument = Jsoup.connect(targetUrl)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64; rv:46.0) Gecko/20100101 Firefox/46.0")
                    .get();

            Elements links = htmlDocument.select("div[class=makers]");
            String result;
            for (int z = 0; z < links.select("span").size(); z++) {
                result = String.valueOf(links.select("span").get(z)).replaceAll("<br>", "|").replace("<span>","").replace("</span>","");
                stringArrayList.add(result);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringArrayList;
    }
}
