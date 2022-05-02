package com.example.androidchatapp.Services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Process;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

public class TestService extends Service {
    private final IBinder binder = new Binder();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        while(true){
                            Log.e("service", "service send event" + this.toString());
                            EventBus.getDefault().post("test eventbus");
                            try{
                                Thread.sleep(3000);
                            } catch (InterruptedException e){

                            }
                        }
                    }
                }
        ).start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        Log.e("service", "service started");

        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        while(true){
                            Log.e("service", "service running" + this.toString());
                            try{
                                Thread.sleep(3000);
                            } catch (InterruptedException e){

                            }
                        }
                    }
                }
        ).start();
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        Log.e("service", "service destroyed");
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
