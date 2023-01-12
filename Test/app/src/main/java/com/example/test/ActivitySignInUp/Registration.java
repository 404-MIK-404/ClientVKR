package com.example.test.ActivitySignInUp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;

import com.example.test.ActivityMain.Main;
import com.example.test.Client.Client;
import com.example.test.R;


/***
 *
 * Экран регистраций пользователя чтобы он потом смог зайти в данное приложение.
 *
 * @author MIK
 *
 */

public class Registration extends AppCompatActivity {

    private EditText enterPasswordRepeat,enterPasswordUser,enterEmailUser;
    private final Client User = Client.UserAndroid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        getSupportActionBar().hide();
        init();
    }

    /**
     * Иницилизация кнопок и прочего
     */

    private void init()
    {
        enterPasswordRepeat = findViewById(R.id.enterPasswordRepeat);
        enterPasswordUser = findViewById(R.id.enterPasswordUser);
        enterEmailUser = findViewById(R.id.enterEmailUser);
    }

    /**
     *
     *  В идеале тут этого не должно быть, в идеале я должен был создать какой нибудь класс
     *  чтобы он взаимодействовал с данными и передавал всё это в Веб Сокет.
     *  то есть почта и пароль должен быть отправлен серваку чтобы он его закинул и после этого закинуть ему на почту которую он указал 6 значный код который система придумала.
     */
    private void Reg(String Email, String Password)
    {
        User.CreateAction("action","REG_ACC");
        User.CreateAction("email",Email);
        User.CreateAction("password",Password);
        User.SendJsonMessage();
        User.WaitMessage();
        if("succ".equals(User.getResultFromServer()))
        {
            startActivity(new Intent(Registration.this, Main.class));
        }
        else
        {
            if (User.getCodeVeryfaction().isEmpty()){
                Toast.makeText(this,"Ошибка при создания профиля",Toast.LENGTH_LONG).show();
            }
        }
    }


    /**
     *
     * Здесь происходит регистрация юзера
     *
     */

    public void RegisterUser(View view)
    {
        if (!TextUtils.isEmpty(enterEmailUser.getText().toString()) && !TextUtils.isEmpty(enterPasswordUser.getText().toString()) && !TextUtils.isEmpty(enterPasswordRepeat.getText().toString()))
        {
            if (enterPasswordRepeat.getText().toString().equals(enterPasswordUser.getText().toString()))
            {
                Reg(enterEmailUser.getText().toString(),enterPasswordUser.getText().toString());
            }
            else {
                Toast.makeText(this,"Пароли неправильно введены",Toast.LENGTH_LONG).show();
            }
        }
        else{
            Toast.makeText(this,"Некоторые поля пустые, пожалуйста заполните все поля",Toast.LENGTH_LONG).show();
        }
    }

    public void backToMainActivity(View view)
    {
        startActivity(new Intent(this, SignIn.class));
    }




}