package com.example.test.ActivitySignInUp;

import androidx.annotation.*;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.*;

import com.example.test.ActivityMain.Main;
import com.example.test.Client.Client;
import com.example.test.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.*;
import com.google.android.gms.common.*;
import com.google.android.gms.common.api.*;
import com.google.android.gms.tasks.Task;

public class SignIn extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private EditText enterUsernameText,enterPasswordText;
    private final Client User = Client.UserAndroid;
    private SignInButton signInButton;
    private static final int SIGN_IN = 123;
    private GoogleApiClient googleApiClient;
    GoogleSignInClient mGoogleSignInClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initiateSocketConnection();
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        initGoogle();
        init();
    }


    /**
     *
     * Здесь идёт иницилизация гугл сервиса как я помню.
     *
     */
    private void initGoogle(){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id_ex))
                .requestServerAuthCode(getString(R.string.default_web_client_id_ex))
                .requestScopes(new Scope("https://www.googleapis.com/auth/calendar"))
                .requestEmail()
                .build();
        googleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this,gso);
    }


    private void init()
    {
        enterPasswordText = findViewById(R.id.enterPasswordText);
        enterUsernameText = findViewById(R.id.enterUsernameText);
        signInButton = findViewById(R.id.signInButton);
        setGooglePlusButtonText(signInButton,"Войти через Google");
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.signInButton:
                        signIn();
                        break;
                }
            }
        });

    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, SIGN_IN);
    }

    private void initiateSocketConnection(){
        User.setActivityNow(this);
        User.initiateSocketConnection();

    }


    @Override
    protected void onStart()
    {
        super.onStart();
    }


    private void startSingIn(String log,String pass)
    {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if("succ".equals(User.SignIn(log,pass)))
                {
                    changeActivityToMain();
                }
                else
                {
                    if (String.valueOf(User.getCodeVeryfaction()).isEmpty()){
                        Toast.makeText(SignIn.this,"Ошибка при заходе в профиль",Toast.LENGTH_LONG).show();
                    }
                }
            }
        },1);
    }

    private void changeActivityToMain()
    {
        Intent intent = new Intent(this, Main.class);
        startActivity(intent);
    }


    /**
     *
     * Здесь идёт создание гугл кнопки, почему то я не мог менять размер и что то ещё с ним сделать пришлось это программно делать
     *
     */
    private void setGooglePlusButtonText(SignInButton signInButton, String buttonText) {
        for (int i = 0; i < signInButton.getChildCount(); i++) {
            View v = signInButton.getChildAt(i);
            if (v instanceof TextView) {
                TextView tv = (TextView) v;
                tv.setText(buttonText);
                tv.setTextSize(22);
                tv.setTextColor(Color.parseColor("#5D5C61"));
                return;
            }
        }
    }

    public void onClickButton(View view)
    {
        if (!enterPasswordText.getText().toString().isEmpty() && !enterUsernameText.getText().toString().isEmpty())
        {
            startSingIn(enterUsernameText.getText().toString(),enterPasswordText.getText().toString());
        }
    }

    public void changeActivityRegister(View view)
    {
        startActivity(new Intent(this, Registration.class));
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            Toast.makeText(this, "Sign-in Successful", Toast.LENGTH_SHORT).show();
            changeActivityToMain();
        } catch (ApiException e) {
            Log.w("Error", "signInResult:failed code=" + e.getStatusCode());
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()){
                if("succ".equals(User.SignIn(result)))
                {
                    handleSignInResult(task);
                }
                else
                {
                    Toast.makeText(this,"Ошибка при заходе в профиль",Toast.LENGTH_LONG).show();
                }
            }
            else {
                System.out.println(result.getStatus());
            }
        }
    }



}