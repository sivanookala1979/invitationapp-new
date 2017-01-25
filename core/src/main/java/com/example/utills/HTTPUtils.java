package com.example.utills;

import com.example.syncher.BaseSyncher;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;


public class HTTPUtils {

    public static String getDataFromServer(String urlData, String requestedMethod, boolean accessToken) throws MalformedURLException, IOException, ProtocolException {
        return getDataFromServer(urlData, requestedMethod, null, accessToken);
    }

    public static String getDataFromServer(String urlData, String requestedMethod, String content, boolean accessToken) throws MalformedURLException, IOException, ProtocolException {
        URL url = new URL(urlData);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod(requestedMethod);
        if (accessToken) {
            con.setRequestProperty("Authorization", BaseSyncher.getAccessToken());
        }
        if (content != null) {
            byte[] postDataBytes = content.getBytes("UTF-8");
            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            con.setDoOutput(true);
            con.setDoInput(true);
            OutputStream outputStream = con.getOutputStream();
            outputStream.write(postDataBytes);
            outputStream.flush();
            outputStream.close();
        }
        con.connect();
        StringBuffer response = new StringBuffer();
        if (con.getResponseCode() != 200) {
            readContent(response, con.getErrorStream());
        } else {
            readContent(response, con.getInputStream());
        }
        System.out.println("Response received for " + urlData + "\n" + response.toString());
        return response.toString();
    }

    public static void readContent(StringBuffer response, InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String inputLine;
        while ((inputLine = reader.readLine()) != null) {
            response.append(inputLine).append("\n");
        }
        reader.close();
    }

    public static String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    public static JSONObject getJSONFromUrl(String url) throws IOException {
        InputStream inputStream = null;
        JSONObject jsonObject = null;
        String json = "";
        HttpURLConnection urlConnection = null;
        try {
            URL url1 = new URL(url);
            urlConnection = (HttpURLConnection) url1.openConnection();
            urlConnection.connect();
            inputStream = urlConnection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            inputStream.close();
            json = sb.toString();
            jsonObject = new JSONObject(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
