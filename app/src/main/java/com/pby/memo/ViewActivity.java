package com.pby.memo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ViewActivity extends AppCompatActivity implements View.OnClickListener {

    Button alter, delete, back;
    TextView alert, title, content;
    DBHelper dbhelper;
    SQLiteDatabase db;
    Intent intent;
    String string_date, string_title, string_content;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        //Button
        alter = findViewById(R.id.alter);
        delete = findViewById(R.id.delete);
        back = findViewById(R.id.back);

        //TextView
        alert = findViewById(R.id.alert);
        title = findViewById(R.id.title);
        content = findViewById(R.id.content);

        //Listener
        alter.setOnClickListener(this);
        delete.setOnClickListener(this);
        back.setOnClickListener(this);

        //Database Helper
        dbhelper = new DBHelper(ViewActivity.this, "memo", null, 1);

        //SQLite
        db = dbhelper.getWritableDatabase();
        db = dbhelper.getReadableDatabase();

        //Intent
        intent = getIntent();
        string_date = intent.getStringExtra("date");
        string_title = intent.getStringExtra("title");
        string_content = intent.getStringExtra("content");

        alert.setText("时间: " + string_date);
        title.setText("标题: " + string_title);
        content.setText("内容:\n" + string_content);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.alter:
                Intent intent = new Intent(ViewActivity.this, AppendActivity.class);
                intent.putExtra("date", string_date);
                intent.putExtra("title", string_title);
                intent.putExtra("content", string_content);
                startActivity(intent);
                this.finish();
                break;
            case R.id.delete:
                AlertDialog.Builder adb = new AlertDialog.Builder(ViewActivity.this);
                adb.setTitle("删除");
                adb.setMessage("是否删除");
                adb.setPositiveButton("删除",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                db.delete("record", "date = ? and title = ? and content= ? ", new String[]{string_date, string_title, string_content});
                                ViewActivity.this.finish();
                            }
                        });
                adb.setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        });
                adb.show();
                break;
            case R.id.back:
                this.finish();
                break;
        }
    }
}
