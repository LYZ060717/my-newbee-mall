package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.api.Api;
import com.example.myapplication.util.HttpUtil;
import com.example.myapplication.util.SPUtil;

import org.json.JSONException;
import org.json.JSONObject;

public class AccountActivity extends AppCompatActivity {

    private static final String TAG = "AccountActivity";

    private EditText etNickname;
    private EditText etSignature;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        initViews();
        loadUserInfo();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 从修改密码页面返回后刷新用户信息
        loadUserInfo();
    }

    private void initViews() {
        // 返回按钮
        ImageView ivBack = findViewById(R.id.iv_back);
        ivBack.setOnClickListener(v -> finish());

        // 昵称相关
        etNickname = findViewById(R.id.et_nickname);
        TextView tvSaveNickname = findViewById(R.id.tv_save_nickname);
        tvSaveNickname.setOnClickListener(v -> saveNickname());

        // 个性签名相关
        etSignature = findViewById(R.id.et_signature);
        TextView tvSaveSignature = findViewById(R.id.tv_save_signature);
        tvSaveSignature.setOnClickListener(v -> saveSignature());

        // 修改密码 - 跳转到新页面
        View llChangePassword = findViewById(R.id.ll_change_password);
        llChangePassword.setOnClickListener(v ->
                startActivity(new Intent(this, ChangePasswordActivity.class))
        );

        // 退出登录
        TextView tvLogout = findViewById(R.id.tv_logout);
        tvLogout.setOnClickListener(v -> showLogoutDialog());
    }

    /**
     * 加载用户信息
     */
    private void loadUserInfo() {
        String token = SPUtil.getToken(this);
        if (token == null || token.isEmpty()) {
            Toast.makeText(this, "请先登录", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        new Thread(() -> {
            try {
                String response = HttpUtil.get(Api.USER_INFO, token);
                Log.d(TAG, "用户信息响应: " + response);

                JSONObject json = new JSONObject(response);
                int resultCode = json.getInt("resultCode");

                runOnUiThread(() -> {
                    if (resultCode == 200) {
                        try {
                            JSONObject data = json.getJSONObject("data");
                            String nickname = data.optString("nickName", "");
                            String signature = data.optString("introduceSign", "");

                            etNickname.setText(nickname);
                            etSignature.setText(signature);
                        } catch (JSONException e) {
                            Log.e(TAG, "解析用户信息失败", e);
                        }
                    } else {
                        String message = json.optString("message", "获取用户信息失败");
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                        if (resultCode == 401 || resultCode == 501) {
                            SPUtil.clearToken(this);
                            finish();
                        }
                    }
                });
            } catch (Exception e) {
                Log.e(TAG, "请求用户信息异常", e);
                runOnUiThread(() ->
                        Toast.makeText(this, "网络请求失败", Toast.LENGTH_SHORT).show()
                );
            }
        }).start();
    }

    /**
     * 保存昵称
     */
    private void saveNickname() {
        String nickname = etNickname.getText().toString().trim();
        if (nickname.isEmpty()) {
            Toast.makeText(this, "昵称不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        String token = SPUtil.getToken(this);
        JSONObject body = new JSONObject();
        try {
            body.put("nickName", nickname);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        updateUserInfo(token, body.toString(), "昵称修改成功");
    }

    /**
     * 保存个性签名
     */
    private void saveSignature() {
        String signature = etSignature.getText().toString().trim();
        String token = SPUtil.getToken(this);

        JSONObject body = new JSONObject();
        try {
            body.put("introduceSign", signature);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        updateUserInfo(token, body.toString(), "个性签名修改成功");
    }

    /**
     * 更新用户信息（昵称/个性签名）
     */
    private void updateUserInfo(String token, String jsonBody, String successMsg) {
        new Thread(() -> {
            try {
                String response = HttpUtil.post(Api.USER_UPDATE, jsonBody, token);
                Log.d(TAG, "更新用户信息响应: " + response);

                JSONObject json = new JSONObject(response);
                int resultCode = json.getInt("resultCode");

                runOnUiThread(() -> {
                    if (resultCode == 200) {
                        Toast.makeText(this, successMsg, Toast.LENGTH_SHORT).show();
                    } else {
                        String message = json.optString("message", "修改失败");
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (Exception e) {
                Log.e(TAG, "更新用户信息异常", e);
                runOnUiThread(() ->
                        Toast.makeText(this, "网络请求失败", Toast.LENGTH_SHORT).show()
                );
            }
        }).start();
    }

    /**
     * 显示退出登录确认对话框
     */
    private void showLogoutDialog() {
        new AlertDialog.Builder(this)
                .setTitle("退出登录")
                .setMessage("确定要退出登录吗？")
                .setPositiveButton("确定", (dialog, which) -> {
                    SPUtil.clearToken(this);
                    Toast.makeText(this, "已退出登录", Toast.LENGTH_SHORT).show();

                    // 返回到"我的"页面，刷新登录状态
                    setResult(RESULT_OK);
                    finish();
                })
                .setNegativeButton("取消", null)
                .show();
    }
}
