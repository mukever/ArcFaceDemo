package moma.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;


import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.Date;

import moma.terminer.Port;
import moma.terminer.Terminer;

/**
 * Created by diamond on 2018/1/16.
 */

public class DisCoveryService extends Service {
    public static final String TAG = "发现===";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "服务启动");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        if(!Terminer.getInstance().isCtlonline()){
            Log.d(TAG, "广播数据发现包"+new Date());
            Terminer.getInstance().sendDis();
        }
        Intent discory_intent = new Intent(this, DiscoryReceiver.class);
        long triggerAtTime = SystemClock.elapsedRealtime() + 1000;
        PendingIntent pidis = PendingIntent.getBroadcast(this, 0, discory_intent, 0);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pidis);
        return super.onStartCommand(intent, flags, startId);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "服务结束");
    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
