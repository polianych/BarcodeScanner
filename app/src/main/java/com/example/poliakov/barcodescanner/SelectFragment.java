package com.example.poliakov.barcodescanner;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class SelectFragment extends Fragment implements OnClickListener {
    public static final String IMPORT_FRAGMENT_TAG = "fragment:import";

//    private OnFragmentInteractionListener mListener;

    public SelectFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setTitle("Select");
        View RootView = inflater.inflate(R.layout.fragment_select, container, false);

        FloatingActionButton importButton = (FloatingActionButton) RootView.findViewById(R.id.import_button);
        importButton.setOnClickListener(this);

        DaoSession daoSession = ((App) getActivity().getApplication()).getDaoSession();
        BarcodeDatabaseDao BarcodeDatabaseDao = daoSession.getBarcodeDatabaseDao();
        List barcodeDatabaseList = BarcodeDatabaseDao.loadAll();

        RecyclerView recyclerView = (RecyclerView) RootView.findViewById(R.id.barcode_database_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        BarcodeDatabaseAdapter adapter = new BarcodeDatabaseAdapter(barcodeDatabaseList, new BarcodeDatabaseAdapter.OnItemClickListener() {
            @Override public void onItemClick(BarcodeDatabase item) {
                Fragment barcodeDatabaseFragment = BarcodeDatabaseFragment.newInstance(item.getId());
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.flContent, barcodeDatabaseFragment, BarcodeDatabaseFragment.BARCODE_DATABASE_FRAGMENT_TAG);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        recyclerView.setAdapter(adapter);
        //Add divider between items of RecyclerView
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        return RootView;
    }

    @Override
    public void onClick(View view) {
        final SelectFragment fragment = this;
        switch (view.getId()) {
            case R.id.import_button:
                Fragment importFragment = new ImportFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.flContent, importFragment, IMPORT_FRAGMENT_TAG);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
        }
    }
}
