package com.example.kilimoeasy;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ViewFlipper;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    ViewFlipper v_flipper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int images[] = {R.drawable.slide1,R.drawable.slide6,R.drawable.slide2,R.drawable.slide5,
                R.drawable.slide3,R.drawable.slide4};

        v_flipper = findViewById(R.id.v_flipper);

        //for loop
        for (int image: images) {
            flipperImages(image);
        }

        //launching login customer activity
        Button customerLoginBtn = (Button)findViewById(R.id.customerLoginBtn);
        customerLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(getApplicationContext(),CustomerLogin.class);
                //show how to pass info to another activity
                startActivity(startIntent);
            }
        });

        //launching login Farmer activity
        Button farmerLoginBtn = (Button)findViewById(R.id.farmerLoginBtn);
        farmerLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(getApplicationContext(),FarmerLogin.class);
                //show how to pass info to another activity
                startActivity(startIntent);
            }
        });

        //launching login Vendor activity
        Button vendorLoginBtn = (Button)findViewById(R.id.vendorLoginBtn);
        vendorLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(getApplicationContext(),VendorLogin.class);
                //show how to pass info to another activity
                startActivity(startIntent);
            }
        });

        //launching login Admin activity
        Button adminLoginBtn = (Button)findViewById(R.id.adminLoginBtn);
        adminLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(getApplicationContext(),AdminSpecialLogin.class);
                //show how to pass info to another activity
                startActivity(startIntent);
            }
        });


        //launch an activity outside our app
        Button googleBtn = (Button)findViewById(R.id.googleBtn);
        googleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String google = "http://www.google.com";
                Uri webaddress = Uri.parse(google);

                Intent gotoGoogle = new Intent(Intent.ACTION_VIEW, webaddress);
                if(gotoGoogle.resolveActivity(getPackageManager()) != null) {
                    startActivity(gotoGoogle);
                }
            }
        });

    }

    public void flipperImages(int image){
        ImageView imageView = new ImageView(this);
        imageView.setBackgroundResource(image);

        v_flipper.addView(imageView);
        v_flipper.setFlipInterval(4000);//4sec
        v_flipper.setAutoStart(true);

        //animation
        v_flipper.setInAnimation(this, android.R.anim.slide_in_left);
        v_flipper.setOutAnimation(this, android.R.anim.slide_out_right);
    }
}
