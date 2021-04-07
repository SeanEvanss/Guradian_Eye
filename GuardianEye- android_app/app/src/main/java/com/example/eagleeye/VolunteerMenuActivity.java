package com.example.eagleeye;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class VolunteerMenuActivity extends AppCompatActivity {
    private Button mVolunteerPersonalBtn,mVolunteerEmergencyBtn, mVolunteerRideBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_menu);

        MyThread thread = new MyThread();
        thread.start();

        mVolunteerPersonalBtn = (Button) findViewById(R.id.volunteerPersonalBtn);
        mVolunteerEmergencyBtn = (Button) findViewById(R.id.volunteerEmergencyBtn);
        mVolunteerRideBtn = (Button) findViewById(R.id.volunteerRideBtn);

        mVolunteerPersonalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (VolunteerMenuActivity.this, ViewAcceptedTaskActivity.class);
                startActivity(intent);
            }
        });

        mVolunteerEmergencyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (VolunteerMenuActivity.this, ViewAcceptedEmergencyActivity.class);
                startActivity(intent);
            }
        });
        mVolunteerRideBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (VolunteerMenuActivity.this, ViewAcceptedRideActivity.class);
                startActivity(intent);
            }
        });

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

        SimpleSocketServer_old server = new SimpleSocketServer_old( port );
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