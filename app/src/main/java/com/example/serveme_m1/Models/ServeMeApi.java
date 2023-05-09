package com.example.serveme_m1.Models;

import com.example.serveme_m1.Models.RequestModels.CancelRequest;
import com.example.serveme_m1.Models.RequestModels.GetServiceRequest;
import com.example.serveme_m1.Models.ResponseModels.ResponseTemp;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ServeMeApi {
    @GET("api/User")
    Call<JsonObject> check();

    @GET("api/User")
    Call<JsonArray> check1();

    @POST("api/Service/cancelRequest")
    Call<ResponseTemp> cancelRequest(@Body CancelRequest body);

    @POST("api/Service/getPendingServices")
    Call<ResponseTemp<ServiceModel>> getPendingRequests(@Body GetServiceRequest body);

}
