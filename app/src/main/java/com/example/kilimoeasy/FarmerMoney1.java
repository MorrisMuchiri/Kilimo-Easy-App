package com.example.kilimoeasy;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FarmerMoney1 extends AppCompatActivity {

    ListView listviewfarmerpayment;
    List<FMoney> fpaymentList;

    private static final String TAG = "FarmerMoney";
    DatabaseReference databaseFarmerPay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer_money1);

        listviewfarmerpayment = findViewById(R.id.listviewfarmerpayment1);
        fpaymentList = new ArrayList<>();

        Intent intent = getIntent();

        String id = intent.getStringExtra(Farmer_Payment.FORDER_ID);
        databaseFarmerPay = FirebaseDatabase.getInstance().getReference("farmer_payments").child(id);
    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseFarmerPay.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                fpaymentList.clear();

                for(DataSnapshot cordersSnapshot: dataSnapshot.getChildren()){
                    FMoney forders = cordersSnapshot.getValue(FMoney.class);

                    fpaymentList.add(forders);
                }

                FarmerMoneyList adapter = new FarmerMoneyList(FarmerMoney1.this, fpaymentList);
                listviewfarmerpayment.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
