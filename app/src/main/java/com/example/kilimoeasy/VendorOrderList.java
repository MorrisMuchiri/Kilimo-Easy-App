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

public class VendorOrderList extends ArrayAdapter<VOrders> {
    private Activity context;
    private List<VOrders> voList;

    public VendorOrderList(Activity context, List<VOrders> voList){
        super(context, R.layout.vendororder_layout, voList);
        this.context = context;
        this.voList = voList;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewOrder = inflater.inflate(R.layout.vendororder_layout,null, true);

        int p = 0;
        int q = 0;

        TextView voproduce = listViewOrder.findViewById(R.id.voproduce);
        TextView votype = listViewOrder.findViewById(R.id.votype);
        TextView voquantity = listViewOrder.findViewById(R.id.voquantity);
        TextView vototalprice = listViewOrder.findViewById(R.id.vototalprice);
        TextView vodate = listViewOrder.findViewById(R.id.vodate);
        TextView volocation = listViewOrder.findViewById(R.id.volocation);

        VOrders vorders = voList.get(position);

        p = Integer.parseInt(String.valueOf(vorders.getVendorquantity()));
        q = Integer.parseInt(String.valueOf(vorders.getVendortotalprice()));

        int totalprice = q*p;
        String total = Integer.toString(totalprice);
        voproduce.setText(vorders.getVendorproduce());
        votype.setText(vorders.getVendorspinner());
        voquantity.setText(vorders.getVendorquantity());
        vototalprice.setText(total);
        vodate.setText(vorders.getVendordate());
        volocation.setText(vorders.getVendororderlocation());

        return listViewOrder;
    }
}
