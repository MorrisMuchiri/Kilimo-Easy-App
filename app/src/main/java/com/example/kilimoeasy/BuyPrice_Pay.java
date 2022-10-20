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

public class BuyPrice_Pay extends AppCompatActivity {
    public static final String PRODUCE_NAME = "producename";
    public static final String PRODUCE_ID = "produceid";

    ListView listViewProduce;
    List<Produce> produceList1_pay;
    DatabaseReference databaseProduce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_price__pay);

        listViewProduce = findViewById(R.id.listViewProduce1_pay);
        databaseProduce = FirebaseDatabase.getInstance().getReference("produce_type");
        produceList1_pay = new ArrayList<>();

        //MAKING AN ORDER
        listViewProduce.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Produce produce = produceList1_pay.get (position);

                Intent intent = new Intent(getApplicationContext(), Farmer_Payment.class);

                intent.putExtra(PRODUCE_ID, produce.getType_id());
                intent.putExtra(PRODUCE_NAME, produce.getPname());

                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseProduce.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                produceList1_pay.clear();

                for(DataSnapshot produceSnapshot : dataSnapshot.getChildren()){
                    Produce produce = produceSnapshot.getValue(Produce.class);

                    produceList1_pay.add(produce);
                }

                ProduceList1_pay adapter = new ProduceList1_pay(BuyPrice_Pay.this, produceList1_pay);
                listViewProduce.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
