package com.example.kj.lexithymia;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by kapashnin on 10.09.18.
 */

public class JsonServerProcessor extends AsyncTask<String, Void, Integer>
{

        ProgressDialog progressDialog;
        int delay = 5000;
        Context mContext;
        public JsonServerProcessor(Context _mContext) {
            super();
            mContext = _mContext;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(mContext, "Wait", "Downloading...");
        }

        @Override
        protected Integer doInBackground(String... strings)
        {
            StorageAccess sa = new StorageAccess(mContext);

            if (strings[0] == "read") {
                sa.storeData(readJsonServer());
            } else {
                String json_str = sa.loadData();

                writeJsonServer(json_str);
            }
            return 0;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            progressDialog.dismiss();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            progressDialog.dismiss();
        }

    public Integer writeJsonServer(String json_str)
    {
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni != null && ni.isConnected()) {

            //////////// write the file
            try {
                HttpURLConnection huc = null;
                DataOutputStream dos;
                DataInputStream dis;

                URL url = new URL(Constants.JSON_URL_UPDATE);
                huc = (HttpURLConnection) url.openConnection();
                huc.setDoInput(true);
                huc.setDoOutput(true);
                huc.setUseCaches(false);
                huc.setRequestMethod("POST");

                huc.setRequestProperty("Content-Type", "application/json");
                huc.setRequestProperty("Accept", "application/json");
                huc.setRequestProperty("Content-Length", ""+json_str.getBytes().length);
                huc.setConnectTimeout(15000);
                huc.setRequestProperty("charset", "utf-8");
                huc.setInstanceFollowRedirects(false);
                huc.connect();


                dos = new DataOutputStream(huc.getOutputStream());
                dos.write(json_str.getBytes());
                dos.flush();
                dos.close();

                int stat = huc.getResponseCode();
                BufferedInputStream in;
                if (stat == 200) {
                    //in = new BufferedInputStream(huc.getInputStream());
                    //String response = convertIsToStr(in);
                    //TextView tv = tv.findViewById();
                    //tv.setText("res="+stat);

                }
                huc.disconnect();

                return stat;

            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            // no connection
        }
        return -1;
    }

    public String readJsonServer()
    {
        String response = null;

        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni != null && ni.isConnected()) {

            try {
                InputStream ins;
                BufferedInputStream in;
                HttpURLConnection huc = null;

                URL url = new URL(Constants.JSON_URL_UPDATE);
                huc = (HttpURLConnection) url.openConnection();
                huc.setRequestProperty("Content-Type", "application/json");
                huc.setRequestProperty("Accept",       "application/json");
                huc.setRequestMethod("GET");
                int stat = huc.getResponseCode();

                if (stat == 200) {
                    in = new BufferedInputStream(huc.getInputStream());
                    response = convertIsToStr(in);
                }
                huc.disconnect();
                return response;

            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            // no connection
        }
        return response;
    }

    private String convertIsToStr(InputStream is) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line = "";
        String res = "";
        while ((line = br.readLine()) != null) {
            res += line;
        }
        if (null !=is) {
            is.close();
        }
        return res;
    }

}
