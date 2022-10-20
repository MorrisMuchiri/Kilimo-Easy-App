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

public class CustomerOrderList extends ArrayAdapter<COrders> {

    private Activity context;
    private List<COrders> coList;

    public CustomerOrderList(Activity context, List<COrders> coList){
        super(context, R.layout.customerorder_layout, coList);
        this.context = context;
        this.coList = coList;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewOrder = inflater.inflate(R.layout.customerorder_layout,null, true);

        int p = 0;
        int q = 0;

        TextView coproduce = listViewOrder.findViewById(R.id.coproduce);
        TextView cotype = listViewOrder.findViewById(R.id.cotype);
        TextView coquantity = listViewOrder.findViewById(R.id.coquantity);
        TextView cototalprice = listViewOrder.findViewById(R.id.cototalprice);
        TextView codate = listViewOrder.findViewById(R.id.codate);
        TextView colocation = listViewOrder.findViewById(R.id.colocation);

        COrders corders = coList.get(position);

        p = Integer.parseInt(String.valueOf(corders.getCustomerquantity()));
        q = Integer.parseInt(String.valueOf(corders.getCustomertotalprice()));

        int totalprice = q*p;
        String total = Integer.toString(totalprice);
        coproduce.setText(corders.getCustomerproduce());
        cotype.setText(corders.getCustomerspinner());
        coquantity.setText(corders.getCustomerquantity());
        cototalprice.setText(total);
        codate.setText(corders.getCustomerdate());
        colocation.setText(corders.getCustomerorderlocation());

        return listViewOrder;
    }
}
