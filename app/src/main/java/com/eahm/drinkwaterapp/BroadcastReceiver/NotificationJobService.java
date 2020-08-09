package com.eahm.drinkwaterapp.BroadcastReceiver;


import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;
import androidx.core.app.NotificationManagerCompat;

import com.eahm.drinkwaterapp.Class.AwesomeTextsClass;
import com.eahm.drinkwaterapp.Class.NotificationClass;
import com.eahm.drinkwaterapp.Class.SettingsClass;
import com.eahm.drinkwaterapp.DB.RecordsContract;
import com.eahm.drinkwaterapp.DB.RecordsDbHelper;
import com.eahm.drinkwaterapp.Models.RecordModel;
import com.eahm.drinkwaterapp.R;
import com.eahm.drinkwaterapp.Services.FloatWidgetService;
import com.eahm.drinkwaterapp.Singleton.AlarmApp;
import com.eahm.drinkwaterapp.ThisApplication;
import com.eahm.drinkwaterapp.Utils.Constants;
import com.eahm.drinkwaterapp.Utils.Utils;
import com.eahm.drinkwaterapp.WaterDrinkActivity;

import java.util.Calendar;

public class NotificationJobService extends JobIntentService {

    private static final int JOB_ID = 2; // Unique job ID for this service

    public static void enqueueWork(Context context, Intent intent) {
        enqueueWork(context, NotificationJobService.class, JOB_ID, intent);
    }


    //region VARIABLES
    private final static String LOG_JOB_SERVICE = "LOG_JOB_SERVICE";
    private static final String LOG_RECORD = "LOG_RECORD";

    Context context;

    boolean isAppVisible = false;

    String actionName = "";
    String actionCodeName = "";

    Utils utils = new Utils();
    SettingsClass settings = new SettingsClass();
    AwesomeTextsClass awesomeTexts = new AwesomeTextsClass();

    RecordsDbHelper dbHelper;
    SQLiteDatabase dbWrite;
    SQLiteDatabase dbRead;

    //endregion VARIABLES

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        this.context = getApplicationContext();

        Log.i(LOG_JOB_SERVICE, "Alarma Recibida, bien hecho!");

        Bundle extras = intent.getExtras();

        if(extras == null){
            Log.i(LOG_RECORD, "No se registro tu accion :(");
            Log.i(LOG_JOB_SERVICE, "Extras nulos");
            return;
        }

        actionName = "";
        actionCodeName = "";

        actionName = intent.getAction();
        actionCodeName = extras.getString("CODE","");


        if(ThisApplication.isActivityVisible()){
            Log.i(LOG_RECORD, "base de datos inicializada");
            isAppVisible = true;
        }

        dbHelper = new RecordsDbHelper(context);
        Log.i(LOG_RECORD, "base de datos inicializada");

