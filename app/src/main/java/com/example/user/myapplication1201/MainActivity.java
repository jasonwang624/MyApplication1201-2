package com.example.user.myapplication1201;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    TextView tv,tv2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = (TextView) findViewById(R.id.textView);
        tv2 = (TextView) findViewById(R.id.textView2);
    }
    public void click1(View v)
    {
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        StringRequest request = new StringRequest("https://temperatuer-6972c.firebaseio.com/temp.json",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            double temp = obj.getDouble("temp");
                            tv.setText(String.valueOf(temp));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(request);
        queue.start();

    }
    public void click2(View v)
    {
        GetData t = new GetData();
        t.start();
    }


    private class GetData extends Thread{
        InputStream inputStream = null;
        @Override
        public void run()
        {
            URL url = null;
            while (true)
            {
                try {
                    url = new URL("https://temperatuer-6972c.firebaseio.com/temp.json");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.connect();
                    inputStream = conn.getInputStream();

                    //==============
                    ByteArrayOutputStream result = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = inputStream.read(buffer)) != -1) {
                        result.write(buffer, 0, length);
                    }
                    String str = result.toString();
                    JSONObject obj = new JSONObject(str);
                    final double temp = obj.getDouble("temp");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tv.setText("溫度:"+String.valueOf(temp));
                            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                            Date now = new Date();
                            String strDate = sdf.format(now);
                            tv2.setText("現在時間:" +strDate);
                        }
                    });
                    Thread.sleep(1000);

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }


        }
    }
}
