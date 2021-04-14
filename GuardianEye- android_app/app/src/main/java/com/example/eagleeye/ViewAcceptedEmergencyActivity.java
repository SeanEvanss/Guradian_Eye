package com.example.eagleeye;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.eagleeye.Adapter.Acceptedemergencyadapter;
import com.example.eagleeye.Model.MyGlobals;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;

public class ViewAcceptedEmergencyActivity extends AppCompatActivity {
    private ArrayList<String[]> emergenciesInfo;
    protected LinearLayout scrolllinearLayout;

    private MyGlobals mGlobals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_emergencies);
        scrolllinearLayout=findViewById(R.id.scrollLinearView);

        mGlobals= new MyGlobals(getApplicationContext(),ViewAcceptedEmergencyActivity.this);

        MyThread thread= new MyThread();
        thread.start();

        emergenciesInfo= new ArrayList<String[]>();

        //display(scrolllinearLayout, emergenciesInfo);

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

        Log.i("rec-msg", "acceptTask: IP:"+SERVER_IP);

        SimpleSocketServer server = new SimpleSocketServer( port, scrolllinearLayout, ViewAcceptedEmergencyActivity.this,0);
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
}