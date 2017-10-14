package com.company;

/**
 * Created by Mojtaba on 12/18/2015.
 */
public class ImagesModel {

    public int id;
    public String maker;
    public String model;
    public String address_page;
    public String[] address_images;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getMaker() {
        return maker;
    }

    public void setMaker(String maker) {
        this.maker = maker;
    }

    public String getAddress_page() {
        return address_page;
    }

    public void setAddress_page(String address_page) {
        this.address_page = address_page;
    }

    public String[] getAddress_images() {
        return address_images;
    }

    public void setAddress_images(String[] address_images) {
        this.address_images = address_images;
    }
}