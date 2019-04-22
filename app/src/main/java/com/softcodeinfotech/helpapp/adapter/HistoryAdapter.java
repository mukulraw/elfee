package com.softcodeinfotech.helpapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.softcodeinfotech.helpapp.EditHelp;
import com.softcodeinfotech.helpapp.R;
import com.softcodeinfotech.helpapp.model.HelpModel;
import com.softcodeinfotech.helpapp.myHelpsPOJO.Datum;

import java.util.ArrayList;
import java.util.List;


public class HistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

//
    private Context mContext;
    private List<Datum> mHelpHistoryModel;
    private String TAG = "HistoryAdapter";
    private int mScrenwidth;

    public HistoryAdapter(Context mContext, List<Datum> mHelpHistoryModel, int mScrenwidth) {
        this.mContext = mContext;
        this.mHelpHistoryModel = mHelpHistoryModel;
        this.mScrenwidth = mScrenwidth;
    }

    public void setData(List<Datum> list)
    {
        this.mHelpHistoryModel = list;
        notifyDataSetChanged();
    }

    public class HistoryAdapterHolder extends RecyclerView.ViewHolder {

        TextView title, state, date, desc;
        ImageView image;


        public HistoryAdapterHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.textView19);
            state = itemView.findViewById(R.id.textView18);
            date = itemView.findViewById(R.id.textView23);
            desc = itemView.findViewById(R.id.textView24);
            image = itemView.findViewById(R.id.imageView3);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.help_list_model, viewGroup, false);
        return new HistoryAdapterHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        final Datum  item =mHelpHistoryModel.get(i);
        ((HistoryAdapterHolder) viewHolder).title.setText(item.getHowTo());
        ((HistoryAdapterHolder) viewHolder).desc.setText(item.getCity());
        ((HistoryAdapterHolder) viewHolder).date.setText(item.getCreatedDate());
        ((HistoryAdapterHolder) viewHolder).state.setText(item.getCategory());


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


        loader.displayImage(iimm.get(0), ((HistoryAdapterHolder) viewHolder).image, options);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(mContext , EditHelp.class);
                intent.putExtra("catId" , item.getCatId());
                intent.putExtra("helpId" , item.getHelpId());
                intent.putExtra("cat" , item.getCategory());
                intent.putExtra("i1" , item.getFile1());
                intent.putExtra("i2" , item.getFile2());
                intent.putExtra("i3" , item.getFile3());
                intent.putExtra("i4" , item.getFile4());
                intent.putExtra("i5" , item.getFile5());
                intent.putExtra("i6" , item.getFile6());
                intent.putExtra("i7" , item.getFile7());
                intent.putExtra("i8" , item.getFile8());
                intent.putExtra("i9" , item.getFile9());
                intent.putExtra("i10" , item.getFile10());
                intent.putExtra("tt" , item.getHowTo());
                intent.putExtra("nn" , item.getNeed());
                intent.putExtra("status" , item.getStatus());
                mContext.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return mHelpHistoryModel.size();
    }
}
