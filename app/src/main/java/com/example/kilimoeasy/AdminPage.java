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

public class AdminPage extends AppCompatActivity {
    TextView fullName,cverifyMsg, addproducedatbase, adminanalysis, vanalysis, payfarmer, customerpayment, vendorpayment;
    FirebaseAuth cAuth;
    FirebaseFirestore cStore;
    String userId;
    Button cresendCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_page);
        fullName = findViewById(R.id.adminIntro);

        cAuth = FirebaseAuth.getInstance();
        cStore = FirebaseFirestore.getInstance();

        cresendCode = findViewById(R.id.aresendCode);
        cverifyMsg = findViewById(R.id.averifyMsg);

        addproducedatbase = findViewById(R.id.addproducedatabase);
        adminanalysis = findViewById(R.id.adminanalysis);
        vanalysis = findViewById(R.id.adminanalysis1);
        payfarmer = findViewById(R.id.payfarmer);
        customerpayment = findViewById(R.id.payfarmer1);
        vendorpayment = findViewById(R.id.payfarmer2);



        payfarmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),BuyPrice_Pay.class));
            }
        });
        addproducedatbase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),AdminProduce.class));
            }
        });
        adminanalysis.setOnClickListener(new View.OnClickListener() {
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
        customerpayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),SalePrice_Pay1.class));
            }
        });
        vendorpayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),SalePrice_Pay301.class));
            }
        });


        userId = cAuth.getCurrentUser().getUid();
        final FirebaseUser user = cAuth.getCurrentUser();

        if(!user.isEmailVerified()){
            cverifyMsg.setVisibility(View.INVISIBLE);
            cresendCode.setVisibility(View.INVISIBLE);

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
                fullName.setText((documentSnapshot.getString("aName")));
            }
        });

    }

    public void logout3 (View view) {
        FirebaseAuth.getInstance().signOut();//logout
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        finish();
    }


}
