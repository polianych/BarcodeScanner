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
    public static final String BARCODE_DATABASE_FRAGMENT_TAG = "fragment:barcode_database";
    public static final String PASSCODE_VALUE = "1819482";


    // TODO: Rename and change types of parameters
    private Long mBarcodeDatabaseId;
    private BarcodeDatabase mBarcodeDatabase;
    private TextView databaseNameView;
    private TextView barcodesScansCountView;
    private TextView barcodesCountView;

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
        this.databaseNameView = (TextView) RootView.findViewById(R.id.database_name);
        this.barcodesScansCountView = (TextView) RootView.findViewById(R.id.barcodes_count);
        this.barcodesCountView = (TextView) RootView.findViewById(R.id.barcodes_scans_count);
        this.populateData();
        Button scanButton = RootView.findViewById(R.id.start_scan_button);
        Button clearScansButton = RootView.findViewById(R.id.clear_scans_button);
        scanButton.setOnClickListener(this);
        clearScansButton.setOnClickListener(this);
        return RootView;
    }
    private void populateData() {
        getActivity().setTitle(mBarcodeDatabase.getName());
        databaseNameView.setText(mBarcodeDatabase.getName());
        barcodesScansCountView.setText(mBarcodeDatabase.getBarcodesCount().toString());
        barcodesCountView.setText(mBarcodeDatabase.getBarcodesScansCount().toString());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_scan_button:
                Fragment scanFragment = ScanFragment.newInstance(mBarcodeDatabase.getId());
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.flContent, scanFragment, ScanFragment.SCAN_FRAGMENT_TAG);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
            case R.id.clear_scans_button:
                this.openClearScansDialo();
        };
    }

    public void openClearScansDialo() {
        ClearScansDialogFragment dialogFragment = new ClearScansDialogFragment();
        dialogFragment.show(getActivity().getSupportFragmentManager(), "clear_scans_dialog");
    }

    public void clearScansByPasscode(String passcode) {
        if (PASSCODE_VALUE.equals(passcode)){
            mBarcodeDatabase.deleteBarcodeScans();
            Toast.makeText(getActivity(), "Barcode Scans related to this DB deleted!", Toast.LENGTH_SHORT).show();
            this.populateData();
        } else {
            Toast.makeText(getActivity(), "Passcode is invalid", Toast.LENGTH_SHORT).show();
        }
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
