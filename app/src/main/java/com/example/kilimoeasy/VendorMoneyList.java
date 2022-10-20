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

public class VendorMoneyList extends ArrayAdapter<VMoney>  {

    private Activity context;
    private List<VMoney> vmList;

    public VendorMoneyList(Activity context, List<VMoney> vmList){
        super(context, R.layout.vendor_money_list, vmList);
        this.context = context;
        this.vmList = vmList;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewOrder = inflater.inflate(R.layout.vendor_money_list,null, true);


        TextView vpaymentid = listViewOrder.findViewById(R.id.vpaymentid);
        TextView vpaymentamount = listViewOrder.findViewById(R.id.vpaymentamount);
        TextView vpaymentdate = listViewOrder.findViewById(R.id.vpaymentdate);
        TextView vpaymentstatus = listViewOrder.findViewById(R.id.vpaymentstatus);

        VMoney vmoney = vmList.get(position);

        vpaymentid.setText(vmoney.getVpaymentId());
        vpaymentamount.setText(vmoney.getVpaymentamount());
        vpaymentdate.setText(vmoney.getVpaymentdate());
        vpaymentstatus.setText(vmoney.getVpaymentstatus());

        return listViewOrder;
    }
}
