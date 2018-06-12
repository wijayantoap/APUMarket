package com.application.wijayantoap.apupre_loved;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.application.wijayantoap.apupre_loved.Interface.ItemClickListener;
import com.application.wijayantoap.apupre_loved.Model.Activity;
import com.application.wijayantoap.apupre_loved.Model.Category;
import com.application.wijayantoap.apupre_loved.Model.Flag;
import com.application.wijayantoap.apupre_loved.Model.Item;
import com.application.wijayantoap.apupre_loved.ViewHolder.CategoryViewHolder;
import com.application.wijayantoap.apupre_loved.ViewHolder.ItemViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ItemActivity extends AppCompatActivity {

    ImageView imageView;

    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference itemList;
    DatabaseReference table_flag;
    DatabaseReference table_activity;
    DatabaseReference table_auction;

    String categoryId = "";
    String date, username;

    FirebaseRecyclerAdapter adapter;

    Toolbar mActionBarToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences("userInfo", MODE_PRIVATE);
        username = sharedPreferences.getString("username", "");

        DateFormat df = new SimpleDateFormat("dd MMM yyyy");
        date = df.format(Calendar.getInstance().getTime());

        // Firebase
        database= FirebaseDatabase.getInstance();
        itemList = database.getReference("Item");
        table_activity = database.getReference("Activity");
        table_flag = database.getReference("Flag");
        table_auction = database.getReference("Auction");

        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewItem);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

        ImageView imageBackground = (ImageView) findViewById(R.id.image_id);

        // get intent
        if (getIntent() != null) {
            categoryId = getIntent().getStringExtra("CategoryId");
        }
        if(!categoryId.isEmpty() && categoryId != null) {

            switch (categoryId){
                case "Vehicles": setTitle("Vehicles");
                    Picasso.with(this).load("https://images.pexels.com/photos/990113/pexels-photo-990113.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940").fit().centerInside()
                            .into(imageBackground);
                break;
                case "Electronics": setTitle("Electronics");
                    Picasso.with(this).load("https://images.pexels.com/photos/325153/pexels-photo-325153.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940").fit().centerCrop()
                            .into(imageBackground);
                    break;
                case "Furniture": setTitle("Furniture");
                    Picasso.with(this).load("https://images.pexels.com/photos/159839/office-home-house-desk-159839.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940").fit().centerCrop()
                            .into(imageBackground);
                    break;
                case "Hobbies": setTitle("Hobbies");
                    Picasso.with(this).load("https://images.pexels.com/photos/346709/pexels-photo-346709.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940").fit().centerCrop()
                            .into(imageBackground);
                    break;
                case "Clothes": setTitle("Clothes");
                    Picasso.with(this).load("https://images.pexels.com/photos/325876/pexels-photo-325876.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940").fit().centerCrop()
                            .into(imageBackground);
                    break;
                case "Miscellaneous": setTitle("Miscellaneous");
                    Picasso.with(this).load("https://images.pexels.com/photos/1712/sunglasses-apple-iphone-desk.jpg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940").fit().centerCrop()
                            .into(imageBackground);
                    break;
            }

            loadListItem(categoryId);
        }
        loadListItem(categoryId);
    }

    private void loadListItem(String categoryId) {
        Query query = itemList.orderByChild("categoryId").equalTo(categoryId);

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
            protected void onBindViewHolder(@NonNull ItemViewHolder holder, final int position, @NonNull final Item model) {
                final DatabaseReference dbRef = getRef(position);
                final String postKey = dbRef.getKey();

                holder.itemTitle.setText(model.getName());
                holder.itemUsername.setText(model.getUsername());
                holder.itemPrice.setText(model.getPrice());
                holder.itemDescription.setText(model.getDescription());
                holder.itemPhone.setText(model.getPhone());
                holder.itemDate.setText(model.getDate());
                holder.itemQuality.setText(model.getQuality());
                Picasso.with(getBaseContext()).load(model.getPicture()).fit().centerInside()
                        .into(holder.itemImage);
                holder.layoutFlag.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String title = ((TextView) recyclerView.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.textTitle)).getText().toString();
                        AlertDialog.Builder alertDialog = new android.support.v7.app.AlertDialog.Builder(ItemActivity.this);
                        alertDialog.setTitle("Flag");
                        alertDialog.setMessage("Flag this post to alert the administrators?");
                        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Activity activity = new Activity(username, "Flagged " + title, date);
                                table_activity.push().setValue(activity);
                                Flag flag = new Flag(username, title, date);
                                table_flag.push().setValue(flag);
                                Toast.makeText(ItemActivity.this, "Flagged " + title, Toast.LENGTH_SHORT).show();
                            }
                        });
                        alertDialog.setNegativeButton("No", null);
                        alertDialog.show();
                    }
                });
                holder.layoutMessage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String owner = ((TextView) recyclerView.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.textUsername)).getText().toString();
                        table_auction.child(owner).child(username).setValue(username);
                        Intent intent = new Intent(ItemActivity.this, ChatActivity.class);
                        intent.putExtra("chatId", owner);
                        startActivity(intent);
                    }
                });
                final Item clickItem = model;
                holder.itemImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        itemList.child(postKey).child("view").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                int item = dataSnapshot.getValue(int.class);
                                item = item + 1;
                                dataSnapshot.getRef().setValue(item);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        Intent intent= new Intent(ItemActivity.this, ImageActivity.class);
                        intent.putExtra("image_url", model.getPicture().toString());
                        startActivity(intent);
                    }
                });
                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        //
                    }
                });
            }

        };
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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        adapter.stopListening();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) // Press Back Icon
        {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
