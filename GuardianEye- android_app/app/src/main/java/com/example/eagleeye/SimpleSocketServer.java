package com.example.eagleeye;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.eagleeye.Model.MyGlobals;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLOutput;
import java.util.ArrayList;

public class SimpleSocketServer extends Thread
{
    private ServerSocket serverSocket;
    private int port;
    private boolean running = false;
    private boolean isRequest= false;
    private LinearLayout linearLayout;
    private Activity act;

    private MyGlobals mGlobals;
    private int requestType; // 0 for emergencies, 1 for tasks, 2 for rides

    private ArrayList<String[]> JSONInfo;
    public SimpleSocketServer(int port, LinearLayout menu, Activity act, int requestType )
    {
        this.port = port;
        this.linearLayout = menu;
        this.act = act;
        this.requestType= requestType;
    }

    public void startServer()
    {
        try
        {
            serverSocket = new ServerSocket( port );
            this.start();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void stopServer()
    {
        running = false;
        this.interrupt();
    }

    @Override
    public void run()
    {
        running = true;

        //Do a manual call here
        mGlobals= new MyGlobals(act.getApplicationContext(), act);
        mGlobals.postRequest("Null", act.getApplicationContext().getString(R.string.api_url) + "volunteersneeded", new MyGlobals.OnResponseActionCallBack(){
            @Override
            public void doAction(String JSONdata) {
                try {
                    //Process JSON here
                    JSONInfo= new ArrayList<String[]>();


                    if(requestType==1){ //for tasks
                        JSONObject base= new JSONObject(JSONdata);
                        JSONArray obj = base.getJSONArray("result");
                        for(int i=0; i< obj.length();++i){
                            JSONObject curr_obj= obj.getJSONObject(i);
                            JSONObject client= curr_obj.getJSONObject("client");
                            JSONObject details= curr_obj.getJSONObject("details");

                            if(details.getString("type").equals("20002") || details.getString("type").equals("20003")) {
                                String name = client.getString("name");
                                String location= details.getJSONObject("details").getString("location");
                                String destination= details.getJSONObject("details").getString("destination");
                                String request_id = details.getString("id");

                                Log.i("SEAN", "details ID: " + request_id.toString());

                                String[] data = {request_id, name, location, destination};
                                JSONInfo.add(data);
                            }
                        }
                    }
                    else { //for emergencies
                        JSONObject base= new JSONObject(JSONdata);
                        JSONArray obj = base.getJSONArray("result");
                        for (int i = 0; i < obj.length(); ++i) {
                            JSONObject curr_obj = obj.getJSONObject(i);
                            JSONObject client = curr_obj.getJSONObject("client");
                            JSONObject details = curr_obj.getJSONObject("details");

                            //We only save the entires that conform to emergencies
                            if(details.getString("type").equals("20001")){
                                String name = client.getString("name");
                                String address = client.getString("address");
                                String request_id = details.getString("id");

                                Log.i("SEAN", "details ID: " + request_id.toString());

                                String[] data = {request_id, name, address};
                                JSONInfo.add(data);
                            }
                        }
                    }
                    //Boolean volunteer = obj.getJSONObject("details").getJSONObject("requesting").getBoolean("volunteer");
                    //Integer status = obj.getJSONObject("details").getInt("status");

//              if (status == 30001){
                    if (true){

                        act.runOnUiThread(new Runnable() {

                            @Override
                            public void run() {

                                //This is for requesting

                                //Maybe rvoerride or replace the current textview
                                //Pass layout here and create/update textview with drive/volunteer found + name
                                Log.i("SEAN", "run: attempt to run display");
                                display(linearLayout,JSONInfo);

                            }
                        });
                    }
                }catch(JSONException e){
                    e.printStackTrace();
                }

            }
        });

        while( running )
        {
            try
            {
                System.out.println( "Listening for a connection" );

                // Call accept() to receive the next connection
                Socket socket = serverSocket.accept();
                System.out.println("Accepted.");
                // Pass the socket to the RequestHandler thread for processing
                RequestHandler requestHandler = new RequestHandler( socket, linearLayout, act, requestType );
                requestHandler.start();


            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

            System.out.println( "Server shutdown" );
        }
    }



class RequestHandler extends Thread {
    private Socket socket;
    private LinearLayout linearLayout;
    private Activity act;
    private int requestType;

    RequestHandler(Socket socket, LinearLayout menu, Activity act, int requestType) {
        this.socket = socket;
        this.linearLayout = menu;
        this.act = act;
        this.requestType= requestType;
    }


    @Override
    public void run() {

        try {
            System.out.println("Received a connection");
            //We do a check for the current tasks/emergencies when we load the socket

            // Get input and output streams
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream());
            out.println("Hello from volunteer.");
            out.flush();
            // Write out our header to the client

            String message = in.readLine();
            Log.i("SEAN", "run: "+message);
            //Toast.makeText(,"Succeeded",Toast.LENGTH_LONG).show();
            try {
                //Process JSON here
                JSONInfo= new ArrayList<String[]>();


                if(requestType==1){ //for tasks
                    JSONArray obj = new JSONArray(message);
                    for(int i=0; i< obj.length();++i){
                        JSONObject curr_obj= obj.getJSONObject(i);
                        JSONObject client= curr_obj.getJSONObject("client");
                        JSONObject details= curr_obj.getJSONObject("details");

                        if(details.getString("type").equals("20002") || details.getString("type").equals("20003")) {
                            String name = client.getString("name");
                            String location= details.getJSONObject("details").getString("location");
                            String destination= details.getJSONObject("details").getString("destination");
                            String request_id = details.getString("id");

                            Log.i("SEAN", "details ID: " + request_id.toString());

                            String[] data = {request_id, name, location, destination};
                            JSONInfo.add(data);
                        }
                    }
                }
                else { //for emergencies
                    JSONArray obj = new JSONArray(message);
                    for (int i = 0; i < obj.length(); ++i) {
                        JSONObject curr_obj = obj.getJSONObject(i);
                        JSONObject client = curr_obj.getJSONObject("client");
                        JSONObject details = curr_obj.getJSONObject("details");

                        //We only save the entires that conform to emergencies
                        if(details.getString("type").equals("20001")){
                            String name = client.getString("name");
                            String address = client.getString("address");
                            String request_id = details.getString("id");

                            Log.i("SEAN", "details ID: " + request_id.toString());

                            String[] data = {request_id, name, address};
                            JSONInfo.add(data);
                        }
                    }
                }
                //Boolean volunteer = obj.getJSONObject("details").getJSONObject("requesting").getBoolean("volunteer");
                //Integer status = obj.getJSONObject("details").getInt("status");

//              if (status == 30001){
                if (true){

                        act.runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            //This is for requesting

                            //Maybe rvoerride or replace the current textview
                            //Pass layout here and create/update textview with drive/volunteer found + name
                            Log.i("SEAN", "run: attempt to run display");
                            display(linearLayout,JSONInfo);

                        }
                    });
                }
            }catch(JSONException e){
                out.println("Not in proper JSON format.");
                out.flush();
                e.printStackTrace();
            }

            System.out.println("start of closing");

            // Close our connection
            in.close();
            out.close();
            socket.close();

            System.out.println("Connection closed");

        } catch (IOException e) {
            e.printStackTrace();

        }

    }
}
    protected void display(LinearLayout scrolllinearLayout, ArrayList<String[]> taskInfo){

        StringBuilder response= new StringBuilder();
        scrolllinearLayout.removeAllViews();


        for(int i=0; i< (taskInfo.size());i++){

            //For tasks
            if(requestType==1){
                response.append(taskInfo.get(i)[1]+"\n");// name
                response.append(taskInfo.get(i)[2]);// address
                response.append(taskInfo.get(i)[3]);// destination
            }
            else{
                response.append(taskInfo.get(i)[1]+"\n");// name
                response.append(taskInfo.get(i)[2]);// address
            }


            Button curr_task= new Button(scrolllinearLayout.getContext());

            curr_task.setSingleLine(false);
            curr_task.setText(response);
            curr_task.setTextSize(20);
            curr_task.setTag(i);
            curr_task.setWidth(20);
            curr_task.setGravity(Gravity.CENTER);

            curr_task.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Toast was for debuggng purposes
                    //Toast.makeText(getApplicationContext(), "Curr button "+current_button_ID, Toast.LENGTH_LONG).show();
                    mGlobals= new MyGlobals(act.getApplicationContext(),act);
                    mGlobals.postRequest("Emergency alert sent", act.getApplicationContext().getString(R.string.api_url)+
                            "volunteerfound?id=0&request="+taskInfo.get((Integer)curr_task.getTag())[0].toString());


                    new AlertDialog.Builder(view.getContext())
                            .setTitle("Emergency accepted")
                            .setMessage("Is this emergency completed ?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    scrolllinearLayout.removeView(curr_task);
                                    mGlobals.postRequest("Completed", act.getApplicationContext().getString(R.string.api_url)+
                                            "completed?id="+taskInfo.get((Integer)curr_task.getTag())[0].toString());
                                    dialogInterface.dismiss();
                                }
                            })
                            /*
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            })

                             */
                            .show();
                }
            });
            scrolllinearLayout.addView(curr_task);
            response.setLength(0);
        }

    }


}



