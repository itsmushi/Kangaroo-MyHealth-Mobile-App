package com.example.kangaroonew;

import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class Home extends AppCompatActivity {

    ImageButton inboxBtn;
    ImageButton appointmentBtn;
    ImageButton feedbackBtn;
    ImageButton shareBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

       inboxBtn=(ImageButton)findViewById(R.id.inboxBtn);
        appointmentBtn=(ImageButton)findViewById(R.id.appointmentBtn);
        feedbackBtn=(ImageButton)findViewById(R.id.feedbackBtn);
        shareBtn=(ImageButton)findViewById(R.id.shareBtn);

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

}

