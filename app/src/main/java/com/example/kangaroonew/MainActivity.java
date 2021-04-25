package com.example.kangaroonew;

import android.app.ProgressDialog;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {


    private MaterialButton loginBtn;
    private TextInputEditText email;
    private TextInputEditText password;
    private ProgressDialog progressBar;

    JsonApiPlaceholder jsonPlaceHolder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar=new ProgressDialog(this);
        loginBtn=(MaterialButton) findViewById(R.id.loginBtn);
        email=(TextInputEditText) findViewById(R.id.email);
        password=(TextInputEditText) findViewById(R.id.password);

        Gson gson =new GsonBuilder().serializeNulls().create(); //makes gson take into account nulls when they are mentioned


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://kangaroobackend.herokuapp.com/api/")
                .addConverterFactory(GsonConverterFactory.create(gson))
//                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        //create interface reference
        jsonPlaceHolder=retrofit.create(JsonApiPlaceholder.class);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

    private void validateUser(String email, String password) {
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

                       sendUserToHomeActivity();
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

    private void sendUserToHomeActivity() {
        Intent homeActivity=new Intent(this,Home.class);
        startActivity(homeActivity);
        finish();
    }


}
