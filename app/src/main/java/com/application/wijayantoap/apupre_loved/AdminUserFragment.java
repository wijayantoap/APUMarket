package com.application.wijayantoap.apupre_loved;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.application.wijayantoap.apupre_loved.Common.Common;
import com.application.wijayantoap.apupre_loved.Interface.ItemClickListener;
import com.application.wijayantoap.apupre_loved.Model.Activity;
import com.application.wijayantoap.apupre_loved.Model.Item;
import com.application.wijayantoap.apupre_loved.Model.User;
import com.application.wijayantoap.apupre_loved.ViewHolder.ItemViewHolder;
import com.application.wijayantoap.apupre_loved.ViewHolder.UserViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class AdminUserFragment extends Fragment {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference userList;
    FirebaseRecyclerAdapter adapter;

    DatabaseReference table_activity;

    FirebaseStorage storage;
    StorageReference storageReference;
    String date, username, status;

    TextView textUsername, textEmail, textReport, textItem;
    Spinner spinnerStatus;

    FrameLayout rootLayout;

    User newUser;

    private OnFragmentInteractionListener mListener;

    public AdminUserFragment() {
        // Required empty public constructor
    }

    public static AdminUserFragment newInstance(String param1, String param2) {
        AdminUserFragment fragment = new AdminUserFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Firebase
        database = FirebaseDatabase.getInstance();
        userList = database.getReference("User");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        table_activity = database.getReference("Activity");

        // date
        DateFormat df = new SimpleDateFormat("dd MMM yyyy");
        date = df.format(Calendar.getInstance().getTime());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_user, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewUser);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        rootLayout = view.findViewById(R.id.rootLayout);

        loadListUser();

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

    private void loadListUser() {
        Query query = userList;

        FirebaseRecyclerOptions<User> options =
                new FirebaseRecyclerOptions.Builder<User>()
                        .setQuery(query, User.class)
                        .build();
        adapter = new FirebaseRecyclerAdapter<User, UserViewHolder>(options) {
            @Override
            public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.user_item, parent, false);

                return new UserViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull UserViewHolder holder, int position, @NonNull User model) {
                holder.userName.setText(getRef(position).getKey());
                holder.userEmail.setText(model.getEmail());
                final User clickItem = model;
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

    //Update / Delete

    @Override
    public boolean onContextItemSelected(final MenuItem item) {
        if (item.getTitle().equals(Common.UPDATE)) {
            showUpdateDialog(adapter.getRef(item.getOrder()).getKey(), (User) adapter.getItem(item.getOrder()));
        } else if (item.getTitle().equals(Common.DELETE)) {
            new AlertDialog.Builder(getContext())
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

    private void showUpdateDialog(final String key, final User user) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        alertDialog.setTitle("Edit");
        alertDialog.setMessage("Please fill the information");

        LayoutInflater inflater = this.getLayoutInflater();
        View edit_item_layout = inflater.inflate(R.layout.edit_user_layout, null);

        textUsername = edit_item_layout.findViewById(R.id.textUsername);
        textEmail = edit_item_layout.findViewById(R.id.textEmail);
        textReport = edit_item_layout.findViewById(R.id.textReport);
        textItem = edit_item_layout.findViewById(R.id.textItem);
        spinnerStatus = edit_item_layout.findViewById(R.id.spinnerStatus);

        textUsername.setText(key);
        textEmail.setText(user.getEmail());
        //textReport.setText(String.valueOf(user.getReport()));
        //textItem.setText(String.valueOf(user.getItem()));

        alertDialog.setView(edit_item_layout);
        alertDialog.setIcon(R.drawable.ic_edit);

        // set button
        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                status = spinnerStatus.getSelectedItem().toString();

                newUser = new User(user.getEmail(), user.getPassword(), user.getReport(), status, user.getItem());

                userList.child(key).setValue(newUser);
                if (status.matches("inactive")) {
                    Activity activity = new Activity(key, " has been blocked", date);
                    table_activity.push().setValue(activity);
                    Snackbar.make(rootLayout, key +" has been blocked", Snackbar.LENGTH_SHORT).show();
                }

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

    private void deleteItem(String key) {
        Activity activity = new Activity(key, "User deleted", date);
        table_activity.push().setValue(activity);
        userList.child(key).removeValue();
        Snackbar.make(rootLayout, "User deleted", Snackbar.LENGTH_SHORT).show();
    }
}
