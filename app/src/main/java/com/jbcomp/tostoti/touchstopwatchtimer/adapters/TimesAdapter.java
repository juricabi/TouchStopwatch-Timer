package com.jbcomp.tostoti.touchstopwatchtimer.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.jbcomp.tostoti.touchstopwatchtimer.MainActivity;
import com.jbcomp.tostoti.touchstopwatchtimer.R;
import com.jbcomp.tostoti.touchstopwatchtimer.models.SavedTime;
import com.jbcomp.tostoti.touchstopwatchtimer.stopwatch.TimerActivity;
import com.jbcomp.tostoti.touchstopwatchtimer.stopwatch.TimesActivity;

import java.io.IOException;
import java.util.List;

import static android.content.ContentValues.TAG;

public class TimesAdapter extends RecyclerView.Adapter<TimesAdapter.ViewHolder>  {


    public static class ViewHolder extends RecyclerView.ViewHolder{

        private SavedTime mTime;

        private TextView mTextView;

        public ViewHolder(View view) {
            super(view);
            mTextView = (TextView) view;
        }

        public void setItem(SavedTime time, Context context, Integer position) {
            mTime = time;
            mTextView.setText(mTime.getTime().toString());
        }

    }

    private List<SavedTime> timesDataset;

    public Context getContext() {
        return context;
    }

    private Context context;

    public TimesAdapter(List<SavedTime> dataset, Context context) {
        timesDataset = dataset;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {



        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.time_recycler_box, parent, false);

        final ViewHolder holder = new ViewHolder(v);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pass the 'context' here
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                alertDialog.setTitle("Timer od Delete");
                alertDialog.setMessage("Choose one.");
                alertDialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        holder.mTime.delete();
                        ((TimesActivity)context).refreshContent();

                    }
                });
                alertDialog.setNegativeButton("Timer", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(context.getApplicationContext(), TimerActivity.class);
                        intent.putExtra("SavedTime", holder.mTime);
                        context.getApplicationContext().startActivity(intent);

                    }
                });

                alertDialog.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                AlertDialog dialog = alertDialog.create();
                dialog.show();
            }
        });

        ViewHolder vh = holder;
        return  vh;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setItem(timesDataset.get(position),context, position);
    }

    @Override
    public int getItemCount() {
        return timesDataset.size();
    }
}