        //region FIRST AND LAST NOTIFICATIONS
        if(actionCodeName.equalsIgnoreCase("MORNING")){
            //region MORNING NOTIFICATION
            Log.i(Constants.LOG_MORNING_ALARM, "Alarma Recibida: MORNING");
            settings.setAlarmWorking(true);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

            //revisamos si existe la notificacion de la app
            if(notificationManager.areNotificationsEnabled()){
                Log.i(Constants.LOG_MORNING_ALARM, "Si hay notificaciones (puede estar activa) entonces vamos a cancelar nuestra notificacion");
                notificationManager.cancel(Constants.NOTIFICATION_ID_APP);
            }

            //deleteRecords();
            deleteRecordsFrom2DaysAgo();
            settings.deleteTodayProgress(); // reiniciar el progreso de hoy
            settings.setContratulationStatus(false);
            settings.setDrinkNextTime("");

            //Si la app no esta visible, mostramos una notificacion mañanera hurra!
            if(!isAppVisible){
                Log.i(Constants.LOG_MORNING_ALARM, "Mostrar notificacion, la app no esta activa");
                new NotificationClass().showDrinkNotification(Constants.CHANNEL_ID_APP, Constants.NOTIFICATION_ID_APP, context,
                        awesomeTexts.getTitle(1, context),
                        awesomeTexts.getText(1, context),
                        awesomeTexts.getBigText(1, context));
            }
            else {
                Log.i(Constants.LOG_MORNING_ALARM, "Estas en la app, no puedo mostrarte la notificacion");
            }

            //endregion MORNING NOTIFICATION
            return;
        }
        else if(actionCodeName.equalsIgnoreCase("LAST")){
            //region LAST NOTIFICATION
            Log.i(Constants.LOG_LAST_ALARM, "Alarma Recibida: LAST");
            settings.setAlarmWorking(false);
            settings.setDrinkNextTime("OVER");

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

            //revisamos si existe la notificacion de la app
            if(notificationManager.areNotificationsEnabled()){
                Log.i(Constants.LOG_LAST_ALARM, "Si hay notificaciones (puede estar activa) entonces vamos a cancelar nuestra notificacion");
                notificationManager.cancel(Constants.NOTIFICATION_ID_REPORT);
                notificationManager.cancel(Constants.NOTIFICATION_ID_APP); // Si esta la notificacion la terminamos en este momento, ya que el dia termino. continuamos hasta el dia siguiente
            }

            Log.i(Constants.LOG_LAST_ALARM, "Mostrar notificacion, aun si la app esta activa o no");
            new NotificationClass().showReportNotification(Constants.CHANNEL_ID_REPORT, Constants.NOTIFICATION_ID_REPORT, context,
                    awesomeTexts.getTitle(2, context),
                    awesomeTexts.getText(2, context),
                    awesomeTexts.getBigText(2, context));

            if(!isAppVisible){
                Log.i(Constants.LOG_LAST_ALARM, "App NO visible");
            }
            else {
                Log.i(Constants.LOG_LAST_ALARM, "Estas en la app");
                Log.i(Constants.LOG_LAST_ALARM, "Termino el dia, configurar app a modo TERMINADO");
                //Si deseas enviar algun mensaje a la activity desde aqui
                Intent intentWaterDrinkA = new Intent(context, WaterDrinkActivity.class);
                intentWaterDrinkA.setAction("ON_BROADCAST_RECEIVED");
                intentWaterDrinkA.putExtra("FROM", actionCodeName);
                intentWaterDrinkA.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP); // La activity esta activa
                context.startActivity(intentWaterDrinkA);

            }
            //endregion LAST NOTIFICATION
            return;
        }
        //endregion FIRST AND LAST NOTIFICATIONS

        AlarmApp.getInstance().initialize(context);

        if(actionCodeName.equalsIgnoreCase("CLOSED")){
            //region NOTIFICATION WAS CLOSED
            //La notificacion fue cerrada, mostrarla en 5 minutos
            if(isAppVisible){
                /* Si deseas enviar algun mensaje a la activity desde aqui
                Intent intentWaterDrinkA = new Intent(context, WaterDrinkActivity.class);
                intentWaterDrinkA.setAction("ON_BROADCAST_RECEIVED");
                intentWaterDrinkA.putExtra("FROM", actionCodeName);
                intentWaterDrinkA.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP); // La activity esta activa
                context.startActivity(intentWaterDrinkA);
                */
                Log.i(LOG_JOB_SERVICE, "Notificacion cerrada. La app esta visible");
            }
            else{
                recordDrink(0,5);
                int minutes = 10;
                long nextTime = SystemClock.elapsedRealtime() + (minutes * 60 * 1000); // cada X minutos
                AlarmApp.getInstance().updateAlarm(nextTime);
                Log.i(LOG_JOB_SERVICE, "Notificacion cerrada. La app NO esta visible, establecemos alarma " + minutes + " minutos.");
            }

            //endregion NOTIFICATION WAS CLOSED
        }
        else if(actionCodeName.equalsIgnoreCase("SKIP")){
            //region NOTIFICATION WAS SKIPPED
            if(isAppVisible){
                /* Si deseas enviar algun mensaje a la activity desde aqui
                Intent intentWaterDrinkA = new Intent(context, WaterDrinkActivity.class);
                intentWaterDrinkA.setAction("ON_BROADCAST_RECEIVED");
                intentWaterDrinkA.putExtra("FROM", actionCodeName);
                intentWaterDrinkA.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP); // La activity esta activa
                context.startActivity(intentWaterDrinkA);
                */
                Log.i(LOG_JOB_SERVICE, "Notificacion Saltada. La app esta visible");
            }
            else {
                recordDrink(0,4);
                Log.i(LOG_JOB_SERVICE, "Notificacion Saltada. La app NO esta visible, establecemos alarma");
                int minutes = utils.getMinutes(settings.getNotiSkipTime() * 1000);
                long alarm = SystemClock.elapsedRealtime() + (minutes * 60 * 1000); // cada X minutos
                AlarmApp.getInstance().updateAlarm(alarm);
            }
            //endregion NOTIFICATION WAS SKIPPED
        }
        else if(actionCodeName.equalsIgnoreCase("DRINK")){
            //region USER DRINK WATER!
            if(isAppVisible){
                /* Si deseas enviar algun mensaje a la activity desde aqui
                Intent intentWaterDrinkA = new Intent(context, WaterDrinkActivity.class);
                intentWaterDrinkA.setAction("ON_BROADCAST_RECEIVED");
                intentWaterDrinkA.putExtra("FROM", actionCodeName);
                intentWaterDrinkA.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP); // La activity esta activa
                context.startActivity(intentWaterDrinkA);
                */
                Log.i(LOG_JOB_SERVICE, "Notificacion DRINK. La app esta visible");

            }
            else {
                Log.i(LOG_JOB_SERVICE, "Notificacion DRINK. La app NO esta visible, establecemos alarma");

                // Obtener tiempo intervalo especificado por usuario
                // resetear el tiempo para el cronometro en caso de ir a la app
                // añadir ajuste de cantidad por defecto para la notificacion... al presionar siempre contar 500ml (por ejemplo)

                // Tiempo para la siguiente alarma
                long timeInMillis = utils.getMillis(settings.getIntervalValue());
                long alarmNextTime = SystemClock.elapsedRealtime() + timeInMillis; // cada X minutos

                AlarmApp.getInstance().setNextDrinkingValue();
                AlarmApp.getInstance().updateAlarm(alarmNextTime);

                float notiDrinkAmount = settings.getNotiDrinkAmount();
                float currentProgress = settings.getTodayProgress();

                currentProgress = currentProgress + notiDrinkAmount;

                settings.setTodayProgress(currentProgress); // progress always in mililiters

                recordDrink(notiDrinkAmount,2);

                //Run in main thread
                Handler handler = new Handler(Looper.getMainLooper());

                if(settings.isGoalReached() && !settings.getContratulationStatus()){
                    settings.setContratulationStatus(true);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (Settings.canDrawOverlays(context)) {
                            context.startService(new Intent(context, FloatWidgetService.class));  // Mostrar regalo en pantalla
                        }
                        else {
                            //no hay permisos para mostrar regalo en pantalla
                        }
                    }

                    // Run in main thread
                    handler.post(()->{
                        Toast.makeText(context, context.getString(R.string.text_congratulations_04), Toast.LENGTH_LONG).show();
                    });

                }
                else if(utils.getRandomNumber(0,10) > 5){
                    // Run in main thread
                    handler.post(()->{
                        Toast.makeText(context, context.getString(R.string.text_next_in) + " " + utils.getTimeString(utils.getHours(timeInMillis), utils.getMinutes(timeInMillis), utils.getSeconds(timeInMillis), context), Toast.LENGTH_LONG).show();
                    });
                }
                else {
                    // Run in main thread
                    handler.post(()->{
                        String amountString = String.valueOf(notiDrinkAmount);
                        if(notiDrinkAmount % 1 == 0){
                            Long integer = (long) notiDrinkAmount;
                            amountString = String.valueOf(integer);
                        }

                        Toast.makeText(context, context.getString(R.string.text_well_done) + " +" + amountString + context.getString(R.string.text_vol_unit_mililiters), Toast.LENGTH_LONG).show();
                    });
                }

            }
            //endregion USER DRINK WATER!
        }
        else if(actionCodeName.isEmpty() && !isAppVisible && settings.isAlarmWorking()){
            // La alarma llego antes! establecer esta hora como nuestra hora de beber ya que
            // si entramos en la app podria estar el cronometro aun corriendo... :O
            settings.setDrinkNextTime(utils.getCurrentTimestamp());

           /* new NotificationClass().showDrinkNotification(Constants.CHANNEL_ID_APP, Constants.NOTIFICATION_ID_APP, context,
                   "Tomar Agua",
                   "Entra ahora y programa tu proxima bebida",
                   "Es importante mantener hidratado tu cuerpo para realizar todas tus actividades al maximo nivel");*/

            new NotificationClass().showDrinkNotification(Constants.CHANNEL_ID_APP, Constants.NOTIFICATION_ID_APP, context,
                    awesomeTexts.getTitle(3, context),
                    awesomeTexts.getText(3, context),
                    awesomeTexts.getBigText(3, context));
        }

        //region LOGS
        if(!actionCodeName.isEmpty()){
            Log.i(LOG_JOB_SERVICE, actionCodeName + ": La alarma se ejecutó");
            //AlarmApp.getInstance().updateAlarm(context, nextTime, actionCodeName);
        }
        else {
            Log.i(LOG_JOB_SERVICE, "Sin ActionName, La alarma se ejecutó");
        }
        //endregion LOGS

        if(dbHelper != null) dbHelper.close();
    }



    private void recordDrink(float drinkAmount, int statusCode) {
        // Gets the data repository in write mode
        dbWrite = dbHelper.getWritableDatabase();

        RecordModel newRecord = new RecordModel(Long.parseLong(utils.getCurrentTimestamp()) , drinkAmount, statusCode);

        // Insert the new row, returning the primary key value of the new row
        long newRowId = dbWrite.insert(RecordsContract.RecordsEntry.TABLE_NAME,
                null,
                newRecord.toContentValues()
        );

        dbWrite.close();
        Log.i(LOG_RECORD, "Registramos la bebida en Notificacion. Resultado: " + (newRowId == -1 ? "FALLO al guardar" : "Registrado!"));

    }

    private void deleteRecords(){
        // Gets the data repository in write mode
        dbWrite = dbHelper.getWritableDatabase();

        // Insert the new row, returning the primary key value of the new row
        long newRowId = dbWrite.delete(RecordsContract.RecordsEntry.TABLE_NAME, null, null);

        dbWrite.close();
        Log.i(LOG_RECORD, "Borramos todos los registros. Resultado: " + (newRowId == -1 ? "FALLO al borrar" : "Limpiado!"));
    }

    private void deleteRecordsFrom2DaysAgo(){
        // Gets the data repository in write mode
        dbWrite = dbHelper.getWritableDatabase();

        // Insert the new row, returning the primary key value of the new row
        Calendar rightNow = Calendar.getInstance();
        rightNow.add(Calendar.DATE, -2); // Establecer hace 2 dias atras

        rightNow.set(Calendar.HOUR_OF_DAY, 0);
        rightNow.set(Calendar.MINUTE, 0);
        rightNow.set(Calendar.SECOND, 0);
        long startOfToday = rightNow.getTimeInMillis() / 1000L;

        rightNow.set(Calendar.HOUR_OF_DAY, 23);
        rightNow.set(Calendar.MINUTE, 59);
        rightNow.set(Calendar.SECOND, 59);
        long endOfToday = rightNow.getTimeInMillis() / 1000L;

        String query = "DELETE * FROM " + RecordsContract.RecordsEntry.TABLE_NAME +
                " WHERE " +
                RecordsContract.RecordsEntry.COLUMN_NAME_TIMESTAMP + " >= " + startOfToday
                + " AND " +
                RecordsContract.RecordsEntry.COLUMN_NAME_TIMESTAMP + " <= " + endOfToday;

        String where =  RecordsContract.RecordsEntry.COLUMN_NAME_TIMESTAMP + " >= " + startOfToday + " AND " + RecordsContract.RecordsEntry.COLUMN_NAME_TIMESTAMP + " <= " + endOfToday;

        long delete = dbWrite.delete(RecordsContract.RecordsEntry.TABLE_NAME, where, null);

        dbWrite.close();
        Log.i(LOG_RECORD, "Borramos registros de hace 2 dias! (si es que existen...) Respuesta: " + delete);
    }

}
