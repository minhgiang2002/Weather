package com.example.weather;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    EditText edtTenThanhPho;
    TextView tvTenTp, tvTenQg, tvNhietDo, tvDoAm, tvGio, tvMay, tvNgayThang, tvTrangThai;
    Button btnChon, btnTiepTheo;
    ImageView imgIcon;

    static final String DEFAULT_CITY = "Saigon";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

        getCurrentWeatherData(DEFAULT_CITY);
        clickButton();
    }

    public void getCurrentWeatherData(String city) {
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&lang=vi&units=metric&appid=5576e67f17c97aeee91a13e7c2b14ad0";
        //https://api.openweathermap.org/data/2.5/forecast/daily?lat=44.34&lon=10.99&cnt=7&appid=5576e67f17c97aeee91a13e7c2b14ad0
        //https://api.openweathermap.org/data/2.5/forecast?q=London&appid=5576e67f17c97aeee91a13e7c2b14ad0

        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("KetQua", response);
                        // Lay du lieu tu json
                        processResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("WeatherApp", "Error for city " + city, error);
                    }
                }
        );
        queue.add(request);
    }


    private void init() {
        edtTenThanhPho = findViewById(R.id.edtTenTp);
        tvTenTp = findViewById(R.id.tvThanhPho);
        tvTenQg = findViewById(R.id.tvQuocGia);
        tvDoAm = findViewById(R.id.tvDoAm);
        tvGio = findViewById(R.id.tvGio);
        tvMay = findViewById(R.id.tvMay);
        tvNgayThang = findViewById(R.id.tvNgayThang);
        btnChon = findViewById(R.id.btnThanhPho);
        btnTiepTheo = findViewById(R.id.btnTiepTheo);
        imgIcon = findViewById(R.id.imgThoiTiet);
        tvNhietDo = findViewById(R.id.tvNhietDo);
        tvTrangThai = findViewById(R.id.tvTrangThai);
    }

    private void processResponse(String response) {
        try {
            JSONObject object = new JSONObject(response);

            // Remove the following line as "list" is not present in the API response
            // JSONObject jsonObject = object.getJSONArray("list").getJSONObject(0);

            // Set ten thanh pho, quoc gia
            String name = object.getString("name");
            tvTenTp.setText(name);

            JSONObject jsonObjectCountry = object.getJSONObject("sys");
            String quocGia = jsonObjectCountry.getString("country");
            tvTenQg.setText(quocGia);

            // Set text cho ngay (Chua co)
            String day = object.getString("dt");
            long lDay = Long.valueOf(day);
            Date date = new Date(lDay * 1000L);
            SimpleDateFormat sdf = new SimpleDateFormat("E, dd MMM yyyy HH:mm");
            String strDay = sdf.format(date);
            tvNgayThang.setText(strDay);

            // Set icon cho imgIcon
            JSONArray jsonArrayWeather = object.getJSONArray("weather");
            JSONObject jsonObjectWeather = jsonArrayWeather.getJSONObject(0);
            String status = jsonObjectWeather.getString("main");
            String icon = jsonObjectWeather.getString("icon");

            //Picasso.with(MainActivity.this).load("http://openweathermap.org/img/wn/" +icon+ "@2x.png").into(imgIcon);

            String vietnameseStatus = translateStatusToVietnamese(status);
            tvTrangThai.setText(vietnameseStatus);

            Picasso.with(MainActivity.this).load("https://openweathermap.org/img/wn/" + icon + ".png").into(imgIcon);
            //https://openweathermap.org/img/wn/04n.png04d
            // Lay gia tri nhiet do, do am , gio...
            JSONObject jsonObjectMain = object.getJSONObject("main");
            String strNhietDo = jsonObjectMain.getString("temp");
            String doAm = jsonObjectMain.getString("humidity");
            double temperature = jsonObjectMain.getDouble("temp");
            String nhietDo= String.valueOf((int) temperature);
            tvNhietDo.setText(nhietDo + "°C");
            tvDoAm.setText(doAm + "%");

            // Lay gia tri gio, may
            JSONObject jsonObjectWind = object.getJSONObject("wind");
            String gio = jsonObjectWind.getString("speed");
            tvGio.setText(gio + "m/s");
            JSONObject jsonObjectCloud = object.getJSONObject("clouds");
            String may = jsonObjectCloud.getString("all");
            tvMay.setText(may + "%");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    private void clickButton() {
        btnChon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city = edtTenThanhPho.getText().toString();
                getCurrentWeatherData(city);

            }
        });
        btnTiepTheo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city = edtTenThanhPho.getText().toString();
                Intent intent = new Intent(MainActivity.this, DetailWeatherActivity.class);
                intent.putExtra("name", city);
                startActivity(intent);

            }
        });
    }
   private String translateStatusToVietnamese(String englishStatus) {
       switch (englishStatus) {
           case "Clear":
               return "Trời quang đãng";
           case "Clouds":
               return "Nhiều mây";
           case "Few clouds":
               return "Mây ít";
           case "Scattered clouds":
               return "Mây rải rác";
           case "Broken clouds":
               return "Mây rải rác";
           case "Shower rain":
               return "Mưa rào";
           case "Rain":
               return "Mưa";
           case "Thunderstorm":
               return "Dông";
           case "Snow":
               return "Tuyết";
           case "Mist":
               return "Sương mù";
           default:
               return englishStatus;
       }
   }




}
