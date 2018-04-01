package com.main.homeworkcinimex;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AskActivity extends AppCompatActivity {

    private String group;
    private TextView homework_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask);

        group = getIntent().getStringExtra("group");
        setTitle(getResources().getString(R.string.app_label) + " " + group);

        Button ask_button = findViewById(R.id.ask_button);
        homework_date = findViewById(R.id.homework_date);

        ask_button.setOnClickListener(onClickListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String day = (new SimpleDateFormat("EEEE", Locale.getDefault())).format(new Date());
        int hour = Integer.parseInt((new SimpleDateFormat("HH", Locale.getDefault())).format(new Date()));
        if (group.equals(getResources().getString(R.string.A2))) {
            if (day.equals(getResources().getString(R.string.day_1st))) {
                if (hour < 18) {
                    homework_date.setText(R.string.for_the_1th);
                } else
                    homework_date.setText(R.string.for_the_3rd);
            } else if (day.equals(getResources().getString(R.string.day_2nd))) {
                homework_date.setText(R.string.for_the_3rd);
            } else if (day.equals(getResources().getString(R.string.day_3rd))) {
                if (hour < 18) {
                    homework_date.setText(R.string.for_the_3rd);
                } else
                    homework_date.setText(R.string.for_the_1th);
            } else {
                homework_date.setText(R.string.for_the_1th);
            }
        } else if (group.equals(getResources().getString(R.string.B1))) {
            if (day.equals(getResources().getString(R.string.day_2nd))) {
                if (hour < 9) {
                    homework_date.setText(R.string.for_the_2nd);
                } else
                    homework_date.setText(R.string.for_the_4th);
            } else if (day.equals(getResources().getString(R.string.day_3rd))) {
                homework_date.setText(R.string.for_the_4th);
            } else if (day.equals(getResources().getString(R.string.day_4th))) {
                if (hour < 9) {
                    homework_date.setText(R.string.for_the_4th);
                } else
                    homework_date.setText(R.string.for_the_2nd);
            } else {
                homework_date.setText(R.string.for_the_2nd);
            }
        }
    }

    private final View.OnClickListener onClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ask_button:
                    Intent intent = new Intent(AskActivity.this, EditActivity.class);
                    intent.putExtra("group", group);
                    startActivity(intent);
                    break;
            }
        }
    };
}