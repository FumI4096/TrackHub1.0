package com.example.trackhub;

import android.app.AlertDialog;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;



public class main_page extends AppCompatActivity {
    CardView profileBtn, notificationBtn;
    LinearLayout postLayout, botLayout;
    Button addLostPostBtn, addFoundPostBtn, getFoundItemsBtn, getLostItemsBtn, yourPostsBtn, trackBotBtn, testImageBtn;
    private String studentId;
    public boolean navFound, navLost, navYourPost;

    private ImageProcess ip = new ImageProcess();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        getWindow().setNavigationBarColor(getResources().getColor(android.R.color.black));
        getWindow().setStatusBarColor(getResources().getColor(android.R.color.black));

        navYourPost = true;
        navFound = false;
        navLost = false;

        getFoundItemsBtn = findViewById(R.id.foundItemsBtn);
        getLostItemsBtn = findViewById(R.id.lostItemsBtn);
        addLostPostBtn = findViewById(R.id.addLostPostButton);
        addFoundPostBtn = findViewById(R.id.addFoundPostButton);
        yourPostsBtn = findViewById(R.id.yourPostsButton);
        trackBotBtn = findViewById(R.id.trackBotButton);
        notificationBtn = findViewById(R.id.notificationButton);
        testImageBtn = findViewById(R.id.testImageProces);

        postLayout = findViewById(R.id.container);
        studentId = getIntent().getStringExtra("StudentID");

        postLayout.removeAllViews();


        getLostItems(studentId);
        getFoundItems(studentId);

        if(postLayout.getChildCount() == 0){
            emptyPost("Add your Posts!");
        }

        Log.d("EmptyPost", "Count: " + postLayout.getChildCount());


        yourPostsBtn.setOnClickListener(v -> {
            yourPostsBtn.setBackgroundResource(R.drawable.tertiary_square_button);
            getFoundItemsBtn.setBackgroundResource(R.drawable.secondary_square_button);
            getLostItemsBtn.setBackgroundResource(R.drawable.secondary_square_button);
            
            postLayout.removeAllViews();

            getLostItems(studentId);
            getFoundItems(studentId);

            if(postLayout.getChildCount() == 0){
                emptyPost("Add your Posts!");
            }

            navYourPost = true;
            navFound = false;
            navLost = false;

        });

        getLostItemsBtn.setOnClickListener(v -> {
            yourPostsBtn.setBackgroundResource(R.drawable.secondary_square_button);
            getFoundItemsBtn.setBackgroundResource(R.drawable.secondary_square_button);
            getLostItemsBtn.setBackgroundResource(R.drawable.tertiary_square_button);

            postLayout.removeAllViews();

            getLostItems();

            navYourPost = false;
            navFound = false;
            navLost = true;

        });

        getFoundItemsBtn.setOnClickListener(v -> {
            yourPostsBtn.setBackgroundResource(R.drawable.secondary_square_button);
            getFoundItemsBtn.setBackgroundResource(R.drawable.tertiary_square_button);
            getLostItemsBtn.setBackgroundResource(R.drawable.secondary_square_button);

            postLayout.removeAllViews();

            getFoundItems();

            navYourPost = false;
            navFound = true;
            navLost = false;
        });

        addFoundPostBtn.setOnClickListener(v -> {
            PostDialogFragment dialogFragment = new PostDialogFragment("Enter Found Item Details", studentId);
            dialogFragment.show(getSupportFragmentManager(), "PostDialog");
        });

        addLostPostBtn.setOnClickListener(v -> {
            PostDialogFragment dialogFragment = new PostDialogFragment("Enter Lost Item Details", studentId);
            dialogFragment.show(getSupportFragmentManager(), "PostDialog");
        });

        profileBtn = findViewById(R.id.profileButton);
        profileBtn.setOnClickListener(v -> {
            Intent intentProfilePage = new Intent(main_page.this, profile_page.class);
            intentProfilePage.putExtra("studentId", studentId);
            startActivity(intentProfilePage);
        });

        trackBotBtn.setOnClickListener(v -> {
            Intent trackBotPage = new Intent(main_page.this, TrackBot.class);
            trackBotPage.putExtra("studentId", studentId);
            startActivity(trackBotPage);
        });

