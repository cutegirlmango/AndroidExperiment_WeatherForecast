package cn.mango.weacher;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.mango.weacher.Model.CityModel;
import cn.mango.weacher.Model.MoreWeather;
import cn.mango.weacher.Model.WeatherModel;

public class CityListActivity extends AppCompatActivity {

    private ListView listView;

    private String[] allCitys;
    private String[] allCityids;

    private String[] citys;
    private String[] cityids;

    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_list);


        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(CityModel.jsonData);
            JSONArray array = jsonObject.getJSONArray("cities");

            allCitys = new String[array.length()];
            allCityids = new String[array.length()];

            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);

                allCitys[i] = obj.getString("city");
                allCityids[i] = obj.getString("cityid");
            }

            citys = allCitys;
            cityids = allCityids;


        } catch (JSONException e) {
            e.printStackTrace();
        }

        listView = findViewById(R.id.listView);

        adapter = new ArrayAdapter<String>(CityListActivity.this, R.layout.support_simple_spinner_dropdown_item, citys);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("cityCode", cityids[position]);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }

    public void search(View view) {
        final EditText inputServer = new EditText(this);
        inputServer.setFocusable(true);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("搜索").setView(inputServer).setNegativeButton("关闭", null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (inputServer.getText().toString().equals("")){
                    cityids = allCityids;
                    citys = allCitys;
                } else {

                    int index = -1;

                    for (int i = 0; i < allCitys.length; i++) {
                        if (allCitys[i].equals(inputServer.getText().toString())){
                            index = i;
                            break;
                        }
                    }

                    if (index == -1){
                        Toast.makeText(CityListActivity.this, "找不到该城市", Toast.LENGTH_SHORT).show();
                    }else{
                        final int position = index;

                        listView.post(new Runnable() {
                            @Override
                            public void run() {
                                listView.setSelection(position);
                            }
                        });
                    }

                    Log.d("xxxxxxxxxx", inputServer.getText().toString() + "count:" + citys[0]);
                }

            }
        });

        builder.show();
    }
}
