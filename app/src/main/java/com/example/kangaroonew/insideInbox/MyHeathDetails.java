package com.example.kangaroonew.insideInbox;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
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



    LineChart chart;

    int userID;
    JsonApiPlaceholder jsonPlaceHolder;
    private ProgressDialog progressBar;

    String[] mobileArray;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_heath_details);

        chart = (LineChart) findViewById(R.id.chart);

        userID=getIntent().getExtras().getInt("userID");


        List<Entry> entries = new ArrayList<Entry>();
        entries.add(new Entry(10,6));
        entries.add(new Entry(5,4));
        entries.add(new Entry(13,15));
        entries.add(new Entry(15,16));
        entries.add(new Entry(24,23));
        entries.add(new Entry(16,8));

        LineDataSet dataSet = new LineDataSet(entries, "Label"); // add entries to dataset
        dataSet.setColor(Color.RED);
        dataSet.setValueTextColor(Color.GREEN); // styling, ..

        chart.setBackgroundColor(Color.WHITE);



        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);
        chart.invalidate(); // refresh





        Gson gson =new GsonBuilder().serializeNulls().create(); //makes gson take into account nulls when they are mentioned
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://kangaroobackend.herokuapp.com/api/")
                .addConverterFactory(GsonConverterFactory.create(gson))
//                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        jsonPlaceHolder=retrofit.create(JsonApiPlaceholder.class);

checkingBracelet(this);
//        ArrayAdapter adapter = new ArrayAdapter<String>(this,
//                R.layout.list_view, mobileArray);
//
//        ListView listView = (ListView) findViewById(R.id.mobile_list);
//        listView.setAdapter(adapter);
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
            @Override
            public void onResponse(Call<List<BraceletData>> call, Response<List<BraceletData>> response) {
                if(response.isSuccessful()){
                    List<BraceletData> appointmentList=response.body();
                    mobileArray=new String[appointmentList.size()];
                    String temp="";
                    for(BraceletData appointment: appointmentList){
                        temp="Temperature is "+appointment.getTemperature()+"\n";
                        temp+="Heart rate is "+appointment.getHeartRate();


                        //fill this appointment in array
                        mobileArray[appointmentList.indexOf(appointment)]=temp;

                        //set temp to empty for next loop
                        temp="";
                    }

                    ArrayAdapter adapter = new ArrayAdapter<String>(context,
                            R.layout.list_view, mobileArray);

                    ListView listView = (ListView) findViewById(R.id.mobile_list);
                    listView.setAdapter(adapter);
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
