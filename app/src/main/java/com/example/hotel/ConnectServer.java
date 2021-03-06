package com.example.hotel;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.StrictMode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ConnectServer {

    public static Bitmap noimagebit = loadBitmap("http://jonginfi.iptime.org:5000/room/thumbnail/noimage.png");
    public static Drawable noimagedraw = new BitmapDrawable(noimagebit);
    private static int code = 200;

    private static String address = "http://jonginfi.iptime.org:5000";

    //Get으로 연결하여 결과 반환
    public static JSONArray GET(String urlStr){
        if (android.os.Build.VERSION.SDK_INT > 9) { StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build(); StrictMode.setThreadPolicy(policy); }

        JSONArray res = null;
        try {
            //서버 연결
            URL url = new URL(urlStr);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestProperty("content-type", "application/json");
            connection.setRequestMethod("GET");         // 통신방식
            connection.setDoInput(true);                // 읽기모드 지정
            connection.setUseCaches(false);             // 캐싱데이터를 받을지 안받을지
            connection.setConnectTimeout(5000);        // 통신 타임아웃
            int responseCode = connection.getResponseCode();
            code = responseCode;
            if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                try {
                    res = new JSONArray(response.toString());
                }catch (JSONException e){
                    JSONArray arr = new JSONArray();
                    arr.put(new JSONObject( response.toString()));
                    res = arr;
                }
            }
            if( connection != null)
                connection.disconnect();
        }catch (Exception e){
            e.printStackTrace();
        }
        return res;
    }

    //POST로 연결하여 결과 반환
    public static JSONArray POST(String urlStr, JSONObject o){
        if (android.os.Build.VERSION.SDK_INT > 9) { StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build(); StrictMode.setThreadPolicy(policy); }

        JSONArray res = null;
        try {
            //서버 연결
            URL url = new URL(urlStr);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestProperty("content-type", "application/json");
            connection.setRequestMethod("POST");         // 통신방식
            connection.setDoInput(true);                // 읽기모드 지정
            connection.setUseCaches(false);             // 캐싱데이터를 받을지 안받을지
            connection.setConnectTimeout(5000);        // 통신 타임아웃

            OutputStream os = connection.getOutputStream();
            os.write(o.toString().getBytes());
            os.flush();

            int responseCode = connection.getResponseCode();
            code = responseCode;
            if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                try {
                    res = new JSONArray(response.toString());
                }catch (JSONException e){
                    JSONArray arr = new JSONArray();
                    arr.put(new JSONObject( response.toString()));
                    res = arr;
                }
            }
            if( connection != null)
                connection.disconnect();
        }catch (Exception e){
            e.printStackTrace();
        }
        return res;
    }



    //서버에서 이미지 불러오는 함수
    public static Bitmap loadBitmap(String urlstr){
        Bitmap imgBitmap = null;
        HttpURLConnection connection = null;
        BufferedInputStream bis = null;
        try
        {
            URL url = new URL(urlstr);
            connection = (HttpURLConnection)url.openConnection();
            connection.connect();
            int nSize = connection.getContentLength();
            bis = new BufferedInputStream(connection.getInputStream(), nSize);
            imgBitmap = BitmapFactory.decodeStream(bis);
        }
        catch (Exception e){
            e.printStackTrace();
        }finally {
            if(bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(connection != null )
                connection.disconnect();
        }
        return imgBitmap;
    }


    public static int getCode() {
        return code;
    }

    public static String getAddress(String add) {
        return address+add;
    }
    public static String getAddress() {
        return address;
    }

    public static void setAddress(String add) {
        address = add;
    }
}
