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

public class FarmerMoneyList extends ArrayAdapter<FMoney> {

    private Activity context;
    private List<FMoney> fmList;

    public FarmerMoneyList(Activity context, List<FMoney> fmList){
        super(context, R.layout.farmer_money_list, fmList);
        this.context = context;
        this.fmList = fmList;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewOrder = inflater.inflate(R.layout.farmer_money_list,null, true);


        TextView fpaymentid = listViewOrder.findViewById(R.id.fpaymentid);
        TextView fpaymentamount = listViewOrder.findViewById(R.id.fpaymentamount);
        TextView fpaymentdate = listViewOrder.findViewById(R.id.fpaymentdate);
        TextView fpaymentstatus = listViewOrder.findViewById(R.id.fpaymentstatus);

        FMoney fmoney = fmList.get(position);

        fpaymentid.setText(fmoney.getFpaymentId());
        fpaymentamount.setText(fmoney.getFpaymentamount());
        fpaymentdate.setText(fmoney.getFpaymentdate());
        fpaymentstatus.setText(fmoney.getFpaymentstatus());

        return listViewOrder;
    }
}
