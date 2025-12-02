package com.example.weatherapp.api;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class WeatherResponse {
    @SerializedName("status")
    public String status;
    @SerializedName("forecasts")
    public List<Forecast> forecasts;

    public static class Forecast {
        @SerializedName("city")
        public String city;
        @SerializedName("adcode")
        public String adcode;
        @SerializedName("province")
        public String province;
        @SerializedName("reporttime")
        public String reporttime;
        @SerializedName("casts")
        public List<Cast> casts;
    }

    public static class Cast {
        @SerializedName("date")
        public String date;
        @SerializedName("week")
        public String week;
        @SerializedName("dayweather")
        public String dayweather;
        @SerializedName("nightweather")
        public String nightweather;
        @SerializedName("daytemp")
        public String daytemp;
        @SerializedName("nighttemp")
        public String nighttemp;
        @SerializedName("daywind")
        public String daywind;
        @SerializedName("nightwind")
        public String nightwind;
        @SerializedName("daypower")
        public String daypower;
        @SerializedName("nightpower")
        public String nightpower;
    }
}
