package com.softcodeinfotech.helpapp;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.softcodeinfotech.helpapp.helpDataPOJO.Datum;
import com.softcodeinfotech.helpapp.helpDataPOJO.helpDataBean;
import com.softcodeinfotech.helpapp.sendMessagePOJO.sendMessageBean;
import com.softcodeinfotech.helpapp.ui.IndividualHelpActivity;
import com.softcodeinfotech.helpapp.util.Constant;
import com.softcodeinfotech.helpapp.util.SharePreferenceUtils;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class HelpDetails extends AppCompatActivity {

    TextView title , state , date , desc ,address , cat;

    ViewPager pager;

    String hid , uphone , wphone , dob , image;

    Button chat , call , whatsapp;

    CircleImageView pic;
    TextView uname;

    String uid;

    CardView profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_details);

        hid = getIntent().getStringExtra("hid");

        title = findViewById(R.id.textView19);
        state = findViewById(R.id.textView18);
        date = findViewById(R.id.textView23);
        desc = findViewById(R.id.textView24);
        address = findViewById(R.id.textView25);
        pager = findViewById(R.id.imageView3);
        cat = findViewById(R.id.textView46);
        pic = findViewById(R.id.profile);
        uname = findViewById(R.id.username);

        profile = findViewById(R.id.textView22);
        chat = findViewById(R.id.imageButton7);
        call = findViewById(R.id.imageButton2);
        whatsapp = findViewById(R.id.imageButton9);



        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try {

                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + uphone));
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
                intent.putExtra("name", uname.getText().toString());
                intent.putExtra("dob", dob);
                intent.putExtra("phone", uphone);
                intent.putExtra("image", image);
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


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ServiceInterface serviceInterface = retrofit.create(ServiceInterface.class);

        Call<helpDataBean> call = serviceInterface.helpData(SharePreferenceUtils.getInstance().getString("userId") , hid);

        call.enqueue(new Callback<helpDataBean>() {
            @Override
            public void onResponse(@NonNull Call<helpDataBean> call, @NonNull Response<helpDataBean> response) {

                assert response.body() != null;
                if (response.body().getStatus().equals("1"))
                {

                    Datum item = response.body().getData().get(0);

                    uname.setText(item.getUname());
                    title.setText(getString(R.string.how_for_need) + item.getHowTo());

                    String v = item.getFollowers() + R.string.views;

                    state.setText(v);
                    date.setText(item.getCreatedDate());
                    desc.setText(Html.fromHtml(getString(R.string.needd) + item.getNeed()));
                    address.setText(Html.fromHtml(getString(R.string.addrreess) + item.getAddress()));
                    cat.setText(getString(R.string.catt) + item.getCategory());

                    uphone = item.getUphone();
                    wphone = item.getWphone();
                    dob = item.getDob();

                    image = item.getUimage();


                    DisplayImageOptions options = new DisplayImageOptions.Builder().cacheOnDisk(true).cacheInMemory(true).resetViewBeforeLoading(false).build();
                    ImageLoader loader = ImageLoader.getInstance();
                    loader.displayImage(item.getUimage() , pic , options);


                    List<String> iimm = new ArrayList<>();

                    if (item.getFile1().length() > 0) {
                        iimm.add(item.getFile1());
                    } else if (item.getFile2().length() > 0) {
                        iimm.add(item.getFile2());
                    } else if (item.getFile3().length() > 0) {
                        iimm.add(item.getFile3());
                    } else if (item.getFile4().length() > 0) {
                        iimm.add(item.getFile4());
                    } else if (item.getFile5().length() > 0) {
                        iimm.add(item.getFile5());
                    } else if (item.getFile6().length() > 0) {
                        iimm.add(item.getFile6());
                    } else if (item.getFile7().length() > 0) {
                        iimm.add(item.getFile7());
                    } else if (item.getFile8().length() > 0) {
                        iimm.add(item.getFile8());
                    } else if (item.getFile9().length() > 0) {
                        iimm.add(item.getFile9());
                    } else if (item.getFile10().length() > 0) {
                        iimm.add(item.getFile10());
                    }
                    else
                    {
                        iimm.add("adad");
                    }


                    PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager() , iimm);

                    pager.setAdapter(adapter);



                }
                else {
                    Toast.makeText(HelpDetails.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<helpDataBean> call, Throwable t) {

            }
        });


    }



    private void openWhatsApp() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone=" + wphone));
        startActivity(browserIntent);
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


    class PagerAdapter extends FragmentStatePagerAdapter
    {
        List<String> images = new ArrayList<>();

        public PagerAdapter(FragmentManager fm , List<String> images) {
            super(fm);
            this.images = images;
        }

        @Override
        public Fragment getItem(int i) {
            page frag = new page();
            Bundle b = new Bundle();
            b.putString("url" ,images.get(i));
            frag.setArguments(b);
            return frag;
        }

        @Override
        public int getCount() {
            return images.size();
        }
    }

    public static class page extends Fragment
    {
        String url;
        ImageView image;

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.page_layout , container , false);

            url = getArguments().getString("url");

            image = view.findViewById(R.id.image);

            DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).resetViewBeforeLoading(false).showImageForEmptyUri(R.drawable.noimage).build();

            ImageLoader loader = ImageLoader.getInstance();
            loader.displayImage(url , image , options);

            return view;
        }
    }


}
