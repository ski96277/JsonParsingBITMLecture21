package com.example.imransk.jsonparsingbitmlecture21;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private final String Flowers_name = "http://services.hanselandpetal.com/feeds/flowers.json";
    TextView FlowerTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FlowerTv = (TextView) findViewById(R.id.FlowerTV);

        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();

        if (info.isAvailable() && info.isConnected()) {
            //excute the MyJosonDownloadTask class and send the parameter string flower_name
            new MyJosonDownloadTask().execute(Flowers_name);
        } else {

        }

    }

//Create class that name MyJosonDownloadTask and extends AsyncTask

    private class MyJosonDownloadTask extends AsyncTask<String, Void, String> {

        // this method alwas work in Background
        @Override
        protected String doInBackground(String... params) {

            URL url = null;
            try {
                url = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();
// store in InputStream as a 0 ,1
                InputStream inputStream = connection.getInputStream();
// Now create it in human read able
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
// Now Convert  it in String
                BufferedReader reader = new BufferedReader(inputStreamReader);
                StringBuilder sb = new StringBuilder();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }

                return sb.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        // this method alwas work in main thred that mean UI
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
//Now Parsing the Json Array
            try {
                JSONArray jsonArray = new JSONArray(s);
                JSONObject jsonObject = jsonArray.getJSONObject(1);
                String Flowername = jsonObject.getString("name");
                FlowerTv.setText(Flowername);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        // this method alwas work in main thred that mean UI
// if you want to show how kb is download use by this method
        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }
}
