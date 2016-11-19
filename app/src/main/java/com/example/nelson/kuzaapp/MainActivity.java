package com.example.nelson.kuzaapp;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        Button registerButton=(Button)findViewById(R.id.buttonRegisterFarmer);
        Button farmerLoginButton=(Button)findViewById(R.id.buttonFarmerLogin);
        Button viewProductsButton=(Button)findViewById(R.id.buttonViewProducts);
        Button askExpertButton =(Button)findViewById(R.id.buttonAskExpert);
        //  Button likeFacebookButton = (Button) findViewById(R.id.buttonLikeFacebook);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
                //getOpenFacebookIntent(MainActivity.this);

            }
        });
        farmerLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FarmerLoginActivity.class);
                startActivity(intent);

            }
        });
        viewProductsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  Intent intent = new Intent(getApplicationContext(), BuyerActivity.class);
                startActivity(intent);*/
                Intent intent = new Intent(getApplicationContext(), ViewSellers.class);
                startActivity(intent);

            }
        });
        askExpertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Askexpert.class);
                startActivity(intent);

            }
        });

        boolean twitterInstalled = false;;
        try {
            PackageManager packman = getPackageManager();
            packman.getPackageInfo("com.twitter.android", 0);
            twitterInstalled = true;
        } catch (Exception ex) {
            //ex.printStackTrace();
            twitterInstalled = false;

        }
        //set up Twitter button
        Button btnFollow = (Button) findViewById(R.id.buttonFollowTwitter);
        btnFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // this is the intent you actually want.
                // grabbed this by hooking a debugger up to twitter and debugging into android framework source.
                // this let me inspect the contents of the intent.
                Intent i = new Intent();
                i.setClassName("com.twitter.android", "com.twitter.android.ProfileActivity");
                i.putExtra("screen_name", "netubuli");
                try {
                    startActivity(i);
                }
                catch (Exception ex) {
                    // uh something failed
                    ex.printStackTrace();
                }
            }
        });
        //Log.d(LOG_TAG, "twiterinstalled: " + twitterInstalled);
        if (twitterInstalled){btnFollow.setVisibility(View.VISIBLE);} else{btnFollow.setVisibility(View.GONE);}

/*
      likeFacebookButton.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              //Intent intent = new Intent(getApplicationContext(), AskExpertActivity.class);
              //startActivity(intent);
              getOpenFacebookIntent(MainActivity.this);

          }
      });*/
    }
    public static Intent getOpenFacebookIntent(Context context) {

        try {
            context.getPackageManager().getPackageInfo("com.facebook.katana", 0);
            //return new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/<id_here>"));
            return new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/Tosby"));

        } catch (Exception e) {
            return new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/Tosby"));
        }
    }
}
