package com.example.solarpanel_proj;

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

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

public class GeneratorRecordActivity extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generator_record);

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1) ;
        listview = (ListView) findViewById(R.id.listview) ;
        listview.setAdapter(arrayAdapter) ;

        getGeneratorRecord("id1");

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

    //발전 에너지 이력 조회
    public void getGeneratorRecord(String userId){

        ValueEventListener postListener = new ValueEventListener() {

            //성공
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ExchangeRecordDTO post = dataSnapshot.getValue(ExchangeRecordDTO.class);
                Log.d("기록", "key: " + dataSnapshot.getChildrenCount());

                arrayData.clear();
                arrayIndex.clear();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    String key = postSnapshot.getKey();
                    GeneratorDTO get = postSnapshot.getValue(GeneratorDTO.class);
                        String[] info = {get.id,get.energy,get.date};
                    arrayIndex.add(key);
                    Log.d("기록", "key: " + key);
                    Log.d("기록", "info: " + info[0] + info[1] + info[2]);

                }
                arrayAdapter.clear();
                arrayAdapter.addAll(arrayIndex);
                arrayAdapter.notifyDataSetChanged();
            }

            //실패
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // ...
            }
        };
        Query sortbyAge = FirebaseDatabase.getInstance().getReference().child("generator_record").orderByChild("id").equalTo(userId);
        sortbyAge.addListenerForSingleValueEvent(postListener);
    }
}
