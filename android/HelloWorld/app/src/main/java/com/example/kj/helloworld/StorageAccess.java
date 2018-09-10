package com.example.kj.helloworld;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by kapashnin on 10.09.18.
 */

public class StorageAccess
{
    Context mContext;

    public StorageAccess(Context mContext_) {
        mContext = mContext_;
    }

    public void storeData(String json_str)
    {
        String filename = "feelings.json";
        FileOutputStream os;

        try {
            os = mContext.openFileOutput(filename, Context.MODE_PRIVATE);
            os.write(json_str.getBytes());
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String loadData()
    {
        String filename = "feelings.json";
        String json_str = null;

        try {
            FileInputStream ins = mContext.openFileInput(filename);
            InputStreamReader isr = new InputStreamReader(ins);
            BufferedReader br = new BufferedReader(isr);

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            ins.close();

            return sb.toString();

        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return json_str;
    }