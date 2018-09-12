package com.example.kj.lexithymia;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by kapashnin on 10.09.18.
 */

public class AssetsAccess {

    Context mContext;
    public AssetsAccess(Context c) {
        mContext = c;
    }

    public String get() {
        String ret = null;
           try {
                InputStream ins;
                BufferedReader in;

                ins = mContext.getAssets().open("feelings.json");

                in = new BufferedReader(new InputStreamReader(ins));
                String readLine;
                StringBuffer buf = new StringBuffer();
                while ((readLine = in.readLine()) != null) {
                    buf.append(readLine);
                }
                ret = buf.toString();


                if (null != in) {
                    in.close();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return ret;
        }

}
