package com.example.solarpanel_proj;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class ExchangeRecordDTO {


    public String sender;
    public String receiver;
    public String energy;
    public String money;



    public ExchangeRecordDTO(){

    }

    public ExchangeRecordDTO(String sender, String receiver, String energy,String money) {
        this.sender = sender;
        this.receiver = receiver;
        this.energy = energy;
        this.money = money;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("sender", sender);
        result.put("receiver",receiver);
        result.put("energy", energy);
        result.put("money",money);
        return result;
    }

}
