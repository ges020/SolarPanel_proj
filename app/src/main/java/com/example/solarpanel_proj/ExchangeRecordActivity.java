package com.example.solarpanel_proj;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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

public class ExchangeRecordActivity extends AppCompatActivity {

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

//    String sendEnergy="";
//    Date date;


    ImageView generatorMenuBTN;
    ImageView exchangeMenuBTN;
    ImageView recordMenuBTN;
    ImageView setMenuBTN;

    private ListView m_oListView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange_record);


//        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1) ;
//        listview = (ListView) findViewById(R.id.listview) ;
//        listview.setAdapter(arrayAdapter) ;



        generatorMenuBTN = (findViewById(R.id.menu_pic1));
        exchangeMenuBTN = (findViewById(R.id.menu_pic2));
        recordMenuBTN = (findViewById(R.id.menu_pic3));
        setMenuBTN = (findViewById(R.id.menu_pic4));

        m_oListView = (ListView)findViewById(R.id.listview);

        SharedPreferences sf = getSharedPreferences("userFile",MODE_PRIVATE);
        String loginId = sf.getString("loginId","");

        getExchangeRecordList(loginId);

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
                SharedPreferences sharedPreferences = getSharedPreferences("userFile",MODE_PRIVATE);

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("loginId","");
                editor.commit();

                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivityForResult(intent, sub);//액티비티 띄우기
            }
        });

        m_oListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {

                String strText = (String) parent.getItemAtPosition(position);
                Log.d("click", "ID: "+strText);

            }
        });
    }

    //전력 거래 기록 가져오기
    public void getExchangeRecordList(String userId){
        Log.d("테스트", "getExchangeRecordList");

        ValueEventListener postListener = new ValueEventListener() {

            //성공
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("기록", "key: " + dataSnapshot.getChildrenCount());

                arrayData.clear();
                arrayIndex.clear();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    String key = postSnapshot.getKey();
                    Log.d("기록", "key: " + key);
                    ExchangeRecordDTO get = postSnapshot.getValue(ExchangeRecordDTO.class);

                    String[] info = {get.sender,get.receiver,get.energy,get.money,get.date};
                    arrayIndex.add("전력량 : "+get.energy+" / "+get.date);
                    arrayData.add("판매자 : "+get.sender+" 가격 : "+get.money);
                    Log.d("기록", "info: " + info[0] + info[1] + info[2] + info[3]+info[4]);

                }

                int nDatCnt=0;
                ArrayList<ItemData> oData = new ArrayList<>();
                for (int i=0; i<arrayIndex.size(); ++i)
                {
                    ItemData oItem = new ItemData();
                    oItem.strTitle = arrayIndex.get(i);
                    oItem.strContent = arrayData.get(i);
                    Log.d("커스텀",oItem.strTitle+","+oItem.strContent);
                    oData.add(oItem);
                }

                // ListView, Adapter 생성 및 연결 ------------------------
                ListAdapter oAdapter = new ListAdapter(oData);
                m_oListView.setAdapter(oAdapter);
//                arrayAdapter.clear();
//                arrayAdapter.addAll(arrayIndex);
//                arrayAdapter.notifyDataSetChanged();
            }

            //실패
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        Query sortbyAge = FirebaseDatabase.getInstance().getReference().child("exchange_record").orderByChild("receiver").equalTo(userId);
        sortbyAge.addListenerForSingleValueEvent(postListener);
    }

}
