package com.application.wijayantoap.apupre_loved.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.application.wijayantoap.apupre_loved.Common.Common;
import com.application.wijayantoap.apupre_loved.Interface.ItemClickListener;
import com.application.wijayantoap.apupre_loved.Model.Item;
import com.application.wijayantoap.apupre_loved.R;

public class AdminItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener, View.OnLongClickListener {
    public TextView itemTitle, itemPrice, itemUsername, itemDescription, itemPhone, itemDate, itemQuality, itemViewer;
    public ImageView itemImage;

    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public AdminItemViewHolder(View itemView) {
        super(itemView);

        itemTitle = itemView.findViewById(R.id.textTitle);
        itemPrice = itemView.findViewById(R.id.textPrice);
        itemUsername = itemView.findViewById(R.id.textUsername);
        itemDescription = itemView.findViewById(R.id.textDescription);
        itemPhone = itemView.findViewById(R.id.textPhone);
        itemDate = itemView.findViewById(R.id.textDate);
        itemImage = itemView.findViewById(R.id.imgItem);
        itemQuality = itemView.findViewById(R.id.textQuality);
        itemViewer = itemView.findViewById(R.id.textViewer);

        itemView.setOnCreateContextMenuListener(this);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(), false);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("Select the action");

        menu.add(0,0,getAdapterPosition(), Common.UPDATE );
        menu.add(0,0,getAdapterPosition(), Common.DELETE );

    }

    @Override
    public boolean onLongClick(View v)
    {
        String nameMed = itemUsername.getText().toString();
        return true;
    }
}
