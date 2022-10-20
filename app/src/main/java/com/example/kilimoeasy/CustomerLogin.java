package com.example.kilimoeasy;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class CustomerLogin extends AppCompatActivity {
    EditText cEmail, cPassword;
    Button cLoginBtn;
    TextView cCreateBtn,cforgotTextLink;
    ProgressBar cprogressBar;
    FirebaseAuth cAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_login);

        cEmail = findViewById(R.id.customerloginEmail);
        cPassword = findViewById(R.id.customerloginpassword);
        cLoginBtn = findViewById(R.id.customerloginBtn);
        cCreateBtn = findViewById(R.id.customer2Text);
        cprogressBar = findViewById(R.id.progressBar);
        cAuth = FirebaseAuth.getInstance();
        cforgotTextLink = findViewById(R.id.cforgotPassword);

        cLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = cEmail.getText().toString().trim();
                String password = cPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    cEmail.setError("Email is Required!");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    cPassword.setError("Password is Required!");
                    return;
                }

                if (password.length() < 6) {
                    cPassword.setError("Password Must be >= 6 Characters");
                    return;
                }

                cprogressBar.setVisibility(View.VISIBLE);

                //authenticate the user
                cAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(CustomerLogin.this, "Logged in Successfully!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), CustomerPage.class));
                        } else {
                            Toast.makeText(CustomerLogin.this, "Error!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            cprogressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });

        cCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), CustomerRegister.class));
            }
        });

        cforgotTextLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final EditText resetMail = new EditText(v.getContext());
                final AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("Reset Password ?");
                passwordResetDialog.setMessage("Enter Your Email To Receive Reset Link.");
                passwordResetDialog.setView(resetMail);

                passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                      // extract the email and send reset link

                        String mail = resetMail.getText().toString();
                        cAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(CustomerLogin.this, "Reset Link sent to your Email", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(CustomerLogin.this, "Error! Reset Link is not sent" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });

                passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // close the dialog
                    }
                });

                passwordResetDialog.create().show();

            }
        });
    }
}