package com.softcodeinfotech.helpapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Success extends AppCompatActivity {

    Button ok;
    TextView message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);

        ok = findViewById(R.id.button6);
        message = findViewById(R.id.textView51);

        String ii = getIntent().getStringExtra("ii");

        if (ii.equals("1"))
        {
            message.setText(getString(R.string.your_help_has_been_posted_successfully));
        }
        else
        {
            message.setText(getString(R.string.your_help_has_been_updated_successfully));
        }

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });

    }
}
