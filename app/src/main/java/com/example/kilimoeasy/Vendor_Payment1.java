package com.example.kilimoeasy;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Vendor_Payment1 extends AppCompatActivity {
    List<VOrders> vorderList;

    DatabaseReference databaseVendorPayment;
    LineChart linechartvorders;
    LineDataSet lineDataSet = new LineDataSet(null, null);
    ArrayList<ILineDataSet> iLineDataSets = new ArrayList<>();
    LineData lineData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor__payment1);

        Intent intent = getIntent();

        String id = intent.getStringExtra(SalePrice2.PRODUCE_ID);
        linechartvorders = findViewById(R.id.linechartvorders);
        lineDataSet.setLineWidth(4);

        vorderList = new ArrayList<>();
        databaseVendorPayment = FirebaseDatabase.getInstance().getReference("vendororders").child(id);
    }
    @Override
    protected void onStart() {
        super.onStart();

        retrieveData();
    }

    private void retrieveData() {
        databaseVendorPayment.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Entry> datavals = new ArrayList<Entry>();

                if(dataSnapshot.hasChildren()){
                    for(DataSnapshot myDataSnapshot : dataSnapshot.getChildren()){
                        VOrders vorders = myDataSnapshot.getValue(VOrders.class);
                        int l = 0;
                        int q =0 ;
                        q = Integer.parseInt(String.valueOf(vorders.getVendorquantity()));
                        l = Integer.parseInt(String.valueOf(vorders.getVendororderlocation()));
                        //w = Integer.parseInt(String.valueOf(vorders.getVendortotalprice()));
                        //int totalprice = q*w;
                        datavals.add(new Entry(l,q));
                    }

                    showChart(datavals);
                }else {
                    linechartvorders.clear();
                    linechartvorders.invalidate();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showChart(ArrayList<Entry> datavals) {
        lineDataSet.setValues(datavals);
        lineDataSet.setLabel("Vendor DataSet(Only Unripe) x axis = Location : y axis = Quantity || 1 - Nairobi,2 - Nakuru,3 - Mombasa,4 - Embu,5 - Kisumu");

        //iLineDataSets.clear();
        iLineDataSets.add(lineDataSet);

        lineData = new LineData(iLineDataSets);

        //linechartcorders.clear();
        linechartvorders.setData(lineData);
        linechartvorders.invalidate();
    }
}
