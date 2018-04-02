package com.example.minyiyang.androidlabs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import static com.example.minyiyang.androidlabs.R.id.button;

public class StartActivity extends Activity {
    protected static final String ACTIVITY_NAME = "StartActivity";
    Button btn;
    Button chat;
    Button weather;
    Button toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Log.i(ACTIVITY_NAME, "In onCreate");

        btn = (Button) findViewById(R.id.button);
        chat = (Button) findViewById(R.id.chat_button);
        weather = (Button) findViewById(R.id.weather_button);
        toolbar = (Button) findViewById(R.id.toolbar_button);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //go to the ListItemsActivity
                Intent intent = new Intent(StartActivity.this, ListItemsActivity.class);
                startActivityForResult(intent, 50);
            }
        });
        //start chat
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(ACTIVITY_NAME, "User clicked Start Chat");
                //go to the ChatWindowActivity
                Intent intent = new Intent(StartActivity.this, ChatWindow.class);
                startActivity(intent);
            }
        });
        //go to wether page
        weather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //go to the WeatherForecast
                Intent intent = new Intent(StartActivity.this, WeatherForecast.class);
                startActivity(intent);
            }
        });
        //go to toolbar
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //go to the WeatherForecast
                Intent intent = new Intent(StartActivity.this, TestToolbar.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == 50) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                String messagePassed = data.getStringExtra("Response");
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(StartActivity.this, messagePassed, duration); //this is the ListActivity
                toast.show(); //display your message box
                Log.i(ACTIVITY_NAME, "Returned to StartActivity.onActivityResult");
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(ACTIVITY_NAME, "In onResume");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(ACTIVITY_NAME, "In onStart");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(ACTIVITY_NAME, "In onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(ACTIVITY_NAME, "In onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(ACTIVITY_NAME, "In onDestroy");
    }
}
