package com.application.wijayantoap.apupre_loved.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.application.wijayantoap.apupre_loved.R;

public class UserActivityViewHolder extends RecyclerView.ViewHolder{
    public TextView textUsername, textDetails, textDate;

    public UserActivityViewHolder(View itemView) {
        super(itemView);

        textDetails = (TextView) itemView.findViewById(R.id.textActivity);
        textDate = (TextView) itemView.findViewById(R.id.textActivityDate);
    }
}
