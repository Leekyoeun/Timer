package com.e225555.timer;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

public class AlarmReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent){

        NotificationManager notificationManagr=(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent notificationtent=new Intent(context, MainActivity.class);

        notificationtent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pending1= PendingIntent.getActivity(context, 0, notificationtent, 0);
        NotificationCompat.Builder builder=new NotificationCompat.Builder(context, "default");

        //OREO API 26이상에서는 채널 필요
        if(android.os.Build.VERSION.SDK_INT>=android.os.Build.VERSION_CODES.O){
            builder.setSmallIcon(R.drawable.ic_launcher_foreground);

            String channelName="매일 알람 채널";
            String description="매일 정해진 시간에 알림합니다";
            int importance=NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel channel=new NotificationChannel("default", channelName, importance);
            channel.setDescription(description);

            if(notificationManagr!=null){
                //노티피케이션 채널을 시스템에 등록
                notificationManagr.createNotificationChannel(channel);
            }
        }else builder.setSmallIcon(R.mipmap.ic_launcher);

        builder.setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())

                .setTicker("{Time to watch some coo stuff}")
                .setContentTitle("상태바 드래그시 보이는 타이틀")
                .setContentText("상태바 드래그시 보이는 서브타이틀")
                .setContentInfo("INFO")
                .setContentIntent(pending1);

        if(notificationManagr!=null) {
            //노티피페이션 동작시킴

            Calendar nextNofityTime = Calendar.getInstance();

            //내일 같은 시간으로 알람시간 결정
            nextNofityTime.add(Calendar.DATE, 1);

            //Preference에 설정한 값 저장
            SharedPreferences.Editor editor = context.getSharedPreferences("daily alarm", MODE_PRIVATE).edit();
            editor.putLong("nextNotifyTime", nextNofityTime.getTimeInMillis());
            editor.apply();

            Date currentDateTime = nextNofityTime.getTime();
            String date_text = new SimpleDateFormat("yyyy년 MM월 dd일 EE요일 a hh시 mm분", Locale.getDefault()).format(currentDateTime);
            Toast.makeText(context.getApplicationContext(), "다음 알람은" + date_text + "으로 알람이 설정되었습니다!", Toast.LENGTH_SHORT).show();
                }
            }
        }