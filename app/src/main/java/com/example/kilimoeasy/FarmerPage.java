package com.example.kilimoeasy;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import javax.annotation.Nullable;

public class FarmerPage extends AppCompatActivity {
    TextView fullName,cverifyMsg,buyingprice, checkcorders, coanalysis, vanalysis, farmermoney1;
    FirebaseAuth cAuth;
    FirebaseFirestore cStore;
    String userId;
    Button cresendCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer_page);
        fullName = findViewById(R.id.farmerIntro);

        cAuth = FirebaseAuth.getInstance();
        cStore = FirebaseFirestore.getInstance();

        cresendCode = findViewById(R.id.fresendCode);
        cverifyMsg = findViewById(R.id.fverifyMsg);
        buyingprice = findViewById(R.id.buyingprice);
        //checkcorders = findViewById(R.id.checkcorders);
        coanalysis = findViewById(R.id.coanalysis1);
        vanalysis = findViewById(R.id.voanalysis1);
        farmermoney1 = findViewById(R.id.farmermoney1);

        buyingprice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),BuyPrice.class));
            }
        });

        coanalysis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),SalePrice_Pay2.class));
            }
        });
        vanalysis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),SalePrice_Pay31.class));
            }
        });
        farmermoney1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),BuyPrice_Pay1.class));
            }
        });

        userId = cAuth.getCurrentUser().getUid();
        final FirebaseUser user = cAuth.getCurrentUser();

        if(!user.isEmailVerified()){
            cverifyMsg.setVisibility(View.VISIBLE);
            cresendCode.setVisibility(View.VISIBLE);

            cresendCode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {

                    user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(v.getContext(), "Verification Email has been sent! ", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("tag","onFailure: Email not sent " + e.getMessage());
                        }
                    });
                }
            });
        }


        DocumentReference documentReference = cStore.collection("users").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                fullName.setText((documentSnapshot.getString("faName")));
            }
        });

    }

    public void logout1 (View view) {
        FirebaseAuth.getInstance().signOut();//logout
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        finish();
    }
}
