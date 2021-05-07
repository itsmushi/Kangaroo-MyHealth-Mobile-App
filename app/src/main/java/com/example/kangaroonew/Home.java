package com.example.kangaroonew;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.android.material.appbar.MaterialToolbar;

public class Home extends AppCompatActivity {

    public int userID;      //loggedIn user id
    ImageButton inboxBtn;
    ImageButton appointmentBtn;
    ImageButton feedbackBtn;
    ImageButton shareBtn;
    SharedPreferences app_preferences;

    MaterialToolbar topAppBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        topAppBar=(MaterialToolbar)findViewById(R.id.topAppBar);
        setSupportActionBar(topAppBar);

       inboxBtn=(ImageButton)findViewById(R.id.inboxBtn);
        appointmentBtn=(ImageButton)findViewById(R.id.appointmentBtn);
        feedbackBtn=(ImageButton)findViewById(R.id.feedbackBtn);
        shareBtn=(ImageButton)findViewById(R.id.shareBtn);

        this.userID=getIntent().getIntExtra("userID",0);
        startCheckingAppointmentStatus();

        feedbackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToFeedbackActivity();
            }
        });
        inboxBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToInboxActivity();
            }
        });

        appointmentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToAppointmentActivity();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu2,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.logout){
            Log.d("sdf","sdf");
            Intent logoutIntent =new Intent(this,MainActivity.class);
            logoutIntent.putExtra("logout","1");
            startActivity(logoutIntent);
            finish();
            //codes to logout
        }


        return super.onOptionsItemSelected(item);
    }

    private void sendToAppointmentActivity() {
        Intent appointmentIntent=new Intent(this,Appointment.class);

        startActivity(appointmentIntent);
    }

    private void sendToInboxActivity() {
        Intent inboxIntent=new Intent(this,Inbox.class);

        startActivity(inboxIntent);
    }

    private void sendToFeedbackActivity() {
        Intent feedbackIntent=new Intent(this,Feedback.class);

        startActivity(feedbackIntent);
    }

    private void startCheckingAppointmentStatus() {
        Log.d("df","going to"+  userID);
        Intent appointmentStatus=new Intent(this,AppointmentStatus.class);
        appointmentStatus.putExtra("userID",this.userID);
        startService(appointmentStatus);
    }

}

