package com.example.shangqing.wifiapscan;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.List;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.widget.TextView;
import android.widget.Toast;
import java.io.StringWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.File;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.util.Log;
import android.widget.Button;
import android.widget.ProgressBar;
import java.io.BufferedWriter;
import java.io.FileWriter;
import android.media.MediaScannerConnection;


public class MainActivity extends Activity {
    TextView mainText;
    WifiManager mainWifi;
    WifiReceiver receiverWifi;
    List<ScanResult> wifiList;
    //LocationManager locationManager;

    StringBuilder sb = new StringBuilder();
    private static final String TAG = MainActivity.class.getSimpleName();

    private LocationManager locationMangaer=null;
    private LocationListener locationListener=null;

    private LocationManager lm=null;
    private LocationListener lls=null;
    double longitude;
    double latitude;
    private Button btnGetLocation = null;
    //private EditText editLocation = null;
    private ProgressBar pb =null;

    //private static final String TAG = "Debug";
    private Boolean flag = false;




    public void onCreate(Bundle savedInstanceState) {
        //super.onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout);
        mainText = (TextView) findViewById(R.id.textView2);
        //setSupportActionBar(toolbar);



        locationMangaer = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new MyLocationListener();

        locationMangaer.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10,locationListener);



        mainWifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);

        // Check for wifi is disabled
        if (mainWifi.isWifiEnabled() == false)
        {
            // If wifi disabled then enable it
            Toast.makeText(getApplicationContext(), "wifi is disabled..making it enabled",
                    Toast.LENGTH_LONG).show();

            mainWifi.setWifiEnabled(true);
        }

        // wifi scaned value broadcast receiver
        receiverWifi = new WifiReceiver();

        // Register broadcast receiver
        // Broacast receiver will automatically call when number of wifi connections changed
        registerReceiver(receiverWifi, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        mainWifi.startScan();
        mainText.setText("Starting Scan...");
        

    }
    public void getWifi(View v) {
        // Do something in response to button click
        setContentView(R.layout.layout);

        mainText = (TextView) findViewById(R.id.textView2);
        //mainWifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);

        locationMangaer = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        locationListener = new MyLocationListener();

        locationMangaer.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10,locationListener);

        mainWifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);




        if (!mainWifi.isWifiEnabled()) {
            ;
            // If wifi disabled then enable it
            Toast.makeText(getApplicationContext(), "wifi is disabled..making it enabled",
                    Toast.LENGTH_LONG).show();

            mainWifi.setWifiEnabled(true);
        }
        //mainWifi.startScan();

        receiverWifi = new WifiReceiver();
        registerReceiver(receiverWifi, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        try {
            //registerReceiver(receiverWifi, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
            mainWifi.startScan();
        } catch (Exception e){
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            String sStackTrace = sw.toString(); // stack trace as a string
            System.out.println(sStackTrace);
        }
        //mainWifi.startScan();
        mainText.setText("Starting Scan...");
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 0, 0, "Refresh");
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        mainWifi.startScan();
        mainText.setText("Starting Scan");
        return super.onMenuItemSelected(featureId, item);
    }

    protected void onPause() {
        unregisterReceiver(receiverWifi);
        super.onPause();
    }

    protected void onResume() {
        registerReceiver(receiverWifi, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        super.onResume();
    }

    // Broadcast receiver class called its receive method
    // when number of wifi connections changed

    class WifiReceiver extends BroadcastReceiver {

        public void readExternalStorage(View view)
        {
            File Root = Environment.getExternalStorageDirectory();
            File Dir = new File(Root.getAbsolutePath()+"/Wifiscan");
            File file = new File(Dir,"WifiHometoUSF.txt");
            String Message;
            try{
                FileInputStream fileInputStream = new FileInputStream(file);
                InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                StringBuffer stringBuffer = new StringBuffer();
                while((Message=bufferedReader.readLine())!=null){
                    stringBuffer.append(Message +"\n");
                }
                mainText.setText(stringBuffer.toString());
                mainText.setVisibility(View.VISIBLE);

            }catch (FileNotFoundException e)
            {
                e.printStackTrace();
            } catch (IOException e)
            {
                e.printStackTrace();
            }


        }

        // This method call when number of wifi connections changed
        public void onReceive(Context c, Intent intent) {

            sb = new StringBuilder();

            wifiList = mainWifi.getScanResults();
            //int x = mainWifi.getFrequency();
            sb.append("\nNumber of Wifi APs: "+ (wifiList.size())+"\n\n");
            for(int i = 0; i < wifiList.size(); i++){

                sb.append(new Integer(i+1).toString() + "\n ");

                sb.append("SSID: "+wifiList.get(i).SSID+"\n");
                sb.append("MAC: "+ wifiList.get(i).BSSID+"\n");
                sb.append("RSSI: "+ wifiList.get(i).level+"\n");
                sb.append("Time: "+ wifiList.get(i).timestamp+"\n");
                sb.append("longitude: " + longitude+"\n");
                sb.append("latitude: " + latitude+"\n");
                //sb.append(wifiList.get(i).toString()+"\n");
                //sb.append((wifiList.get(i)).toString());
                sb.append("\n\n");
            }
            mainText.setText(sb);

            //write to file
            String state;
            state = Environment.getExternalStorageState();
            if(Environment.MEDIA_MOUNTED.equals(state))
            {
                File Root = Environment.getExternalStorageDirectory();
                File Dir = new File(Root.getAbsolutePath()+"/Wifiscan");
                if(!Dir.exists())
                {
                    Dir.mkdir();
                }
                File file = new File(Dir,"WifiHometoUSF.txt");
                String Message = mainText.getText().toString();

                try {
                    FileOutputStream fileout = new FileOutputStream(file,true);
                    fileout.write(Message.getBytes());
                    fileout.close();
                    Toast.makeText(getApplicationContext(), "Message Saved", Toast.LENGTH_LONG).show();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            else{
                Toast.makeText(getApplicationContext(),"SD card Not Found",Toast.LENGTH_LONG).show();
            }









        }


    }

    private class MyLocationListener extends BroadcastReceiver implements LocationListener {

        double a;
        double b;
        @Override
        public void onLocationChanged(Location loc) {
            //editLocation.setText("");
            //pb.setVisibility(View.INVISIBLE);
            //Toast.makeText(
            longitude = loc.getLongitude();
            a = loc.getLongitude();
            //Log.v(TAG, longitude);
            latitude = loc.getLatitude();
            b = loc.getLatitude();
            

        }
        public void onReceive(Context c, Intent intent) {
            mainText.setText("long"+String.valueOf(a));
            mainText.setText("lat"+String.valueOf(b));

        }



        @Override
        public void onProviderDisabled(String provider) {}

        @Override
        public void onProviderEnabled(String provider) {}

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    }




}
