package com.example.parth.alarmclock;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Parth on 27-03-2016.
 */
public class alarmAdapter extends RecyclerView.Adapter<alarmAdapter.ViewHolder> {


    public static class ViewHolder extends RecyclerView.ViewHolder {
        //CardView cv;
        TextView personName;

        public ViewHolder(View itemView) {

            super(itemView);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }
}
