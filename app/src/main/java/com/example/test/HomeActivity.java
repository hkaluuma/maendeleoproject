package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {
    //global variables
    String username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //our codes from here
        Button buttonlogin = findViewById(R.id.login_btn);
        buttonlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //we can add our codes
                //make references to the xml
                EditText edtx_username = findViewById(R.id.edtx_username);
                EditText edtx_password = findViewById(R.id.edtx_password);

                //assigning the textfield objects to the global variables
                username = edtx_username.getText().toString();
                password = edtx_password.getText().toString();

                if (username.isEmpty()) {
                    edtx_username.setError("Username is required");
                }else if(password.isEmpty()) {
                    edtx_password.setError("Password is required");
                }else {
                    Toast.makeText(HomeActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                    Intent intentlogin = new Intent(HomeActivity.this, AddPatientActivity.class);
                    startActivity(intentlogin);
                }
            }
        });
    }
}