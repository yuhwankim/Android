package com.kwanwoo.android.activityintent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

public class ThirdActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        Intent intent = getIntent();
        String msg = intent.getStringExtra("UserDefinedExtra");
        EditText et = (EditText)findViewById(R.id.editText);
        et.setText(msg);
    }

    @Override
    public void onBackPressed() {
        EditText et = (EditText)findViewById(R.id.editText);
        Intent resultIntent = new Intent();
        resultIntent.putExtra("ResultString", et.getText().toString());
        setResult(RESULT_OK, resultIntent);

        super.onBackPressed();
    }
}
