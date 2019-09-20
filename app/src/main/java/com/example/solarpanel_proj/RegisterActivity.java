package com.example.solarpanel_proj;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    public static final int sub = 1001; /*다른 액티비티를 띄우기 위한 요청코드(상수)*/

    private static final String TAG = "MainActivity";

    private DatabaseReference mDatabase;

    //view
    String id="id3";
    String password="id3";
    String email = "email2@email";
    String phone = "01030304040";
    String address= "add2";
    String name="name2";
    String energy = "3334";

    Button registerButton = (Button) findViewById(R.id.registerButton);
    TextView cancelButton = (TextView) findViewById(R.id.cancelButton);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                RegisterActivity.this.startActivity(loginIntent);
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                RegisterActivity.this.startActivity(loginIntent);
            }
        });
    }

    //회원가입
    public void postUserSignUp(boolean add){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> postValues = null;
        if(add){
            UserDTO post = new UserDTO(id,password,email,phone,address, name,energy);
            postValues = post.toMap();
        }
        childUpdates.put("/id_list/" + id, postValues);
        mDatabase.updateChildren(childUpdates);
    }
}
