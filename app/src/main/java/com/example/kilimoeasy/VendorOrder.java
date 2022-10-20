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
import android.widget.ArrayAdapter;
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

public class VendorOrder extends AppCompatActivity {

    TextView vendorproducename, vendordate, vendortotalprice;
    Spinner vendorspinner, vendororderlocation;
    EditText vendorquantity;

    Button vendororderbutton;

    ListView listviewvendororder;

    List<VOrders> vorderList;

    DatabaseReference databaseVendorOrders;

    private static final String TAG = "VendorOrder";

    private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_order);

        mDisplayDate = findViewById(R.id.vendordate);

        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        VendorOrder.this,
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

        vendorproducename = findViewById(R.id.vendorproducename);
        vendordate = findViewById(R.id.vendordate);
        vendorspinner = findViewById(R.id.vendorspinner);
        vendorquantity = findViewById(R.id.vendorquantity);
        vendororderlocation = findViewById(R.id.vendororderlocation);
        Integer [] items = new Integer[]{1,2,3,4,5};
        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, items);
        vendororderlocation.setAdapter(adapter);

        vendortotalprice = findViewById(R.id.vendortotalprice);

        vendororderbutton = findViewById(R.id.vendororderbutton);
        listviewvendororder = findViewById(R.id.listviewvendororder);

        vorderList = new ArrayList<>();

        Intent intent = getIntent();

        String id = intent.getStringExtra(SalePrice2.PRODUCE_ID);
        String name = intent.getStringExtra(SalePrice2.PRODUCE_NAME);
        String price = intent.getStringExtra(SalePrice2.PRODUCE_SALEPRICE);

        vendorproducename.setText(name);
        vendortotalprice.setText(price);

        databaseVendorOrders = FirebaseDatabase.getInstance().getReference("vendororders").child(id);

        vendororderbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveOrders();
            }
        });

        //UPDATING
        listviewvendororder.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                VOrders vorders = vorderList.get(position);

                showUpdateDialog(vorders.getVendororderId(), vorders.getVendorproduce());
                return false;
            }
        });
    }

    private void showUpdateDialog(String vendororderId, String vendorproduce) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();

        final View dialogView = inflater.inflate(R.layout.update_vendororder,null);

        dialogBuilder.setView(dialogView);

        final TextView editproduce = dialogView.findViewById(R.id.vupdateproducename);

        Intent intent = getIntent();

        String name = intent.getStringExtra(SalePrice2.PRODUCE_NAME);
        editproduce.setText(name);

        final Button buttonDelete = dialogView.findViewById(R.id.vdeletebutton);
        dialogBuilder.setTitle("Updating Orders: "+vendorproduce);

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            Intent intent = getIntent();

            String id = intent.getStringExtra(SalePrice2.PRODUCE_ID);
            @Override
            public void onClick(View v) {
                deleteorder(id);
            }
        });
    }

    private void deleteorder(String id) {
        DatabaseReference vOrders = FirebaseDatabase.getInstance().getReference("vendororders").child(id);

        vOrders.removeValue();

        Toast.makeText(this, "Order Deleted", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseVendorOrders.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                vorderList.clear();

                for(DataSnapshot vordersSnapshot: dataSnapshot.getChildren()){
                    VOrders vorders = vordersSnapshot.getValue(VOrders.class);

                    vorderList.add(vorders);
                }

                VendorOrderList adapter = new VendorOrderList(VendorOrder.this, vorderList);
                listviewvendororder.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void saveOrders(){
        int w = 0;

        String uploaddate = vendordate.getText().toString();
        String orderquantity = vendorquantity.getText().toString();
        String type = vendorspinner.getSelectedItem().toString();
        String totalprice = vendortotalprice.getText().toString();
        String customerlocation = vendororderlocation.getSelectedItem().toString();
        String customerproduce = vendorproducename.getText().toString();

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
        if (w > 80){
            Toast.makeText(this, "Current Maximum Quantity is 80 Kgs!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(customerlocation)){
            Toast.makeText(this, "Location should not be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!TextUtils.isEmpty(customerlocation)){
            String id = databaseVendorOrders.push().getKey();

            VOrders order = new VOrders(id, orderquantity, uploaddate, totalprice, type, customerlocation, customerproduce);

            databaseVendorOrders.child(id).setValue(order);
            Toast.makeText(this, "Order saved successfully", Toast.LENGTH_SHORT).show();

        }else{
            Toast.makeText(this, "Location should not be empty", Toast.LENGTH_SHORT).show();
        }
    }
}
