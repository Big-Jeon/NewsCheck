package com.bigjeon.newscheck;

import android.icu.util.LocaleData;
import android.os.Build;

import androidx.annotation.RequiresApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Search_News {

    public static String clientId = "VZ8bbtgjNNz0eWv2XFgZ";
    public static String clientSecret = "qkrU4bwLrM";
    public String[][] main(String keyword, String Count_first){
        String[][] Values = new String[3][20];
        String text = null;
        try{
            text = URLEncoder.encode(keyword,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("인코딩 실패", e);
        }

        String apiURL = "https://openapi.naver.com/v1/search/news?query=" + text + "&display=20" + "&start=" + Count_first + "sort=date";
        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("X-Naver-Client-Id", clientId);
        requestHeaders.put("X-Naver-Client-Secret", clientSecret);
        String responseBody = get(apiURL,requestHeaders);

        Values = parseData(responseBody);
        return Values;
    }


    private static String get(String apiURL, Map<String, String> requestHeaders) {
        HttpURLConnection con = connect(apiURL);
        try {
            con.setRequestMethod("GET");
            for (Map.Entry<String, String> header : requestHeaders.entrySet()){
                con.setRequestProperty(header.getKey(), header.getValue());
            }
            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK){
                return readBody(con.getInputStream());
            }else {
                return readBody(con.getErrorStream());
            }
        } catch (IOException e) {
            throw new RuntimeException("응답 실패", e);
        }finally {
            con.disconnect();
        }
    }

    private static HttpURLConnection connect(String apiURL) {
        try{
            URL url = new URL(apiURL);
            return (HttpURLConnection)url.openConnection();
        } catch (MalformedURLException e) {
            throw new RuntimeException("API URL 오류" + apiURL, e);
        }catch (IOException e){
            throw new RuntimeException("연결 실패" + apiURL, e);
        }
    }

    private static String readBody(InputStream body){
        InputStreamReader streamReader = new InputStreamReader(body);
        try (BufferedReader lineReader = new BufferedReader(streamReader)){
            StringBuilder responseBody = new StringBuilder();

            String line;
            while ((line = lineReader.readLine()) != null){
                responseBody.append(line);
            } return responseBody.toString();
        } catch (IOException e) {
            throw new RuntimeException("API 응답 읽기 실패", e);
        }
    }


    private static String[][] parseData(String responseBody) {
        String[][] Data = new String[3][20];
            String[] Title = new String[20];
            String[] URL = new String[20];
            String[] WriteDate = new String[20];
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(responseBody.toString());
                JSONArray jsonArray = jsonObject.getJSONArray("items");

                for (int i = 0; i < 20; i++) {
                    JSONObject item = jsonArray.getJSONObject(i);
                    Title[i] = item.getString("title").replace("<b>", "").replace("</b>", "").replace("&quot", "");
                    URL[i] = item.getString("link");
                    //WriteDate[i] = item.getString("pubDate");
                    String datetime = item.getString("pubDate");
                    SimpleDateFormat old_format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);
                    SimpleDateFormat new_format = new SimpleDateFormat("yyyy-MM-dd HH:mm, E", Locale.KOREA);
                    try {
                        Date old_date = old_format.parse(datetime);
                        WriteDate[i] = new_format.format(old_date);
                    }catch (ParseException e){
                        e.printStackTrace();
                    }
                    System.out.println("TITLE : " + Title[i]);
                    System.out.println("URL = " + URL[i]);
                    System.out.println(item.getString("pubDate"));
                    System.out.println("WriteDate : " + WriteDate[i]);
                }

                    for (int i = 0; i < 20; i++){
                        Data[0][i] = Title[i];
                        Data[1][i] = URL[i];
                        Data[2][i] = WriteDate[i];

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return Data;
    }
}
