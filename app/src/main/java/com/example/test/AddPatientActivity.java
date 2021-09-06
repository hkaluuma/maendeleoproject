package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class AddPatientActivity extends AppCompatActivity {
    //our global variables
    String selected_disease, selected_city, patientnames, age, contacts, password, email, comments;
    Button buttonsubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addpatient);

        //make reference to the xml for the spinner
        Spinner spinnerd =  findViewById(R.id.spinner_disease);
        //assigning data source
        String[] disease_array = {"Malaria","COVID19","Yellow Fever","HepatitisB"};
        //desinged a spinner item design
        //configure An array adapter
        ArrayAdapter<String> disease_adapter = new ArrayAdapter<String>(AddPatientActivity.this, R.layout.spinner_item_design, R.id.spinner_txv, disease_array);
        //assinging spinner to array adapter
        spinnerd.setAdapter(disease_adapter);
        //set on itemselected listener
        spinnerd.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_disease = disease_array[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //selected_disease = "nothing selected";

            }
        });

        //implementation of button
        //making reference to the button
        buttonsubmit = (Button) findViewById(R.id.register_patient);
        buttonsubmit.setOnClickListener(new View.OnClickListener() {
            //variables
            @Override
            public void onClick(View v) {
                //make references to the xml
                EditText edx_names = findViewById(R.id.edtx_patientNames);
                EditText edx_age = findViewById(R.id.edtx_age);
                EditText edx_contact = findViewById(R.id.edtx_contact);
                EditText edx_email = findViewById(R.id.edtx_email);
                EditText edx_password = findViewById(R.id.edtx_password);


                //assigning the textfield objetcts to the global variables
                patientnames = edx_names.getText().toString();

                // check if empty
                if (patientnames.isEmpty()) {
                    edx_names.setError("Patient names are required");
                }



                //happens here
                //Toast.makeText(AddPatientActivity.this, "Submission successful", Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(), "Submission succesful", Toast.LENGTH_SHORT).show();
                //navigate between two activities
                //Intent intentregister = new Intent(AddPatientActivity.this,HomeActivity.class);
                //startActivity(intentregister);

            }
        });




    }

    //functions here
}