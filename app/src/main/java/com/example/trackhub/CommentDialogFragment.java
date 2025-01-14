package com.example.trackhub;

import android.app.Dialog;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import org.jetbrains.annotations.Nullable;

public class CommentDialogFragment extends DialogFragment {
    Database db;
    Button closeBtn;
    LinearLayout commentLyt;
    ImageView sendCommentBtn;
    EditText inputCommentBtn;
    private String itemId, studentId, studentItemId;
    private char itemSymbol;

    public CommentDialogFragment(String studentItemId, String studentId, String itemId, char itemSymbol){
        this.studentItemId = studentItemId;
        this.studentId = studentId;
        this.itemId = itemId;
        this.itemSymbol = itemSymbol;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the dialog layout
        return inflater.inflate(R.layout.activity_comment_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = new Database(getContext());
        commentLyt = view.findViewById(R.id.commentLayout);
        sendCommentBtn = view.findViewById(R.id.sendComment);
        inputCommentBtn = view.findViewById(R.id.inputComment);

        commentLyt.removeAllViews();
        setComment();

        sendCommentBtn.setOnClickListener(v -> {
            String comment = inputCommentBtn.getText().toString();

            if(comment.isEmpty() || comment == null){
                Toast.makeText(getContext(), "Comment is empty", Toast.LENGTH_SHORT).show();
            }
            else{
                db.insertComment(studentId, itemId, inputCommentBtn.getText().toString(), String.valueOf(itemSymbol));
                int commentId = db.getCommentID(itemId, String.valueOf(itemSymbol));

                if(!(studentId.equals(studentItemId))){
                    db.insertNotificationComment(studentId, String.valueOf(db.getCommentID(itemId, String.valueOf(itemSymbol))), studentItemId, String.valueOf(itemSymbol));

                }

                commentLyt.removeAllViews();
                setComment();

                inputCommentBtn.setText("");
            }

        });

        Log.d("TestItemId", "The Id of posted item: " + itemId);


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

    public void setComment(){
        Cursor cursor = db.getComments(itemId, String.valueOf(itemSymbol));

        if (cursor != null && cursor.moveToFirst()) {
            int dateIndex = cursor.getColumnIndex(Database.columnCommentDate);
            int commentIndex = cursor.getColumnIndex(Database.columnComment);
            int studentImageIndex = cursor.getColumnIndex(Database.columnImage);
            int fullNameIndex = cursor.getColumnIndex("fullname");

            do {
                if (dateIndex != -1 && commentIndex != -1 && fullNameIndex != -1 && studentImageIndex != -1) {
                    String fullName = cursor.getString(fullNameIndex);
                    String comment = cursor.getString(commentIndex);
                    String image = cursor.getString(studentImageIndex);
                    String date = cursor.getString(dateIndex);

                    enterComment(fullName, date, comment, image);
                }
            } while (cursor.moveToNext());

            cursor.close();
        } else {
            Log.e("DatabaseError", "Cursor is null or empty.");
        }

    }

    public void enterComment(String fullName, String date, String comment, String image){

        //overall record holder
        LinearLayout recordLayout = new LinearLayout(getContext());
        recordLayout.setOrientation(LinearLayout.VERTICAL);
        recordLayout.setPadding(0, 20, 0, 20);
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
        fullNameView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        nameDateLayout.addView(fullNameView);

        TextView dateView = new TextView(getContext());
        dateView.setText(date);
        dateView.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
        nameDateLayout.addView(dateView);

        profileLayout.addView(nameDateLayout);

        recordLayout.addView(profileLayout);

        TextView descriptionView = new TextView(getContext());
        descriptionView.setText(comment);
        descriptionView.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
        recordLayout.addView(descriptionView);

        commentLyt.addView(recordLayout);

    }
}