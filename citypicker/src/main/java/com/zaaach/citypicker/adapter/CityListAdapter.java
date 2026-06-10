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
import com.zaaach.citypicker.model.City;

import java.util.List;

/**
 * 城市列表适配器
 */
public class CityListAdapter extends RecyclerView.Adapter<CityListAdapter.ViewHolder> {
    private Context mContext;
    private List<City> mData;
    private int selectedIndex = 0;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public CityListAdapter(Context context, List<City> data) {
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
        View view = LayoutInflater.from(mContext).inflate(R.layout.cp_item_city, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        City city = mData.get(position);
        holder.tvCity.setText(city.getName());

        if (position == selectedIndex) {
            holder.tvCity.setTextColor(Color.parseColor("#00796B"));
            holder.tvCity.setTypeface(null, android.graphics.Typeface.BOLD);
            holder.ivCheck.setVisibility(View.VISIBLE);
        } else {
            holder.tvCity.setTextColor(Color.parseColor("#333333"));
            holder.tvCity.setTypeface(null, android.graphics.Typeface.NORMAL);
            holder.ivCheck.setVisibility(View.GONE);
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
        TextView tvCity;
        TextView ivCheck;

        ViewHolder(View itemView) {
            super(itemView);
            tvCity = itemView.findViewById(R.id.tv_city);
            ivCheck = itemView.findViewById(R.id.iv_check);
        }
    }
}
