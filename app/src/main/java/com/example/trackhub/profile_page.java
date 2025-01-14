package com.example.trackhub;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.util.Log;
import android.widget.ImageView;

import android.widget.Button;
import android.widget.TextView;

public class profile_page extends AppCompatActivity implements ProfileDialogFragment.ProfileDialogListener {
    Button logoutBtn;
    ImageView backBtn, pfpBtn;
    Database db;
    private static String studentId;
    TextView fullNameView, idView, contactView, emailView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        getWindow().setNavigationBarColor(getResources().getColor(android.R.color.black));
        getWindow().setStatusBarColor(getResources().getColor(android.R.color.black));

        db = new Database(this);
        backBtn = findViewById(R.id.backButton);
        logoutBtn = findViewById(R.id.logoutButton);
        pfpBtn = findViewById(R.id.pfpButton);
        studentId = getIntent().getStringExtra("studentId");

        fullNameView = findViewById(R.id.fullNameDisplay);
        idView = findViewById(R.id.idDisplay);
        contactView = findViewById(R.id.contactDisplay);
        emailView = findViewById(R.id.emailDisplay);

        Cursor cursor = db.getStudentInfo(studentId);


        int idIndex = cursor.getColumnIndex(Database.columnStudentID);
        int emailIndex = cursor.getColumnIndex(Database.columnEmail);
        int contactIndex = cursor.getColumnIndex(Database.columnContacts);
        int imageIndex = cursor.getColumnIndex(Database.columnImage);
        int fullNameIndex = cursor.getColumnIndex("fullname");

        if(cursor != null && cursor.moveToFirst()){
            if (idIndex != -1 && emailIndex != -1 && contactIndex != -1 && imageIndex != -1 && fullNameIndex != -1){
                String fullName = cursor.getString(fullNameIndex);
                String image = cursor.getString(imageIndex);
                String contact = cursor.getString(contactIndex);
                String email = cursor.getString(emailIndex);
                String id = cursor.getString(idIndex);
                fullNameView.setText(fullName);

                if(image != null){
                    pfpBtn.setImageURI(Uri.parse(image));
                }
                idView.setText(id);
                contactView.setText(contact);
                emailView.setText(email);
            }

            cursor.close();

        }
        else{
            Log.d("ProfilePage", "Error");
        }


        pfpBtn.setOnClickListener(v -> {
            ProfileDialogFragment pdf = new ProfileDialogFragment(studentId);
            pdf.show(getSupportFragmentManager(), "ProfileDialog");
        });

        backBtn.setOnClickListener(v -> {
            Intent intentMainPage = new Intent(profile_page.this, main_page.class);
            intentMainPage.putExtra("StudentID", studentId);
            startActivity(intentMainPage);
        });

        logoutBtn.setOnClickListener(v -> {
            Intent intentLoginPage = new Intent(profile_page.this, MainActivity.class);
            startActivity(intentLoginPage);

        });
    }

    public void onImageSelect(Bitmap bitmap) {
        pfpBtn.setImageBitmap(bitmap);
    }
}