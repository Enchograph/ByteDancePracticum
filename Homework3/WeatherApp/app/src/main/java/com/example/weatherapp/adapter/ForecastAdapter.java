package com.example.weatherapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.weatherapp.R;
import com.example.weatherapp.api.WeatherResponse;
import com.example.weatherapp.databinding.ItemForecastBinding;
import java.util.List;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder> {

    private final Context context;
    private final List<WeatherResponse.Cast> castList;

    public ForecastAdapter(Context context, List<WeatherResponse.Cast> castList) {
        this.context = context;
        this.castList = castList;
    }

    @NonNull
    @Override
    public ForecastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemForecastBinding binding = ItemForecastBinding.inflate(LayoutInflater.from(parent.getContext()), parent,
                false);
        return new ForecastViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ForecastViewHolder holder, int position) {
        WeatherResponse.Cast cast = castList.get(position);

        // 今天、明天、星期X的转换逻辑
        if (position == 0) {
            holder.binding.tvWeek.setText("今天");
        } else if (position == 1) {
            holder.binding.tvWeek.setText("明天");
        } else {
            holder.binding.tvWeek.setText(getWeekDay(cast.week));
        }

        // 格式化日期
        String[] dateParts = cast.date.split("-");
        if (dateParts.length == 3) {
            holder.binding.tvDate.setText(dateParts[1] + "-" + dateParts[2]);
        }

        holder.binding.tvForecastWeather.setText(cast.dayweather);
        holder.binding.tvForecastTempRange.setText(String.format("%s°  %s°", cast.daytemp, cast.nighttemp));

        // 根据天气设置图标
        holder.binding.ivWeatherIcon.setImageResource(getWeatherIcon(cast.dayweather));
    }

    @Override
    public int getItemCount() {
        return castList.size();
    }

    static class ForecastViewHolder extends RecyclerView.ViewHolder {
        ItemForecastBinding binding;

        public ForecastViewHolder(ItemForecastBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private String getWeekDay(String weekNum) {
        switch (weekNum) {
            case "1":
                return "星期一";
            case "2":
                return "星期二";
            case "3":
                return "星期三";
            case "4":
                return "星期四";
            case "5":
                return "星期五";
            case "6":
                return "星期六";
            case "7":
                return "星期日";
            default:
                return "";
        }
    }

    private int getWeatherIcon(String weather) {
        if (weather.contains("晴")) {
            return R.drawable.ic_weather_sunny;
        } else if (weather.contains("多云")) {
            return R.drawable.ic_weather_cloudy;
        } else if (weather.contains("雨")) {
            return R.drawable.ic_weather_rain;
        } else if (weather.contains("阴")) {
            return R.drawable.ic_weather_overcast;
        }
        return R.drawable.ic_weather_sunny; // 默认图标
    }
}
