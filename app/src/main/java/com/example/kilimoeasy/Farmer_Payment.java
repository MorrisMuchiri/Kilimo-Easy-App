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
import com.google.type.Money;

import java.util.ArrayList;
import java.util.List;

public class Farmer_Payment extends AppCompatActivity {
    public static final String FORDER_ID = "farmer_order_id";
    public static final String FORDER_QUANTITY = "farmer_order_quantity";
    public static final String FORDER_TOTAL = "farmer_order_total";

    ListView listviewfarmerpayment;

    List<FOrders> forderList;

    DatabaseReference databaseFarmerOrders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer__payment);

        forderList = new ArrayList<>();

        Intent intent = getIntent();

        String id = intent.getStringExtra(BuyPrice.PRODUCE_ID);
        listviewfarmerpayment = findViewById(R.id.listviewfarmerpayment);

        databaseFarmerOrders = FirebaseDatabase.getInstance().getReference("farmerorders").child(id);

        listviewfarmerpayment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FOrders forders = forderList.get(position);
                Intent intent = new Intent(getApplicationContext(), FarmerMoney.class);

                intent.putExtra(FORDER_ID, forders.getFarmerorderId());
                intent.putExtra(FORDER_QUANTITY, forders.getFarmerquantity());
                intent.putExtra(FORDER_TOTAL, forders.getFarmertotalprice());
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseFarmerOrders.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                forderList.clear();

                for (DataSnapshot fordersSnapshot : dataSnapshot.getChildren()) {
                    FOrders forders = fordersSnapshot.getValue(FOrders.class);

                    forderList.add(forders);
                }

                FarmerOrderList adapter = new FarmerOrderList(Farmer_Payment.this, forderList);
                listviewfarmerpayment.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
