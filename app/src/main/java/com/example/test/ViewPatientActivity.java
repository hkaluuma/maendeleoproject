package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ViewPatientActivity extends AppCompatActivity {
    //global variables
    String fetchpatient_url= "http://192.168.1.160/maendeleo/fetch_patient_names.php";
    ListView patient_lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_patient);
        //make reference to the listview
        patient_lv = (ListView)findViewById(R.id.viewpatients);

        boolean checkconnect = haveNetworkConectivity();
        if(checkconnect){
            Fetchpatientasync patientasync = new Fetchpatientasync();
            patientasync.execute();
        }else{
            Toast.makeText(this, "You are not connected to internet", Toast.LENGTH_SHORT).show();
        }

    }

    class Fetchpatientasync extends AsyncTask<String, String, String>{

        String responcefromphp;
        ProgressDialog pdialog = new ProgressDialog(ViewPatientActivity.this);
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //display message to the user
            pdialog.setMessage("Fetching data ....");
            //user can't cancel the dialog
            pdialog.setCancelable(false);
            //we don't know how long it takes to submit
            pdialog.setIndeterminate(false);
            //show the dialog
            pdialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {

                DefaultHttpClient httpclient = new DefaultHttpClient();

                HttpPost httppost = new HttpPost(fetchpatient_url);
                //https://hc.apache.org/httpcomponents-client-4.5.x/current/httpclient/apidocs/org/apache/http/client/methods/HttpPost.html

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
                responcefromphp = sb.toString();
                inputStream.close();

            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Try Again, Unexpected Error on method doing in background", Toast.LENGTH_LONG).show();
            }

            return responcefromphp;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pdialog.dismiss();
            //waiting for response from php
            String[] patientnames = responcefromphp.split("#");
            ArrayAdapter<String> listviewadapter = new ArrayAdapter<String>(ViewPatientActivity.this,R.layout.listview_item_design,R.id.listview_txv,patientnames);
            patient_lv.setAdapter(listviewadapter);

            patient_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                }
            });
        }
    }

    //interet connections fucntion
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