package com.example.minyiyang.androidlabs;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class MessageFragment extends Fragment {
    TextView frame_message;
    TextView frame_id;
    Button button_delete;
    Bundle getInfo;
    Boolean isTablet;
    Long id;
    private SQLiteDatabase writableDB;
    protected ChatWindow chatWindow;
    Bundle bundle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getInfo = getArguments();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View gui = inflater.inflate(R.layout.activity_message_fragment, null);
        ChatDatabaseHelper chatDatabaseHelper = new ChatDatabaseHelper(getActivity());
        writableDB = chatDatabaseHelper.getWritableDatabase();

        frame_message = (TextView) gui.findViewById(R.id.frame_message);
        frame_id = (TextView) gui.findViewById(R.id.frame_id);
        button_delete = (Button) gui.findViewById(R.id.button_delete);

        isTablet = getInfo.getBoolean("isTablet");
        id = getInfo.getLong("id");
        frame_message.setText("The Message is : " + getInfo.getString("dataMessage"));
        frame_id.setText("The ID is : " + id);

        button_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isTablet) {
                    writableDB.delete(ChatDatabaseHelper.TABLE_NAME, ChatDatabaseHelper.KEY_ID + "=" + id, null);
                    getActivity().finish();
                    Intent intent = getActivity().getIntent();
                    startActivity(intent);
                } else {
                    Intent intent = new Intent();
                    intent.putExtra("id", id);
                    getActivity().setResult(Activity.RESULT_OK, intent);
                    getActivity().finish();
                }
            }
        });
        return gui;
    }
}

