package com.example.kilimoeasy;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class CustomerMoneyList extends ArrayAdapter<CMoney> {
    private Activity context;
    private List<CMoney> cmList;

    public CustomerMoneyList(Activity context, List<CMoney> cmList){
        super(context, R.layout.customer_money_list, cmList);
        this.context = context;
        this.cmList = cmList;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewOrder = inflater.inflate(R.layout.customer_money_list,null, true);


        TextView cpaymentid = listViewOrder.findViewById(R.id.cpaymentid);
        TextView cpaymentamount = listViewOrder.findViewById(R.id.cpaymentamount);
        TextView cpaymentdate = listViewOrder.findViewById(R.id.cpaymentdate);
        TextView cpaymentstatus = listViewOrder.findViewById(R.id.cpaymentstatus);

        CMoney cmoney = cmList.get(position);

        cpaymentid.setText(cmoney.getCpaymentId());
        cpaymentamount.setText(cmoney.getCpaymentamount());
        cpaymentdate.setText(cmoney.getCpaymentdate());
        cpaymentstatus.setText(cmoney.getCpaymentstatus());

        return listViewOrder;
    }
}
