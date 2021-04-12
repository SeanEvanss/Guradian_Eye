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

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;

public class ViewAcceptedEmergencyActivity extends AppCompatActivity {
    private ArrayList<String[]> emergenciesInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_emergencies);


        emergenciesInfo= new ArrayList<String[]>();

        for(int i=0;i < 20; i++) {
            String[] data = {"123 sad avenue", "John Walker no. "+Integer.toString(i)};
            emergenciesInfo.add(data);
        }

        LinearLayout scrolllinearLayout=findViewById(R.id.scrollLinearView);
        display(scrolllinearLayout, emergenciesInfo);

        MyThread thread= new MyThread();
        thread.start();

    }

    protected void display(LinearLayout scrolllinearLayout, ArrayList<String[]> taskInfo){

        StringBuilder response= new StringBuilder();
        scrolllinearLayout.removeAllViews();

        for(int i=0; i< (taskInfo.size());i++){


            response.append(taskInfo.get(i)[0]+"\n");// Origin location
            response.append(taskInfo.get(i)[1]);// Name

            Button curr_task= new Button(scrolllinearLayout.getContext());

            curr_task.setSingleLine(false);
            curr_task.setText(response);
            curr_task.setTextSize(25);
            curr_task.setTag(i);
            curr_task.setWidth(20);
            curr_task.setGravity(Gravity.CENTER);

            curr_task.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Toast was for debuggng purposes
                    //Toast.makeText(getApplicationContext(), "Curr button "+current_button_ID, Toast.LENGTH_LONG).show();

                    new AlertDialog.Builder(view.getContext())
                            .setTitle("Respond to emergency ?")
                            .setMessage("Are you sure you want to respond to this emergency ?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    scrolllinearLayout.removeView(curr_task);
                                    dialogInterface.dismiss();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            })
                            .show();
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