package com.example.solarpanel_proj;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class GeneratorActivity extends AppCompatActivity {
    public static final int sub = 1001; /*다른 액티비티를 띄우기 위한 요청코드(상수)*/

    private DatabaseReference mDatabase;

    //view
    String id="";

    String userEnergy="";

    String changeEnergy="";

    ImageView generatorMenuBTN;
    ImageView exchangeMenuBTN;
    ImageView recordMenuBTN;
    ImageView setMenuBTN;

    ListView listview;

    ArrayAdapter<String> arrayAdapter;

    static ArrayList<String> arrayIndex =  new ArrayList<String>();
    static ArrayList<String> arrayData = new ArrayList<String>();


    UserDTO userDTO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generator);

        postGeneratorEnergy("id1","222");

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1) ;
        listview = (ListView) findViewById(R.id.listview) ;
        listview.setAdapter(arrayAdapter) ;


        generatorMenuBTN = (findViewById(R.id.menu_pic1));
        exchangeMenuBTN = (findViewById(R.id.menu_pic2));
        recordMenuBTN = (findViewById(R.id.menu_pic3));
        setMenuBTN = (findViewById(R.id.menu_pic4));

        generatorMenuBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GeneratorActivity.class);
                startActivityForResult(intent, sub);//액티비티 띄우기
            }
        });
        exchangeMenuBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ExchangeListActivity.class);
                startActivityForResult(intent, sub);//액티비티 띄우기
            }
        });
        recordMenuBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ExchangeRecordActivity.class);
                startActivityForResult(intent, sub);//액티비티 띄우기
            }
        });
        setMenuBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ExchangeRecordActivity.class);
                startActivityForResult(intent, sub);//액티비티 띄우기
            }
        });
    }



    //현재 누적 발전 에너지 변경, 저장
    public void postGeneratorEnergy(String gid, String gchangeEnergy){
        id = gid;
        changeEnergy = gchangeEnergy;

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("/id_list/"+id+"/energy").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Date date = new Date();
                Date newDate = new Date(date.getTime());
                SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
                String stringdate = dt.format(newDate);

                Log.d("Date","현재시각"+stringdate);

                postGeneratorEnergyRecord(id, changeEnergy, stringdate);

                Map<String, Object> childUpdates = new HashMap<>();

                userEnergy = snapshot.getValue().toString();

                String tmpEnergy="";

                tmpEnergy = String.valueOf(Integer.parseInt(userEnergy) + Integer.parseInt(changeEnergy));
                childUpdates.put("/id_list/" + id+"/energy", tmpEnergy );

                mDatabase.updateChildren(childUpdates);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("getUsers", databaseError.toException().toString());
                // ...
            }
        });
    }

    // 발전 전력량 저장
    public void postGeneratorEnergyRecord(String gid, String genergy, String gdate){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> postValues = null;

        id = gid;

        GeneratorDTO post = new GeneratorDTO(id, genergy,gdate);
        postValues = post.toMap();

        mDatabase.child("generator_record").push().setValue(postValues);
    }

    //회원 정보
    public void getUserInfo(String id){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("/id_list/"+id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                userDTO = snapshot.getValue(UserDTO.class);
                Log.d("getUsersDTO", userDTO.password);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("getUsers", databaseError.toException().toString());
                // ...
            }
        });
    }
}
