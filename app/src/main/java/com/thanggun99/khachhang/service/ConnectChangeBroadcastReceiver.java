package com.thanggun99.khachhang.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.thanggun99.khachhang.util.Utils;

/**
 * Created by Thanggun99 on 07/04/2017.
 */

public class ConnectChangeBroadcastReceiver extends BroadcastReceiver {
    public static final String CONNECT_FAIL = "CONNECT_FAIL";
    public static final String CONNECT_AVAIlABLE = "CONNECT_AVAIlABLE";

    @Override
    public void onReceive(final Context context, Intent intent) {
        if (Utils.isConnectingToInternet()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if (Utils.isConnectValiable()) {
                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(CONNECT_AVAIlABLE));
                    } else {
                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(CONNECT_FAIL));
                    }
                }
            }).start();

        } else {
            LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(CONNECT_FAIL));
        }
    }
}
