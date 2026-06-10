package com.zaaach.citypicker.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zaaach.citypicker.R;
import com.zaaach.citypicker.model.Province;

import java.util.List;

/**
 * 省份列表适配器
 */
public class ProvinceListAdapter extends RecyclerView.Adapter<ProvinceListAdapter.ViewHolder> {
    private Context mContext;
    private List<Province> mData;
    private int selectedIndex = 0;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public ProvinceListAdapter(Context context, List<Province> data) {
        mContext = context;
        mData = data;
    }

    public void setSelectedIndex(int index) {
        selectedIndex = index;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.cp_item_province, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Province province = mData.get(position);
        holder.tvProvince.setText(province.getName());

        if (position == selectedIndex) {
            holder.tvProvince.setBackgroundColor(Color.WHITE);
            holder.tvProvince.setTextColor(Color.parseColor("#00796B"));
            holder.indicator.setVisibility(View.VISIBLE);
        } else {
            holder.tvProvince.setBackgroundColor(Color.parseColor("#F5F5F5"));
            holder.tvProvince.setTextColor(Color.parseColor("#333333"));
            holder.indicator.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvProvince;
        View indicator;

        ViewHolder(View itemView) {
            super(itemView);
            tvProvince = itemView.findViewById(R.id.tv_province);
            indicator = itemView.findViewById(R.id.indicator);
        }
    }
}
