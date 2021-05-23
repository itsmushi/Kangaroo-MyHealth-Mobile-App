package com.example.kangaroonew;

import android.app.ProgressDialog;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ListView;
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

import java.util.ArrayList;
import java.util.List;

public class InboxDetails extends AppCompatActivity {
    ListView list;
    JsonApiPlaceholder jsonPlaceHolder;
    int userID;
    private ProgressDialog progressBar;



    ArrayList<String> dateList = new ArrayList<String>();
    ArrayList<String> subtitleList = new ArrayList<String>();
    ArrayList<String> subtitleList2=new ArrayList<String>();

    String[] date={"2012-03-20","2020-11-03"};
    String[] subtitle={"doc is hosp is iin","doc \n agin, staff sdjk"};
    String[] subtitle2={"more info","agaoin morenoww"};
    String[] date2;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox_details);
        userID=getIntent().getExtras().getInt("userID");


        Gson gson =new GsonBuilder().serializeNulls().create(); //makes gson take into account nulls when they are mentioned
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://kangaroobackend.herokuapp.com/api/")
                .addConverterFactory(GsonConverterFactory.create(gson))
//                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();


        final MyListView adapter=new MyListView(InboxDetails.this, date, subtitle,subtitle2);
        list=(ListView)findViewById(R.id.list);
        list.setAdapter(adapter);



        //create interface reference
        jsonPlaceHolder=retrofit.create(JsonApiPlaceholder.class);


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
                        dateList.add(appointment.getDate());
                        subtitleList.add("Appointment at "+appointment.getHospitalId()+" attended with "+appointment.getStaffId());
                        subtitleList2.add("Summary: "+appointment.getStatus());

                    }

//                    date2 = new String[dateList.size()];
//                    dateList.toArray(date2);

//                    date = new String[dateList.size()];
//                    dateList.toArray(date);
//
//                    subtitle=new String[subtitleList.size()];
//                    subtitleList.toArray(subtitle);
//
//                    subtitle2=new String[subtitleList2.size()];
//                    subtitleList2.toArray(subtitle2);




                }
                progressBar.dismiss();
            }

            @Override
            public void onFailure(Call<List<AppointmentClass>> call, Throwable t) {

                Toast.makeText(InboxDetails.this,"Failed to load, error occured!",Toast.LENGTH_LONG).show();

            }

        });





    }

    private void checkAppointments(final Context context) {
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
                        dateList.add(appointment.getDate());
                        subtitleList.add("Appointment at "+appointment.getHospitalId()+" attended with "+appointment.getStaffId());
                        subtitleList2.add("Summary: "+appointment.getStatus());
                    }

                    date = new String[dateList.size()];
                    dateList.toArray(date);

                    subtitle=new String[subtitleList.size()];
                    subtitleList.toArray(subtitle);

                    subtitle2=new String[subtitleList2.size()];
                    subtitleList2.toArray(subtitle2);



                }
                progressBar.dismiss();
            }

            @Override
            public void onFailure(Call<List<AppointmentClass>> call, Throwable t) {

                Toast.makeText(InboxDetails.this,"Failed to load, error occured!",Toast.LENGTH_LONG).show();

            }
        });
    }
}
