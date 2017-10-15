package com.example.poliakov.barcodescanner;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;
public class BarcodeDatabaseAdapter extends RecyclerView.Adapter<BarcodeDatabaseAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(BarcodeDatabase item);
    }
    private List<BarcodeDatabase> mData;
    private final OnItemClickListener listener;

    public BarcodeDatabaseAdapter(List<BarcodeDatabase> mData, OnItemClickListener listener) {
        this.mData = mData;
        this.listener = listener;
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

        viewHolder.bind(mData.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewName;
        private TextView textViewCountCodes;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewName = (TextView) itemView.findViewById(R.id.barcode_database_list_item_name);
            textViewCountCodes = (TextView) itemView.findViewById(R.id.barcode_database_list_item_count_codes);
        }

        public void bind(final BarcodeDatabase item, final OnItemClickListener listener) {
            textViewName.setText(item.getName());
            textViewCountCodes.setText(item.getBarcodesCount().toString());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }
}
