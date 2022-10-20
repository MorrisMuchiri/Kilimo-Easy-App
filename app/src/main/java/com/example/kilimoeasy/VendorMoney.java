package com.example.kilimoeasy;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class VendorMoney extends AppCompatActivity {
    TextView vendororder, vendorpayamount;
    Spinner vendorpayspinner;
    Button vendorpaybutton;

    ListView listviewvendorpayment;

    //pdf
    SimpleDateFormat datePatternformat = new SimpleDateFormat("dd-MM-yyyy hh:mm a");

    List<VMoney> vpaymentList;

    private static final String TAG = "VendorMoney";

    private TextView vendorpaydate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    DatabaseReference databaseVendorPay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_money);

        vendororder = findViewById(R.id.vendororderpay);
        vendorpayamount = findViewById(R.id.vendorpayamount);
        vendorpaybutton = findViewById(R.id.vendorpaybutton);
        vendorpayspinner = findViewById(R.id.vendorpayspinner);
        listviewvendorpayment = findViewById(R.id.listviewvendorpayment);

        vpaymentList = new ArrayList<>();

        Intent intent = getIntent();

        String id = intent.getStringExtra(Vendor_Payment.VORDER_ID);
        String quantity = intent.getStringExtra(Vendor_Payment.VORDER_QUANTITY);
        String total = intent.getStringExtra(Vendor_Payment.VORDER_TOTAL);

        databaseVendorPay = FirebaseDatabase.getInstance().getReference("vendor_payments").child(id);

        vendororder.setText(id);
        int q = 0;
        int p = 0;

        try {
            q = Integer.parseInt(String.valueOf(quantity));
            p = Integer.parseInt(String.valueOf(total));
        }catch (NumberFormatException e){
            e.printStackTrace();
        }
        int totalprice = q*p;
        String total1 = Integer.toString(totalprice);

        vendorpayamount.setText(total1);

        vendorpaydate = findViewById(R.id.vendorpaydate);
        vendorpaydate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        VendorMoney.this,
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
                vendorpaydate.setText(date);
            }
        };

        //DATE DONE

        vendorpaybutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveVPayment();
                printPDF();
            }
        });
    }

    private void printPDF() {
        PdfDocument myPdfDocument = new PdfDocument();
        Paint paint = new Paint();
        Paint forLinePaint = new Paint();
        PdfDocument.PageInfo myPageInfo = new PdfDocument.PageInfo.Builder(250, 350, 1).create();
        PdfDocument.Page myPage = myPdfDocument.startPage(myPageInfo);
        Canvas canvas = myPage.getCanvas();

        paint.setTextSize(15.5f);
        paint.setColor(Color.rgb(0, 50, 250));

        canvas.drawText("Vendor Payment Receipt", 20, 20, paint);
        paint.setTextSize(8.5f);
        canvas.drawText("Thank you for Buying with us!!", 20, 40, paint);
        forLinePaint.setStyle(Paint.Style.STROKE);
        forLinePaint.setPathEffect(new DashPathEffect(new float[]{5,5}, 0));
        forLinePaint.setStrokeWidth(2);
        canvas.drawLine(20, 65, 230, 65, forLinePaint);

        canvas.drawText("Vendor Order ID: "+vendororder.getText(), 20, 80, paint);
        canvas.drawLine(20, 90, 230, 90, forLinePaint);

        canvas.drawText("Payment Details: ", 20, 105, paint);

        canvas.drawText("Total Amount = "+vendorpayamount.getText()+ "Ksh", 20, 135, paint);
        canvas.drawText("Payment Status = "+vendorpayspinner.getSelectedItem().toString(), 20, 145, paint);
        //paint.setTextAlign(Paint.Align.LEFT);

        canvas.drawLine(20, 210, 230, 210, forLinePaint);

        canvas.drawText("Date: "+datePatternformat.format(new Date().getTime()),20, 260, paint);
        canvas.drawText("Payment Method: Cash ", 20, 290, paint);

        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(12f);
        canvas.drawText("Welcome Again!!", canvas.getWidth()/2, 320, paint);

        myPdfDocument.finishPage(myPage);
        File file = new File(this.getExternalFilesDir("/"),"Vendor Receipt.pdf");

        try {
            myPdfDocument.writeTo(new FileOutputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }

        myPdfDocument.close();

    }

    private void saveVPayment() {
        String vendor_pay_amount = vendorpayamount.getText().toString();
        String vendor_pay_date = vendorpaydate.getText().toString();
        String vendor_pay_status = vendorpayspinner.getSelectedItem().toString();

        if (!TextUtils.isEmpty(vendor_pay_date)){
            String id = databaseVendorPay.push().getKey();

            VMoney vmoney = new VMoney(id, vendor_pay_amount, vendor_pay_date, vendor_pay_status);

            databaseVendorPay.child(id).setValue(vmoney);

            Toast.makeText(this, "Payment Made Successfully!!", Toast.LENGTH_SHORT).show();

        }else{ Toast.makeText(this, "Date should not be empty", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseVendorPay.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                vpaymentList.clear();

                for(DataSnapshot cordersSnapshot: dataSnapshot.getChildren()){
                    VMoney vorders = cordersSnapshot.getValue(VMoney.class);

                    vpaymentList.add(vorders);
                }

                VendorMoneyList adapter = new VendorMoneyList(VendorMoney.this, vpaymentList);
                listviewvendorpayment.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
