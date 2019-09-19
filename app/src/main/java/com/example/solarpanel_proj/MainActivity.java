package com.example.solarpanel_proj;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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

    private static final String TAG = "MainActivity";

    private DatabaseReference mDatabase;

    String id="id2";
    String password="id2";
    String email = "email2@email";
    String phone = "01030304040";
    String address= "add2";
    String name="name2";
    String energy = "3334";

    ArrayAdapter<String> arrayAdapter;

    static ArrayList<String> arrayIndex =  new ArrayList<String>();
    static ArrayList<String> arrayData = new ArrayList<String>();

    ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayData) ;
        //listview = (ListView) findViewById(R.id.listview1) ;
        //listview.setAdapter(arrayAdapter) ;

        //postUserSignUp(true);
        //sendEnergy("N","id1","2222");
        //getUserList();
        getUserInfo("id1");
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


    //회원목록
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
                //arrayAdapter.clear();
                //arrayAdapter.addAll(arrayData);
                //arrayAdapter.notifyDataSetChanged();
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

    //전력 전송
    public boolean sendEnergy(String sender, String receiever, String energy){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Map<String, Object> childUpdates = new HashMap<>();

        childUpdates.put("/id_list/" + receiever+"/energy", energy);
        mDatabase.updateChildren(childUpdates);

        return true;
    }

    //회원 전력량
    public String getUserEnergy(String user){


        return user;
    }

    //회원 목록 클릭 이벤트
    public void clickUserList() {
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {

                String strText = (String) parent.getItemAtPosition(position);

            }
        });
    }

    //회원 정보
    public void getUserInfo(String id){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("/id_list/"+id).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                UserDTO result = snapshot.getValue(UserDTO.class);
                Log.d("getUsers", result.password);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("getUsers", databaseError.toException().toString());
                // ...
            }
        });
    }
}
