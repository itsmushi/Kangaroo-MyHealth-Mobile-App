package com.example.kangaroonew;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.kangaroonew.insideInbox.AppointmentDetails;
import com.example.kangaroonew.insideInbox.DosageDetails;
import com.example.kangaroonew.insideInbox.MyHeathDetails;
import com.example.kangaroonew.insideInbox.RecomendationDetails;
import com.example.kangaroonew.insideInbox.TipsDetails;
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
    private TextView myhealthText;
    private TextView dosageText;
    private TextView recommendationText;
    private TextView tipsText;
    JsonApiPlaceholder jsonPlaceHolder;
    private ProgressDialog progressBar;
    private LinearLayout appointmentLayout;

    boolean found=false;
    int userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);

        appointmentText=findViewById(R.id.appointmentText);
        myhealthText=findViewById(R.id.myhealthtext);
        dosageText=findViewById(R.id.dosagetext);
        tipsText=findViewById(R.id.tipstext);
        recommendationText=findViewById(R.id.recommendationtext);

        Gson gson =new GsonBuilder().serializeNulls().create(); //makes gson take into account nulls when they are mentioned
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://kangaroobackend.herokuapp.com/api/")
                .addConverterFactory(GsonConverterFactory.create(gson))
//                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        //create interface reference
        jsonPlaceHolder=retrofit.create(JsonApiPlaceholder.class);

        appointmentLayout=findViewById(R.id.appointmentLayout);

        appointmentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToAppointmentDetails();
            }


        });
        myhealthText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToMyHealthDetails();
            }
        });

        recommendationText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToRecommendationDetails();
            }
        });

        tipsText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToTipsDetails();
            }
        });

        dosageText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToDosageDetails();
            }
        });

        userID=getIntent().getExtras().getInt("userID");

//        checkingInbox();

    }



    private void checkingInbox() {

    }

    private void sendToAppointmentDetails() {
        Intent inboxDetail=new Intent(this, AppointmentDetails.class);
        inboxDetail.putExtra("userID",this.userID);
        startActivity(inboxDetail);
    }

    private void sendToMyHealthDetails() {
        Intent inboxDetail=new Intent(this, MyHeathDetails.class);
        inboxDetail.putExtra("userID",this.userID);
        startActivity(inboxDetail);
    }

    private void sendToDosageDetails(){
        Intent inboxDetail=new Intent(this, DosageDetails.class);
        inboxDetail.putExtra("userID",this.userID);
        startActivity(inboxDetail);
    }

    private void sendToTipsDetails(){
        Intent inboxDetail=new Intent(this, TipsDetails.class);
        inboxDetail.putExtra("userID",this.userID);
        startActivity(inboxDetail);
    }

    private void sendToRecommendationDetails(){
        Intent inboxDetail=new Intent(this, RecomendationDetails.class);
        inboxDetail.putExtra("userID",this.userID);
        startActivity(inboxDetail);
    }


}



