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
    String ack= "/acknowledge?request=";

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
                        Toast.makeText(mContext, "Something went wrong:" + " " + e.getMessage(), Toast.LENGTH_LONG).show();
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
                            Toast.makeText(mContext, response.peekBody(2048).string(), Toast.LENGTH_LONG).show();
                            //mDebug.setText(response.body().string());
                            Log.i(TAG, "run: ack success: "+response.peekBody(2048).string());


                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });


            }
        });
    }

    public void postRequest(String message, String URL) {
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
                currentActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //Log.i(TAG, "onFailure: "+ response.body().string());
                            String JSONresponse= response.peekBody(2048).string();
                            Toast.makeText(mContext, response.peekBody(2048).string(), Toast.LENGTH_LONG).show();
                            //mDebug.setText(response.body().string());
                            Log.i(TAG, "run: success: "+response.peekBody(2048).string());

                                /*
                            JSONObject json = new JSONObject(JSONresponse);
                            String errorCode= json.getString("errorcode");
                            String requestID= json.getJSONObject("details").getString("id");

                            Log.i(TAG, "run: "+errorCode+" "+requestID);

                                 */
                            acknowledgement("Ack call", "http://10.27.50.205:5000/"+ack+"69");


                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });


            }
        });
    }
}
