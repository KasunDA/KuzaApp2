package com.example.nelson.kuzaapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class RegisterActivity extends AppCompatActivity {
    EditText editTextName;//(EditText)findViewById(R.id.editTextName);
    EditText editTextLocation;//(EditText)findViewById(R.id.editTextLocation);
    EditText editTextPhoneNumber;//=(EditText)findViewById(R.id.editTextPhoneNumber);
    EditText editTextIdNumber;//=(EditText)findViewById(R.id.editTextPhoneNumber);
    String name;// = editTextName.getText().toString();
    String location;// = editTextLocation.getText().toString();
    String phoneNumber;// =editTextPhoneNumber.getText().toString();
    String idNumber;// =editTextIdNumber.getText().toString();


    InputStream is=null;
    String result=null;
    String line=null;
    int code;


    Button buttonSaveFarmerDetails;//=(Button)findViewById(R.id.buttonSaveFarmerDetails);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
         editTextName=(EditText)findViewById(R.id.editTextName);
         editTextLocation=(EditText)findViewById(R.id.editTextLocation);
         editTextPhoneNumber=(EditText)findViewById(R.id.editTextPhoneNumber);
         editTextIdNumber=(EditText)findViewById(R.id.editTextPhoneNumber);
        buttonSaveFarmerDetails=(Button)findViewById(R.id.buttonSaveFarmerDetails);
        buttonSaveFarmerDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /* Intent intent = new Intent(getApplicationContext(), MainActivity.class);
               startActivity(intent);*/
                GetDataFromEditText();

                new InsertData().execute();

            }
        });


    }

    public void GetDataFromEditText(){
        name = editTextName.getText().toString();
        location = editTextLocation.getText().toString();
        phoneNumber=editTextPhoneNumber.getText().toString();
        idNumber=editTextIdNumber.getText().toString();

    }

    class InsertData extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            try{
                ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                nameValuePairs.add(new BasicNameValuePair("name", name));
                nameValuePairs.add(new BasicNameValuePair("location", location));
                nameValuePairs.add(new BasicNameValuePair("phoneNumber", phoneNumber));
                nameValuePairs.add(new BasicNameValuePair("idNumber", idNumber));

                try
                {

                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost("http://elearning2.maseno.ac.ke/kuzaAppConnect/register.php");
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    HttpResponse response = httpclient.execute(httppost);
                    HttpEntity entity = response.getEntity();
                    is = entity.getContent();

                    Log.e("pass 1", "connection success ");
                }
                catch(Exception e)
                {
                    Log.e("Fail 1", e.toString());
                    Toast.makeText(getApplicationContext(), "Invalid IP Address",
                            Toast.LENGTH_LONG).show();
                }

                try
                {
                    BufferedReader reader = new BufferedReader
                            (new InputStreamReader(is,"iso-8859-1"),8);
                    StringBuilder sb = new StringBuilder();
                    while ((line = reader.readLine()) != null)
                    {
                        sb.append(line + "\n");
                    }
                    is.close();
                    result = sb.toString();
                    Log.e("pass 2", "connection success ");
                }
                catch(Exception e)
                {
                    Log.e("Fail 2", e.toString());
                }

                try
                {
                    JSONObject json_data = new JSONObject(result);
                    code=(json_data.getInt("code"));

                    if(code==1)
                    {
                        runOnUiThread(new Runnable()
                        {
                            public void run()
                            {
                                Toast.makeText(getBaseContext(), "Farmer registered Successfully",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                    else
                    {
                        Toast.makeText(getBaseContext(), "Sorry, Try Again",
                                Toast.LENGTH_LONG).show();
                    }
                }
                catch(Exception e)
                {
                    Log.e("Fail 3", e.toString());
                }
            }
            finally{

            }
            return null;
        }}

}
