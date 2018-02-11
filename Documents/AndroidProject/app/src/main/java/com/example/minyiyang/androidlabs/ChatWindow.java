package com.example.minyiyang.androidlabs;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ChatWindow extends Activity {
    ListView chatView;
    EditText send_text;
    Button button_send;
    ArrayList<String> message;
    ListAdapter chatAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);

        //initialize the variables
        chatView = (ListView) findViewById(R.id.chatView);
        send_text = (EditText) findViewById(R.id.send_text);
        button_send = (Button) findViewById(R.id.button_send);
        message = new ArrayList<>();

        //click list items
        chatView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String note = String.valueOf(parent.getItemAtPosition(position));
                Toast.makeText(ChatWindow.this, note, Toast.LENGTH_LONG).show();
            }
        });

        //inner class
        class ChatAdapter extends ArrayAdapter<String> {

            public ChatAdapter(Context ctx) {
                super(ctx, 0);
            }

            @Override
            public int getCount() {
                return message.size();
            }

            @Override
            public String getItem(int position) {
                return message.get(position);
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                LayoutInflater inflater = ChatWindow.this.getLayoutInflater();
                View result = null;
                if (position % 2 == 0)
                    result = inflater.inflate(R.layout.chat_row_incoming, null);
                else
                    result = inflater.inflate(R.layout.chat_row_outgoing, null);

                TextView message = (TextView) result.findViewById(R.id.message_text);
                message.setText(getItem(position)); // get the string at position
                return result;
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

        }

        //in this case, “this” is the ChatWindow, which is-A Context object
        final ChatAdapter messageAdapter = new ChatAdapter(this);
        chatView.setAdapter(messageAdapter);

        //save value to arraylist
        button_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message.add(send_text.getText().toString());
                messageAdapter.notifyDataSetChanged(); //this restarts the process of getCount() & getView()
                send_text.setText("");
            }
        });

    }
}
