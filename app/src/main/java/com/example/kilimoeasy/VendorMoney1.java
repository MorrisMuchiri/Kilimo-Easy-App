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

public class VendorMoney1 extends AppCompatActivity {

    ListView listviewvendorpayment;
    List<VMoney> vpaymentList;

    private static final String TAG = "VendorMoney";
    DatabaseReference databaseVendorPay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_money1);

        listviewvendorpayment = findViewById(R.id.listviewvendorpayment1);

        vpaymentList = new ArrayList<>();

        Intent intent = getIntent();

        String id = intent.getStringExtra(Vendor_Payment.VORDER_ID);
        databaseVendorPay = FirebaseDatabase.getInstance().getReference("vendor_payments").child(id);
    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseVendorPay.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                vpaymentList.clear();

                for(DataSnapshot cordersSnapshot: dataSnapshot.getChildren()){
                    VMoney vorders = cordersSnapshot.getValue(VMoney.class);

                    vpaymentList.add(vorders);
                }

                VendorMoneyList adapter = new VendorMoneyList(VendorMoney1.this, vpaymentList);
                listviewvendorpayment.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
