package com.example.a0.serv;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.GnssStatus;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.BatteryManager;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.ContentValues.TAG;

//===========================================
//Тестовое приложение для вакансии разработчик

public class MyService extends Service {
    public MyService() {
    }

    public static int r = 0;
    public static Timer timer;
    public static TimerTask timerTask;
    public static Socket socket;
    public static OutputStreamWriter outputStreamWriter;
    public static Location locationMain = null;

    public static String s = "";

    public static LocationListener locationListener;

    private LocationManager locationManager = null;

    class Binderthis extends Binder{
        MyService get(){
            return MyService.this;
        }
    }

    Binderthis binderthis = new Binderthis();

    private class LocationListener implements android.location.LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            Log.d("tag222", "onLocationChanged: 111");
            locationMain = location;

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.d("tag222", "onStatusChanged: 111");
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.d("tag222", "onProviderEnabled: 111");
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.d("tag222", "onProviderDisabled: 111");
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return binderthis;
    }


    @Override
    public void onCreate() {
        locationListener = new LocationListener();
        timer = new Timer();
        initGps();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        Log.d("tag222", "onCreate: 111");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d("tag222", "onStartCommand: 111");

        if (timerTask!=null) {timerTask.cancel();}

        timerTask = new TimerTask() {
            @Override
            public void run() {

                //шаблон из тестового задания
                String sablon = "rtt003,356217625371698,-59.4364251,-129.9839853,0023,0014,123,090,20130618,195430,-11,24,60,A,0";

                Random random = new Random();

                try {
                    socket = new Socket(InetAddress.getByName("srv1.livegpstracks.com"), 3359);
                    outputStreamWriter = new OutputStreamWriter(socket.getOutputStream());
                    outputStreamWriter.write(locationFormatter(locationMain));
                    outputStreamWriter.flush();
                    outputStreamWriter.close();
                    socket.close();



                } catch (IOException e) {
                    Log.d("tag222", "err 333: ");
                    e.printStackTrace();
                }

                Log.d("tag222", " timertask");
            }
        };
        timer.schedule(timerTask, 0, 1000 * 10);

        return START_STICKY;
    }






    @Override
    public void onDestroy() {
        timer.cancel();
        locationManager.removeUpdates(locationListener);
        Log.d("tag222", "onDestroy: 1111");
    }





    public void initGps() {
        Log.d("tag222", "initGps: ");
        if (locationManager == null) {
            locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }


    public int getBattery(){
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = getApplicationContext().registerReceiver(null, intentFilter);
        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        float fr = level/(float)scale;
        return level;
    }



    public String locationFormatter(Location location) {

        s = "";

        if (location == null) return s;


        try {

             s =//proto
                    "rtt003," + "356217625371698," +
                            //"longtitude " +
                            location.getLongitude() + "," +
                            //"latitude" +
                            location.getLatitude() + "," +
                            //"speed " +
                            location.getSpeed() + "," +
                            //"altitude " +
                            location.getAltitude() + "," +
                            //"bearing " +
                            location.getBearing() + "," +

                            getBattery() + "," +

                           (new SimpleDateFormat("yyyyMMdd").format(new Date())) + "," +

                            (new SimpleDateFormat("hhmmss").format(new Date())) + "," +

                            "+3," +

                            (location.getExtras().get("satellites")) + "," +

                            "60," +

                            "A," +

                            "0";


            //" accuracy " +
//                        location.getAccuracy() + "," +
//                        //"time " +
//                        location.getTime() + "," +
//
//                        " satellit count " + location.getExtras().get("satellites") +
//                        "satel count " + satelliteCount + " " + usedSatelliteCount+
//                        "battery: " + getBattery();

            Log.d("tag222 rr", s);

        }catch (Exception e) {

        }

        return s;
    }


}
