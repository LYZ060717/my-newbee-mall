package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.api.Api;
import com.example.myapplication.util.HttpUtil;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private EditText etSearch;
    private ChipGroup chipGroupHistory;
    private ChipGroup chipGroupHot;
    private List<String> historyList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        initViews();
        initListeners();
        loadHistory();
        loadHotSearch();
    }

    private void initViews() {
        etSearch = findViewById(R.id.et_search);
        chipGroupHistory = findViewById(R.id.chip_group_history);
        chipGroupHot = findViewById(R.id.chip_group_hot);

        // 给顶部搜索栏添加状态栏高度的 padding
        LinearLayout topBar = findViewById(R.id.ll_top_bar);
        ViewCompat.setOnApplyWindowInsetsListener(topBar, (v, insets) -> {
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

        // 搜索按钮
        findViewById(R.id.tv_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performSearch();
            }
        });

        // 清除历史
        findViewById(R.id.iv_clear_history).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearHistory();
            }
        });
    }

    private void performSearch() {
        String keyword = etSearch.getText().toString().trim();
        if (keyword.isEmpty()) {
            Toast.makeText(this, "请输入搜索关键词", Toast.LENGTH_SHORT).show();
            return;
        }

        // 保存到搜索历史
        saveToHistory(keyword);

        // 跳转到搜索结果页面
        Intent intent = new Intent(this, SearchResultActivity.class);
        intent.putExtra(SearchResultActivity.EXTRA_KEYWORD, keyword);
        startActivity(intent);
    }

    private void loadHistory() {
        // TODO: 从 SharedPreferences 加载搜索历史
        // 示例数据
        historyList.clear();
        historyList.add("手机");
        historyList.add("电脑");
        historyList.add("耳机");

        refreshHistoryChips();
    }

    private void refreshHistoryChips() {
        chipGroupHistory.removeAllViews();
        for (String keyword : historyList) {
            Chip chip = new Chip(this);
            chip.setText(keyword);
            chip.setClickable(true);
            chip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    etSearch.setText(keyword);
                    performSearch();
                }
            });
            chipGroupHistory.addView(chip);
        }

        // 显示/隐藏历史标题
        findViewById(R.id.ll_history_title).setVisibility(
                historyList.isEmpty() ? View.GONE : View.VISIBLE);
    }

    private void saveToHistory(String keyword) {
        // TODO: 保存到 SharedPreferences
        // 避免重复
        historyList.remove(keyword);
        historyList.add(0, keyword);
        refreshHistoryChips();
    }

    private void clearHistory() {
        historyList.clear();
        refreshHistoryChips();
        // TODO: 清除 SharedPreferences 中的历史
    }

    private void loadHotSearch() {
        // 示例热门搜索数据
        String[] hotKeywords = {"华为", "iPhone", "小米", "MacBook", "iPad", "耳机", "手表", "充电器"};

        chipGroupHot.removeAllViews();
        for (String keyword : hotKeywords) {
            Chip chip = new Chip(this);
            chip.setText(keyword);
            chip.setClickable(true);
            chip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    etSearch.setText(keyword);
                    performSearch();
                }
            });
            chipGroupHot.addView(chip);
        }
    }
}
