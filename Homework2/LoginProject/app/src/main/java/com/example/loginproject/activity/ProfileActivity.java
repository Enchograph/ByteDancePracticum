package com.example.loginproject.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.loginproject.R;

// 引用LoginActivity中定义的常量
import static com.example.loginproject.activity.LoginActivity.PREFS_NAME;
import static com.example.loginproject.activity.LoginActivity.KEY_USERNAME;
import static com.example.loginproject.activity.LoginActivity.KEY_SIGNATURE;

public class ProfileActivity extends AppCompatActivity {

    private TextView tvUsername, tvSignature;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // 绑定UI控件
        tvUsername = findViewById(R.id.tv_username);
        tvSignature = findViewById(R.id.tv_signature);

        // 加载并显示用户信息
        loadAndDisplayUserInfo();

        // 初始化并设置条目点击事件
        setupProfileItems();
    }

    /**
     * 从SharedPreferences加载用户信息并显示在UI上
     */
    private void loadAndDisplayUserInfo() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String username = prefs.getString(KEY_USERNAME, "未登录"); // 提供一个默认值
        String signature = prefs.getString(KEY_SIGNATURE, "这个人很懒，什么都没留下"); // 提供一个默认值

        tvUsername.setText(username);
        tvSignature.setText(signature);
    }

    /**
     * 为个人中心的每个条目设置内容和点击事件
     */
    private void setupProfileItems() {
        // 设置“个人信息”条目
        setupItem(R.id.item_personal_info, R.drawable.ic_person, "个人信息");

        // 设置“我的收藏”条目
        setupItem(R.id.item_favorites, R.drawable.ic_favorite, "我的收藏");

        // 设置“浏览历史”条目
        setupItem(R.id.item_history, R.drawable.ic_history, "浏览历史");

        // 设置“设置”条目
        setupItem(R.id.item_settings, R.drawable.ic_settings, "设置");

        // 设置“关于我们”条目
        setupItem(R.id.item_about_us, R.drawable.ic_about_us, "关于我们");

        // 设置“意见反馈”条目
        setupItem(R.id.item_feedback, R.drawable.ic_feedback, "意见反馈");
    }

    /**
     * 封装的通用方法，用于设置单个条目的图标、标题和点击事件
     * 
     * @param viewId    条目根布局的ID
     * @param iconResId 图标的资源ID
     * @param title     标题文字
     */
    private void setupItem(int viewId, int iconResId, final String title) {
        View item = findViewById(viewId);
        ImageView icon = item.findViewById(R.id.iv_item_icon);
        TextView titleView = item.findViewById(R.id.tv_item_title);

        icon.setImageResource(iconResId);
        titleView.setText(title);

        item.setOnClickListener(v -> showToast("点击了 " + title));
    }

    /**
     * 封装一个显示Toast的方法
     * 
     * @param message 要显示的消息
     */
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
