package com.example.serveme_m1.resolvers;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;

public class SharedPref {

    private static Map<String,String> appData = new HashMap<String,String>();
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;
    private static SharedPref self;

    public SharedPref(Context context){

        sharedPreferences = context.getSharedPreferences("ApplicationData",context.MODE_PRIVATE);
        editor = sharedPreferences.edit();        //Second arg in getString return the dafault value if the preference is not present.
        
        appData.put("isLoggedIn",sharedPreferences.getString("isLoggedIn","No"));
        appData.put("userId",sharedPreferences.getString("userId",""));
        appData.put("email",sharedPreferences.getString("email",""));
        appData.put("date",sharedPreferences.getString("date",""));
        appData.put("isVendor",sharedPreferences.getString("isVendor",""));
    }

    public static SharedPref getInstance(Context context){
        return new SharedPref(context);
    }

    public Map<String,String> getSharedPred(){
        return appData;
    }

    public boolean setSharedPred(Map<String,String> ad){
        for(Map.Entry<String,String> entry : ad.entrySet()){
            editor.putString(entry.getKey().toString(),entry.getValue().toString());
        }
        editor.commit();
        return true;
    }

}
