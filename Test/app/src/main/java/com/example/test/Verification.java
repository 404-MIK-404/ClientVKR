package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.test.ActivityMain.Main;
import com.example.test.Client.Client;


/**
 *
 * Здесь пользователь оказывается на экране верификаций по его почте должен прийти 6 значный код и он должен был ввести и если норм проходил дальше.
 * Пользователь оказываетесь тут если он первый раз заходит в данное приложение
 *
 */
public class Verification extends AppCompatActivity {

    private EditText editTextNumberPassword;
    private final Client User = Client.UserAndroid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        User.setActivityNow(this);
        setContentView(R.layout.activity_verification);
        init();
    }

    private void init(){
        editTextNumberPassword = findViewById(R.id.editTextNumberPassword);
    }

    public void sendValueVeryfication(View view){

        if (!TextUtils.isEmpty(editTextNumberPassword.getText())
            && editTextNumberPassword.getText().toString().equals(User.getCodeVeryfaction())){
            if ("succ".equals(User.SendVeryficationCode(Integer.parseInt(editTextNumberPassword.getText().toString())))){

                changeActivityToMain();
            }
            else {
                Toast.makeText(this,"Код верификаций неверен",Toast.LENGTH_LONG).show();
            }

        }
        else {
            Toast.makeText(this,"Код верификаций неверен",Toast.LENGTH_LONG).show();
        }

    }

    private void changeActivityToMain(){
        startActivity(new Intent(Verification.this, Main.class));
    }

}