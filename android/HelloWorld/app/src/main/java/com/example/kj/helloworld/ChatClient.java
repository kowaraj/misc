package com.example.kj.helloworld;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Array;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by kapashnin on 02.09.18.
 */

public class ChatClient extends AppCompatActivity implements View.OnClickListener {

    Button sendButton;
    EditText messageText;
    ListView messageList;
    MyAdapter mAdapter = null;
    ArrayList<Feeling> feelings = null;
    List<Feeling> listOfFeelings = null;

    Context mContext;

    public class Data {

        private ArrayList<Feeling> data;

        public Data()
        {
            data = new ArrayList<Feeling>();
        }

        public Data(String json_str)
        {
            fromJson(json_str);
        }

        // setters

        public void fromJson(String json_str)
        {
            Feeling[] array = jsonStringToArray(json_str);
            fromArray(array);
        }

        public void fromArray(Feeling[] array)
        {
            data = new ArrayList<Feeling>(Arrays.asList(array));
        }

        // getters

        public ArrayList<Feeling> asArrayList()
        {
            return data;
        }

        public String asJsonString()
        {
            return arrayListToJsonString(data);
        }

        // converters

        public Feeling[] jsonStringToArray(String json_str)
        {
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();
            Feeling[] fs_array = gson.fromJson(json_str, Feeling[].class);
            return fs_array;
        }

        public String arrayListToJsonString(ArrayList<Feeling> fs)
        {
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();
            Feeling[] fs_array = fs.toArray(new Feeling[0]);
            String json_str = gson.toJson(fs_array, Feeling[].class);
            return json_str;

        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_example);

        mContext = this;

        sendButton = (Button) findViewById(R.id.bSend);
        sendButton.setOnClickListener(this);

        messageText = (EditText) findViewById(R.id.messageText);

        //////////// read the file
        try {

            InputStream ins;
            BufferedReader in;

            ins = mContext.getAssets().open("feelings.json");

            in = new BufferedReader(new InputStreamReader(ins));
            String readLine;
            StringBuffer buf = new StringBuffer();
            while ((readLine = in.readLine()) != null) {
                buf.append(readLine);
            }
            String infoString = buf.toString();
            processFeelingsInfo(infoString);

            if (null != in) {
                in.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        // TODO

        messageList = findViewById(R.id.messageList);

        feelings = Data.asArrayList(); //new ArrayList<Feeling>(listOfFeelings); //.to //new ArrayList<Feeling>();

        mAdapter = new MyAdapter(this, feelings);

        messageList.removeAllViewsInLayout();
        messageList.setAdapter(mAdapter);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.bSend:

                String fString = messageText.getText().toString();
                // If the message is not empty string
                if (!fString.equals("")) {
                    Feeling f = new Feeling(fString, "Seligman", new Date(), 2);
                    feelings.add(f);
                    MainActivity.listOfFeelingsUpdated.add(f);
                    mAdapter.notifyDataSetChanged();
                    f = null;
                    messageText.setText("");
                }
                break;

            default:
                break;
        }
    }

    private void __processFeelingsInfo(String infoString)
    {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();

        listOfFeelings = new ArrayList<Feeling>();
        listOfFeelings = Arrays.asList(gson.fromJson(infoString, Feeling[].class));
    }
}
