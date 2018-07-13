package com.example.ibwev.testprint;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by ibwev on 9/26/2017.
 */

public class TransactionAdapter extends ArrayAdapter {

    SimpleDateFormat simpleDateFormat;

    List<Transaction> list = new ArrayList();

    public TransactionAdapter(Context context, int resource) {
        super(context, resource);
    }

    public void add(Transaction object) {
        super.add(object);
        list.add(object);  //https://www.youtube.com/watch?v=KSX4zIhiZlM 19:29
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //https://stackoverflow.com/questions/5894818/how-to-sort-arraylistlong-in-java-in-decreasing-order
        Collections.sort(list, new Comparator<Transaction>() {
            public int compare(Transaction o1, Transaction o2) {
                return o2.compareTo(o1);
            }
        });
        View row;
        row = convertView;
        TransactionAdapter.TransactionHolder transactionHolder;

        if(row == null){
            LayoutInflater layoutInflater = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.row_layout_amount, parent, false);
            transactionHolder = new TransactionAdapter.TransactionHolder();
            transactionHolder.first = (TextView)row.findViewById(R.id.addressRA);
            transactionHolder.second = (TextView)row.findViewById(R.id.fNameRA);
            transactionHolder.third = (TextView)row.findViewById(R.id.lNameRA);
            transactionHolder.fourth = (TextView)row.findViewById(R.id.owedRA);
            row.setTag(transactionHolder);
        }else{
            transactionHolder = (TransactionAdapter.TransactionHolder)row.getTag();
        }

        Transaction t = (Transaction) this.getItem(position);
        transactionHolder.first.setText(t.stringDatePaid());

        if (t.getPrePay()) {
            transactionHolder.second.setText(t.getTransactionType());
        }else if(t.getTransactionType().equals("rent")) {
            transactionHolder.second.setText(t.stringRentToDate());
            transactionHolder.third.setText(Integer.toString(t.getOwedToRentTo()));

        }else{
            transactionHolder.second.setText(t.getTransactionType());
            transactionHolder.third.setText(t.getDescription());
        }

        if (t.getTransactionType().equals("bill"))
            transactionHolder.fourth.setTextColor(Color.RED);
        else
            transactionHolder.fourth.setTextColor(Color.BLACK);
        transactionHolder.fourth.setText(Integer.toString(t.getAmountPaid()));
        //contactHolder.tvAmount.setText("try");

        return row;
    }

    private static class TransactionHolder
    {
        TextView first, second, third, fourth;
    }
}
