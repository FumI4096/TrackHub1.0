package com.example.trackhub;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ProfileDialogFragment extends DialogFragment {

    private Database db;
    private static final int SELECT_IMAGE = 2;
    private String getFile;
    private String studentId;
    private static ImageView imagePreview;
    private static Bitmap bitmap;



    public ProfileDialogFragment(String id){
        this.studentId = id;
    }

    public interface ProfileDialogListener {
        void onImageSelect(Bitmap bitmap);
    }

    private ProfileDialogListener listener;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Inflate the custom layout for the dialog
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_profile_dialog, null);

        getActivity().getWindow().setNavigationBarColor(getResources().getColor(android.R.color.black));
        getActivity().getWindow().setStatusBarColor(getResources().getColor(android.R.color.black));

        db = new Database(getContext());

        Button btnSubmitPost = view.findViewById(R.id.postButton);
        imagePreview = view.findViewById(R.id.imageDisplay);
        int studentIdInt = Integer.parseInt(studentId);
        

        imagePreview.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_IMAGE);
        });

        btnSubmitPost.setOnClickListener(v -> {
            db.insertImage(studentIdInt, getFile);
            if (listener != null && bitmap != null) {
                listener.onImageSelect(bitmap);
                Toast.makeText(getActivity(), "Profile Successfully Updated!", Toast.LENGTH_SHORT).show();
            }

            dismiss();
        });

        return new android.app.AlertDialog.Builder(getActivity())
                .setView(view)
                .setCancelable(true)
                .create();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SELECT_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null && data.getData() != null) {
                    try {
                        Uri selectedImageUri = data.getData();
                        bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImageUri);
                        //checkImage = checkChooseImageEmpty(String.valueOf(bitmap));
                        getFile = saveImageToInternalStorage(bitmap);
                        imagePreview.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), "Error loading image", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "No image selected", Toast.LENGTH_SHORT).show();
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(getActivity(), "Canceled", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof ProfileDialogListener) {
            listener = (ProfileDialogListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement ProfileDialogListener");
        }
    }

    private String saveImageToInternalStorage(Bitmap bitmap) {
        Context context = getContext();
        File directory = context.getFilesDir();
        String fileName = "image_" + System.currentTimeMillis() + ".png";
        File imageFile = new File(directory, fileName);
        try (FileOutputStream fos = new FileOutputStream(imageFile)) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            return imageFile.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}

