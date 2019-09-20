package com.example.solarpanel_proj;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class UserDTO {

    public String id;
    public String password;
    public String email;
    public String phone;
    public String name;
    public String energy;



    public UserDTO(){
        //this.getClass();
    }

    public UserDTO(String id, String password, String email, String phone, String name, String energy) {
        this.id = id;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.name = name;
        this.energy = energy;


    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("password",password);
        result.put("email", email);
        result.put("phone", phone);
        result.put("name", name);
        result.put("energy", energy);

        return result;
    }

}
