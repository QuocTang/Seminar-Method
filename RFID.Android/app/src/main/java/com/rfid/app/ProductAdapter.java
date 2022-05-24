package com.rfid.app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ProductAdapter extends BaseAdapter {

    private RFIDReportActivity context;
    private int layout;
    private List<Products> productList;

    public ProductAdapter(RFIDReportActivity context, int layout, List<Products> productList) {
        this.context = context;
        this.layout = layout;
        this.productList = productList;
    }

    @Override
    public int getCount() {
        return productList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class ViewHolder{
        TextView txtTime, txtItemCode, txtTheoryValue, txtRealValue, txtGap;
        ImageView imgDelete, imgEdit;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        ViewHolder holder;
        if(view == null){
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layout, null);
            holder.txtTime = view.findViewById(R.id.time);
            holder.txtItemCode = (TextView) view.findViewById(R.id.tvItemCode);
            holder.txtRealValue = (TextView) view.findViewById(R.id.tvReal);
            holder.txtTheoryValue = (TextView) view.findViewById(R.id.tvTheory);
            holder.txtGap = (TextView) view.findViewById(R.id.tvGap);
            holder.imgDelete = (ImageView) view.findViewById(R.id.ivDelete);
            holder.imgEdit = (ImageView) view.findViewById(R.id.ivEdit);
            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }

        Products products = productList.get(position);

        holder.txtTime.setText(products.getTime());
        holder.txtItemCode.setText(products.getItem_code());
        holder.txtRealValue.setText("Real_value: " + products.getReal_value());
        holder.txtTheoryValue.setText("Theory_value: " + products.getTheory_value());
        holder.txtGap.setText("Gap: " + products.getGap());

        //Delete and Update
//        holder.imgEdit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(context, UpdateProductsActivity.class);
//                intent.putExtra("dataProducts", products);
//                context.startActivities(new Intent[]{intent});
//            }
//        });

        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfirmDelete(products.getItem_code(), products.getItem_code());
            }
        });

        return view;
    }

    private void ConfirmDelete(String item_code, String id){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Do you want to delete " + item_code + "?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                context.DeleteProducts(item_code);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }
}
