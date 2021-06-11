package com.example.kangaroonew.insideInbox;

import android.app.ProgressDialog;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.kangaroonew.Inbox;
import com.example.kangaroonew.JsonApiPlaceholder;
import com.example.kangaroonew.R;
import com.example.kangaroonew.models.AppointmentWithName;
import com.example.kangaroonew.models.AppointmentWithName;
import com.example.kangaroonew.models.Staff;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.List;
public class AppointmentDetails extends AppCompatActivity {
    int userID;
    String name="";
    JsonApiPlaceholder jsonPlaceHolder;
    private ProgressDialog progressBar;
    String[] mobileArray;
//    = {"Android","IPhone","WindowsMobile","Blackberry",
//            "WebOS","Ubuntu","Windows7","Max OS X"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_details);

        userID=getIntent().getExtras().getInt("userID");

        Gson gson =new GsonBuilder().serializeNulls().create(); //makes gson take into account nulls when they are mentioned
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://kangaroobackend.herokuapp.com/api/")
                .addConverterFactory(GsonConverterFactory.create(gson))
//                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        jsonPlaceHolder=retrofit.create(JsonApiPlaceholder.class);


        checkingAppointments(this);



//        ArrayAdapter adapter = new ArrayAdapter<String>(this,
//                R.layout.list_view, mobileArray);
//
//        ListView listView = (ListView) findViewById(R.id.mobile_list);
//        listView.setAdapter(adapter);
    }



    private void checkingAppointments(final Context context) {
        progressBar=new ProgressDialog(this);

        progressBar.setTitle("Loading");
        progressBar.setMessage("Please wait...");
        progressBar.setCanceledOnTouchOutside(true);
        progressBar.show();


        final Call<List<AppointmentWithName>> appointmentList=jsonPlaceHolder.userAppointmentsFull(userID);
        appointmentList.enqueue(new Callback<List<AppointmentWithName>>() {

            @Override
            public void onResponse(Call<List<AppointmentWithName>> call, Response<List<AppointmentWithName>> response) {

                if(response.isSuccessful()){

                    List<AppointmentWithName> appointmentList=response.body();

                   mobileArray=new String[appointmentList.size()];
                   String temp="";
                    for(AppointmentWithName appointment: appointmentList){
                        temp="Appointment at ";
                        temp+=appointment.getHospitalId().toString();  //send to API to get names values
                        temp+=" with "+appointment.getStaffId() ;
                        temp+="\n";
                        temp+="Description: "+"\n";
                        temp+=appointment.getDescription();
                        temp+="\n";
                        temp+="Date: "+appointment.getDate();
                        temp+="  Time: "+appointment.getAppointmentTime();

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
            public void onFailure(Call<List<AppointmentWithName>> call, Throwable t) {
                progressBar.dismiss();
                Toast.makeText(AppointmentDetails.this,"Failed to load, error occured!",Toast.LENGTH_LONG).show();
//                Log.d("Ds","status is NOT HERE");
            }
        });




    }


    private String getStaffName(int id){
        final Call<Staff> staff=jsonPlaceHolder.staffDetails(id);

        try {
            Response<Staff> response= staff.execute();
            Log.d("d","error here11");
            Staff staff1=response.body();
                 name=staff1.getFname()+" "+staff1.getLname();
            Log.d("d","error here22");

        } catch (IOException e) {
            Log.d("d","error here");
            e.printStackTrace();
        }
//        staff.enqueue(new Callback<Staff>() {
//            @Override
//            public void onResponse(Call<Staff> call, Response<Staff> response) {
//                if(response.isSuccessful()){
//                    Staff staff1=response.body();
//                 name=staff1.getFname()+" "+staff1.getLname();
//
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Staff> call, Throwable t) {
//
//            }
//        });
        return name;

    }
}
