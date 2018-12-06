package com.pby.memo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button append;
    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        append = findViewById(R.id.append);

        append.setOnClickListener(this);
    }

    public void onClick(View view) {
        if (view.getId() == R.id.append) {
            Intent intent = new Intent(MainActivity.this, AppendActivity.class);
            startActivity(intent);
        }
    }
}
