package com.company;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MakerModel {

    @JsonProperty("ID")
    private int id;
    @JsonProperty("NAME")
    private String name;
    @JsonProperty("ADDRESS")
    private String address;
    @JsonProperty("NUMBER")
    private int number;
    @JsonProperty("IMAGE_ADDRESS")
    private String image_address;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getImage_address() {
        return image_address;
    }

    public void setImage_address(String image_address) {
        this.image_address = image_address;
    }
}
