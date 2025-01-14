package com.example.trackhub;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import org.jetbrains.annotations.Nullable;

public class ConfirmationDialogFragment extends DialogFragment {
    Button yesBtn, noBtn;
    boolean confirmation = false;

    public interface ConfirmationDialogListener {
        void onConfirmation(boolean confirmed);
    }

    private ConfirmationDialogListener listener;

    public void setConfirmationDialogListener(ConfirmationDialogListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Inflate the custom layout for the dialog
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_confirmation_dialog, null);

        yesBtn = view.findViewById(R.id.yesButtonModal);
        noBtn = view.findViewById(R.id.noButton);

        yesBtn.setOnClickListener(v -> {
            listener.onConfirmation(true);
            dismiss();
        });

        noBtn.setOnClickListener(v -> {
            listener.onConfirmation(false);
            dismiss();
        });

        return new android.app.AlertDialog.Builder(getActivity())
                .setView(view)
                .setCancelable(true)
                .create();
    }


}