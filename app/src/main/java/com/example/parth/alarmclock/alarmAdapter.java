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

public class alarmAdapter extends ArrayAdapter<Alarm> {

    private ArrayList<Alarm> alarms;
    Context c;

    // public alarmAdapter(Context context,int resource, int id, ArrayList<String> AlarmList, ArrayList<String> AlarmNameList) {
    public alarmAdapter(Context context, int resource, int id, ArrayList<Alarm> alarms) {

        super(context, R.layout.alarm_list_view, R.id.alarmTime, alarms);

        this.alarms = alarms;
        this.c = context;
    }


    public static class ViewHolder {

        TextView alarmTime;
        TextView alarmName;
        CheckBox OnOff;

    }

    ViewHolder vh;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        vh = null;
        View view = convertView;
        if (convertView == null) {
            vh = new ViewHolder();
            LayoutInflater vi = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            // vi = LayoutInflater.from(getContext());
            view = vi.inflate(R.layout.alarm_list_view, parent, false);
            vh.alarmTime = (TextView) view.findViewById(R.id.alarmTime);
            vh.alarmName = (TextView) view.findViewById(R.id.alarmName);
            vh.OnOff = (CheckBox) view.findViewById(R.id.onOffButton);

            view.setTag(vh);
        } else
            vh = (ViewHolder) view.getTag();

        int id = alarms.get(position).getAlarmId();
        AlarmDatabase alarmDatabase = new AlarmDatabase(getContext());
        Alarm alarm = alarmDatabase.getAlarm(id);
        Log.v("in list view adapter", "id: " + alarm.getAlarmId());
        vh.alarmTime.setText(alarms.get(position).getTimeInString());
        String string = alarms.get(position).getAlarmName();
        if(string.equals("")) {
            vh.alarmName.setVisibility(View.GONE);
        }else{
            vh.alarmName.setText(alarm.getAlarmName());
        }
        vh.OnOff.setChecked(alarm.getIsActive());
        Log.v("in alarm adapter", "is active: " + alarm.getIsActive());
        if(vh.OnOff.isChecked() == false){
            alarm.setIsActive(false);
        }
        return view;
    }
}
