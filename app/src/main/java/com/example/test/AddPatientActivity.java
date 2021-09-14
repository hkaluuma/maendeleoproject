package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class AddPatientActivity extends AppCompatActivity {
    //our global variables
    String selected_disease, selected_city, patientnames, age, contacts, password, email, comments;
    Button buttonsubmit;
    String addpatient_url = "http://192.168.1.160/maendeleo/addpatient.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addpatient);

        //make reference to the xml for the spinner
        Spinner spinnerd = findViewById(R.id.spinner_disease);
        Spinner spinnercity = findViewById(R.id.spinner_city);

        //to capture comments
        EditText edt_comments = findViewById(R.id.edtx_othercommments);
        comments = edt_comments.getText().toString();
        //assigning data source
        String[] disease_array = {"nothing selected", "Malaria", "COVID19", "Yellow Fever", "HepatitisB"};
        String[] city_array = {"nothing selected", "Nairobi", "Dodoma", "Kampala", "Kigali", "Juba"};

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
                selected_disease = "nothing selected";

            }
        });

        //configure our arrayadapter for spinner city
        ArrayAdapter<String> city_adapter = new ArrayAdapter<String>(AddPatientActivity.this, R.layout.spinner_item_design, R.id.spinner_txv, city_array);
        spinnercity.setAdapter(city_adapter);

        spinnercity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_city = city_array[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selected_city = "nothing selected";

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
                EditText edx_password = findViewById(R.id.editext_pasword);



                //assigning the textfield objetcts to the global variables
                patientnames = edx_names.getText().toString();
                //patientnames = edx_names.getText().toString();
                age = edx_age.getText().toString();
                contacts = edx_contact.getText().toString();
                email = edx_email.getText().toString();
                password = edx_password.getText().toString();


                // check if empty
                if (patientnames.isEmpty()) {
                    edx_names.setError("Patient names are required");
                } else if (password.isEmpty()) {
                    edx_password.setError("Password is required");
                } else if (age.isEmpty()) {
                    edx_age.setError("Patient age is required");
                } else if (contacts.isEmpty()) {
                    edx_contact.setError("Phonenumber is required");
                } else if (email.isEmpty()) {
                    edx_email.setError("Email is required");
                } else if (selected_disease.equals("nothing selected")) {
                    Toast.makeText(AddPatientActivity.this, "No disease selected", Toast.LENGTH_SHORT).show();
                } else if (selected_city.equals("nothing selected")) {
                    Toast.makeText(AddPatientActivity.this, "No city selected", Toast.LENGTH_SHORT).show();
                } else {

                    boolean checkconnection = haveNetworkConectivity();
                    if(checkconnection){
                        Submitpatient asyncobj = new Submitpatient();
                        asyncobj.execute();

                    }else{
                        Toast.makeText(AddPatientActivity.this, "Network not connected, Check your connection", Toast.LENGTH_SHORT).show();
                    }

                }


            }
        });


    }//end of oncreate method

    //async task
    class Submitpatient extends AsyncTask<String, String, String>{
        ProgressDialog pdialog;
        String responsefromphp;
        @Override
        protected void onPreExecute() {
            pdialog = new ProgressDialog(AddPatientActivity.this);
            super.onPreExecute();
            //display message to the user
            pdialog.setMessage("Please wait ....");
            //user can't cancel the dialog
            pdialog.setCancelable(false);
            //we don't know how long it takes to submit
            pdialog.setIndeterminate(false);
            //show the dialog
            pdialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            //new codes
            try {

                DefaultHttpClient httpclient = new DefaultHttpClient();

                HttpPost httppost = new HttpPost(addpatient_url);
                //https://hc.apache.org/httpcomponents-client-4.5.x/current/httpclient/apidocs/org/apache/http/client/methods/HttpPost.html

                ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);

                nameValuePairs.add(new BasicNameValuePair("cap_disease", selected_disease));
                nameValuePairs.add(new BasicNameValuePair("cap_fullname", patientnames));
                nameValuePairs.add(new BasicNameValuePair("cap_password", password));
                nameValuePairs.add(new BasicNameValuePair("cap_age", age));
                nameValuePairs.add(new BasicNameValuePair("cap_contact", contacts));
                nameValuePairs.add(new BasicNameValuePair("cap_email", email));
                nameValuePairs.add(new BasicNameValuePair("cap_city", selected_city));
                nameValuePairs.add(new BasicNameValuePair("cap_comments", comments));

                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);
                //used to read data from an input like file (https://www.w3spoint.com/input-output-tutorial-java)
                InputStream inputStream = response.getEntity().getContent();

                BufferedReader rd = new BufferedReader(new InputStreamReader(inputStream), 4096);
                //bufferedreader (https://www.javatpoint.com/java-bufferedreader-class)
                //Java BufferedReader class is used to read the text from a character-based input stream. It can be used to read data line by line by readLine() method.
                //buffered reader takes parameters of the reader and input buffer size int
                String line;

                StringBuilder sb = new StringBuilder();
                //StringBuilder is a mutable sequence of characters. StringBuilder is used when we want to modify Java strings in-place(https://zetcode.com/java/stringbuilder/)
                while ((line = rd.readLine()) != null) {
                    sb.append(line);
                }
                rd.close();
                responsefromphp = sb.toString();
                inputStream.close();

            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Try Again, Unexpected Error on method doing in background", Toast.LENGTH_LONG).show();
            }

            return responsefromphp;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //dismisses dialog
            pdialog.dismiss();
            if(responsefromphp.equals("1")){
                //Toast.makeText(AddPatientActivity.this, "You are connected to network", Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(), "Submission succesful", Toast.LENGTH_SHORT).show();
                //navigate between two activities
                Intent intentregister = new Intent(AddPatientActivity.this, MainpageActivity.class);
                startActivity(intentregister);
            }else if(responsefromphp.equals("0")){
                Toast.makeText(AddPatientActivity.this, "Failed response from php", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(AddPatientActivity.this, "Technical error, contact admin", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //functions here
    //method to check internet availability(WiFi and MobileData)
    private boolean haveNetworkConectivity() {
        boolean haveConnectedWi;
        haveConnectedWi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWi || haveConnectedMobile;
    }

}


