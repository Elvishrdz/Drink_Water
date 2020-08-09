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

public class AlarmApp {

    //region VARIABLES
    private final static String LOG_ALARM_APP = "LOG_ALARM_APP";

    private Context context;
    private AlarmManager alarmManager;
    private Intent intent;
    private PendingIntent pendingIntent;

    private Utils utils = new Utils();
    private SettingsClass settings = new SettingsClass();
    //endregion VARIABLES

    //region SINGLETON
    private static volatile AlarmApp instance = new AlarmApp();

    public static AlarmApp getInstance() {
        return instance;
    }

    private AlarmApp(){} //private constructor

    //endregion SINGLETON

    //region APP ALARM

    public void initialize(Context context){
        if(this.context == null) this.context = context;
        initAlarm();
    }

    private void initAlarm(){
        if(context == null){
            Log.i(LOG_ALARM_APP, "Init alarm: No se ha inicializado el contexto de la Alarma");
            return;
        }

        if(alarmManager == null)
            alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        intent = new Intent(context, AlarmReceiver.class);
        intent.setAction("ALARM_DRINK");
        //intent.putExtra("CODE", code);
        pendingIntent = PendingIntent.getBroadcast(context, Constants.CODE_REQUEST_ALARM_APP, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public void updateAlarm(long timeInMillis) {
        if(context == null){
            Log.i(LOG_ALARM_APP, "Update alarm: No se ha inicializado el contexto de la Alarma");
            return;
        }

        // Esta es la alarma para tomar agua, cada vez que el usuario realiza una
        // accion con la notificacion esta vendra a actualizar la alarma unica de
        // la app segun la eleccion...

        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        initAlarm();

        if(utils.existAlarm(context, Constants.CODE_REQUEST_ALARM_APP, intent)) {
            //Cancel
            alarmManager.cancel(pendingIntent);
            pendingIntent.cancel();
        }

        initAlarm();

        //Wake up the device to fire a one-time (non-repeating) alarm in one minute:
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, timeInMillis, pendingIntent);

        Log.i(LOG_ALARM_APP, "Proxima alarma en " + timeInMillis + " millisegundos (" + utils.getMinutes(timeInMillis) + " minutos)");

    }

    public void cancelAlarm(){
        if(alarmManager == null){
            Log.i(LOG_ALARM_APP, "Cancel alarm: No se ha inicializado alarmManager");
            return;
        }

        if(pendingIntent == null){
            Log.i(LOG_ALARM_APP, "Cancel alarm: No se ha inicializado pendingIntent");
            return;
        }

        //Cancel
        alarmManager.cancel(pendingIntent);
        pendingIntent.cancel();

    }

    public boolean exists() {
        // AUN NO ES 100% CONFIABLE

        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        initAlarm();

        boolean exists = true;

        if(!utils.existAlarm(context, Constants.CODE_REQUEST_ALARM_APP, intent)) {
            Log.i(LOG_ALARM_APP, "La alarma no existe");
            //poner alarma!
            exists = false;
        }
        Log.i(LOG_ALARM_APP, "Exist: " + exists );
        return exists;
    }



    //endregion APP ALARM

    //region GET's

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Intent getIntent() {
        return intent;
    }

    public PendingIntent getPendingIntent() {
        return pendingIntent;
    }

    public void setNextDrinkingValue() {
        int allSeconds = settings.getIntervalValue();
        long timeInMillis = utils.getMillis(allSeconds);
        Log.i(LOG_ALARM_APP, "Intervalo de: " + allSeconds + " = en millisegundos: " + timeInMillis);

        //Next Time - Agregar tiempo definido por usuario para la siguiente toma de agua
        long futureTime = System.currentTimeMillis() + timeInMillis;
        long epoch = futureTime/1000;
        String timestamp = String.valueOf(epoch);
        Log.i(LOG_ALARM_APP, "Timestamp actual: " + utils.getCurrentTimestamp());
        Log.i(LOG_ALARM_APP, "Timestamp siguiente bebida: " + timestamp);
        settings.setDrinkNextTime(timestamp);
    }

    //endregion GET's

}
