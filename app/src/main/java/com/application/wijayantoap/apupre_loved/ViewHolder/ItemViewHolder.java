package com.application.wijayantoap.apupre_loved.ViewHolder;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.application.wijayantoap.apupre_loved.Interface.ItemClickListener;
import com.application.wijayantoap.apupre_loved.R;

public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

public TextView itemTitle, itemPrice, itemUsername, itemDescription, itemPhone, itemDate, itemQuality;
    public ImageView itemImage;

    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public ItemViewHolder(View itemView) {
        super(itemView);

        itemTitle = (TextView) itemView.findViewById(R.id.textTitle);
        itemPrice = (TextView) itemView.findViewById(R.id.textPrice);
        itemUsername = (TextView) itemView.findViewById(R.id.textUsername);
        itemDescription = (TextView) itemView.findViewById(R.id.textDescription);
        itemPhone = (TextView) itemView.findViewById(R.id.textPhone);
        itemDate = (TextView) itemView.findViewById(R.id.textDate);
        itemImage = (ImageView) itemView.findViewById(R.id.imgItem);
        itemQuality = (TextView) itemView.findViewById(R.id.textQuality);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(),false);
    }
}
