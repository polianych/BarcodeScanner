package com.example.poliakov.barcodescanner;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import android.Manifest;
import android.widget.Toast;

import java.io.IOException;


/**
 * A simple {@link Fragment} subclass.
 */
public class ScanFragment extends Fragment implements OnClickListener {


    public ScanFragment() {
        // Required empty public constructor
    }
    private static final String BARCODE_DATABASE_ID = "barcodeDatabaseId";
    private Long mBarcodeDatabaseId;
    private BarcodeDatabase mBarcodeDatabase;
    private static final String TAG = "MyActivity";
    private static final int PERMISSIONS_REQUEST_CAPTURE_IMAGE = 1;
    CameraSource cameraSource;
    TextView txtView;
    SurfaceView cameraView;
    Detector detector;
    MediaPlayer mediaPlayer;

    public static ScanFragment newInstance(Long barcodeDatabaseId) {
        ScanFragment fragment = new ScanFragment();
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
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Scan");
        final ScanFragment activity = this;

        View RootView = inflater.inflate(R.layout.fragment_scan, container, false);

        Button startBtn = (Button) RootView.findViewById(R.id.start_scan_button);
        Button stopBtn = (Button) RootView.findViewById(R.id.stop_scan_button);
        this.txtView = (TextView) RootView.findViewById(R.id.txtContent);
        TextView titleView = (TextView) RootView.findViewById(R.id.titleContent);
        this.cameraView = (SurfaceView) RootView.findViewById(R.id.camera_view);
        titleView.setText(mBarcodeDatabase.getName());
        this.mediaPlayer = new MediaPlayer();
//        this.mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//            public void onCompletion(MediaPlayer mp) {
//                activity.mediaPlayer.reset();
//            }
//        });


        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // User may have declined earlier, ask Android if we should show him a reason

            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CAMERA)) {
                // show an explanation to the user
                // Good practise: don't block thread after the user sees the explanation, try again to request the permission.
            } else {
                // request the permission.
                // CALLBACK_NUMBER is a integer constants
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, PERMISSIONS_REQUEST_CAPTURE_IMAGE);
                // The callback method gets the result of the request.
            }
        } else {
            //Init surface and wait user click SCAN button
            activity.initBarcodeScan();
        }
        startBtn.setOnClickListener(this);
        stopBtn.setOnClickListener(this);

        return RootView;
    }

    @Override
    public void onClick(View v) {
        final ScanFragment fragment = this;
        switch (v.getId()) {
            case R.id.start_scan_button:
                Toast.makeText(getActivity(), "Start scan", Toast.LENGTH_SHORT).show();
                fragment.startBarcodeScan();
                break;
            case R.id.stop_scan_button:
                Toast.makeText(getActivity(), "Stop scan", Toast.LENGTH_SHORT).show();
                fragment.stopBarcodeScan();
                break;
        };
    }

    public void initBarcodeScan() {
        final ScanFragment activity = this;

        this.detector =
                new BarcodeDetector.Builder(getActivity().getApplicationContext())
                        .setBarcodeFormats(Barcode.ALL_FORMATS)
                        .build();

        if(!this.detector.isOperational()){
            this.txtView.setText("Could not set up the detector!");
            return;
        }
        this.cameraSource = new CameraSource
                .Builder(getActivity().getApplicationContext(), this.detector)
                .setRequestedPreviewSize(640, 780)
                .setAutoFocusEnabled(true)
                .build();
        this.cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                Log.v(TAG, "Surface created!");
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                activity.stopBarcodeScan();
            }
        });
    }

    public void startBarcodeScan() {
        final ScanFragment activity = this;
        try {
            this.detector.setProcessor(new Detector.Processor<Barcode>() {
                @Override
                public void release() {
                }

                @Override
                public void receiveDetections(Detector.Detections<Barcode> detections) {
                    final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                    if (barcodes.size() != 0) {
                        activity.txtView.post(new Runnable() {    // Use the post method of the TextView
                            public void run() {
                                activity.stopBarcodeScan();
                                activity.processScannedValue(barcodes.valueAt(0).displayValue);
                                activity.txtView.setText(    // Update the TextView
                                        barcodes.valueAt(0).displayValue
                                );
                            }
                        });
                    }
                }
            });
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                this.cameraSource.start(this.cameraView.getHolder());
            }
        } catch (IOException ie) {
            Log.e("CAMERA SOURCE", ie.getMessage());
        }
    }

    public void stopBarcodeScan() {
        this.detector.release();
        this.cameraSource.stop();
    }

    public void processScannedValue(String value) {
//        if (value.equals("Hello World!")) {
//            try{
//                AssetFileDescriptor afd = getApplicationContext().getAssets().openFd("coin.mp3");
//                this.mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
//                afd.close();
//            } catch (IOException ioe) {}
//
//        } else {
//            try{
//                AssetFileDescriptor afd = getApplicationContext().getAssets().openFd("lost.mp3");
//                this.mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
//                afd.close();
//            } catch (IOException ioe) {}
//
//        }
//        try {
//            this.mediaPlayer.prepare();
//            this.mediaPlayer.start();
//        } catch (IOException ioe) {}
    }

    @Override
    public void onRequestPermissionsResult ( int requestCode, String[] permissions,
                                             int[] grantResults){
        switch (requestCode) {
            case PERMISSIONS_REQUEST_CAPTURE_IMAGE: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    this.initBarcodeScan();
                } else {
                    Log.d("", "permission denied");
                }
                return;
            }
        }
    }

}
