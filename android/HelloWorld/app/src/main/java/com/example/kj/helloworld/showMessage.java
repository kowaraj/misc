package com.example.kj.helloworld;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class showMessage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_message);

        Intent in = getIntent();
        String msg = in.getStringExtra("msg");
        TextView tvMessage = findViewById(R.id.tvMessage);
        tvMessage.setText(msg);
    }
}
