package com.jbcomp.tostoti.touchstopwatchtimer.stopwatch;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.jbcomp.tostoti.touchstopwatchtimer.R;
import com.jbcomp.tostoti.touchstopwatchtimer.adapters.TimesAdapter;
import com.jbcomp.tostoti.touchstopwatchtimer.models.SavedTime;
import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.sql.Time;
import java.util.List;


public class TimesActivity extends Activity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private Button clearButton;
    private RecyclerView.LayoutManager mLayoutManager;
    private SwipeRefreshLayout srl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_times);
        srl = findViewById(R.id.activity_main_swipe_refresh_layout);

        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                                     @Override
                                     public void onRefresh() {
                                         refreshContent();
                                     }
                                 });



        clearButton = findViewById(R.id.button_clear);

        clearButton.setOnClickListener(new View.OnClickListener() {
             @Override
              public void onClick(View v) {
                 AlertDialog.Builder builder1 = new AlertDialog.Builder(TimesActivity.this);
                 builder1.setMessage("Really?");
                 builder1.setCancelable(true);

                 builder1.setPositiveButton(
                         "Yes",

                         new DialogInterface.OnClickListener() {
                             public void onClick(DialogInterface dialog, int id) {
                                 Delete.table(SavedTime.class);
                                 setmRecyclerView();
                             }
                         });

                 builder1.setNegativeButton(
                         "No",
                         new DialogInterface.OnClickListener() {
                             public void onClick(DialogInterface dialog, int id) {
                                 dialog.cancel();
                             }
                         });

                 AlertDialog alert11 = builder1.create();
                 alert11.show();

            }
        });


        setmRecyclerView();


    }


    private void setmRecyclerView(){
        mRecyclerView = (RecyclerView) findViewById(R.id.times_recycler_view);


        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);


        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        List<SavedTime> timesList = SQLite.select().
                from(SavedTime.class).queryList();

        mAdapter = new TimesAdapter(timesList, this);

        mRecyclerView.addItemDecoration(new DividerItemDecoration(mRecyclerView.getContext(),DividerItemDecoration.HORIZONTAL));

        mRecyclerView.setAdapter(mAdapter);


    }


    public void refreshContent(){
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                setmRecyclerView();
                srl.setRefreshing(false);
            }
            });
        }

}
