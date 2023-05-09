package com.example.serveme_m1;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.example.serveme_m1.databinding.ActivityMainBinding;
import com.example.serveme_m1.resolvers.SharedPref;

import org.joda.time.DateTime;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    int LOGIN = 1;
    int REGISTRATION = 0 ;
    int OPEN_REGISTRATION =201;

    private void changeFragment(Fragment frag){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout,frag);
        fragmentTransaction.commit();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

//======================================================================================================================================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        changeFragment(new HomeFragment());



        binding.bottomNavigationView.setOnItemSelectedListener(item->{
            switch (item.getItemId()){
                case R.id.home:
                    changeFragment(new HomeFragment());
                    break;
                case R.id.search:
                    changeFragment(new SearchFragment());
                    break;
                case R.id.profile:
                    changeFragment(new ProfileFragment());
                    break;


            }
            return true;
        });



    }


}