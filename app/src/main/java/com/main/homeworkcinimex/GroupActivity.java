package com.main.homeworkcinimex;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class GroupActivity extends AppCompatActivity {

    private TextView homework_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        Button a2_button = findViewById(R.id.A2_button);
        Button b1_button = findViewById(R.id.B1_button);
        homework_date = findViewById(R.id.homework_date);

        a2_button.setOnClickListener(onClickListener);
        b1_button.setOnClickListener(onClickListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String date_now = getResources().getString(R.string.date_now) + " " +
                (new SimpleDateFormat(getResources().getString(R.string.date_format), Locale.getDefault())).format(new Date());
        homework_date.setText(date_now);
    }

    private final View.OnClickListener onClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.A2_button:
                    Intent intentA2 = new Intent(GroupActivity.this, AskActivity.class);
                    intentA2.putExtra("group", getResources().getString(R.string.A2));
                    startActivity(intentA2);
                    break;
                case R.id.B1_button:
                    Intent intentB1 = new Intent(GroupActivity.this, AskActivity.class);
                    intentB1.putExtra("group", getResources().getString(R.string.B1));
                    startActivity(intentB1);
                    break;
            }
        }
    };
}