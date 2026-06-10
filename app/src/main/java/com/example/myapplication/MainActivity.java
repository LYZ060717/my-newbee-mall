package com.example.myapplication;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapplication.fragment.CartFragment;
import com.example.myapplication.fragment.CategoryFragment;
import com.example.myapplication.fragment.HomeFragment;
import com.example.myapplication.fragment.MyFragment;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private Fragment[] fragments;
    private int currentTabIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initFragments();
        initTabLayout();
    }

    private void initFragments() {
        fragments = new Fragment[4];
        // 默认显示首页
        fragments[0] = new HomeFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fl_content, fragments[0])
                .commit();
    }

    private void initTabLayout() {
        tabLayout = findViewById(R.id.tab_layout);

        String[] titles = {"首页", "分类", "购物车", "我的"};
        int[] icons = {
                R.drawable.selector_tab_home,
                R.drawable.selector_tab_category,
                R.drawable.selector_tab_cart,
                R.drawable.selector_tab_mine
        };

        for (int i = 0; i < titles.length; i++) {
            tabLayout.addTab(tabLayout.newTab()
                    .setText(titles[i])
                    .setIcon(icons[i]));
        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switchFragment(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        // 首页 Tab 默认选中
        tabLayout.selectTab(tabLayout.getTabAt(0));
    }

    /**
     * 切换 Fragment
     * 首次切到某个 Tab 时懒创建，之后只做 hide / show
     */
    private void switchFragment(int index) {
        if (index == currentTabIndex) return;

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        // 隐藏当前 Fragment
        ft.hide(fragments[currentTabIndex]);

        // 目标 Fragment 未创建则懒加载
        if (fragments[index] == null) {
            fragments[index] = createFragment(index);
            ft.add(R.id.fl_content, fragments[index]);
        } else {
            ft.show(fragments[index]);
        }

        ft.commit();
        currentTabIndex = index;
    }

    /**
     * 根据索引创建对应的 Fragment
     */
    private Fragment createFragment(int index) {
        switch (index) {
            case 0:
                return new HomeFragment();
            case 1:
                return new CategoryFragment();
            case 2:
                return new CartFragment();
            case 3:
                return new MyFragment();
            default:
                return new HomeFragment();
        }
    }
}
