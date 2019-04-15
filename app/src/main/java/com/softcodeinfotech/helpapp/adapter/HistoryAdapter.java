package com.softcodeinfotech.helpapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.softcodeinfotech.helpapp.EditHelp;
import com.softcodeinfotech.helpapp.R;
import com.softcodeinfotech.helpapp.model.HelpModel;
import com.softcodeinfotech.helpapp.myHelpsPOJO.Datum;

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

        TextView mHelpTitle, mHelpTimeStamp;

        public HistoryAdapterHolder(@NonNull View itemView) {
            super(itemView);

            mHelpTitle = itemView.findViewById(R.id.helpTitle);
            mHelpTimeStamp = itemView.findViewById(R.id.helpTime);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.my_history, viewGroup, false);
        return new HistoryAdapterHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        final Datum  item =mHelpHistoryModel.get(i);

        ((HistoryAdapterHolder) viewHolder).mHelpTitle.setText(item.getHowTo());
        ((HistoryAdapterHolder) viewHolder).mHelpTimeStamp.setText(item.getCreatedDate());

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
