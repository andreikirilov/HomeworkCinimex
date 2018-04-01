package com.main.homeworkcinimex;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class EditActivity extends AppCompatActivity {

    private String group;
    private String date;
    private String task;
    private Button edit_button;
    private Button refresh_button;
    private TextView homework_date;
    private TextView homework_text;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        group = getIntent().getStringExtra("group");
        setTitle(getResources().getString(R.string.app_label) + " " + group);

        edit_button = findViewById(R.id.edit_button);
        refresh_button = findViewById(R.id.refresh_button);
        homework_date = findViewById(R.id.homework_date);
        homework_text = findViewById(R.id.homework_text);
        homework_text.setMovementMethod(new ScrollingMovementMethod());

        edit_button.setOnClickListener(onClickListener);
        refresh_button.setOnClickListener(onClickListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (NetworkManager.isNetworkAvailable(EditActivity.this)) {
            new getHomework().execute();
        } else {
            edit_button.setVisibility(View.INVISIBLE);
            refresh_button.setVisibility(View.VISIBLE);
            homework_date.setText(R.string.error);
            homework_text.setText(R.string.connection_error);
            Toast.makeText(getApplicationContext(), R.string.connection_error, Toast.LENGTH_LONG).show();
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class getHomework extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(EditActivity.this);
            progressDialog.setMessage(getApplicationContext().getResources().getString(R.string.loading));
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler httpHandler = new HttpHandler();
            String url = "http://" + getResources().getString(R.string.host) + "/gethw?grp=" + group;
            String jsonString = httpHandler.makeServiceCall(url);
            if (jsonString != null) {
                try {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    date = jsonObject.getString("date");
                    task = jsonObject.getString("task");
                } catch (JSONException ignored) {
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (progressDialog.isShowing()) {
                try {
                    Thread.sleep(1500);
                    progressDialog.dismiss();
                } catch (InterruptedException ignored) {
                }
            }
            if (date != null) {
                edit_button.setVisibility(View.VISIBLE);
                refresh_button.setVisibility(View.INVISIBLE);
                String date_act = getResources().getString(R.string.date_act) + " " + date;
                homework_date.setText(date_act);
                homework_text.setText(task);
                Toast.makeText(getApplicationContext(), R.string.json_get_success, Toast.LENGTH_LONG).show();
            } else {
                edit_button.setVisibility(View.INVISIBLE);
                refresh_button.setVisibility(View.VISIBLE);
                homework_date.setText(R.string.error);
                homework_text.setText(R.string.json_get_error);
                Toast.makeText(getApplicationContext(), R.string.json_get_error, Toast.LENGTH_LONG).show();
            }
        }
    }

    private final View.OnClickListener onClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.edit_button:
                    Intent intent = new Intent(EditActivity.this, SaveActivity.class);
                    intent.putExtra("group", group);
                    intent.putExtra("date", homework_date.getText().toString());
                    intent.putExtra("text", homework_text.getText().toString());
                    startActivity(intent);
                    break;
                case R.id.refresh_button:
                    if (NetworkManager.isNetworkAvailable(EditActivity.this)) {
                        new getHomework().execute();
                    } else {
                        edit_button.setVisibility(View.INVISIBLE);
                        refresh_button.setVisibility(View.VISIBLE);
                        homework_date.setText(R.string.error);
                        homework_text.setText(R.string.connection_error);
                        Toast.makeText(getApplicationContext(), R.string.connection_error, Toast.LENGTH_LONG).show();
                    }
                    break;
            }
        }
    };
}