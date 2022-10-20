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

public class CustomerMoney1 extends AppCompatActivity {

    ListView listviewcustomerpayment;
    List<CMoney> cpaymentList;

    private static final String TAG = "CustomerMoney";
    DatabaseReference databaseCustomerPay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_money1);

        listviewcustomerpayment = findViewById(R.id.listviewcustomerpayment1);

        cpaymentList = new ArrayList<>();

        Intent intent = getIntent();

        String id = intent.getStringExtra(Customer_Payment.CORDER_ID);

        databaseCustomerPay = FirebaseDatabase.getInstance().getReference("customer_payments").child(id);

    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseCustomerPay.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                cpaymentList.clear();

                for(DataSnapshot cordersSnapshot: dataSnapshot.getChildren()){
                    CMoney corders = cordersSnapshot.getValue(CMoney.class);

                    cpaymentList.add(corders);
                }

                CustomerMoneyList adapter = new CustomerMoneyList(CustomerMoney1.this, cpaymentList);
                listviewcustomerpayment.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
