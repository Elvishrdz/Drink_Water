package com.eahm.drinkwaterapp.Utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.eahm.drinkwaterapp.Class.SettingsClass;
import com.eahm.drinkwaterapp.Interfaces.Callbacks;
import com.eahm.drinkwaterapp.R;

import org.joda.time.DateTime;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class Utils {

    public String LoadData(Context context, String inFile) {
        String tContents = "";

        try {
            InputStream stream = context.getAssets().open(inFile);

            int size = stream.available();
            byte[] buffer = new byte[size];
            stream.read(buffer);
            stream.close();
            tContents = new String(buffer);
        } catch (IOException e) {
            // Handle exceptions here
            tContents = "Fail: " + e.toString();
        }

        return tContents;

    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static String getDayName(int day, Context context) {

        switch (day){
            case 1:
                return context.getString(R.string.monday);
            case 2:
                return context.getString(R.string.tuesday);
            case 3:
                return context.getString(R.string.wednesday);
            case 4:
                return context.getString(R.string.thursday);
            case 5:
                return context.getString(R.string.friday);
            case 6:
                return context.getString(R.string.saturday);
            case 7:
                return context.getString(R.string.sunday);
            default:
                return "DAY_NOT_FOUNDED";
        }


    }

    public static String getMonthName(int month, Context context){

        switch (month){
            case 1:
                return context.getString(R.string.january);
            case 2:
                return context.getString(R.string.fabruary);
            case 3:
                return context.getString(R.string.march);
            case 4:
                return context.getString(R.string.april);
            case 5:
                return context.getString(R.string.may);
            case 6:
                return context.getString(R.string.june);
            case 7:
                return context.getString(R.string.july);
            case 8:
                return context.getString(R.string.august);
            case 9:
                return context.getString(R.string.september);
            case 10:
                return context.getString(R.string.october);
            case 11:
                return context.getString(R.string.november);
            case 12:
                return context.getString(R.string.december);
            default:
                return "DAY_NOT_FOUNDED";
        }

    }

    public static String getUserCountry(Context context) {
        try {
            final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            final String simCountry = tm.getSimCountryIso();
            if (simCountry != null && simCountry.length() == 2) { // SIM country code is available
                return simCountry.toLowerCase(Locale.US);
            }
            else if (tm.getPhoneType() != TelephonyManager.PHONE_TYPE_CDMA) { // device is not 3G (would be unreliable)
                String networkCountry = tm.getNetworkCountryIso();
                if (networkCountry != null && networkCountry.length() == 2) { // network country code is available
                    return networkCountry.toLowerCase(Locale.US);
                }
            }
        }
        catch (Exception e) {

        }
        return null;
    }

    public static String convertFirstLetterToUpperCase(String str) {

        // Create a char array of given String
        char ch[] = str.toCharArray();
        for (int i = 0; i < str.length(); i++) {

            // If first character of a word is found
            if (i == 0 && ch[i] != ' ' ||
                    ch[i] != ' ' && ch[i - 1] == ' ') {

                // If it is in lower-case
                if (ch[i] >= 'a' && ch[i] <= 'z') {

                    // Convert into Upper-case
                    ch[i] = (char)(ch[i] - 'a' + 'A');
                }
            }

            // If apart from first character
            // Any one is in Upper-case
            else if (ch[i] >= 'A' && ch[i] <= 'Z')

                // Convert into Lower-Case
                ch[i] = (char)(ch[i] + 'a' - 'A');
        }

        // Convert the char array to equivalent String
        String st = new String(ch);
        return st;
    }


    public static int convertDpToPx(float dp, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    public static int convertSpToPx(float sp, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
    }

    public String getDeviceName(Context context){
        //https://developer.android.com/training/articles/user-data-ids.html
        //String uniqueID = UUID.randomUUID().toString();
        @SuppressLint("HardwareIds") String android_id = Settings.Secure.getString(context.getContentResolver(),Settings.Secure.ANDROID_ID);
        if (android_id == null) android_id = "117";
        return  Build.MANUFACTURER
                + "_" + Build.MODEL + "_" + Build.VERSION.RELEASE
                + "_" + Build.VERSION_CODES.class.getFields()[android.os.Build.VERSION.SDK_INT].getName()
                + "_" + android_id;
    }

    public String validateEmail(String email, Context context) {

        if (email.isEmpty()) { 
            return context.getString(R.string.validate_email_empty);
        }
        else if (!email.contains("@") || !email.contains(".")){
            return context.getString(R.string.validate_email_invalid);
        }

        return "TRUE";
    }
    public String validatePassword(String password, Context context){

        if (password.isEmpty()) {
            return context.getString(R.string.validate_password_empty);
        }
        else if (password.length() < 6) {
            return context.getString(R.string.validate_password_too_short);
        }

        return "TRUE";
    }
    public String validatePhoneNumber(String phoneNumber, Context context) {

        if (phoneNumber.isEmpty()) {
            return context.getString(R.string.validate_phone_empty);
        } else if (phoneNumber.length() < 8) {
            return context.getString(R.string.validate_phone_incomplete);
        } else if (phoneNumber.length() > 15) {
            return context.getString(R.string.validate_phone_too_long);
        } else if (!phoneNumber.matches("[0-9]+")) {
            return context.getString(R.string.validate_phone_only_numbers);
        }

        return "TRUE";
    }

    public void displayAlertDialogOneOption(Activity activity, final Callbacks callback, final String CODE, String message, String yesText, boolean isCancelable){
       DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        callback.onYesResponse(CODE,"");
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(message)
                .setPositiveButton(yesText, dialogClickListener)
                .setCancelable(isCancelable).show();


    }

    public void displayAlertDialogYesNo(Activity activity, final Callbacks callback, final String CODE, String message, String yesText, String noText, boolean isCancelable){
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        callback.onYesResponse(CODE,"");
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        callback.onNoResponse(CODE, "");
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(message).setPositiveButton(yesText, dialogClickListener)
                .setNegativeButton(noText, dialogClickListener).setCancelable(isCancelable).show();


    }

    public void setDimensionToFitScreen(Activity activity, VideoView videoView) {
        // Adjust the size of the video
        // so it fits on the screen
        float videoProportion = getVideoProportion();
        int screenWidth = activity.getResources().getDisplayMetrics().widthPixels;
        int screenHeight = activity.getResources().getDisplayMetrics().heightPixels;
        float screenProportion = (float) screenHeight / (float) screenWidth;
        android.view.ViewGroup.LayoutParams lp = videoView.getLayoutParams();

        if (videoProportion < screenProportion) {
            lp.height= screenHeight;
            lp.width = (int) ((float) screenHeight / videoProportion);
        } else {
            lp.width = screenWidth;
            lp.height = (int) ((float) screenWidth * videoProportion);
        }
        videoView.setLayoutParams(lp);
    }

    // This method gets the proportion of the video that you want to display.
    // I already know this ratio since my video is hardcoded, you can get the
    // height and width of your video and appropriately generate  the proportion
    //    as :height/width
    private float getVideoProportion(){
        return 1.5f;
    }

    public static int getDeviceHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics mDisplayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(mDisplayMetrics);
        return mDisplayMetrics.heightPixels;
    }

    public static int getDeviceWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics mDisplayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(mDisplayMetrics);
        return mDisplayMetrics.widthPixels;
    }

    public boolean existAlarm(Context context, int RequestCode, Intent intent){
        return (PendingIntent.getBroadcast( context, RequestCode, intent, PendingIntent.FLAG_NO_CREATE) != null);
    }

    public int getRandomNumber(int min,int max) {
        //This gives a random integer between max (inclusive) and min (exclusive).
        return (new Random()).nextInt((max - min) + 1) + min;
    }

    public long getMillis(int hour, int minute) {
        long hoursInMillis = hour * 60 * 60 * 1000;
        long secondsInMillis = minute * 60 * 1000;

        return hoursInMillis + secondsInMillis;
    }

    public long getMillis(int allseconds) {
        return allseconds * 1000;
    }




    public long getMillis(String timestamp) {
        return Long.parseLong(timestamp) * 1000  ;
    }

    public int getSeconds(int hour, int minute) {
        return (hour * 3600) + (minute * 60);
    }

    public int getSeconds(long timeInMillis) {
        return ((int) (timeInMillis / 1000) % 60 );
    }

    public int getMinutes(long timeInMillis){
        return (int) ((timeInMillis / (1000*60)) % 60);
    }

    public int getHours(long timeInMillis){
        return (int) ((timeInMillis / (1000*60*60)) % 24);
    }

    public String getCurrentTimestamp() {
        return String.valueOf(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()));
    }


    private DateTime getDateFromStringTimestamp(String timestamp){
        Timestamp stamp = new Timestamp(Long.parseLong(timestamp));
        Date date1 = new Date(stamp.getTime());
        DateTime datetime = new DateTime(date1);
        return datetime;
    }

    public static DateTime getDateTimeByTimestamp(String timestamp) {
        return new DateTime(Long.parseLong(timestamp) * 1000L);
    }

    public String convertSecondTo_HHmmss_String(int totalAmountOfSeconds) {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        df.setTimeZone(tz);
        return df.format(new Date(totalAmountOfSeconds*1000L));

    }

      public String calculateTimeBetweenDates(DateTime startRideDateTime, DateTime endRideDateTime, Context context ){

        int startSeconds = (startRideDateTime.getHourOfDay() * 3600) + (startRideDateTime.getMinuteOfHour() * 60) + startRideDateTime.getSecondOfMinute();
        int endSeconds = (endRideDateTime.getHourOfDay() * 3600) + (endRideDateTime.getMinuteOfHour() * 60) + endRideDateTime.getSecondOfMinute();

        int totalSecs = endSeconds - startSeconds;

        String result = convertSecondTo_HHmmss_String(totalSecs);

        String[] parts = result.split(":");
        int hours = Integer.parseInt( parts[0] );
        int minutes = Integer.parseInt( parts[1] );
        int secs = Integer.parseInt( parts[2] );

        return getTimeString(hours, minutes, secs, context);
    }

    public String getTimeString(int hourOfDay, int minute, int seconds, Context context) {

        String a = "";

        String hora = context.getResources().getString(R.string.text_hours);
        String minutos = context.getResources().getString(R.string.text_minutes);
        String segundos = context.getResources().getString(R.string.text_seconds);

        if(hourOfDay == 1) hora = context.getResources().getString(R.string.text_hour);
        if(minute == 1) minutos = context.getResources().getString(R.string.text_minute);
        if(seconds == 1) segundos = context.getResources().getString(R.string.text_second);

        if(hourOfDay > 0){
            a = a + hourOfDay + " " + hora + " ";
        }

        if(minute > 0) {
            a = a + minute + " " + minutos + " ";
        }

        if(seconds != 0){
            a = a + seconds + " " + segundos + " ";
        }

        return a;
    }


    public int getTimeByPos(int allSeconds, int position){
        String result = convertSecondTo_HHmmss_String(allSeconds);

        String[] parts = result.split(":");

        if(position == 0) return Integer.parseInt( parts[0] );
        if(position == 1 ) return Integer.parseInt( parts[1] );
        if(position == 2) return Integer.parseInt( parts[2] );

        return -7;
    }

    public String getFormattedTimeAMPM(int hourOfDay, int minute, int seconds, boolean AMPM, boolean useSeconds){
        /*long allSeconds = Long.parseLong( dataset.get(position));
        String stringTime = utils.getHours(allSeconds) + ":" + utils.getMinutes(allSeconds) + "." + utils.getSeconds(allSeconds);*/

       /* long millis = Long.parseLong(timestamp) * 1000L;

        int hourOfDay = getHours(millis);
        int minute = getMinutes(millis);
        int seconds = getSeconds(millis);*/

        final String ZERO = "0";

        //Formateo el hora obtenido: antepone el 0 si son menores de 10
        String formattedHour =  (hourOfDay < 10)? String.valueOf(ZERO + hourOfDay) : String.valueOf(hourOfDay);

        //Formateo el minuto obtenido: antepone el 0 si son menores de 10
        String formattedMinute = (minute < 10)? String.valueOf(ZERO + minute):String.valueOf(minute);

        //Obtengo el valor a.m. o p.m., dependiendo de la selección del usuario
        String AM_PM;
        if(hourOfDay < 12) {
            if(hourOfDay == 0) formattedHour = "12";
            AM_PM = "a.m.";
        } else {
            formattedHour = String.valueOf( hourOfDay - 12 );
            AM_PM = "p.m.";
        }
        //Muestro la hora con el formato deseado
        String formattedTime = formattedHour + ":" + formattedMinute + " " + AM_PM;

        if(useSeconds){
            formattedTime = formattedHour + ":" + formattedMinute + " " + seconds + " " + AM_PM;
        }

        return formattedTime;

    }

    public View createCustomTitle(Context context, String title, int textColor, int backgroundColor, int textSizeDP) {
        // Create a TextView programmatically
        TextView tv = new TextView(context);
        ViewGroup.LayoutParams lp = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, // Width of TextView
                ViewGroup.LayoutParams.WRAP_CONTENT); // Height of TextView
        tv.setLayoutParams(lp);
        tv.setPadding(10, 10, 10, 10);
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP,textSizeDP);
        tv.setText(title);
        tv.setTextColor(textColor);
        tv.setBackgroundColor(backgroundColor);

        return tv;
    }

    public String getTodayString(Context context) {

        DateTime datetime = Utils.getDateTimeByTimestamp(getCurrentTimestamp());

        String stringDate = Utils.getDayName(datetime.getDayOfWeek(),context) + " " +
                datetime.getDayOfMonth() + " " + context.getResources().getString(R.string.text_of) + " " +
                Utils.getMonthName(datetime.getMonthOfYear(), context) + " " +
                datetime.getYear();

        return stringDate;
    }

    public String getFinalProgressString(boolean useAbbreviations, Context context) {

        float myProgressMl = new SettingsClass().getTodayProgress();
        float inLiters = myProgressMl / 1000;
        String myFinalProgress;

        String metricMililiters = context.getResources().getString(R.string.text_mililiters);
        String metricLiters = context.getResources().getString(R.string.text_liters);

        if(useAbbreviations){
            metricMililiters = context.getResources().getString(R.string.text_vol_unit_mililiters);
            metricLiters = context.getResources().getString(R.string.text_vol_unit_liters);
        }

        if(inLiters > 0.9){
            if(!useAbbreviations) metricLiters = " " + metricLiters;

            if(inLiters % 1 == 0){
                long integer = (long) inLiters;
                myFinalProgress = integer + metricLiters;
            }
            else {
                myFinalProgress = inLiters + metricLiters;
            }
        }
        else {
            if(!useAbbreviations) metricMililiters = " " + metricMililiters;

            if(myProgressMl % 1 == 0){
                long integer = (long) myProgressMl;
                myFinalProgress = integer + metricMililiters;
            }
            else {
                myFinalProgress = myProgressMl + metricMililiters;
            }
        }

        return myFinalProgress;

    }

    public String getGoalString(boolean useAbbreviations, Context context) {

        float myGoalMl = new SettingsClass().getGoalValue();
        float inLiters = myGoalMl / 1000;
        String myFinalGoal;

        String metricMililiters = context.getResources().getString(R.string.text_mililiters);
        String metricLiters = context.getResources().getString(R.string.text_liters);

        if(useAbbreviations){
            metricMililiters = context.getResources().getString(R.string.text_vol_unit_mililiters);
            metricLiters = context.getResources().getString(R.string.text_vol_unit_liters);
        }

        if(inLiters > 0.9){
            if(!useAbbreviations) metricLiters = " " + metricLiters;

            if(inLiters % 1 == 0){
                long integer = (long) inLiters;
                myFinalGoal = integer + metricLiters;
            }
            else {
                myFinalGoal = inLiters + metricLiters;
            }
        }
        else {
            if(!useAbbreviations) metricMililiters = " " + metricMililiters;

            if(myGoalMl % 1 == 0){
                long integer = (long) myGoalMl;
                myFinalGoal = integer + metricMililiters;
            }
            else {
                myFinalGoal = myGoalMl + metricMililiters;
            }
        }

        return myFinalGoal;
    }

    public static boolean isDateToday(long milliSeconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);

        Date getDate = calendar.getTime();

        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date today = calendar.getTime();


/*
        int result = getDate.compareTo(today);
        if (getDate.compareTo(today) > 0) {
            Log.i("TAG_NOTI", "Date1 is after Date2");
        } else if (getDate.compareTo(today) < 0) {
            Log.i("TAG_NOTI", "Date1 is before Date2");
        } else if (getDate.compareTo(today) == 0) {
            Log.i("TAG_NOTI", "Date1 is equal to Date2");
        }

        Log.i("TAG_NOTI", "-- " + milliSeconds + " / Recibido: " + getDate.toString() + " - Hoy: " + today.toString() + " -- result: " + result);
*/
        return getDate.compareTo(today) > 0;

    }
}
