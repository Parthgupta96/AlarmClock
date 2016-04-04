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
  private ArrayList<String> alarmNameList;
  Context c;

    public alarmAdapter(Context context,int resource, int id, ArrayList<String> AlarmList, ArrayList<String> AlarmNameList) {
        super(context, R.layout.alarm_list_view, R.id.alarmTime, AlarmList);
        this.alarmList = AlarmList;
        this.alarmNameList = AlarmNameList;
        this.c = context;
    }


    public static class ViewHolder{

        TextView alarmTime;
        TextView alarmName;
        CheckBox OnOff;

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        View view = convertView;
        if(convertView==null){
            Log.v("error","in get view");
             vh = new ViewHolder();
            LayoutInflater vi = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

           // vi = LayoutInflater.from(getContext());
            view = vi.inflate(R.layout.alarm_list_view, parent, false);
            vh.alarmTime = (TextView)view.findViewById(R.id.alarmTime);
            vh.alarmName = (TextView)view.findViewById(R.id.alarmName);
            vh.OnOff = (CheckBox)view.findViewById(R.id.onOffButton);

            view.setTag(vh);
        }
        else
            vh = (ViewHolder)view.getTag();


        vh.alarmTime.setText(alarmList.get(position));
        vh.alarmName.setText(alarmNameList.get(position));
        vh.OnOff.setChecked(true);

        return view;
    }
}
