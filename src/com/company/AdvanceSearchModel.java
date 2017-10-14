package com.company;

import java.util.ArrayList;

/**
 * Created by Mojta on 2/23/2016.
 */
public class AdvanceSearchModel {

    private String nPriceMin;
    private String nPriceMax;
    private String nWidthMin;
    private String nWidthMax;
    private String nCPUCoresMin;
    private String nCPUCoresMax;
    private String nRamMin;
    private String nRamMax;
    private String fDisplayInchesMin;
    private String fDisplayInchesMax;
    private String nCamPrimMin;
    private String nCamPrimMax;
    private ArrayList<String> sMakers;
    private ArrayList<String> sAvailabilities;
    private ArrayList<String> sNumberSIMs;
    private ArrayList<String> sOSes;


    public String getnPriceMin() {
        return nPriceMin;
    }

    public void setnPriceMin(String nPriceMin) {
        this.nPriceMin = nPriceMin;
    }

    public String getnPriceMax() {
        return nPriceMax;
    }

    public void setnPriceMax(String nPriceMax) {
        this.nPriceMax = nPriceMax;
    }

    public String getnWidthMin() {
        return nWidthMin;
    }

    public void setnWidthMin(String nWidthMin) {
        this.nWidthMin = nWidthMin;
    }

    public String getnWidthMax() {
        return nWidthMax;
    }

    public void setnWidthMax(String nWidthMax) {
        this.nWidthMax = nWidthMax;
    }

    public String getnCPUCoresMin() {
        return nCPUCoresMin;
    }

    public void setnCPUCoresMin(String nCPUCoresMin) {
        this.nCPUCoresMin = nCPUCoresMin;
    }

    public String getnCPUCoresMax() {
        return nCPUCoresMax;
    }

    public void setnCPUCoresMax(String nCPUCoresMax) {
        this.nCPUCoresMax = nCPUCoresMax;
    }

    public String getnRamMin() {
        return nRamMin;
    }

    public void setnRamMin(String nRamMin) {
        this.nRamMin = nRamMin;
    }

    public String getnRamMax() {
        return nRamMax;
    }

    public void setnRamMax(String nRamMax) {
        this.nRamMax = nRamMax;
    }

    public String getfDisplayInchesMin() {
        return fDisplayInchesMin;
    }

    public void setfDisplayInchesMin(String fDisplayInchesMin) {
        this.fDisplayInchesMin = fDisplayInchesMin;
    }

    public String getfDisplayInchesMax() {
        return fDisplayInchesMax;
    }

    public void setfDisplayInchesMax(String fDisplayInchesMax) {
        this.fDisplayInchesMax = fDisplayInchesMax;
    }

    public String getnCamPrimMin() {
        return nCamPrimMin;
    }

    public void setnCamPrimMin(String nCamPrimMin) {
        this.nCamPrimMin = nCamPrimMin;
    }

    public String getnCamPrimMax() {
        return nCamPrimMax;
    }

    public void setnCamPrimMax(String nCamPrimMax) {
        this.nCamPrimMax = nCamPrimMax;
    }

    public ArrayList<String> getsMakers() {
        return sMakers;
    }

    public void setsMakers(ArrayList<String> sMakers) {
        this.sMakers = sMakers;
    }

    public ArrayList<String> getsAvailabilities() {
        return sAvailabilities;
    }

    public void setsAvailabilities(ArrayList<String> sAvailabilities) {
        this.sAvailabilities = sAvailabilities;
    }

    public ArrayList<String> getsNumberSIMs() {
        return sNumberSIMs;
    }

    public void setsNumberSIMs(ArrayList<String> sNumberSIMs) {
        this.sNumberSIMs = sNumberSIMs;
    }

    public ArrayList<String> getsOSes() {
        return sOSes;
    }

    public void setsOSes(ArrayList<String> sOSes) {
        this.sOSes = sOSes;
    }
}
