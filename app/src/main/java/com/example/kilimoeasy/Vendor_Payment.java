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

public class Vendor_Payment extends AppCompatActivity {
    public static final String VORDER_ID = "vendor_order_id";
    public static final String VORDER_QUANTITY = "vendor_order_quantity";
    public static final String VORDER_TOTAL = "vendor_order_total";
    List<VOrders> vorderList;

    DatabaseReference databaseVendorPayment;
    ListView listviewvendorpayment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor__payment);

        vorderList = new ArrayList<>();

        Intent intent = getIntent();

        String id = intent.getStringExtra(SalePrice2.PRODUCE_ID);
        listviewvendorpayment = findViewById(R.id.listviewcustomerpayment3);

        databaseVendorPayment = FirebaseDatabase.getInstance().getReference("vendororders").child(id);

        listviewvendorpayment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                VOrders vorders = vorderList.get(position);
                Intent intent = new Intent(getApplicationContext(), VendorMoney.class);

                intent.putExtra(VORDER_ID, vorders.getVendororderId());
                intent.putExtra(VORDER_QUANTITY, vorders.getVendorquantity());
                intent.putExtra(VORDER_TOTAL, vorders.getVendortotalprice());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseVendorPayment.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                vorderList.clear();

                for(DataSnapshot vordersSnapshot: dataSnapshot.getChildren()){
                    VOrders vorders = vordersSnapshot.getValue(VOrders.class);

                    vorderList.add(vorders);
                }

                VendorOrderList adapter = new VendorOrderList(Vendor_Payment.this, vorderList);
                listviewvendorpayment.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
