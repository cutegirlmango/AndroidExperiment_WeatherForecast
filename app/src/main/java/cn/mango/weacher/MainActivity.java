package cn.mango.weacher;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import cn.mango.weacher.Model.MoreWeather;
import cn.mango.weacher.Model.WeatherModel;
import cn.mango.weacher.Net.HttpMethod;
import cn.mango.weacher.Net.NetConnection;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TextView tempTextView;
    private TextView cityTextView;
    private TextView weatherTextView;
    private TextView windTextView;

    private TextView readTextView;

    public String cityCode = "101010100";  // 默认北京

    private final Handler mhandle = new Handler();
    private Runnable timeRunable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        this.tempTextView = findViewById(R.id.temp_textView);
        this.cityTextView = findViewById(R.id.city_textView);
        this.weatherTextView = findViewById(R.id.textView_weather);
        this.windTextView = findViewById(R.id.textView_wind);
        this.readTextView = findViewById(R.id.textView_read);

    }

    @Override
    protected void onStart() {
        super.onStart();

        loadData();
    }

    public void loadData() {
        new NetConnection(MainActivity.this, "http://aider.meizu.com/app/weather/listWeather?cityIds=" + cityCode, HttpMethod.GET, new NetConnection.SuccessCallback(){
            @Override
            public void onSuccess(String result) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(result);

                    if (jsonObject.getInt("code") == 200){
                        jsonObject = jsonObject.getJSONArray("value").getJSONObject(0);

                        WeatherModel weatherModel = new WeatherModel();
                        weatherModel.setCity(jsonObject.getString("city"));

                        String[] indexes = new String[jsonObject.getJSONArray("indexes").length()];
                        for (int i = 0; i < jsonObject.getJSONArray("indexes").length(); i++) {
                            indexes[i] = jsonObject.getJSONArray("indexes").getJSONObject(i).getString("content");
                        }
                        weatherModel.setIndexes(indexes);

                        weatherModel.setTemp(jsonObject.getJSONObject("realtime").getString("temp"));
                        weatherModel.setWeather(jsonObject.getJSONObject("realtime").getString("weather"));
                        weatherModel.setWind(jsonObject.getJSONObject("realtime").getString("wD") + " - " + jsonObject.getJSONObject("realtime").getString("wS"));

                        MoreWeather[] moreWeather = new MoreWeather[jsonObject.getJSONArray("weathers").length()]
                                ;
                        for (int i = 0; i < moreWeather.length; i++) {
                            JSONObject obj = jsonObject.getJSONArray("weathers").getJSONObject(i);
                            moreWeather[i] = new MoreWeather(obj.getString("week"), obj.getString("weather"), obj.getString("temp_day_c") + "°", obj.getString("temp_night_c") + "°");
                        }

                        weatherModel.setMore(moreWeather);

                        refreshView(weatherModel);
                    }else{
                        Toast.makeText(MainActivity.this, "网络请求失败", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new NetConnection.FailCallback(){

            @Override
            public void onFail() {
                Toast.makeText(MainActivity.this, "网络请求失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void refreshView(final WeatherModel weatherModel) {
        this.tempTextView.setText(weatherModel.getTemp());
        this.cityTextView.setText(weatherModel.getCity());
        this.weatherTextView.setText(weatherModel.getWeather());
        this.windTextView.setText(weatherModel.getWind());

        if (timeRunable != null) {
            mhandle.removeCallbacks(timeRunable);
        }

        timeRunable = new Runnable() {
            @Override
            public void run() {
                int index = (int)(Math.random() * (weatherModel.getIndexes().length - 1));
                readTextView.setText(weatherModel.getIndexes()[index]);
                    //递归调用本runable对象，实现每隔一秒一次执行任务
                mhandle.postDelayed(this, 3000);
            }
        };
        mhandle.post(timeRunable);

        // 刷新一周天气


        int[] days = { R.id.day_0, R.id.day_1, R.id.day_2, R.id.day_3, R.id.day_4 };
        int[] imgs = { R.id.img_0, R.id.img_1, R.id.img_2, R.id.img_3, R.id.img_4 };
        int[] temps = { R.id.temp_0, R.id.temp_1, R.id.temp_2, R.id.temp_3, R.id.temp_4 };
        for (int i = 0; i < 5; i++) {
            // 刷新日期
            Log.d("xxxx","" + weatherModel.getMore().length);

            MoreWeather more = weatherModel.getMore()[i];
            ((TextView)findViewById(days[i])).setText(more.getWeek());
            ((ImageView)findViewById(imgs[i])).setImageResource(more.getWeather());
            ((TextView)findViewById(temps[i])).setText(more.getDay_c() + "/" + more.getNight_c());
        }

        Toast.makeText(this, "数据刷新成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.beijing_item) {
            this.cityCode = "101010100";
            loadData();
        } else if (id == R.id.jiangxi_item) {
            this.cityCode = "101240601";
            loadData();
        } else if (id == R.id.jinan_item) {
            this.cityCode = "101120101";
            loadData();
        } else if (id == R.id.nav_manage) {
            Intent intent = new Intent(this, CityListActivity.class);
            startActivityForResult(intent, 0);
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            this.cityCode = data.getStringExtra("cityCode");
            loadData();
        }
    }
}
