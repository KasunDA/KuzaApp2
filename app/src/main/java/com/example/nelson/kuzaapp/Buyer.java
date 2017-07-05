package com.example.nelson.kuzaapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class Buyer extends AppCompatActivity {
    String location;
    String product;

    EditText editTextProduct;
    Spinner spinnerLocation;
    private ArrayAdapter locationAdapter;
    private Button buttonGetProducts;
    String [] locationItems={"All Areas","Kisumu","Luanda","Maseno","Awasi","Ahero","Kisii"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer);

        editTextProduct=(EditText)findViewById(R.id.editTextProduct);
        spinnerLocation=(Spinner)findViewById(R.id.spinnerLocation);
        locationAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,locationItems);
        locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLocation.setAdapter(locationAdapter);

        buttonGetProducts=(Button)findViewById(R.id.buttonGetProducts);
        buttonGetProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                location=spinnerLocation.getSelectedItem().toString();
                product=editTextProduct.getText().toString();
                Intent intent = new Intent(getApplicationContext(), ViewSellers.class);
                intent.putExtra("location",location);
                intent.putExtra("product",product);
                startActivity(intent);

            }
        });


    }
}
