package com.example.kangaroonew;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.util.Log;
import androidx.annotation.Nullable;
import com.example.kangaroonew.models.AppointmentClass;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class AppointmentStatus extends IntentService {

    JsonApiPlaceholder jsonPlaceHolder;


    public AppointmentStatus() {


        super("AppointmentStatus");

    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        int userid=new Home().userID;

        Gson gson =new GsonBuilder().serializeNulls().create(); //makes gson take into account nulls when they are mentioned
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://kangaroobackend.herokuapp.com/api/")
                .addConverterFactory(GsonConverterFactory.create(gson))
//                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        //create interface reference
        jsonPlaceHolder=retrofit.create(JsonApiPlaceholder.class);


        while (true){
            Call<AppointmentClass> appointmentCall=jsonPlaceHolder.appointmentStatus(1);    //this have to be dynamic


            appointmentCall.enqueue(new Callback<AppointmentClass>() {


                @Override
                public void onResponse(Call<AppointmentClass> call, Response<AppointmentClass> response) {
                    if(response.isSuccessful()){
                        Log.d("sdf","response is "+response.body().getStatus());
                    }
                }

                @Override
                public void onFailure(Call<AppointmentClass> call, Throwable t) {

                }
            });

            try {
                Thread.sleep(300000);


            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }

    }
}
