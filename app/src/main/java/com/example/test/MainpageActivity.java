package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class MainpageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainpage);
        //new codes here
        ImageView imv_addpatient =  findViewById(R.id.imv_addpatient);
        imv_addpatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainpageActivity.this, "Adding patient", Toast.LENGTH_SHORT).show();
                Intent intentadd = new Intent(MainpageActivity.this, AddPatientActivity.class);
                startActivity(intentadd);
            }
        });
    }
}