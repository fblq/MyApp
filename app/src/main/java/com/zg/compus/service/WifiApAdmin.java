package com.zg.compus.service;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.util.Log;
import com.zg.compus.constant.WifiApInfo;

import java.lang.reflect.Method;

/**
 * Created by zhong.zhou on 16/4/12.
 */
public class WifiApAdmin {
    public static final String TAG = "WifiApAdmin";
    //wifi服务管理
    private WifiManager wifiManager;

    public WifiApAdmin(Context context) {
        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        //关闭已存在的热点
        if (isWifiApEnabled()) {
            if (closeWifiAp()) {
                Log.v(TAG, "closeWifiAp succeed");
            } else {
                Log.v(TAG, "closeWifiAp failed");
            }
        }
    }

    /**
     * create wifi spot
     *
     * @return Boolean
     */
    public Boolean createWifiAp(String name, String password) {
        //创建wifi热点前需要检查wifi是否关闭,二者只能同时开一个
        if (wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(false);
        }
        try {
            Method method = wifiManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, Boolean.TYPE);
            WifiConfiguration configuration = new WifiConfiguration();
            configuration.SSID = name;
            configuration.preSharedKey = password;
            return (Boolean) method.invoke(wifiManager, configuration, true);
        } catch (Exception e) {
            Log.v(TAG, "createWifiAp:", e);
            return false;
        }
    }

    /**
     * close wifi spot
     *
     * @return Boolean
     */
    public Boolean closeWifiAp() {
        try {
            Method method = wifiManager.getClass().getMethod("getWifiApConfiguration");
            WifiConfiguration wifiConfiguration = (WifiConfiguration) method.invoke(wifiManager);
            Method method1 = wifiManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, Boolean.TYPE);
            return (Boolean) method1.invoke(wifiManager, wifiConfiguration, false);
        } catch (Exception e) {
            Log.v(TAG, "closeWifiAp", e);
            return false;
        }
    }

    /**
     * judge the status of wifi spot
     *
     * @return Boolean
     */
    public Boolean isWifiApEnabled() {
        try {
            Method method = wifiManager.getClass().getMethod("isWifiApEnabled");
            return (Boolean) method.invoke(wifiManager);
        } catch (Exception e) {
            Log.v(TAG, "isWifiApEnabled", e);
            return false;
        }
    }
}
