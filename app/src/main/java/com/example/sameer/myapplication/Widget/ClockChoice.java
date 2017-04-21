package com.example.sameer.myapplication.Widget;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RemoteViews;

import com.example.sameer.myapplication.R;

/**
 * Created by Sameer on 4/21/2017.
 */

public class ClockChoice extends AppCompatActivity implements View.OnClickListener {
    private SharedPreferences clockPrefs;
    //count of designs
    private int numDesigns;
    //image buttons for each design
    private ImageButton[] designBtns;
    //identifiers for each clock element
    private int[] designs;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clock_choice);
        numDesigns = this.getResources().getInteger(R.integer.num_clocks);
        designBtns = new ImageButton[numDesigns];
        designs = new int[numDesigns];
        for(int d=0; d<numDesigns; d++){
            designs[d] = this.getResources().getIdentifier
                    ("AnalogClock"+d, "id", getPackageName());
            designBtns[d]=(ImageButton)findViewById(this.getResources().getIdentifier
                    ("design_"+d, "id", getPackageName()));
            designBtns[d].setOnClickListener(this);
        }
        clockPrefs = getSharedPreferences("CustomClockPrefs", 0);

    }

    @Override
    public void onClick(View v) {
        int picked = -1;
        for(int c=0; c<numDesigns; c++){
            if(v.getId()==designBtns[c].getId()){
                picked=c;
                break;
            }
        }
        int pickedClock = designs[picked];
        RemoteViews remoteViews = new RemoteViews
                (this.getApplicationContext().getPackageName(),
                        R.layout.clock_widget_layout);
        for(int d=0; d<designs.length; d++){
            if(d!=pickedClock)
                remoteViews.setViewVisibility(designs[d], View.INVISIBLE);
        }
        remoteViews.setViewVisibility(pickedClock, View.VISIBLE);
        //get component name for widget class
        ComponentName comp = new ComponentName(this, ClockWidget.class);
//get AppWidgetManager
        AppWidgetManager appWidgetManager =
                AppWidgetManager.getInstance(this.getApplicationContext());
//update
        appWidgetManager.updateAppWidget(comp, remoteViews);
        SharedPreferences.Editor custClockEdit = clockPrefs.edit();
        custClockEdit.putInt("clockdesign", picked);
        custClockEdit.commit();
        finish();
    }
}
