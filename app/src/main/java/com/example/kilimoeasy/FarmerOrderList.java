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

public class FarmerOrderList extends ArrayAdapter<FOrders> {

    private Activity context;
    private List<FOrders> foList;

    public FarmerOrderList(Activity context, List<FOrders> foList){
        super(context, R.layout.farmerorder_layout, foList);
        this.context = context;
        this.foList = foList;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewOrder = inflater.inflate(R.layout.farmerorder_layout,null, true);

        int p = 0;
        int q = 0;

        TextView foproduce = listViewOrder.findViewById(R.id.foproduce);
        TextView fotype = listViewOrder.findViewById(R.id.fotype);
        TextView foquantity = listViewOrder.findViewById(R.id.foquantity);
        TextView fototalprice = listViewOrder.findViewById(R.id.fototalprice);
        TextView fodate = listViewOrder.findViewById(R.id.fodate);
        TextView folocation = listViewOrder.findViewById(R.id.folocation);

        FOrders forders = foList.get(position);

        p = Integer.parseInt(String.valueOf(forders.getFarmerquantity()));
        q = Integer.parseInt(String.valueOf(forders.getFarmertotalprice()));

        int totalprice = q*p;
        String total = Integer.toString(totalprice);
        foproduce.setText(forders.getFarmerproduce());
        fotype.setText(forders.getFarmerspinner());
        foquantity.setText(forders.getFarmerquantity());
        fototalprice.setText(total);
        fodate.setText(forders.getFarmerdate());
        folocation.setText(forders.getFarmerorderlocation());

        return listViewOrder;
    }
}
