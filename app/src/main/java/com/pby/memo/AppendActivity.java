package com.pby.memo;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.TimeZone;

public class AppendActivity extends AppCompatActivity implements View.OnClickListener {

    Button save, time, cancel;
    TextView alert;
    EditText title, content;
    Calendar now;
    String year, month, day, hour, minute;
    DatePickerDialog dpd;
    TimePickerDialog tpd;
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    SetDate();
                    break;
            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_append);

        //Button
        save = findViewById(R.id.save);
        time = findViewById(R.id.time);
        cancel = findViewById(R.id.cancel);
        //TextView
        alert = findViewById(R.id.alert);
        //EditText
        title = findViewById(R.id.title);
        content = findViewById(R.id.content);

        save.setOnClickListener(this);
        time.setOnClickListener(this);
        cancel.setOnClickListener(this);

        new TimeThread().start();

        showInput(title);
        showInput(content);

        now = Calendar.getInstance();
        now.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        hour = String.valueOf(now.get(Calendar.HOUR_OF_DAY));
        minute = String.valueOf(now.get(Calendar.MINUTE));
        year = String.valueOf(now.get(Calendar.YEAR));
        month = String.valueOf(now.get(Calendar.MONTH) + 1);
        day = String.valueOf(now.get(Calendar.DATE));
        SetDate();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.save:
                //TODO
                break;
            case R.id.time:
                tpd = new TimePickerDialog(AppendActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker tp, int h, int m) {
                        hour = String.valueOf(h);
                        minute = String.valueOf(m);
                    }
                }, now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), true);
                tpd.show();
                dpd = new DatePickerDialog(AppendActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                        year = String.valueOf(y);
                        month = String.valueOf(m + 1);
                        day = String.valueOf(d);
                    }
                }, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DATE));
                dpd.show();
                break;
            case R.id.cancel:
                this.finish();
                break;
        }
    }

    private String check(final String str) {
        return str.length() == 1 ? "0" : "";
    }

    private void SetDate() {
        String date = year + '-' +
                check(month) + month + '-' +
                check(day) + day + ' ' +
                check(hour) + hour + ':' +
                check(minute) + minute;
        alert.setText(date);
    }

    private void showInput(final EditText et) {
        et.setFocusable(true);
        et.setFocusableInTouchMode(true);
        et.requestFocus();
        InputMethodManager imm = (InputMethodManager) et.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(et, 0);
    }

    public class TimeThread extends Thread {
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
