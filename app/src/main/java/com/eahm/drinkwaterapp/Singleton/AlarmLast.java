package com.eahm.drinkwaterapp.Singleton;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.eahm.drinkwaterapp.BroadcastReceiver.AlarmReceiver;
import com.eahm.drinkwaterapp.Class.SettingsClass;
import com.eahm.drinkwaterapp.Utils.Constants;
import com.eahm.drinkwaterapp.Utils.Utils;

import java.util.Calendar;

public class AlarmLast {

    //region VARIABLES
    Intent intent;
    AlarmManager alarmManager;
    PendingIntent pendingIntent;
    static int CODE_REQUEST_LAST_ALARM = 364;

    Utils utils = new Utils();
    //endregion VARIABLES

    //region SINGLETON
    private static volatile AlarmLast instance = new AlarmLast();

    public static AlarmLast getInstance() {
        return instance;
    }

    private AlarmLast(){} //private constructor

    //endregion SINGLETON

    private void initAlarm(Context context){
        intent = new Intent(context, AlarmReceiver.class);
        intent.setAction("NOTIFICATION_LAST");
        intent.putExtra("NOTI_ID", Constants.NOTIFICATION_ID_APP);
        intent.putExtra("CODE", "LAST"); //Night Notification!
        pendingIntent = PendingIntent.getBroadcast(context, CODE_REQUEST_LAST_ALARM, intent, 0);
    }

    public void adminLastAlarm(Context context) {
        //Esta alarma es para calcular los resultados del dia de hoy... ya que esta es la
        //hora limite para tomar agua en este dia... :O

        SettingsClass settings = new SettingsClass();
        //MIDNIGHT
        int prevHour = settings.getEndPrevHour();
        int prevMin = settings.getEndPrevMin();

        int currentHour = settings.getEndHour();
        int currentMin = settings.getEndMin();

        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        initAlarm(context);

        Log.i(Constants.LOG_LAST_ALARM, "Conteo de media noche!");

        if((prevHour != currentHour || prevMin != currentMin)){
            Log.i(Constants.LOG_LAST_ALARM, "La hora establecida fue actualizada");
            settings.setEndPrevHour(currentHour);
            settings.setEndPrevMin(currentMin);

            Log.i(Constants.LOG_LAST_ALARM, "Cancelar alarma previa");
            alarmManager.cancel(pendingIntent);
            pendingIntent.cancel();

            initAlarm(context);
        }
        else Log.i(Constants.LOG_LAST_ALARM, "No se ha modificado la hora");

        if(!utils.existAlarm(context, CODE_REQUEST_LAST_ALARM, intent)) {
            Log.i(Constants.LOG_LAST_ALARM, "La alarma de medianoche no existe");
        }
        else Log.i(Constants.LOG_LAST_ALARM, "La alarma existe");

        Log.i(Constants.LOG_LAST_ALARM, "Establecer la alarma");

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, currentHour);
        calendar.set(Calendar.MINUTE, currentMin);

        if(Calendar.getInstance().after(calendar)){
            // Today Set time passed, count to tomorrow... Move to tomorrow!
            calendar.add(Calendar.DATE, 1);
            Log.i(Constants.LOG_LAST_ALARM, "Se paso la hora! hasta mañana!!");
        }

        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        Log.i(Constants.LOG_LAST_ALARM, "Alarma reestablecida para cada noche a las " + currentHour + ":" + currentMin);

    }


    //region GET AND SET
    public Intent getIntent() {
        return intent;
    }

    public void setIntent(Intent intent) {
        this.intent = intent;
    }

    public PendingIntent getPendingIntent() {
        return pendingIntent;
    }

    public void setPendingIntent(PendingIntent pendingIntent) {
        this.pendingIntent = pendingIntent;
    }
    //endregion GET AND SET
}
