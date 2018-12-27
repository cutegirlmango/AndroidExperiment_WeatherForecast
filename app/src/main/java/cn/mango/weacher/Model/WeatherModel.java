package cn.mango.weacher.Model;

public class WeatherModel {
    private String city;
    private String[] indexes;
    private String weather;
    private String wind;
    private String temp;    // 温度
    private MoreWeather[] more; // 更多天气


    public WeatherModel() {
    }

    public WeatherModel(String city, String[] indexes, String weather, String wind, MoreWeather[] more) {
        this.city = city;
        this.indexes = indexes;
        this.weather = weather;
        this.wind = wind;
        this.more = more;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String[] getIndexes() {
        return indexes;
    }

    public void setIndexes(String[] indexes) {
        this.indexes = indexes;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getWind() {
        return wind;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }

    public MoreWeather[] getMore() {
        return more;
    }

    public void setMore(MoreWeather[] more) {
        this.more = more;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }
}

