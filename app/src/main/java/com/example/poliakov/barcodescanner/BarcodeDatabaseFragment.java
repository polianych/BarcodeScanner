package com.example.poliakov.barcodescanner;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link BarcodeDatabaseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BarcodeDatabaseFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String BARCODE_DATABASE_ID = "barcodeDatabaseId";

    // TODO: Rename and change types of parameters
    private Long mBarcodeDatabaseId;
    private BarcodeDatabase mBarcodeDatabase;

    public BarcodeDatabaseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param barcodeDatabaseId Parameter 1.
     * @return A new instance of fragment BarcodeDatabaseFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BarcodeDatabaseFragment newInstance(Long barcodeDatabaseId) {
        BarcodeDatabaseFragment fragment = new BarcodeDatabaseFragment();
        Bundle args = new Bundle();
        args.putLong(BARCODE_DATABASE_ID, barcodeDatabaseId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mBarcodeDatabaseId = getArguments().getLong(BARCODE_DATABASE_ID);
            DaoSession daoSession = ((App) getActivity().getApplication()).getDaoSession();
            BarcodeDatabaseDao BarcodeDatabaseDao = daoSession.getBarcodeDatabaseDao();
            mBarcodeDatabase = BarcodeDatabaseDao.load(mBarcodeDatabaseId);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View RootView = inflater.inflate(R.layout.fragment_barcode_database, container, false);
        TextView databaseNameView = (TextView) RootView.findViewById(R.id.database_name);
        Button scanButton = RootView.findViewById(R.id.start_scan_button);
        scanButton.setOnClickListener(this);
        databaseNameView.setText(mBarcodeDatabase.getName());
        getActivity().setTitle(mBarcodeDatabase.getName());
        return RootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_scan_button:
                Fragment scanFragment = ScanFragment.newInstance(mBarcodeDatabase.getId());
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.flContent, scanFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
        };
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
