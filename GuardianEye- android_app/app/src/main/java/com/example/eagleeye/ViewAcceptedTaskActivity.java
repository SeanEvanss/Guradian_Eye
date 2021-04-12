package com.example.eagleeye;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;

public class ViewAcceptedTaskActivity extends AppCompatActivity {
    private RecyclerView recview;
    private Acceptedtaskadapter adapter;
    private Button mVolunteerPersonal2BackBtn;
    private ArrayList<String[]> tasksInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_tasks);

        tasksInfo= new ArrayList<String[]>();

        for(int i=0;i < 30; i++) {
            String[] data = {"123 maple road", "456 maple drive", "$"+Integer.toString(i)};
            tasksInfo.add(data);
        }

        LinearLayout scrolllinearLayout=findViewById(R.id.scrollLinearView);
        display(scrolllinearLayout, tasksInfo);

        MyThread thread = new MyThread();
        thread.start();
    }

    protected void display(LinearLayout scrolllinearLayout, ArrayList<String[]> taskInfo){

        StringBuilder response= new StringBuilder();
        scrolllinearLayout.removeAllViews();

        for(int i=0; i< (taskInfo.size());i++){

            String current_button_ID= Integer.toString(i);

            response.append(taskInfo.get(i)[0]);// Origin location
            response.append(taskInfo.get(i)[1]);// Destination
            response.append(taskInfo.get(i)[2]);// Fee

            Button curr_task= new Button(scrolllinearLayout.getContext());

            curr_task.setText(response);
            curr_task.setTextSize(25);
            curr_task.setTag(i);
            curr_task.setWidth(20);
            curr_task.setGravity(Gravity.CENTER);

            curr_task.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getApplicationContext(), "Curr button "+current_button_ID, Toast.LENGTH_LONG).show();
                }
            });
            scrolllinearLayout.addView(curr_task);
            response.setLength(0);
        }

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