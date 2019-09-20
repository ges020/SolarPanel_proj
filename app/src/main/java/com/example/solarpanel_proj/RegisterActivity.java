package com.example.solarpanel_proj;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    public static final int sub = 1001; /*다른 액티비티를 띄우기 위한 요청코드(상수)*/

    private static final String TAG = "MainActivity";

    private DatabaseReference mDatabase;

    //view
    String id="";
    String password="";
    String email = "";
    String phone = "";
    String name="";
    String energy = "0";

    Button registerButton;
    TextView cancelButton;

    EditText e_id;
    EditText e_pw;
    EditText e_email;
    EditText e_phone;
    EditText e_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerButton = (Button) findViewById(R.id.registerButton);
        cancelButton = (TextView) findViewById(R.id.cancelButton);

        e_id = (findViewById(R.id.idText));
        e_pw = (findViewById(R.id.passwordText));
        e_email = (findViewById(R.id.emailText));
        e_phone = (findViewById(R.id.phoneText));
        e_name = (findViewById(R.id.nameText));


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                id = e_id.getText().toString();
                password = e_pw.getText().toString();
                email = e_email.getText().toString();
                phone = e_phone.getText().toString();
                name = e_name.getText().toString();

                boolean isOk = emptyCheck();
                if(isOk) {
                    postUserSignUp(isOk);
                    Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                    RegisterActivity.this.startActivity(loginIntent);
                }
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

    public boolean emptyCheck(){
        String msg = "";
        boolean check = true;
        if(phone == ""){
            msg="휴대폰 번호를 입력해주세요";
            check=false;
        }
        if(name == ""){
            msg = "이름을 입력해 주세요";
            check=false;
        }
        if(email == ""){
            msg="이메일을 입력해주세요";
            check=false;
        }
        if(password==""){
            msg="비밀번호를 입력해주세요";
            check=false;
        }
        if(id==""){
            msg="아이디를 입력해주세요";
            check=false;
        }

        Toast toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
        toast.show();
        return check;
    }

    //회원가입
    public void postUserSignUp(boolean add){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> postValues = null;
        if(add){
            UserDTO post = new UserDTO(id,password,email,phone, name,energy);
            postValues = post.toMap();
        }
        childUpdates.put("/id_list/" + id, postValues);
        mDatabase.updateChildren(childUpdates);
    }
}
