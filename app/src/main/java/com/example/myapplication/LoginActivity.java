package com.example.myapplication;

import android.content.Intent;
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
import com.example.myapplication.util.SPUtil;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername;
    private EditText etPassword;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);

        // 返回按钮
        ImageView ivBack = findViewById(R.id.iv_back);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // 立即注册
        TextView tvRegister = findViewById(R.id.tv_register);
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        // 登录按钮
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doLogin();
            }
        });
    }

    /**
     * 执行登录
     */
    private void doLogin() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // 参数校验
        if (username.isEmpty()) {
            Toast.makeText(this, "请输入用户名", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.isEmpty()) {
            Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
            return;
        }

        // 禁用按钮，防止重复点击
        btnLogin.setEnabled(false);
        btnLogin.setText("登录中...");

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
                    String response = HttpUtil.post(Api.LOGIN, jsonBody.toString());
                    handleLoginResponse(response);
                } catch (Exception e) {
                    e.printStackTrace();
                    String msg = e.getMessage();
                    final String errorMsg = (msg != null && !msg.isEmpty()) ? msg : "未知错误";
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            resetButton();
                            Toast.makeText(LoginActivity.this, "网络请求失败: " + errorMsg, Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        }).start();
    }

    /**
     * 处理登录响应
     */
    private void handleLoginResponse(String response) {
        Log.d("LoginActivity", "API返回: " + response);

        try {
            JSONObject json = new JSONObject(response);
            int resultCode = json.getInt("resultCode");
            String message = json.getString("message");

            if (resultCode == 200) {
                // 登录成功，保存 token
                String token = json.getString("data");
                SPUtil.saveToken(this, token);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                    }
                });
            } else {
                // 登录失败
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        resetButton();
                        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(LoginActivity.this, "返回内容: " + debugInfo, Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    /**
     * 恢复按钮状态
     */
    private void resetButton() {
        btnLogin.setEnabled(true);
        btnLogin.setText("登录");
    }
}
