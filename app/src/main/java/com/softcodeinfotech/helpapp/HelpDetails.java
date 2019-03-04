package com.softcodeinfotech.helpapp;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.softcodeinfotech.helpapp.sendMessagePOJO.sendMessageBean;
import com.softcodeinfotech.helpapp.ui.IndividualHelpActivity;
import com.softcodeinfotech.helpapp.util.Constant;
import com.softcodeinfotech.helpapp.util.SharePreferenceUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class HelpDetails extends AppCompatActivity {

    TextView title , state , date , desc ,address;

    ImageView image;

    String tit , des , tim , sta , add , lt , ln , ima , uid , ph;

    Button profile , chat , call , whatsapp;

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
        ph = getIntent().getStringExtra("phone");

        title = findViewById(R.id.textView19);
        state = findViewById(R.id.textView18);
        date = findViewById(R.id.textView23);
        desc = findViewById(R.id.textView24);
        address = findViewById(R.id.textView25);
        image = findViewById(R.id.imageView3);

        profile = findViewById(R.id.imageButton8);
        chat = findViewById(R.id.imageButton7);
        call = findViewById(R.id.imageButton2);
        whatsapp = findViewById(R.id.imageButton9);

        title.setText(tit);
        state.setText(sta);
        date.setText(tim);
        desc.setText(Html.fromHtml("Details: </br>" + des));
        address.setText(Html.fromHtml("City:  " + add));



        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheOnDisk(true).cacheInMemory(true).resetViewBeforeLoading(false).build();
        ImageLoader loader = ImageLoader.getInstance();
        loader.displayImage(ima , image , options);


        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try {

                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + ph));
                    startActivity(intent);



                } catch (Exception e) {

                    e.printStackTrace();
                }

            }
        });

        whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                openWhatsApp();
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String user_id = String.valueOf(uid);
                Intent intent = new Intent(HelpDetails.this, IndividualHelpActivity.class);
                intent.putExtra("user_id", user_id);
                startActivity(intent);

            }
        });

        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(HelpDetails.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.send_message_dailog);
                dialog.setCancelable(true);
                dialog.show();

                final EditText mess = dialog.findViewById(R.id.editText6);
                Button send = dialog.findViewById(R.id.button12);

                send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String m = mess.getText().toString();

                        if (m.length() > 0)
                        {

                            Retrofit retrofit = new Retrofit.Builder()
                                    .baseUrl(Constant.BASE_URL)
                                    .addConverterFactory(ScalarsConverterFactory.create())
                                    .addConverterFactory(GsonConverterFactory.create())
                                    .build();

                            ServiceInterface serviceInterface = retrofit.create(ServiceInterface.class);


                            Call<sendMessageBean> call = serviceInterface.sendMessage(SharePreferenceUtils.getInstance().getString(Constant.USER_id), uid , m);

                            call.enqueue(new Callback<sendMessageBean>() {
                                @Override
                                public void onResponse(@NonNull Call<sendMessageBean> call, @NonNull Response<sendMessageBean> response) {

                                    dialog.dismiss();

                                    Intent intent = new Intent(HelpDetails.this , MessageActivity.class);
                                    startActivity(intent);

                                }

                                @Override
                                public void onFailure(@NonNull Call<sendMessageBean> call, @NonNull Throwable t) {

                                }
                            });

                        }

                    }
                });

            }
        });

    }



    private void openWhatsApp() {
        String smsNumber = "ph";
        boolean isWhatsappInstalled = whatsappInstalledOrNot("com.whatsapp");
        if (isWhatsappInstalled) {

            Intent sendIntent = new Intent("android.intent.action.MAIN");
            sendIntent.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.Conversation"));
            sendIntent.putExtra("jid", PhoneNumberUtils.stripSeparators(ph) + "@s.whatsapp.net");//phone number without "+" prefix

            startActivity(sendIntent);
        } else {
            Uri uri = Uri.parse("market://details?id=com.whatsapp");
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            Toast.makeText(this, "WhatsApp not Installed",
                    Toast.LENGTH_SHORT).show();
            startActivity(goToMarket);
        }
    }

    private boolean whatsappInstalledOrNot(String uri) {
        PackageManager pm = getPackageManager();
        boolean app_installed = false;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }

}
