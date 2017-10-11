package com.example.poliakov.barcodescanner;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ImportFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ImportFragment extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Uri importFileUri;
    private EditText nameEditText;
    private Button importCsvFileBtn;


    public ImportFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ImportFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ImportFragment newInstance(String param1, String param2) {
        ImportFragment fragment = new ImportFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.add_csv_file) {
            Log.i("","clicked add scv file"+"\n");
            MainActivity activity = (MainActivity) getActivity();
            activity.readCsvFile();
        } else if (view.getId() == R.id.import_csv_file) {
            this.importCsvFile();
        }
    }

    public void importCsvFile() {
        if(this.importFileUri instanceof Uri == false){
            Toast.makeText(getActivity(), "Please select file to import", Toast.LENGTH_SHORT).show();
            return;
        }
        String name = this.nameEditText.getText().toString();
        if(name.length() == 0) {
            Toast.makeText(getActivity(), "Please type name of BarcodeDatabase", Toast.LENGTH_SHORT).show();
        }
        this.importCsvFileBtn.setEnabled(false);

        InputStream inputStream = null;
        ArrayList<String> codes = new ArrayList<String>();
        try {
            inputStream = getActivity().getContentResolver().openInputStream(this.importFileUri);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            Log.i("","open text file - content"+"\n");
            while ((line = reader.readLine()) != null) {
                codes.add(line); //this adds an element to the list.
            }
            reader.close();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Set<String> codesSet = new HashSet<String>(codes);

        if (codesSet.toArray().length != codes.toArray().length) {
            Toast.makeText(getActivity(), "Imported file should have unique codes", Toast.LENGTH_SHORT).show();
            this.importCsvFileBtn.setEnabled(true);
            return;
        } else {
            Toast.makeText(getActivity(), "Import is started", Toast.LENGTH_SHORT).show();
        }

        DaoSession daoSession = ((App) getActivity().getApplication()).getDaoSession();
        BarcodeDatabaseDao BarcodeDatabaseDao = daoSession.getBarcodeDatabaseDao();
        BarcodeDatabase BarcodeDatabase = new BarcodeDatabase();
        BarcodeDatabase.setName(name);
        BarcodeDatabase.setDate(new Date());
        BarcodeDatabaseDao.insert(BarcodeDatabase);
        Log.d("DaoExample", "Inserted new BarcodeDatabase, ID: " + BarcodeDatabase.getId());
        ArrayList<Barcode> barcodes = new ArrayList<Barcode>();
        Barcode barcode;
        for (String code: codes) {
            barcode = new Barcode();
            barcode.setBarcodeDatabase(BarcodeDatabase);
            barcode.setCode(code);
            barcodes.add(barcode);
        }
        BarcodeDao BarcodeDao = daoSession.getBarcodeDao();
        BarcodeDao.insertInTx(barcodes);

        Toast.makeText(getActivity(), "Imported: " + BarcodeDatabase.getName() + ". Total codes count: " + codes.toArray().length, Toast.LENGTH_SHORT).show();

        Fragment selectFragment = new SelectFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.flContent, selectFragment);
        fragmentTransaction.commit();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.i("","import fragment created"+"\n");
        View RootView = inflater.inflate(R.layout.fragment_import, container, false);
        Button addCsvFileBtn = (Button) RootView.findViewById(R.id.add_csv_file);
        this.importCsvFileBtn = (Button) RootView.findViewById(R.id.import_csv_file);
        this.nameEditText = (EditText) RootView.findViewById(R.id.import_name_edit_text);
        addCsvFileBtn.setOnClickListener(this);
        this.importCsvFileBtn.setOnClickListener(this);
        return RootView;
    }

    public void setImportFileUri(Uri uri){
        this.importFileUri = uri;
    }

}
