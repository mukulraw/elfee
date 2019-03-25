package com.softcodeinfotech.helpapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.softcodeinfotech.helpapp.HelpDetails;
import com.softcodeinfotech.helpapp.myHelpsPOJO.Datum;
import com.softcodeinfotech.helpapp.response.GethelplistResponse;
import com.softcodeinfotech.helpapp.ui.IndividualHelpActivity;
import com.softcodeinfotech.helpapp.R;
import com.softcodeinfotech.helpapp.model.GetHelpListModel;

import java.util.ArrayList;
import java.util.List;

public class GetHelpListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<Datum> mHelpListModel;
    private String TAG = "HelpListAdapter";
    private int mScrenwidth;

    public GetHelpListAdapter(Context mContext, List<Datum> mHelpListModel, int mScrenwidth) {

        this.mContext = mContext;
        this.mHelpListModel = mHelpListModel;
        this.mScrenwidth = mScrenwidth;
    }


    public class GetHelpListAdapterHolder extends RecyclerView.ViewHolder {
        TextView title, state, date, desc;
        ImageView image;


        public GetHelpListAdapterHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.textView19);
            state = itemView.findViewById(R.id.textView18);
            date = itemView.findViewById(R.id.textView23);
            desc = itemView.findViewById(R.id.textView24);
            image = itemView.findViewById(R.id.imageView3);

        }


    }


    public void setData(List<Datum> list) {
        this.mHelpListModel = list;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.help_list_model, viewGroup, false);
        return new GetHelpListAdapterHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        viewHolder.setIsRecyclable(false);
        final Datum item = mHelpListModel.get(i);


        ((GetHelpListAdapterHolder) viewHolder).title.setText(item.getHowTo());
        ((GetHelpListAdapterHolder) viewHolder).desc.setText(item.getCity());
        ((GetHelpListAdapterHolder) viewHolder).date.setText(item.getCreatedDate());
        ((GetHelpListAdapterHolder) viewHolder).state.setText(item.getCategory());


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
        } else {
            iimm.add("asd");
        }


        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).resetViewBeforeLoading(false).showImageForEmptyUri(R.drawable.noimage).build();

        ImageLoader loader = ImageLoader.getInstance();


        loader.displayImage(iimm.get(0), ((GetHelpListAdapterHolder) viewHolder).image, options);


        ((GetHelpListAdapterHolder) viewHolder).itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*String user_id = String.valueOf(getHelpListModel.getUserId());
                Intent intent = new Intent(mContext,IndividualHelpActivity.class);
                intent.putExtra("user_id", user_id);
                 mContext.startActivity(intent);
*/

                Intent intent = new Intent(mContext, HelpDetails.class);
                intent.putExtra("hid", item.getHelpId());
                mContext.startActivity(intent);


                // Toast.makeText(mContext, "clicked", Toast.LENGTH_SHORT).show();
                //  Toast.makeText(mContext, ""+user_id, Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    public int getItemCount() {
        return mHelpListModel.size();
    }
}
