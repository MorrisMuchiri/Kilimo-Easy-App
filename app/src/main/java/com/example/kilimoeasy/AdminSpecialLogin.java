package com.example.kilimoeasy;

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
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class AdminSpecialLogin extends AppCompatActivity {
    private EditText cEmail, cPassword;
    private Button cLoginBtn;
    private TextView cforgotTextLink;
    private ProgressBar cprogressBar;

    private FirebaseAuth cAuth;
    private DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_special_login);

        init();

        reference = FirebaseDatabase.getInstance().getReference().child("Admin");

        clickListener();
    }

    private void clickListener(){

        cLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = cEmail.getText().toString();
                String password = cPassword.getText().toString();

                if(TextUtils.isEmpty(email)) {
                    Toast.makeText(AdminSpecialLogin.this, "Enter Valid Email!!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(password)) {
                    Toast.makeText(AdminSpecialLogin.this, "Enter Valid Password!!", Toast.LENGTH_SHORT).show();
                    return;
                }

                cprogressBar.setVisibility(View.VISIBLE);
                loginValidations(email, password);

            }
        });

    }

    private void loginValidations(final String email, final String password) {
        //here to check for admin

        Query query = reference.orderByChild("admin").equalTo(true);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    if (snapshot.exists() && snapshot.hasChild("email")) {

                        String cEmail = snapshot.child("email").getValue(String.class);

                        if (cEmail.equalsIgnoreCase(email)) {
                            loginUser(email, password);
                        } else {
                            cprogressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(AdminSpecialLogin.this, "Please input Admin email", Toast.LENGTH_SHORT).show();
                        }


                }else{
                    cprogressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(AdminSpecialLogin.this, "Please input Admin email", Toast.LENGTH_SHORT).show();
                }

            }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AdminSpecialLogin.this, "Error!"+ databaseError.getMessage(),
                        Toast.LENGTH_SHORT).show();
                cprogressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void init() {
        cEmail = findViewById(R.id.adminloginEmail1);
        cPassword = findViewById(R.id.adminloginpassword1);
        cLoginBtn = findViewById(R.id.adminloginBtn1);
        cprogressBar = findViewById(R.id.adprogressBar1);
        cAuth = FirebaseAuth.getInstance();
    }

    private void loginUser(String email, String password){

        cAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()){
                            cprogressBar.setVisibility(View.INVISIBLE);
                            startActivity(new Intent(AdminSpecialLogin.this, AdminPage.class));
                        }else{
                            cprogressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(AdminSpecialLogin.this, "Error!"+task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }
}
