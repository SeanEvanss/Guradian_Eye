package com.example.eagleeye.Model;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.widget.Toast;

import com.example.eagleeye.ElderlyMenuActivity;
import com.example.eagleeye.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MyGlobals {
    Context mContext;
    Activity currentActivity;
    String TAG= "Global function";
    String ack= "acknowledge?request=";

    public MyGlobals(Context context,Activity activity){
        this.mContext= context;
        this.currentActivity= activity;
    }

    public RequestBody buildRequestBody(String msg){
        String postBodyString;
        MediaType mediaType;
        RequestBody requestBody;

        postBodyString= msg;
        mediaType = MediaType.parse("text/plain");
        requestBody = RequestBody.create(mediaType, postBodyString);
        return requestBody;
    }

    public void acknowledgement(String message, String URL){
        RequestBody requestBody = buildRequestBody(message);
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request
                .Builder()
                .post(requestBody)
                .url(URL)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                Log.i(TAG, "onFailure:"+e.getMessage());
                currentActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Toast.makeText(mContext, "Something went wrong:" + " " + e.getMessage(), Toast.LENGTH_LONG).show();
                        Log.i(TAG, "run: failed: "+ e.getMessage());
                        call.cancel();
                    }
                });

            }
            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                currentActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //Log.i(TAG, "onFailure: "+ response.body().string());
                            //Toast.makeText(mContext, response.peekBody(2048).string(), Toast.LENGTH_LONG).show();
                            //mDebug.setText(response.body().string());
                            Log.i(TAG, "run: ack successfully sent: ");


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });


            }
        });
    }

    public void postRequest(String message, String URL) {
        postRequest(message, URL, null);
    }

    public void postRequest(String message, String URL, OnResponseActionCallBack cb) {
        RequestBody requestBody = buildRequestBody(message);
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request
                .Builder()
                .post(requestBody)
                .url(URL)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                Log.i(TAG, "onFailure:"+e.getMessage());
                currentActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mContext, "Something went wrong:" + " " + e.getMessage(), Toast.LENGTH_LONG).show();
                        Log.i(TAG, "run: failed: "+ e.getMessage());
                        call.cancel();
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (cb != null) {
                    String JSONresponse= response.body().string();
                    cb.doAction(JSONresponse);
                }
                currentActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (cb != null) {
                                //String JSONresponse= response.body().string();
                                //cb.doAction(JSONresponse);

                            }
                            else{
                                String JSONresponse= response.body().string();
                                //Toast.makeText(mContext, JSONresponse, Toast.LENGTH_LONG).show();
                                //mDebug.setText(response.body().string());
                                Log.i(TAG, "run: ack success: "+JSONresponse);

                                JSONObject json = new JSONObject(JSONresponse);
                                String request_ID= json.getJSONObject("result").getJSONObject("details").getString("id");
                                Log.i(TAG, "JSON ack: "+request_ID);

                                acknowledgement("Ack call", mContext.getString(R.string.api_url)+ack+request_ID);
                            }
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    public interface OnResponseActionCallBack{
        public void doAction(String data);
    }

}
