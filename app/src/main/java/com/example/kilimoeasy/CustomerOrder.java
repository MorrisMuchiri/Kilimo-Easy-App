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

public class CustomerOrder extends AppCompatActivity {

    TextView customerproducename, customerdate, customertotalprice;
    Spinner customerspinner, customerorderlocation;
    EditText customerquantity;

    Button customerorderbutton;

    ListView listviewcustomerorder;

    List<COrders> corderList;
    //List<Produce> produceList;

    DatabaseReference databaseCustomerOrders;

    private static final String TAG = "CustomerOrder";

    private TextView mDisplayDate1;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_order);

        mDisplayDate1 = findViewById(R.id.customerdate);

        mDisplayDate1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        CustomerOrder.this,
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
                mDisplayDate1.setText(date);
            }
        };

        //DATE DONE

        customerproducename = findViewById(R.id.customerproducename);
        customerdate = findViewById(R.id.customerdate);
        customerspinner = findViewById(R.id.customerspinner);
        customerquantity = findViewById(R.id.customerquantity);
        customerorderlocation = findViewById(R.id.customerorderlocation);
        Integer [] items = new Integer[]{1,2,3,4,5};
        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, items);
        customerorderlocation.setAdapter(adapter);

        customertotalprice = findViewById(R.id.customertotalprice);

        customerorderbutton = findViewById(R.id.customerorderbutton);
        listviewcustomerorder = findViewById(R.id.listviewcustomerorder);

        corderList = new ArrayList<>();

        Intent intent = getIntent();

        String id = intent.getStringExtra(SalePrice.PRODUCE_ID);
        String name = intent.getStringExtra(SalePrice.PRODUCE_NAME);
        String price = intent.getStringExtra(SalePrice.PRODUCE_SALEPRICE);

        customerproducename.setText(name);
        customertotalprice.setText(price);

        databaseCustomerOrders = FirebaseDatabase.getInstance().getReference("customerorders").child(id);

        customerorderbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveOrders();
            }
        });

        //UPDATING
        listviewcustomerorder.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                COrders corders = corderList.get(position);

                showUpdateDialog(corders.getCustomerorderId(), corders.getCustomerproduce());
                return false;
            }
        });
    }

    private void showUpdateDialog(final String customerorderId, String produceName){

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();

        final View dialogView = inflater.inflate(R.layout.update_customerorder,null);

        dialogBuilder.setView(dialogView);

        final TextView editproduce = dialogView.findViewById(R.id.cupdateproducename);
        final Spinner editlocation = dialogView.findViewById(R.id.cupdatelocation);
        Integer [] items = new Integer[]{1,2,3,4,5};
        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, items);
        editlocation.setAdapter(adapter);

        final Spinner editproducetype = dialogView.findViewById(R.id.cupdatetype);
        final EditText editquantity = dialogView.findViewById(R.id.cupdatequantity);
        final TextView edittotal = dialogView.findViewById(R.id.cupdatetotal);
        final TextView editdate = dialogView.findViewById(R.id.cupdatedate);

        Intent intent = getIntent();
        String name = intent.getStringExtra(SalePrice.PRODUCE_NAME);
        String price = intent.getStringExtra(SalePrice.PRODUCE_SALEPRICE);

        editproduce.setText(name);
        edittotal.setText(price);

        final TextView mDisplayDate;
        mDisplayDate = dialogView.findViewById(R.id.cupdatedate);

        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        CustomerOrder.this,
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

        final Button buttonUpdate = dialogView.findViewById(R.id.cupdatebutton);
        final Button buttonDelete = dialogView.findViewById(R.id.cdeletebutton);

        dialogBuilder.setTitle("Updating Orders: "+produceName);

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String quantity = editquantity.getText().toString();
                String date = editdate.getText().toString();
                String total = edittotal.getText().toString();
                String producetype = editproducetype.getSelectedItem().toString();
                String location = editlocation.getSelectedItem().toString();
                String producename = editproduce.getText().toString();

                if(TextUtils.isEmpty(quantity)){
                    editquantity.setError("Quantity Required");
                    return;
                }

                updateCustomerOrder(customerorderId,quantity,date,total,producetype,location,producename);

                alertDialog.dismiss();
            }
        });
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            Intent intent = getIntent();

            String id = intent.getStringExtra(SalePrice.PRODUCE_ID);
            @Override
            public void onClick(View v) {
                deleteorder(id);
            }
        });

    }

    private void deleteorder(String id) {
        DatabaseReference drOrder = FirebaseDatabase.getInstance().getReference("customerorders").child(id);
        drOrder.removeValue();

        Toast.makeText(this, "Order Deleted", Toast.LENGTH_SHORT).show();
    }

    private boolean updateCustomerOrder(String orderid, String quantity, String date, String total, String producetype, String location, String producename){

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("customerorders").child(orderid);
        COrders corders = new COrders(orderid, quantity, date, total, producetype, location, producename);
        
        databaseReference.setValue(corders);

        Toast.makeText(this, "Orders Updated!!", Toast.LENGTH_SHORT).show();

        return true;
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

                CustomerOrderList adapter = new CustomerOrderList(CustomerOrder.this, corderList);
                listviewcustomerorder.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void saveOrders(){
        int w = 0;

        String uploaddate = customerdate.getText().toString();
        String orderquantity = customerquantity.getText().toString();
        String type = customerspinner.getSelectedItem().toString();
        String totalprice = customertotalprice.getText().toString();
        String customerlocation = customerorderlocation.getSelectedItem().toString();
        String customerproduce = customerproducename.getText().toString();

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
        if (!TextUtils.isEmpty(customerlocation)){
            String id = databaseCustomerOrders.push().getKey();

            COrders order = new COrders(id, orderquantity, uploaddate, totalprice, type, customerlocation, customerproduce);

            databaseCustomerOrders.child(id).setValue(order);
            Toast.makeText(this, "Order saved successfully", Toast.LENGTH_SHORT).show();

        }else{
            Toast.makeText(this, "Location should not be empty", Toast.LENGTH_SHORT).show();
        }
    }
}
