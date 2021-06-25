package com.example.kangaroonew;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kangaroonew.insideInbox.TipsDetails;
import com.example.kangaroonew.models.BabyGrowth;
import com.example.kangaroonew.models.PregnancyDate;
import com.example.kangaroonew.models.Tip;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Home extends AppCompatActivity {

    public int userID;      //loggedIn user id
    ImageButton inboxBtn;
    ImageButton appointmentBtn;
    ImageButton feedbackBtn;
    ImageButton shareBtn;
    SharedPreferences app_preferences;
    TextView textView4;
    private ProgressDialog progressBar;

    MaterialToolbar topAppBar;

    JsonApiPlaceholder jsonPlaceHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        topAppBar=(MaterialToolbar)findViewById(R.id.topAppBar);
        setSupportActionBar(topAppBar);
        textView4=findViewById(R.id.textView4);
        textView4.setText("Now it is week ");


       inboxBtn=(ImageButton)findViewById(R.id.inboxBtn);
        appointmentBtn=(ImageButton)findViewById(R.id.appointmentBtn);
        feedbackBtn=(ImageButton)findViewById(R.id.feedbackBtn);
        shareBtn=(ImageButton)findViewById(R.id.shareBtn);

        this.userID=getIntent().getIntExtra("userID",0);
//        startCheckingAppointmentStatus();

        Gson gson =new GsonBuilder().serializeNulls().create(); //makes gson take into account nulls when they are mentioned
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://kangaroobackend.herokuapp.com/api/")
                .addConverterFactory(GsonConverterFactory.create(gson))
//                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        progressBar=new ProgressDialog(this);

        jsonPlaceHolder=retrofit.create(JsonApiPlaceholder.class);

        getHomeBabyGrowth();


        feedbackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToFeedbackActivity();
            }
        });
        inboxBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToInboxActivity();
            }
        });

        appointmentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToAppointmentActivity();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu2,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.logout){

            Intent logoutIntent =new Intent(this,MainActivity.class);
            logoutIntent.putExtra("logout","outt");


            startActivity(logoutIntent);
            finish();
            //codes to logout
        }


        return super.onOptionsItemSelected(item);
    }

    private void sendToAppointmentActivity() {
        Intent appointmentIntent=new Intent(this,Appointment.class);
appointmentIntent.putExtra("userID",this.userID);
        startActivity(appointmentIntent);
    }

    private void sendToInboxActivity() {
        Intent inboxIntent=new Intent(this,Inbox.class);
        inboxIntent.putExtra("userID",this.userID);
        startActivity(inboxIntent);
    }

    private void sendToFeedbackActivity() {
        Intent feedbackIntent=new Intent(this,Feedback.class);

        startActivity(feedbackIntent);
    }

    public int getWeekDiff(Date date2){

        Date date1= Calendar.getInstance().getTime();;
        long diff = date1.getTime() - date2.getTime();
        long seconds = diff / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;
        long weeks=days/7;
        if(weeks<1){    //if weeks are less than 1 return 1
            weeks=1;
        }
        if(weeks>40){
            weeks=40;
        }

        return (int) weeks;
    }

    public void getHomeBabyGrowth(){
    final Call<PregnancyDate> pregDate=jsonPlaceHolder.getPregDate(userID);
        pregDate.enqueue(new Callback<PregnancyDate>() {
        @Override
        public void onResponse(Call<PregnancyDate> call, Response<PregnancyDate> response) {
            if(response.isSuccessful()){

                String date=response.body().getPregnancyDate();


                try {
                    Date date1=new SimpleDateFormat("yyyy-MM-dd").parse(date);

                    int weekNo= getWeekDiff(date1);

                    Call<BabyGrowth> tip=jsonPlaceHolder.getGrowth(weekNo);
                    tip.enqueue(new Callback<BabyGrowth>() {
                        @Override
                        public void onResponse(Call<BabyGrowth> call, Response<BabyGrowth> response) {
                            Log.d("j","date is: "+response.toString() );
                            if(response.isSuccessful()){
                                String tipp=response.body().getStage();
                                Log.d("j","date is:"+ tipp);
                                textView4.append(String.valueOf(weekNo));
                                textView4.append("\nYour  baby is as big as "+ tipp);
                                textView4.append("\n Countdown "+ String.valueOf(38-weekNo) +" left");
                            }
                        }

                        @Override
                        public void onFailure(Call<BabyGrowth> call, Throwable t) {
                            Log.d("j","date is:"+ "tipp2");
                            Toast.makeText(Home.this,"Failed to load, error occured!",Toast.LENGTH_LONG).show();
                        }
                    });



                } catch (ParseException e) {
                    e.printStackTrace();
                }


            }
            progressBar.dismiss();
        }

        @Override
        public void onFailure(Call<PregnancyDate> call, Throwable t) {

            progressBar.dismiss();
            Toast.makeText(Home.this,"Failed to load, error occured!",Toast.LENGTH_LONG).show();
        }
    });

}

    private void startCheckingAppointmentStatus() {
//        Log.d("df","going to"+  userID);
//        Intent appointmentStatus=new Intent(this,AppointmentStatus.class);
//        appointmentStatus.putExtra("userID",this.userID);

//        startService(appointmentStatus);
    }

}

