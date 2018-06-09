package com.application.wijayantoap.apupre_loved;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.application.wijayantoap.apupre_loved.Model.Activity;
import com.application.wijayantoap.apupre_loved.ViewHolder.UserActivityViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import static android.widget.LinearLayout.VERTICAL;

public class AdminHomeFragment extends Fragment {

    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;


    FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference table_activity = database.getReference("Activity");
    FirebaseRecyclerAdapter adapter;

    public AdminHomeFragment() {
        // Required empty public constructor
    }

    public static AdminHomeFragment newInstance(String param1, String param2) {
        AdminHomeFragment fragment = new AdminHomeFragment();
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
        View view = inflater.inflate(R.layout.fragment_admin_home, container, false);

        // load activity
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewActivity);
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        loadActivity();

        return  view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
        } else {
            
        }
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

    private void loadActivity() {
        Query query = table_activity;

        FirebaseRecyclerOptions<Activity> options =
                new FirebaseRecyclerOptions.Builder<Activity>()
                        .setQuery(query, Activity.class)
                        .build();
        adapter = new FirebaseRecyclerAdapter<Activity, UserActivityViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull UserActivityViewHolder holder, int position, @NonNull Activity model) {
                holder.textDetails.setText(model.getDetails());
                holder.textDate.setText(model.getDate());
                holder.textUsername.setText(model.getUsername());
            }

            @Override
            public UserActivityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_activity_item, parent, false);

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
        adapter.stopListening();
    }
}
