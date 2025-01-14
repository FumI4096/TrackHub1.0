package com.example.trackhub;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.content.Intent;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Button loginBtn;
    private TextView toReg;
    private EditText enterIdText, enterPasswordText;
    private StringBuilder temporaryTextChange = new StringBuilder();
    private CheckBox showPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        getWindow().setNavigationBarColor(getResources().getColor(android.R.color.black));
        getWindow().setStatusBarColor(getResources().getColor(android.R.color.black));

        Database db = new Database(this);

        loginBtn = findViewById(R.id.loginButton);
        toReg = findViewById(R.id.toRegister);
        showPass = findViewById(R.id.showPassword);

        enterIdText = findViewById(R.id.enterId);
        enterPasswordText = findViewById(R.id.enterPassword);


        toReg.setOnClickListener(v -> {
            Intent intentRegPage = new Intent(MainActivity.this, register_page.class);
            startActivity(intentRegPage);
        });

        loginBtn.setOnClickListener(v -> {
            boolean overallValid = true;

            String getId = enterIdText.getText().toString();
            String getPassword = enterPasswordText.getText().toString();

            boolean checkInputs = checkAllInputsEmpty(getId, getPassword);

            if(checkInputs){
                Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show();
            }
            else{
                if(!db.login(getId, getPassword)){
                    Toast.makeText(this, "Incorrect login information. Please try again", Toast.LENGTH_SHORT).show();
                    enterIdText.setText("");
                    enterPasswordText.setText("");

                    overallValid = false;
                }

                if(overallValid){
                    enterIdText.setText("");
                    enterPasswordText.setText("");

                    Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();
                    Intent intentMainPage = new Intent(MainActivity.this, main_page.class);
                    intentMainPage.putExtra("StudentID", getId);
                    startActivity(intentMainPage);
                }

            }

        });

        showPass.setOnCheckedChangeListener((buttonView, isChecked) -> {
            int start = enterPasswordText.getSelectionStart();
            int end = enterPasswordText.getSelectionEnd();
            if (isChecked) {
                enterPasswordText.setTransformationMethod(null);

            }
            else {
                enterPasswordText.setTransformationMethod(new PasswordTransformationMethod());

            }
            enterPasswordText.setSelection(start, end);
        });

    }

    public boolean checkAllInputsEmpty(String id, String password){
        return id.isEmpty() || password.isEmpty();
    }
}