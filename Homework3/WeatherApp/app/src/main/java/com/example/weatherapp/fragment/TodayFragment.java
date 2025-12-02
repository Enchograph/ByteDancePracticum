package com.example.weatherapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.weatherapp.api.WeatherResponse;
import com.example.weatherapp.databinding.FragmentTodayBinding;

public class TodayFragment extends Fragment {

    private FragmentTodayBinding binding;
    private static final String ARG_WEATHER_DATA = "weather_data";
    private WeatherResponse.Forecast forecastData;

    // 提供一个静态方法来创建Fragment实例并传递数据
    public static TodayFragment newInstance(WeatherResponse.Forecast forecast) {
        TodayFragment fragment = new TodayFragment();
        Bundle args = new Bundle();
        // WeatherResponse.Forecast 需要是可序列化的，我们让其父类和它自己都实现 Serializable
        // args.putSerializable(ARG_WEATHER_DATA, forecast);
        // 为了简单，我们暂时不通过 bundle 传，而是通过公共方法设置
        fragment.setForecastData(forecast);
        return fragment;
    }

    public void setForecastData(WeatherResponse.Forecast forecast) {
        this.forecastData = forecast;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        binding = FragmentTodayBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        updateUI();
    }

    public void updateWeatherData(WeatherResponse.Forecast forecast) {
        this.forecastData = forecast;
        if (binding != null) {
            updateUI();
        }
    }

    private void updateUI() {
        if (forecastData == null || forecastData.casts == null || forecastData.casts.isEmpty())
            return;

        WeatherResponse.Cast todayCast = forecastData.casts.get(0);

        binding.tvCityName.setText(forecastData.city);
        binding.tvWeatherInfo.setText(todayCast.dayweather); // 通常显示白天的天气
        binding.tvTemperature.setText(todayCast.daytemp + "°"); // 通常显示白天的温度
        binding.tvTempRange.setText(String.format("最高: %s° 最低: %s°", todayCast.daytemp, todayCast.nighttemp));
// 2. 设置白天卡片详细信息
        binding.tvDayWeather.setText(todayCast.dayweather);
        binding.tvDayTemp.setText(todayCast.daytemp + "°");
        binding.tvDayWind.setText(todayCast.daywind + " " + todayCast.daypower + "级");
// 3. 设置夜间卡片详细信息
        binding.tvNightWeather.setText(todayCast.nightweather);
        binding.tvNightTemp.setText(todayCast.nighttemp + "°");
        binding.tvNightWind.setText(todayCast.nightwind + " " + todayCast.nightpower + "级");

        // 白天
        binding.tvDayWeather.setText(todayCast.dayweather);
        binding.tvDayTemp.setText(String.format("%s°", todayCast.daytemp));
        binding.tvDayWind.setText(String.format("%s %s级", todayCast.daywind, todayCast.daypower));

        // 夜间
        binding.tvNightWeather.setText(todayCast.nightweather);
        binding.tvNightTemp.setText(String.format("%s°", todayCast.nighttemp));
        binding.tvNightWind.setText(String.format("%s %s级", todayCast.nightwind, todayCast.nightpower));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
