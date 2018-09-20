package com.example.kj.lexithymia;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by kapashnin on 02.09.18.
 */

public class ChatClient extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener, View.OnTouchListener {

    Button sendButton;
    EditText messageText;
    ListView messageList;
    MyAdapter mAdapter = null;
    Data data = null;

    Context mContext;
    StorageAccess sa;
    ArrayList<Integer> toggleButtonIDs;
    Spinner spinner1;
    String emoExcluded = null;
    ToggleButton tb1;
    ToggleButton tb2;
    Reducer r;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_example);

        r = new Reducer(this);

        getSupportActionBar().setTitle("Your Activity Title"); // for set actionbar title
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // for add back arrow in action bar

        mContext = this;
        tb1 = findViewById(R.id.tbColor);
        //tb1.setRotation(-90);

        tb2 = findViewById(R.id.tbColor2);
        //tb2.setRotation(-90);

//        spinner1 = (Spinner) findViewById(R.id.spinner1);
//        List<String> list = new ArrayList<String>();
//        list.add("list 1");
//        list.add("list 2");
//        list.add("list 3");
//        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_spinner_item, list);
//        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner1.setAdapter(dataAdapter);




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


        LinearLayout l1 = (LinearLayout) findViewById(R.id.l2v1);
        LinearLayout l2 = (LinearLayout) findViewById(R.id.l2v2);
        LinearLayout l3 = (LinearLayout) findViewById(R.id.l2v3);
        LinearLayout l4 = (LinearLayout) findViewById(R.id.l2v4);
        RelativeLayout l5 = (RelativeLayout) findViewById(R.id.lo_plutchik);
        l1.setVisibility(View.GONE);
        l2.setVisibility(View.GONE);
        l3.setVisibility(View.GONE);
        l4.setVisibility(View.GONE);
        l5.setVisibility(View.VISIBLE);

        ImageView image  = (ImageView) findViewById(R.id.plutchik);
        //image.setImageResource(R.drawable.plutchik_my_crop);
        image.setOnTouchListener(this);


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_menu, menu);
        MenuItem i = menu.findItem(R.id.spinner1);
        Spinner s = (Spinner) i.getActionView();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.feelingsGroup, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        s.setAdapter(adapter);
        s.setOnItemSelectedListener(this);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        switch (item.getItemId()) {
            case R.id.spinner1:
                // User chose the "Settings" item, show the app settings UI...
                spinner1.showContextMenu();
                return true;


            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

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
            fv = fv_full.split(",")[1];
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
//            case R.id.bSend2:
//                LinearLayout l1 = (LinearLayout) findViewById(R.id.l2v1);
//                LinearLayout l4 = (LinearLayout) findViewById(R.id.l2v4);
//                if(l1.getVisibility() == View.GONE) {
//                    l1.setVisibility(View.VISIBLE);
//                    l4.setVisibility(View.GONE);
//                } else {
//                    l4.setVisibility(View.VISIBLE);
//                    l1.setVisibility(View.GONE);
//                }
//                break;
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
                //onToggle(view.getId());

                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String item = adapterView.getItemAtPosition(i).toString();
        LinearLayout l1 = (LinearLayout) findViewById(R.id.l2v1);
        LinearLayout l2 = (LinearLayout) findViewById(R.id.l2v2);
        LinearLayout l3 = (LinearLayout) findViewById(R.id.l2v3);
        LinearLayout l4 = (LinearLayout) findViewById(R.id.l2v4);
        RelativeLayout l5 = (RelativeLayout) findViewById(R.id.lo_plutchik);

        if (item.toString().equals("Seligman")) {
            l1.setVisibility(View.VISIBLE);
            l4.setVisibility(View.VISIBLE);
            l3.setVisibility(View.GONE);
            l2.setVisibility(View.GONE);
            l5.setVisibility(View.GONE);
        } else if (item.toString().equals("Ekman")) {
            l2.setVisibility(View.VISIBLE);
            l3.setVisibility(View.VISIBLE);
            l1.setVisibility(View.GONE);
            l4.setVisibility(View.GONE);
            l5.setVisibility(View.GONE);
        } else if (item.toString().equals("Plutchik")) {
            l2.setVisibility(View.GONE);
            l3.setVisibility(View.GONE);
            l1.setVisibility(View.GONE);
            l4.setVisibility(View.GONE);
            l5.setVisibility(View.VISIBLE);

        } else if (item.toString().equals("You")) {
            l2.setVisibility(View.VISIBLE);
            l3.setVisibility(View.VISIBLE);
            l1.setVisibility(View.GONE);
            l4.setVisibility(View.GONE);
        }
        else {

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private static String To00Hex(int value) {
        String hex = "00".concat(Integer.toHexString(value));
        return hex.substring(hex.length()-2, hex.length());
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        int a = motionEvent.getAction();
        switch (a) {
            case MotionEvent.ACTION_DOWN:

                TextView tx = findViewById(R.id.textViewX);
                TextView ty = findViewById(R.id.textViewY);
                int x = (int) motionEvent.getX(); // in pixels
                int y = (int) motionEvent.getY();
                tx.setText("x= " + x);
                ty.setText("y= " + y);

                ImageView i = findViewById(R.id.plutchik);
                i.setDrawingCacheEnabled(true);
                Bitmap b = Bitmap.createBitmap(i.getDrawingCache());
                i.setDrawingCacheEnabled(false);
                int p = b.getPixel(x, y);
                String hexColor = String.format("#%08X", (0xffFFFFFF & p));

                int vw_dp = i.getWidth(); //size in dp (of view)
                int vh_dp = i.getHeight();

                float dp = getResources().getDisplayMetrics().density;

                Bitmap b_fg = BitmapFactory.decodeResource(getResources(), R.drawable.plutchik_my_v6);
                int bw_fg_dp = b_fg.getWidth(); //size in dp
                int bh_fg_dp = b_fg.getHeight();
                int bw_fg_px = (int) (bw_fg_dp / dp); //size in pixels
                int bh_fg_px = (int) (bh_fg_dp / dp);

                float ratio_view_w = ((float) bw_fg_dp) / vw_dp; // >1, if view is smaller
                float ratio_view_h = ((float) bh_fg_dp) / vh_dp; // >1, if view is smaller

                Bitmap b_bg = BitmapFactory.decodeResource(getResources(), R.drawable.plutchik_my_v6_background);
                int bw_bg_dp = b_bg.getWidth(); //size in dp (of background)
                int bh_bg_dp = b_bg.getHeight();
                int bw_bg_px = (int) (bw_bg_dp / dp); //size in pixels (of background)
                int bh_bg_px = (int) (bh_bg_dp / dp);

                int x_bg = (int) (x * ratio_view_w);
                int y_bg = (int) (y * ratio_view_h);
                int p_bg = b_bg.getPixel(x_bg, y_bg);
                String hexColor_bg = String.format("#%08X", (0xffFFFFFF & p_bg));

                //int[] ps_bg = new int[bw_bg_px * bh_bg_px];
                //b_bg.getPixels(ps_bg, 0, bw_bg_px, x, y, bw_bg_px, bh_bg_px);

                TextView tv1 = findViewById(R.id.textViewXbg);

                tv1.setText("" + hexColor_bg);

                //String cs = pixelToColorString(p);
                String cs_bg = pixelToColorString(p_bg);
                String[] emo = Constants.PLUTCHIK.getEmotionFromColor(cs_bg);
                if (emo == null)
                    return false;

                String emo_name = Constants.PLUTCHIK.getEmotionName(emo);
                String emo_color = Constants.PLUTCHIK.getEmotionColor(emo);
                String emo_color_bg = Constants.PLUTCHIK.getEmotionColorBg(emo);

                Emo e = new Emo(emo_name, emo_color);
                r.emoPicked(e);
                String listOfEmos = r.getEmosAsString();
                messageText.setText(listOfEmos);


                break;
        }
        return true;
    }



    public void pickedEmo1 (Emo e) {
        String emo_name = e.name;
        String emo_color = e.color;

        tb1.setBackgroundColor(Color.parseColor(emo_color));
        if (Color.luminance(Color.parseColor(emo_color)) < 0.2)
            tb1.setTextColor(Color.WHITE);

        tb1.setText(emo_name);
        tb1.setTextOn(emo_name);
        tb1.setTextOff(emo_name);
        tb1.setChecked(true);
        tb1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEmo1ButtonClick(v);
            }
        });

        LinearLayout lo_res = findViewById(R.id.lo_plutchik_status);
        r.setLayout1(lo_res);
    }


    public void pickedEmo2 (Emo e) {
        String emo_name = e.name;
        String emo_color = e.color;

        tb2.setBackgroundColor(Color.parseColor(emo_color));
        if (Color.luminance(Color.parseColor(emo_color)) < 0.2)
            tb2.setTextColor(Color.WHITE);

        tb2.setText(emo_name);
        tb2.setTextOn(emo_name);
        tb2.setTextOff(emo_name);
        tb2.setChecked(true);
        tb2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEmo2ButtonClick(v);
            }
        });

        LinearLayout lo_res2 = findViewById(R.id.lo_plutchik_status2);
        r.setLayout2(lo_res2);
    }

    public void cleanUpPickedEmos() {

    }

    public String makeVertical(String emo_name) {
        String s = "";
        {
            for (int i = 0; i < emo_name.length(); i++) {
                if (s == null)
                    s = "";
                s = s + String.valueOf(emo_name.charAt(i)) + "\n";
            }
        }
        return s;
    }

    public void addDyadButton(LinearLayout lo, ToggleButton tb) {
        tb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDyadButtonClick(v);
            }
        });
        lo.addView(tb);
    }

    public void removeDyadsOfEmo1()
    {
        LinearLayout lo_res1 = findViewById(R.id.lo_plutchik_status);
        lo_res1.removeAllViews();

        tb1.setTextOff("NOT PICKED");
        tb1.setTextOn("NOT PICKED+");
        tb1.setChecked(false);
        tb1.setBackgroundColor(Color.parseColor("#dddddd"));
    }
    public void removeDyadsOfEmo2()
    {
        LinearLayout lo_res2 = findViewById(R.id.lo_plutchik_status2);
        lo_res2.removeAllViews();

        tb2.setTextOff("NOT PICKED");
        tb2.setTextOn("NOT PICKED+");
        tb2.setChecked(false);
        tb2.setBackgroundColor(Color.parseColor("#dddddd"));
    }

    public void onEmo1ButtonClick(View v)
    {
        r.unpickEmo1();
        String listOfEmos = r.getEmosAsString();
        messageText.setText(listOfEmos);
    }

    public void onEmo2ButtonClick(View v)
    {
        //tb1.setWidth(400);

        LinearLayout lo = findViewById(R.id.lo_plutchik_e1);
        lo.setRotation(-90);
        tb1.setWidth(40);
        tb1.setHeight(400);
        //tb1.setRotation(-40);
        //tb1.setRotation(-90);

        return;

        //r.unpickEmo2();
        //String listOfEmos = r.getEmosAsString();
        //messageText.setText(listOfEmos);
    }

    public void onDyadButtonClick(View v)
    {

        ToggleButton tb = (ToggleButton) v;
        String fv = tb.getText().toString();

        if (!tb.isChecked()) {
            tb.setChecked(false);
            r.dyadUnpicked(fv);
        } else {
            tb.setChecked(true);
            r.dyadPicked(fv);
        }
        messageText.setText(r.getEmosAsString());
    }



/*    public void onToggle2(View v)
    {

        boolean checked = tb.isChecked();
        String text = messageText.getText().toString();
        if (!checked) {
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
    */

    public String pixelToColorString(int p) {

        String ca = To00Hex(Color.alpha(p));
        String cr = To00Hex(Color.red(p));
        String cg = To00Hex(Color.green(p));
        String cb = To00Hex(Color.blue(p));
        StringBuilder str = new StringBuilder("#");
        str.append(ca);
        str.append(cr);
        str.append(cg);
        str.append(cb);
        return str.toString();
    }
}
