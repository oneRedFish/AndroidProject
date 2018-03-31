package com.example.minyiyang.androidlabs;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;

public class MessageDetails extends Activity {
    protected static final String ACTIVITY_NAME = "MessageDetails";

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_message_details);
        Bundle infoToPass = getIntent().getExtras();
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        MessageFragment df = new MessageFragment();
        df.setArguments(infoToPass);
        ft.addToBackStack(null); //only undo FT on back button
        ft.replace(R.id.emptyFrame, df);
        ft.commit();
        Log.i(ACTIVITY_NAME, "is finished");
    }

}


