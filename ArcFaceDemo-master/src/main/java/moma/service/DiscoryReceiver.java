package moma.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by diamond on 2018/1/16.
 */

public class DiscoryReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, DisCoveryService.class);
        context.startService(i);
    }
}
