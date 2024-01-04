package com.example.weather;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class DetailWeatherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_weather);

        Intent intent = getIntent();
        String city = intent.getStringExtra("name");
        get7DayWeather(city);
    }

    public void get7DayWeather(String city) {
        String apiKey = "5576e67f17c97aeee91a13e7c2b14ad0";
        String url = "http://api.openweathermap.org/data/2.5/forecast?q=" + city + "&appid=" + apiKey;

        RequestQueue requestQueue = Volley.newRequestQueue(DetailWeatherActivity.this);
        StringRequest request = new StringRequest(Request.Method.GET, url,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );
        requestQueue.add(request);
    }

}