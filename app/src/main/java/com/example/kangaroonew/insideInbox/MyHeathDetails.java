package com.example.kangaroonew.insideInbox;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.kangaroonew.JsonApiPlaceholder;
import com.example.kangaroonew.R;
import com.example.kangaroonew.models.AppointmentClass;
import com.example.kangaroonew.models.BraceletData;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import retrofit2.converter.gson.GsonConverterFactory;

import java.util.ArrayList;
import java.util.List;

public class MyHeathDetails extends AppCompatActivity {



    LineChart chart_temp;
    LineChart chart_heart;


    int userID;
    JsonApiPlaceholder jsonPlaceHolder;
    private ProgressDialog progressBar;
    List<Entry> entries_temp = new ArrayList<Entry>();
    List<Entry> entries_heart = new ArrayList<Entry>();



    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_heath_details);

        chart_temp = (LineChart) findViewById(R.id.chart_temp);
        chart_heart = (LineChart) findViewById(R.id.chart_heart);

        userID=getIntent().getExtras().getInt("userID");



        Gson gson =new GsonBuilder().serializeNulls().create(); //makes gson take into account nulls when they are mentioned
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://kangaroobackend.herokuapp.com/api/")
                .addConverterFactory(GsonConverterFactory.create(gson))
//                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        jsonPlaceHolder=retrofit.create(JsonApiPlaceholder.class);

    checkingBracelet(this);

    }

    private void checkingBracelet(final Context context) {
        progressBar=new ProgressDialog(this);

        progressBar.setTitle("Loading");
        progressBar.setMessage("Please wait...");
        progressBar.setCanceledOnTouchOutside(true);
        progressBar.show();
//        final Call<List<AppointmentClass>> appointmentList=jsonPlaceHolder.userAppointments(userID);
        final Call<List<BraceletData>> braceletData=jsonPlaceHolder.braceletData(userID);
        braceletData.enqueue(new Callback<List<BraceletData>>() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onResponse(Call<List<BraceletData>> call, Response<List<BraceletData>> response) {
                if(response.isSuccessful()){
                    List<BraceletData> appointmentList=response.body();


                    int hr=1;
                    for(BraceletData appointment: appointmentList){

                        entries_temp.add(new Entry(hr, Float.valueOf(appointment.getTemperature())));
                        entries_heart.add(new Entry(hr,Float.valueOf(appointment.getHeartRate())));
                        hr++;


                    }
//                    Log.d("dd","Reading is "+ entries_heart);

                    //for temp graph
                    LineDataSet dataSet_temp = new LineDataSet(entries_temp, "Temperature"); // add entries to dataset
                    dataSet_temp.setColor(Color.RED);
                    dataSet_temp.setValueTextColor(Color.BLACK); // styling, ..
                    chart_temp.setBackgroundColor(R.color.inboxTitles);

                    Description desc_temp=new Description();
                    desc_temp.setText("Graph for temperature");
                    desc_temp.setTextSize(20);
                    chart_temp.setDescription(desc_temp);

                    chart_temp.setNoDataText("No data recorded yet");
                    chart_temp.setDrawBorders(true);
                    LineData line_temp = new LineData(dataSet_temp);
                    chart_temp.setData(line_temp);
                    chart_temp.invalidate(); // refresh



                    //for heart graph
                    LineDataSet dataSet_heart = new LineDataSet(entries_heart, "Heart Rate");
                    dataSet_heart.setColor(Color.RED);
                    dataSet_heart.setValueTextColor(Color.BLACK); // styling, ..

                    chart_heart.setBackgroundColor(R.color.inboxTitles);

                    Description desc_heart=new Description();
                    desc_heart.setText("Graph for Heart Rate");
                    desc_heart.setTextSize(20);
                    chart_heart.setDescription(desc_heart);

                    chart_heart.setNoDataText("No data recorded yet");

                    chart_heart.setDrawBorders(true);

                    LineData line_heart = new LineData(dataSet_heart);

                    chart_heart.setData(line_heart);

                    chart_heart.invalidate(); // refresh


                    progressBar.dismiss();
                }
            }

            @Override
            public void onFailure(Call<List<BraceletData>> call, Throwable t) {
                progressBar.dismiss();
                Toast.makeText(MyHeathDetails.this,"Failed to load, error occured!",Toast.LENGTH_LONG).show();
//                Log.d("Ds","status is NOT HERE");
            }
        });
    }
}
