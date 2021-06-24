package com.example.kangaroonew.insideInbox;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.kangaroonew.R;

public class TipsDetails extends AppCompatActivity {

    TextView tipsText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tips_details);

        tipsText =findViewById(R.id.textTipsDetails);

        tipsText.setText("tips text is this");

    }
}