package com.example.eagleeye;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

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

public class SimpleSocketServer extends Thread
{
    private ServerSocket serverSocket;
    private int port;
    private boolean running = false;
    private boolean isRequest= false;
    private LinearLayout linearLayout;
    private Activity act;
    public SimpleSocketServer(int port, LinearLayout menu, Activity act )
    {
        this.port = port;
        this.linearLayout = menu;
        this.act = act;
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
        while( running )
        {
            try
            {
                System.out.println( "Listening for a connection" );

                // Call accept() to receive the next connection
                Socket socket = serverSocket.accept();
                System.out.println("Accepted.");
                // Pass the socket to the RequestHandler thread for processing
                RequestHandler requestHandler = new RequestHandler( socket, linearLayout, act );
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

    RequestHandler(Socket socket, LinearLayout menu, Activity act) {
        this.socket = socket;
        this.linearLayout = menu;
        this.act = act;
    }


    @Override
    public void run() {
        try {
            System.out.println("Received a connection");

            // Get input and output streams
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream());
            out.println("Hello from volunteer.");
            out.flush();
            // Write out our header to the client

            String message = in.readLine();
            System.out.println("The message sent from the socket was: " + message);
            try {
                JSONObject obj = new JSONObject(message);


                Boolean volunteer = obj.getJSONObject("details").getJSONObject("requesting").getBoolean("volunteer");
                Integer status = obj.getJSONObject("details").getInt("status");


                if (volunteer && status == 30001){
                    act.runOnUiThread(new Runnable() {


                        @Override
                        public void run() {
                            if(isRequest){
                                //This is for requesting

                                //Maybe rvoerride or replae the current textview
                                //Pass layout here and create/update textview with drive/volunteer found + name 
                            }
                            else{
                                //This is for awaiting
                                TextView textView= new TextView(linearLayout.getContext());
                                try {
                                    String text = obj.getJSONObject("client").getString("name");
                                    System.out.println(text);
                                    textView.setPadding(10,0,0,0);
                                    textView.setText(text);
                                    textView.setTextSize(20);
                                    textView.setBackgroundColor(0xFFFFFF);
                                    linearLayout.addView(textView);}catch(JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                }
            }catch(JSONException e){
                out.println("Not in proper JSON format.");
                out.flush();
                e.printStackTrace();
            }


            // Close our connection
            in.close();
            out.close();
            socket.close();

            System.out.println("Connection closed");

        } catch (IOException e) {
            e.printStackTrace();

        }
    }
}}



