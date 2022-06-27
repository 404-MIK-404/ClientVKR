package com.example.test.Services;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.CountDownTimer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.test.ActivityTasks.SeeCurrentTask;
import com.example.test.R;

import java.util.ArrayList;
import java.util.List;

public class NotificationTime {

    // Идентификатор канала
    private static String CHANNEL_ID = "Channel Remember Task";

    private List<Integer> idTaskNotifyCreate = new ArrayList<>();


    private NotificationManagerCompat notificationManager;

    public void createChannelNotify(CharSequence name,String description,NotificationManager notificationManager){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void setNotify(Activity NowActivity, String Title, String Description, PendingIntent resultPendingIntent, Context context,int TASK_ID){
            NotificationCompat.Builder builder =
                    new NotificationCompat.Builder(NowActivity, CHANNEL_ID)
                            .setSmallIcon(R.drawable.notification_icon_1_hour)
                            .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),R.drawable.notification_icon_1_hour))
                            .setContentTitle(Title)
                            .setContentText(Description)
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                            .setAutoCancel(true)
                            .setContentIntent(resultPendingIntent);
            this.notificationManager = NotificationManagerCompat.from(NowActivity);
            this.notificationManager.notify(TASK_ID, builder.build());
    }

    public void cancelAllNotify(){
        notificationManager.cancelAll();
    }

}
