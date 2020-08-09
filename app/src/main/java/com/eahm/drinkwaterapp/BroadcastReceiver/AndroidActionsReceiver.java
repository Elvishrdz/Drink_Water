package com.eahm.drinkwaterapp.BroadcastReceiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

import com.eahm.drinkwaterapp.Class.SettingsClass;
import com.eahm.drinkwaterapp.Singleton.AlarmApp;
import com.eahm.drinkwaterapp.Singleton.AlarmFirst;
import com.eahm.drinkwaterapp.Singleton.AlarmLast;
import com.eahm.drinkwaterapp.ThisApplication;
import com.eahm.drinkwaterapp.Utils.Constants;
import com.eahm.drinkwaterapp.Utils.Utils;
import com.eahm.drinkwaterapp.WaterDrinkActivity;

public class AndroidActionsReceiver extends BroadcastReceiver {

    //region VARIABLES
    private final static String TAG_BOOT_COMPLETED = "TAG_BOOT_COMPLETED";

    Context context;

    SettingsClass settings = new SettingsClass();
    Utils utils = new Utils();

    boolean isAppVisible = false;
    //endregion VARIABLES

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        Log.i(TAG_BOOT_COMPLETED, "AndroidActionsReceiver ejecutado...");

        String action = intent.getAction();
        if(action == null){
            Log.i(TAG_BOOT_COMPLETED, "AndroidActionsReceiver Action NULL. Terminado");
            return;
        }

        if(ThisApplication.isActivityVisible()){
            //Toast.makeText(context, "La app esta visible", Toast.LENGTH_SHORT).show();
            isAppVisible = true;
        }

        //if( ThisApplication.isActivityVisible()){
        //    Toast.makeText(context, "La app esta visible", Toast.LENGTH_SHORT).show();
        //}

        switch (action) {
            case Intent.ACTION_SCREEN_OFF:
                //region ACTION AUTOMATICALLY SCREEN IS OFF
                Log.i(TAG_BOOT_COMPLETED, "Action: Screen Off");

                if(isAppVisible){
                    Log.i(TAG_BOOT_COMPLETED, "Enviando intent a WaterDrinkActivity.class " + action);
                    //Si deseas enviar algun mensaje a la activity desde aqui
                    Intent intentWaterDrinkA = new Intent(context, WaterDrinkActivity.class);
                    intentWaterDrinkA.setAction("ON_BROADCAST_RECEIVED");
                    intentWaterDrinkA.putExtra("FROM", action);
                    intentWaterDrinkA.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP); // La activity esta activa
                    context.startActivity(intentWaterDrinkA);

                }
                //endregion ACTION AUTOMATICALLY SCREEN IS OFF
                break;
            case Intent.ACTION_BOOT_COMPLETED:
                //region ACTION BOOT COMPLETED
                Log.i(TAG_BOOT_COMPLETED, "Action: Boot Completed");

                long timeInMillis = 0;

                String timestamp = settings.getDrinkNextTime();

                if(timestamp.isEmpty()){
                    // No hay registro de la proxima bebida
                }
                else if(timestamp.equalsIgnoreCase("OVER")){
                    // La alarma ya termino
                }
                else {

                    Log.i(TAG_BOOT_COMPLETED, "Tiempo siguiente guardado: " + timestamp);

                    timeInMillis = utils.getMillis(timestamp);
                    Log.i(TAG_BOOT_COMPLETED, "Timestamp nextDrink en millis: " + timeInMillis);

                    long currentTimestamp = System.currentTimeMillis()/1000;
                    Log.i(TAG_BOOT_COMPLETED, "Timestamp actual en millis: " + timeInMillis);

                    timeInMillis = Long.parseLong(timestamp);

                    if(timeInMillis <= currentTimestamp){
                        Log.i(TAG_BOOT_COMPLETED, "Beber agua, (ya se paso la hora definida)");
                        timeInMillis = 0;
                    }
                    else {
                        int secondsRemaining = (int) (timeInMillis - currentTimestamp);
                        timeInMillis = secondsRemaining * 1000; // convert in millis
                        Log.i(TAG_BOOT_COMPLETED, "Siguiente bebida en: " + timestamp);
                    }

                }

                Log.i(TAG_BOOT_COMPLETED, "Alarmas establecidas timeinmillis: " + timeInMillis);
                AlarmFirst.getInstance().adminFirstAlarm(context);
                AlarmLast.getInstance().adminLastAlarm(context);

                AlarmApp.getInstance().initialize(context);
                AlarmApp.getInstance().updateAlarm(timeInMillis);
                //endregion ACTION BOOT COMPLETED
                break;
        }

        Log.i(TAG_BOOT_COMPLETED, "AndroidActionsReceiver Terminado.");
    }

}
