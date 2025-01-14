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

public class NotificationDialogFragment extends DialogFragment {
    Database db;
    Button closeBtn;
    LinearLayout notificationLyt;
    private String studentId;

    public NotificationDialogFragment(String studentId){
        this.studentId = studentId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the dialog layout
        return inflater.inflate(R.layout.activity_notification_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().getWindow().setNavigationBarColor(getResources().getColor(android.R.color.black));
        getActivity().getWindow().setStatusBarColor(getResources().getColor(android.R.color.black));

        db = new Database(getContext());
        notificationLyt = view.findViewById(R.id.notificationLayout);

        notificationLyt.removeAllViews();
        setNotifications();

        if(notificationLyt.getChildCount() == 0){
            TextView showText = new TextView(getContext());
            showText.setText("No Notification!");
            showText.setTextSize(20);
            showText.setPadding(0, 50, 0, 50);
            showText.setTypeface(null, Typeface.BOLD);
            showText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            showText.setTextColor(getResources().getColor(R.color.black, null));

            notificationLyt.addView(showText);
        }

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

        public void setNotifications(){
            Cursor cursor = db.getNotificationComment("3220076");

            if (cursor != null && cursor.moveToFirst()) {
                Log.d("NotificationCount", "Number of notifications: " + cursor.getCount());
                int dateIndex = cursor.getColumnIndex(Database.columnCommentDate);
                int commentIdIndex = cursor.getColumnIndex(Database.columnPostCommentID);
                int studentCommentIndex = cursor.getColumnIndex(Database.columnComment);
                int studentImageIndex = cursor.getColumnIndex(Database.columnImage);
                int fullNameIndex = cursor.getColumnIndex("fullname");
                int symbolIndex = cursor.getColumnIndex(Database.columnCommentSymbol);
                int notifPostId = cursor.getColumnIndex(Database.columnNotifyPostID);
                int notifId = cursor.getColumnIndex(Database.columnNotifyID);

                do {
                    if (commentIdIndex != -1) {
                        String fullName = cursor.getString(fullNameIndex);
                        String image = cursor.getString(studentImageIndex);
                        String date = cursor.getString(dateIndex);
                        String symbol = cursor.getString(symbolIndex);
                        String studentComment = cursor.getString(studentCommentIndex);
                        String commentId = cursor.getString(commentIdIndex);

                        if (fullName != null && !fullName.isEmpty()) {
                            enterNotification(fullName, date, image, studentComment, symbol, commentId);
                        }
                    }
                } while (cursor.moveToNext());

                cursor.close();
            }
        else{
            TextView showText = new TextView(getContext());
            showText.setText("No Notification!");
            showText.setTextSize(20);
            showText.setPadding(0, 50, 0, 50);
            showText.setTypeface(null, Typeface.BOLD);
            showText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            showText.setTextColor(getResources().getColor(R.color.black, null));

            notificationLyt.addView(showText);
        }

    }

    public void enterNotification(String fullName, String date, String image, String comment, String symbol, String commentId){

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
        String statement = fullName + " commented in your post!";
        fullNameView.setText(statement);
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

        recordLayout.setOnClickListener(v -> {
            ShowPostDialogFragment spd = new ShowPostDialogFragment(fullName, date, image, comment, symbol, studentId, commentId);
            spd.show(requireActivity().getSupportFragmentManager(), "ShowPostDialog");
        });

        notificationLyt.addView(recordLayout);

    }
}