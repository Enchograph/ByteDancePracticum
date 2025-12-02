package com.example.weatherapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.weatherapp.adapter.ForecastAdapter;
import com.example.weatherapp.api.WeatherResponse;
import com.example.weatherapp.databinding.FragmentForecastBinding;

public class ForecastFragment extends Fragment {

    private FragmentForecastBinding binding;
    private WeatherResponse.Forecast forecastData;

    public static ForecastFragment newInstance(WeatherResponse.Forecast forecast) {
        ForecastFragment fragment = new ForecastFragment();
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
        binding = FragmentForecastBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.rvForecast.setLayoutManager(new LinearLayoutManager(getContext()));
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

        binding.tvForecastCity.setText(forecastData.city);
        ForecastAdapter adapter = new ForecastAdapter(getContext(), forecastData.casts);
        binding.rvForecast.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
