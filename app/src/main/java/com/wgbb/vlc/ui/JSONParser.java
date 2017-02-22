package com.wgbb.vlc.ui;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.util.Log;

/**
 * Created by GianniYan on 2016/3/15.
 */
public class JSONParser extends AsyncTask< String , Void, StringBuilder> {

    InputStream is = null;
    //JSONObject jObj = null;
    String json = "";
    StringBuilder sb = null;
    // constructor
    protected StringBuilder doInBackground(  String... us ) {
        //String page = new Communicator().executeHttpGet(us[0]);
        String s = us[0];
        // Making HTTP request
        InputStream  is = null;
       // String url = "https://api.myjson.com/bins/3bbs8";
       String url = "http://www.chineseradionetwork.com/datafeeds/radiopath.svc/getStreamFileList";
        int timeout = 5000;
        HttpURLConnection c = null;
        try {
            URL u = new URL(url);
            c = (HttpURLConnection) u.openConnection();
            c.setRequestMethod("GET");
            c.setRequestProperty("Content-length", "0");
            c.setUseCaches(false);
            c.setAllowUserInteraction(false);
            c.setConnectTimeout(timeout);
            c.setReadTimeout(timeout);
            c.connect();
            int status = c.getResponseCode();

            switch (status) {
                case 200:
                case 201:
                    is = c.getInputStream();

                    //System.out.println(is.toString());
                    BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
                    sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        System.out.println(line);
                        System.out.println("123");
                        sb.append(line);

                        //System.out.println(line);
                    }
                    //json = sb.toString();
                    //json = json.replace("\\", "");
                    //json = sb.toString();
                    //System.out.println(sb);
                    br.close();
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb;
        //return json;
    }

    protected void onPostExecute(StringBuilder message) {
        //process message;
        super.onPostExecute(message);
        System.out.println(message);
    }
}
