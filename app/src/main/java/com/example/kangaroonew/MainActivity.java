package com.example.kangaroonew;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.net.wifi.hotspot2.pps.Credential;
import android.os.Build;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import com.example.kangaroonew.models.User;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;



public class MainActivity extends AppCompatActivity {


    private MaterialButton loginBtn;
    private TextInputEditText email;
    private TextInputEditText password;
    private ProgressDialog progressBar;
    private Toolbar mToolbar;
    private boolean logout=false;
    private TextView forgetPassword;
    SharedPreferences app_preferences;

    Retrofit retrofit;


    JsonApiPlaceholder jsonPlaceHolder;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        mToolbar= findViewById(R.id.main_page_toolbar);
//        //setSupportActionBar(mToolbar);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
//        mToolbar.setTitle("MyHealth");


        try {
            String status=getIntent().getExtras().getString("logout");
            Log.d("SDf","logged out  is "+ status);
            app_preferences =PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor editor = app_preferences.edit();

                Log.d("SDf","logged outtt");

                editor.clear();
                editor.apply();

                logout=true;


        }catch (Exception e){

        }

        initialization();

        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://kangaroobackend.herokuapp.com/forget-password"));
                startActivity(browserIntent);
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                sendUserToHomeActivity();

                String emailText=email.getText().toString();
                String passwordText=password.getText().toString();


                if(TextUtils.isEmpty(emailText)) {
                    Toast.makeText(MainActivity.this, "Please enter your email", Toast.LENGTH_SHORT).show();
                }else{
//                    if(android.util.Patterns.EMAIL_ADDRESS.matcher(emailText).matches()){  //valid email entered
                        if (TextUtils.isEmpty(passwordText)) {
                            Toast.makeText(MainActivity.this, "Please enter your password", Toast.LENGTH_SHORT).show();
                        } else {
                            validateUser(emailText, passwordText);
                        }


//                else {
//                        Toast.makeText(MainActivity.this,"Please enter a valid email address",Toast.LENGTH_SHORT).show();
//                    }
                }

        }});
    }

    private void initialization() {

        progressBar=new ProgressDialog(this);
        loginBtn=(MaterialButton) findViewById(R.id.loginBtn);
        email=(TextInputEditText) findViewById(R.id.email);
        password=(TextInputEditText) findViewById(R.id.password);
        forgetPassword=(TextView) findViewById(R.id.forgetPassword);



        Gson gson =new GsonBuilder().serializeNulls().create(); //makes gson take into account nulls when they are mentioned


        retrofit = new Retrofit.Builder()
                .baseUrl("https://kangaroobackend.herokuapp.com/api/")
                .addConverterFactory(GsonConverterFactory.create(gson))
//                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        //create interface reference
        jsonPlaceHolder=retrofit.create(JsonApiPlaceholder.class);


        app_preferences =PreferenceManager.getDefaultSharedPreferences(this);

        app_preferences.getString("email","");
        app_preferences.getString("password","");

       String emailTemp= app_preferences.getString("email","");
       String passwordTemp= app_preferences.getString("password","");

        if(!logout){
            if(TextUtils.equals(emailTemp,"") ){

            }else{
                validateUser(app_preferences.getString("email",""), app_preferences.getString("password",""));
            }

        }

    }

    private void validateUser(final String email, final String password) {
        //after validation

        progressBar.setTitle("Logging in");
        progressBar.setMessage("Please wait...");
        progressBar.setCanceledOnTouchOutside(true);
        progressBar.show();

        User user=new User();
        user.setEmail(email);
        user.setPassword(password);

        Call<User> result=jsonPlaceHolder.validateUser(user);
        result.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()){
                    Log.d("dfs",response.message());
                   if(response.body().getEmail()!=null){
                       progressBar.dismiss();

                       SharedPreferences.Editor editor = app_preferences.edit();
                       editor.putString("email",email);
                       editor.putString("password",password);

                       editor.commit();


                       sendUserToHomeActivity(response.body().getId());
                   }else{
                       progressBar.dismiss();
                       Toast.makeText(MainActivity.this, "Sorry, Incorrect credentials!", Toast.LENGTH_LONG).show();


                   }

                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                progressBar.dismiss();
                Toast.makeText(MainActivity.this, "Internal error!", Toast.LENGTH_SHORT).show();
            }
        });



    }

    private void sendUserToHomeActivity(int userId) {
        Intent homeActivity=new Intent(this,Home.class);
        homeActivity.putExtra("userID",userId);
        startActivity(homeActivity);
        finish();
    }




}
