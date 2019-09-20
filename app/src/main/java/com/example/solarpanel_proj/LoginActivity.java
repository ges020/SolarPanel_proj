package com.example.solarpanel_proj;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    public static final int sub = 1001; /*다른 액티비티를 띄우기 위한 요청코드(상수)*/

    private static final String TAG = "MainActivity";

    private DatabaseReference mDatabase;

    //test
    static final String[] LIST_MENU = {"LIST1", "LIST2", "LIST3"} ;

    //view
    String id="id3";
    String password="id3";
    String email = "email2@email";
    String phone = "01030304040";
    String address= "add2";
    String name="name2";
    String energy = "3334";

    ArrayAdapter<String> arrayAdapter;

    static ArrayList<String> arrayIndex =  new ArrayList<String>();
    static ArrayList<String> arrayData = new ArrayList<String>();

    ListView listview;

    //user
    UserDTO userDTO;
    String userEnergy="-1";

    //exchange
    ExchangeRecordDTO exchangeDTO;

    String changeEnergy="";
    String userId="";


    String sender="";
    String receiver="";

    // 로그인 성공 시 sharedpreference에 값 저장해 자동 로그인이 가능하게 함.
    // 로그아웃 구현하기
    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void userSignIn(String sid, String spw){
        Log.d("userSignIn", "userSignIn: ");

        id = sid;
        password = spw;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("/id_list/"+sid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                userDTO = snapshot.getValue(UserDTO.class);
                Log.d("userInfo", "userInfo: " + userDTO);

                try {
                    if (password.equals(userDTO.password)) {
                        Log.d("login", "success ");
                        Toast toast = Toast.makeText(getApplicationContext(), "로그인 성공", Toast.LENGTH_SHORT);
                        toast.show();
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivityForResult(intent, sub);//액티비티 띄우기

                    } else {
                        Log.d("login", "fail ");
                        Toast toast = Toast.makeText(getApplicationContext(), "아이디나 비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }catch(Exception e){
                    Toast toast = Toast.makeText(getApplicationContext(), "존재하지 않는 아이디입니다", Toast.LENGTH_SHORT);
                    toast.show();

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("getUsers", databaseError.toException().toString());
                // ...
            }
        });

    }
}
