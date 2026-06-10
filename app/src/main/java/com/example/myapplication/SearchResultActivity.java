package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.api.Api;
import com.example.myapplication.bean.GoodsBean;
import com.example.myapplication.util.HttpUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchResultActivity extends AppCompatActivity {

    private static final String TAG = "SearchResultActivity";
    public static final String EXTRA_KEYWORD = "keyword";

    private EditText etSearch;
    private RecyclerView rvGoods;
    private LinearLayout llEmpty;
    private LinearLayout llLoading;

    private String keyword;
    private List<GoodsBean> goodsList = new ArrayList<>();
    private GoodsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        keyword = getIntent().getStringExtra(EXTRA_KEYWORD);
        if (keyword == null || keyword.isEmpty()) {
            finish();
            return;
        }

        initViews();
        initListeners();
        searchGoods();
    }

    private void initViews() {
        etSearch = findViewById(R.id.et_search);
        rvGoods = findViewById(R.id.rv_goods);
        llEmpty = findViewById(R.id.ll_empty);
        llLoading = findViewById(R.id.ll_loading);

        // 设置搜索框内容
        etSearch.setText(keyword);
        etSearch.setSelection(keyword.length());

        // 给顶部栏添加状态栏高度的 padding
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.ll_top_bar), (v, insets) -> {
            Insets statusBar = insets.getInsets(WindowInsetsCompat.Type.statusBars());
            v.setPadding(v.getPaddingLeft(), statusBar.top, v.getPaddingRight(), v.getPaddingBottom());
            return insets;
        });

        // 初始化 RecyclerView
        rvGoods.setLayoutManager(new LinearLayoutManager(this));
        adapter = new GoodsAdapter();
        rvGoods.setAdapter(adapter);
    }

    private void initListeners() {
        // 返回按钮
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // 搜索按钮
        findViewById(R.id.tv_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyword = etSearch.getText().toString().trim();
                if (!keyword.isEmpty()) {
                    searchGoods();
                }
            }
        });
    }

    /**
     * 搜索商品
     */
    private void searchGoods() {
        showLoading();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String url = Api.SEARCH + "?keyword=" + keyword + "&pageNumber=1&pageSize=20";
                    String response = HttpUtil.get(url);
                    JSONObject json = new JSONObject(response);

                    if (json.getInt("resultCode") == 200) {
                        JSONObject data = json.getJSONObject("data");
                        JSONArray list = data.getJSONArray("list");

                        goodsList.clear();
                        for (int i = 0; i < list.length(); i++) {
                            JSONObject item = list.getJSONObject(i);
                            GoodsBean goods = new GoodsBean();
                            goods.setGoodsId(item.getLong("goodsId"));
                            goods.setGoodsName(item.getString("goodsName"));
                            goods.setGoodsIntro(item.getString("goodsIntro"));
                            goods.setGoodsCoverImg(item.getString("goodsCoverImg"));
                            goods.setSellingPrice(item.getInt("sellingPrice"));
                            goodsList.add(goods);
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showResult();
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showEmpty();
                            }
                        });
                    }
                } catch (Exception e) {
                    Log.e(TAG, "searchGoods error", e);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showEmpty();
                            Toast.makeText(SearchResultActivity.this, "搜索失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }

    private void showLoading() {
        llLoading.setVisibility(View.VISIBLE);
        rvGoods.setVisibility(View.GONE);
        llEmpty.setVisibility(View.GONE);
    }

    private void showResult() {
        llLoading.setVisibility(View.GONE);
        if (goodsList.isEmpty()) {
            llEmpty.setVisibility(View.VISIBLE);
            rvGoods.setVisibility(View.GONE);
        } else {
            llEmpty.setVisibility(View.GONE);
            rvGoods.setVisibility(View.VISIBLE);
            adapter.notifyDataSetChanged();
        }
    }

    private void showEmpty() {
        llLoading.setVisibility(View.GONE);
        rvGoods.setVisibility(View.GONE);
        llEmpty.setVisibility(View.VISIBLE);
    }

    /**
     * 商品列表适配器
     */
    class GoodsAdapter extends RecyclerView.Adapter<GoodsAdapter.ViewHolder> {

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_search_goods, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            GoodsBean goods = goodsList.get(position);

            // 设置商品图片
            Glide.with(holder.itemView.getContext())
                    .load(goods.getGoodsCoverImg())
                    .placeholder(R.drawable.banner_placeholder)
                    .into(holder.ivGoods);

            // 设置商品名称
            holder.tvGoodsName.setText(goods.getGoodsName());

            // 设置商品简介
            holder.tvGoodsIntro.setText(goods.getGoodsIntro());

            // 设置价格
            holder.tvPrice.setText("¥ " + String.format("%.2f", goods.getSellingPrice() / 100.0));

            // 点击跳转到商品详情
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(SearchResultActivity.this, GoodsDetailActivity.class);
                    intent.putExtra(GoodsDetailActivity.EXTRA_GOODS_ID, goods.getGoodsId());
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return goodsList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            ImageView ivGoods;
            TextView tvGoodsName;
            TextView tvGoodsIntro;
            TextView tvPrice;

            ViewHolder(View itemView) {
                super(itemView);
                ivGoods = itemView.findViewById(R.id.iv_goods);
                tvGoodsName = itemView.findViewById(R.id.tv_goods_name);
                tvGoodsIntro = itemView.findViewById(R.id.tv_goods_intro);
                tvPrice = itemView.findViewById(R.id.tv_price);
            }
        }
    }
}
