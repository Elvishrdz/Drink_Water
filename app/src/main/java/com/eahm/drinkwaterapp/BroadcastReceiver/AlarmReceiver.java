package com.eahm.drinkwaterapp.BroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import androidx.core.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.Toast;

import com.eahm.drinkwaterapp.DB.RecordsContract;
import com.eahm.drinkwaterapp.DB.RecordsDbHelper;
import com.eahm.drinkwaterapp.Models.RecordModel;
import com.eahm.drinkwaterapp.R;
import com.eahm.drinkwaterapp.Services.FloatWidgetService;
import com.eahm.drinkwaterapp.Singleton.AlarmApp;
import com.eahm.drinkwaterapp.Class.AwesomeTextsClass;
import com.eahm.drinkwaterapp.Class.SettingsClass;
import com.eahm.drinkwaterapp.ThisApplication;
import com.eahm.drinkwaterapp.Class.NotificationClass;
import com.eahm.drinkwaterapp.Utils.Constants;
import com.eahm.drinkwaterapp.Utils.Utils;
import com.eahm.drinkwaterapp.WaterDrinkActivity;

import java.util.Calendar;

public class AlarmReceiver extends BroadcastReceiver {

    //region VARIABLES
    private final static String LOG_ALARM_RECEIVER = "LOG_ALARM_RECEIVER";
    //endregion VARIABLES

    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle extras = intent.getExtras();

        if(extras != null && extras.containsKey("NOTI_ID")){
            Log.i(LOG_ALARM_RECEIVER, "Cerrar la notificacion");
            int notificationID = extras.getInt("NOTI_ID");
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            notificationManager.cancel(notificationID);
        }

        // Usar JobService o IntentService para operaciones largas.
        Intent mIntent = new Intent(context, NotificationJobService.class);
        mIntent.putExtras(intent); // Enviar todos mis intents
        NotificationJobService.enqueueWork(context, mIntent);
    }

}
