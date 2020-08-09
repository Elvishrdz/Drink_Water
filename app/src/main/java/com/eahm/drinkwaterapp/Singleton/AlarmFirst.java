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

public class AlarmFirst {

    //region VARIABLES
    private Intent intent;
    private  AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private static int CODE_REQUEST_FIRST_ALARM = 753;

    Utils utils = new Utils();

    //endregion VARIABLES

    //region SINGLETON
    private static volatile AlarmFirst instance = new AlarmFirst();

    public static AlarmFirst getInstance() {
        return instance;
    }

    private AlarmFirst(){} //private constructor

    //endregion SINGLETON

    private void initAlarm(Context context){
        intent = new Intent(context, AlarmReceiver.class);
        intent.setAction("NOTIFICATION_MORNING");
        intent.putExtra("NOTI_ID", Constants.NOTIFICATION_ID_APP);
        intent.putExtra("CODE", "MORNING"); //Morning Notification!
        pendingIntent = PendingIntent.getBroadcast(context, CODE_REQUEST_FIRST_ALARM, intent, 0);
    }

    public void adminFirstAlarm(Context context) {
        //Vamos a mostrar una notificacion a partir de las 6am aproximadamente (default)

        SettingsClass settings = new SettingsClass();

        int prevHour = settings.getStartPrevHour();
        int prevMin = settings.getStartPrevMin();

        int currentHour = settings.getStartHour();
        int currentMin = settings.getStartMin();

        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        initAlarm(context);

        Log.i(Constants.LOG_MORNING_ALARM, "Current: " + currentHour +":" + currentMin +" -- Prev: " + prevHour + ":" + prevMin);

        if((prevHour != currentHour || prevMin != currentMin)){
            Log.i(Constants.LOG_MORNING_ALARM, "La hora establecida fue actualizada");
            settings.setStartPrevHour(currentHour);
            settings.setStartPrevMin(currentMin);

            Log.i(Constants.LOG_MORNING_ALARM, "Cancelar alarma previa");
            alarmManager.cancel(pendingIntent);
            pendingIntent.cancel();

            initAlarm(context);
        }
        else Log.i(Constants.LOG_MORNING_ALARM, "No se ha modificado la hora");

        if(!utils.existAlarm(context, CODE_REQUEST_FIRST_ALARM, intent)) {
            Log.i(Constants.LOG_MORNING_ALARM, "La alarma mañanera no existe");
        }
        else Log.i(Constants.LOG_MORNING_ALARM, "La alarma existe");

        Log.i(Constants.LOG_MORNING_ALARM, "Establecer la alarma ");

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, currentHour);
        calendar.set(Calendar.MINUTE, currentMin);

        if(Calendar.getInstance().after(calendar)){
            // Today Set time passed, count to tomorrow... Move to tomorrow!
            calendar.add(Calendar.DATE, 1);
            Log.i(Constants.LOG_MORNING_ALARM, "Se paso la hora! hasta mañana!!");
        }

        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        Log.i(Constants.LOG_MORNING_ALARM, "Alarma reestablecida para cada mañana a las " + currentHour + ":" + currentMin);

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
