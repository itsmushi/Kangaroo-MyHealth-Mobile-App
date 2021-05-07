package com.example.kangaroonew;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import androidx.annotation.Nullable;
import com.example.kangaroonew.models.AppointmentClass;
import com.example.kangaroonew.models.Hospital;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.List;


public class AppointmentStatus extends IntentService {


    private static final String ACTION_RESP="";
    JsonApiPlaceholder jsonPlaceHolder;


    boolean found=false;


    public AppointmentStatus() {
        super("AppointmentStatus");
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        int userID=intent.getExtras().getInt("userID");

        Log.d("sdf","user id "+userID);



        Gson gson =new GsonBuilder().serializeNulls().create(); //makes gson take into account nulls when they are mentioned
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://kangaroobackend.herokuapp.com/api/")
                .addConverterFactory(GsonConverterFactory.create(gson))
//                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        //create interface reference
        jsonPlaceHolder=retrofit.create(JsonApiPlaceholder.class);

        while(true){
            final Call<List<AppointmentClass>> appointmentList=jsonPlaceHolder.userAppointments(userID);
            appointmentList.enqueue(new Callback<List<AppointmentClass>>() {
                @Override
                public void onResponse(Call<List<AppointmentClass>> call, Response<List<AppointmentClass>> response) {
                    if(response.isSuccessful()){

                        List<AppointmentClass> appointmentList=response.body();
                        for(AppointmentClass appointment: appointmentList){

                            Log.d("Ds","status is "+appointment.getStatus());
                            if(TextUtils.equals(appointment.getStatus(),"1")){   //the appointment is accepted
                            Intent broadcastIntent = new Intent();
                            broadcastIntent.setAction(AppointmentStatus.ACTION_RESP);
                            broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
                            broadcastIntent.putExtra("resp",appointment.getId());
                                Log.d("Ds","status in "+appointment.getId());

                            sendBroadcast(broadcastIntent);
//                            found=true;
//                            Log.d("SDf","broadcast sent");
//                            stopSelf();
                        }
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<AppointmentClass>> call, Throwable t) {

                }
            });

            try {
                Thread.sleep(2000);


            } catch (InterruptedException e) {
                e.printStackTrace();
            }

//            if(found){
//                break;
//            }
        }

//        stopSelf();



//        while (true){
//            Log.d("sd","user id is "+userID);
//            appointmentId=4;
//
//
//
//            Call<AppointmentClass> appointmentCall=jsonPlaceHolder.appointmentStatus(appointmentId);    //this have to be dynamic
//
//            appointmentCall.enqueue(new Callback<AppointmentClass>() {
//                @Override
//                public void onResponse(Call<AppointmentClass> call, Response<AppointmentClass> response) {
//                    if(response.isSuccessful()){
//                        if(TextUtils.equals((response.body().getStatus()),"1")){   //the appointment is accepted
//                            Intent broadcastIntent = new Intent();
//                            broadcastIntent.setAction(AppointmentStatus.ACTION_RESP);
//                            broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
//                            broadcastIntent.putExtra("resp",appointmentId);
//                            sendBroadcast(broadcastIntent);
////                            Log.d("SDf","broadcast sent");
//                            stopSelf();
//                        }
//
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<AppointmentClass> call, Throwable t) {
//
//                }
//            });
//

//
//
//        }

    }
}
