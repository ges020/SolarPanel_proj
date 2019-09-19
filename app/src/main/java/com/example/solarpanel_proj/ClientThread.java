package com.example.solarpanel_proj;

import android.os.Bundle;
import android.util.Log;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import android.os.Handler;


public class ClientThread extends Thread {
    public void run() {
        Log.d("클라이언트 run", "run");

        String host = "192.168.0.84";
        int port = 8080;

        try {
            Log.d("서버 연결 전", "연결 전");

            Socket socket = new Socket(host, port);
            Log.d("서버 연결", "연결");


            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.writeObject("send");
            outputStream.flush();
            Log.d("ClientThread", "서버로 보냄.");
            //ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            //final String input = (String) inputStream.readObject(); // Object로 받아도 무방

           // String input = socket.getInputStream().toString();
            InputStream is = socket.getInputStream();
            byte[] bytes = null;
            bytes = new byte[100];

            int readByteCount = is.read(bytes);

            String input = new String(bytes, 0, readByteCount, "UTF-8");

            Log.d("ClientThread","받은 데이터 : "+input);


            Handler handler = new Handler();

            handler.post(new Runnable() {
                @Override
                public void run() {
                    //textView.setText("받은 데이터 : "+input);
                    //Log.d("sendData","받은 데이터 : "+input);
                }
            });

        } catch (Exception e) {
            Log.d("서버","서버 오류");

            e.printStackTrace();
        }
    }
}
