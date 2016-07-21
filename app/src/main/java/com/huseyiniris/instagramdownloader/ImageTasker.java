package com.huseyiniris.instagramdownloader;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.ImageView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

public class ImageTasker extends AsyncTask<Void,Void,Void> {
    
    
    private String mRequestUrl;
    private ProgressDialog mProgressDialog;
    private Activity mActivity;
    private ImageView mGetImageView;

    private Bitmap mImageBitmap;
    private URL mResponseUrl;


    public ImageTasker(Activity activity,ImageView imageView, String requestUrl){
        mGetImageView = imageView;
        mRequestUrl = requestUrl;
        mActivity = activity;
        mProgressDialog = new ProgressDialog(activity);
    }
    @Override
    protected Void doInBackground(Void... params) {
        getImageURL(mRequestUrl);
        return null;
    }
    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        mGetImageView.setImageBitmap(mImageBitmap);
        mProgressDialog.dismiss();
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setMessage("Loading");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }
    private void getImageURL(String URL){
        try {
            mResponseUrl = new URL(URL);
            HttpURLConnection con = (HttpURLConnection) mResponseUrl.openConnection();
            con.setRequestMethod("GET");
            con.setDoInput(true);
            con.setRequestProperty("User-Agent", "Mozilla/5.0");

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;

            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            in.close();

            Document doc = Jsoup.parse(response.toString());
            String element = doc.select("meta[property=og:image]").attr("content");

            mResponseUrl = new URL(element);
            System.out.println(element);
            mImageBitmap = BitmapFactory.decodeStream(mResponseUrl.openConnection().getInputStream());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
