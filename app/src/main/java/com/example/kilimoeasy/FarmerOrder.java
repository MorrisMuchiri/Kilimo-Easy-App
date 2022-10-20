package com.example.kilimoeasy;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FarmerOrder extends AppCompatActivity {
    TextView farmerproducename, farmerdate, farmertotalprice;
    Spinner farmerspinner, farmerorderlocation;
    EditText farmerquantity;

    Button farmerorderbutton;

    ListView listviewfarmerorder;

    List<FOrders> forderList;

    DatabaseReference databaseFarmerOrders;

    private static final String TAG = "FarmerOrder";

    private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer_order);

        mDisplayDate = findViewById(R.id.farmerdate);

        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        FarmerOrder.this,
                        android.R.style.Theme_DeviceDefault_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day
                );
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDataSet: dd/mm/yyy: " + day+ "/" + month + "/" + year);

                String date = day + "/" + month + "/" + year;
                mDisplayDate.setText(date);
            }
        };

        //DATE DONE

        farmerproducename = findViewById(R.id.farmerproducename);
        farmerdate = findViewById(R.id.farmerdate);
        farmerspinner = findViewById(R.id.farmerspinner);
        farmerquantity = findViewById(R.id.farmerquantity);
        farmerorderlocation = findViewById(R.id.farmerorderlocation);
        farmertotalprice = findViewById(R.id.farmertotalprice);

        farmerorderbutton = findViewById(R.id.farmerorderbutton);
        listviewfarmerorder = findViewById(R.id.listviewfarmerorder);

        forderList = new ArrayList<>();

        Intent intent = getIntent();

        String id = intent.getStringExtra(BuyPrice.PRODUCE_ID);
        String name = intent.getStringExtra(BuyPrice.PRODUCE_NAME);
        String price = intent.getStringExtra(BuyPrice.PRODUCE_BUYPRICE);

        farmerproducename.setText(name);
        farmertotalprice.setText(price);

        databaseFarmerOrders = FirebaseDatabase.getInstance().getReference("farmerorders").child(id);

        farmerorderbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveOrders();
            }
        });

        //UPDATING
        listviewfarmerorder.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                FOrders forders = forderList.get(position);

                showUpdateDialog(forders.getFarmerorderId(), forders.getFarmerproduce());
                return false;
            }
        });
    }

    private void showUpdateDialog(String farmerorderId, String farmerproduce) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();

        final View dialogView = inflater.inflate(R.layout.update_farmerorder,null);

        dialogBuilder.setView(dialogView);

        final TextView editproduce = dialogView.findViewById(R.id.fupdateproducename);
        Intent intent = getIntent();

        String name = intent.getStringExtra(BuyPrice.PRODUCE_NAME);
        editproduce.setText(name);

        final Button buttonDelete = dialogView.findViewById(R.id.fdeletebutton);
        dialogBuilder.setTitle("Updating Orders: "+farmerproduce);

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            Intent intent = getIntent();

            String id = intent.getStringExtra(BuyPrice.PRODUCE_ID);
            @Override
            public void onClick(View v) {
                deleteorder(id);
            }
        });
    }

    private void deleteorder(String id) {
        DatabaseReference fOrders = FirebaseDatabase.getInstance().getReference("farmerorders").child(id);

        fOrders.removeValue();

        Toast.makeText(this, "Order Deleted", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseFarmerOrders.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                forderList.clear();

                for(DataSnapshot fordersSnapshot: dataSnapshot.getChildren()){
                    FOrders forders = fordersSnapshot.getValue(FOrders.class);

                    forderList.add(forders);
                }

                FarmerOrderList adapter = new FarmerOrderList(FarmerOrder.this, forderList);
                listviewfarmerorder.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void saveOrders(){
        int w = 0;

        String uploaddate = farmerdate.getText().toString();
        String orderquantity = farmerquantity.getText().toString();
        String type = farmerspinner.getSelectedItem().toString();
        String totalprice = farmertotalprice.getText().toString();
        String customerlocation = farmerorderlocation.getSelectedItem().toString();
        String customerproduce = farmerproducename.getText().toString();

        if (TextUtils.isEmpty(orderquantity)){
            Toast.makeText(this, "Quantity should not be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        w = Integer.parseInt(String.valueOf(orderquantity));

        if (TextUtils.isEmpty(uploaddate)){
            Toast.makeText(this, "Date should not be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        if (w <= 0){
            Toast.makeText(this, "Start Quantity above 0!!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!TextUtils.isEmpty(customerlocation)){
            String id = databaseFarmerOrders.push().getKey();

            FOrders order = new FOrders(id, orderquantity, uploaddate, totalprice, type, customerlocation, customerproduce);

            databaseFarmerOrders.child(id).setValue(order);
            Toast.makeText(this, "Order saved successfully", Toast.LENGTH_SHORT).show();

        }else{
            Toast.makeText(this, "Location should not be empty", Toast.LENGTH_SHORT).show();
        }
    }
}
