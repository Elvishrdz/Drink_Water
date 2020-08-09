package com.eahm.drinkwaterapp.Class;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import android.util.Log;

import com.eahm.drinkwaterapp.BroadcastReceiver.AlarmReceiver;
import com.eahm.drinkwaterapp.R;
import com.eahm.drinkwaterapp.Utils.Utils;
import com.eahm.drinkwaterapp.WaterDrinkActivity;


public class NotificationClass {

    //region VARIABLES
    private final static String LOG_NOTIFICATIONS = "LOG_NOTIFICATIONS";

    private final int CODE_SKIP = 123;
    private final int CODE_DRINK = 456;
    private final int CODE_CLOSED = 789;
    //endregion VARIABLES

    //region NOTIFICATIONS

    private void createNotificationChannel(String CHANNEL_ID, Context context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            CharSequence name = context.getString(R.string.app_name); //R.string.channel_name
            String description = context.getString(R.string.channel_description); //R.string.channel_description

            int importance = NotificationManager.IMPORTANCE_HIGH; //SET IMPORTANCE OF NOTIFICATION
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    //Using AlarmReceiver.java BroadcastReceiver
    public void showDrinkNotification(String CHANNEL_ID, int NOTIFICATION_ID, Context context, String title, String text, String bigText){

        createNotificationChannel(CHANNEL_ID, context);

        //region THIS NOTIFICATION ON CLICK
        Intent intentNotification = new Intent(context, WaterDrinkActivity.class);
        intentNotification.setAction("ON_NOTIFICATION_CLICKED");
        intentNotification.putExtra("NOTI_ID", NOTIFICATION_ID);
        intentNotification.putExtra("IS_FROM_NOTIFICATION", true);
        intentNotification.putExtra("IS_DRINK_TIME", true);
        intentNotification.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intentNotification, PendingIntent.FLAG_ONE_SHOT);

        //endregion THIS NOTIFICATION ON CLICK

        //region NOTIFICACION CLOSED
        Intent intentDeleted = new Intent(context, AlarmReceiver.class);
        intentDeleted.setAction("ON_DELETED");
        intentDeleted.putExtra("NOTI_ID", NOTIFICATION_ID);
        intentDeleted.putExtra("CODE", "CLOSED");
        PendingIntent pendingIntentDeleted = PendingIntent.getBroadcast(context, CODE_CLOSED, intentDeleted, PendingIntent.FLAG_UPDATE_CURRENT);
        //endregion NOTIFICACION CLOSED

        //region NOTIFICATION BUTTON SKIP
        Intent intentSkip = new Intent(context, AlarmReceiver.class);
        intentSkip.setAction("ON_SKIP");
        intentSkip.putExtra("NOTI_ID", NOTIFICATION_ID);
        intentSkip.putExtra("CODE", "SKIP");
        PendingIntent pendingIntentSkip = PendingIntent.getBroadcast(context, CODE_SKIP, intentSkip, PendingIntent.FLAG_UPDATE_CURRENT);
        //endregion NOTIFICATION BUTTON SKIP

        //region NOTIFICATION BUTTON DRINK
        Intent intentDrink = new Intent(context, AlarmReceiver.class);
        intentDrink.setAction("ON_DRINK");
        intentDrink.putExtra("NOTI_ID", NOTIFICATION_ID);
        intentDrink.putExtra("CODE", "DRINK");
        PendingIntent pendingIntentDrink = PendingIntent.getBroadcast(context, CODE_DRINK, intentDrink, PendingIntent.FLAG_UPDATE_CURRENT);
        //endregion NOTIFICATION BUTTON DRINK

        //region CUSTOMIZE NOTIFICATION
        int notiColor;
        int notiSmallIcon;
        Bitmap notiLargeIcon = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);

        if(new SettingsClass().isGoalReached()){
            notiLargeIcon = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_app_2);

