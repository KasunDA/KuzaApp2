package com.example.nelson.kuzaapp;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

public class FarmerActivity extends AppCompatActivity {
    String farmProduct,unit,unitPrice,desc,image;
    EditText editTextFarmProduct;
    EditText editTextUnit;
    EditText editTextUnitPrice;
    Button Choose;
    Button CaptureImage;
    ImageView imageView;
    EditText description;
    Button buttonAddProduct;

    InputStream is=null;
    String result=null;
    String line=null;
    String  idNumber;
    int code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer);
        editTextFarmProduct=(EditText)findViewById(R.id.FarmProduct);
        editTextUnit=(EditText)findViewById(R.id.Unit);
        editTextUnitPrice=(EditText)findViewById(R.id.UnitPrice);
        Choose = (Button)findViewById(R.id.Choose);
        CaptureImage = (Button)findViewById(R.id.captureImage);
        imageView = (ImageView)findViewById(R.id.imageView);
        description = (EditText)findViewById(R.id.description);
        buttonAddProduct=(Button)findViewById(R.id.AddProduct);
        buttonAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                idNumber= getIntent().getStringExtra("idNumber");
                GetDataFromEditText();
                new InsertFarmProductData().execute();
            }
        });


    }
     public void GetDataFromEditText(){
        farmProduct = editTextFarmProduct.getText().toString();
        unit = editTextUnit.getText().toString();
        unitPrice = editTextUnitPrice.getText().toString();
         desc = description.getText().toString();
    }
    public void TakePicture(){

    }
    class InsertFarmProductData extends AsyncTask<String, Void, String> {
        private ProgressDialog pDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(FarmerActivity.this);
            pDialog.setMessage("Adding your item..please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
        @Override
        protected String doInBackground(String... params) {
            try{
                ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("idNumber", idNumber));
                nameValuePairs.add(new BasicNameValuePair("farmProduct", farmProduct));
                nameValuePairs.add(new BasicNameValuePair("unit", unit));
                nameValuePairs.add(new BasicNameValuePair("unitPrice", unitPrice));

                try
                {

                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost("http://bsmartkuza.com/kuzaAppConnect/add_farm_product.php");
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
                                pDialog.dismiss();
                                Toast.makeText(getBaseContext(), "Your Item has been added",
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
