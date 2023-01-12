package com.example.test.Services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.*;

import androidx.annotation.Nullable;

import com.example.test.ActivityTasks.SeeCurrentTask;
import com.example.test.Client.Client;
import com.example.test.R;
import com.example.test.Task.Tasks;


/**
 *
 * Данная фича было не должны образом реализована как я хотел.
 * Хотелось сделать упоминалку что за 1 час до окончания данного таска пришло уведомление, что тип надо сделать этот таск и т.д.
 * Но данную фичу было очень тяжело сделать и я пытался каким то образом его сделать правильно, но не получилось, к сожалению.
 *
 *
 * @author MIK
 *
 *
 */

public class ServiceNotificationRemember extends Service {

    private final Client User = Client.UserAndroid;
    private static final NotificationTime noti = new NotificationTime();

    @Override
    public void onCreate() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        long TIMER_INTERVAL = intent.getLongExtra("Timer",0);
        int ID_TASK = intent.getIntExtra("Number_Task",0);
        Tasks currentTask = intent.getParcelableExtra("CurrentTask");
        new CountDownTimer(TIMER_INTERVAL,1){

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    Context context = ServiceNotificationRemember.this.getBaseContext();
                    Intent resultIntent = new Intent(ServiceNotificationRemember.this, SeeCurrentTask.class);
                    resultIntent.putExtra("CurrentTask",currentTask);
                    PendingIntent resultPendingIntent = PendingIntent.getActivity(ServiceNotificationRemember.this,ID_TASK,resultIntent,PendingIntent.FLAG_UPDATE_CURRENT);
                    noti.createChannelNotify(getString(R.string.channel_name),getString(R.string.channel_desc),getSystemService(NotificationManager.class));
                    noti.setNotify(User.getActivityNow(),"Напоминание","Задание с названием: " + currentTask.getNameTask(),resultPendingIntent,context,ID_TASK);
                }
            }
        }.start();
        return super.onStartCommand(intent, flags, startId);
    }

    public static void cancellNotify(){
        noti.cancelAllNotify();
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
