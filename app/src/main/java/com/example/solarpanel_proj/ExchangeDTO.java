package com.example.solarpanel_proj;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class ExchangeDTO {


    public String sender;
    public String receiever;
    public String energy;




    public ExchangeDTO(){

    }

    public ExchangeDTO(String sender, String receiever, String energy) {
        this.sender = sender;
        this.receiever = receiever;
        this.energy = energy;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("sender", sender);
        result.put("receiever",receiever);
        result.put("energy", energy);
        return result;
    }

}
