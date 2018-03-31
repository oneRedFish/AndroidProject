package com.example.minyiyang.androidlabs;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ChatWindow extends Activity {
    protected static final String ACTIVITY_NAME = "ChatWindow";
    ListView chatView;
    EditText send_text;
    Button button_send;
    ArrayList<String> message = new ArrayList<>();
    ListAdapter chatAdapter;
    ChatDatabaseHelper mydb;
    FrameLayout fLayout;
    boolean checkFram = true;
    Cursor cursor;
    private boolean isTablet;
    Bundle infoToPass;
    private String content;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);
        //initialize the variables
        chatView = (ListView) findViewById(R.id.chatView);
        send_text = (EditText) findViewById(R.id.send_text);
        button_send = (Button) findViewById(R.id.button_send);
        fLayout = (FrameLayout) findViewById(R.id.tableFrame);
        isTablet = (fLayout != null);
        infoToPass = new Bundle();

        //initialize ChatDatabaseHelper
        mydb = new ChatDatabaseHelper(this);
        db = mydb.getWritableDatabase();
        //update viewlist
        showData();
        //check fram
        checkFram();
        Log.i("FrameLayout", "avaliable: " + checkFram);
    }

    public void checkFram() {
        if (fLayout == null)
            checkFram = false;
    }

    public void showData() {
        cursor = db.query(false, mydb.TABLE_NAME, new String[]{mydb.KEY_ID, mydb.KEY_MESSAGE}, null, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            content = cursor.getString(cursor.getColumnIndex(mydb.KEY_MESSAGE));
            Log.i(ACTIVITY_NAME, "SQL MESSAGE:" + content);
            message.add(cursor.getString(1));
            cursor.moveToNext();
        }
        Log.i(ACTIVITY_NAME, "Cursor’s column count =" + cursor.getColumnCount());

        int columnIndex = cursor.getColumnIndex("KEY_MESSAGE");

        for (int i = 0; i < cursor.getCount(); i++) {
            Log.i(ACTIVITY_NAME, "Cursor’s column name =" + cursor.getColumnName(columnIndex));
            cursor.moveToNext();
        }

        //click list items
        chatView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String note = String.valueOf(parent.getItemAtPosition(position));
                Toast.makeText(ChatWindow.this, note, Toast.LENGTH_LONG).show();
                cursor.moveToPosition(position);
                id = cursor.getLong(cursor.getColumnIndex(ChatDatabaseHelper.KEY_ID));
                Log.i(ACTIVITY_NAME, "Cursor’s column id =" + id);
                //put data into bundle
                infoToPass.putString("dataMessage", note);
                infoToPass.putLong("id", id);
                infoToPass.putBoolean("isTablet", isTablet);
                //if on tablet:
                if (isTablet) {
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    MessageFragment df = new MessageFragment();
                    df.setArguments(infoToPass);
                    ft.addToBackStack(null); //only undo FT on back button
                    ft.replace(R.id.tableFrame, df);
                    ft.commit();
                } else //this is a phone:
                {
                    Intent next = new Intent(ChatWindow.this, MessageDetails.class);
                    next.putExtras(infoToPass);
                    startActivityForResult(next, 3);
                }
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
            public long getItemId(int position) {
                //to that position to get data
                return position;
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
                //insert data
                ContentValues cv = new ContentValues();
                cv.put(mydb.KEY_MESSAGE, send_text.getText().toString());
                db.insert(mydb.TABLE_NAME, null, cv);
                //refresh database
                cursor = db.query(false, mydb.TABLE_NAME, new String[]{mydb.KEY_ID, mydb.KEY_MESSAGE}, null, null, null, null, null, null);
                //reset data
                send_text.setText("");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 3) {
            Bundle basket = data.getExtras();
            Long id = basket.getLong("id");
            db.delete(mydb.TABLE_NAME, mydb.KEY_ID + "=" + id, null);
            Log.i("ChatWindow", id + " is deleted");
            message.clear();
            showData();
        }
    }

    @Override
    public void onDestroy(){
        mydb.close();
        super.onDestroy();
    }
}
