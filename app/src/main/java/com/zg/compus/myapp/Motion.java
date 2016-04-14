package com.zg.compus.myapp;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.zg.compus.constant.WifiApInfo;
import com.zg.compus.service.ListenThread;
import com.zg.compus.service.WifiApAdmin;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class Motion extends AppCompatActivity {
    private static final String TAG = "Motion";
    private final Context context = this;
    /**
     * the button of create wifi spot
     */
    private Button createWifiAp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_motion);
        /**
         * the list of button
         */
        Button createWifiAp = (Button) this.findViewById(R.id.createWifiAp);
        /**
         * asynchronous rollbacks
         */
        createWifiAp.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                WifiApAdmin wifiApAdmin = new WifiApAdmin(context);
                if (wifiApAdmin.createWifiAp(WifiApInfo.WIFI_NAME, WifiApInfo.WIFI_PASSWORD)) {
                    Log.v(TAG, "createWifiAp succeed");
                    /**
                     * start thread listening the port of 8090
                     */
                    //Callable<Object> callable = new ListenThread(8090);
                    ListenThread listenThread = new ListenThread(8090);
                    FutureTask<Object> futureTask = new FutureTask<Object>(listenThread);
                    new Thread(futureTask).start();
                } else {
                    Log.v(TAG, "createWifiAp failed");
                }
            }
        });
    }
}
