package com.application.wijayantoap.apupre_loved;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.application.wijayantoap.apupre_loved.Interface.ItemClickListener;
import com.application.wijayantoap.apupre_loved.Model.Item;
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

import java.util.StringTokenizer;

public class SearchActivity extends AppCompatActivity {

    ImageView imageView;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference itemList;

    String currentSearch;

    public FirebaseRecyclerAdapter adapter;

    EditText editFind;
    ImageView imgBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        AlertDialog.Builder builder =
                new AlertDialog.Builder(SearchActivity.this, R.style.Theme_AppCompat_Light_Dialog);
        builder.setTitle("Notice");
        builder.setMessage(R.string.search_alert);
        builder.setPositiveButton("OK", null);//second parameter used for onclicklistener
        builder.show();

        // Firebase
        database = FirebaseDatabase.getInstance();
        itemList = database.getReference("Item");

        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewSearch);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        loadSearch(currentSearch);

        imgBack = (ImageView) findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        editFind = findViewById(R.id.editFind);
        editFind.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    currentSearch = editFind.getText().toString();
                    loadSearch(currentSearch);
                    editFind.clearFocus();
                    return true;
                }
                return false;
            }
        });
    }

    private void loadSearch(String currentSearch) {
        Query query = itemList.orderByChild("name").startAt(currentSearch).endAt(currentSearch + "\uf8ff");

        final FirebaseRecyclerOptions<Item> options =
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
            protected void onBindViewHolder(@NonNull final ItemViewHolder holder, final int position, @NonNull final Item model) {

                final DatabaseReference dbRef = getRef(position);
                final String postKey = dbRef.getKey();

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

                        Intent intent= new Intent(SearchActivity.this, ImageActivity.class);
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
        adapter.stopListening();
    }
}
