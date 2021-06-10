package com.example.kangaroonew.insideInbox;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.kangaroonew.JsonApiPlaceholder;
import com.example.kangaroonew.R;
import com.example.kangaroonew.models.BraceletData;
import com.example.kangaroonew.models.DosageData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.List;

public class DosageDetails extends AppCompatActivity {
    int userID;
    JsonApiPlaceholder jsonPlaceHolder;

    String[] mobileArray = {"Android","IPhone","WindowsMobile","Blackberry",
            "WebOS","Ubuntu","Windows7","Max OS X"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dosage_details);

        userID=getIntent().getExtras().getInt("userID");

        Gson gson =new GsonBuilder().serializeNulls().create(); //makes gson take into account nulls when they are mentioned
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://kangaroobackend.herokuapp.com/api/")
                .addConverterFactory(GsonConverterFactory.create(gson))
//                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        jsonPlaceHolder=retrofit.create(JsonApiPlaceholder.class);

        checkingDosage(this);


//        ArrayAdapter adapter = new ArrayAdapter<String>(this,
//                R.layout.list_view, mobileArray);
//
//        ListView listView = (ListView) findViewById(R.id.mobile_list);
//        listView.setAdapter(adapter);
    }

    private void checkingDosage(final Context context) {
//        final Call<List<BraceletData>> braceletData=jsonPlaceHolder.braceletData(userID);
        final Call<List<DosageData>> dosageData=jsonPlaceHolder.dosageData(userID);

        dosageData.enqueue(new Callback<List<DosageData>>() {
            @Override
            public void onResponse(Call<List<DosageData>> call, Response<List<DosageData>> response) {
                if(response.isSuccessful()){
                    List<DosageData> appointmentList=response.body();
                    mobileArray=new String[appointmentList.size()];
                    String temp="";
                    for(DosageData appointment: appointmentList){
                        temp="Dosage is "+appointment.getMedicineUsage();


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
            public void onFailure(Call<List<DosageData>> call, Throwable t) {

            }
        });

    }
}
