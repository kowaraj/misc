package com.example.kj.helloworld;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener {

    Button bStrength;
    EditText etFeeling;

    public static final List<Feeling> listOfFeelingsUpdated  = new ArrayList<Feeling>();


    private final static String TAG = "Main Activity";
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;


        bStrength = (Button) findViewById(R.id.bStrength);
        bStrength.setOnClickListener(this);

        etFeeling = (EditText) findViewById(R.id.etFeeling);
        etFeeling.setOnKeyListener(this);

        Button bTestListActivity = findViewById(R.id.bTestListActivity);
        bTestListActivity.setOnClickListener(this);

        Button bChatClient = findViewById(R.id.bChatClient);
        bChatClient.setOnClickListener(this);

        Button bReadServer = findViewById(R.id.bReadServer);
        bReadServer.setOnClickListener(this);

        Button bWriteServer = findViewById(R.id.bWriteServer);
        bWriteServer.setOnClickListener(this);

        Log.i(TAG, "onCreate");
    }

    @Override
    public void onClick(View view) {
        etFeeling = (EditText) findViewById(R.id.etFeeling);
        String strFeeling = etFeeling.getText().toString();


        switch (view.getId()) {
            case R.id.bWriteServer:
            {
                JsonServerProcessor jsp = new JsonServerProcessor(mContext);
                jsp.execute("write");
                break;
            }
            case R.id.bReadServer:
            {
                JsonServerProcessor jsp = new JsonServerProcessor(mContext);
                jsp.execute("read");
                break;
            }
            case R.id.bChatClient:
            {
                Intent in = new Intent(this, ChatClient.class);
                in.putExtra("msg", "dummy");
                startActivity(in);
                break;
            }

            case R.id.bTestListActivity:
            {
                Intent in = new Intent(this, test.class);
                in.putExtra("msg", "test");
                startActivity(in);
                break;
            }

            case R.id.bStrength:
            {
                TextView tvMessage = (TextView) findViewById(R.id.tvMessage);
                //tvMessage.setText("I am feeling ..." + etFeeling.getText().toString());

                // same for the other activity
                Intent in = new Intent(this, showMessage.class);
                in.putExtra("msg", etFeeling.getText().toString());
                startActivity(in);
                break;
            }
        }
    }



    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        switch (view.getId()) {
            case R.id.etFeeling:



                break;

            default:
                break;
        }

        return false;
    }


}
