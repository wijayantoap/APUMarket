package com.application.wijayantoap.apupre_loved;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.application.wijayantoap.apupre_loved.Interface.ItemClickListener;
import com.application.wijayantoap.apupre_loved.Model.Activity;
import com.application.wijayantoap.apupre_loved.Model.Flag;
import com.application.wijayantoap.apupre_loved.Model.Item;
import com.application.wijayantoap.apupre_loved.Model.Message;
import com.application.wijayantoap.apupre_loved.Model.Report;
import com.application.wijayantoap.apupre_loved.ViewHolder.ItemViewHolder;
import com.application.wijayantoap.apupre_loved.ViewHolder.MessageViewHolder;
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

public class ChatActivity extends AppCompatActivity {

    String chatId;

    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference message;

    FirebaseRecyclerAdapter adapter;

    ImageView imgSend;
    EditText editText;

    String username;
    Toolbar mActionBarToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        database = FirebaseDatabase.getInstance();
        message = database.getReference("Message");

        SharedPreferences sharedPreferences = this.getSharedPreferences("userInfo", MODE_PRIVATE);
        username = sharedPreferences.getString("username", "");

        DateFormat df = new SimpleDateFormat("dd MMM yyyy hh:mm");
        final String date = df.format(Calendar.getInstance().getTime());

        recyclerView = findViewById(R.id.recyclerViewMessage);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

        // get intent
        if (getIntent() != null) {
            chatId = getIntent().getStringExtra("chatId");
        }
        if (!chatId.isEmpty() && chatId != null) {
            loadMessage();
        }

        mActionBarToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mActionBarToolbar);
        getSupportActionBar().setTitle(chatId);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editText = findViewById(R.id.editMessage);
        imgSend = findViewById(R.id.imgSend);
        imgSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String messageVerif = editText.getText().toString();

                if (!messageVerif.matches("")) {
                    message.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Message newMessage = new Message(username, messageVerif, date);
                            message.child(chatId).push().setValue(newMessage);
                            editText.setText("");
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }

            }
        });

    }

    private void loadMessage() {
        Query query = message.child(chatId);
        Toast.makeText(this, ""+chatId, Toast.LENGTH_SHORT).show();

        FirebaseRecyclerOptions<Message> options =
                new FirebaseRecyclerOptions.Builder<Message>()
                        .setQuery(query, Message.class)
                        .build();
        adapter = new FirebaseRecyclerAdapter<Message, MessageViewHolder>(options) {
            @Override
            public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.message_item, parent, false);

                return new MessageViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull MessageViewHolder holder, final int position, @NonNull final Message model) {
                holder.textMessage.setText(model.getSender()+": " +model.getMessage());
                holder.textTimeStamp.setText(model.getTimeStamp());
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
