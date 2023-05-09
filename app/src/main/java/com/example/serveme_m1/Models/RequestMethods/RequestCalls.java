package com.example.serveme_m1.Models.RequestMethods;

import android.content.Context;
import android.util.Log;

import com.example.serveme_m1.Models.ApiRetrofit;
import com.example.serveme_m1.Models.RequestModels.GetServiceRequest;
import com.example.serveme_m1.Models.ResponseModels.ResponseTemp;
import com.example.serveme_m1.Models.ServiceModel;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;

public class RequestCalls {
    public static ArrayList<ServiceModel> getPendingRequesets(String id, Context context){
        Log.e("RequestParam",id);
        ArrayList<ServiceModel> list = new ArrayList<ServiceModel>();
        GetServiceRequest request_data = new GetServiceRequest(id);
        Call<ResponseTemp<ServiceModel>> call  = ApiRetrofit
                .getCall().getPendingRequests(request_data);
        NewThread t = new NewThread(call);
        Thread thread = new Thread(t);
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

//        call.enqueue(new Callback<ResponseTemp<ServiceModel>>() {
//            @Override
//            public void onResponse(Call<ResponseTemp<ServiceModel>> call, Response<ResponseTemp<ServiceModel>> response) {
//                Log.e("API Retrofit","In on response ");
//                Log.e("getPendingReqRetrof","Data"+new String(response.body().getData().toString()));
//                list.addAll(response.body().getData());
//
//            }
//
//            @Override
//            public void onFailure(Call<ResponseTemp<ServiceModel>> call, Throwable t) {
//                Log.e("API Response","On Failure");
//            }
//        });
        Log.e("RequestCall","Request sent "+t.result.toString());
        return t.result;
    }
    private static class NewThread implements Runnable {

        private Call<ResponseTemp<ServiceModel>> call;
        ArrayList<ServiceModel> result= new ArrayList<ServiceModel>();

        public NewThread(Call<ResponseTemp<ServiceModel>> call) {

            this.call = call;
        }

        @Override
        public void run() {
            try {
                Response<ResponseTemp<ServiceModel>> response = call.execute();
                result.addAll(response.body().getData());

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}


