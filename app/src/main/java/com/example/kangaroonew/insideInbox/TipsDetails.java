package com.example.kangaroonew.insideInbox;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kangaroonew.JsonApiPlaceholder;
import com.example.kangaroonew.R;
import com.example.kangaroonew.models.PregnancyDate;
import com.example.kangaroonew.models.Recommendation;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TipsDetails extends AppCompatActivity {
    JsonApiPlaceholder jsonPlaceHolder;
    int userID;
    private ProgressDialog progressBar;
    TextView tipsText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tips_details);

        tipsText =findViewById(R.id.textTipsDetails);
        userID=getIntent().getExtras().getInt("userID");
        tipsText.setText("Getting tip for Today...");
        Gson gson =new GsonBuilder().serializeNulls().create(); //makes gson take into account nulls when they are mentioned
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://kangaroobackend.herokuapp.com/api/")
                .addConverterFactory(GsonConverterFactory.create(gson))
//                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        progressBar=new ProgressDialog(this);

        jsonPlaceHolder=retrofit.create(JsonApiPlaceholder.class);

        getTip();

    }

    private void getTip() {
        progressBar.setTitle("Loading");
        progressBar.setMessage("Please wait...");
        progressBar.setCanceledOnTouchOutside(true);
        progressBar.show();

        final Call<PregnancyDate> pregDate=jsonPlaceHolder.getPregDate(userID);
        pregDate.enqueue(new Callback<PregnancyDate>() {
            @Override
            public void onResponse(Call<PregnancyDate> call, Response<PregnancyDate> response) {
                if(response.isSuccessful()){

                    String date=response.body().getPregnancyDate();


                    try {
                        Date date1=new SimpleDateFormat("yyyy-MM-dd").parse(date);
                        Log.d("j","date is:"+ date1.toString());
                       int weekNo= getWeekDiff(date1);
//                        tipsText.setText(weekNo);
                        Log.d("j","date is:"+ weekNo);

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }


                }
                progressBar.dismiss();
            }

            @Override
            public void onFailure(Call<PregnancyDate> call, Throwable t) {

                progressBar.dismiss();
                Toast.makeText(TipsDetails.this,"Failed to load, error occured!",Toast.LENGTH_LONG).show();
            }
        });

    }


    public int getWeekDiff(Date date2){

        Date date1=Calendar.getInstance().getTime();;
        long diff = date1.getTime() - date2.getTime();
        long seconds = diff / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;
        long weeks=days/7;
        if(weeks<1){    //if weeks are less than 1 return 1
            weeks=1;
        }

        return (int) weeks;
    }


}