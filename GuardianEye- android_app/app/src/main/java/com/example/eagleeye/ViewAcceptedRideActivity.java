package com.example.eagleeye;

import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;

import com.example.eagleeye.Adapter.Accceptedrideadapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.widget.Button;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class ViewAcceptedRideActivity extends AppCompatActivity {

    private RecyclerView recview;
    private Accceptedrideadapter adapter;
    private Button mBackBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_rides);

        //MyThread thread = new MyThread();
        //thread.start();



    }



/*
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

        SimpleSocketServer server = new SimpleSocketServer( port );
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

 */
}