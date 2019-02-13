package com.softcodeinfotech.helpapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.softcodeinfotech.helpapp.ui.IndividualHelpActivity;

public class HelpDetails extends AppCompatActivity {

    TextView title , state , date , desc ,address;
    ImageView image;


    String tit , des , tim , sta , add , lt , ln , ima , uid;

    ImageButton profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_details);

        tit = getIntent().getStringExtra("title");
        des = getIntent().getStringExtra("desc");
        tim = getIntent().getStringExtra("time");
        sta = getIntent().getStringExtra("state");
        add = getIntent().getStringExtra("address");
        lt = getIntent().getStringExtra("lat");
        ln = getIntent().getStringExtra("lng");
        ima = getIntent().getStringExtra("image");
        uid = getIntent().getStringExtra("uid");

        title = findViewById(R.id.textView19);
        state = findViewById(R.id.textView18);
        date = findViewById(R.id.textView23);
        desc = findViewById(R.id.textView24);
        address = findViewById(R.id.textView25);
        image = findViewById(R.id.imageView3);

        profile = findViewById(R.id.imageButton8);

        title.setText(tit);
        state.setText(sta);
        date.setText(tim);
        desc.setText("Details\n" + des);
        address.setText("Address:  " + add);

        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheOnDisk(true).cacheInMemory(true).resetViewBeforeLoading(false).build();
        ImageLoader loader = ImageLoader.getInstance();
        loader.displayImage(ima , image , options);

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String user_id = String.valueOf(uid);
                Intent intent = new Intent(HelpDetails.this, IndividualHelpActivity.class);
                intent.putExtra("user_id", user_id);
                startActivity(intent);

            }
        });

    }
}
