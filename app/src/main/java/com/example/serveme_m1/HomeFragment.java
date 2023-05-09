package com.example.serveme_m1;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;
import com.example.serveme_m1.Models.CategioryModel;
import com.example.serveme_m1.Models.ServiceModel;
import com.example.serveme_m1.resolvers.SharedPref;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    RecyclerView service_listing;
    ServiceLIstingAdapter adapter ;

    ArrayList<ServiceModel> services = new ArrayList<ServiceModel>() ;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     *
     * @return A new instance of fragment HomeFragment.
     */
    CardView ongoingTest ;
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onClick(View v) {
    Toast.makeText(getContext(), "Onclick pressed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        create_onCatOnclicks(view);

        service_listing = (RecyclerView) view.findViewById(R.id.service_listing);
        LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        service_listing.setLayoutManager(horizontalLayoutManagaer);

        Map<String,String> appData = SharedPref.getInstance(getContext()).getSharedPred();

        if(appData.get("isLoggedIn").toLowerCase(Locale.ROOT).equals("yes")){
            Dialog pb = new Dialog(getContext());

            if(pb.getWindow()!=null)
                pb.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            pb.setContentView(R.layout.custom_progress);
            pb.show();
            Toast.makeText(getContext(), "Calling api ", Toast.LENGTH_SHORT).show();
            RequestQueue requestQueue = Volley.newRequestQueue(this.getContext());
            RequestFuture<JSONObject> future = RequestFuture.newFuture();
            Map<String,String> params = new HashMap<String,String>();
            params.put("Id",appData.get("userId"));

            JsonObjectRequest request = new JsonObjectRequest(JsonRequest.Method.POST, getString(R.string.base_url) + getString(R.string.getPendingServices), new JSONObject(params), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.e("ServiceLIstingResponse",response.toString());
                    Gson gson = new Gson();
                    if(response.has("message")){
                        try {
                            if(response.getString("message").toLowerCase(Locale.ROOT).equals("success")){
                                JSONArray data = response.getJSONArray("data");

                                for(int i =0 ; i < data.length() ;i++) {
                                    if (data.get(i) instanceof JSONObject) {
                                        JSONObject t = data.getJSONObject(i);
                                        ObjectMapper mapper = new ObjectMapper();
                                        ServiceModel temp =  new ServiceModel();
                                        temp.setServiceId(t.getInt("serviceId"));
                                        temp.setDescription(t.getString("description"));
                                        temp.setId(t.getString("id"));
                                        if(t.isNull("vendorId")){
                                            Log.e("VendorId","null");
                                            temp.setVendorId(0);

                                        }
                                        else{
                                            temp.setVendorId(t.getInt("vendorId"));
                                        }
                                        temp.setBid(Float.parseFloat(t.get("bid").toString()));
                                        temp.setCategoryId(t.getInt("categoryId"));
                                        temp.setStatus(t.getString("status"));
                                        CategioryModel tempC = new CategioryModel(t.getJSONObject("category").getInt("categoryId"),t.getJSONObject("category").getString("name"));
                                        temp.setCategory(tempC);
                                        Log.e("Listing item",temp.toString());
                                        services.add(temp);

                                    }
                                }
                                adapter = new ServiceLIstingAdapter(getContext(),services);
                                Log.e("Services list count ", String.valueOf(services.size()));
                                service_listing.setAdapter(adapter);


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                pb.dismiss();

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("HomeFragmentError",new String(error.toString()));
                }
            }
            );
            Log.e("Requet Data", new String(request.getBody()));
            requestQueue.add(request);

        }
        else{
            //hide what id needed to hide

        }


        //End of API call
        Toast.makeText(this.getContext(), "Create complete", Toast.LENGTH_SHORT).show();

        return view;
    }

    private void create_onCatOnclicks(View view) {
        for(int i = 1; i<=9;i++){
            final int ni = i;
            int idView = getResources().getIdentifier("category"+i,"id",getContext().getPackageName());
            View eventView = view.findViewById(idView);
            eventView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int subview =getResources().getIdentifier("categoryText"+ni,"id",getContext().getPackageName());
                    TextView textView = (TextView) view.findViewById(subview);
//                    ViewGroup row = (ViewGroup) view.g;
                    Toast.makeText(getContext(), textView.getText(), Toast.LENGTH_SHORT).show();
                    Intent i  = new Intent(getContext(),RequestService.class);
                    i.putExtra("Category",textView.getText());
                    startActivity(i);
                }
            });
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        Toast.makeText(getContext(), "Home fragment on resume", Toast.LENGTH_SHORT).show();
            Log.i("DatasetNotifier","Inside if. Adapter not null");
            services.clear();
            services.addAll(getServiceListing(this.getContext()));
            this.adapter.notifyDataSetChanged();

    }

    ArrayList<ServiceModel> getServiceListing(Context context){
        Map<String,String> appData = SharedPref.getInstance(getContext()).getSharedPred();
        ArrayList<ServiceModel> tempresult = new ArrayList<ServiceModel>();
        if(appData.get("isLoggedIn").toLowerCase(Locale.ROOT).equals("yes")){
            Dialog pb = new Dialog(getContext());

            if(pb.getWindow()!=null)
                pb.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            pb.setContentView(R.layout.custom_progress);
            pb.show();
            Toast.makeText(getContext(), "Calling api ", Toast.LENGTH_SHORT).show();
            RequestQueue requestQueue = Volley.newRequestQueue(this.getContext());
            RequestFuture<JSONObject> future = RequestFuture.newFuture();
            Map<String,String> params = new HashMap<String,String>();
            params.put("Id",appData.get("userId"));

            JsonObjectRequest request = new JsonObjectRequest(JsonRequest.Method.POST, getString(R.string.base_url) + getString(R.string.getPendingServices), new JSONObject(params), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.e("ServiceLIstingResponse",response.toString());
                    Gson gson = new Gson();
                    if(response.has("message")){
                        try {
                            if(response.getString("message").toLowerCase(Locale.ROOT).equals("success")){
                                JSONArray data = response.getJSONArray("data");

                                for(int i =0 ; i < data.length() ;i++) {
                                    if (data.get(i) instanceof JSONObject) {
                                        JSONObject t = data.getJSONObject(i);
                                        ObjectMapper mapper = new ObjectMapper();
                                        ServiceModel temp =  new ServiceModel();
                                        temp.setServiceId(t.getInt("serviceId"));
                                        temp.setDescription(t.getString("description"));
                                        temp.setId(t.getString("id"));
                                        if(t.isNull("vendorId")){
                                            Log.e("VendorId","null");
                                            temp.setVendorId(0);

                                        }
                                        else{
                                            temp.setVendorId(t.getInt("vendorId"));
                                        }
                                        temp.setBid(Float.parseFloat(t.get("bid").toString()));
                                        temp.setCategoryId(t.getInt("categoryId"));
                                        temp.setStatus(t.getString("status"));
                                        CategioryModel tempC = new CategioryModel(t.getJSONObject("category").getInt("categoryId"),t.getJSONObject("category").getString("name"));
                                        temp.setCategory(tempC);
                                        Log.e("Listing item",temp.toString());
                                        tempresult.add(temp);

                                    }
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
                    Log.e("HomeFragmentError",new String(error.toString()));
                }
            }
            );
            Log.e("Requet Data", new String(request.getBody()));
            requestQueue.add(request);

        }

        return tempresult;

    }
}