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


    private ThreadManager inThread, outThread;
    private Socket socket;
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

    public void run() {

        if (running)
            return;

        running = true;
        socket = new Socket();
        inThread = new ThreadManager();
        outThread = new ThreadManager();

        outThread.start();
        outThread.addTask(new Runnable() {
            @Override
            public void run() {
                try {

                    Log.e("TCP Client", "C: Connecting...");

                    socket.connect(new InetSocketAddress(InetAddress.getByName(IP), PORT), CONNECTION_TIMEOUT);

                    dataInStream = new DataInputStream(socket.getInputStream());
                    dataOutStream = new DataOutputStream(socket.getOutputStream());
                    dataOutStream.flush();

                    Log.e("TCP Client", "C: Connected");


                    inThread.start();
                    inThread.addTask(new Runnable() {
                        @Override
                        public void run() {

                                while (running) {
                                    try {
                                        String result = dataInStream.readUTF();

                                        // get the json response from server here

                                        //Fix it like i have it in my RTS game server
                                            //Method that take in the response type here

                                    } catch (IOException e) {
                                        Log.e("TCP", "S: Error Other", e);
                                    }

                                }
                        }
                    });
                } catch (Exception e) {
                    Log.e("TCP", "C: Error Socket", e);
                }
            }
        });
    }

    public void disconnect(){
        if(!running){
            Log.e("Error: ", "No connection to server");
            return;
        }

        Log.e("Command", "Disconnecting");
        outThread.addTask(new Runnable() {
            @Override
            public void run() {
                try {
                    if (socket != null) {
                        socket.close();
                        socket = null;
                    }
                    if (dataOutStream != null) {
                        dataOutStream.close();
                        dataOutStream = null;
                    }
                    if (dataInStream != null) {
                        dataInStream.close();
                        dataInStream = null;
                    }

                    outThread.stop();
                    inThread.stop();

                    running = false;

                } catch (IOException e) {
                        e.printStackTrace();
                }
            }

        });
    }

}