            int random = new Utils().getRandomNumber(1, 5);
            switch (random){
                case 1:
                    notiColor = context.getResources().getColor(R.color.purple);
                    notiSmallIcon = R.drawable.ic_trophy_white;
                    break;
                case 2:
                    notiColor = context.getResources().getColor(R.color.blue);
                    notiSmallIcon = R.drawable.ic_trophy_white;
                    break;
                case 3:
                    notiColor = context.getResources().getColor(R.color.black);
                    notiSmallIcon = R.drawable.ic_trophy_yellow;
                    break;
                case 4:
                    notiColor = context.getResources().getColor(R.color.grey_3);
                    notiSmallIcon = R.drawable.ic_trophy_white;
                    break;
                default:
                    notiColor = context.getResources().getColor(R.color.purple);
                    notiSmallIcon = R.drawable.ic_trophy_yellow;
                    break;
            }
        }
        else {
            int random = new Utils().getRandomNumber(1, 5);
            switch (random){
                case 1:
                    notiColor = context.getResources().getColor(R.color.green_2);
                    notiSmallIcon = R.drawable.ic_drink_holo_dark;
                    break;
                case 2:
                    notiColor = context.getResources().getColor(R.color.black);
                    notiSmallIcon = R.drawable.ic_drink_holo_dark;
                    break;
                case 3:
                    notiColor = context.getResources().getColor(R.color.blue);
                    notiSmallIcon = R.drawable.ic_drink_holo_dark;
                    break;
                case 4:
                    notiColor = context.getResources().getColor(R.color.white);
                    notiSmallIcon = R.drawable.ic_drink_blue;
                    break;
                default:
                    notiColor = context.getResources().getColor(R.color.yellow);
                    notiSmallIcon = R.drawable.ic_drink_blue;
                    break;
            }
        }
        //endregion CUSTOMIZE NOTIFICATION

        //region CREATE THE NOTIFICATION
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                //.setSmallIcon(R.mipmap.ic_launcher, 0) // pequeño circulo a la derecha del icono de la notificacion
                .setSmallIcon(notiSmallIcon)
                .setLargeIcon(notiLargeIcon)
                .setColor(notiColor)
                .setColorized(true)
                .setContentTitle(title)
                .setContentText(text)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(bigText))
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setContentIntent(pendingIntent)
                .addAction(0, context.getResources().getString(R.string.text_skip), pendingIntentSkip) //R.drawable.icon_skip
                .addAction(0, context.getResources().getString(R.string.text_drink), pendingIntentDrink) //R.drawable.icon_drink
                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                //.setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                //.setOnlyAlertOnce(true)
                .setAutoCancel(true)
                .setDeleteIntent(pendingIntentDeleted)
                ;


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setBadgeIconType(Notification.BADGE_ICON_LARGE);
        }

        Uri uriAudio = Uri.parse("android.resource://"+ context.getPackageName() + "/" + R.raw.app_notification_sound);
        long[] arrayVibrate = new long[] { 1000, 1000};

        builder.setSound(uriAudio);
        builder.setVibrate(arrayVibrate);
        builder.setLights(Color.RED, 3000, 3000);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(NOTIFICATION_ID, builder.build());

        //endregion CREATE THE NOTIFICATION

    }


    public void showReportNotification(String CHANNEL_ID, int NOTIFICATION_ID, Context context, String title, String text, String bigText ){

        createNotificationChannel(CHANNEL_ID, context);

        //region THIS NOTIFICATION ON CLICK
        Intent intentNotification = new Intent(context, WaterDrinkActivity.class);
        intentNotification.setAction("ON_NOTIFICATION_CLICKED");
        intentNotification.putExtra("NOTI_ID", NOTIFICATION_ID);
        intentNotification.putExtra("IS_FROM_NOTIFICATION", true);
        intentNotification.putExtra("SHOW_REPORT", true);
        intentNotification.putExtra("IS_LAST_NOTIFICATION", true);
        intentNotification.putExtra("TIMESTAMP", new Utils().getCurrentTimestamp());
        intentNotification.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intentNotification, PendingIntent.FLAG_ONE_SHOT);

        //endregion THIS NOTIFICATION ON CLICK

        //region CUSTOMIZE NOTIFICATION
        int notiColor;
        int notiSmallIcon;
        Bitmap notiLargeIcon = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_app_2);

        if(new SettingsClass().isGoalReached()){
            int random = new Utils().getRandomNumber(1, 2);
            switch (random){
                case 1:
                    notiColor = context.getResources().getColor(R.color.white);
                    notiSmallIcon = R.drawable.icon_trophie_yellow;
                    break;
                default:
                    notiColor = context.getResources().getColor(R.color.yellow);
                    notiSmallIcon = R.drawable.icon_trophie_white;
                    break;
            }
        }
        else {
            int random = new Utils().getRandomNumber(1, 3);
            switch (random){
                case 1:
                    notiColor = context.getResources().getColor(R.color.colorAccent);
                    notiSmallIcon = R.drawable.icon_report_analytics;
                    break;
                case 2:
                    notiColor = context.getResources().getColor(R.color.green);
                    notiSmallIcon = R.drawable.icon_report_analytics;
                    break;
                default:
                    notiColor = context.getResources().getColor(R.color.purple);
                    notiSmallIcon = R.drawable.icon_report_analytics;
                    break;
            }
        }
        //endregion CUSTOMIZE NOTIFICATION

        //region CREATE THE NOTIFICATION
        NotificationCompat.Builder builder =  new NotificationCompat.Builder(context, CHANNEL_ID)
                //.setSmallIcon(R.mipmap.ic_launcher, 0) // pequeño circulo a la derecha del icono de la notificacion
                .setSmallIcon(notiSmallIcon)
                .setLargeIcon(notiLargeIcon)
                .setColor(notiColor)
                .setColorized(true)
                .setContentTitle(title)
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(bigText))
                //.setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                //.setOnlyAlertOnce(true)
                .setAutoCancel(true)
                ;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setBadgeIconType(Notification.BADGE_ICON_LARGE);
        }

        //Uri uriAudio = Uri.parse("android.resource://"+ context.getPackageName() + "/" + R.raw.app_notification_sound);
        long[] arrayVibrate = new long[] { 1000, 1000};

        //builder.setSound(uriAudio);
        builder.setVibrate(arrayVibrate);
        //builder.setLights(Color.RED, 3000, 3000);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(NOTIFICATION_ID, builder.build());

        //endregion CREATE THE NOTIFICATION

    }


    public void showReportNotificationBigPicture(String CHANNEL_ID, int NOTIFICATION_ID, Context context, String title, String text){

        createNotificationChannel(CHANNEL_ID, context);

        //region THIS NOTIFICATION ON CLICK
        Intent intentNotification = new Intent(context, WaterDrinkActivity.class);
        intentNotification.setAction("ON_NOTIFICATION_CLICKED");
        intentNotification.putExtra("NOTI_ID", NOTIFICATION_ID);
        intentNotification.putExtra("IS_FROM_NOTIFICATION", true);
        intentNotification.putExtra("SHOW_REPORT", true);
        intentNotification.putExtra("IS_LAST_NOTIFICATION", true);
        intentNotification.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intentNotification, PendingIntent.FLAG_ONE_SHOT);

        //endregion THIS NOTIFICATION ON CLICK

        //region CREATE THE NOTIFICATION
        NotificationCompat.Builder builder =  new NotificationCompat.Builder(context, CHANNEL_ID)
                //.setSmallIcon(R.mipmap.ic_launcher, 0) // pequeño circulo a la derecha del icono de la notificacion
                .setSmallIcon(R.drawable.icon_report_analytics)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                .setColor(context.getResources().getColor(R.color.com_facebook_blue))
                .setColorized(true)
                .setContentTitle(title)
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                //.setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                //.setOnlyAlertOnce(true)
                .setAutoCancel(true)
                ;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setBadgeIconType(Notification.BADGE_ICON_LARGE);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.design_welcome_top);
            builder.setStyle(new NotificationCompat.BigPictureStyle()
                    .bigPicture(bitmap)
                    .bigLargeIcon(null));
        }

        //Uri uriAudio = Uri.parse("android.resource://"+ context.getPackageName() + "/" + R.raw.app_notification_sound);
        long[] arrayVibrate = new long[] { 1000, 1000};

        //builder.setSound(uriAudio);
        builder.setVibrate(arrayVibrate);
        //builder.setLights(Color.RED, 3000, 3000);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(NOTIFICATION_ID, builder.build());

        //endregion CREATE THE NOTIFICATION

    }


    //endregion NOTIFICATIONS

    //region AUDIOS MANAGEMENT
    private void playAudio(Context context, int audioResourceID){

        final int APP_NOTIFICATION_SOUND = R.raw.app_notification_sound;

        String name = context.getResources().getResourceName(audioResourceID);
        Log.i(LOG_NOTIFICATIONS, "ID NOMBRE; " + name);

        if (name == null || !name.startsWith(context.getPackageName())) {
            // id is not an id used by a layout element.
            return;
        }

        AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        Vibrator vib = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

        switch (am.getRingerMode()) {
            case AudioManager.RINGER_MODE_SILENT:
                Log.i("MyApp","Silent mode");
                break;
            case AudioManager.RINGER_MODE_VIBRATE:
                Log.i("MyApp","Vibrate mode");
                vib.vibrate(400);
                break;
            case AudioManager.RINGER_MODE_NORMAL:
                Log.i("MyApp","Normal mode");

                //  MediaPlayer.create(DriverMapActivity.this, soundID).start();

                MediaPlayer mediaPlayer = MediaPlayer.create(context, audioResourceID);
                vib.vibrate(400);
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mediaPlayer.release();
                    }
                });
                mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                    @Override
                    public boolean onError(MediaPlayer mp, int what, int extra) {
                        try{
                            mediaPlayer.release();
                        }
                        catch (Exception ignore){

                        }

                        return false;
                    }
                });

                break;
        }
    }
    //endregion
}
