package com.application.wijayantoap.apupre_loved.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.application.wijayantoap.apupre_loved.R;

public class FlagViewHolder extends RecyclerView.ViewHolder{

    public TextView textTitle, textUsername, textDate;

    public FlagViewHolder(View itemView) {
        super(itemView);

        textTitle = itemView.findViewById(R.id.textTitle);
        textUsername = itemView.findViewById(R.id.textUsername);
        textDate = itemView.findViewById(R.id.textDate);
    }
}
