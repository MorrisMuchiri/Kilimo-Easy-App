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

public class CustomerMoney extends AppCompatActivity {

    TextView customerorder, customerpayamount;
    Spinner customerpayspinner;
    Button customerpaybutton;
    ListView listviewcustomerpayment;

    //pdf
    SimpleDateFormat datePatternformat = new SimpleDateFormat("dd-MM-yyyy hh:mm a");

    List<CMoney> cpaymentList;

    private static final String TAG = "CustomerMoney";

    private TextView customerpaydate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    DatabaseReference databaseCustomerPay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_money);

        customerorder = findViewById(R.id.customerorderpay);
        customerpayamount = findViewById(R.id.customerpayamount);
        customerpaybutton = findViewById(R.id.customerpaybutton);
        customerpayspinner = findViewById(R.id.customerpayspinner);
        listviewcustomerpayment = findViewById(R.id.listviewcustomerpayment);

        cpaymentList = new ArrayList<>();

        Intent intent = getIntent();

        String id = intent.getStringExtra(Customer_Payment.CORDER_ID);
        String quantity = intent.getStringExtra(Customer_Payment.CORDER_QUANTITY);
        String total = intent.getStringExtra(Customer_Payment.CORDER_TOTAL);

        databaseCustomerPay = FirebaseDatabase.getInstance().getReference("customer_payments").child(id);

        customerorder.setText(id);
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

        customerpayamount.setText(total1);

        customerpaydate = findViewById(R.id.customerpaydate);
        customerpaydate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        CustomerMoney.this,
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
                customerpaydate.setText(date);
            }
        };

        //DATE DONE

        customerpaybutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveCPayment();
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

        canvas.drawText("Customer Payment Receipt", 20, 20, paint);
        paint.setTextSize(8.5f);
        canvas.drawText("Thank you for Buying with us!!", 20, 40, paint);
        forLinePaint.setStyle(Paint.Style.STROKE);
        forLinePaint.setPathEffect(new DashPathEffect(new float[]{5,5}, 0));
        forLinePaint.setStrokeWidth(2);
        canvas.drawLine(20, 65, 230, 65, forLinePaint);

        canvas.drawText("Customer Order ID: "+customerorder.getText(), 20, 80, paint);
        canvas.drawLine(20, 90, 230, 90, forLinePaint);

        canvas.drawText("Payment Details: ", 20, 105, paint);

        canvas.drawText("Total Amount = "+customerpayamount.getText()+ "Ksh", 20, 135, paint);
        canvas.drawText("Payment Status = "+customerpayspinner.getSelectedItem().toString(), 20, 145, paint);
        //paint.setTextAlign(Paint.Align.LEFT);

        canvas.drawLine(20, 210, 230, 210, forLinePaint);

        canvas.drawText("Date: "+datePatternformat.format(new Date().getTime()),20, 260, paint);
        canvas.drawText("Payment Method: Cash ", 20, 290, paint);

        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(12f);
        canvas.drawText("Welcome Again!!", canvas.getWidth()/2, 320, paint);

        myPdfDocument.finishPage(myPage);
        File file = new File(this.getExternalFilesDir("/"),"Customer Receipt.pdf");

        try {
            myPdfDocument.writeTo(new FileOutputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }

        myPdfDocument.close();

    }

    private void saveCPayment() {
        String customer_pay_amount = customerpayamount.getText().toString();
        String customer_pay_date = customerpaydate.getText().toString();
        String customer_pay_status = customerpayspinner.getSelectedItem().toString();

        if (!TextUtils.isEmpty(customer_pay_date)){
           String id = databaseCustomerPay.push().getKey();

           CMoney cmoney = new CMoney(id, customer_pay_amount, customer_pay_date, customer_pay_status);

           databaseCustomerPay.child(id).setValue(cmoney);

            Toast.makeText(this, "Payment Made Successfully!!"+"Receipt Downloaded", Toast.LENGTH_SHORT).show();

        }else{ Toast.makeText(this, "Date should not be empty", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseCustomerPay.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                cpaymentList.clear();

                for(DataSnapshot cordersSnapshot: dataSnapshot.getChildren()){
                    CMoney corders = cordersSnapshot.getValue(CMoney.class);

                    cpaymentList.add(corders);
                }

                CustomerMoneyList adapter = new CustomerMoneyList(CustomerMoney.this, cpaymentList);
                listviewcustomerpayment.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
