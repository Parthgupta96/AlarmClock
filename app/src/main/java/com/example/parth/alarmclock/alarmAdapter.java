package com.example.parth.alarmclock;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Parth on 27-03-2016.
 */

public class alarmAdapter extends ArrayAdapter<String> {
  private ArrayList<String> alarmList;
    String string;

    public alarmAdapter(Context context,int resource,String AlarmList) {
        super(context, resource);
       // alarmList = AlarmList;
        Log.v("error","in const");
        string = AlarmList;
    }


    public static class ViewHolder{
        //CardView cv;
        TextView alarmTime;
        CheckBox OnOff;

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        View view = convertView;
        if(convertView==null){
            Log.v("error","in get view");
             vh = new ViewHolder();
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            convertView = vi.inflate(R.layout.alarm_list_view, null);
            vh.alarmTime = (TextView)convertView.findViewById(R.id.alarmTime);
            vh.OnOff = (CheckBox)convertView.findViewById(R.id.onOffButton);

        }

        vh.alarmTime.setText("hi");
        vh.OnOff.setChecked(true);

        return convertView;
    }
//
//    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//
//        return null;
//    }
//
//
//    public int getItemCount() {
//        return 0;
//    }
//
//
//    public void onBindViewHolder(ViewHolder holder, int position) {
//
//    }
}
