package com.softcodeinfotech.helpapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.softcodeinfotech.helpapp.ui.SignupLoginActivity;

public class Needy extends AppCompatActivity {

    Button next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_needy);

        next = findViewById(R.id.button16);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Needy.this , SignupLoginActivity.class);
                startActivity(intent);

            }
        });


    }
}
