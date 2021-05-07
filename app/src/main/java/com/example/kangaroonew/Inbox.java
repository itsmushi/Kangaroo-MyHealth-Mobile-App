package com.example.kangaroonew;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.kangaroonew.models.AppointmentClass;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.List;

import static java.lang.Integer.parseInt;

public class Inbox extends AppCompatActivity {
    private ResponseReceiver receiver;
    private TextView appointmentText;
    JsonApiPlaceholder jsonPlaceHolder;
    AppointmentClass appointmentClass=new AppointmentClass();
    boolean found=false;
    int userID=14;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);

        appointmentText=findViewById(R.id.appointmentText);

        Gson gson =new GsonBuilder().serializeNulls().create(); //makes gson take into account nulls when they are mentioned
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://kangaroobackend.herokuapp.com/api/")
                .addConverterFactory(GsonConverterFactory.create(gson))
//                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        //create interface reference
        jsonPlaceHolder=retrofit.create(JsonApiPlaceholder.class);

        checkingInbox();
//        IntentFilter filter = new IntentFilter(ResponseReceiver.ACTION_RESP);
//        filter.addCategory(Intent.CATEGORY_DEFAULT);
//        receiver = new ResponseReceiver();
//        registerReceiver(receiver, filter);



    }

    private void checkingInbox() {

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
                                Log.d("sdf","response is "+appointment.getDate());
                                String txt=appointmentText.getText().toString();
                                appointmentText.setText(txt+appointment.getDate());
                                found=true;

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
                Log.d("SDf","waiting again");

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if(found){
                break;
            }
        }
    }


    public class ResponseReceiver extends BroadcastReceiver {
        public static final String ACTION_RESP ="";


        @Override
        public void onReceive(Context context, Intent intent) {

            final Call<AppointmentClass> appointmentClassCall=jsonPlaceHolder.oneAppointment(1);

            appointmentClassCall.enqueue(new Callback<AppointmentClass>() {
                @Override
                public void onResponse(Call<AppointmentClass> call, Response<AppointmentClass> response) {

                    if(response.isSuccessful()){

                        appointmentClass.setDate(response.body().getDate());
                        Log.d("sdf","response is "+response.body().getDate());
                        String txt=appointmentText.getText().toString();
                        appointmentText.setText(txt+appointmentClass.getDate());
//                        unregisterReceiver(receiver);d
                    }
                }

                @Override
                public void onFailure(Call<AppointmentClass> call, Throwable t) {

                }
            });

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }
}



