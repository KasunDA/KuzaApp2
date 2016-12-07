package com.example.nelson.kuzaapp;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.os.AsyncTask;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ViewSellers extends AppCompatActivity {

    ListView sellersLV;
    List<String> sellersList = new ArrayList<String>();
    List<String> productsList;
    ArrayAdapter<String> sellerslistAdapter;
    // Progress Dialog
    private ProgressDialog pDialog;

    // url to get all products list
    private static String url_get_sellers = "http://elearning2.maseno.ac.ke/kuzaAppConnect/getSellers.php";

    // JSON Node names
    private final String TAG_SUCCESS = "success";
    private final String TAG_PRODUCTS = "products";
    private  final String dbname = "name";
    private  final String dbmobile = "mobile";
    private  final String dblocation = "location";
    private  final String dbprice = "price";
    private  final String dbproduct = "productname";

    // products JSONArray
    JSONArray products = null;

    String name;
    String mobile;
    String price;
    String location;
    String product;

    InputStream is = null;
    //String result = null;
    String line = null;

    String data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_sellers);

/*
        name = getIntent().getStringExtra("name");
        mobile = getIntent().getStringExtra("mobile");
        email = getIntent().getStringExtra("email");
        location = getIntent().getStringExtra("location");
*/
        //productsList = new ArrayList<HashMap<String, String>>();
        productsList= new ArrayList<String>();
        // Loading products in Background Thread
        new LoadAllProducts().execute();
        sellersLV=(ListView)findViewById(R.id.sellersListView);


    }

    class LoadAllProducts extends AsyncTask<String, String, String> {
        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ViewSellers.this);
            pDialog.setMessage("retrieving products. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String result = "";
            try {
                ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                nameValuePairs.add(new BasicNameValuePair("name", ""));
                nameValuePairs.add(new BasicNameValuePair("location", ""));

                // String url = "http://elearning2.maseno.ac.ke/kuzaAppConnect/getSellers.php";
                String url = "http://bsmartkuza.com/kuzaAppConnect/getSellers.php";

                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url(url.trim()).build();

                Response response = client.newCall(request).execute();
                result = response.body().string();

                Log.d("RESPONSe",result);


            } catch (Exception e) {
                Log.e("Fail 3", e.toString());
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pDialog.dismiss();
            try {
                // Checking for SUCCESS TAG
                JSONObject json_data = new JSONObject(s);
                //code=(json_data.getInt("code"));
                int success = json_data.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // products found
                    // Getting Array of Products
                    products = json_data.getJSONArray(TAG_PRODUCTS);

                    // looping through All Products
                    for (int i = 0; i < products.length(); i++) {
                        JSONObject c = products.getJSONObject(i);

                        // Storing each json item in variable
                        name = c.getString(dbname);
                        mobile = c.getString(dbmobile);
                        location=c.getString(dblocation);
                        price=c.getString(dbprice);
                        product=c.getString(dbproduct);

                        data=name+"|"+mobile+"|"+product+"|"+location+"|"+price+"\n";

                        productsList.add(data);
                        Log.d("RESPONSe for LiST",data);

                    }
                    sellerslistAdapter=new ArrayAdapter<String>(ViewSellers.this,android.R.layout.simple_list_item_1,productsList);
                    sellersLV.setAdapter(sellerslistAdapter);

                } else {
                    Toast.makeText(getApplicationContext(), "Failed",
                            Toast.LENGTH_LONG).show();
                }
                //Toast.makeText(ViewSellersActivity.this, s, Toast.LENGTH_SHORT).show();
            }catch(JSONException e){

            }
        }
    }

    public void exitSystem(View v) {
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);

    }


}