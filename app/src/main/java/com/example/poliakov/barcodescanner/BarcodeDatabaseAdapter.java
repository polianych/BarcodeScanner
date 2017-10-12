package com.example.poliakov.barcodescanner;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;
public class BarcodeDatabaseAdapter extends RecyclerView.Adapter<BarcodeDatabaseAdapter.ViewHolder> {

    private List<BarcodeDatabase> mData;

    public BarcodeDatabaseAdapter(List<BarcodeDatabase> mData) {
        this.mData = mData;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.barcode_database_list_item, null);
        ViewHolder viewHolder = new BarcodeDatabaseAdapter.ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.textViewName.setText(mData.get(position).getName());
        viewHolder.textViewCountCodes.setText(mData.get(position).getBarcodesCount().toString());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewName;
        public TextView textViewCountCodes;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewName = (TextView) itemView.findViewById(R.id.barcode_database_list_item_name);
            textViewCountCodes = (TextView) itemView.findViewById(R.id.barcode_database_list_item_count_codes);
        }
    }
}
