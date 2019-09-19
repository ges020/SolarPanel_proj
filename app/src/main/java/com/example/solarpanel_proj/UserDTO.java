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
    public String address;
    public String name;
    public String energy;



    public UserDTO(){
        // Default constructor required for calls to DataSnapshot.getValue(UserDTO.class)
    }

    public UserDTO(String id, String password, String email, String phone, String address, String name, String energy) {
        this.id = id;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.address = address;
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
        result.put("address", address);
        result.put("name", name);
        result.put("energy", energy);

        return result;
    }

}
