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
import java.net.UnknownHostException;
import java.util.Date;

import moma.terminer.Port;
import moma.terminer.Terminer;

/**
 * Created by diamond on 2018/1/16.
 */

public class EchoService extends Service {
    public static final String TAG = "心跳==";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "服务启动");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "服务执行" + new Date().toString());
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        long time = System.currentTimeMillis();
        //Log.d(TAG,String.valueOf(time));
        if(Terminer.getInstance().isCtlonline() && (time - Terminer.getInstance().getSecho() < 10000)) {
            //启动心跳服务
            Log.d(TAG,Terminer.getInstance().getCtladdr().getHostAddress());;
            Terminer.getInstance().setSecho(System.currentTimeMillis());
            Terminer.getInstance().sendEcho();
        }else {
            Log.d(TAG, "心跳不在线"+new Date());
            Terminer.getInstance().setCtlonline(false);
        }

        Intent echo_intent = new Intent(this, EchoReceive.class);
        long triggerAtTime = SystemClock.elapsedRealtime() + 5000;
        PendingIntent ecpi = PendingIntent.getBroadcast(this, 0, echo_intent, 0);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, ecpi);

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
