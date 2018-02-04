package moma.listen;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import moma.terminer.Terminer;


/**
 * Created by diamond on 2018/1/16.
 */

public class ListenService extends Service {
    public static final String TAG = "接受==";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "服务启动");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "服务执行");
        new Thread(new Runnable() {
            @Override
            public void run() {
                Terminer.getInstance().listenDis();
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                Terminer.getInstance().listenEcho();
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Terminer.getInstance().listenCtl();
            }
        }).start();
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
