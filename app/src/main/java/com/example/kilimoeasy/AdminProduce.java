package com.example.kilimoeasy;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdminProduce extends AppCompatActivity {
    Spinner spinnerFruits;
    EditText editTextName1;
    EditText editTextName2;
    Button buttonAdd;

    DatabaseReference databaseProduce;

    //adding a view
    ListView listViewProduce;

    List<Produce> produceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_produce);

        databaseProduce = FirebaseDatabase.getInstance().getReference("produce_type");

        spinnerFruits = findViewById(R.id.adminprodspinner);
        editTextName1 = findViewById(R.id.saleprice);
        editTextName2 = findViewById(R.id.buyprice);
        buttonAdd = findViewById(R.id.adminprodbutton);


        //adding a view
        listViewProduce = findViewById(R.id.listViewProduce);

        produceList = new ArrayList<>();

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addProduce();
            }
        });

        listViewProduce.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                Produce produce = produceList.get(position);

                showUpdateDialog(produce.getType_id(), produce.getPname());
                return false;
            }
        });
    }

    //adding a view


    @Override
    protected void onStart() {
        super.onStart();

        databaseProduce.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                produceList.clear();

                for(DataSnapshot produceSnapshot : dataSnapshot.getChildren()){
                    Produce produce = produceSnapshot.getValue(Produce.class);

                    produceList.add(produce);
                }

                ProduceList adapter = new ProduceList(AdminProduce.this, produceList);
                listViewProduce.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

//Updating and Deleting

    private void showUpdateDialog(final String produceId, final String pname){

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();

        final View dialogView = inflater.inflate(R.layout.update_dialog, null);

        dialogBuilder.setView(dialogView);

        final EditText editbuyprice = dialogView.findViewById(R.id.edittextchangebuy);
        final EditText editsaleprice = dialogView.findViewById(R.id.edittextchangesale);
        final Spinner editproduce = dialogView.findViewById(R.id.spinner123);
        final Button buttonUpdate = dialogView.findViewById(R.id.buttonUpdate);
        final Button buttonDelete = dialogView.findViewById(R.id.buttonDelete);

        dialogBuilder.setTitle("Updating Produce " +pname);

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fruits = editproduce.getSelectedItem().toString();
                String buyprice = editbuyprice.getText().toString();
                String saleprice = editsaleprice.getText().toString();

                if(TextUtils.isEmpty(buyprice)){
                    editbuyprice.setError("Buy Price Required");
                    return;
                }
                if(TextUtils.isEmpty(saleprice)){
                    editbuyprice.setError("Buy Price Required");
                    return;
                }

                updateProduce(produceId, fruits, saleprice, buyprice);

                alertDialog.dismiss();
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteProduce(produceId);
            }
        });


    }

    private void deleteProduce(String produceId) {
        DatabaseReference drProduce = FirebaseDatabase.getInstance().getReference("produce_type").child(produceId);
        DatabaseReference drProduce2 = FirebaseDatabase.getInstance().getReference("customerorders").child(produceId);

        drProduce.removeValue();
        drProduce2.removeValue();

        Toast.makeText(this, "Produce is deleted", Toast.LENGTH_SHORT).show();
    }


    private boolean updateProduce(String id,String fruits, String buyprice, String saleprice){

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("produce_type").child(id);

        Produce produce = new Produce(id,fruits, buyprice,saleprice);

        databaseReference.setValue(produce);

        Toast.makeText(this, "Produce Updated Successfully", Toast.LENGTH_SHORT).show();

        return true;
    }



    private void addProduce(){
        String saleprice = editTextName1.getText().toString().trim();
        String buyprice = editTextName2.getText().toString().trim();
        String fruits = spinnerFruits.getSelectedItem().toString();

        if(TextUtils.isEmpty(buyprice)){
            Toast.makeText(this, "You should enter a buy price", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!TextUtils.isEmpty(saleprice)) {

            String id = databaseProduce.push().getKey();

            Produce produce = new Produce(id, fruits, saleprice, buyprice);

            databaseProduce.child(id).setValue(produce);

            Toast.makeText(this, "Produce Added", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "You should enter a Sale price", Toast.LENGTH_SHORT).show();
        }

    }
}
