package com.application.wijayantoap.apupre_loved;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.application.wijayantoap.apupre_loved.Interface.ItemClickListener;
import com.application.wijayantoap.apupre_loved.Model.Activity;
import com.application.wijayantoap.apupre_loved.Model.Item;
import com.application.wijayantoap.apupre_loved.Model.User;
import com.application.wijayantoap.apupre_loved.ViewHolder.ItemViewHolder;
import com.application.wijayantoap.apupre_loved.ViewHolder.UserActivityViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;

    String username;

    TextView textViewUser, txtEmail, txtReport, txtItem;
    CardView cardViewProduct, cardViewAuction;
    RelativeLayout layoutReport;

    private OnFragmentInteractionListener mListener;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference table_user = database.getReference("User");
    final DatabaseReference table_activity = database.getReference("Activity");
    FirebaseRecyclerAdapter adapter;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();

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
        View view =  inflater.inflate(R.layout.fragment_profile, container, false);

        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("userInfo", MODE_PRIVATE);
        username = sharedPreferences.getString("username", "");

        textViewUser = view.findViewById(R.id.textUser);
        txtEmail = view.findViewById(R.id.textEmail);
        txtItem = view.findViewById(R.id.textItems);

        cardViewProduct = view.findViewById(R.id.cardViewProduct);
        cardViewProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get user and send to new activity
                Intent intent = new Intent(getActivity(), UserItemActivity.class);
                intent.putExtra("usernameExtra", textViewUser.getText());
                startActivity(intent);
            }
        });

        cardViewAuction = view.findViewById(R.id.cardViewAuction);
        cardViewAuction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AuctionActivity.class);
                startActivity(intent);
            }
        });

        layoutReport = view.findViewById(R.id.layoutSendReport);
        layoutReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ReportActivity.class);
                intent.putExtra("emailExtra", txtEmail.getText());
                startActivity(intent);
            }
        });

        textViewUser.setText(username);

        table_user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.child(username).getValue(User.class);

                String email = user.getEmail();
                int item = user.getItem();

                txtEmail.setText(email);
                txtItem.setText(String.valueOf(item) + " Items");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        // load activity
        recyclerView = view.findViewById(R.id.recyclerViewProfileActivity);
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        loadActivity(username);
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        CardView cardViewExit = (CardView) getActivity().findViewById(R.id.cardViewExit);
        cardViewExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setMessage("Are you sure you want to exit?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                logout();
                                Intent intent = new Intent(getActivity(), LoginActivity.class);
                                startActivity(intent);
                                getActivity().finish();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            //Toast.makeText(context, "Profile Fragment Attached", Toast.LENGTH_SHORT).show();

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

    public void logout() {
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("userInfo", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("username", "");
        editor.apply();
    }

    private void loadActivity(String username) {
        Query query = table_activity.orderByChild("username").equalTo(username);

        FirebaseRecyclerOptions<Activity> options =
                new FirebaseRecyclerOptions.Builder<Activity>()
                        .setQuery(query, Activity.class)
                        .build();
        adapter = new FirebaseRecyclerAdapter<Activity, UserActivityViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull UserActivityViewHolder holder, int position, @NonNull Activity model) {
                holder.textDetails.setText(model.getDetails());
                holder.textDate.setText(model.getDate());
            }

            @Override
            public UserActivityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_activity_for_user_item, parent, false);

                return new UserActivityViewHolder(view);
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
    public void onDestroy() {
        super.onDestroy();
        adapter.stopListening();
    }
}
