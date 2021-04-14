package com.example.eagleeye;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eagleeye.Adapter.Acceptedtaskadapter;
import com.example.eagleeye.Model.ADLTask;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ViewAcceptedTaskActivity extends AppCompatActivity {
    private ArrayList<String[]> tasksInfo;
    LinearLayout scrolllinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_tasks);

        tasksInfo= new ArrayList<String[]>();
        scrolllinearLayout=findViewById(R.id.scrollLinearView);
        //display(scrolllinearLayout, tasksInfo);
        MyThread thread = new MyThread();
        thread.start();
    }


    public class MyThread extends Thread {

        public void run(){

            try{acceptTask(9000);}
            catch(Exception e){e.printStackTrace();}
        }
    }
    public void acceptTask(int args) throws UnknownHostException {

        int port = args;
        String SERVER_IP = getLocalIpAddress();

        System.out.println(SERVER_IP);
        Log.i("rec-msg", "acceptTask: IP:"+SERVER_IP);

        SimpleSocketServer server = new SimpleSocketServer( port, scrolllinearLayout, ViewAcceptedTaskActivity.this,1 );
        server.startServer();

        // Automatically shutdown in 1 minute
        try
        {
            System.out.println("Sleeping");
            Thread.sleep( 1000000 );
        }
        catch( Exception e )
        {
            e.printStackTrace();
        }

        System.out.println( "Shutting down server" );
        server.stopServer();
    }
    private String getLocalIpAddress() throws UnknownHostException {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        assert wifiManager != null;
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipInt = wifiInfo.getIpAddress();
        return InetAddress.getByAddress(ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(ipInt).array()).getHostAddress();
    }
    /*
    public void postRequestVolunteer(String message, String URL) {

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

                Toast.makeText(getApplicationContext(), "Something went wrong:" + " " + e.getMessage(), Toast.LENGTH_LONG).show();
                Log.i("SEAN", "run: failed: "+ e.getMessage());
                call.cancel();

            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                try {
                    //Log.i(TAG, "onFailure: "+ response.body().string());
                    String JSONresponse= response.peekBody(2048).string();



                    Log.i("SEAN", "run: ack success: "+JSONresponse);



                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        });
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
    */


}