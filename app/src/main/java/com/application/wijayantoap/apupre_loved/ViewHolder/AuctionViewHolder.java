package com.application.wijayantoap.apupre_loved.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.application.wijayantoap.apupre_loved.Interface.ItemClickListener;
import com.application.wijayantoap.apupre_loved.R;

public class AuctionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView txtUsername, txtLastMessage, txtTime;
    public RelativeLayout rootLayout;

    private ItemClickListener itemClickListener;

    public AuctionViewHolder(View itemView) {
        super(itemView);

        txtUsername = itemView.findViewById(R.id.textUsername);
        txtLastMessage = itemView.findViewById(R.id.textLastMessage);
        txtTime = itemView.findViewById(R.id.textTime);
        rootLayout = itemView.findViewById(R.id.rootLayout);
    }


    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v,getAdapterPosition(),false);
    }
}
