package com.example.trackhub;

import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.DialogFragment;

import org.jetbrains.annotations.Nullable;

public class ShowPostDialogFragment extends DialogFragment {
    Database db;
    LinearLayout postLyt;
    Button closeBtn;
    private String studentId, itemSymbol, commentId;
    private String fullName, date, image, comment;

    public ShowPostDialogFragment(String fullName, String date, String image, String comment, String itemSymbol, String studentId, String commentId){
        this.fullName = fullName;
        this.date = date;
        this.image = image;
        this.comment = comment;
        this.studentId = studentId;
        this.itemSymbol = itemSymbol;
        this.commentId = commentId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the dialog layout
        return inflater.inflate(R.layout.activity_show_post, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().getWindow().setNavigationBarColor(getResources().getColor(android.R.color.black));
        getActivity().getWindow().setStatusBarColor(getResources().getColor(android.R.color.black));

        db = new Database(getContext());
        postLyt = view.findViewById(R.id.postLayout);

        postLyt.removeAllViews();
        setPost();

        closeBtn = view.findViewById(R.id.closeButton);

        closeBtn.setOnClickListener(v -> {
            dismiss();
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        // Set the full-screen dialog theme
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialog);
        super.onCreate(savedInstanceState);
    }

    public void setPost(){
        Cursor postCursor;
        if(itemSymbol.equals("L")) {
            //postcommentid
            postCursor = db.getAllLostItemDetails(studentId, commentId);

            int dateIndex = postCursor.getColumnIndex(Database.columnLiDate);
            int messageIndex = postCursor.getColumnIndex(Database.columnLiDesc);
            int imageIndex = postCursor.getColumnIndex(Database.columnLiImage);
            int studentImageIndex = postCursor.getColumnIndex(Database.columnImage);
            int fullNameIndex = postCursor.getColumnIndex("fullName");

            if (postCursor != null && postCursor.moveToFirst()) {
                do {
                    if (dateIndex != -1 && messageIndex != -1 && imageIndex != -1 && studentImageIndex != -1) {
                        String date = postCursor.getString(dateIndex);
                        String description = postCursor.getString(messageIndex);
                        String image = postCursor.getString(imageIndex);
                        String studentImage = postCursor.getString(studentImageIndex);
                        String fullName = postCursor.getString(fullNameIndex);

                        enterPost(fullName, studentImage, image, date, description);

                    }
                }
                while(postCursor.moveToNext());
            }
        }
        else{
            postCursor = db.getAllFoundItemDetails(studentId, commentId);

            int dateIndex = postCursor.getColumnIndex(Database.columnFiDate);
            int messageIndex = postCursor.getColumnIndex(Database.columnFiDesc);
            int imageIndex = postCursor.getColumnIndex(Database.columnFiImage);
            int studentImageIndex = postCursor.getColumnIndex(Database.columnImage);
            int fullNameIndex = postCursor.getColumnIndex("fullName");

            if (postCursor != null && postCursor.moveToFirst()) {
                do {
                    if (dateIndex != -1 && messageIndex != -1 && imageIndex != -1 && studentImageIndex != -1) {
                        String date = postCursor.getString(dateIndex);
                        String description = postCursor.getString(messageIndex);
                        String image = postCursor.getString(imageIndex);
                        String studentImage = postCursor.getString(studentImageIndex);
                        String fullName = postCursor.getString(fullNameIndex);

                        enterPost(fullName, studentImage, image, date, description);

                    }
                }
                while(postCursor.moveToNext());
            }
        }

        enterComment(fullName, date, image, comment);

    }

    public void enterPost(String fullName, String studentImage, String postImage, String date, String description){
        //overall record holder
        LinearLayout recordLayout = new LinearLayout(getContext());
        recordLayout.setOrientation(LinearLayout.VERTICAL);
        recordLayout.setPadding(30, 20, 30, 20);

        //image, name, date
        LinearLayout profileLayout = new LinearLayout(getContext());
        profileLayout.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams profileLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        profileLayoutParams.setMargins(0, 0, 0, 30);
        profileLayout.setLayoutParams(profileLayoutParams);

        //name date
        LinearLayout nameDateLayout = new LinearLayout(getContext());
        nameDateLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams nameDateLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        nameDateLayout.setLayoutParams(nameDateLayoutParams);

        //profile image card
        CardView profileCardView = new CardView(getContext());
        CardView.LayoutParams profileCardLayoutParams = new CardView.LayoutParams(
                160,
                160
        );
        profileCardLayoutParams.setMargins(0, 0, 20, 0);
        profileCardView.setLayoutParams(profileCardLayoutParams);

        float radiusInDp = 80f;
        float radiusInPx = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                radiusInDp,
                getResources().getDisplayMetrics()
        );

        profileCardView.setRadius(radiusInPx);

        //item image card
        CardView imageCardView = new CardView(getContext());
        CardView.LayoutParams imageCardLayoutParams = new CardView.LayoutParams(
                CardView.LayoutParams.MATCH_PARENT,
                800
        );
        imageCardView.setLayoutParams(imageCardLayoutParams);


        ImageView profileView = new ImageView(getContext());
        if(studentImage != null){
            profileView.setImageURI(Uri.parse(studentImage));
        }
        else{
            profileView.setImageResource(R.drawable.profilehub);
        }

        profileView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        ));

