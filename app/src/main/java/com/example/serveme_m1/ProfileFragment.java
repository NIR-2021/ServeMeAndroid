package com.example.serveme_m1;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.example.serveme_m1.resolvers.SharedPref;

import org.joda.time.DateTime;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    TextView name,email,address,phone;
    RelativeLayout profile_layout;
    RelativeLayout not_loggin;
    Button profile_login,logout,favs,orders,rewards,registerAsVendor;
    int LOGIN = 1;
    int REGISTRATION = 0 ;
    int OPEN_REGISTRATION =201;
    Map<String, String> appData;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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

        appData = SharedPref.getInstance(this.getContext()).getSharedPred();
        Log.e("appData At profile",appData.toString());

    }

    private void load_profile_data() {

        RequestQueue requestQueue = Volley.newRequestQueue(this.getContext());
        Map<String,String> params = new HashMap<String,String>();
        params.put("email",appData.get("email"));
        params.put("Id",appData.get("userId"));

        Dialog d = new Dialog(getContext());
        if(d.getWindow()!=null)
            d.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        d.setContentView(R.layout.custom_progress);
        d.show();


        JsonObjectRequest request = new JsonObjectRequest(JsonRequest.Method.POST, getString(R.string.base_url)+getString(R.string.getProfileDetails), new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response){
                Log.e("profileFragmentResponse",response.toString());
                try {
                    if(response.getString("message").toLowerCase(Locale.ROOT).equals("failure")){
                        //logout and request login again
                    }
                    else{
                        JSONObject data = response.getJSONObject("data");
                        //print details on profile
                        name.setText(data.getString("firstName").toUpperCase(Locale.ROOT)+data.getString("lastName").toUpperCase(Locale.ROOT));
                        phone.setText(data.getString("phoneNumber"));
                        address.setText(data.getString("address"));
                        email.setText(data.getString("email"));
                        Log.e("ProfileFragment","Profile setup complete");
                        d.dismiss();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ErrorAtProfile",new String(error.toString()));
                d.dismiss();
            }
        }){

        };
        requestQueue.add(request);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init_layout(view);

        if(appData.get("isLoggedIn").equals("No") ){
            Log.e("LoginStatAtProfile","not logged in");

            not_loggin.setVisibility(View.VISIBLE);
            profile_layout.setVisibility(View.GONE);
            logout.setVisibility(View.GONE);
            registerAsVendor.setVisibility(View.GONE);
//            rewards.setVisibility(View.GONE);
//            favs.setVisibility(View.GONE);
//            orders.setVisibility(View.GONE);

        }
        else{
            Log.e("LoginStatAtProfile","logged in");
            profile_layout.setVisibility(View.VISIBLE);
            not_loggin.setVisibility(View.GONE);
            load_profile_data();
        }
    }

    private void init_layout(View view) {

        name = (TextView) view.findViewById(R.id.profile);
        email = (TextView) view.findViewById(R.id.username);
        address = (TextView) view.findViewById(R.id.address);
        phone = (TextView) view.findViewById(R.id.name);
        profile_layout = view.findViewById(R.id.profile_layout_1);
        not_loggin = (RelativeLayout) view.findViewById(R.id.not_logged_layout);
        profile_login = (Button) view.findViewById(R.id.profile_login);
        logout = (Button) view.findViewById(R.id.logout);
//        rewards = (Button) view.findViewById(R.id.reward1);
//        favs = (Button) view.findViewById(R.id.favs);
//        orders = (Button) view.findViewById(R.id.orders);
        registerAsVendor = (Button) view.findViewById(R.id.regAsVendor);

        profile_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent login = new Intent(getContext(),Login.class);
                startActivityForResult(login,1);
                Fragment fragment = new HomeFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frameLayout, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Logout pressed", Toast.LENGTH_SHORT).show();
                Map<String,String> appData = new HashMap<String,String>();
                appData.put("isLoggedIn","No");
                SharedPref.getInstance(getContext()).setSharedPred(appData);
                Fragment fragment = new HomeFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frameLayout, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        registerAsVendor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(),VendorRegistration.class);
                startActivity(i);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        return view;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REGISTRATION){
            //100 : Jump user to login page
            if(resultCode == 100 || resultCode == 200){
                Intent login = new Intent(getContext(),Login.class);
                startActivityForResult(login,LOGIN);
            }
            //Just close the regisration

        }

        else if(requestCode == LOGIN){
            if(resultCode == RESULT_CANCELED){
                Log.i("Login Info","Login Cancelled.");
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
                SharedPref.getInstance(getContext()).setSharedPred(appData);

            }
            else if(resultCode == OPEN_REGISTRATION){
                startActivityForResult(new Intent(getContext(),Registration.class),REGISTRATION);
            }
        }
    }
}