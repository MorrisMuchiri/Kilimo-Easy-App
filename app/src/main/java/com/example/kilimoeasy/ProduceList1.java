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

public class ProduceList1 extends ArrayAdapter<Produce> {
    private Activity context;
    private List<Produce> produceList;

    public ProduceList1(Activity context, List<Produce> produceList){
        super(context, R.layout.produce_list1, produceList);
        this.context = context;
        this.produceList = produceList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.produce_list1, null, true);

        TextView producename = listViewItem.findViewById(R.id.producename2);
        TextView buyprice = listViewItem.findViewById(R.id.buyprice2);

        Produce produce = produceList.get(position);

        producename.setText(produce.getPname());
        buyprice.setText(produce.getBuy_price());

        return  listViewItem;
    }
}
