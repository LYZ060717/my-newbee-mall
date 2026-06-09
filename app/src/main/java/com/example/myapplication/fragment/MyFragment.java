package com.example.myapplication.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.myapplication.AccountActivity;
import com.example.myapplication.LoginActivity;
import com.example.myapplication.R;
import com.example.myapplication.api.Api;
import com.example.myapplication.util.HttpUtil;
import com.example.myapplication.util.SPUtil;

import org.json.JSONException;
import org.json.JSONObject;

public class MyFragment extends Fragment {

    private static final String TAG = "MyFragment";

    private TextView tvNickname;
    private TextView tvLoginName;
    private TextView tvSignature;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvNickname = view.findViewById(R.id.tv_nickname);
        tvLoginName = view.findViewById(R.id.tv_login_name);
        tvSignature = view.findViewById(R.id.tv_signature);

        // 给顶部标题栏添加状态栏高度的 padding
        LinearLayout titleBar = view.findViewById(R.id.ll_title_bar);
        ViewCompat.setOnApplyWindowInsetsListener(titleBar, (v, insets) -> {
            Insets statusBar = insets.getInsets(WindowInsetsCompat.Type.statusBars());
            v.setPadding(0, statusBar.top, 0, 0);
            return insets;
        });

        // 点击整个卡片区域跳转登录
        View card = view.findViewById(R.id.ll_profile_card);
        card.setOnClickListener(v -> {
            String token = SPUtil.getToken(requireContext());
            if (token == null || token.isEmpty()) {
                startActivity(new Intent(requireContext(), LoginActivity.class));
            }
        });

        // 账号管理点击事件
        View accountManage = view.findViewById(R.id.ll_account_manage);
        accountManage.setOnClickListener(v -> {
            String token = SPUtil.getToken(requireContext());
            if (token == null || token.isEmpty()) {
                Toast.makeText(requireContext(), "请先登录", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(requireContext(), LoginActivity.class));
            } else {
                startActivity(new Intent(requireContext(), AccountActivity.class));
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        // 每次页面可见时刷新登录状态（从登录页返回后自动更新）
        if (tvNickname != null) {
            checkLoginState();
        }
    }

    /**
     * 检查登录状态
     */
    private void checkLoginState() {
        String token = SPUtil.getToken(requireContext());
        Log.d(TAG, "当前Token: " + token);

        if (token == null || token.isEmpty()) {
            // 未登录
            tvNickname.setText("未登录");
            tvLoginName.setText("点击登录账号");
            tvSignature.setText("");
            Log.d(TAG, "用户未登录");
        } else {
            // 已登录，获取用户信息
            Log.d(TAG, "用户已登录，Token: " + token);
            loadUserInfo(token);
        }
    }

    /**
     * 加载用户信息
     */
    private void loadUserInfo(String token) {
        new Thread(() -> {
            try {
                String response = HttpUtil.get(Api.USER_INFO, token);
                Log.d(TAG, "用户信息响应: " + response);

                if (getActivity() == null) return;
                JSONObject json = new JSONObject(response);
                int resultCode = json.getInt("resultCode");

                getActivity().runOnUiThread(() -> {
                    if (resultCode == 200) {
                        try {
                            JSONObject data = json.getJSONObject("data");
                            String nickname = data.optString("nickName", "");
                            String loginName = data.optString("loginName", "");
                            String introduceSign = data.optString("introduceSign", "");

                            tvNickname.setText("昵称：" + (nickname.isEmpty() ? loginName : nickname));
                            tvLoginName.setText("登录名：" + loginName);
                            tvSignature.setText("个性签名：" + (introduceSign.isEmpty() ? "这个人很懒，什么都没写~" : introduceSign));
                        } catch (JSONException e) {
                            Log.e(TAG, "解析用户信息失败", e);
                        }
                    } else {
                        // Token 过期或无效，清除 Token 并显示未登录状态
                        Log.w(TAG, "Token 无效，清除登录状态");
                        SPUtil.clearToken(requireContext());
                        tvNickname.setText("未登录");
                        tvLoginName.setText("点击登录账号");
                        tvSignature.setText("");
                    }
                });
            } catch (Exception e) {
                Log.e(TAG, "请求用户信息异常", e);
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        tvNickname.setText("未登录");
                        tvLoginName.setText("点击登录账号");
                        tvSignature.setText("");
                    });
                }
            }
        }).start();
    }
}
