package com.example.utills;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;

import com.example.syncher.BaseSyncher;
import org.json.JSONObject;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;


public class HTTPUtils {

    public static String getDataFromServer(String urlData, String requestedMethod, boolean accessToken) throws MalformedURLException, IOException, ProtocolException {
        return getDataFromServer(urlData, requestedMethod, null, accessToken);
    }
    public static String getDataFromServer(String urlData, String requestedMethod)
            throws MalformedURLException, IOException, ProtocolException {

        return getDataFromServer(urlData, requestedMethod, null,true);
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
        System.out.println("Response received for " + urlData + "\n*" + response.toString()+"*");
        return response.toString().trim();
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

    public static String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        System.out.println(" JPEG Bitmap size : " + temp.length() + " JPEG Byte array length :" + b.length);
        return temp;
    }

    public static Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0,
                    encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    public static Bitmap getBitmapFromCameraData(Intent data, Context context) {
        Uri selectedImage = data.getData();
        String[] filePathColumn = {MediaStore.MediaColumns.DATA};
        Cursor cursor = context.getContentResolver().query(selectedImage,
                filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();
        BitmapFactory.Options option = new BitmapFactory.Options();
        option.inJustDecodeBounds = true;
        int sample = 32;
        BitmapFactory.decodeFile(picturePath, option);
        if (option.outHeight > 960 || option.outWidth > 200) {
            if (option.outHeight > option.outWidth) {
                sample = option.outHeight / 200;
            } else {
                sample = option.outWidth / 200;
            }
        }
        option.inSampleSize = sample;
        option.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(picturePath, option);
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap scaleBitmapImage,
                                                int width) {
        // TODO Auto-generated method stub
        int targetWidth = width;
        int targetHeight = width;
        Bitmap targetBitmap = Bitmap.createBitmap(targetWidth, targetHeight,
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(targetBitmap);
        Path path = new Path();
        path.addCircle(((float) targetWidth - 1) / 2,
                ((float) targetHeight - 1) / 2,
                (Math.min(((float) targetWidth), ((float) targetHeight)) / 2),
                Path.Direction.CCW);
        canvas.clipPath(path);
        Bitmap sourceBitmap = scaleBitmapImage;
        canvas.drawBitmap(sourceBitmap, new Rect(0, 0, sourceBitmap.getWidth(),
                sourceBitmap.getHeight()), new Rect(0, 0, targetWidth,
                targetHeight), null);
        return targetBitmap;
    }

    public static Bitmap getImageBitmapFromUrl(URL url)
    {
        Bitmap bm = null;
        try {
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            if(conn.getResponseCode() != 200)
            {
                return bm;
            }
            conn.connect();
            InputStream is = conn.getInputStream();

            BufferedInputStream bis = new BufferedInputStream(is);
            try
            {
                bm = BitmapFactory.decodeStream(bis);
            }
            catch(OutOfMemoryError ex)
            {
                bm = null;
            }
            bis.close();
            is.close();
        } catch (Exception e) {}

        return bm;
    }

    public static String convertDateToString(Date indate) {
        String dateString = null;
        SimpleDateFormat sdfr = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        try {
            dateString = sdfr.format(indate);
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return dateString;
    }
}
