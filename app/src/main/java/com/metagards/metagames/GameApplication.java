package com.metagards.metagames;

import android.app.Application;
import android.content.Context;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.engineio.client.transports.WebSocket;

public class GameApplication extends Application {

    private static GameApplication instance;
    public static Context appLevelContext;
    private Socket mSocket;
    private static final  String URL = "http://64.227.186.5:3002/dragon_tiger";
    public static GameApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        appLevelContext=getApplicationContext();

        try {
            IO.Options opts = new IO.Options();
            opts.transports = new String[] { WebSocket.NAME };
            mSocket = IO.socket(URL,opts);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

    }
    public Socket getmSocket(){
        return mSocket;
    }

}