        profileCardView.addView(profileView);

        profileLayout.addView(profileCardView);

        TextView fullNameView = new TextView(getContext());
        fullNameView.setText(fullName);
        fullNameView.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
        fullNameView.setTypeface(null, Typeface.BOLD);
        fullNameView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        nameDateLayout.addView(fullNameView);

        TextView dateView = new TextView(getContext());
        dateView.setText(date);
        dateView.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
        nameDateLayout.addView(dateView);

        profileLayout.addView(nameDateLayout);
        recordLayout.addView(profileLayout);

        TextView descriptionView = new TextView(getContext());
        descriptionView.setText(description);
        descriptionView.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
        recordLayout.addView(descriptionView);

        ImageView imageView = new ImageView(getContext());
        if (postImage != null && !postImage.isEmpty()) {
            imageView.setImageURI(Uri.parse(postImage));
        }
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(new CardView.LayoutParams(
                CardView.LayoutParams.MATCH_PARENT,
                CardView.LayoutParams.MATCH_PARENT
        ));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        imageCardView.addView(imageView);

        recordLayout.addView(imageCardView);


        postLyt.addView(recordLayout);
    }

    public void enterComment(String fullName, String date, String image, String comment){
        //overall record holder
        LinearLayout recordLayout = new LinearLayout(getContext());
        recordLayout.setOrientation(LinearLayout.VERTICAL);
        recordLayout.setPadding(20, 20, 20, 20);
        recordLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));

        //image, name, date
        LinearLayout profileLayout = new LinearLayout(getContext());
        profileLayout.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams profileLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        profileLayoutParams.setMargins(0, 0, 0, 30);
        profileLayout.setLayoutParams(profileLayoutParams);

        //name date
        LinearLayout nameDateLayout = new LinearLayout(getContext());
        nameDateLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams nameDateLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        nameDateLayout.setLayoutParams(nameDateLayoutParams);

        //profile image card
        CardView profileCardView = new CardView(getContext());
        CardView.LayoutParams profileCardLayoutParams = new CardView.LayoutParams(
                160,
                160
        );
        profileCardLayoutParams.setMargins(0, 0, 20, 0);
        profileCardView.setLayoutParams(profileCardLayoutParams);

        float radiusInDp = 80f;
        float radiusInPx = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                radiusInDp,
                getResources().getDisplayMetrics()
        );

        profileCardView.setRadius(radiusInPx);

        //item image card
        CardView imageCardView = new CardView(getContext());
        CardView.LayoutParams imageCardLayoutParams = new CardView.LayoutParams(
                CardView.LayoutParams.MATCH_PARENT,
                800
        );
        imageCardView.setLayoutParams(imageCardLayoutParams);


        ImageView profileView = new ImageView(getContext());
        if(image != null){
            profileView.setImageURI(Uri.parse(image));
        }
        else{
            profileView.setImageResource(R.drawable.profilehub);
        }

        profileView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        ));

        profileCardView.addView(profileView);

        profileLayout.addView(profileCardView);

        TextView fullNameView = new TextView(getContext());
        fullNameView.setText(fullName);
        fullNameView.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
        fullNameView.setTypeface(null, Typeface.BOLD);
        fullNameView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
        nameDateLayout.addView(fullNameView);

        TextView dateView = new TextView(getContext());
        dateView.setText(date);
        dateView.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
        nameDateLayout.addView(dateView);

        profileLayout.addView(nameDateLayout);
        recordLayout.addView(profileLayout);

        TextView commentView = new TextView(getContext());
        commentView.setText(comment);
        commentView.setTextColor(ContextCompat.getColor(getContext(), R.color.black));

        recordLayout.addView(commentView);


        postLyt.addView(recordLayout);

    }

}