package com.example.kangaroonew.insideInbox;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kangaroonew.JsonApiPlaceholder;
import com.example.kangaroonew.R;
import com.example.kangaroonew.models.Recommendation;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecomendationDetails extends AppCompatActivity {

    TextView recommendationText;
    JsonApiPlaceholder jsonPlaceHolder;
    final int min = 1;
    final int max = 5;

    private ProgressDialog progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recomendation_details);

        recommendationText=findViewById(R.id.textRecommendationDetails);

        recommendationText.setText("recommendation....");

        Gson gson =new GsonBuilder().serializeNulls().create(); //makes gson take into account nulls when they are mentioned
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://kangaroobackend.herokuapp.com/api/")
                .addConverterFactory(GsonConverterFactory.create(gson))
//                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        jsonPlaceHolder=retrofit.create(JsonApiPlaceholder.class);

        final int random = new Random().nextInt((max - min) + 1) + min;
        getRecommendation(random);
    }

    private void getRecommendation(int recId) {
        progressBar=new ProgressDialog(this);

        progressBar.setTitle("Loading");
        progressBar.setMessage("Please wait...");
        progressBar.setCanceledOnTouchOutside(true);
        progressBar.show();
        final Call<List<Recommendation>> recommendation=jsonPlaceHolder.recommendation(recId);
        recommendation.enqueue(new Callback<List<Recommendation>>() {
            @Override
            public void onResponse(Call<List<Recommendation>> call, Response<List<Recommendation>> response) {
                if(response.isSuccessful()){
                    Log.d("Ds","status is "+recId);
                    List<Recommendation> recommendations=response.body();
                    if(recommendations.size()<1){

                        final int random = new Random().nextInt((max - min) + 1) + min;
                        progressBar.dismiss();
                        getRecommendation(random);
                        return;
                    }
                    for(Recommendation recommendation:recommendations){
                        String recomm=recommendation.getDescription();
                        recommendationText.setText(recomm);
                    }

                }
                progressBar.dismiss();
            }

            @Override
            public void onFailure(Call<List<Recommendation>> call, Throwable t) {
                progressBar.dismiss();
                Toast.makeText(RecomendationDetails.this,"Failed to load, error occured!",Toast.LENGTH_LONG).show();
//
            }
        });
    }
}