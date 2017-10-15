package com.example.poliakov.barcodescanner;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.hardware.Camera;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


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
    private boolean flashmode = false;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mBarcodeDatabaseId = getArguments().getLong(BARCODE_DATABASE_ID);
            DaoSession daoSession = ((App) getActivity().getApplication()).getDaoSession();
            BarcodeDatabaseDao BarcodeDatabaseDao = daoSession.getBarcodeDatabaseDao();
            mBarcodeDatabase = BarcodeDatabaseDao.load(mBarcodeDatabaseId);
//            if (mBarcodeDatabase == null) {
//                Toast.makeText(getActivity(), "Please, select DB", Toast.LENGTH_SHORT).show();
//                Fragment selectFragment = new SelectFragment();
//                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.flContent, selectFragment);
//                fragmentTransaction.commit();
//            }
        }
        getActivity().setTitle(mBarcodeDatabase.getName());
        final ScanFragment activity = this;

        View RootView = inflater.inflate(R.layout.fragment_scan, container, false);

        Button startBtn = (Button) RootView.findViewById(R.id.start_scan_button);
        Button stopBtn = (Button) RootView.findViewById(R.id.stop_scan_button);
        Button flashBtn = (Button) RootView.findViewById(R.id.set_flash);
        this.txtView = (TextView) RootView.findViewById(R.id.txtContent);
        this.cameraView = (SurfaceView) RootView.findViewById(R.id.camera_view);
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
        flashBtn.setOnClickListener(this);

        return RootView;
    }

    @Override
    public void onClick(View v) {
        final ScanFragment fragment = this;
        switch (v.getId()) {
            case R.id.start_scan_button:
                fragment.startBarcodeScan();
                break;
            case R.id.stop_scan_button:
                fragment.stopBarcodeScan();
                break;
            case R.id.set_flash:
                fragment.flashOnButton();
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
                .setRequestedFps(15.0f)
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
        this.txtView.setText("");
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
                                activity.onDetectBarcode(barcodes.valueAt(0).displayValue);
                            }
                        });
                    }
                }
            });
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                this.cameraSource.start(this.cameraView.getHolder());
                this.initCameraFocusListener();
            }
        } catch (IOException ie) {
            Log.e("CAMERA SOURCE", ie.getMessage());
        }
    }

    private void initCameraFocusListener() {
        cameraView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                cameraFocus(event, cameraSource, Camera.Parameters.FOCUS_MODE_AUTO);
                return false;
            }
        });
    }

    private boolean cameraFocus(MotionEvent event, @NonNull CameraSource cameraSource, @NonNull String focusMode) {
        Field[] declaredFields = CameraSource.class.getDeclaredFields();

        int pointerId = event.getPointerId(0);
        int pointerIndex = event.findPointerIndex(pointerId);
        // Get the pointer's current position
        float x = event.getX(pointerIndex);
        float y = event.getY(pointerIndex);

        float touchMajor = event.getTouchMajor();
        float touchMinor = event.getTouchMinor();

        Rect touchRect = new Rect((int)(x - touchMajor / 2), (int)(y - touchMinor / 2), (int)(x + touchMajor / 2), (int)(y + touchMinor / 2));
        Rect focusArea = new Rect();

        focusArea.set(touchRect.left * 2000 / cameraView.getWidth() - 1000,
                touchRect.top * 2000 / cameraView.getHeight() - 1000,
                touchRect.right * 2000 / cameraView.getWidth() - 1000,
                touchRect.bottom * 2000 / cameraView.getHeight() - 1000);

        // Submit focus area to camera

        ArrayList<Camera.Area> focusAreas = new ArrayList<Camera.Area>();
        focusAreas.add(new Camera.Area(focusArea, 1000));

        Camera camera = getCamera(this.cameraSource);
        if (camera != null) {
            Camera.Parameters params = camera.getParameters();
            params.setFocusMode(focusMode);
            params.setFocusAreas(focusAreas);
            camera.setParameters(params);
            // Start the autofocus operation
            camera.autoFocus(new Camera.AutoFocusCallback() {
                @Override
                public void onAutoFocus(boolean b, Camera camera) {
                    // currently set to auto-focus on single touch
                }
            });
            return true;
        } else {
            return false;
        }
    }

    private void flashOnButton() {
        Camera camera = getCamera(this.cameraSource);
        if (camera != null) {
            try {
                Camera.Parameters param = camera.getParameters();
                param.setFlashMode(!flashmode?Camera.Parameters.FLASH_MODE_TORCH :Camera.Parameters.FLASH_MODE_OFF);
                camera.setParameters(param);
                flashmode = !flashmode;
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
    private static Camera getCamera(@NonNull CameraSource cameraSource) {
        Field[] declaredFields = CameraSource.class.getDeclaredFields();

        for (Field field : declaredFields) {
            if (field.getType() == Camera.class) {
                field.setAccessible(true);
                try {
                    Camera camera = (Camera) field.get(cameraSource);
                    if (camera != null) {
                        return camera;
                    }
                    return null;
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
        return null;
    }

    private void onDetectBarcode(String value) {
        DaoSession daoSession = ((App) getActivity().getApplication()).getDaoSession();
        BarcodeDao barcodeDao = daoSession.getBarcodeDao();
        BarcodeScanDao barcodeScanDao = daoSession.getBarcodeScanDao();

        BarcodeScan barcodeScan = new BarcodeScan();
        barcodeScan.setDate(new Date());
        barcodeScan.setBarcodeDatabaseId(mBarcodeDatabase.getId());
        barcodeScan.setCode(value);

        if (barcodeDao.queryBuilder()
                .where(BarcodeDao.Properties.Code.eq(value))
                .where(BarcodeDao.Properties.BarcodeDatabaseId.eq(mBarcodeDatabase.getId())).count() == 1) {
            Long validBarcodesCount = barcodeScanDao.queryBuilder()
                    .where(BarcodeScanDao.Properties.BarcodeDatabaseId.eq(mBarcodeDatabase.getId()))
                    .where(BarcodeScanDao.Properties.Code.eq(value))
                    .where(BarcodeScanDao.Properties.Type.eq(BarcodeScan.VALID_TYPE))
                    .count();
            if (validBarcodesCount == 0) {
                barcodeScan.setType(BarcodeScan.VALID_TYPE);
            } else {
                barcodeScan.setType(BarcodeScan.PRESENT_TYPE);
            }
        } else {
            barcodeScan.setType(BarcodeScan.NOT_FOUND_TYPE);
        }
        barcodeScanDao.insert(barcodeScan);
        processScannedValue(barcodeScan);
    }

    public void stopBarcodeScan() {
        this.detector.release();
        this.cameraSource.stop();
    }

    public void processScannedValue(BarcodeScan barcodeScan) {
        switch (barcodeScan.getType()) {
            case BarcodeScan.VALID_TYPE:
                this.txtView.setText("Success! " + barcodeScan.getCode() + " valid");
                break;
            case BarcodeScan.NOT_FOUND_TYPE:
                this.txtView.setText("Error! " + barcodeScan.getCode() + " not found");
                break;
            case BarcodeScan.PRESENT_TYPE:
                this.txtView.setText("Error! " + barcodeScan.getCode() + " already scanned");
                break;
        }
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
