package com.example.sev_user.bookmanager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import database.Alarm;
import database.DatabaseHelper;

/**
 * Created by Administrator on 12/12/2017.
 */

public class AlarmAdapter extends ArrayAdapter<Alarm> {

    private ArrayList<Alarm> mdata;
    private LayoutInflater mlayLayoutInflater;
    DatabaseHelper mDatabaseHelper=new DatabaseHelper(getContext());


    public AlarmAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Alarm>  objects) {
        super(context, resource, objects);
        this.mdata = objects;
        this.mlayLayoutInflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = mlayLayoutInflater.inflate(R.layout.reminder_layout, null);
        }

        Alarm alarm =mdata.get(position);

        if(alarm != null) {
            TextView tvBookName = (TextView) view.findViewById(R.id.tv_book_name);
            tvBookName.setText(mDatabaseHelper.getBook(alarm.getmIdBook()).getmTitle());
            TextView tv_timetoread = (TextView) view.findViewById(R.id.tv_time_to_read);
            tv_timetoread.setText(alarm.getmTime().toString());

        }
        return view;
    }
}