        notificationBtn.setOnClickListener(v -> {
            NotificationDialogFragment ndf = new NotificationDialogFragment(studentId);
            ndf.show(getSupportFragmentManager(), "NotificationDialog");
        });

        testImageBtn.setOnClickListener(v -> {
            boolean result = ip.processImage(this, R.drawable.testimage);

            if(result){
                Toast.makeText(this, "Image has been tested", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(this, "Nope", Toast.LENGTH_SHORT).show();
            }
        });

    };

    public void getFoundItems(){
        Database db = new Database(this);
        Cursor cursor = db.getAllFoundItemDetails();

        int dateIndex = cursor.getColumnIndex(Database.columnFiDate);
        int messageIndex = cursor.getColumnIndex(Database.columnFiDesc);
        int imageIndex = cursor.getColumnIndex(Database.columnFiImage);
        int studentImageIndex = cursor.getColumnIndex(Database.columnImage);
        int itemIndex = cursor.getColumnIndex(Database.columnFiID);
        int studentItemIndex = cursor.getColumnIndex(Database.columnFiSdID);
        int fullNameIndex = cursor.getColumnIndex("fullName");

        if (cursor != null && cursor.moveToFirst()) {
            do {
                if (dateIndex != -1 && messageIndex != -1 && imageIndex != -1 && studentImageIndex != -1) {
                    String date = cursor.getString(dateIndex);
                    String description = cursor.getString(messageIndex);
                    String image = cursor.getString(imageIndex);
                    String studentImage = cursor.getString(studentImageIndex);
                    String fullName = cursor.getString(fullNameIndex);
                    String studentItemPost = cursor.getString(studentItemIndex);
                    String id = cursor.getString(itemIndex);

                    //overall record holder
                    LinearLayout recordLayout = new LinearLayout(this);
                    recordLayout.setOrientation(LinearLayout.VERTICAL);
                    recordLayout.setPadding(30, 20, 30, 20);

                    //image, name, date
                    LinearLayout profileLayout = new LinearLayout(this);
                    profileLayout.setOrientation(LinearLayout.HORIZONTAL);
                    LinearLayout.LayoutParams profileLayoutParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    profileLayoutParams.setMargins(0, 0, 0, 30);
                    profileLayout.setLayoutParams(profileLayoutParams);

                    //name date
                    LinearLayout nameDateLayout = new LinearLayout(this);
                    nameDateLayout.setOrientation(LinearLayout.VERTICAL);
                    LinearLayout.LayoutParams nameDateLayoutParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    nameDateLayout.setLayoutParams(nameDateLayoutParams);

                    //profile image card
                    CardView profileCardView = new CardView(this);
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
                    CardView imageCardView = new CardView(this);
                    CardView.LayoutParams imageCardLayoutParams = new CardView.LayoutParams(
                            CardView.LayoutParams.MATCH_PARENT,
                            800
                    );
                    imageCardView.setLayoutParams(imageCardLayoutParams);


                    ImageView profileView = new ImageView(this);
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

                    TextView fullNameView = new TextView(this);
                    fullNameView.setText(fullName);
                    fullNameView.setTextColor(ContextCompat.getColor(this, R.color.black));
                    fullNameView.setTypeface(null, Typeface.BOLD);
                    fullNameView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                    nameDateLayout.addView(fullNameView);

                    TextView dateView = new TextView(this);
                    dateView.setText(date);
                    dateView.setTextColor(ContextCompat.getColor(this, R.color.black));
                    nameDateLayout.addView(dateView);

                    profileLayout.addView(nameDateLayout);
                    recordLayout.addView(profileLayout);

                    TextView descriptionView = new TextView(this);
                    descriptionView.setText(description);
                    descriptionView.setTextColor(ContextCompat.getColor(this, R.color.black));
                    recordLayout.addView(descriptionView);

                    ImageView imageView = new ImageView(this);
                    if (image != null && !image.isEmpty()) {
                        imageView.setImageURI(Uri.parse(image));
                    }
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    imageView.setLayoutParams(new CardView.LayoutParams(
                            CardView.LayoutParams.MATCH_PARENT,
                            CardView.LayoutParams.MATCH_PARENT
                    ));
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

                    imageCardView.addView(imageView);

                    recordLayout.addView(imageCardView);

                    Button commentButton = new Button(this);
                    commentButton.setText("Comment");
                    commentButton.setTextColor(getResources().getColor(R.color.white, null));
                    commentButton.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    ));
                    commentButton.setBackground(ContextCompat.getDrawable(this, R.drawable.tertiary_square_button));

                    commentButton.setOnClickListener(v -> {
                        CommentDialogFragment codf = new CommentDialogFragment(studentItemPost, studentId, id, 'F');
                        codf.show(getSupportFragmentManager(), "CommentDialog");
                    });

                    recordLayout.addView(commentButton);

                    postLayout.addView(recordLayout);
                }


            } while (cursor.moveToNext());
            cursor.close();
        }
        else {
            emptyPost("Be the first to post a found item!");
        }
    }

    public void getLostItems(){
        Database db = new Database(this);
        Cursor cursor = db.getAllLostItemDetails();

        int dateIndex = cursor.getColumnIndex(Database.columnLiDate);
        int messageIndex = cursor.getColumnIndex(Database.columnLiDesc);
        int imageIndex = cursor.getColumnIndex(Database.columnLiImage);
        int studentImageIndex = cursor.getColumnIndex(Database.columnImage);
        int itemIndex = cursor.getColumnIndex(Database.columnLiID);
        int studentItemIndex = cursor.getColumnIndex(Database.columnLiSdID);
        int fullNameIndex = cursor.getColumnIndex("fullName");

        if (cursor != null && cursor.moveToFirst()) {
            do {
                if (dateIndex != -1 && messageIndex != -1 && imageIndex != -1 && studentImageIndex != -1) {
                    String date = cursor.getString(dateIndex);
                    String description = cursor.getString(messageIndex);
                    String image = cursor.getString(imageIndex);
                    String studentImage = cursor.getString(studentImageIndex);
                    String id = cursor.getString(itemIndex);
                    String studentItemPost = cursor.getString(studentItemIndex);
                    String fullName = cursor.getString(fullNameIndex);

                    //overall record holder
                    LinearLayout recordLayout = new LinearLayout(this);
                    recordLayout.setOrientation(LinearLayout.VERTICAL);
                    recordLayout.setPadding(30, 20, 30, 20);

                    //image, name, date
                    LinearLayout profileLayout = new LinearLayout(this);
                    profileLayout.setOrientation(LinearLayout.HORIZONTAL);
                    LinearLayout.LayoutParams profileLayoutParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    profileLayoutParams.setMargins(0, 0, 0, 30);
                    profileLayout.setLayoutParams(profileLayoutParams);

                    //name date
                    LinearLayout nameDateLayout = new LinearLayout(this);
                    nameDateLayout.setOrientation(LinearLayout.VERTICAL);
                    LinearLayout.LayoutParams nameDateLayoutParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    nameDateLayout.setLayoutParams(nameDateLayoutParams);

                    //profile image card
                    CardView profileCardView = new CardView(this);
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
                    CardView imageCardView = new CardView(this);
                    CardView.LayoutParams imageCardLayoutParams = new CardView.LayoutParams(
                            CardView.LayoutParams.MATCH_PARENT,
                            800
                    );
                    imageCardView.setLayoutParams(imageCardLayoutParams);


                    ImageView profileView = new ImageView(this);
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

                    TextView fullNameView = new TextView(this);
                    fullNameView.setText(fullName);
                    fullNameView.setTextColor(ContextCompat.getColor(this, R.color.black));
                    fullNameView.setTypeface(null, Typeface.BOLD);
                    fullNameView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                    nameDateLayout.addView(fullNameView);

                    TextView dateView = new TextView(this);
                    dateView.setText(date);
                    dateView.setTextColor(ContextCompat.getColor(this, R.color.black));
                    nameDateLayout.addView(dateView);

                    profileLayout.addView(nameDateLayout);

                    recordLayout.addView(profileLayout);

                    TextView descriptionView = new TextView(this);
                    descriptionView.setText(description);
                    descriptionView.setTextColor(ContextCompat.getColor(this, R.color.black));
                    recordLayout.addView(descriptionView);

                    ImageView imageView = new ImageView(this);
                    if (image != null && !image.isEmpty()) {
                        imageView.setImageURI(Uri.parse(image));
                    }
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    imageView.setLayoutParams(new CardView.LayoutParams(
                            CardView.LayoutParams.MATCH_PARENT,
                            CardView.LayoutParams.MATCH_PARENT
                    ));
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

                    imageCardView.addView(imageView);

                    recordLayout.addView(imageCardView);

                    Button commentButton = new Button(this);
                    commentButton.setText("Comment");
                    commentButton.setTextColor(getResources().getColor(R.color.white, null));
                    commentButton.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    ));
                    commentButton.setBackground(ContextCompat.getDrawable(this, R.drawable.tertiary_square_button));

                    commentButton.setOnClickListener(v -> {
                        CommentDialogFragment codf = new CommentDialogFragment(studentItemPost, studentId, id, 'L');
                        codf.show(getSupportFragmentManager(), "CommentDialog");
                    });

                    recordLayout.addView(commentButton);

                    postLayout.addView(recordLayout);
                }


            } while (cursor.moveToNext());
            cursor.close();
        }
        else {
            emptyPost("Be the first to post a lost item!");
        }
    }

    public void getFoundItems(String id){
        Database db = new Database(this);
        Cursor cursor = db.getAllFoundItemDetails(id);

        int dateIndex = cursor.getColumnIndex(Database.columnFiDate);
        int messageIndex = cursor.getColumnIndex(Database.columnFiDesc);
        int imageIndex = cursor.getColumnIndex(Database.columnFiImage);
        int studentImageIndex = cursor.getColumnIndex(Database.columnImage);
        int idIndex = cursor.getColumnIndex(Database.columnFiID);
        int studentItemIndex = cursor.getColumnIndex(Database.columnFiSdID);
        int fullNameIndex = cursor.getColumnIndex("fullName");

        if (cursor != null && cursor.moveToFirst()) {
            do {
                if (dateIndex != -1 && messageIndex != -1 && imageIndex != -1 && studentImageIndex != -1) {
                    String date = cursor.getString(dateIndex);
                    String description = cursor.getString(messageIndex);
                    String image = cursor.getString(imageIndex);
                    String studentImage = cursor.getString(studentImageIndex);
                    String itemId = cursor.getString(idIndex);
                    String studentItemPost = cursor.getString(studentItemIndex);
                    String fullName = cursor.getString(fullNameIndex);

                    //overall record holder
                    LinearLayout recordLayout = new LinearLayout(this);
                    recordLayout.setOrientation(LinearLayout.VERTICAL);
                    recordLayout.setPadding(30, 20, 30, 20);

                    //image, name, date
                    LinearLayout profileLayout = new LinearLayout(this);
                    profileLayout.setOrientation(LinearLayout.HORIZONTAL);
                    LinearLayout.LayoutParams profileLayoutParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    profileLayoutParams.setMargins(0, 0, 0, 30);
                    profileLayout.setLayoutParams(profileLayoutParams);

                    //name date
                    LinearLayout nameDateLayout = new LinearLayout(this);
                    nameDateLayout.setOrientation(LinearLayout.VERTICAL);
                    LinearLayout.LayoutParams nameDateLayoutParams = new LinearLayout.LayoutParams(
                            0,
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            1.0f
                    );
                    nameDateLayout.setLayoutParams(nameDateLayoutParams);

                    //profile image card
                    CardView profileCardView = new CardView(this);
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
                    CardView imageCardView = new CardView(this);
                    CardView.LayoutParams imageCardLayoutParams = new CardView.LayoutParams(
                            CardView.LayoutParams.MATCH_PARENT,
                            800
                    );
                    imageCardView.setLayoutParams(imageCardLayoutParams);


                    ImageView profileView = new ImageView(this);
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

                    TextView fullNameView = new TextView(this);
                    fullNameView.setText(fullName);
                    fullNameView.setTextColor(ContextCompat.getColor(this, R.color.black));
                    fullNameView.setTypeface(null, Typeface.BOLD);
                    fullNameView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                    nameDateLayout.addView(fullNameView);

                    TextView dateView = new TextView(this);
                    dateView.setText(date);
                    dateView.setTextColor(ContextCompat.getColor(this, R.color.black));
                    nameDateLayout.addView(dateView);

                    profileLayout.addView(nameDateLayout);

                    ImageView deleteCurrentPostButton = new ImageView(this);
                    deleteCurrentPostButton.setImageResource(R.drawable.deleteicon);
                    deleteCurrentPostButton.setLayoutParams(new LinearLayout.LayoutParams(
                            60,
                            60
                    ));
                    profileLayout.addView(deleteCurrentPostButton);

                    deleteCurrentPostButton.setOnClickListener(v -> {
                        ConfirmationDialogFragment cdf = new ConfirmationDialogFragment();
                        cdf.setConfirmationDialogListener(confirmed -> {
                            if(confirmed){
                                db.deleteComments(itemId, "F");
                                db.deleteNotificationComments(itemId, "F");
                                db.deleteFoundItems(itemId);
                                postLayout.removeView(recordLayout);
                                Toast.makeText(this, "Post Deleted Successfully", Toast.LENGTH_SHORT).show();

                                if(postLayout.getChildCount() == 0){
                                    emptyPost("Add your Posts!");
                                }
                            }
                        });

                        cdf.show(getSupportFragmentManager(), "ConfirmationDialog");
                    });

                    recordLayout.addView(profileLayout);

                    TextView descriptionView = new TextView(this);
                    descriptionView.setText(description);
                    descriptionView.setTextColor(ContextCompat.getColor(this, R.color.black));
                    recordLayout.addView(descriptionView);

                    ImageView imageView = new ImageView(this);
                    if (image != null && !image.isEmpty()) {
                        imageView.setImageURI(Uri.parse(image));
                    }
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    imageView.setLayoutParams(new CardView.LayoutParams(
                            CardView.LayoutParams.MATCH_PARENT,
                            CardView.LayoutParams.MATCH_PARENT
                    ));
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

                    imageCardView.addView(imageView);

                    recordLayout.addView(imageCardView);

                    Button commentButton = new Button(this);
                    commentButton.setText("Comment");
                    commentButton.setTextColor(getResources().getColor(R.color.white, null));
                    commentButton.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    ));
                    commentButton.setBackground(ContextCompat.getDrawable(this, R.drawable.tertiary_square_button));

                    commentButton.setOnClickListener(v -> {
                        CommentDialogFragment codf = new CommentDialogFragment(studentItemPost, studentId, itemId, 'F');
                        codf.show(getSupportFragmentManager(), "CommentDialog");
                    });

                    recordLayout.addView(commentButton);


                    postLayout.addView(recordLayout);
                }

            } while (cursor.moveToNext());
            cursor.close();
        }
    }

    public void getLostItems(String id){
        Database db = new Database(this);
        Cursor cursor = db.getAllLostItemDetails(id);

        int dateIndex = cursor.getColumnIndex(Database.columnLiDate);
        int messageIndex = cursor.getColumnIndex(Database.columnLiDesc);
        int imageIndex = cursor.getColumnIndex(Database.columnLiImage);
        int idIndex = cursor.getColumnIndex(Database.columnLiID);
        int studentImageIndex = cursor.getColumnIndex(Database.columnImage);
        int studentItemIndex = cursor.getColumnIndex(Database.columnLiSdID);
        int fullNameIndex = cursor.getColumnIndex("fullName");

        if (cursor != null && cursor.moveToFirst()) {
            do {
                if (dateIndex != -1 && messageIndex != -1 && imageIndex != -1 && studentImageIndex != -1) {
                    String date = cursor.getString(dateIndex);
                    String description = cursor.getString(messageIndex);
                    String image = cursor.getString(imageIndex);
                    String studentImage = cursor.getString(studentImageIndex);
                    String itemId = cursor.getString(idIndex);
                    String studentItemPost = cursor.getString(studentItemIndex);
                    String fullName = cursor.getString(fullNameIndex);

                    //overall record holder
                    LinearLayout recordLayout = new LinearLayout(this);
                    recordLayout.setOrientation(LinearLayout.VERTICAL);
                    recordLayout.setPadding(30, 20, 30, 20);

                    //image, name, date
                    LinearLayout profileLayout = new LinearLayout(this);
                    profileLayout.setOrientation(LinearLayout.HORIZONTAL);
                    LinearLayout.LayoutParams profileLayoutParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    profileLayoutParams.setMargins(0, 0, 0, 30);
                    profileLayout.setLayoutParams(profileLayoutParams);

                    //name date
                    LinearLayout nameDateLayout = new LinearLayout(this);
                    nameDateLayout.setOrientation(LinearLayout.VERTICAL);
                    LinearLayout.LayoutParams nameDateLayoutParams = new LinearLayout.LayoutParams(
                            0,
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            1.0f
                    );
                    nameDateLayout.setLayoutParams(nameDateLayoutParams);

                    //profile image card
                    CardView profileCardView = new CardView(this);
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
                    CardView imageCardView = new CardView(this);
                    CardView.LayoutParams imageCardLayoutParams = new CardView.LayoutParams(
                            CardView.LayoutParams.MATCH_PARENT,
                            800
                    );
                    imageCardView.setLayoutParams(imageCardLayoutParams);


                    ImageView profileView = new ImageView(this);
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

                    TextView fullNameView = new TextView(this);
                    fullNameView.setText(fullName);
                    fullNameView.setTextColor(ContextCompat.getColor(this, R.color.black));
                    fullNameView.setTypeface(null, Typeface.BOLD);
                    fullNameView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                    nameDateLayout.addView(fullNameView);

                    TextView dateView = new TextView(this);
                    dateView.setText(date);
                    dateView.setTextColor(ContextCompat.getColor(this, R.color.black));
                    nameDateLayout.addView(dateView);

                    profileLayout.addView(nameDateLayout);

                    ImageView deleteCurrentPostButton = new ImageView(this);
                    deleteCurrentPostButton.setImageResource(R.drawable.deleteicon);
                    deleteCurrentPostButton.setLayoutParams(new LinearLayout.LayoutParams(
                            60,
                            60
                    ));
                    profileLayout.addView(deleteCurrentPostButton);

                    deleteCurrentPostButton.setOnClickListener(v -> {
                        ConfirmationDialogFragment cdf = new ConfirmationDialogFragment();

                        cdf.setConfirmationDialogListener(confirmed -> {
                            if(confirmed){
                                db.deleteComments(itemId, "L");
                                db.deleteNotificationComments(itemId, "L");
                                db.deleteLostItems(itemId);
                                postLayout.removeView(recordLayout);
                                Toast.makeText(this, "Post Deleted Successfully", Toast.LENGTH_SHORT).show();

                                if(postLayout.getChildCount() == 0){
                                    emptyPost("Add your Posts!");
                                }
                            }
                        });

                        cdf.show(getSupportFragmentManager(), "ConfirmationDialog");

                    });

                    recordLayout.addView(profileLayout);

                    TextView descriptionView = new TextView(this);
                    descriptionView.setText(description);
                    descriptionView.setTextColor(ContextCompat.getColor(this, R.color.black));
                    recordLayout.addView(descriptionView);

                    ImageView imageView = new ImageView(this);
                    if (image != null && !image.isEmpty()) {
                        imageView.setImageURI(Uri.parse(image));
                    }
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    imageView.setLayoutParams(new CardView.LayoutParams(
                            CardView.LayoutParams.MATCH_PARENT,
                            CardView.LayoutParams.MATCH_PARENT
                    ));
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

                    imageCardView.addView(imageView);

                    recordLayout.addView(imageCardView);

                    Button commentButton = new Button(this);
                    commentButton.setText("Comment");
                    commentButton.setTextColor(getResources().getColor(R.color.white, null));
                    commentButton.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    ));
                    commentButton.setBackground(ContextCompat.getDrawable(this, R.drawable.tertiary_square_button));

                    commentButton.setOnClickListener(v -> {
                        CommentDialogFragment codf = new CommentDialogFragment(studentItemPost, studentId, itemId, 'L');
                        codf.show(getSupportFragmentManager(), "CommentDialog");
                    });


                    recordLayout.addView(commentButton);

                    postLayout.addView(recordLayout);
                }

            } while (cursor.moveToNext());
            cursor.close();
        }
    }

    public void emptyPost(String text){
        TextView showText = new TextView(this);
        showText.setText(text);
        showText.setTextSize(20);
        showText.setPadding(0, 50, 0, 50);
        showText.setTypeface(null, Typeface.BOLD);
        showText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        showText.setTextColor(getResources().getColor(R.color.black, null));

        postLayout.addView(showText);

    }

    public LinearLayout getPostLayout(){
        return postLayout;
    }

}
