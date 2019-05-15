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
    Data data = null;

    Context mContext;
    StorageAccess sa;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_example);

        mContext = this;

        sendButton = (Button) findViewById(R.id.bSend);
        sendButton.setOnClickListener(this);

        messageText = (EditText) findViewById(R.id.messageText);



        messageList = findViewById(R.id.messageList);

        sa = new StorageAccess(mContext);
        String json_str = sa.loadData();
        if ((json_str == null) || (json_str.length()==0)) {
            AssetsAccess aa = new AssetsAccess(mContext);
            json_str = aa.get();
            data = new Data(json_str);
            sa.storeData(json_str);
        } else {
            data = new Data(json_str);
        }
        mAdapter = new MyAdapter(this, data.get());

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
                    data.addItem(f);


                    //TODO move to a proper place (eg: onDestroy?)
                    sa.storeData(data.asJsonString());


                    mAdapter.notifyDataSetChanged();
                    f = null;
                    messageText.setText("");
                }
                break;

            default:
                break;
        }
    }

}
