package com.example.kj.lexithymia;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by kapashnin on 02.09.18.
 */

public class MyAdapter extends ArrayAdapter<Feeling> {
    private final Context context;
    private final ArrayList<Feeling> feelings;

    public MyAdapter(Context context, ArrayList<Feeling> fs) {
        super(context, R.layout.message, fs);
        this.context = context;
        this.feelings = fs;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //super.getView(position, convertView, parent);
        View messageView;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        messageView = inflater.inflate(R.layout.message, parent,false);

        TextView timeView = messageView.findViewById(R.id.textViewTime);
        timeView.setText(feelings.get(position).getTime());

        TextView msgView = messageView.findViewById(R.id.textViewMessage);
        msgView.setText(feelings.get(position).name);

        TextView groupView = messageView.findViewById(R.id.textViewGroup);
        groupView.setText(feelings.get(position).group);

        return messageView;
    }



}