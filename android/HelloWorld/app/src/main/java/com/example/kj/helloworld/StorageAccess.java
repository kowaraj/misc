package com.example.kj.helloworld;

import android.content.Context;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by kapashnin on 10.09.18.
 */

public class StorageAccess {
    Context mContext;
    String mFilename;
    File mFile;

    public StorageAccess(Context mContext_) {
        mContext = mContext_;
        mFilename = "feelings.json";
        final File dir = new File(mContext.getFilesDir(), "");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        mFile = new File(dir, mFilename);
        if (!mFile.exists()) {
            try {
                mFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void storeData(String json_str) {
        FileOutputStream os;

        try {
            os = mContext.openFileOutput(mFile.getName(), Context.MODE_PRIVATE);
            os.write(json_str.getBytes());
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String loadData() {
        String json_str = null;

        try {
            FileInputStream ins = mContext.openFileInput(mFile.getName());
            InputStreamReader isr = new InputStreamReader(ins);
            BufferedReader br = new BufferedReader(isr);

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            ins.close();

            return sb.toString();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json_str;
    }
}