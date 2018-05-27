package com.application.wijayantoap.apupre_loved;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.application.wijayantoap.apupre_loved.Interface.ItemClickListener;
import com.application.wijayantoap.apupre_loved.Model.Item;
import com.application.wijayantoap.apupre_loved.ViewHolder.ItemViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

public class UserItemActivity extends AppCompatActivity {

    ImageView imageView;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference itemList;

    String usernameExtra;

    public FirebaseRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_item);

        // Firebase
        database= FirebaseDatabase.getInstance();
        itemList = database.getReference("Item");

        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewItemUser);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // get intent
        if (getIntent() != null) {
            usernameExtra = getIntent().getStringExtra("usernameExtra");
        }
        if(!usernameExtra.isEmpty() && usernameExtra != null) {
            loadListItem(usernameExtra);
        }
    }

    private void loadListItem(String categoryId) {
        Query query = itemList.orderByChild("username").equalTo(usernameExtra);

        FirebaseRecyclerOptions<Item> options =
                new FirebaseRecyclerOptions.Builder<Item>()
                        .setQuery(query, Item.class)
                        .build();
        adapter = new FirebaseRecyclerAdapter<Item, ItemViewHolder>(options) {
            @Override
            public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.product_item, parent, false);

                return new ItemViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull ItemViewHolder holder, int position, @NonNull Item model) {
                holder.itemTitle.setText(model.getName());
                holder.itemUsername.setText(model.getUsername());
                holder.itemPrice.setText(model.getPrice());
                holder.itemDescription.setText(model.getDescription());
                holder.itemPhone.setText(model.getPhone());
                holder.itemDate.setText(model.getDate());
                holder.itemQuality.setText(model.getQuality());
                Picasso.with(getBaseContext()).load(model.getPicture())
                        .into(holder.itemImage);
                final Item clickItem = model;
                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        //
                    }
                });
            }

        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        //adapter.stopListening();
    }
}
