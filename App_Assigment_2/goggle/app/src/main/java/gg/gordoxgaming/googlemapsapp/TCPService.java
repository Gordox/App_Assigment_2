package gg.gordoxgaming.googlemapsapp;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by Gordox on 2016-10-25.
 */
public class TCPService extends Service{

    private static final int PORT = 7117;
    private static final String IP = "195.178.227.53";
    public static final int CONNECTION_TIMEOUT = 5000;

    private Socket socket;
    public InputStream inStream;
    public OutputStream outStream;
    public DataInputStream dataInStream;
    public DataOutputStream dataOutStream;
    private volatile boolean running = false;


    public TCPService(){

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void run(){

        if(running)
            return;

        running = true;
        socket = new Socket();



        try {

            //InetAddress serverAddress = InetAddress.getByName(IP);

            Log.e("TCP Client", "C: Connecting...");

            socket.connect(new InetSocketAddress(InetAddress.getByName(IP), PORT), CONNECTION_TIMEOUT);

            Log.e("TCP Client", "C: Connected");

            try {

                inStream = socket.getInputStream();
                outStream = socket.getOutputStream();

                dataInStream = new DataInputStream(inStream);
                dataOutStream = new DataOutputStream(outStream);
                dataOutStream.flush();

                while(running){
                    try{
                        String result = dataInStream.readUTF();


                    }catch (IOException e) {

                    }
                }



            } catch (Exception e) {
                Log.e("TCP", "S: Error Other", e);
            }


        } catch (Exception e) {
            Log.e("TCP", "C: Error Socket", e);
        }
    }

    public void disconnect(){
        if(!running){
            Log.e("Error: ", "No connection to server");
            return;
        }

        Log.e("Command", "DISCONNECTING");
        try {
            if(socket != null){
                socket.close();
                socket = null;
            }
            if(dataOutStream != null){
                dataOutStream.close();
                dataOutStream = null;
            }
            if(dataInStream != null){
                dataInStream.close();
                dataInStream = null;
            }
            if(outStream != null){
                outStream.close();
                outStream = null;
            }
            if(inStream != null){
                inStream.close();
                inStream = null;
            }

            running = false;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
