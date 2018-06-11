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
import com.application.wijayantoap.apupre_loved.Model.Flag;
import com.application.wijayantoap.apupre_loved.ViewHolder.FlagViewHolder;
import com.application.wijayantoap.apupre_loved.ViewHolder.UserActivityViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


public class AdminFlagFragment extends Fragment {
    private OnFragmentInteractionListener mListener;

    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference table_flag = database.getReference("Flag");
    FirebaseRecyclerAdapter adapter;

    public AdminFlagFragment() {
        // Required empty public constructor
    }

    public static AdminFlagFragment newInstance(String param1, String param2) {
        AdminFlagFragment fragment = new AdminFlagFragment();
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
        View view =  inflater.inflate(R.layout.fragment_admin_flag, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewFlag);
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        loadActivity();

        return view;
    }

    private void loadActivity() {
        Query query = table_flag;

        FirebaseRecyclerOptions<Flag> options =
                new FirebaseRecyclerOptions.Builder<Flag>()
                        .setQuery(query, Flag.class)
                        .build();
        adapter = new FirebaseRecyclerAdapter<Flag, FlagViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FlagViewHolder holder, int position, @NonNull Flag model) {
                holder.textTitle.setText(model.getTitle());
                holder.textDate.setText(model.getDate());
                holder.textUsername.setText(model.getUsername());
            }

            @Override
            public FlagViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.flag_item, parent, false);

                return new FlagViewHolder(view);
            }
        };
        recyclerView.setAdapter(adapter);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
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
