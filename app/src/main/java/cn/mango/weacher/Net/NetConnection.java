package cn.mango.weacher.Net;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by json on 2018/10/13.
 */

public class NetConnection {

    private static final String TAG = "NetConnection";

    public NetConnection(final Context context, final String url, final HttpMethod method, final SuccessCallback successCallback, final FailCallback failCallback, final String ...kvs){
        Log.d(TAG,"网络请求");
        new AsyncTask<Void,Void,String>(){
            @Override
            protected String doInBackground(Void... params) {
                Log.d(TAG,"多线程开启");
                // 创建参数对
                StringBuffer paramsStr = new StringBuffer();
                for (int i = 0; i < kvs.length; i+=2) {
                    paramsStr.append(kvs[i]).append("=").append(kvs[i+1]).append("&");
                }

                try {
                    URLConnection uc = null;

                    switch (method){
                        case POST:
                            uc = new URL(url).openConnection();
                            uc.setDoOutput(true);
                            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(uc.getOutputStream(),"utf-8"));
                            bw.write(paramsStr.toString());
                            break;
                        default:
                            uc = new URL(url).openConnection();

                            break;
                    }



                    uc.connect();

                    // 读取数据
                    BufferedReader br = new BufferedReader(new InputStreamReader(uc.getInputStream(),"utf-8"));
                    String line = null;
                    StringBuffer result = new StringBuffer();

                    while((line = br.readLine()) != null){
                        result.append(line);
                    }


                    Log.d(TAG,result.toString());

                    return result.toString();


                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                if (s != null){
                    if (successCallback != null){
                        successCallback.onSuccess(s);
                    }
                }else{
                    if (failCallback != null){
                        failCallback.onFail();
                    }
                }

                super.onPostExecute(s);
            }
        }.execute();
    }

    public static interface SuccessCallback{
        void onSuccess(String result);
    }

    public static interface FailCallback{
        void onFail();
    }
}