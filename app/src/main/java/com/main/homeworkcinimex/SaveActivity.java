package com.main.homeworkcinimex;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SaveActivity extends AppCompatActivity {

    private String response;
    private String group;
    private TextView homework_date;
    private TextView homework_edit;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save);

        group = getIntent().getStringExtra("group");
        setTitle(getResources().getString(R.string.app_label) + " " + group);

        Button save_button = findViewById(R.id.save_button);
        homework_date = findViewById(R.id.homework_date);
        homework_edit = findViewById(R.id.homework_edit);
        homework_date.setText(getIntent().getStringExtra("date"));
        homework_edit.setText(getIntent().getStringExtra("text"));

        save_button.setOnClickListener(onClickListener);
    }

    @SuppressLint("StaticFieldLeak")
    private class setHomework extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(SaveActivity.this);
            progressDialog.setMessage(getApplicationContext().getResources().getString(R.string.sending));
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler httpHandler = new HttpHandler();
            try {
                String url = "http://" + getResources().getString(R.string.host) + "/sethw?grp=" + group + "&txt=" + URLEncoder.encode(homework_edit.getText().toString(), "UTF-8");
                response = httpHandler.makeServiceCall(url);
            } catch (UnsupportedEncodingException ignored) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (progressDialog.isShowing()) {
                try {
                    Thread.sleep(1000);
                    progressDialog.dismiss();
                } catch (InterruptedException ignored) {
                }
            }
            if (response != null) {
                String date_new = getResources().getString(R.string.date_new) + " "
                        + (new SimpleDateFormat(getResources().getString(R.string.date_format), Locale.getDefault())).format(new Date());
                homework_date.setText(date_new);
                Toast.makeText(getApplicationContext(), R.string.json_set_success, Toast.LENGTH_LONG).show();
            } else {
                homework_date.setText(getResources().getString(R.string.error));
                Toast.makeText(getApplicationContext(), R.string.json_set_error, Toast.LENGTH_LONG).show();
            }
        }
    }

    private final View.OnClickListener onClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.save_button:
                    if (NetworkManager.isNetworkAvailable(SaveActivity.this)) {
                        new setHomework().execute();
                    } else {
                        homework_date.setText(getResources().getString(R.string.error));
                        Toast.makeText(getApplicationContext(), R.string.connection_error, Toast.LENGTH_LONG).show();
                    }
                    break;
            }
        }
    };
}