package com.example.trackhub;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class PostDialogFragment extends DialogFragment {

    private Database db;
    private static final int SELECT_IMAGE = 2;
    private static boolean checkImage;
    private static TextView infoTitleView;
    private String getInfoTitle;
    private String getFile;
    private String studentId;
    private static ImageView imagePreview;

    public PostDialogFragment(String infoTitle, String id){
        this.getInfoTitle = infoTitle;
        this.studentId = id;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Inflate the custom layout for the dialog
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_post_dialog, null);

        getActivity().getWindow().setNavigationBarColor(getResources().getColor(android.R.color.black));
        getActivity().getWindow().setStatusBarColor(getResources().getColor(android.R.color.black));


        infoTitleView = view.findViewById(R.id.infoTitleHeader);
        infoTitleView.setText(getInfoTitle);
        db = new Database(getContext());

        main_page mp = (main_page) getActivity();

        EditText editTextPost = view.findViewById(R.id.editDescription);
        Button btnChooseImage = view.findViewById(R.id.chooseImageButton);
        Button btnSubmitPost = view.findViewById(R.id.postButton);
        imagePreview = view.findViewById(R.id.imageDisplay);

        btnChooseImage.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_IMAGE);
        });


        imagePreview.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_IMAGE);
        });

        btnSubmitPost.setOnClickListener(v -> {
            String postText = editTextPost.getText().toString();
            Drawable postImage = imagePreview.getDrawable();
            if(postText.isEmpty() && !(checkImageChange(postImage))){
                Toast.makeText(getActivity(), "Please enter text and image for your post.", Toast.LENGTH_SHORT).show();
            }
            else if(postText.isEmpty()){
                Toast.makeText(getActivity(), "Please enter text for your post.", Toast.LENGTH_SHORT).show();
            }
            else if(!(checkImageChange(postImage))){
                Toast.makeText(getActivity(), "Please enter image for your post.", Toast.LENGTH_SHORT).show();
            }
            else{
                if(infoTitleView.getText().toString().equals("Enter Found Item Details")){
                    db.insertFoundItemDetail(studentId, postText, getFile);
                    Toast.makeText(getActivity(), "Found Item Posted Successfully!", Toast.LENGTH_SHORT).show();

                    if(mp.navFound){
                        mp.getPostLayout().removeAllViews();
                        mp.getFoundItems();
                    }
                    else if(mp.navYourPost){
                        mp.getPostLayout().removeAllViews();
                        mp.getLostItems(studentId);
                        mp.getFoundItems(studentId);
                    }
                }
                else{
                    db.insertLostItemDetail(studentId, postText, getFile);
                    Toast.makeText(getActivity(), "Lost Item Posted Successfully!", Toast.LENGTH_SHORT).show();

                    if(mp.navLost){
                        mp.getPostLayout().removeAllViews();
                        mp.getLostItems();
                    }
                    else if(mp.navYourPost){
                        mp.getPostLayout().removeAllViews();
                        mp.getLostItems(studentId);
                        mp.getFoundItems(studentId);
                    }
                }
                dismiss();
            }
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
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImageUri);
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


    public boolean checkImageChange(Drawable image){
        Drawable targetDrawable = ContextCompat.getDrawable(getActivity(), R.drawable.noimage);
        if(image.getConstantState() != targetDrawable.getConstantState()){
            return true;
        }
        else{
            return false;
        }
    }
}

