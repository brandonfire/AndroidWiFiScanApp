package com.example.shangqing.wifiapscan;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.widget.TextView;

import java.util.List;

public class wifiscan {


    TextView mainText;
    WifiManager mainWifi;
    MainActivity.WifiReceiver receiverWifi;
    List<ScanResult> wifiList;
    StringBuilder sb = new StringBuilder();
    private static final String TAG = MainActivity.class.getSimpleName();


    public void onReceive(Context c, Intent intent) {

        sb = new StringBuilder();
        wifiList = mainWifi.getScanResults();
        sb.append("\n   Number of Wifi APs:"+ (wifiList.size()+1)+"\n\n");
        //sb.append(wifiscan.usf);
        for(int i = 0; i < wifiList.size(); i++){

            sb.append(new Integer(i+1).toString() + ". ");
            sb.append((wifiList.get(i)).toString());
            sb.append("\n\n");
        }

        mainText.setText(sb);
    }

}
