package com.example.kj.lexithymia;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by kapashnin on 10.09.18.
 */
public class Data {

    private ArrayList<Feeling> data;

    public Data()
    {
        data = new ArrayList<Feeling>();
    }

    public Data(String json_str) {
        data = jsonStringToArrayList(json_str);
    }



    // getters

    public ArrayList<Feeling> get()
    {
        return data;
    }

    public String asJsonString()
    {
        return arrayListToJsonString(data);
    }

    // converters

    private Feeling[] jsonStringToArray(String json_str)
    {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        Feeling[] fs_array = gson.fromJson(json_str, Feeling[].class);
        return fs_array;
    }

    private ArrayList<Feeling> jsonStringToArrayList(String json_str)
    {
        Feeling[] array = jsonStringToArray(json_str);
        return new ArrayList<Feeling>(Arrays.asList(array));
    }

    private String arrayListToJsonString(ArrayList<Feeling> fs)
    {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        Feeling[] fs_array = fs.toArray(new Feeling[0]);
        String json_str = gson.toJson(fs_array, Feeling[].class);
        return json_str;

    }

    // modifiers

    public void addItem(Feeling f) {
        data.add(f);
    }

}
