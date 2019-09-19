package com.example.solarpanel_proj;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
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

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

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

    //xml

    ImageView BTN_0;
    ImageView BTN_1;
    ImageView BTN_2;
    ImageView BTN_3;
    ImageView BTN_4;
    ImageView BTN_5;
    ImageView BTN_6;
    ImageView BTN_7;
    ImageView BTN_8;
    ImageView BTN_9;

    ImageView sendBTN;
    ImageView delBTN;

    ImageView generatorMenuBTN;
    ImageView exchangeMenuBTN;
    ImageView recordMenuBTN;

    String resultValueStr="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        BTN_1 = (findViewById(R.id.normal_frame7));
        BTN_2 = (findViewById(R.id.normal_frame8));
        BTN_3 = (findViewById(R.id.normal_frame9));
        BTN_4 = (findViewById(R.id.normal_frame4));
        BTN_5 = (findViewById(R.id.normal_frame5));
        BTN_6 = (findViewById(R.id.normal_frame6));
        BTN_7 = (findViewById(R.id.normal_frame1));
        BTN_8 = (findViewById(R.id.normal_frame2));
        BTN_9 = (findViewById(R.id.normal_frame3));
        BTN_0 = (findViewById(R.id.normal_frame0));

        generatorMenuBTN = (findViewById(R.id.menu_pic1));
        exchangeMenuBTN = (findViewById(R.id.menu_pic2));
        recordMenuBTN = (findViewById(R.id.menu_pic3));

        //arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1) ;
       // listview = (ListView) findViewById(R.id.listview) ;
        //listview.setAdapter(arrayAdapter) ;

        //getUserList();
        //getExchangeEnergyList();
        //postExchangeEnergy("id1","111");

        //ClientThread thread = new ClientThread();
        //thread.start();

        //userSignIn("id1","id1");
        //postUserSignUp(true);
        //sendEnergy("N","id1","2222");
        //getUserEnergy("id1");
        //updateUserEnergy("222","id1","id2");
        //updateUserEnergy("id2","222",false);
//        Calendar time = Calendar.getInstance();
//        ,time.getTime()
        //postExchangeRecord("id2","id1","2223");
       // getExchangeRecordList("id1");

        BTN_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resultValueStr+="1";
                Log.d("BTN_1 click", resultValueStr);
            }
        });

        BTN_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resultValueStr+="2";
                Log.d("BTN_2 click", resultValueStr);
            }
        });
        BTN_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resultValueStr+="3";
                Log.d("BTN_3 click", resultValueStr);
            }
        });
        BTN_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resultValueStr+="4";
                Log.d("BTN_4 click", resultValueStr);
            }
        });
        BTN_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resultValueStr+="5";
                Log.d("BTN_5 click", resultValueStr);
            }
        });
        BTN_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resultValueStr+="6";
                Log.d("BTN_6 click", resultValueStr);
            }
        });
        BTN_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resultValueStr+="7";
                Log.d("BTN_7 click", resultValueStr);
            }
        });
        BTN_8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resultValueStr+="8";
                Log.d("BTN_8 click", resultValueStr);
            }
        });
        BTN_9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resultValueStr+="9";
                Log.d("BTN_9 click", resultValueStr);
            }
        });
        BTN_0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resultValueStr+="0";
                Log.d("BTN_0 click", resultValueStr);
            }
        });
        generatorMenuBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), generatorActivity.class);
                startActivityForResult(intent, sub);//액티비티 띄우기
            }
        });
        exchangeMenuBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), exchangeActivity.class);
                startActivityForResult(intent, sub);//액티비티 띄우기
            }
        });
        recordMenuBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), recordActivity.class);
                startActivityForResult(intent, sub);//액티비티 띄우기
            }
        });

//        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView parent, View v, int position, long id) {
//
//                String strText = (String) parent.getItemAtPosition(position);
//                Log.d("click", "ID: "+strText);
//
//            }
//        });


    }
    public String delReultValue(String str) {
        if (str != null && str.length() > 0 && str.charAt(str.length()-1)=='x') {
            str = str.substring(0, str.length()-1);
        }
        return str;
    }

    //회원 목록 조회
    public void getUserList(){
        ValueEventListener postListener = new ValueEventListener() {

            //성공
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserDTO post = dataSnapshot.getValue(UserDTO.class);
                Log.d("getUsers", "key: " + dataSnapshot.getChildrenCount());

                arrayData.clear();
                arrayIndex.clear();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    String key = postSnapshot.getKey();
                    UserDTO get = postSnapshot.getValue(UserDTO.class);
                    String[] info = {get.id,get.password,get.email,get.phone,get.address, get.name,get.energy};
                    arrayIndex.add(key);
                    Log.d("getUsers", "key: " + key);
                    Log.d("getUsers", "info: " + info[0] + info[1]);

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
        Query sortbyAge = FirebaseDatabase.getInstance().getReference().child("id_list").orderByChild("id");
        sortbyAge.addListenerForSingleValueEvent(postListener);

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

    //전력 거래 기록 저장
    public void postExchangeRecord(String sender, String receiver, String sendEnergy){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> postValues = null;
//
//        SimpleDateFormat sfd = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
//        sfd.format(new Date(timestamp))
//
//        Log.d("시간", date.toString());


        ExchangeRecordDTO post = new ExchangeRecordDTO(sender,receiver,sendEnergy);
        postValues = post.toMap();

        childUpdates.put("/exchange_record/" + sender, postValues);
        mDatabase.updateChildren(childUpdates);
    }


    //발전 에너지 저장
    public void postGeneratorEnergy(String gid){
        id = gid;
        mDatabase.child("/id_list/"+id+"/energy").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Map<String, Object> childUpdates = new HashMap<>();

                userEnergy = snapshot.getValue().toString();

                String tmpEnergy="";

                tmpEnergy = String.valueOf(Integer.parseInt(userEnergy) + Integer.parseInt(changeEnergy));
                childUpdates.put("/id_list/" + receiver+"/energy", tmpEnergy );

                mDatabase.updateChildren(childUpdates);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("getUsers", databaseError.toException().toString());
                // ...
            }
        });
    }

}
