package com.example.weatherapp.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.weatherapp.R;
import com.example.weatherapp.api.ApiService;
import com.example.weatherapp.api.WeatherResponse;
import com.example.weatherapp.databinding.ActivityMainBinding;
import com.example.weatherapp.fragment.ForecastFragment;
import com.example.weatherapp.fragment.TodayFragment;
import com.example.weatherapp.model.City;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private ApiService apiService;
    private final String API_KEY = "cd6c6c9320319c582f22243f38db7d11";

    private List<City> cityList = new ArrayList<>();
    private City currentCity;
    private WeatherResponse.Forecast currentForecast;

    private TodayFragment todayFragment;
    private ForecastFragment forecastFragment;
    private Fragment activeFragment;
    private FragmentManager fm;

    private List<Button> cityButtons = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 沉浸式状态栏
        getWindow().getDecorView()
                .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        initRetrofit();
        initCities();
        initFragments(savedInstanceState);
        initBottomNav();

        // 默认加载第一个城市的天气
        if (cityList.size() > 0) {
            switchCity(cityList.get(2)); // 默认选中广州
        }
    }

    private void initRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://restapi.amap.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);
    }

    private void initCities() {
        // adcode: 北京-110000, 上海-310000, 广州-440100, 深圳-440300
        cityList.add(new City("北京", "110000"));
        cityList.add(new City("上海", "310000"));
        cityList.add(new City("广州", "440100"));
        cityList.add(new City("深圳", "440300"));

        createCityButtons();
    }

    private void createCityButtons() {
        binding.cityButtonsContainer.removeAllViews();
        cityButtons.clear();

        for (City city : cityList) {
            Button button = new Button(this);
            button.setText(city.name);
            button.setTextColor(Color.WHITE);
            button.setBackgroundResource(R.drawable.btn_city_normal);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    dpToPx(80), // 宽度
                    dpToPx(40) // 高度
            );
            params.setMargins(dpToPx(8), 0, dpToPx(8), 0);
            button.setLayoutParams(params);
            button.setOnClickListener(v -> switchCity(city));

            binding.cityButtonsContainer.addView(button);
            cityButtons.add(button);
        }
    }

    private void switchCity(City city) {
        this.currentCity = city;
        updateCityButtonStyles();
        fetchWeatherData(city.adcode);
    }

    private void updateCityButtonStyles() {
        for (int i = 0; i < cityList.size(); i++) {
            Button button = cityButtons.get(i);
            if (cityList.get(i).adcode.equals(currentCity.adcode)) {
                button.setBackgroundResource(R.drawable.btn_city_selected);
            } else {
                button.setBackgroundResource(R.drawable.btn_city_normal);
            }
        }
    }

    private void initFragments(Bundle savedInstanceState) {
        fm = getSupportFragmentManager();
        if (savedInstanceState == null) {
            todayFragment = TodayFragment.newInstance(null);
            forecastFragment = ForecastFragment.newInstance(null);
            fm.beginTransaction()
                    .add(R.id.fragment_container, forecastFragment, "2").hide(forecastFragment)
                    .add(R.id.fragment_container, todayFragment, "1")
                    .commit();
            activeFragment = todayFragment;
        } else {
            todayFragment = (TodayFragment) fm.findFragmentByTag("1");
            forecastFragment = (ForecastFragment) fm.findFragmentByTag("2");
            // 根据保存的状态恢复 activeFragment (此处简化，默认 today)
            activeFragment = todayFragment;
        }
    }

    private static final int ID_TODAY = 1;
    private static final int ID_FORECAST = 2;

    private void initBottomNav() {
        BottomNavigationView navView = binding.bottomNavigationBar;
        Menu menu = navView.getMenu();
        // 使用我们自己定义的整数ID
        menu.add(Menu.NONE, ID_TODAY, Menu.NONE, "城市").setIcon(R.drawable.ic_city);
        menu.add(Menu.NONE, ID_FORECAST, Menu.NONE, "预测").setIcon(R.drawable.ic_forecast);

        navView.setOnItemSelectedListener(item -> {
            // 在监听器中也使用相同的ID进行判断
            if (item.getItemId() == ID_TODAY) {
                fm.beginTransaction().hide(activeFragment).show(todayFragment).commit();
                activeFragment = todayFragment;
                return true;
            } else if (item.getItemId() == ID_FORECAST) {
                fm.beginTransaction().hide(activeFragment).show(forecastFragment).commit();
                activeFragment = forecastFragment;
                return true;
            }
            return false;
        });
    }

    private void fetchWeatherData(String cityAdcode) {
        apiService.getWeather(cityAdcode, "all", API_KEY).enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null && "1".equals(response.body().status)) {
                    currentForecast = response.body().forecasts.get(0);
                    // 更新两个Fragment的数据
                    todayFragment.updateWeatherData(currentForecast);
                    forecastFragment.updateWeatherData(currentForecast);
                } else {
                    Toast.makeText(MainActivity.this, "获取天气失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "网络错误: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private int dpToPx(int dp) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                getResources().getDisplayMetrics());
    }
}
