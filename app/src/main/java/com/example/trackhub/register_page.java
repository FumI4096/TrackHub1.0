package com.example.trackhub;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.text.method.PasswordTransformationMethod;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class register_page extends AppCompatActivity {

    
    private Button RegisterBtn;
    private TextView toLoginBtn;
    private EditText idEdit, firstNameEdit, lastNameEdit, emailEdit, passwordEdit, contactEdit;
    String getImage;
    private CheckBox showPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        getWindow().setNavigationBarColor(getResources().getColor(android.R.color.black));
        getWindow().setStatusBarColor(getResources().getColor(android.R.color.black));


        showPass = findViewById(R.id.showPassword);
        RegisterBtn = findViewById(R.id.registerButton);
        toLoginBtn = findViewById(R.id.toLogin);
        idEdit = findViewById(R.id.regId);
        firstNameEdit = findViewById(R.id.regFirstName);
        lastNameEdit = findViewById(R.id.regLastName);
        emailEdit = findViewById(R.id.regEmail);
        passwordEdit = findViewById(R.id.regPassword);
        contactEdit = findViewById(R.id.regContact);

        Database db = new Database(this);

        RegisterBtn.setOnClickListener(v -> {
            boolean overallValid = true;

            String getId = idEdit.getText().toString();
            String getFirstName = firstNameEdit.getText().toString();
            String getLastName = lastNameEdit.getText().toString();
            String getEmail = emailEdit.getText().toString();
            String getPassword = passwordEdit.getText().toString();
            String getContacts = contactEdit.getText().toString();

            boolean checkInputs = checkAllInputsEmpty(getId, getFirstName, getLastName, getEmail, getPassword, getContacts);

            if(checkInputs){
                Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show();
            }
            else{

                if(db.checkIdRegistered(Integer.parseInt(getId))){
                    Toast.makeText(this, "ID already registered", Toast.LENGTH_SHORT).show();
                    idEdit.setText("");

                    overallValid = false;
                }

                if (!checkIdValid(getId)) {
                    Toast.makeText(this, "Invalid ID", Toast.LENGTH_SHORT).show();
                    idEdit.setText("");

                    overallValid = false;
                }

                if (!checkNameValid(getFirstName)) {
                    Toast.makeText(this, "Invalid First Name", Toast.LENGTH_SHORT).show();
                    firstNameEdit.setText("");

                    overallValid = false;
                }

                if (!checkNameValid(getLastName)) {
                    Toast.makeText(this, "Invalid Last Name", Toast.LENGTH_SHORT).show();
                    lastNameEdit.setText("");

                    overallValid = false;
                }

                if (!checkPasswordValid(getPassword)) {
                    passwordEdit.setText("");

                    overallValid = false;
                }

                if(db.checkEmailRegistered(getEmail)){
                    Toast.makeText(this, "Email already registered", Toast.LENGTH_SHORT).show();
                    emailEdit.setText("");

                    overallValid = false;
                }

                if (!checkEmailValid(getEmail)) {
                    Toast.makeText(this, "Invalid Email", Toast.LENGTH_SHORT).show();
                    emailEdit.setText("");

                    overallValid = false;
                }

                if (!checkContactValid(getContacts)) {
                    Toast.makeText(this, "Invalid Contact Number", Toast.LENGTH_SHORT).show();
                    contactEdit.setText("");

                    overallValid = false;
                }

                if(overallValid){
                    idEdit.setText("");
                    firstNameEdit.setText("");
                    lastNameEdit.setText("");
                    passwordEdit.setText("");
                    emailEdit.setText("");
                    contactEdit.setText("");

                    int toIntId = Integer.parseInt(getId);

                    db.insertStudentInfo(toIntId, getFirstName, getLastName, getEmail, getPassword , getContacts);
                    Toast.makeText(this, "Registration submitted Successfully!", Toast.LENGTH_SHORT).show();
                    Intent intentLoginPage = new Intent(register_page.this, MainActivity.class);
                    startActivity(intentLoginPage);
                }

            }

        });

        toLoginBtn.setOnClickListener(v -> {
            idEdit.setText("");
            firstNameEdit.setText("");
            lastNameEdit.setText("");
            passwordEdit.setText("");
            emailEdit.setText("");
            contactEdit.setText("");

            Intent intentLoginPage = new Intent(register_page.this, MainActivity.class);
            startActivity(intentLoginPage);
        });

        showPass.setOnCheckedChangeListener((buttonView, isChecked) -> {
            int start = passwordEdit.getSelectionStart();
            int end = passwordEdit.getSelectionEnd();
            if (isChecked) {
                passwordEdit.setTransformationMethod(null);

            }
            else {
                passwordEdit.setTransformationMethod(new PasswordTransformationMethod());

            }
            passwordEdit.setSelection(start, end);
        });

    }

    public boolean checkAllInputsEmpty(String id, String firstName, String lastName, String email, String password, String contact){
        return id.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty() || contact.isEmpty();
    }

    public boolean checkIdValid(String id){
        String pattern = "^[0-9]+$";

        return id != null && id.matches(pattern);
    }

    public boolean checkNameValid(String name){
        String pattern = "^[a-zA-Z]+$";

        return name != null && name.matches(pattern);
    }

    public boolean checkEmailValid(String email){
        String pattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

        return email != null && email.matches(pattern);
    }

    public boolean checkPasswordValid(String password){
        if(password.length() < 8){
            Toast.makeText(this, "Password must be at least 8 characters long", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(!password.matches(".*[A-Z].*")){
            Toast.makeText(this, "Password must contain at least 1 uppercase letter", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(!password.matches(".*[a-z].*")){
            Toast.makeText(this, "Password must contain at least 1 lowercase letter", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!password.matches(".*\\d.*")) {
            Toast.makeText(this, "Password must contain at least 1 digit", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!password.matches(".*[@$!%*?&_].*")) {
            Toast.makeText(this, "Password must contain at least 1 special character", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    public boolean checkContactValid(String contact){
        String pattern = "[0-9]{11}$";

        return contact != null && contact.matches(pattern);
    }
}