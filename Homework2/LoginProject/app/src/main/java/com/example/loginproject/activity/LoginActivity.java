package com.example.loginproject.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.loginproject.R;
import com.example.loginproject.data.DatabaseHelper;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText etUsername, etPassword;
    private MaterialButton btnLogin, btnWechatLogin, btnAppleLogin;
    private DatabaseHelper dbHelper;

    public static final String PREFS_NAME = "MyPrefsFile";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_SIGNATURE = "signature";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 初始化数据库帮助类
        dbHelper = new DatabaseHelper(this);

        // 预埋一个默认账号，仅在数据库为空时执行
        prepopulateUserIfNeeded();

        // 绑定UI控件
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        btnWechatLogin = findViewById(R.id.btn_wechat_login);
        btnAppleLogin = findViewById(R.id.btn_apple_login);

        // 设置登录按钮点击事件
        btnLogin.setOnClickListener(v -> login());

        // 设置社交登录按钮点击事件
        btnWechatLogin.setOnClickListener(v -> showToast("微信登录功能开发中..."));
        btnAppleLogin.setOnClickListener(v -> showToast("Apple登录功能开发中..."));
    }

    /**
     * 如果数据库中没有任何用户，则预埋一个默认用户
     */
    private void prepopulateUserIfNeeded() {
        if (!dbHelper.hasUsers()) {
            dbHelper.addUser("admin", "123456");
        }
    }

    /**
     * 处理登录逻辑
     */
    private void login() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // 验证输入是否为空
        if (TextUtils.isEmpty(username)) {
            etUsername.setError("用户名不能为空");
            etUsername.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            etPassword.setError("密码不能为空");
            etPassword.requestFocus();
            return;
        }

        // 验证凭据
        if (dbHelper.checkUser(username, password)) {
            // 登录成功，保存用户信息到SharedPreferences
            saveUserInfo(username);

            // 跳转到个人中心页面
            Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
            startActivity(intent);
            finish(); // 关闭登录页面，防止用户返回
        } else {
            // 登录失败
            showToast("用户名或密码错误");
        }
    }

    /**
     * 保存用户信息到SharedPreferences
     * 
     * @param username 登录成功的用户名
     */
    private void saveUserInfo(String username) {
        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_SIGNATURE, "欢迎来到信息App"); // 默认签名
        editor.apply();
    }

    /**
     * 封装一个显示Toast的方法，方便调用
     * 
     * @param message 要显示的消息
     */
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
