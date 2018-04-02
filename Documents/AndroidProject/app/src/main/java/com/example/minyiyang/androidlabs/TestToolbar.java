package com.example.minyiyang.androidlabs;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

public class TestToolbar extends AppCompatActivity {
    String content = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_toolbar);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu m) {
        getMenuInflater().inflate(R.menu.toolbar_menu, m);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.cherry:
                Log.d("Toolbar", "cherry is selected");
                View toolbarView = (View) findViewById(R.id.toolbarView);
                if (content == null)
                    Snackbar.make(toolbarView, "You selected item 1", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                else
                    Snackbar.make(toolbarView, content, Snackbar.LENGTH_LONG).setAction("Action", null).show();

                break;
            // action with ID action_settings was selected
            case R.id.citrus:
                Log.d("Toolbar", "citrus is selected");
                AlertDialog.Builder builder = new AlertDialog.Builder(TestToolbar.this);
                builder.setTitle(R.string.dialogTitle);
                // Add the buttons
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        TestToolbar.this.finish();
                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
                // Create the AlertDialog
                AlertDialog dialog = builder.create();
                dialog.show();
                break;
            // action with ID action_settings was selected
            case R.id.strawberry:
                Log.d("Toolbar", "strawberry is selected");
                LayoutInflater layoutInflater = LayoutInflater.from(this);
                final View conView = layoutInflater.inflate(R.layout.dialog_signin, null);

                new AlertDialog.Builder(this)
                        .setCancelable(false)
                        .setView(conView)
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                EditText message = (EditText) conView.findViewById(R.id.new_message);
                                content = message.getText().toString();
                            }
                        })
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // LoginDialogFragment.this.getDialog().cancel();
                            }
                        }).show();
                break;
            case R.id.about:
                Toast.makeText(this, "Version 1.0, by Minyi Yang", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }

        return true;
    }

}
