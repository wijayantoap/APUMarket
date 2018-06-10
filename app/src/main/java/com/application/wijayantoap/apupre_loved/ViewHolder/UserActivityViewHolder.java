package com.application.wijayantoap.apupre_loved.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.application.wijayantoap.apupre_loved.R;

public class UserActivityViewHolder extends RecyclerView.ViewHolder{
    public TextView textUsername, textDetails, textDate;
    public ImageView imgDotActivity;

    public UserActivityViewHolder(View itemView) {
        super(itemView);

        textDetails = (TextView) itemView.findViewById(R.id.textActivity);
        textDate = (TextView) itemView.findViewById(R.id.textActivityDate);
        textUsername = (TextView) itemView.findViewById(R.id.textUsername);
        imgDotActivity = itemView.findViewById(R.id.imgDot);
    }
}
