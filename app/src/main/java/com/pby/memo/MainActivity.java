package com.pby.memo;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    Button append;
    ListView list;
    DBHelper dbhelper;
    SQLiteDatabase db;
    List<ContentValues> dataList;

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    dataList = new ArrayList<>();
                    Cursor cursor = db.query("record", new String[]{"title", "date", "content"}, null, null, null, null, "date");
                    while (cursor.moveToNext()) {
                        String title = cursor.getString(cursor.getColumnIndex("title"));
                        String date = cursor.getString(cursor.getColumnIndex("date"));
                        String content = cursor.getString(cursor.getColumnIndex("content"));
                        ContentValues cv = new ContentValues();
                        cv.put("title", title);
                        cv.put("date", date);
                        cv.put("content", content);
                        dataList.add(cv);
                    }
                    cursor.close();
                    RecordAdapter adapter = new RecordAdapter(MainActivity.this, R.layout.record, dataList);
                    list.setAdapter(adapter);
                    break;
            }
            return false;
        }
    });

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.append:
                Intent intent = new Intent(MainActivity.this, AppendActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Button
        append = findViewById(R.id.append);

        //ListView
        list = findViewById(R.id.list);

        //Listener
        append.setOnClickListener(this);
        list.setOnItemClickListener(this);

        //Database Helper
        dbhelper = new DBHelper(MainActivity.this, "memo", null, 1);

        //SQLite
        db = dbhelper.getWritableDatabase();
        db = dbhelper.getReadableDatabase();

        //Thread
        new TimeThread().start();
    }

    public class RecordAdapter extends ArrayAdapter<ContentValues> {
        private int recordID;

        private RecordAdapter(@NonNull Context context, int resource, @NonNull List<ContentValues> objects) {
            super(context, resource, objects);
            recordID = resource;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ContentValues cv = getItem(position);
            View view;
            ViewHolder vh;
            if (convertView == null) {
                view = LayoutInflater.from(getContext()).inflate(recordID, parent, false);
                vh = new ViewHolder();
                vh.title = view.findViewById(R.id.title);
                vh.date = view.findViewById(R.id.date);
                view.setTag(vh);
            } else {
                view = convertView;
                vh = (ViewHolder) view.getTag();
            }
            vh.title.setText(cv.getAsString("title"));
            vh.date.setText(cv.getAsString("date"));
            return view;
        }

        class ViewHolder {
            TextView title, date;
        }
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ContentValues cv = dataList.get(position);
        Intent intent = new Intent(MainActivity.this, ViewActivity.class);
        intent.putExtra("date", cv.getAsString("date"));
        intent.putExtra("title", cv.getAsString("title"));
        intent.putExtra("content", cv.getAsString("content"));
        intent.putExtra("position", position);
        startActivity(intent);
    }

    private class TimeThread extends Thread {
        @Override
        public void run() {
            super.run();
            do {
                try {
                    Thread.sleep(1000);
                    Message msg = new Message();
                    msg.what = 1;
                    handler.sendMessage(msg);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (true);
        }
    }
}
