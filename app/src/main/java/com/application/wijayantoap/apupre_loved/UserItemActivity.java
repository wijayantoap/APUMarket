package com.application.wijayantoap.apupre_loved;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.application.wijayantoap.apupre_loved.Common.Common;
import com.application.wijayantoap.apupre_loved.Interface.ItemClickListener;
import com.application.wijayantoap.apupre_loved.Model.Activity;
import com.application.wijayantoap.apupre_loved.Model.Item;
import com.application.wijayantoap.apupre_loved.ViewHolder.AdminItemViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

public class UserItemActivity extends AppCompatActivity {


    ImageView imageView;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    RelativeLayout rootLayout;

    FirebaseDatabase database;
    DatabaseReference itemList;
    String usernameExtra = "";

    EditText editTitle, editPrice, editDescription, editQuality, editPhone;
    TextView txtImgPath;
    Spinner spinnerCategory;
    Button btnChoose, btnUpload, btnSubmit;

    FirebaseRecyclerAdapter adapter;
    DatabaseReference table_activity;

    Uri saveUri;
    private final int PICK_IMAGE_REQUEST = 71;
    String category, username;

    FirebaseStorage storage;
    StorageReference storageReference;
    String date;
    Item newItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_item);

        SharedPreferences sharedPreferences = getSharedPreferences("userInfo", MODE_PRIVATE);
        username = sharedPreferences.getString("username", "");

        DateFormat df = new SimpleDateFormat("dd MMM yyyy");
        date = df.format(Calendar.getInstance().getTime());

        // Firebase
        database = FirebaseDatabase.getInstance();
        itemList = database.getReference("Item");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        table_activity = database.getReference("Activity");

        rootLayout = findViewById(R.id.rootLayout);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewItemUser);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // get intent
        if (getIntent() != null) {
            usernameExtra = getIntent().getStringExtra("usernameExtra");
        }
        if (!usernameExtra.isEmpty() && usernameExtra != null) {
            loadListItem(usernameExtra);
        }
    }

    private void loadListItem(String categoryId) {
        Query query = itemList.orderByChild("username").equalTo(usernameExtra);

        FirebaseRecyclerOptions<Item> options =
                new FirebaseRecyclerOptions.Builder<Item>()
                        .setQuery(query, Item.class)
                        .build();
        adapter = new FirebaseRecyclerAdapter<Item, AdminItemViewHolder>(options) {
            @Override
            public AdminItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.admin_product_item, parent, false);

                return new AdminItemViewHolder(view);
            }

            @Override
            public Item getItem(int position) {
                return super.getItem(getItemCount() - 1 - position);
            }

            @Override
            protected void onBindViewHolder(@NonNull AdminItemViewHolder holder, int position, @NonNull Item model) {
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
        adapter.stopListening();
    }

    //Update / Delete

    @Override
    public boolean onContextItemSelected(final MenuItem item) {
        if (item.getTitle().equals(Common.UPDATE)) {
            showUpdateDialog(adapter.getRef(item.getOrder()).getKey(), (Item) adapter.getItem(item.getOrder()));
        } else if (item.getTitle().equals(Common.DELETE)) {
            new AlertDialog.Builder(this)
                    .setMessage("Are you sure you want to delete this?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            deleteItem(adapter.getRef(item.getOrder()).getKey());
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        }
        return super.onContextItemSelected(item);
    }

    private void deleteItem(String key) {
        itemList.child(key).removeValue();
        Activity activity = new Activity(username, "Item deleted", date);
        table_activity.push().setValue(activity);
        Snackbar.make(rootLayout, "Item deleted", Snackbar.LENGTH_SHORT).show();
    }


    private void showUpdateDialog(final String key, final Item item) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(UserItemActivity.this);
        alertDialog.setTitle("Edit");
        alertDialog.setMessage("Please fill the information");

        LayoutInflater inflater = this.getLayoutInflater();
        View edit_item_layout = inflater.inflate(R.layout.edit_item_layout, null);

        editTitle = edit_item_layout.findViewById(R.id.editTitle);
        editPrice = edit_item_layout.findViewById(R.id.editPrice);
        editDescription = edit_item_layout.findViewById(R.id.editDescription);
        editQuality = edit_item_layout.findViewById(R.id.editQuality);
        editPhone = edit_item_layout.findViewById(R.id.editPhoneNumber);
        spinnerCategory = edit_item_layout.findViewById(R.id.spinnerCategory);
        btnChoose = edit_item_layout.findViewById(R.id.btnChoose);
        txtImgPath = edit_item_layout.findViewById(R.id.imgPath);

        editTitle.setText(item.getName());
        editPrice.setText(item.getPrice());
        editDescription.setText(item.getDescription());
        editQuality.setText(item.getQuality());
        editPhone.setText(item.getPhone());
        txtImgPath.setText(item.getPicture());

        editQuality.setFilters(new InputFilter[]{new InputFilterMinMax("1", "5")});

        // event for button
        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        alertDialog.setView(edit_item_layout);
        alertDialog.setIcon(R.drawable.ic_edit);

        // set button
        alertDialog.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                category = spinnerCategory.getSelectedItem().toString();

                newItem = new Item(editTitle.getText().toString(), username,
                        editDescription.getText().toString(), editPrice.getText().toString(),
                        txtImgPath.getText().toString(), editQuality.getText().toString(),
                        editPhone.getText().toString(), date, category);

                itemList.child(key).setValue(newItem);
                Snackbar.make(rootLayout, "Item edited", Snackbar.LENGTH_SHORT).show();
                Activity activity = new Activity(username, "Edited an item with the title " + editTitle.getText().toString(), date);
                table_activity.push().setValue(activity);

            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }


    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            saveUri = data.getData();
            Toast.makeText(this, "Image selected", Toast.LENGTH_SHORT).show();
        }
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to upload the image?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        uploadItem();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }


    private void uploadItem() {
        if (saveUri != null) {
            final ProgressDialog mDialog = new ProgressDialog(this);
            mDialog.setMessage("Uploading...");
            mDialog.show();

            String imageName = UUID.randomUUID().toString();
            final StorageReference imageFolder = storageReference.child("images/" + imageName);
            imageFolder.putFile(saveUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            mDialog.dismiss();
                            Toast.makeText(UserItemActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                            imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    // set value for newItem if image upload and get download link

                                    txtImgPath.setText(uri.toString());
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    mDialog.dismiss();
                    Toast.makeText(UserItemActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    mDialog.setMessage("Uploaded " + progress + " %");
                }
            });
        }
    }
}
