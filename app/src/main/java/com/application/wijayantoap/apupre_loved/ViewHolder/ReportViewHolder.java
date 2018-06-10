package com.application.wijayantoap.apupre_loved.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.application.wijayantoap.apupre_loved.Interface.ItemClickListener;
import com.application.wijayantoap.apupre_loved.R;

public class ReportViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView textTitle, textReport, textEmail, textDate;
    public ToggleButton toggleButton;
    public RelativeLayout rootLayout;
    private ItemClickListener itemClickListener;
    public Button btnEmail;

    public ReportViewHolder(View itemView) {
        super(itemView);

        textTitle = itemView.findViewById(R.id.textTitle);
        textReport = itemView.findViewById(R.id.textReport);
        textEmail = itemView.findViewById(R.id.textEmail);
        textDate = itemView.findViewById(R.id.textDate);
        toggleButton = itemView.findViewById(R.id.toggleButton);
        rootLayout = itemView.findViewById(R.id.rootLayout);
        btnEmail = itemView.findViewById(R.id.btnReply);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v,getAdapterPosition(),false);
    }
}
