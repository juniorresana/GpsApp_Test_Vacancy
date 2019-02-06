package com.example.a0.serv;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

//===========================================
//Тестовое приложение для вакансии разработчик


public class TWo extends AppCompatActivity {

    public TWo tWo;
    public static ServiceConnection serviceConnection;
    public static MyService myService;
    Intent intent;

    private LocationManager locationManager;
    private LocationListener locationListener;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tWo = this;



        runOnUiThread(new Runnable() {
            @Override
            public void run() {


        if (Build.VERSION.SDK_INT<23){

        } else {
            if (ActivityCompat.checkSelfPermission(tWo, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(tWo, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                ActivityCompat.requestPermissions(tWo, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                return;
            } else {
               // locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

            }
        }

            }
        });



        intent = new Intent(TWo.this, MyService.class);
        ((Button)findViewById(R.id.bStart)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TWo.this, "start", Toast.LENGTH_SHORT).show();
                startService(intent);
            }
        });



        ((Button)findViewById(R.id.bStop)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService(intent);
                Toast.makeText(TWo.this, "stop", Toast.LENGTH_SHORT).show();
            }
        });


        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                myService = ((MyService.Binderthis)service).get();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };



        ((Button)findViewById(R.id.bResult)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView = findViewById(R.id.result);
                textView.setText("");

                if (locationFormaterShow(myService.s)!=null){

                    for (String string : locationFormaterShow(myService.s)){
                        textView.append(string.toString() + "\n");
                    }
                } else {
                    textView.setText("Данные пока не известны");
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        bindService(intent, serviceConnection, 0);
    }

    public ArrayList<String> locationFormaterShow(String s){

        try {
            if (s.length() < 2) return null;
            ArrayList<String> list = new ArrayList<>(Arrays.asList(s.split(",")));
            list.set(0, "(тестовое значение): " + list.get(0));
            list.set(1, "(тестовое значение): " + list.get(1));
            list.set(2, "Широта: " + list.get(2));
            list.set(3, "Долгота: " + list.get(3));
            list.set(4, "Скорость: " + list.get(4));
            list.set(5, "Высота: " + list.get(5));
            list.set(6, "Азимут: " + list.get(6));
            list.set(7, "Уровень батареи: " + list.get(7));
            list.set(8, "Дата: " + list.get(8));
            list.set(9, "Время " + list.get(9));
            list.set(10, "Часовой пояс (тестовое значение): " + list.get(10));
            list.set(11, "Количество спутников: " + list.get(11));
            list.set(12, "Мощность (тестовое значение): " + list.get(12));
            list.set(13, "Тип (тестовое значение): " + list.get(13));
            list.set(14, "Значение SOS (тестовое значение): " + list.get(14));

            return list;



        }catch (Exception e){
            return null;
        }


    }
}
