package com.example.serveme_m1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.example.serveme_m1.Models.CategioryModel;
import com.example.serveme_m1.resolvers.SharedPref;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class VendorRegistration extends AppCompatActivity {

    CheckBox checkBox1,checkBox2,checkBox3,checkBox4,checkBox5,checkBox6,checkBox7,checkBox8,checkBox9;
    TextInputEditText name,email,phone,description,address;
    Button register,cancel;
    RequestQueue requestQueue;
    ArrayList<CategioryModel> categories = new ArrayList<CategioryModel>() ;
    ArrayList<Integer> categorIds = new ArrayList<Integer>();
    int tracker = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vedor_registration);
        init_view();
        init_onclick();
    }


    private void init_onclick() {
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ArrayList<String> categories = getCheckBoxs();
                get_categories(categories);
                Log.e("categories",categorIds.toString());

                commit_request();


//
//                while(tracker < categories.size()){
//
//                }
//                d.dismiss();
                commit_request();

            }
        });
    }

    private void commit_request() {

        Map<String,Object> params = new HashMap<String,Object>();
        params.put("vendorName",name.getText().toString());
        params.put("vendorPhone",phone.getText().toString());
        params.put("vendorAddress",address.getText().toString());
        params.put("vendorDescription",description.getText().toString());
        params.put("vendorEmail",email.getText().toString());
        params.put("id", SharedPref.getInstance(VendorRegistration.this).getSharedPred().get("userId"));
        params.put("categoryIds",categorIds);
        params.put("categoryId","0");

        Dialog d = new Dialog(VendorRegistration.this);
        if(d.getWindow()!=null)
            d.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        d.setContentView(R.layout.custom_progress);
        d.setCancelable(false);
        d.show();

        JsonObjectRequest reg_request = new JsonObjectRequest(Request.Method.POST, getString(R.string.base_url) + getString(R.string.registerVendor), new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if(response.has("message")){
                    try {
                        if(response.getString("message").toLowerCase(Locale.ROOT).equals("success")){
                            Toast.makeText(VendorRegistration.this, "Registration Successfull", Toast.LENGTH_SHORT).show();
                            d.dismiss();
                            Map<String,String> appData = new HashMap<String,String>();
                            appData.put("isVendor","yes");
                            SharedPref.getInstance(VendorRegistration.this).setSharedPred(appData);
                            finish();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                d.dismiss();
                Log.e("vendroReg Error",new String(error.networkResponse.data));
            }
        });
        Log.e("commitRequestBOdy",new String(reg_request.getBody()));
        requestQueue.add(reg_request);

    }

    private ArrayList<String> getCheckBoxs() {
        ArrayList<String> result = new ArrayList<String>();
        if(checkBox1.isChecked())
            result.add(checkBox1.getText().toString());
        if(checkBox2.isChecked())
            result.add(checkBox2.getText().toString());
        if(checkBox3.isChecked())
            result.add(checkBox3.getText().toString());
        if(checkBox4.isChecked())
            result.add(checkBox4.getText().toString());
        if(checkBox5.isChecked())
            result.add(checkBox5.getText().toString());
        if(checkBox6.isChecked())
            result.add(checkBox6.getText().toString());
        if(checkBox7.isChecked())
            result.add(checkBox7.getText().toString());
        if(checkBox8.isChecked())
            result.add(checkBox8.getText().toString());
        if(checkBox9.isChecked())
            result.add(checkBox9.getText().toString());

        return result;
    }

    private void init_view() {

        requestQueue = Volley.newRequestQueue(VendorRegistration.this);

        checkBox1 = findViewById(R.id.checkBox);
        checkBox2 = findViewById(R.id.checkBox1);
        checkBox3 = findViewById(R.id.checkBox2);
        checkBox4 = findViewById(R.id.checkBox3);
        checkBox5 = findViewById(R.id.checkBox4);
        checkBox6 = findViewById(R.id.checkBox5);
        checkBox7 = findViewById(R.id.checkBox6);
        checkBox8 = findViewById(R.id.checkBox7);
        checkBox9 = findViewById(R.id.checkBox8);
        name =(TextInputEditText) findViewById(R.id.name);
        phone =(TextInputEditText) findViewById(R.id.phone);
        address =(TextInputEditText) findViewById(R.id.address);
        email =(TextInputEditText) findViewById(R.id.email);
        description =(TextInputEditText) findViewById(R.id.description);

        register = findViewById(R.id.register);
        cancel = findViewById(R.id.cancel);


    }

    private void get_categories(ArrayList<String> categories) {


        Dialog d = new Dialog(VendorRegistration.this);
        if(d.getWindow()!=null)
            d.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        d.setContentView(R.layout.custom_progress);
        d.setCancelable(false);
        d.show();

        for(String categoy : categories){
            Map<String,String> temp = new HashMap<String,String>();
            temp.put("name",categoy);

            JsonObjectRequest getCat = new JsonObjectRequest(Request.Method.POST, getString(R.string.base_url)+getString(R.string.getCategory), new JSONObject(temp), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.e("GetCategory",response.toString());
                    if(response.has("message")){
                        try {
                            if(response.getString("message").toLowerCase(Locale.ROOT).equals("success")){
                                JSONObject data = response.getJSONObject("data");
                                categorIds.add( data.getInt("categoryId"));
                                if(categorIds.size() == categories.size()){
                                    commit_request();
                                    d.dismiss();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            }
            );

            Log.e("Request getCategor", new String(getCat.getBody()));
            requestQueue.add(getCat);

        }


    }
}