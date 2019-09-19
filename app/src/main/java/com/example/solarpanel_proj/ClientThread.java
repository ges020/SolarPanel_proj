package com.example.solarpanel_proj;

import android.os.Bundle;
import android.util.Log;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import android.os.Handler;


public class ClientThread extends Thread {
    public void run() {
        String host = "localhost";
        int port = 5001;

        try {
            Socket socket = new Socket(host, port);

            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.writeObject("전송");
            outputStream.flush();
            Log.d("ClientThread", "서버로 보냄.");
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            final String input = (String) inputStream.readObject(); // Object로 받아도 무방
            Log.d("ClientThread","받은 데이터 : "+input);


            Handler handler = new Handler();
            
            handler.post(new Runnable() {
                @Override
                public void run() {
                    //textView.setText("받은 데이터 : "+input);
                    Log.d("sendData","받은 데이터 : "+input);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
