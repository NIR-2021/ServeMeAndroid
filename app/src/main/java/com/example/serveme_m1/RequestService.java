package com.example.serveme_m1;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.collection.ArraySet;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.example.serveme_m1.resolvers.SharedPref;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class RequestService extends AppCompatActivity {

    AutoCompleteTextView category;
    TextInputEditText description,bid;
    Button submit,cancel;
    RequestQueue requestQueue;
    private ArraySet<String> dropdownAdapter = new ArraySet<String>();
    Map<String, String> params = new HashMap<String,String>();
    private Map<String, String> appData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_service);
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        init_view();
        Intent i = getIntent();
        if(i.getStringExtra("Category")!=null){
            category.setText(i.getStringExtra("Category"));
        }

        on_clicks();


    }

    private boolean validate_view() {
        boolean validated = true;
        if(category.getText().toString().toLowerCase(Locale.ROOT).equals("category")){
            category.setError("Please select a category");
            validated = false;
        }
        else if (description.length() == 0){
            description.setError("This field is required.");
            validated = false;
        }
        else if(bid.length() ==0){
            bid.setError("This field is required.");
            validated = false;
        }

        return validated;
    }

    private void on_clicks() {
        submit.setOnClickListener(new View.OnClickListener() {
            Integer categoryId;

            @Override
            public void onClick(View view) {
                if(validate_view()){
                Toast.makeText(RequestService.this, category.getText().toString(), Toast.LENGTH_SHORT).show();
                    params.put("name",category.getText().toString());
                    JsonObjectRequest getCat = new JsonObjectRequest(Request.Method.POST, getString(R.string.base_url)+getString(R.string.getCategory), new JSONObject(params), new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.e("GetCategory",response.toString());
                            if(response.has("message")){
                                try {
                                    if(response.getString("message").toLowerCase(Locale.ROOT).equals("success")){
                                      JSONObject data = response.getJSONObject("data");
                                      categoryId = data.getInt("categoryId");
                                      submit_req(categoryId,category.getText().toString());
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
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void submit_req(Integer categoryId,String categoryName) {
        params.clear();
        Toast.makeText(this, "Submit Reqeusting", Toast.LENGTH_SHORT).show();
        params.put("Id",appData.get("userId"));
        params.put("description",description.getText().toString());
        params.put("bid",bid.getText().toString());
        params.put("categoryId",categoryId.toString());

        JsonObjectRequest quote = new JsonObjectRequest(Request.Method.POST, getString(R.string.base_url) + getString(R.string.getPreRequestQuotation), new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if(response.has("message")){
                    try {
                        if(response.getString("message").toLowerCase(Locale.ROOT).equals("success")){
                            JSONObject data = response.getJSONObject("data");
                            ViewGroup viewGroup = findViewById(android.R.id.content);
                            AlertDialog.Builder builder = new AlertDialog.Builder(RequestService.this);
                            View view1  = LayoutInflater.from(getApplicationContext()).inflate(R.layout.custom_alert_dialog,viewGroup,false);
                            builder.setCancelable(false);
                            builder.setView(view1);

                            TextView total = view1.findViewById(R.id.total);
                            TextView tax = view1.findViewById(R.id.tax);
                            TextView subtotal = view1.findViewById(R.id.subtotal);
                            TextView category1 = view1.findViewById(R.id.categoryDetails);
                            TextView description = view1.findViewById(R.id.descriptionDetails);
                            Button place = view1.findViewById(R.id.btnPlace);
                            Button cancel = view1.findViewById(R.id.btnCancel);

                            total.setText(data.getString("total"));
                            tax.setText(data.getString("tax"));
                            subtotal.setText(data.getString("bid"));
                            category1.setText(categoryName);
                            description.setText(data.getString("description"));

                            AlertDialog alertDialog = builder.create();

                            place.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    commit_request(params);
                                    alertDialog.dismiss();
                                }
                            });
                            cancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Toast.makeText(RequestService.this, "Cancel pressed", Toast.LENGTH_SHORT).show();
                                    alertDialog.dismiss();
                                }
                            });

                            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            alertDialog.show();

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
        requestQueue.add(quote);

    }

    private void commit_request(Map<String,String> params ){
        JsonObjectRequest submit = new JsonObjectRequest(Request.Method.POST, getString(R.string.base_url)+getString(R.string.requestService), new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if(response.has("message")){
                    try {
                        if(response.getString("message").toLowerCase(Locale.ROOT).equals("success")){
                            Toast.makeText(RequestService.this, "Request saved", Toast.LENGTH_SHORT).show();
                            finish();
                        }else{
                            Toast.makeText(RequestService.this, "Request Failed", Toast.LENGTH_SHORT).show();
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

            }
        }
        );

        requestQueue.add(submit);
    }

    private void init_view() {
        category = (AutoCompleteTextView) findViewById(R.id.category);
        description = (TextInputEditText) findViewById(R.id.description);
        bid = (TextInputEditText) findViewById(R.id.bid);
        submit = (Button) findViewById(R.id.submit);
        cancel = (Button) findViewById(R.id.cancel);
        appData = SharedPref.getInstance(getApplicationContext()).getSharedPred();

        JsonObjectRequest request = new JsonObjectRequest(JsonRequest.Method.POST, getString(R.string.base_url) + getString(R.string.getCategories), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("CategoryResponse", response.toString());
                        if (response.has("message")) {
                            try {
                                if (response.getString("message").toLowerCase(Locale.ROOT).equals("success")) {
                                    Toast.makeText(getApplicationContext(), "On response", Toast.LENGTH_SHORT).show();
                                    JSONArray data = response.getJSONArray("data");
                                    ObjectMapper mapper = new ObjectMapper();
                                    for (int i = 0; i < data.length(); i++) {
                                        JSONObject item = data.getJSONObject(i);
                                        Log.e("Item data", item.toString());
                                        Log.e("items Details",item.getString("name"));
                                        dropdownAdapter.add(item.getString("name"));
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            String[] items = new String[dropdownAdapter.size()];
                            items = dropdownAdapter.toArray(new String[dropdownAdapter.size()]);
                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(),
                                    R.layout.dropdown_list,
                                    items);
                            category.setAdapter(arrayAdapter);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {}
        }
        );
        try {
            Log.e("CategoryResponse",request.getHeaders().toString());
        } catch (AuthFailureError e) {
            e.printStackTrace();
        }

        requestQueue.add(request);
    }
}