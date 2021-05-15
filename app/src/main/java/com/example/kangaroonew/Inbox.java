package com.example.kangaroonew;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
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

    private TextView appointmentText;
    JsonApiPlaceholder jsonPlaceHolder;
    private ProgressDialog progressBar;

    boolean found=false;
    int userID;

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

        userID=getIntent().getExtras().getInt("userID");
        progressBar=new ProgressDialog(this);


        checkingInbox();



    }

    private void checkingInbox() {

         //checking if any of the user's appointment has been accepted
            final Call<List<AppointmentClass>> appointmentList=jsonPlaceHolder.userAppointments(userID);
            appointmentList.enqueue(new Callback<List<AppointmentClass>>() {

                @Override
                public void onResponse(Call<List<AppointmentClass>> call, Response<List<AppointmentClass>> response) {
                    progressBar.setTitle("Loading");
                    progressBar.setMessage("Please wait...");
                    progressBar.setCanceledOnTouchOutside(true);
                    progressBar.show();
                    if(response.isSuccessful()){

                        List<AppointmentClass> appointmentList=response.body();
                        for(AppointmentClass appointment: appointmentList){

                            Log.d("Ds","status is "+appointment.getStatus());
                            if(TextUtils.equals(appointment.getStatus(),"1")){//the appointment is accepted
                                Log.d("sdf","response is "+appointment.getDate());
                                String txt="Your appointment is on ";
                                appointmentText.setText(txt+appointment.getDate());
                                found=true;
                            }
                        }
                        if(!found){ //appointment not accepted
                            appointmentText.setText("There is no appointment to attend yet!");
                        }
                    }
                    progressBar.dismiss();
                }

                @Override
                public void onFailure(Call<List<AppointmentClass>> call, Throwable t) {
                    found=true;
                    Toast.makeText(Inbox.this,"Failed to load, error occured!",Toast.LENGTH_LONG).show();
                    Log.d("Ds","status is NOT HERE");
                }
            });

    }


}



