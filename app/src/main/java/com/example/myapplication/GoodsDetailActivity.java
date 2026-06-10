package com.example.myapplication;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.myapplication.api.Api;
import com.example.myapplication.bean.GoodsBean;
import com.example.myapplication.util.HttpUtil;
import com.youth.banner.Banner;
import com.youth.banner.adapter.BannerAdapter;
import com.youth.banner.indicator.CircleIndicator;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GoodsDetailActivity extends AppCompatActivity {

    private static final String TAG = "GoodsDetailActivity";
    public static final String EXTRA_GOODS_ID = "goods_id";

    private Banner banner;
    private TextView tvPrice;
    private TextView tvGoodsName;
    private TextView tvGoodsIntro;
    private LinearLayout llGoodsDetailImgs;

    private Long goodsId;
    private GoodsBean goodsBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_detail);

        goodsId = getIntent().getLongExtra(EXTRA_GOODS_ID, 0);
        if (goodsId == 0) {
            Toast.makeText(this, "商品不存在", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initViews();
        initListeners();
        loadGoodsDetail();
    }

    private void initViews() {
        banner = findViewById(R.id.banner_goods);
        tvPrice = findViewById(R.id.tv_price);
        tvGoodsName = findViewById(R.id.tv_goods_name);
        tvGoodsIntro = findViewById(R.id.tv_goods_intro);
        llGoodsDetailImgs = findViewById(R.id.ll_goods_detail_imgs);

        // 给顶部栏添加状态栏高度的 padding
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.ll_top_bar), (v, insets) -> {
            Insets statusBar = insets.getInsets(WindowInsetsCompat.Type.statusBars());
            v.setPadding(v.getPaddingLeft(), statusBar.top, v.getPaddingRight(), v.getPaddingBottom());
            return insets;
        });
    }

    private void initListeners() {
        // 返回按钮
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // 加入购物车
        findViewById(R.id.tv_add_cart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToCart();
            }
        });

        // 立即购买
        findViewById(R.id.tv_buy_now).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buyNow();
            }
        });
    }

    /**
     * 加载商品详情
     */
    private void loadGoodsDetail() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String url = Api.GOODS_DETAIL + goodsId;
                    String response = HttpUtil.get(url);
                    JSONObject json = new JSONObject(response);

                    if (json.getInt("resultCode") == 200) {
                        JSONObject data = json.getJSONObject("data");
                        goodsBean = parseGoodsBean(data);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                bindDataToView();
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(GoodsDetailActivity.this, "获取商品详情失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (Exception e) {
                    Log.e(TAG, "loadGoodsDetail error", e);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(GoodsDetailActivity.this, "网络请求失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }

    /**
     * 解析商品数据
     */
    private GoodsBean parseGoodsBean(JSONObject json) throws Exception {
        GoodsBean goods = new GoodsBean();
        goods.setGoodsId(json.getLong("goodsId"));
        goods.setGoodsName(json.getString("goodsName"));
        goods.setGoodsIntro(json.getString("goodsIntro"));
        goods.setGoodsCoverImg(json.getString("goodsCoverImg"));
        goods.setSellingPrice(json.getInt("sellingPrice"));
        goods.setGoodsDetailContent(json.optString("goodsDetailContent", ""));
        return goods;
    }

    /**
     * 绑定数据到视图
     */
    private void bindDataToView() {
        if (goodsBean == null) return;

        // 设置价格（分为单位转为元）
        tvPrice.setText("¥ " + String.format("%.2f", goodsBean.getSellingPrice() / 100.0));

        // 设置商品名称
        tvGoodsName.setText(goodsBean.getGoodsName());

        // 设置商品描述
        tvGoodsIntro.setText(goodsBean.getGoodsIntro());

        // 初始化轮播图
        initBanner();

        // 加载商品详情图片
        loadDetailImages();
    }

    /**
     * 初始化轮播图
     */
    private void initBanner() {
        CircleIndicator indicator = new CircleIndicator(this);
        indicator.getIndicatorConfig()
                .setNormalColor(Color.parseColor("#CCCCCC"))
                .setSelectedColor(Color.parseColor("#009688"));

        banner.setIndicator(indicator)
                .setBannerRound(8f)
                .setLoopTime(3000);

        // 使用封面图作为轮播图（实际项目中可能有多张图片）
        List<String> imageList = new ArrayList<>();
        imageList.add(goodsBean.getGoodsCoverImg());

        banner.setAdapter(new GoodsBannerAdapter(imageList))
                .start();
    }

    /**
     * 加载商品详情图片
     */
    private void loadDetailImages() {
        String detailContent = goodsBean.getGoodsDetailContent();
        if (detailContent == null || detailContent.isEmpty()) {
            return;
        }

        // 解析详情内容中的图片（假设是JSON数组格式的图片URL）
        try {
            JSONArray images = new JSONArray(detailContent);
            for (int i = 0; i < images.length(); i++) {
                String imgUrl = images.getString(i);
                addDetailImage(imgUrl);
            }
        } catch (Exception e) {
            // 如果不是JSON格式，可能是HTML内容，直接显示文本
            Log.e(TAG, "parse detail content error", e);
        }
    }

    /**
     * 添加详情图片到布局
     */
    private void addDetailImage(String imgUrl) {
        ImageView imageView = new ImageView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.bottomMargin = dp2px(4);
        imageView.setLayoutParams(params);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);

        Glide.with(this)
                .load(imgUrl)
                .placeholder(R.drawable.banner_placeholder)
                .into(imageView);

        llGoodsDetailImgs.addView(imageView);
    }

    /**
     * 加入购物车
     */
    private void addToCart() {
        if (goodsBean == null) return;

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject body = new JSONObject();
                    body.put("goodsId", goodsBean.getGoodsId());
                    body.put("goodsCount", 1);

                    String response = HttpUtil.post(Api.CART_ADD, body.toString());
                    JSONObject json = new JSONObject(response);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (json.optInt("resultCode") == 200) {
                                Toast.makeText(GoodsDetailActivity.this, "已加入购物车", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(GoodsDetailActivity.this, json.optString("message", "加入购物车失败"), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } catch (Exception e) {
                    Log.e(TAG, "addToCart error", e);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(GoodsDetailActivity.this, "网络请求失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }

    /**
     * 立即购买
     */
    private void buyNow() {
        if (goodsBean == null) return;

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // 创建订单
                    JSONObject body = new JSONObject();
                    body.put("addressId", 0); // 需要选择地址
                    body.put("couponId", 0);

                    String response = HttpUtil.post(Api.ORDER_CREATE, body.toString());
                    JSONObject json = new JSONObject(response);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (json.optInt("resultCode") == 200) {
                                Toast.makeText(GoodsDetailActivity.this, "订单创建成功", Toast.LENGTH_SHORT).show();
                                // TODO: 跳转到订单详情或支付页面
                            } else {
                                Toast.makeText(GoodsDetailActivity.this, json.optString("message", "创建订单失败"), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } catch (Exception e) {
                    Log.e(TAG, "buyNow error", e);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(GoodsDetailActivity.this, "网络请求失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }

    private int dp2px(float dp) {
        float density = getResources().getDisplayMetrics().density;
        return (int) (dp * density + 0.5f);
    }

    /**
     * 商品轮播图适配器
     */
    static class GoodsBannerAdapter extends BannerAdapter<String, GoodsBannerAdapter.BannerViewHolder> {

        public GoodsBannerAdapter(List<String> data) {
            super(data);
        }

        @Override
        public BannerViewHolder onCreateHolder(ViewGroup parent, int viewType) {
            ImageView imageView = new ImageView(parent.getContext());
            imageView.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            return new BannerViewHolder(imageView);
        }

        @Override
        public void onBindView(BannerViewHolder holder, String url, int position, int size) {
            Glide.with(holder.imageView.getContext())
                    .load(url)
                    .placeholder(R.drawable.banner_placeholder)
                    .into(holder.imageView);
        }

        static class BannerViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
            ImageView imageView;

            public BannerViewHolder(View itemView) {
                super(itemView);
                imageView = (ImageView) itemView;
            }
        }
    }
}
