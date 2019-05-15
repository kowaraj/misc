package com.example.kj.helloworld;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Feeling {

    public String name;
    public String group;
    public Date date;
    public Integer value;

    public Feeling() {
    }

    public Feeling(String name, String group, Date date, Integer value) {
        this.name = name;
        this.group = group;
        this.date = date;
        this.value = value;
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
