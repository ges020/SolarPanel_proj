package com.example.solarpanel_proj;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Contacts;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ExchangeListActivity extends AppCompatActivity {

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

    String money = "";


    ImageView generatorMenuBTN;
    ImageView exchangeMenuBTN;
    ImageView recordMenuBTN;
    ImageView setMenuBTN;
    ImageView exchangeBTN;

    ArrayList<ExchangeDTO> exchangeList = new ArrayList<ExchangeDTO>();

    private ListView m_oListView = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange_list);

        generatorMenuBTN = (findViewById(R.id.menu_pic1));
        exchangeMenuBTN = (findViewById(R.id.menu_pic2));
        recordMenuBTN = (findViewById(R.id.menu_pic3));
        setMenuBTN = (findViewById(R.id.menu_pic4));
        exchangeBTN = (findViewById(R.id.exchangeBTN));
        listview = (findViewById(R.id.listview));

        //arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1) ;



        // 데이터 1000개 생성--------------------------------.
        String[] strDate = {"2017-01-03", "1965-02-23", "2016-04-13", "2010-01-01", "2017-06-20",
                "2012-07-08", "1980-04-14", "2016-09-26", "2014-10-11", "2010-12-24"};
        int nDatCnt=0;
        ArrayList<ItemData> oData = new ArrayList<>();
        for (int i=0; i<1000; ++i)
        {
            ItemData oItem = new ItemData();
            oItem.strTitle = "데이터 " + (i+1);
            oItem.strContent = strDate[nDatCnt++];
            oData.add(oItem);
            if (nDatCnt >= strDate.length) nDatCnt = 0;
        }

        // ListView, Adapter 생성 및 연결 ------------------------
        m_oListView = (ListView)findViewById(R.id.listview);
        ListAdapter oAdapter = new ListAdapter(oData);
        m_oListView.setAdapter(oAdapter);





        //listview = (ListView) findViewById(R.id.listview) ;
        //listview.setAdapter(arrayAdapter) ;

        //getExchangeEnergyList();

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
        exchangeBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivityForResult(intent, sub);//액티비티 띄우기
            }
        });
    }

    //회원 간 전력량 변경
    public void updateUserEnergy(String energy, String sender1, String receiver1){
        mDatabase = FirebaseDatabase.getInstance().getReference();

        changeEnergy = energy;
        userId = id;
        sender = sender1;
        receiver = receiver1;

        mDatabase.child("/id_list/"+sender+"/energy").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Map<String, Object> childUpdates = new HashMap<>();

                userEnergy = snapshot.getValue().toString();

                String tmpEnergy="";

                tmpEnergy = String.valueOf(Integer.parseInt(userEnergy) - Integer.parseInt(changeEnergy));
                childUpdates.put("/id_list/" + sender+"/energy", tmpEnergy );

                mDatabase.updateChildren(childUpdates);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("getUsers", databaseError.toException().toString());
                // ...
            }
        });

        mDatabase.child("/id_list/"+receiver+"/energy").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Map<String, Object> childUpdates = new HashMap<>();

                userEnergy = snapshot.getValue().toString();

                String tmpEnergy="";

                tmpEnergy = String.valueOf(Integer.parseInt(userEnergy) + Integer.parseInt(changeEnergy));
                childUpdates.put("/id_list/" + receiver+"/energy", tmpEnergy );

                mDatabase.updateChildren(childUpdates);

                Toast toast = Toast.makeText(getApplicationContext(), "전력 거래에 성공했습니다", Toast.LENGTH_SHORT);
                toast.show();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("getUsers", databaseError.toException().toString());
                // ...
            }
        });
    }


    //거래 목록 조회
    public void getExchangeEnergyList(){

        ValueEventListener postListener = new ValueEventListener() {

            //성공
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ExchangeDTO post = dataSnapshot.getValue(ExchangeDTO.class);
                Log.d("기록", "key: " + dataSnapshot.getChildrenCount());

                arrayData.clear();
                arrayIndex.clear();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    String key = postSnapshot.getKey();
                    ExchangeDTO get = postSnapshot.getValue(ExchangeDTO.class);
                    String[] info = {get.sender,get.energy};
                    arrayIndex.add(key);
                    Log.d("기록", "key: " + key);
                    Log.d("기록", "info: " + info[0] + info[1]);

                }
                arrayAdapter.clear();
                arrayAdapter.addAll(arrayIndex);
                arrayAdapter.notifyDataSetChanged();
            }

            //실패
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        Query sortbyAge = FirebaseDatabase.getInstance().getReference().child("exchange_list");
        sortbyAge.addListenerForSingleValueEvent(postListener);
    }
}
