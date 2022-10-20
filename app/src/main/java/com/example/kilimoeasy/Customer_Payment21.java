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

public class Customer_Payment21 extends AppCompatActivity {
    List<COrders> corderList;

    DatabaseReference databaseCustomerOrders;
    ListView listviewcustomerpayment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer__payment21);
        corderList = new ArrayList<>();

        Intent intent = getIntent();

        String id = intent.getStringExtra(SalePrice_Pay.PRODUCE_ID);
        listviewcustomerpayment = findViewById(R.id.listviewcustomerpayment21);

        databaseCustomerOrders = FirebaseDatabase.getInstance().getReference("customerorders").child(id);
    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseCustomerOrders.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                corderList.clear();

                for(DataSnapshot cordersSnapshot: dataSnapshot.getChildren()){
                    COrders corders = cordersSnapshot.getValue(COrders.class);

                    corderList.add(corders);
                }

                CustomerOrderList adapter = new CustomerOrderList(Customer_Payment21.this, corderList);
                listviewcustomerpayment.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
