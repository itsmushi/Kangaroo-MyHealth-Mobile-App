package com.example.kangaroonew;

import android.app.ProgressDialog;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.kangaroonew.models.Comment;
import com.example.kangaroonew.models.Hospital;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.ArrayList;
import java.util.List;

public class Feedback extends AppCompatActivity {

    TextInputLayout hospitalTextInput;
    AutoCompleteTextView hospitalAutocomplete;
    ArrayList hospitalList;
    List<Hospital> hospitals1;
    ArrayAdapter hospitalsArrayAdapter;
    Button submit;
    JsonApiPlaceholder jsonPlaceHolder;
    TextInputEditText comment;
    private ProgressDialog progressBar;
    private boolean hospitalSelectedFlag=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        hospitalTextInput=(TextInputLayout)findViewById(R.id.menuHospital);
        hospitalAutocomplete=(AutoCompleteTextView)findViewById(R.id.autoCompleteHospital);
        comment=(TextInputEditText)findViewById(R.id.commentInput);
        submit=(Button)findViewById(R.id.buttonSubmit);
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
        hospitalList=new ArrayList<Hospital>();
        fillHospitals();


        hospitalsArrayAdapter=new ArrayAdapter<>(getApplicationContext(),R.layout.list_item,hospitalList);
        hospitalAutocomplete.setAdapter(hospitalsArrayAdapter);
        hospitalAutocomplete.setThreshold(1);



        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    submitComment(hospitalAutocomplete.getText().toString(),comment.getText().toString());
            }
        });

    }

    private void submitComment(String hospital, String comment) {

        for(Hospital hosp: hospitals1){
            if(hosp.getName().equals(hospital)){    //this is the hospital user selected
                hospitalSelectedFlag=true;
                if(!TextUtils.isEmpty(comment)){    //user actually put a comment
                    Comment newComment=new Comment();
                    newComment.setHospitalId(hosp.getId());
                    newComment.setComment(comment);

                    progressBar.setTitle("Please wait");
                    progressBar.setMessage("Sending Feedback...");
                    progressBar.setCanceledOnTouchOutside(true);
                    progressBar.show();

                    Call<Comment> commentCall=jsonPlaceHolder.newPost(newComment);

                    commentCall.enqueue(new Callback<Comment>() {
                        @Override
                        public void onResponse(Call<Comment> call, Response<Comment> response) {
                            if(response.isSuccessful()){
                                Toast.makeText(Feedback.this, "Feedback received", Toast.LENGTH_SHORT).show();
                                sendUserToHomeActivity();
                            }
                            progressBar.dismiss();
                        }

                        @Override
                        public void onFailure(Call<Comment> call, Throwable t) {
                            Toast.makeText(Feedback.this, "Internal error!", Toast.LENGTH_SHORT).show();
                            progressBar.dismiss();
                        }

                    });
                }else{
                    Toast.makeText(Feedback.this, "Please enter a comment", Toast.LENGTH_SHORT).show();
                }

                break;

            }

        }

        if(!hospitalSelectedFlag){
            Toast.makeText(Feedback.this, "Please select a hospital that exists", Toast.LENGTH_SHORT).show();
        }


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
                Toast.makeText(Feedback.this, "Internal error!", Toast.LENGTH_SHORT).show();
                progressBar.dismiss();
            }
        });

    }

    private void sendUserToHomeActivity() {
        Intent homeActivity=new Intent(this,Home.class);
        startActivity(homeActivity);
        finish();
    }
}


