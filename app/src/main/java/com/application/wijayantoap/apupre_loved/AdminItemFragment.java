package com.application.wijayantoap.apupre_loved;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.application.wijayantoap.apupre_loved.Common.Common;
import com.application.wijayantoap.apupre_loved.Interface.ItemClickListener;
import com.application.wijayantoap.apupre_loved.Model.Activity;
import com.application.wijayantoap.apupre_loved.Model.Item;
import com.application.wijayantoap.apupre_loved.ViewHolder.AdminItemViewHolder;
import com.application.wijayantoap.apupre_loved.ViewHolder.ItemViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;


public class AdminItemFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference itemList, table_activity, table_user;

    FirebaseRecyclerAdapter adapter;

    EditText editFind;
    String currentSearch = "";
    TextView txtImgPath;
    EditText editTitle, editPrice, editDescription, editQuality, editPhone;
    Spinner spinnerCategory;
    Button btnChoose;
    String category, username, mImageUrl;

    FrameLayout rootLayout;

    String date;
    Item newItem;

    private final int PICK_IMAGE_REQUEST = 71;
    Uri saveUri;
    FirebaseStorage storage;
    StorageReference storageReference;

    public AdminItemFragment() {
        // Required empty public constructor
    }
    public static AdminItemFragment newInstance(String param1, String param2) {
        AdminItemFragment fragment = new AdminItemFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_item, container, false);

        // Firebase
        database= FirebaseDatabase.getInstance();
        itemList = database.getReference("Item");
        table_user = database.getReference("User");
        table_activity = database.getReference("Activity");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        rootLayout = view.findViewById(R.id.rootLayout);

        DateFormat df = new SimpleDateFormat("dd MMM yyyy");
        date = df.format(Calendar.getInstance().getTime());

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewItem);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        editFind = view.findViewById(R.id.editFind);
        editFind.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    currentSearch = editFind.getText().toString().toLowerCase();
                    loadListItem(currentSearch);
                    adapter.startListening();
                }
                return true;
            }
        });

        loadListItem(currentSearch);


        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {

        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void loadListItem(String currentSearch) {
        Query query = itemList.orderByChild("toLowerCase").startAt(currentSearch).endAt(currentSearch + "\uf8ff");

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
            protected void onBindViewHolder(@NonNull final AdminItemViewHolder holder, int position, @NonNull final Item model) {
                holder.itemTitle.setText(model.getName());
                holder.itemUsername.setText(model.getUsername());
                holder.itemPrice.setText(model.getPrice());
                holder.itemDescription.setText(model.getDescription());
                holder.itemPhone.setText(model.getPhone());
                holder.itemDate.setText(model.getDate());
                holder.itemQuality.setText(model.getQuality());
                holder.itemViewer.setText(String.valueOf(model.getView()));
                Picasso.with(getActivity()).load(model.getPicture()).fit().centerInside()
                        .into(holder.itemImage);
                final Item clickItem = model;
                holder.itemImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent= new Intent(getContext(), ImageActivity.class);
                        intent.putExtra("image_url", model.getPicture().toString());
                        startActivity(intent);
                    }
                });
                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
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

    // update / delete

    @Override
    public boolean onContextItemSelected(final MenuItem item) {
        if (item.getTitle().equals(Common.UPDATE)) {
            showUpdateDialog(adapter.getRef(item.getOrder()).getKey(), (Item) adapter.getItem(item.getOrder()));
        } else if (item.getTitle().equals(Common.DELETE)) {
            new AlertDialog.Builder(getActivity())
                    .setMessage("Are you sure you want to delete this?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            deleteItem(adapter.getRef(item.getOrder()).getKey(), (Item) adapter.getItem(item.getOrder()));
                            updateItem();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        }
        return super.onContextItemSelected(item);
    }

    private void deleteItem(String key, Item item) {
        username = item.getUsername();
        String imageName = item.getPicture();

        itemList.child(key).removeValue();
        Activity activity = new Activity(username, "Item deleted by admin", date);
        table_activity.push().setValue(activity);
        Snackbar.make(rootLayout, "Item deleted", Snackbar.LENGTH_SHORT).show();
        adapter.notifyDataSetChanged();
    }


    private void showUpdateDialog(final String key, final Item item) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
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

        username = item.getUsername();

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
                        editPhone.getText().toString(), date, category, 0, editTitle.getText().toString().toLowerCase());

                itemList.child(key).setValue(newItem);
                Snackbar.make(rootLayout, "Item edited", Snackbar.LENGTH_SHORT).show();
                Activity activity = new Activity(username, "Admin edited an item with the title " + editTitle.getText().toString(), date);
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
        adapter.notifyDataSetChanged();
    }


    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            saveUri = data.getData();
            Toast.makeText(getActivity(), "Image selected", Toast.LENGTH_SHORT).show();
        }
        new AlertDialog.Builder(getActivity())
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
            final ProgressDialog mDialog = new ProgressDialog(getActivity());
            mDialog.setMessage("Uploading...");
            mDialog.show();

            String imageName = UUID.randomUUID().toString();
            final StorageReference imageFolder = storageReference.child("images/" + imageName);
            imageFolder.putFile(saveUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            mDialog.dismiss();
                            Toast.makeText(getContext(), "Uploaded", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(getContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void updateItem() {
        table_user.child(username).child("item").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int item = dataSnapshot.getValue(int.class);
                item = item - 1;
                if (item < 0) {
                    item = 0;
                }
                dataSnapshot.getRef().setValue(item);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
