package com.example.kangaroonew;

import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.fragment.app.FragmentManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Appointment extends AppCompatActivity {

    MaterialDatePicker<Long> datePicker;
    MaterialButton dateButton;
    MaterialButton confirmButton;
    MaterialButton cancelButton;
    private String appointmentDate;

    TextInputLayout hospitalTextInput;
    AutoCompleteTextView hospitalAutocomplete;
    ArrayList hospitalList;
    List<Hospital> hospitals1;
    ArrayAdapter hospitalsArrayAdapter;

    TextInputLayout staffTextInput;
    AutoCompleteTextView staffAutocomplete;
    ArrayList staffList;
    List<Staff> staffs1;
    ArrayAdapter staffArrayAdapter;

    TextInputEditText appointmentDescription;

    JsonApiPlaceholder jsonPlaceHolder;
    private boolean hospitalSelectedFlag=false;
    private int hospitalSelected;
    private int staffSelected;
    private ProgressDialog progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);

        initialization();

        progressBar=new ProgressDialog(this);

        progressBar.setTitle("Loading");
        progressBar.setMessage("Please wait...");
        progressBar.setCanceledOnTouchOutside(true);
        progressBar.show();



        Gson gson =new GsonBuilder().serializeNulls().create(); //makes gson take into account nulls when they are mentioned
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://kangaroobackend.herokuapp.com/api/")
                .addConverterFactory(GsonConverterFactory.create(gson))
//                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        //create interface reference
        jsonPlaceHolder=retrofit.create(JsonApiPlaceholder.class);


        fillHospitals();



        hospitalAutocomplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                hospitalSelectedFlag=true;
                for(Hospital hosp: hospitals1) {
                    if (hosp.getName().equals(hospitalAutocomplete.getText().toString())) {    //this is the hospital user selected
                        hospitalSelected=hosp.getId();
                        staffList.clear();
                        staffAutocomplete.setText("");
                        staffArrayAdapter=new ArrayAdapter<>(getApplicationContext(),R.layout.list_item,staffList);
                        staffAutocomplete.setAdapter(staffArrayAdapter);
                        staffAutocomplete.setThreshold(1);
                        fillStaffs(hospitalSelected);
                        break;
                    }
                }
            }
        });



        staffAutocomplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for(Staff staff: staffs1) {
                    if (staff.getName().equals(staffAutocomplete.getText().toString())) {    //this is the staff user selected
                        staffSelected=staff.getId();
                        break;
                    }
                }
            }
        });



        hospitalsArrayAdapter=new ArrayAdapter<>(getApplicationContext(),R.layout.list_item,hospitalList);
        hospitalAutocomplete.setAdapter(hospitalsArrayAdapter);
        hospitalAutocomplete.setThreshold(1);


        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectingDate();
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAppointment(appointmentDate,hospitalSelected,staffSelected,appointmentDescription.getText().toString());
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUserToHomeActivity();
            }
        });
    }

    private void initialization(){
        dateButton=(MaterialButton)findViewById(R.id.chooseDate);

        cancelButton=(MaterialButton)findViewById(R.id.cancel);
        confirmButton=(MaterialButton)findViewById(R.id.confirm);

        hospitalTextInput=(TextInputLayout)findViewById(R.id.menuHospital2);
        hospitalAutocomplete=(AutoCompleteTextView)findViewById(R.id.autoCompleteHospital2);

        staffTextInput=(TextInputLayout)findViewById(R.id.menuStaff);
        staffAutocomplete=(AutoCompleteTextView)findViewById(R.id.autoCompleteStaff);

        appointmentDescription=(TextInputEditText)findViewById(R.id.description);

        hospitalList=new ArrayList<Hospital>();
        staffList=new ArrayList<Staff>();
    }

    private void setAppointment(String appointmentDate, int hospitalSelected, int staffSelected, String appointmentDescription) {
        AppointmentClass appointment=new AppointmentClass();
        appointment.setDate(appointmentDate);
        appointment.setHospitalId(hospitalSelected);
        appointment.setStaffId(staffSelected);
        appointment.setDescription(appointmentDescription);

        progressBar.setTitle("Setting appointment");
        progressBar.setMessage("Please wait...");
        progressBar.setCanceledOnTouchOutside(true);
        progressBar.show();
        Call<AppointmentClass>newAppointment =jsonPlaceHolder.newAppointment(appointment);
        
        newAppointment.enqueue(new Callback<AppointmentClass>() {
            @Override
            public void onResponse(Call<AppointmentClass> call, Response<AppointmentClass> response) {
                 Log.d("kng","creating...failed"+ response.message());
                if(response.isSuccessful()){
                    Log.d("kng","creating...");
                   Toast.makeText(Appointment.this, "Appointment created!", Toast.LENGTH_SHORT).show();
                   sendUserToHomeActivity();
                }
                progressBar.dismiss();
            }

            @Override
            public void onFailure(Call<AppointmentClass> call, Throwable t) {
              Toast.makeText(Appointment.this, "Internal error!", Toast.LENGTH_SHORT).show();
              progressBar.dismiss();
            }
        });
    }

    private void selectingDate() {
        datePicker =MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
                .build();

        datePicker.show(getSupportFragmentManager(), "Choose Date");
        datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @Override
            public void onPositiveButtonClick(Long selection) {
                Date date = new Date(selection.longValue());
                DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getApplicationContext());

                android.text.format.DateFormat df = new android.text.format.DateFormat();
                appointmentDate=df.format("yyyy-MM-dd", date).toString();

            }
        });
    }

    private void fillHospitals() {
        final Call<List<Hospital>> hospitals=jsonPlaceHolder.allHospital();

        hospitals.enqueue(new Callback<List<Hospital>>() {
            @Override
            public void onResponse(Call<List<Hospital>> call, Response<List<Hospital>> response) {
                if(response.isSuccessful()){
                    hospitals1=response.body();
                    for(Hospital hosp: hospitals1){
                        hospitalList.add(hosp.getName());
                    }
                }
                progressBar.dismiss();
            }

            @Override
            public void onFailure(Call<List<Hospital>> call, Throwable t) {
                Toast.makeText(Appointment.this, "Internal error!", Toast.LENGTH_SHORT).show();
                progressBar.dismiss();
            }

        });

    }

    private void fillStaffs(int hospitalId) {
        if(hospitalId!=0){  //user has actually selected hospital so as to see its staffs

            progressBar.setTitle("Loading");
            progressBar.setMessage("Please wait...");
            progressBar.setCanceledOnTouchOutside(true);
            progressBar.show();
            final Call<List<Staff>> staffs=jsonPlaceHolder.hospitalStaffs(hospitalId);

            staffs.enqueue(new Callback<List<Staff>>() {
                @Override
                public void onResponse(Call<List<Staff>> call, Response<List<Staff>> response) {
                    if(response.isSuccessful()){
                        staffs1=response.body();
                        for(Staff staff: staffs1){
                           staffList.add(staff.getName());
                        }
                        progressBar.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<List<Staff>> call, Throwable t) {
                    Toast.makeText(Appointment.this, "Internal error!", Toast.LENGTH_SHORT).show();
                    progressBar.dismiss();
                }
            });
        }
    }

    private void sendUserToHomeActivity() {
        Intent homeActivity=new Intent(this,Home.class);
        startActivity(homeActivity);
        finish();
    }
}
