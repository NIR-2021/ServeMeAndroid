package com.example.serveme_m1.resolvers;

import android.util.Log;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class ValidationResolver {

    public static String getErrors(JSONObject js) {
        String err_String = "";
        try {
        JSONObject err = js.getJSONObject("errors");
        Log.e("ErrorsList:", err.toString());

        for (Iterator<String> it = err.keys(); it.hasNext(); ) {
            String key = it.next();
            JSONArray ar = err.getJSONArray(key);
            for (int i = 0; i < ar.length(); i++) {
                err_String += ar.get(i);
                err_String += "\n";

            }
        }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return err_String;
    }
}
