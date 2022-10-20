package com.example.kilimoeasy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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

public class Customer_Payment extends AppCompatActivity {
    public static final String CORDER_ID = "customer_order_id";
    public static final String CORDER_QUANTITY = "customer_order_quantity";
    public static final String CORDER_TOTAL = "customer_order_total";

    List<COrders> corderList;

    DatabaseReference databaseCustomerOrders;
    ListView listviewcustomerpayment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer__payment);

        corderList = new ArrayList<>();

        Intent intent = getIntent();

        String id = intent.getStringExtra(SalePrice_Pay.PRODUCE_ID);
        listviewcustomerpayment = findViewById(R.id.listviewcustomerpayment);

        databaseCustomerOrders = FirebaseDatabase.getInstance().getReference("customerorders").child(id);

        listviewcustomerpayment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                COrders corders = corderList.get(position);
                Intent intent = new Intent(getApplicationContext(), CustomerMoney.class);

                intent.putExtra(CORDER_ID, corders.getCustomerorderId());
                intent.putExtra(CORDER_QUANTITY, corders.getCustomerquantity());
                intent.putExtra(CORDER_TOTAL, corders.getCustomertotalprice());
                startActivity(intent);
            }
        });
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

                CustomerOrderList adapter = new CustomerOrderList(Customer_Payment.this, corderList);
                listviewcustomerpayment.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
