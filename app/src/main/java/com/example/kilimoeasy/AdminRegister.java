package com.example.kilimoeasy;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AdminRegister extends AppCompatActivity {
    public static final String TAG = "TAG";
    EditText cFullName, cEmail, cPassword, cPhone;
    Button cRegisterBtn;
    TextView cLoginBtn;
    FirebaseAuth cAuth;
    ProgressBar cprogressBar;
    FirebaseFirestore cStore;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_register);

        cFullName = findViewById(R.id.adminfullName);
        cEmail = findViewById(R.id.adminEmail);
        cPassword = findViewById(R.id.adminpassword);
        cPhone = findViewById(R.id.adminphone);
        cRegisterBtn = findViewById(R.id.adminregisterBtn);
        cLoginBtn = findViewById(R.id.admin1Text);

        cAuth = FirebaseAuth.getInstance();
        cStore = FirebaseFirestore.getInstance();
        cprogressBar = findViewById(R.id.aprogressBar);

        if(cAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(),AdminPage.class));
            finish();
        }


        cRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = cEmail.getText().toString().trim();
                String password = cPassword.getText().toString().trim();
                final String fullName = cFullName.getText().toString().trim();
                final String phone = cPhone.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    cEmail.setError("Email is Required!");
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    cPassword.setError("Password is Required!");
                    return;
                }

                if(password.length() < 6){
                    cPassword.setError("Password Must be >= 6 Characters");
                    return;
                }

                cprogressBar.setVisibility(View.VISIBLE);

                // REGISTER THE USER IN FIREBASE

                cAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            //send verification email

                            FirebaseUser fuser = cAuth.getCurrentUser();
                            fuser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(AdminRegister.this, "Verification Email has been sent! ", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG,"onFailure: Email not sent " + e.getMessage());
                                }
                            });


                            Toast.makeText(AdminRegister.this, "User Created!", Toast.LENGTH_SHORT).show();
                            userID = cAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = cStore.collection("users").document(userID);
                            Map<String,Object> user = new HashMap<>();
                            user.put("aName",fullName);
                            user.put("aemail",email);
                            user.put("aphone",phone);
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG,"onSuccess: user Profile is created for "+ userID);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG,"onFailure: " + e.toString());
                                }
                            });

                            startActivity(new Intent(getApplicationContext(),AdminPage.class));

                        }else{
                            Toast.makeText(AdminRegister.this, "Error!" + task.getException().getMessage() , Toast.LENGTH_SHORT).show();
                            cprogressBar.setVisibility(View.GONE);
                        }

                    }
                });
            }
        });

        cLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),AdminLogin.class));
            }
        });
    }
}
