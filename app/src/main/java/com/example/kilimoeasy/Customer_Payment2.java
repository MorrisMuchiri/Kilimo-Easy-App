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

public class Customer_Payment2 extends AppCompatActivity {
    List<COrders> corderList;

    DatabaseReference databaseCustomerOrders;
    LineChart linechartcorders;
    LineDataSet lineDataSet = new LineDataSet(null, null);
    ArrayList<ILineDataSet> iLineDataSets = new ArrayList<>();
    LineData lineData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer__payment2);

        corderList = new ArrayList<>();

        Intent intent = getIntent();

        String id = intent.getStringExtra(SalePrice_Pay2.PRODUCE_ID);
        linechartcorders = findViewById(R.id.linechartcorders);
        lineDataSet.setLineWidth(4);

        databaseCustomerOrders = FirebaseDatabase.getInstance().getReference("customerorders").child(id);
    }

    @Override
    protected void onStart() {
        super.onStart();

        retrieveData();
    }

    private void retrieveData() {
        databaseCustomerOrders.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Entry> datavals = new ArrayList<Entry>();

                if(dataSnapshot.hasChildren()){
                    for(DataSnapshot myDataSnapshot : dataSnapshot.getChildren()){
                        COrders corders = myDataSnapshot.getValue(COrders.class);
                        int w = 0;
                        int q =0 ;
                        int l = 0;
                        q = Integer.parseInt(String.valueOf(corders.getCustomerquantity()));
                        l = Integer.parseInt(String.valueOf(corders.getCustomerorderlocation()));
                        //w = Integer.parseInt(String.valueOf(corders.getCustomertotalprice()));
                       // int totalprice = q*w;
                        datavals.add(new Entry(l,q));
                    }

                    showChart(datavals);
                }else {
                    linechartcorders.clear();
                    linechartcorders.invalidate();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showChart(ArrayList<Entry> datavals) {
        lineDataSet.setValues(datavals);
        lineDataSet.setLabel("Customer DataSet (Ripe/Unripe) x axis = Location : y axis = Quantity || 1 - Nairobi,2 - Nakuru,3 - Mombasa,4 - Embu,5 - Kisumu");

        //iLineDataSets.clear();
        iLineDataSets.add(lineDataSet);

        lineData = new LineData(iLineDataSets);

        //linechartcorders.clear();
        linechartcorders.setData(lineData);
        linechartcorders.invalidate();
    }
}
