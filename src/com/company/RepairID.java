package com.company;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Mojta on 3/9/2016.
 */
public class RepairID {

    public static void main(String[] args) {

        int counter = 8000;

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            JsonModel[] jsons = mapper.readValue(new File("D:\\json_shit2.json"), JsonModel[].class);
            ArrayList<JsonModel> jsonModels = new ArrayList<>(Arrays.asList(jsons));
            System.out.println("**** " +  jsonModels.size());

            for (JsonModel m : jsonModels){
                m.setId(counter--);
                System.out.println(m.getId());
            }

            Gson gson = new Gson();
            FileWriter writer = new FileWriter(new File("D:\\json_shit69.json"));
            writer.write(gson.toJson(jsonModels));
            writer.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
