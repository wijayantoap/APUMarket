package com.application.wijayantoap.apupre_loved.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.application.wijayantoap.apupre_loved.R;

public class MessageViewHolder extends RecyclerView.ViewHolder {

    public TextView textMessage, textTimeStamp;


    public MessageViewHolder(View itemView) {
        super(itemView);

        textMessage = itemView.findViewById(R.id.textMessage);
        textTimeStamp = itemView.findViewById(R.id.textTime);
    }
}
