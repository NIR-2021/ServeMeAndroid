package com.example.serveme_m1;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;


import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.serveme_m1.Models.User;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Registration extends AppCompatActivity {

    TextView errs;
    Button signup,login,cancel;
    TextInputEditText firstName,lastName,email,address,password,renter_password,vendorName,phone;
    LinearLayout vendorSection;
    Gson gson = new Gson();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
//        errs = (TextView) findViewById(R.id.errors);
        firstName = (TextInputEditText) findViewById(R.id.firstName);
        lastName = (TextInputEditText) findViewById(R.id.lastName);
        address = (TextInputEditText) findViewById(R.id.address);
        password = (TextInputEditText) findViewById(R.id.password);
//        renter_password = (EditText) findViewById(R.id.repeat_password);
        email = (TextInputEditText) findViewById(R.id.email);
        phone = (TextInputEditText) findViewById(R.id.phone);
////        vendorRegistration = (CheckBox) findViewById(R.id.checkBox);
//        vendorName = (EditText) findViewById(R.id.vendor_name);
//        vendorSection = (LinearLayout) findViewById(R.id.vendor_section);
        cancel = (Button) findViewById(R.id.cancel);
        signup = (Button) findViewById(R.id.signup);
        login = (Button) findViewById(R.id.login);
        init_button();

    }

    public boolean validate(){
        boolean validated = true;

        if(firstName.length() == 0){
            firstName.setError("This field is required");
            validated = false;
        }
        if(lastName.length() == 0){
            lastName.setError("This field is required");
            validated = false;
        }
        if(address.length() == 0){
            address.setError("This field is required");
            validated = false;
        }
        if(password.length() == 0){
            password.setError("This field is required");
            validated = false;
        }

        if(email.length() == 0){
            email.setError("This field is required");
            validated = false;
        }
        if(phone.length() == 0){
            phone.setError("This field is required");
            validated = false;
        }

        return validated;
    }



    private void init_button() {

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                setResult(100,intent);
                finish();
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(Registration.this, "Submit pressed", Toast.LENGTH_SHORT).show();
               if(validate() || true){
                   User user = new User();
                   user.setFirstName(firstName.getText().toString());
                   user.setLastName(lastName.getText().toString());
                   user.setEmail(email.getText().toString());
                   user.setAddress(address.getText().toString());
                   user.setPhoneNumber(phone.getText().toString());
                   user.setPassword(password.getText().toString());
                   RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

                   Dialog d = new Dialog(Registration.this);
                   if(d.getWindow()!=null)
                       d.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                   d.setContentView(R.layout.custom_progress);


                   d.setCancelable(false);
                   d.show();

                   Map<String,String> params = new HashMap<String,String>();
                   params.put("Firstname",user.getFirstName());
                   params.put("lastname",user.getLastName());
                   params.put("email",user.getEmail());
                   params.put("address",user.getAddress());
                   params.put("password",user.getPassword());
                   params.put("phonenumber",user.getPhoneNumber());
//                   params.put("Firstname","string");
//                   params.put("lastname","string");
//                   params.put("email","user@example.com");
//                   params.put("address","string");
//                   params.put("password","String@123");
//                   params.put("phonenumber","123456790");

                   JsonObjectRequest req = new JsonObjectRequest(getString(R.string.base_url)+getString(R.string.registration_url), new JSONObject(params),
                           new Response.Listener<JSONObject>() {
                               @Override
                               public void onResponse(JSONObject response) {
                                   d.dismiss();
                                   ///Start working from here
                                   Log.e("OnsuccessResponse",response.toString());
                                   try {
                                       if((response.getString("message")).equals("Success")){
                                           Toast.makeText(Registration.this, "Registration successfull", Toast.LENGTH_SHORT).show();
                                            setResult(200);
                                           finish();
                                       }
                                       else{
                                           Toast.makeText(Registration.this, "Failure at OnSuccessResponse", Toast.LENGTH_SHORT).show();
                                       }
                                   } catch (JSONException e) {
                                       e.printStackTrace();
                                   }
                               }
                           }, new Response.ErrorListener() {
                       @SuppressLint("ResourceAsColor")
                       @Override
                       public void onErrorResponse(VolleyError error) {
                           d.dismiss();
                           Toast.makeText(Registration.this, "OnErrorResponse", Toast.LENGTH_SHORT).show();
                           NetworkResponse response = error.networkResponse;
                           String err_String = "";
                           try {
                               if(response == null || response.data == null){
                                   Log.e("Response Error","Server error (internal) Check connection string in the api.");
                               }
                               else{
                                   Log.e("Raw Response",error.networkResponse.data.toString());
                                   JSONObject js = new JSONObject(new String(error.networkResponse.data));
//                                   Log.e("Error Json raw:",new String(js.getString("message")));
                                   if(!js.has("message")){
                                       JSONObject err = js.getJSONObject("errors");
                                       Log.e("ErrorsList:",err.toString());

                                       for (Iterator<String> it = err.keys(); it.hasNext(); ) {
                                           String key = it.next();
                                           JSONArray ar = err.getJSONArray(key);
                                           for(int i =0;i<ar.length();i++){
                                               err_String += ar.get(i);
                                               err_String += "\n";

                                           }
                                       }
                                   }
                                   else{
                                       Toast.makeText(Registration.this, "Message tag found", Toast.LENGTH_SHORT).show();
                                       JSONObject er2 = js.getJSONObject("data");
                                       Log.e("data",er2.toString());
                                       Iterator<String> keys = er2.keys();
                                       er2 = er2.getJSONObject(keys.next());//dup
                                       String er_st = er2.getJSONArray("errors").getJSONObject(0).getString("errorMessage");
//                                       String er_stt = er_st.getString("errorMessages");

                                       Log.e("Message_Data",er_st+"==Data");
                                       err_String = er_st;
                                   }

                               }
                               AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Registration.this);
                               alertDialogBuilder.setMessage(err_String);
                               alertDialogBuilder.setTitle("Warning:");
                               alertDialogBuilder.setPositiveButton("ok",
                                       new DialogInterface.OnClickListener() {
                                           @Override
                                           public void onClick(DialogInterface arg0, int arg1) {
                                                Log.i("AlertDialogue","Positive response pressed");
                                           }
                                       });
                               AlertDialog alertDialog = alertDialogBuilder.create();
                               alertDialog.show();

                           } catch (JSONException e) {
                               e.printStackTrace();
                           }
                       }
                   }){
                       @Override
                       public Map<String, String> getHeaders() throws AuthFailureError {
                           HashMap<String, String> headers = new HashMap<String,String>();
                           // headers.put("Content-Type", "application/json; charset=utf-8");
                           return headers;
                       }
                       @Override
                       public String getBodyContentType() {
                           return "application/json";
                       }
                   };
                    Log.e("Requet Data", new String(req.getBody()));

                   requestQueue.add(req);

               }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setResult(200);
                finish();
            }
        });

//
//        vendorRegistration.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                boolean checked = vendorRegistration.isChecked();
//                if(checked){
//
//                }
//            }
//        });
    }

    public void onCheckboxClicked(View view) {
        boolean checked = ((CheckBox) view).isChecked();
        if(checked){
            vendorSection.setVisibility(View.VISIBLE);
        }
        else{
            vendorSection.setVisibility(View.GONE);
        }
    }


}