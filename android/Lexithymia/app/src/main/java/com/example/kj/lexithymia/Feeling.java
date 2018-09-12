package com.example.kj.lexithymia;

import android.support.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Feeling {

    public String name;
    public String group;
    public Date date;
    public Integer value;
    public Integer id;
    public String type;

    public Feeling() {
    }

    public Feeling(String name, String group, Date date, Integer value) {
        this.id = 2;
        this.name = name;
        this.group = group;
        this.date = date;
        this.value = value;
        this.type = "feelings";
    }

    public String getDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
        return sdf.format(date);
    }

    public String getTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("h:mm a");
        if (date == null)
            date = new Date();
        return sdf.format(date);
    }
}
