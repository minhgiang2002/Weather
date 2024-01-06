package com.example.weather;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import android.media.session.PlaybackState;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DetailWeatherActivity extends AppCompatActivity {
    String City1="";
    ImageView imgback;
    TextView txtName;
    ListView Lv;
    Adapter Adapter;
    ArrayList<Weather> MangWeather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_weather);
        Anhxa();

        Intent intent = getIntent();
        String City = intent.getStringExtra("name");
        Log.d("ketqua", "Dữ liệu truyền qua: " + City);

        if (City == null || City.equals("")) {
            City1 = "Saigon";
            get7DayWeather(City1);
        } else {
            City1 = City;
            get7DayWeather(City1);
        }


    }

    private void Anhxa () {
        imgback = (ImageView) findViewById (R.id. imageviewBack);
        txtName = (TextView) findViewById(R.id.textviewTenTP);
        Lv = (ListView) findViewById(R.id.listview);
        MangWeather = new ArrayList<Weather>();
        Adapter = new Adapter(DetailWeatherActivity.this, MangWeather);
        Lv.setAdapter(Adapter);

    }

    public void get7DayWeather(String city) {
        String url = "https://api.openweathermap.org/data/2.5/forecast/daily?q="+city+"&lang=vi&units=metric&appid=bd5e378503939ddaee76f12ad7a97608";
        RequestQueue requestQueue = Volley.newRequestQueue(DetailWeatherActivity.this);
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonobject = new JSONObject(response);
                            JSONObject jsonobjectcity = jsonobject.getJSONObject("city");
                            String name =jsonobjectcity.getString("name");
                            txtName.setText(name);

                            JSONArray jsonArrayList = jsonobject.getJSONArray("list");
                            for(int i = 0 ;i <jsonArrayList.length();i++) {
                                JSONObject jsonObjectlist = jsonArrayList.getJSONObject(i);
                                String day = jsonObjectlist.getString("dt");

                                long l = Long.valueOf(day);
                                Date date = new Date(l * 1000L);
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE yyyy-MM-dd ") ;
                                String Day = simpleDateFormat.format(date);
                                JSONObject jsonObjectTemp = jsonObjectlist.getJSONObject ("temp") ;
                                String max = jsonObjectTemp.getString ("max") ;
                                String min = jsonObjectTemp.getString ("min") ;

                                Double a = Double.valueOf(max) ;
                                Double b = Double.valueOf(min);
                                String NhietdoMax = String. valueOf(a.intValue ());
                                String NhietdoMin = String. valueOf(b.intValue()) ;
                                JSONArray jsonArrayWeather = jsonObjectlist.getJSONArray ("weather") ;
                                JSONObject jsonObjectWeather = jsonArrayWeather.getJSONObject (0) ;
                                String status = jsonObjectWeather.getString ("description") ;
                                String icon = jsonObjectWeather.getString ("icon") ;

                                    MangWeather.add (new Weather (Day, status, icon, NhietdoMax, NhietdoMin) ) ;



                            }
                            Adapter.notifyDataSetChanged();
                        }catch (JSONException e)
                        {
                            e.printStackTrace();
                        }

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