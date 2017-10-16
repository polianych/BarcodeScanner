package com.example.poliakov.barcodescanner;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

/**
 * Created by poliakov on 10/16/17.
 */

public class ManualEnterDialogFragment extends DialogFragment {

    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public interface ManualEnterDialogListener {
        public void onDialogPositiveClick(String code);
        public void onDialogNegativeClick(ManualEnterDialogFragment dialog);
    }

    // Use this instance of the interface to deliver action events
    ManualEnterDialogListener mListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Build the dialog and set up the button click handlers
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        final View RootView = inflater.inflate(R.layout.manual_enter_dialog, null);
        builder.setView(RootView)
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Send the positive button event back to the host activity
                        EditText codeEditText = (EditText) RootView.findViewById(R.id.barcode);
                        String code = codeEditText.getText().toString();
                        mListener.onDialogPositiveClick(code);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Send the negative button event back to the host activity
                        mListener.onDialogNegativeClick(ManualEnterDialogFragment.this);
                    }
                });
        return builder.create();
    }


    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host context implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (ManualEnterDialogListener) context;
        } catch (ClassCastException e) {
            // The context doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
                    + " must implement NoticeDialogListener");
        }
    }
}