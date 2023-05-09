package com.example.serveme_m1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.serveme_m1.resolvers.ValidationResolver;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    Button login,cancel,register;
//    EditText username,password;
    TextInputEditText username,password;
    TextView errormsg;
    int OPEN_REGISTRATION =201;


    void init_items(){
        username = (TextInputEditText) findViewById(R.id.username);
        password = (TextInputEditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.login);
        cancel = (Button) findViewById(R.id.cancel);
        register = (Button) findViewById(R.id.register);
//        errormsg = (TextView) findViewById(R.id.errormsg);

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init_items();

        button_listeners();
    }

    private void button_listeners() {
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Login.this, "Login pressed", Toast.LENGTH_SHORT).show();
                Map<String,String> login = new HashMap<String,String>();
                login.put("Email",username.getText().toString());
                login.put("Password",password.getText().toString());

                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                Dialog d = new Dialog(Login.this);
                if(d.getWindow()!=null)
                    d.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                d.setContentView(R.layout.custom_progress);

                d.setCancelable(false);
                d.show();
                JsonObjectRequest req = new JsonObjectRequest(getString(R.string.base_url)+getString(R.string.logsin_url), new JSONObject(login), new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            d.dismiss();
                            String userId = "";
                            String email = "";
                            Log.e("login_responseSuccess",response.toString());
                            Toast.makeText(Login.this, "Login Successful", Toast.LENGTH_SHORT).show();
                            try {
                                userId += response.getJSONObject("data").getString("userId");
                                email += response.getJSONObject("data").getString("email");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Intent intent = new Intent();
                            intent.putExtra("userId",userId.toString());
                            intent.putExtra("email",email.toString());
                            setResult(RESULT_OK,intent);
                            finish();
                        }
                    },new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error)
                        {
                            d.dismiss();
                            Log.e("LoginError",error.toString());
                            String err_string = "";
                            AlertDialog.Builder ab = new AlertDialog.Builder(Login.this);
                            ab.setTitle("Login Failed");

                            Toast.makeText(Login.this, "In the response Error", Toast.LENGTH_SHORT).show();
                            try {

                                Log.e("Login_errorRaw", String.valueOf(new JSONObject(new String(error.networkResponse.data))));
                                JSONObject res = new JSONObject(new String(error.networkResponse.data));
                                if(res.has("message")){
                                    if(res.getString("message").equals("failure")){
                                        err_string = "Username or Password not correct";
                                        ab.setMessage(err_string);
                                    }

                                }
                                else{
                                    err_string = ValidationResolver.getErrors(res);
                                    ab.setMessage(err_string);
                                }
                            }
                            catch (JSONException e) {
                                e.printStackTrace();
                            }
                            ab.setPositiveButton("ok",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface arg0, int arg1) {
                                            Log.i("AlertDialogue","Positive response pressed");
                                        }
                                    });
                            AlertDialog a = ab.create();
                            a.show();
                        }
                });
                requestQueue.add(req);

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(OPEN_REGISTRATION);
                finish();
            }
        });
    }


}