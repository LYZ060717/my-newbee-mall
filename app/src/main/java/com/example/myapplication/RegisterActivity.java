package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.api.Api;
import com.example.myapplication.util.HttpUtil;
import com.example.myapplication.util.MD5Util;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    private EditText etUsername;
    private EditText etPassword;
    private EditText etConfirmPassword;
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        etConfirmPassword = findViewById(R.id.et_confirm_password);
        btnRegister = findViewById(R.id.btn_register);

        // 返回按钮
        ImageView ivBack = findViewById(R.id.iv_back);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // 已有账号去登录
        TextView tvGoLogin = findViewById(R.id.tv_go_login);
        tvGoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // 注册按钮
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doRegister();
            }
        });
    }

    /**
     * 执行注册
     */
    private void doRegister() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        // 参数校验
        if (username.isEmpty()) {
            Toast.makeText(this, "请输入用户名", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.isEmpty()) {
            Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
            return;
        }
        if (confirmPassword.isEmpty()) {
            Toast.makeText(this, "请确认密码", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "两次输入的密码不一致", Toast.LENGTH_SHORT).show();
            return;
        }

        // 禁用按钮
        btnRegister.setEnabled(false);
        btnRegister.setText("注册中...");

        // 密码 MD5 加密
        String encryptedPassword = MD5Util.md5(password);

        // 构建请求体
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("loginName", username);
            jsonBody.put("passwordMd5", encryptedPassword);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // 子线程请求网络
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String response = HttpUtil.post(Api.REGISTER, jsonBody.toString());
                    handleRegisterResponse(response);
                } catch (Exception e) {
                    e.printStackTrace();
                    String msg = e.getMessage();
                    final String errorMsg = (msg != null && !msg.isEmpty()) ? msg : "未知错误";
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            resetButton();
                            Toast.makeText(RegisterActivity.this, "网络请求失败: " + errorMsg, Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        }).start();
    }

    /**
     * 处理注册响应
     */
    private void handleRegisterResponse(String response) {
        Log.d("RegisterActivity", "API返回: " + response);

        try {
            JSONObject json = new JSONObject(response);
            int resultCode = json.getInt("resultCode");
            String message = json.getString("message");

            if (resultCode == 200) {
                // 注册成功
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                        finish(); // 返回登录页
                    }
                });
            } else {
                // 注册失败
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        resetButton();
                        Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
            final String debugInfo = response;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    resetButton();
                    Toast.makeText(RegisterActivity.this, "返回内容: " + debugInfo, Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    /**
     * 恢复按钮状态
     */
    private void resetButton() {
        btnRegister.setEnabled(true);
        btnRegister.setText("注册");
    }
}
