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

public class ProduceList extends ArrayAdapter <Produce> {

    private Activity context;
    private List<Produce> produceList;

    public ProduceList(Activity context, List<Produce> produceList){
        super(context, R.layout.producelist_layout, produceList);
        this.context = context;
        this.produceList = produceList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.producelist_layout, null, true);

        TextView producename = listViewItem.findViewById(R.id.producename);
        TextView saleprice = listViewItem.findViewById(R.id.saleprice1);
        TextView buyprice = listViewItem.findViewById(R.id.buyprice1);

        Produce produce = produceList.get(position);

        producename.setText(produce.getPname());
        saleprice.setText(produce.getSale_price());
        buyprice.setText(produce.getBuy_price());

        return  listViewItem;
    }
}
