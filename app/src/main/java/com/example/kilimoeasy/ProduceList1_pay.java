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

public class ProduceList1_pay extends ArrayAdapter<Produce> {
    private Activity context;
    private List<Produce> produceList;

    public ProduceList1_pay(Activity context, List<Produce> produceList){
        super(context, R.layout.produce_list1_pay, produceList);
        this.context = context;
        this.produceList = produceList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.produce_list1_pay, null, true);

        TextView producename = listViewItem.findViewById(R.id.producename2pay);

        Produce produce = produceList.get(position);

        producename.setText(produce.getPname());

        return  listViewItem;
    }
}
