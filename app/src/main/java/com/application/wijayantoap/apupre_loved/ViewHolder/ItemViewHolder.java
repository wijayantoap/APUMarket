package com.application.wijayantoap.apupre_loved.ViewHolder;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.application.wijayantoap.apupre_loved.Interface.ItemClickListener;
import com.application.wijayantoap.apupre_loved.R;

public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView itemTitle, itemPrice, itemUsername, itemDescription, itemPhone, itemDate, itemQuality;
    public ImageView itemImage;
    public LinearLayout layoutFlag, layoutMessage;

    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public ItemViewHolder(View itemView) {
        super(itemView);

        itemTitle = itemView.findViewById(R.id.textTitle);
        itemPrice = itemView.findViewById(R.id.textPrice);
        itemUsername = itemView.findViewById(R.id.textUsername);
        itemDescription = itemView.findViewById(R.id.textDescription);
        itemPhone = itemView.findViewById(R.id.textPhone);
        itemDate = itemView.findViewById(R.id.textDate);
        itemImage = itemView.findViewById(R.id.imgItem);
        itemQuality = itemView.findViewById(R.id.textQuality);
        layoutFlag = itemView.findViewById(R.id.layoutFlag);
        layoutMessage = itemView.findViewById(R.id.layoutMessage);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(), false);
    }
}
