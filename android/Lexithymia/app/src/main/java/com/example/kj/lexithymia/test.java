package com.example.kj.lexithymia;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

public class test extends ListActivity {

    String [] feelings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        feelings = getResources().getStringArray(R.array.feelings);
        setListAdapter((ListAdapter) new ArrayAdapter<String>(this, R.layout.feelings_item, feelings));
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent in = new Intent(this, showMessage.class);
        in.putExtra("msg", feelings[(int)id]);
        startActivity(in);

    }
}
