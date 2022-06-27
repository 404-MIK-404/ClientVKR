package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.*;
import android.view.View;
import android.widget.TextView;

import com.example.test.ActivityMain.Main;
import com.example.test.ActivitySignInUp.SignIn;
import com.example.test.Client.Client;



import pl.droidsonroids.gif.GifImageView;

public class WaitConnectionServer extends AppCompatActivity {

    private final Client User = Client.UserAndroid;
    private GifImageView GifLoadConnectToServer;
    private TextView TextViewTryReconnectToServer;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait_connection_server);
        getSupportActionBar().hide();
        init();
    }


    private void init(){
        GifLoadConnectToServer = findViewById(R.id.GifLoadConnectToServer);
        TextViewTryReconnectToServer = findViewById(R.id.TextViewTryReconnectToServer);
        setVisibleOn();

    }

    private void setVisibleOff(){
        GifLoadConnectToServer.setVisibility(View.VISIBLE);
        TextViewTryReconnectToServer.setVisibility(View.VISIBLE);
    }

    private void reconnectFail(){
        GifLoadConnectToServer.setVisibility(View.GONE);
        TextViewTryReconnectToServer.setText("Подключиться к серверу не получилось");
    }

    public void recconectToServerClick(View view){
        setVisibleOff();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    if (User.reconnectToServer()){
                        changeActivity();
                    }
                    else{
                        reconnectFail();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },1);

    }

    private void changeActivity(){
        if (!User.getUIDUser().isEmpty()){
            Intent change = new Intent(WaitConnectionServer.this, Main.class);
            startActivity(change);
        }
        else {
            Intent change = new Intent(WaitConnectionServer.this, SignIn.class);
            startActivity(change);
        }

    }


    private void setVisibleOn(){
        GifLoadConnectToServer.setVisibility(View.GONE);
        TextViewTryReconnectToServer.setVisibility(View.GONE);

    }



}