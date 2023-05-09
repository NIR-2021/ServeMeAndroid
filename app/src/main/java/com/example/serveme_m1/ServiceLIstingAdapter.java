package com.example.serveme_m1;

import static com.example.serveme_m1.R.color.red;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.serveme_m1.Models.ApiRetrofit;
import com.example.serveme_m1.Models.RequestMethods.RequestCalls;
import com.example.serveme_m1.Models.RequestModels.CancelRequest;
import com.example.serveme_m1.Models.ResponseModels.ResponseTemp;
import com.example.serveme_m1.Models.ServiceModel;
import com.example.serveme_m1.resolvers.SharedPref;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServiceLIstingAdapter extends RecyclerView.Adapter<ServiceLIstingAdapter.MyViewHolder> {

    private Context rContext;
    ArrayList<ServiceModel> dataholder;
    Dialog dialog;

    public ServiceLIstingAdapter(Context rContext,ArrayList<ServiceModel> dataholder) {
        this.rContext = rContext;
        this.dataholder = dataholder;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.service_singlerow_layout,parent,false);
        MyViewHolder vHolder = new MyViewHolder(view);

        dialog = new Dialog(rContext);
        dialog.setContentView(R.layout.custom_alert_dialog);

        vHolder.item_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView category  = dialog.findViewById(R.id.categoryDetails);
                TextView description  = dialog.findViewById(R.id.descriptionDetails);

                ServiceModel temp = new ServiceModel();
                temp = dataholder.get(vHolder.getAbsoluteAdapterPosition());
                float taxC = temp.getBid();
                Log.e("Temp Tax", String.valueOf(taxC));
                taxC = (float) (taxC * (0.15));

                ViewGroup viewGroup = dialog.findViewById(android.R.id.content);
                AlertDialog.Builder builder = new AlertDialog.Builder(rContext);
                View view1  = LayoutInflater.from(rContext).inflate(R.layout.custom_alert_dialog,viewGroup,false);
                builder.setCancelable(false);
                builder.setView(view1);

                TextView total = view1.findViewById(R.id.total);
                TextView tax = view1.findViewById(R.id.tax);
                TextView subtotal = view1.findViewById(R.id.subtotal);
                TextView category1 = view1.findViewById(R.id.categoryDetails);
                TextView description1 = view1.findViewById(R.id.descriptionDetails);
                Button place = view1.findViewById(R.id.btnPlace);
                Button cancel = view1.findViewById(R.id.btnCancel);

                total.setText(String.valueOf(temp.getBid()+taxC));
                tax.setText(String.valueOf(taxC));
                subtotal.setText(String.valueOf(temp.getBid()));
                category1.setText(temp.getCategory().getName());
                description1.setText(temp.getDescription());

                AlertDialog alertDialog = builder.create();

                cancel.setText("Close");
                place.setText("Withdraw");
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });
                ServiceModel finalTemp = temp;
                place.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CancelRequest requestData = new CancelRequest(finalTemp.getId(), finalTemp.getServiceId());
                        Toast.makeText(rContext, "Widthdraw pressed", Toast.LENGTH_SHORT).show();
                        Call<ResponseTemp> cancelRequest = ApiRetrofit.getCancelRequest().cancelRequest(requestData);
                        cancelRequest.enqueue(new Callback<ResponseTemp>() {
                            @Override
                            public void onResponse(Call<ResponseTemp> call, Response<ResponseTemp> response) {
                                if(response.isSuccessful()){
                                    if(response.body().getMessage().equals("success")){
    //                                    Log.e("responseMessage ",new String(response.toString()));
    //                                    ResponseTemp res = response.body();
    //                                    Log.e("Res.message",res.getMessage());
    //                                    alertDialog.dismiss();
                                        Toast.makeText(rContext, "Request Withdrawn", Toast.LENGTH_SHORT).show();
                                        //update the adapter
                                       dataholder = RequestCalls.getPendingRequesets(SharedPref.getInstance(rContext).getSharedPred().get("userId"),rContext);
                                       Log.e("NewAdapterData",dataholder.toString());
                                       alertDialog.dismiss();
                                       notifyDataSetChanged();
                                    }
                                    else{
                                        Toast.makeText(rContext, "Withdrew request failed.", Toast.LENGTH_SHORT).show();
                                    }

                                }
                                else{
                                    Toast.makeText(rContext, "Error returned from api", Toast.LENGTH_SHORT).show();
                                    alertDialog.dismiss();
                                }

                            }

                            @Override
                            public void onFailure(Call<ResponseTemp> call, Throwable t) {
                                Toast.makeText(rContext , "Failure Calling the API", Toast.LENGTH_SHORT).show();
                                Log.e("cancelRequest","Failure to call the api "+t.getMessage());

                            }
                        });
                    }
                });

                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialog.show();

            }
        });

        return vHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.category.setText(dataholder.get(position).getCategory().getName());
//        holder.date.setText(dataholder.get(position).getD);
        holder.description.setText(dataholder.get(position).getDescription());
        if(dataholder.get(position).getStatus().equals("pending")){
            holder.status.setText("pending");
            holder.status.setTextColor(Color.parseColor("#FFA500"));
        }
        else if(dataholder.get(position).getStatus().toLowerCase(Locale.ROOT).equals("cancelled")){
            holder.status.setText("Cancelled");
            holder.status.setTextColor(Color.parseColor(String.valueOf("#F41A1A")));
        }
        else if(dataholder.get(position).getStatus().toLowerCase(Locale.ROOT).equals("accepted")){
            holder.status.setText("Accepted");
            holder.status.setTextColor(Color.parseColor("#4CAF50"));
        }


    }

    @Override
    public int getItemCount() {
        return dataholder.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView category,date,description,status;
        CardView item_content;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            item_content = itemView.findViewById(R.id.ongoing1);
            category = itemView.findViewById(R.id.category);
            date = itemView.findViewById(R.id.date);
            description = itemView.findViewById(R.id.description);
            status = itemView.findViewById(R.id.status);


        }
    }


}
