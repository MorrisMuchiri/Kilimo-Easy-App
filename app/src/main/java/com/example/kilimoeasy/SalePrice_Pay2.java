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

public class SalePrice_Pay2 extends AppCompatActivity {
    public static final String PRODUCE_NAME = "producename";
    public static final String PRODUCE_ID = "produceid";

    ListView listViewProduce;
    List<Produce> produceList2;
    DatabaseReference databaseProduce;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale_price__pay2);

        listViewProduce = findViewById(R.id.listViewProducePay2);
        databaseProduce = FirebaseDatabase.getInstance().getReference("produce_type");
        produceList2 = new ArrayList<>();

        //CHECKING ORDERS
        listViewProduce.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Produce produce = produceList2.get (position);

                Intent intent = new Intent(getApplicationContext(), Customer_Payment2.class);

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

                produceList2.clear();

                for(DataSnapshot produceSnapshot : dataSnapshot.getChildren()){
                    Produce produce = produceSnapshot.getValue(Produce.class);

                    produceList2.add(produce);
                }

                ProduceList2_Pay adapter = new ProduceList2_Pay(SalePrice_Pay2.this, produceList2);
                listViewProduce.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
