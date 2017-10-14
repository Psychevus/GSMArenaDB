package com.company;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JsonModel {
    public final static String LOCATION = "setting.json";

    @JsonProperty("id")
    private int id;
    @JsonProperty("maker")
    private String maker;
    @JsonProperty("model")
    private String model;
    @JsonProperty("address")
    private String address;
    @JsonProperty("image_ADDRESS")
    private String image_ADDRESS;

    private boolean favorite;

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMaker() {
        return maker;
    }

    public void setMaker(String maker) {
        this.maker = maker;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImage_ADDRESS() {
        return image_ADDRESS;
    }

    public void setImage_ADDRESS(String image_ADDRESS) {
        this.image_ADDRESS = image_ADDRESS;
    }
}
