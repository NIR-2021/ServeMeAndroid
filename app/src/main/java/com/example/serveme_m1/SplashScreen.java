package com.example.serveme_m1;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.serveme_m1.R;
import com.example.serveme_m1.resolvers.SharedPref;

import org.joda.time.DateTime;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class SplashScreen extends AppCompatActivity {

    int LOGIN = 1;
    int REGISTRATION = 0 ;
    int OPEN_REGISTRATION =201;


    Animation splash_anim;
    ImageView splash_image;
    ConstraintLayout splash_layout;
    Map<String,String> appData;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REGISTRATION){
            //100 : Jump user to login page
            if(resultCode == 100 || resultCode == 200){
                Intent login = new Intent(getApplicationContext(),Login.class);
                startActivityForResult(login,LOGIN);
            }
            //Just close the regisration

        }

        else if(requestCode == LOGIN){
            if(resultCode == RESULT_CANCELED){
                Log.i("Login Info","Login Cancelled.");
                startActivity(new Intent(this,MainActivity.class));
                finish();
            }
            else if(resultCode == RESULT_OK){
                Log.i("Login Info","Login successfull.");
                String userId = data.getStringExtra("userId");
                HashMap<String,String> appData = new HashMap<String,String>();
                appData.put("userId",userId);
                appData.put("isLoggedIn","Yes");
                appData.put("email",data.getStringExtra("email"));
                appData.put("date", DateTime.now().toString());
                Log.e("Login Successfull",appData.toString());
                SharedPref.getInstance(getApplicationContext()).setSharedPred(appData);
                startActivity(new Intent(this,MainActivity.class));
                finish();


            }
            else if(resultCode == OPEN_REGISTRATION){
                startActivityForResult(new Intent(this,Registration.class),REGISTRATION);
            }
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        splash_layout = (ConstraintLayout) findViewById(R.id.splash_layout);
        splash_image = (ImageView) findViewById(R.id.splash_image);
        splash_anim = AnimationUtils.loadAnimation(this,R.anim.splash_anim);
        splash_image.setAnimation(splash_anim);
        splash_layout.animate().alpha(0).setStartDelay(2000).setDuration(1000);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                LoadApplicationData();

                if(appData.get("isLoggedIn").toLowerCase(Locale.ROOT).equals("no")){
                    Intent intent = new Intent(SplashScreen.this,Login.class);
                    startActivityForResult(intent,LOGIN);
                }
                else{
                    Intent intent = new Intent(SplashScreen.this,MainActivity.class);
                    startActivity(intent);
                    finish();

                }
            }
        },3100);


    }

    private synchronized void LoadApplicationData() {
        Log.e("OnStart Loading","Performing loading");
       appData = SharedPref.getInstance(getApplicationContext()).getSharedPred();
    }
}