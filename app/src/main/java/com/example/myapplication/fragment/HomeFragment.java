package com.example.myapplication.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.myapplication.LoginActivity;
import com.example.myapplication.R;
import com.youth.banner.Banner;
import com.youth.banner.adapter.BannerAdapter;
import com.youth.banner.indicator.CircleIndicator;

import java.util.Arrays;
import java.util.List;

public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initBanner(view);
        initLoginClick(view);
        initScrollListener(view);
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

    /**
     * 初始化滚动监听，实现导航栏透明度渐变效果
     */
    private void initScrollListener(View view) {
        NestedScrollView scrollView = view.findViewById(R.id.scroll_view);
        LinearLayout navBar = view.findViewById(R.id.ll_nav_bar);
        TextView tvTitle = view.findViewById(R.id.tv_title);
        TextView tvDivider = view.findViewById(R.id.tv_divider);
        TextView tvLogin = view.findViewById(R.id.tv_login);

        // 给导航栏添加状态栏高度的 padding，使其内容位于状态栏下方
        ViewCompat.setOnApplyWindowInsetsListener(navBar, (v, insets) -> {
            Insets statusBar = insets.getInsets(WindowInsetsCompat.Type.statusBars());
            v.setPadding(0, statusBar.top, 0, 0);
            return insets;
        });

        // 颜色定义
        final int colorGreen = Color.parseColor("#00796B");  // 绿色
        final int colorWhite = Color.WHITE;                   // 白色

        // 轮播图高度（用于计算滚动比例）
        final int bannerHeight = (int) (220 * getResources().getDisplayMetrics().density);

        scrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                // 计算滚动比例（0~1）
                float ratio = Math.min(1f, (float) scrollY / bannerHeight);

                // 设置导航栏背景透明度
                int alpha = (int) (ratio * 255);
                int bgColor = Color.argb(alpha, 0, 121, 107); // teal_700: #00796B
                navBar.setBackgroundColor(bgColor);

                // 计算文字颜色：绿色 -> 白色
                int textR = (int) (0 + ratio * (255 - 0));      // 0 -> 255
                int textG = (int) (121 + ratio * (255 - 121));   // 121 -> 255
                int textB = (int) (107 + ratio * (255 - 107));   // 107 -> 255
                int textColor = Color.rgb(textR, textG, textB);

                // 设置文字颜色
                tvTitle.setTextColor(textColor);
                tvLogin.setTextColor(textColor);

                // 分隔线颜色：半透明绿色 -> 半透明白色
                int dividerAlpha = 128; // 80 hex = 128 decimal
                int dividerColor = Color.argb(dividerAlpha, textR, textG, textB);
                tvDivider.setTextColor(dividerColor);
            }
        });
    }

    private Banner<Integer, LocalBannerAdapter> banner;

    private void initBanner(View view) {
        banner = view.findViewById(R.id.banner);

        CircleIndicator indicator = new CircleIndicator(requireContext());
        indicator.getIndicatorConfig()
                .setNormalColor(Color.parseColor("#CCCCCC"))
                .setSelectedColor(Color.parseColor("#009688"));

        banner.setIndicator(indicator)
                .setBannerRound(8f)
                .setLoopTime(3000);

        // 本地轮播图资源
        List<Integer> bannerResList = Arrays.asList(
                R.drawable.banner_1,
                R.drawable.banner_2
        );

        banner.setAdapter(new LocalBannerAdapter(bannerResList))
                .start();
    }

    /**
     * Banner 图片适配器（本地资源方式）
     */
    static class LocalBannerAdapter extends BannerAdapter<Integer, LocalBannerAdapter.BannerViewHolder> {

        public LocalBannerAdapter(List<Integer> data) {
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
        public void onBindView(BannerViewHolder holder, Integer resId, int position, int size) {
            Glide.with(holder.imageView.getContext())
                    .load(resId)
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
