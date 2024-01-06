package com.example.weather;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Adapter extends BaseAdapter {
    Context context;
    ArrayList<Weather> arrayList;

    public Adapter(Context context, ArrayList<Weather> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (view == null) {
            view = inflater.inflate(R.layout.detail, null);
        }

        Weather weather = arrayList.get(i);

        TextView txtDay = (TextView) view.findViewById(R.id.textviewNgay);
        TextView txtStatus = (TextView) view.findViewById(R.id.textviewTrangThai);
        TextView txtMaxTemp = (TextView) view.findViewById(R.id.textviewMaxTemp);
        TextView txtMinTemp = (TextView) view.findViewById(R.id.textviewMinTemp);
        ImageView imgStatus = (ImageView) view.findViewById(R.id.imageviewTrangthai);

        txtDay.setText(weather.Day);
        txtStatus.setText(weather.Status);
        txtMaxTemp.setText(weather.MaxTemp + "°C");
        txtMinTemp.setText(weather.MinTemp + "°C");

       /* Picasso.with(context)
                .load("http://openweathermap.org/img/wn/" + weather.Image + ".png")
                .into(imgStatus);*/
        Picasso.with(context).load("https://openweathermap.org/img/wn/" + weather.Image + ".png").into(imgStatus);

        return view;
    }
}

