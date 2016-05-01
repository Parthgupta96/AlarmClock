package com.example.parth.alarmclock;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.undo.UndoAdapter;

import java.util.ArrayList;

/**
 * Created by Parth on 27-03-2016.
 */

public class alarmAdapter extends ArrayAdapter<Alarm> implements UndoAdapter{
private MainActivity main ;
    private ArrayList<Alarm> alarms;
    Context c;
    Alarm currentAlarm;
    Cursor cursor;


    // public alarmAdapter(Context context,int resource, int id, ArrayList<String> AlarmList, ArrayList<String> AlarmNameList) {
    public alarmAdapter(Context context, int resource, int id, ArrayList<Alarm> alarms) {

        super(context, R.layout.alarm_list_view, R.id.alarmTime, alarms);

        this.alarms = alarms;
        this.c = context;
        main = (MainActivity)context;
    }

    @NonNull
    @Override
    public View getUndoView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(c).inflate(R.layout.undo_row, parent, false);
        }
        return view;
    }

    @NonNull
    @Override
    public View getUndoClickView(@NonNull View view) {
        return view.findViewById(R.id.undo_row_undobutton);
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
            view = vi.inflate(R.layout.alarm_list_view, parent, false);
            vh.alarmTime = (TextView) view.findViewById(R.id.alarmTime);
            vh.alarmName = (TextView) view.findViewById(R.id.alarmName);
            vh.OnOff = (CheckBox) view.findViewById(R.id.onOffButton);

            view.setTag(vh);
        } else
            vh = (ViewHolder) view.getTag();

        int id = alarms.get(position).getAlarmId();
        final AlarmDatabase alarmDatabase = new AlarmDatabase(getContext());

        //final Alarm alarm = alarmDatabase.getAlarm(id);
        final Alarm alarm = alarms.get(position);

        vh.alarmTime.setText(alarm.getTimeInString());

        String string = alarm.getAlarmName();
        if(string.equals("")) {
            vh.alarmName.setVisibility(View.GONE);
        }else{
            vh.alarmName.setVisibility(View.VISIBLE);
            vh.alarmName.setText(alarm.getAlarmName());
        }
        vh.OnOff.setChecked(alarm.getIsActive());
        //Log.v("in alarm adapter", "is active: " + alarm.getIsActive());
        vh.OnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //alarm.getAlarmId();
                //alarm.setIsActive(!alarm.getIsActive());
                if (alarm.getIsActive()) {//now inActive
                } else {
                    //alarm.scheduleAlarm(getContext(),!alarm.getIsActive());
                    alarm.showSnackbar();
                }
                //alarmDatabase.updateData();
                alarmDatabase.updateData(alarm,!alarm.getIsActive());
                main.scheduleNextAlarm();
                main.updateListView();

            }
        });
        return view;
    }
}
