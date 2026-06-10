package com.example.myapplication.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.myapplication.GoodsDetailActivity;
import com.example.myapplication.LoginActivity;
import com.example.myapplication.R;
import com.example.myapplication.SearchActivity;
import com.example.myapplication.api.Api;
import com.example.myapplication.bean.CarouselBean;
import com.example.myapplication.bean.GoodsBean;
import com.example.myapplication.util.HttpUtil;
import com.youth.banner.Banner;
import com.youth.banner.adapter.BannerAdapter;
import com.youth.banner.indicator.CircleIndicator;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";

    private Banner<String, UrlBannerAdapter> banner;
    private LinearLayout llNewGoods;
    private LinearLayout llHotGoods;
    private LinearLayout llRecommendGoods;
    private TextView tvHotTitle;
    private TextView tvRecommendTitle;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        initBanner(view);
        initLoginClick(view);
        initSearchClick(view);
        initScrollListener(view);
        loadIndexData();
    }

    private void initViews(View view) {
        llNewGoods = view.findViewById(R.id.ll_new_goods);
        llHotGoods = view.findViewById(R.id.ll_hot_goods);
        llRecommendGoods = view.findViewById(R.id.ll_recommend_goods);
        tvHotTitle = view.findViewById(R.id.tv_hot_title);
        tvRecommendTitle = view.findViewById(R.id.tv_recommend_title);
    }

    private void initLoginClick(View view) {
        TextView tvLogin = view.findViewById(R.id.tv_login);
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(requireContext(), LoginActivity.class));
            }
        });
    }

    private void initSearchClick(View view) {
        View searchBar = view.findViewById(R.id.ll_search_bar);
        if (searchBar != null) {
            searchBar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(requireContext(), SearchActivity.class));
                }
            });

            View editText = searchBar.findViewById(R.id.et_search_home);
            if (editText != null) {
                editText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(requireContext(), SearchActivity.class));
                    }
                });
            }
        }
    }

    /**
     * 初始化滚动监听，实现导航栏透明度渐变效果
     */
    private void initScrollListener(View view) {
        NestedScrollView scrollView = view.findViewById(R.id.scroll_view);
        LinearLayout navBar = view.findViewById(R.id.ll_nav_bar);
        TextView tvTitle = view.findViewById(R.id.tv_title);
        TextView tvDivider = view.findViewById(R.id.tv_divider);
        TextView tvLogin = view.findViewById(R.id.tv_login);

        ViewCompat.setOnApplyWindowInsetsListener(navBar, (v, insets) -> {
            Insets statusBar = insets.getInsets(WindowInsetsCompat.Type.statusBars());
            // 增加顶部 padding（状态栏高度）
            v.setPadding(0, statusBar.top, 0, 0);

            // 增加导航栏高度，让绿色区域向下延伸
            ViewGroup.LayoutParams lp = v.getLayoutParams();
            lp.height = statusBar.top + dp2px(56 + 12); // 状态栏高度 + 原高度 + 向下延伸12dp
            v.setLayoutParams(lp);

            // 动态设置轮播图的顶部间距，避免被导航栏遮挡
            View banner = scrollView.findViewById(R.id.banner);
            if (banner != null) {
                ViewGroup.MarginLayoutParams bannerLp = (ViewGroup.MarginLayoutParams) banner.getLayoutParams();
                bannerLp.topMargin = lp.height;
                banner.setLayoutParams(bannerLp);
            }

            return insets;
        });

        final int bannerHeight = (int) (220 * getResources().getDisplayMetrics().density);

        scrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                float ratio = Math.min(1f, (float) scrollY / bannerHeight);

                // 导航栏背景色渐变
                int alpha = (int) (ratio * 255);
                int bgColor = Color.argb(alpha, 0, 121, 107);
                navBar.setBackgroundColor(bgColor);

                // 文字颜色渐变：绿色 -> 白色
                int textR = (int) (0 + ratio * (255 - 0));
                int textG = (int) (121 + ratio * (255 - 121));
                int textB = (int) (107 + ratio * (255 - 107));
                int textColor = Color.rgb(textR, textG, textB);

                tvTitle.setTextColor(textColor);
                tvLogin.setTextColor(textColor);

                // 分隔线颜色渐变
                int dividerAlpha = 128;
                int dividerColor = Color.argb(dividerAlpha, textR, textG, textB);
                tvDivider.setTextColor(dividerColor);
            }
        });
    }

    /**
     * 初始化轮播图（先用本地资源，API 加载成功后替换）
     */
    private void initBanner(View view) {
        banner = view.findViewById(R.id.banner);

        CircleIndicator indicator = new CircleIndicator(requireContext());
        indicator.getIndicatorConfig()
                .setNormalColor(Color.parseColor("#CCCCCC"))
                .setSelectedColor(Color.parseColor("#009688"));

        banner.setIndicator(indicator)
                .setBannerRound(8f)
                .setLoopTime(3000);
    }

    /**
     * 从 API 加载首页数据
     */
    private void loadIndexData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String response = HttpUtil.get(Api.INDEX_INFOS);
                    JSONObject json = new JSONObject(response);

                    int resultCode = json.optInt("resultCode", 0);
                    if (resultCode != 200) {
                        String message = json.optString("message", "加载失败");
                        Log.e(TAG, "API 错误: " + message);
                        return;
                    }

                    JSONObject data = json.optJSONObject("data");
                    if (data == null) {
                        Log.e(TAG, "data 为空");
                        return;
                    }

                    // 解析轮播图
                    List<CarouselBean> carousels = parseCarousels(data.optJSONArray("carousels"));
                    // 解析商品列表
                    List<GoodsBean> newGoodses = parseGoodsList(data.optJSONArray("newGoodses"));
                    List<GoodsBean> hotGoodses = parseGoodsList(data.optJSONArray("hotGoodses"));
                    List<GoodsBean> recommendGoodses = parseGoodsList(data.optJSONArray("recommendGoodses"));

                    // 回到主线程更新 UI
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                updateBanner(carousels);
                                renderGoodsList(llNewGoods, newGoodses);
                                renderGoodsList(llHotGoods, hotGoodses);
                                renderGoodsList(llRecommendGoods, recommendGoodses);

                                // 显示/隐藏标题
                                if (tvHotTitle != null) {
                                    tvHotTitle.setVisibility(hotGoodses.isEmpty() ? View.GONE : View.VISIBLE);
                                }
                                if (tvRecommendTitle != null) {
                                    tvRecommendTitle.setVisibility(recommendGoodses.isEmpty() ? View.GONE : View.VISIBLE);
                                }
                            }
                        });
                    }
                } catch (Exception e) {
                    Log.e(TAG, "加载首页数据失败", e);
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(requireContext(), "加载首页数据失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
        }).start();
    }

    /**
     * 解析轮播图 JSON 数组
     */
    private List<CarouselBean> parseCarousels(JSONArray jsonArray) {
        List<CarouselBean> list = new ArrayList<>();
        if (jsonArray == null) return list;

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.optJSONObject(i);
            if (obj != null) {
                CarouselBean bean = new CarouselBean();
                bean.setCarouselUrl(obj.optString("carouselUrl", ""));
                bean.setRedirectUrl(obj.optString("redirectUrl", ""));
                list.add(bean);
            }
        }
        return list;
    }

    /**
     * 解析商品 JSON 数组
     */
    private List<GoodsBean> parseGoodsList(JSONArray jsonArray) {
        List<GoodsBean> list = new ArrayList<>();
        if (jsonArray == null) return list;

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.optJSONObject(i);
            if (obj != null) {
                GoodsBean bean = new GoodsBean();
                bean.setGoodsId(obj.optLong("goodsId", 0));
                bean.setGoodsName(obj.optString("goodsName", ""));
                bean.setGoodsIntro(obj.optString("goodsIntro", ""));
                bean.setGoodsCoverImg(obj.optString("goodsCoverImg", ""));
                bean.setSellingPrice(obj.optInt("sellingPrice", 0));
                bean.setTag(obj.optString("tag", ""));
                list.add(bean);
            }
        }
        return list;
    }

    /**
     * 更新轮播图数据
     */
    private void updateBanner(List<CarouselBean> carousels) {
        if (carousels == null || carousels.isEmpty()) {
            // 没有数据时保持默认
            return;
        }

        List<String> urls = new ArrayList<>();
        for (CarouselBean c : carousels) {
            urls.add(c.getCarouselUrl());
        }

        banner.setAdapter(new UrlBannerAdapter(urls))
                .start();
    }

    /**
     * 动态渲染商品列表（两列网格）
     */
    private void renderGoodsList(LinearLayout container, List<GoodsBean> goodsList) {
        if (container == null || goodsList == null) return;
        container.removeAllViews();

        for (int i = 0; i < goodsList.size(); i += 2) {
            // 创建一行
            LinearLayout row = new LinearLayout(requireContext());
            row.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            rowParams.bottomMargin = dp2px(4);
            row.setLayoutParams(rowParams);

            // 左侧商品
            row.addView(createGoodsCard(goodsList.get(i), true));

            // 右侧商品（如果有）
            if (i + 1 < goodsList.size()) {
                row.addView(createGoodsCard(goodsList.get(i + 1), false));
            } else {
                // 奇数个商品时，右侧补空占位
                View placeholder = new View(requireContext());
                LinearLayout.LayoutParams phParams = new LinearLayout.LayoutParams(0, 0, 1f);
                phParams.setMarginStart(dp2px(2));
                placeholder.setLayoutParams(phParams);
                row.addView(placeholder);
            }

            container.addView(row);
        }
    }

    /**
     * 创建单个商品卡片
     */
    private View createGoodsCard(GoodsBean goods, boolean isLeft) {
        LinearLayout card = new LinearLayout(requireContext());
        card.setOrientation(LinearLayout.VERTICAL);
        card.setBackgroundColor(Color.WHITE);
        card.setClickable(true);
        card.setFocusable(true);

        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        if (isLeft) {
            cardParams.setMarginEnd(dp2px(2));
        } else {
            cardParams.setMarginStart(dp2px(2));
        }
        card.setLayoutParams(cardParams);

        // 点击跳转到商品详情
        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(), GoodsDetailActivity.class);
                intent.putExtra(GoodsDetailActivity.EXTRA_GOODS_ID, goods.getGoodsId());
                startActivity(intent);
            }
        });

        // 商品图片
        ImageView imageView = new ImageView(requireContext());
        LinearLayout.LayoutParams imgParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, dp2px(170));
        imageView.setLayoutParams(imgParams);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Glide.with(this).load(goods.getGoodsCoverImg()).placeholder(R.drawable.banner_placeholder).into(imageView);
        card.addView(imageView);

        // 信息区域
        LinearLayout infoLayout = new LinearLayout(requireContext());
        infoLayout.setOrientation(LinearLayout.VERTICAL);
        int padding = dp2px(8);
        infoLayout.setPadding(padding, padding, padding, padding);

        // 商品名称
        TextView tvName = new TextView(requireContext());
        tvName.setText(goods.getGoodsName());
        tvName.setTextColor(Color.parseColor("#333333"));
        tvName.setTextSize(13);
        tvName.setMaxLines(2);
        tvName.setEllipsize(android.text.TextUtils.TruncateAt.END);
        infoLayout.addView(tvName);

        // 价格
        TextView tvPrice = new TextView(requireContext());
        tvPrice.setText("¥ " + goods.getSellingPrice());
        tvPrice.setTextColor(Color.parseColor("#00796B"));
        tvPrice.setTextSize(15);
        tvPrice.setTypeface(null, android.graphics.Typeface.BOLD);
        LinearLayout.LayoutParams priceParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        priceParams.topMargin = dp2px(6);
        tvPrice.setLayoutParams(priceParams);
        infoLayout.addView(tvPrice);

        card.addView(infoLayout);
        return card;
    }

    /**
     * dp 转 px
     */
    private int dp2px(float dp) {
        return (int) (dp * getResources().getDisplayMetrics().density + 0.5f);
    }

    /**
     * Banner 图片适配器（网络 URL 方式）
     */
    static class UrlBannerAdapter extends BannerAdapter<String, UrlBannerAdapter.BannerViewHolder> {

        public UrlBannerAdapter(List<String> data) {
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

            public BannerViewHolder(@NonNull View itemView) {
                super(itemView);
                imageView = (ImageView) itemView;
            }
        }
    }
}
