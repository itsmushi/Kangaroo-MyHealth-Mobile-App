package com.example.kangaroonew.insideInbox;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.kangaroonew.JsonApiPlaceholder;
import com.example.kangaroonew.R;
import com.example.kangaroonew.models.AppointmentClass;
import com.example.kangaroonew.models.BraceletData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.List;

public class MyHeathDetails extends AppCompatActivity {

    int userID;
    JsonApiPlaceholder jsonPlaceHolder;

    String[] mobileArray;
//    = {"Android","IPhone","WindowsMobile","Blackberry",
//            "WebOS","Ubuntu","Windows7","Max OS X"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_heath_details);

        userID=getIntent().getExtras().getInt("userID");


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
                }
            }

            @Override
            public void onFailure(Call<List<BraceletData>> call, Throwable t) {

            }
        });
    }
}
