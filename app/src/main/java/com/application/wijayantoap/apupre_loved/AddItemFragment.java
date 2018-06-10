package com.application.wijayantoap.apupre_loved;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.application.wijayantoap.apupre_loved.Model.Activity;
import com.application.wijayantoap.apupre_loved.Model.Item;
import com.application.wijayantoap.apupre_loved.Model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

import nl.dionsegijn.steppertouch.OnStepCallback;
import nl.dionsegijn.steppertouch.StepperTouch;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;


public class AddItemFragment extends Fragment {

    EditText editTitle, editPrice, editDescription, editPhone;
    String quality = "?";
    CardView cardViewUpload, cardViewSubmit;

    FirebaseDatabase database;
    DatabaseReference items;
    FirebaseStorage storage;
    StorageReference storageReference;
    Item newItem;
    String category;
    Uri saveUri;
    private final int PICK_IMAGE_REQUEST = 71;
    String username;
    StepperTouch stepperTouch;

    DatabaseReference table_activity, table_user;
    DateFormat df = new SimpleDateFormat("dd MMM yyyy");
    final String date = df.format(Calendar.getInstance().getTime());

    private OnFragmentInteractionListener mListener;

    public AddItemFragment() {
        // Required empty public constructor
    }

    public static AddItemFragment newInstance(String param1, String param2) {
        AddItemFragment fragment = new AddItemFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        database = FirebaseDatabase.getInstance();
        items = database.getReference("Item");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        table_activity = database.getReference("Activity");
        table_user = database.getReference("User");


        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("userInfo", MODE_PRIVATE);
        username = sharedPreferences.getString("username", "");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editTitle = view.findViewById(R.id.editItemName);
        editPrice = view.findViewById(R.id.editItemPrice);
        editDescription = view.findViewById(R.id.editItemDescription);
        editPhone = view.findViewById(R.id.editPhoneNumber);
        cardViewUpload = view.findViewById(R.id.cardViewUploadImage);
        cardViewSubmit = view.findViewById(R.id.cardViewSubmit);

        stepperTouch = getView().findViewById(R.id.stepperTouch);
        stepperTouch.stepper.setMin(1);
        stepperTouch.stepper.setMax(5);
        stepperTouch.stepper.setValue(0);
        stepperTouch.stepper.addStepCallback(new OnStepCallback() {
            @Override
            public void onStep(int value, boolean positive) {
                quality = String.valueOf(value);
            }
        });

        Spinner mySpinner= view.findViewById(R.id.spinnerCategory);
        category = mySpinner.getSelectedItem().toString();

        cardViewUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        cardViewSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String validateTitle = editTitle.getText().toString();
                String validatePrice = editPrice.getText().toString();
                String validateDescription = editDescription.getText().toString();
                String validatePhone = editPhone.getText().toString();

                if (validateTitle.matches("") || validatePrice.matches("") || validateDescription.matches("") || validatePhone.matches(""))
                {
                    Toast.makeText(getContext(), "Please fill all the form", Toast.LENGTH_SHORT).show();
                } else{
                    if (newItem != null) {
                        items.push().setValue(newItem);
                        Activity activity = new Activity(username, "Uploaded an item with the title " + validateTitle, date);
                        table_activity.push().setValue(activity);
                        updateItem();
                        Intent intent = new Intent(getActivity(), UserItemActivity.class);
                        intent.putExtra("usernameExtra", username);
                        startActivity(intent);
                        loadFragment();
                        newItem = null;
                        saveUri = null;
                    } else {
                        Toast.makeText(getContext(), "Please upload an image", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
    }

    private void updateItem() {
        table_user.child(username).child("item").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int item = dataSnapshot.getValue(int.class);
                item = item+1;
                dataSnapshot.getRef().setValue(item);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void loadFragment() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
        clearForm();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_item, container, false);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            //Toast.makeText(context, "AddItem Fragment Attached", Toast.LENGTH_SHORT).show();

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


    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"),PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            saveUri = data.getData();
            Toast.makeText(getContext(), "Image selected", Toast.LENGTH_SHORT).show();
        }
        new AlertDialog.Builder(getActivity())
                .setMessage("Are you sure you want to upload the image?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        uploadItem();
                        cardViewSubmit.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void uploadItem() {
        if(saveUri != null) {
            final ProgressDialog mDialog = new ProgressDialog(getContext());
            mDialog.setMessage("Uploading...");
            mDialog.show();

            String imageName = UUID.randomUUID().toString();
            final StorageReference imageFolder = storageReference.child("images/"+imageName);
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
                                    newItem = new Item(editTitle.getText().toString(),username,
                                            editDescription.getText().toString(), editPrice.getText().toString(), uri.toString(), quality,
                                            "+60" +editPhone.getText().toString(), date, category);
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    mDialog.dismiss();
                    Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    mDialog.setMessage("Uploaded " +progress+ " %");
                }
            });
        }
    }

    private void clearForm() {
        editPhone.getText().clear();
        editDescription.getText().clear();
        editPrice.getText().clear();
        editTitle.getText().clear();
    }

}
