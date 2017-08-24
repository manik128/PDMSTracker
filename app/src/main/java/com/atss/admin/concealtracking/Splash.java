package com.atss.admin.concealtracking;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

public class Splash extends AppCompatActivity {

    android.support.v7.app.ActionBar abr;
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sp;
    boolean hasLoggedIn=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        sp  = getSharedPreferences(Splash.MyPREFERENCES, 0);
        hasLoggedIn = sp.getBoolean("login", false);

        abr= getSupportActionBar();
        abr.hide();



        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {


                if (hasLoggedIn) {
                    Intent intent = new Intent(Splash.this, TabHome.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
                else{
                Intent n = new Intent(getApplicationContext(), Login.class);
                startActivity(n);
                finish();}

            }
        }, 1000);


    }
}
