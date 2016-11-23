package com.example.nelson.kuzaapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * A login screen that offers login via email/password.
 */
public class FarmerLoginActivity extends AppCompatActivity {
    private AutoCompleteTextView editTextPhoneNumber;
    private EditText editTextIdNumber;
    private View mProgressView;
    private View mLoginFormView;
    String phoneNumber;
    String idNumber;
    InputStream is=null;
    String result=null;
    String line=null;
    int code;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer_login);
        // Set up the login form.
        editTextPhoneNumber = (AutoCompleteTextView) findViewById(R.id.editTextPhoneNumber);
        editTextIdNumber = (EditText) findViewById(R.id.editTextIdNumber);
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        Button buttonLogin = (Button) findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //attemptLogin();
                GetLoginData();
                new Login().execute();
            }
        });


    }

    public void GetLoginData() {
        phoneNumber=editTextPhoneNumber.getText().toString();
        idNumber=editTextIdNumber.getText().toString();
    }
    class Login extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(FarmerLoginActivity.this);
            pDialog.setMessage("Logging in..please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
        @Override
        protected String doInBackground(String... params) {
            try{
                ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                nameValuePairs.add(new BasicNameValuePair("phoneNumber", phoneNumber));
                nameValuePairs.add(new BasicNameValuePair("idNumber", idNumber));

                try
                {

                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost("http://bsmartkuza.com/kuzaAppConnect/login.php");
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
                                // Toast.makeText(getBaseContext(), "Farmer registered Successfully",Toast.LENGTH_SHORT).show();
                                pDialog.dismiss();
                                Intent intent = new Intent(getApplicationContext(), FarmerActivity.class);
                                intent.putExtra("idNumber",idNumber);
                                startActivity(intent);
                            }
                        });

                    }
                    else
                    {
                        runOnUiThread(new Runnable()
                        {
                            public void run()
                            {
                                Toast.makeText(getBaseContext(), "id number and phone number mismatch",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
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















