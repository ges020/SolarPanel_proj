package com.example.solarpanel_proj;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

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

    TextView todayEnergyText;
    TextView totalEnergyText;
    TextView monthEnergyText;
    TextView profitText;

    ImageView generatorMenuBTN;
    ImageView recordMenuBTN;
    ImageView setMenuBTN;

    ImageView detailBTN;

//    ListView listview;

    ArrayAdapter<String> arrayAdapter;

    static ArrayList<String> arrayIndex =  new ArrayList<String>();
    static ArrayList<String> arrayData = new ArrayList<String>();


    UserDTO userDTO;
    String loginId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generator);

        SharedPreferences sf = getSharedPreferences("userFile",MODE_PRIVATE);
        loginId = sf.getString("loginId","");

        //postGeneratorEnergy("id1","222");

//        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1) ;
//        listview = (ListView) findViewById(R.id.listview) ;
//        listview.setAdapter(arrayAdapter) ;


        todayEnergyText = (findViewById(R.id.todayEnergyText));
        totalEnergyText = (findViewById(R.id.totalEnergyText));
        monthEnergyText = (findViewById(R.id.monthEnergyText));
        profitText = (findViewById(R.id.profitText));

        setUserGenerator();

        generatorMenuBTN = (findViewById(R.id.menu_pic1));
        recordMenuBTN = (findViewById(R.id.menu_pic3));
        setMenuBTN = (findViewById(R.id.menu_pic4));
        detailBTN = (findViewById(R.id.detailBTN));

        generatorMenuBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GeneratorActivity.class);
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
                SharedPreferences sharedPreferences = getSharedPreferences("userFile",MODE_PRIVATE);

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("loginId","");
                editor.commit();

                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivityForResult(intent, sub);//액티비티 띄우기
            }
        });
        detailBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GeneratorRecordActivity.class);
                startActivityForResult(intent, sub);//액티비티 띄우기
            }
        });
    }



    public void setUserGenerator(){
        getEnergyRecordMonth();
        getTodayEnergy();
        getTotalEnergy();
        SharedPreferences sf = getSharedPreferences("userFile",MODE_PRIVATE);
        String loginId = sf.getString("loginId","");

        getProfitEnergy(loginId);
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

    public void getEnergyRecordMonth(){
        ValueEventListener postListener = new ValueEventListener() {

            //성공
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("월별기록", "key: " + dataSnapshot.getChildrenCount());

                arrayData.clear();
                arrayIndex.clear();
                int sum = 0;

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    String key = postSnapshot.getKey();
                    GeneratorDTO get = postSnapshot.getValue(GeneratorDTO.class);
                    String[] info = {get.id,get.energy,get.date};
                    if(loginId.equals(get.id)) {
                        sum += Integer.parseInt(get.energy);
                    }
                    arrayIndex.add(key);
                    Log.d("기록", "key: " + key);
                    Log.d("기록", "info: " + info[0] + info[1]+info[2]);
                }
                monthEnergyText.setText(String.valueOf(sum));
//                arrayAdapter.clear();
//                arrayAdapter.addAll(arrayIndex);
//                arrayAdapter.notifyDataSetChanged();
            }

            //실패
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("fail","실패");
                // ...
            }
        };

        Date date = new Date();
        Date newDate = new Date(date.getTime());
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM");
        String stringdate = dt.format(newDate);

        Query sortbyAge = FirebaseDatabase.getInstance().getReference().child("generator_record").orderByChild("date").startAt(stringdate);
        sortbyAge.addListenerForSingleValueEvent(postListener);
    }

    public void getTodayEnergy(){
        ValueEventListener postListener = new ValueEventListener() {

            //성공
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("today", "key: " + dataSnapshot.getChildrenCount());

                arrayData.clear();
                arrayIndex.clear();
                int sum = 0;

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    String key = postSnapshot.getKey();
                    GeneratorDTO get = postSnapshot.getValue(GeneratorDTO.class);
                    String[] info = {get.id,get.energy,get.date};
                    if(loginId.equals(get.id)) {
                        sum += Integer.parseInt(get.energy);
                    }
                    arrayIndex.add(key);
                    Log.d("기록", "key: " + key);
                    Log.d("기록", "info: " + info[0] + info[1]+info[2]);
                }
                todayEnergyText.setText(String.valueOf(sum));
//                arrayAdapter.clear();
//                arrayAdapter.addAll(arrayIndex);
//                arrayAdapter.notifyDataSetChanged();
            }

            //실패
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("fail","실패");
                // ...
            }
        };

        Date date = new Date();
        Date newDate = new Date(date.getTime());
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
        String stringdate = dt.format(newDate);

        Query sortbyAge = FirebaseDatabase.getInstance().getReference().child("generator_record").orderByChild("date").startAt(stringdate);
        sortbyAge.addListenerForSingleValueEvent(postListener);
    }

    public void getTotalEnergy(){
        ValueEventListener postListener = new ValueEventListener() {

            //성공
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("today", "key: " + dataSnapshot.getChildrenCount());

                arrayData.clear();
                arrayIndex.clear();
                int sum = 0;

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    String key = postSnapshot.getKey();
                    GeneratorDTO get = postSnapshot.getValue(GeneratorDTO.class);
                    String[] info = {get.id,get.energy,get.date};
                    if(loginId.equals(get.id)) {
                        sum += Integer.parseInt(get.energy);
                    }
                    arrayIndex.add(key);
                    Log.d("기록", "key: " + key);
                    Log.d("기록", "info: " + info[0] + info[1]+info[2]);
                }
                totalEnergyText.setText(String.valueOf(sum));
//                arrayAdapter.clear();
//                arrayAdapter.addAll(arrayIndex);
//                arrayAdapter.notifyDataSetChanged();
            }

            //실패
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("fail","실패");
                // ...
            }
        };


        Query sortbyAge = FirebaseDatabase.getInstance().getReference().child("generator_record");
        sortbyAge.addListenerForSingleValueEvent(postListener);
    }

    public void getProfitEnergy(String userId){
        ValueEventListener postListener = new ValueEventListener() {

            //성공
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("today", "key: " + dataSnapshot.getChildrenCount());

                int sum = 0;

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    String key = postSnapshot.getKey();
                    ExchangeRecordDTO get = postSnapshot.getValue(ExchangeRecordDTO.class);
                    String[] info = {get.money,get.energy,get.date};
                    if(loginId.equals(get.sender)) {
                        sum += Integer.parseInt(get.money);
                    }
                    Log.d("머니", "sum: " + String.valueOf(sum));
                    Log.d("money", "info: " + info[0] + info[1]+info[2]);
                }
                profitText.setText(String.valueOf(sum));
            }

            //실패
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("fail","실패");
                // ...
            }
        };
        Query sortbyAge = FirebaseDatabase.getInstance().getReference().child("exchange_record").orderByChild("sender").equalTo(userId);
        sortbyAge.addListenerForSingleValueEvent(postListener);
    }
}
