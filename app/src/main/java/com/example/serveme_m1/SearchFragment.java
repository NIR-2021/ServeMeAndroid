package com.example.serveme_m1;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.leanback.widget.SearchBar;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ArrayList<CategioryModel> categories = new ArrayList<CategioryModel>();
    ArrayList<String> dropdownAdapter = new ArrayList<String>() ;
    AutoCompleteTextView dropdown ;
    RequestQueue requestQueue;
    SearchView test;
    String selected_item;
    FloatingActionButton search;

    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);
//        dropdown = (AutoCompleteTextView) view.findViewById(R.id.dropdown);
        requestQueue = Volley.newRequestQueue(getContext());
//        getData();
//        String[] items = dropdownAdapter.toArray(new String[dropdownAdapter.size()]);

//        Log.e("adapter count",dropdownAdapter.toString());

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        test = (SearchView) view.findViewById(R.id.search_bar);
        search = view.findViewById(R.id.fab);

        init_onclick(view);

    }

    private void init_onclick(View view) {

        test.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                update_query_list(newText);
                Toast.makeText(getContext(), newText, Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open an activity that deals with sending a request
                Intent intent = new Intent(getContext(),RequestService.class);
                startActivity(intent);
            }
        });

    }

    private void update_query_list(String newText) {
        //create a api reqeust and add the data to the recycler view
    }

    private void getData() {



        JsonObjectRequest request = new JsonObjectRequest(JsonRequest.Method.POST, getString(R.string.base_url) + getString(R.string.getCategories), null,
        new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("CategoryResponse", response.toString());
                if (response.has("message")) {
                    try {
                        if (response.getString("message").toLowerCase(Locale.ROOT).equals("success")) {
                            Toast.makeText(getContext(), "On response", Toast.LENGTH_SHORT).show();
                            JSONArray data = response.getJSONArray("data");
                            ObjectMapper mapper = new ObjectMapper();
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject item = data.getJSONObject(i);
                                Log.e("Item data", item.toString());
                                categories.add(new CategioryModel(item.getInt("categoryId"), item.getString("name")));
                                Log.e("items Details",item.getString("name"));
                                dropdownAdapter.add(item.getString("name"));
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String[] items = new String[dropdownAdapter.size()];
                    items = dropdownAdapter.toArray(new String[dropdownAdapter.size()]);
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(),
                            R.layout.dropdown_list,
                            items);
                    dropdown.setAdapter(arrayAdapter);
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