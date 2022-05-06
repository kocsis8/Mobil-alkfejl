package com.example.online_aram_lejelents;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class RecordsListAdapter extends ArrayAdapter<Meres> {
    private static final String TAG = "RecordsListAdapter";

    private final Context mContext;
    int mResource;

    public RecordsListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Meres> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mResource = resource;
    }

    @SuppressLint("ViewHolder")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
         String serial = getItem(position).getSerial();
         String allas = getItem(position).getAllas();
         String date = getItem(position).getDate();

         Meres meres = new Meres(serial,allas,date);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource,parent,false);

        TextView tvserial = (TextView) convertView.findViewById(R.id.textView1);
        TextView tvallas = (TextView) convertView.findViewById(R.id.textView2);
        TextView tvdate = (TextView) convertView.findViewById(R.id.textView3);

        tvserial.setText(date);
        tvallas.setText("Gy.sz.:"+serial);
        tvdate.setText(allas);

        return convertView;

    }
}
