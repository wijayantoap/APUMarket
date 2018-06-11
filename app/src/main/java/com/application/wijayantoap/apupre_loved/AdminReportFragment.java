package com.application.wijayantoap.apupre_loved;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.application.wijayantoap.apupre_loved.Interface.ItemClickListener;
import com.application.wijayantoap.apupre_loved.Model.Category;
import com.application.wijayantoap.apupre_loved.Model.Report;
import com.application.wijayantoap.apupre_loved.ViewHolder.CategoryViewHolder;
import com.application.wijayantoap.apupre_loved.ViewHolder.ReportViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import static android.view.View.VISIBLE;


public class AdminReportFragment extends Fragment {

    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference reportList;
    FirebaseRecyclerAdapter adapter;

    String currentSearch = "";

    EditText editFind;

    public AdminReportFragment() {
        // Required empty public constructor
    }

    public static AdminReportFragment newInstance(String param1, String param2) {
        AdminReportFragment fragment = new AdminReportFragment();
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
        View view = inflater.inflate(R.layout.fragment_admin_report, container, false);

        // Firebase
        database = FirebaseDatabase.getInstance();
        reportList = database.getReference("Report");

        recyclerView = view.findViewById(R.id.recyclerViewReport);
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

        loadReport(currentSearch);

        editFind = view.findViewById(R.id.editFind);
        editFind.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    currentSearch = editFind.getText().toString();
                    loadReport(currentSearch);
                    adapter.startListening();
                }
                return true;
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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

    private void loadReport(String currentSearch){

        Query query = FirebaseDatabase
                .getInstance()
                .getReference()
                .child("Report").orderByChild("email").startAt(currentSearch).endAt(currentSearch + "\uf8ff");;

        FirebaseRecyclerOptions<Report> options =
                new FirebaseRecyclerOptions.Builder<Report>()
                        .setQuery(query, Report.class)
                        .build();
        adapter = new FirebaseRecyclerAdapter<Report, ReportViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final ReportViewHolder holder, int position, @NonNull Report model) {
                holder.textEmail.setText(model.getEmail());
                holder.textDate.setText(model.getDate());
                holder.textReport.setText(model.getReport());
                holder.textTitle.setText(model.getTitle());
                holder.rootLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                holder.toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked) {
                            holder.textReport.setVisibility(VISIBLE);
                            holder.btnEmail.setVisibility(VISIBLE);
                        } else {
                            holder.textReport.setVisibility(View.GONE);
                            holder.btnEmail.setVisibility(View.GONE);
                        }
                    }
                });
                holder.btnEmail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        Uri data = Uri.parse("mailto:" + holder.textEmail.getText().toString() + "?subject=" + "RE: " + holder.textTitle.getText().toString() + "&body=" + "");
                        intent.setData(data);
                        startActivity(intent);
                    }
                });
            }

            @Override
            public ReportViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(getActivity())
                        .inflate(R.layout.report_item, parent, false);

                return new ReportViewHolder(view);
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
