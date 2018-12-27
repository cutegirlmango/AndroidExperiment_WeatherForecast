package cn.mango.weacher.Model;

import cn.mango.weacher.R;

public class MoreWeather {
    private String week;
    private String weather;
    private String day_c;   // 白天气温
    private String night_c; // 夜间气温

    public MoreWeather() {
    }

    public MoreWeather(String week, String weather, String day_c, String night_c) {
        this.week = week;
        this.weather = weather;
        this.day_c = day_c;
        this.night_c = night_c;
    }

    public String getWeek() {
        String str = null;
        switch (this.week) {
            case "星期一":
                str = "Mon";
                break;
            case "星期二":
                str = "Tue";
                break;
            case "星期三":
                str = "Wed";
                break;
            case "星期四":
                str = "Thu";
                break;
            case "星期五":
                str = "Fri";
                break;
            case "星期六":
                str = "Sat";
                break;
            case "星期日":
                str = "Sun";
                break;
            default:
                str = "";
                break;
        }
        return str;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public int getWeather() {
        int img = 0;
        switch(weather){
            case "晴":
                img = R.mipmap.sunny;
                break;
            case "多云":
                img = R.mipmap.duoyun;
                break;
            case "小雨":
                img = R.mipmap.rain;
                break;
            case "阴":
                img = R.mipmap.other;
                break;
            case "小雪":
                img = R.mipmap.snow;
                break;
            default:
                img = R.mipmap.other;
                break;
        }

        return img;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getDay_c() {
        return day_c;
    }

    public void setDay_c(String day_c) {
        this.day_c = day_c;
    }

    public String getNight_c() {
        return night_c;
    }

    public void setNight_c(String night_c) {
        this.night_c = night_c;
    }
}