package com.test.callkit.androidcall;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.preference.PreferenceManager;


public class VoipBackgroundService extends Service
{
    public static boolean isServiceRunningInForeground(Context context, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                if (service.foreground) {
                    return true;
                }

            }
        }
        return false;
    }


    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent)
    {
        super.onTaskRemoved(rootIntent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        SharedPreferences sp = getSharedPreferences("My Pref" ,Context.MODE_PRIVATE);
        String token  = sp.getString("fcm_token","");
        Log.d("FCM TOKEN","Token  is "+ token);
        Log.d("MySipService", "onStartCommand");
        Log.d("token from SP>>>>>", token);

        if(intent.hasExtra("connectionId") && intent.hasExtra("username"))
        {
            String connectionId = intent.getStringExtra("connectionId");
            String username = intent.getStringExtra("username");
            if(!isServiceRunningInForeground(VoipBackgroundService.this,VoipForegroundService.class)) {
                show_call_notification("incoming",token,username,connectionId);

                KeyguardManager km = (KeyguardManager) getApplicationContext().getSystemService(Context.KEYGUARD_SERVICE);
                KeyguardManager.KeyguardLock kl = km.newKeyguardLock("name");
                kl.disableKeyguard();
            }
            ApiCalls apiCalls =  new ApiCalls();
            apiCalls.gettwiliotoken(connectionId, new RetreivedTokenCallback() {
                @SuppressLint("MissingPermission")
                @Override
                public void onTokenRetreived(String token) {
                    Log.d("onTokenRetreived",token);
                    if(!isServiceRunningInForeground(VoipBackgroundService.this,VoipForegroundService.class)) {
                        show_call_notification("incoming",token,username,connectionId);

                        KeyguardManager km = (KeyguardManager) getApplicationContext().getSystemService(Context.KEYGUARD_SERVICE);
                        KeyguardManager.KeyguardLock kl = km.newKeyguardLock("name");
                        kl.disableKeyguard();
                    }

                }
            });
        }
//        return START_NOT_STICKY;
        return START_STICKY;
    }



    public void show_call_notification(String action, String token,String username,String roomName)
    {
        Log.d("show_call_notification",action);
        Intent serviceIntent = new Intent(this, VoipForegroundService.class);
        serviceIntent.setAction(action);
        serviceIntent.putExtra("token",token);
        serviceIntent.putExtra("username",username);
        serviceIntent.putExtra("roomName",roomName);




        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            VoipBackgroundService.this.startForegroundService(serviceIntent);

        } else {
            VoipBackgroundService.this.startService(serviceIntent);
        }
    }



    @Override
    public void onCreate()
    {
        super.onCreate();




    }

    public static String getPref(String key, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, null);
    }

}
