package com.example.kj.lexithymia;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ToggleButton;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;

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
    ArrayList<Integer> toggleButtonIDs;


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

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        toggleButtonIDs = new ArrayList<Integer>();

        //PERMA
        toggleButtonIDs.add(R.id.tba1);
        toggleButtonIDs.add(R.id.tba2);
        toggleButtonIDs.add(R.id.tba3);
        toggleButtonIDs.add(R.id.tba4);
        toggleButtonIDs.add(R.id.tba5);

        //FACS
        toggleButtonIDs.add(R.id.tbb1);
        toggleButtonIDs.add(R.id.tbb2);
        toggleButtonIDs.add(R.id.tbb3);
        toggleButtonIDs.add(R.id.tbb4);
        toggleButtonIDs.add(R.id.tbb5);
        toggleButtonIDs.add(R.id.tbb6);
        toggleButtonIDs.add(R.id.tbb7);

        //EXTENDED
        toggleButtonIDs.add(R.id.tbc1);
        toggleButtonIDs.add(R.id.tbc2);
        toggleButtonIDs.add(R.id.tbc3);
        toggleButtonIDs.add(R.id.tbc4);
        toggleButtonIDs.add(R.id.tbc5);
        toggleButtonIDs.add(R.id.tbc6);
        toggleButtonIDs.add(R.id.tbc7);
        toggleButtonIDs.add(R.id.tbc8);
        toggleButtonIDs.add(R.id.tbc9);
        toggleButtonIDs.add(R.id.tbc10);
        toggleButtonIDs.add(R.id.tbc11);

        //PERMA-opposite
        toggleButtonIDs.add(R.id.tbd1);
        toggleButtonIDs.add(R.id.tbd2);
        toggleButtonIDs.add(R.id.tbd3);
        toggleButtonIDs.add(R.id.tbd4);
        toggleButtonIDs.add(R.id.tbd5);

        //some extra
        toggleButtonIDs.add(R.id.tbe1);
        toggleButtonIDs.add(R.id.tbe2);
        toggleButtonIDs.add(R.id.tbe3);
        toggleButtonIDs.add(R.id.tbe4);
        toggleButtonIDs.add(R.id.tbe5);
        toggleButtonIDs.add(R.id.tbe6);
        toggleButtonIDs.add(R.id.tbe7);
        toggleButtonIDs.add(R.id.tbe8);
        toggleButtonIDs.add(R.id.tbe9);

        for (Integer tbid: toggleButtonIDs) {
            ((ToggleButton) findViewById(tbid)).setOnClickListener(this);
        }

    }



    public void onToggle(int _id)
    {
        ToggleButton tb = findViewById(_id);

        String[] n = tb.getContentDescription().toString().split("\\.");
        String className = n[0];
        String fieldName = n[1];
        String classFullName = "com.example.kj.lexithymia.Constants$" + className;
        String fv = null;
        Class<?> c = null;

        try {
            //c = Class.forName(classFullName);
            Field f = Class.forName(classFullName).getField(fieldName);
            //Class<?> t = f.getType();
            String fv_full = (String) f.get(null);
            fv = fv_full.split(",")[0];
            //Constructor<?> cons = c.getConstructor(String.class);
            //Object object = cons.newInstance("P");

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        boolean checked = tb.isChecked();
        String text = messageText.getText().toString();
        if (checked) {
            if (messageText.getText().length()>0) {
                messageText.setText(text + ", " + fv);
            } else {
                messageText.setText(fv);
            }
        } else {
            String mod = text.replace(", "+fv, "")
                    .replace(fv+", ", "")
                    .replace(fv, "");

            messageText.setText(mod);
        }

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.bSend:

                // get the input
                String fString = messageText.getText().toString();

                // un-toggle the buttons
                for (Integer tbid: toggleButtonIDs) {
                    ((ToggleButton) findViewById(tbid)).setChecked(false);
                }

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
                onToggle(view.getId());

                break;
        }
    }

}
