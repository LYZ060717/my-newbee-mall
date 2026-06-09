package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.api.Api;
import com.example.myapplication.util.HttpUtil;
import com.example.myapplication.util.MD5Util;
import com.example.myapplication.util.SPUtil;

import org.json.JSONException;
import org.json.JSONObject;

public class ChangePasswordActivity extends AppCompatActivity {

    private static final String TAG = "ChangePasswordActivity";

    private EditText etOldPassword;
    private EditText etNewPassword;
    private EditText etConfirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        initViews();
    }

    private void initViews() {
        // 返回按钮
        ImageView ivBack = findViewById(R.id.iv_back);
        ivBack.setOnClickListener(v -> finish());

        // 输入框
        etOldPassword = findViewById(R.id.et_old_password);
        etNewPassword = findViewById(R.id.et_new_password);
        etConfirmPassword = findViewById(R.id.et_confirm_password);

        // 确认修改按钮
        TextView tvSavePassword = findViewById(R.id.tv_save_password);
        tvSavePassword.setOnClickListener(v -> savePassword());
    }

    /**
     * 修改密码
     */
    private void savePassword() {
        String oldPassword = etOldPassword.getText().toString().trim();
        String newPassword = etNewPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        if (oldPassword.isEmpty()) {
            Toast.makeText(this, "请输入原密码", Toast.LENGTH_SHORT).show();
            return;
        }
        if (newPassword.isEmpty()) {
            Toast.makeText(this, "请输入新密码", Toast.LENGTH_SHORT).show();
            return;
        }
        if (newPassword.length() < 6) {
            Toast.makeText(this, "新密码长度不能少于6位", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!newPassword.equals(confirmPassword)) {
            Toast.makeText(this, "两次输入的密码不一致", Toast.LENGTH_SHORT).show();
            return;
        }

        String token = SPUtil.getToken(this);
        JSONObject body = new JSONObject();
        try {
            body.put("oldPassword", MD5Util.md5(oldPassword));
            body.put("newPassword", MD5Util.md5(newPassword));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Thread(() -> {
            try {
                Log.d(TAG, "修改密码请求地址: " + Api.USER_UPDATE);
                Log.d(TAG, "修改密码请求体: " + body.toString());
                String response = HttpUtil.post(Api.USER_UPDATE, body.toString(), token);
                Log.d(TAG, "修改密码响应: " + response);

                JSONObject json = new JSONObject(response);
                int resultCode = json.getInt("resultCode");

                runOnUiThread(() -> {
                    if (resultCode == 200) {
                        Toast.makeText(this, "密码修改成功", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        String message = json.optString("message", "密码修改失败");
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (Exception e) {
                Log.e(TAG, "修改密码异常: " + e.getMessage(), e);
                runOnUiThread(() ->
                        Toast.makeText(this, "修改密码失败: " + e.getMessage(), Toast.LENGTH_LONG).show()
                );
            }
        }).start();
    }
}
