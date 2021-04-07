package com.example.eagleeye;

import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SimpleSocketServer_old extends Thread
{
    private ServerSocket serverSocket;
    private int port;
    private boolean running = false;

    public SimpleSocketServer_old(int port )
    {
        this.port = port;
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
                RequestHandler requestHandler = new RequestHandler( socket );
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

    RequestHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            System.out.println("Received a connection");

            // Get input and output streams
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream());

            // Write out our header to the client
            out.println("Echo Server 1.0");
            out.flush();

//            // get the input stream from the connected socket
//            InputStream inputStream = socket.getInputStream();
//            // create a DataInputStream so we can read data from it.
//            DataInputStream dataInputStream = new DataInputStream(inputStream);
//            System.out.println(dataInputStream);
            // read the message from the socket
//            String message = dataInputStream.readUTF();
            String message = in.readLine();
            System.out.println("The message sent from the socket was: " + message);
            //Toast.makeText(getContextClassLoader(), message, Toast.LENGTH_SHORT).show();
            Log.d("Rec-msg", "run: "+message);

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



