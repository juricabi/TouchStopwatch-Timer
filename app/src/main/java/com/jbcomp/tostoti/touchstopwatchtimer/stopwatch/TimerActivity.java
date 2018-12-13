package com.jbcomp.tostoti.touchstopwatchtimer.stopwatch;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.view.MotionEvent;
import android.view.View;

import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.jbcomp.tostoti.touchstopwatchtimer.R;
import com.jbcomp.tostoti.touchstopwatchtimer.models.SavedTime;

import static java.lang.Math.abs;
import static java.lang.Math.toIntExact;


public class TimerActivity extends Activity {

    private FloatingActionButton startButton;
    private TextView timeTextView;
    private NumberPicker np;
    private long startTime = 0L;
    ProgressBar pb;

    private Handler customHandler = new Handler();

    long timeInMilliseconds = 0L;

    long timeSwapBuff = 0L;

    long updatedTime = 0L;

    SavedTime maxTime;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        timeTextView = findViewById(R.id.timeView);
        startButton = findViewById(R.id.startButton);
        np = findViewById(R.id.np);
        pb = findViewById(R.id.progressBar);



        maxTime = (SavedTime)getIntent().getSerializableExtra("SavedTime");
        timeTextView.setText(milsToText(maxTime.getTime()));
        pb.setMax(2000);
        if(maxTime.getTime() > 2000)
        {
            pb.setProgress(2000);
        }
        else{
            pb.setProgress(toIntExact(maxTime.getTime()));
        }


        final String[] timeValues = new String[51];
        {
            timeValues[25] = maxTime.getTime().toString();

            for(int i = 0; i<25; i++)
            {
                Long diff = maxTime.getTime() - 10 * (25 - i);
                timeValues[i] = Long.toString(diff);
            }

            for(int i = 1; i<26; i++)
            {
                Long diff = maxTime.getTime() + 10 * i;
                timeValues[i+25] = Long.toString(diff);
            }


        }

        np.setMaxValue(timeValues.length-1);
        np.setMinValue(0);
        np.setWrapSelectorWheel(false);
        np.setDisplayedValues(timeValues);
        np.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        np.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int pickValue = np.getValue();
                maxTime = new SavedTime(Long.parseLong(timeValues[pickValue]));
                timeTextView.setText(milsToText(maxTime.getTime()));
                maxTime.save();
                if(maxTime.getTime() > 2000)
                {
                    pb.setProgress(2000);
                }
                else{
                    pb.setProgress(toIntExact(maxTime.getTime()));
                }
            }
        });




        startButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        timeSwapBuff = 0L;
                        pb.setProgress(toIntExact(maxTime.getTime()));
                        startTime = SystemClock.uptimeMillis() + maxTime.getTime();
                        customHandler.postDelayed(updateTimerThread, 0);
                        break;

                    case MotionEvent.ACTION_UP:

                        SavedTime savedTime = new SavedTime(updatedTime);

                        timeSwapBuff += timeInMilliseconds;
                        customHandler.removeCallbacks(updateTimerThread);


                    case MotionEvent.ACTION_CANCEL:
                        // RELEASED
                        break;
                }
                return false;
            }
        });

    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private Runnable updateTimerThread = new Runnable() {

        public void run() {

            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
            if(abs(timeInMilliseconds) < 2000)
            {
                pb.setProgress(toIntExact(abs(timeInMilliseconds)));
            }
            updatedTime = timeSwapBuff + abs(timeInMilliseconds);
            timeTextView.setText(milsToText(updatedTime));
            customHandler.postDelayed(this, 0);

        }

    };


    private String milsToText(long mils)
    {


        int secs = (int) (mils / 1000);
        int mins = secs / 60;
        secs = secs % 60;
        int milliseconds = (int) (mils % 1000);

        return "" + mins + ":"
                + String.format("%02d", secs) + ":"
                + String.format("%03d", milliseconds);

    }


}

