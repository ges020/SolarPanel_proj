package com.example.solarpanel_proj;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class GeneratorDTO {

    public String id;
    public String energy;
    public String date;


    public GeneratorDTO(){

    }

    public GeneratorDTO(String id, String energy, String date) {
        this.id = id;
        this.energy = energy;
        this.date = date;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("energy", energy);
        result.put("date",date);

        return result;
    }
}
