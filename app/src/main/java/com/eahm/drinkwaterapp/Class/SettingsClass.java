package com.eahm.drinkwaterapp.Class;

import android.util.Log;

import com.pixplicity.easyprefs.library.Prefs;

public class SettingsClass {

    //region VARIABLES
    private static String START_PREV_HOUR = "START_HOUR_PREV";
    private static String START_PREV_MIN = "START_MIN_PREV";

    private static String START_CURR_HOUR = "START_HOUR";
    private static String START_CURR_MIN = "START_MIN";

    private static String END_PREV_HOUR = "END_HOUR_PREV";
    private static String END_PREV_MIN = "END_MIN_PREV";

    private static String END_CURR_HOUR = "END_HOUR";
    private static String END_CURR_MIN = "END_MIN";

    private static String GOAL_VALUE = "MY_GOAL_VALUE";
    private static String GOAL_METRIC_VOLUME = "MY_GOAL_METRIC_VOLUME";
    private static String INTERVAL_VALUE = "MY_INTERVAL_VALUE";

    private static String DRINK_NEXT_TIME = "MY_DRINK_NEXT_TIME";
    private static String DRINK_PROGRESS_AMOUNT = "MY_DRINK_PROGRESS_AMOUNT";

    private static String NOTI_SKIP_TIME = "MY_NOTI_SKIP_TIME";
    private static String NOTI_DRINK_AMOUNT = "MY_NOTI_DRINK_AMOUNT";

    //endregion VARIABLES

    //region START HOUR

    public int getStartPrevHour() {
        int _default = 6;
        if(!Prefs.contains(START_PREV_HOUR)){
            setStartPrevHour(_default);
        }

        return Prefs.getInt(START_PREV_HOUR, _default);
    }

    public int getStartPrevMin() {
        int _default = 5;
        if(!Prefs.contains(START_PREV_MIN)){
            setStartPrevMin(_default);
        }

        return Prefs.getInt(START_PREV_MIN, _default);
    }

    public int getStartHour(){
        int _default = 6;
        if(!Prefs.contains(START_CURR_HOUR)){
            setStartHour(_default);
        }

        return Prefs.getInt(START_CURR_HOUR, _default);
    }

    public int getStartMin() {
        int _default = 5;
        if(!Prefs.contains(START_CURR_MIN)){
            setStartMin(_default);
        }

        return Prefs.getInt(START_CURR_MIN, _default);
    }

    public void setStartPrevHour(int currentHour){
        Prefs.putInt(START_PREV_HOUR, currentHour);
    }

    public void setStartPrevMin(int currentMin){
        Prefs.putInt(START_PREV_MIN, currentMin);
    }

    public void setStartHour(int hour){
       Prefs.putInt(START_CURR_HOUR, hour);
    }

    public void setStartMin(int minute){
        Prefs.putInt(START_CURR_MIN, minute);
    }
    //endregion START HOUR

    //region END HOUR

    public int getEndPrevHour() {
        int _default = 23;
        if(!Prefs.contains(END_PREV_HOUR)){
            setEndPrevHour(_default);
        }

        return Prefs.getInt(END_PREV_HOUR, _default);
    }

    public int getEndPrevMin() {
        int _default = 45;
        if(!Prefs.contains(END_PREV_MIN)){
            setEndPrevMin(_default);
        }

        return Prefs.getInt(END_PREV_MIN, _default);
    }

    public int getEndHour() {
        int _default = 23;
        if(!Prefs.contains(END_CURR_HOUR)){
            setEndHour(_default);
        }

        return Prefs.getInt(END_CURR_HOUR, _default);
    }

    public int getEndMin() {
        int _default = 55;
        if(!Prefs.contains(END_CURR_MIN)){
            setEndMin(_default);
        }

        return Prefs.getInt(END_CURR_MIN, _default);
    }

    public void setEndPrevHour(int currentHour) {
        Prefs.putInt(END_PREV_HOUR, currentHour);
    }

    public void setEndPrevMin(int currentMin) {
        Prefs.putInt(END_PREV_MIN, currentMin);
    }

    public void setEndHour(int hour) {
        Prefs.putInt(END_CURR_HOUR, hour);
    }

    public void setEndMin(int minute) {
        Prefs.putInt(END_CURR_MIN, minute);
    }
    //endregion END HOUR

    public void deleteTimeData(){
        Prefs.remove(START_PREV_HOUR);
        Prefs.remove(START_PREV_MIN);
        Prefs.remove(START_CURR_HOUR);
        Prefs.remove(START_CURR_MIN);

        Prefs.remove(END_PREV_HOUR);
        Prefs.remove(END_PREV_MIN);
        Prefs.remove(END_CURR_HOUR);
        Prefs.remove(END_CURR_MIN);

        Log.i("LOG_SETTING_HOURS", "Preferencias (Hora inicio-fin) borradas...");
    }

    //region GOAL AMOUNT
    public float getGoalValue() {
        float _default = 500; // Saved in Mililiters
        if(!Prefs.contains(GOAL_VALUE)){
            setGoalValue(_default);
        }

        return Prefs.getFloat(GOAL_VALUE, _default);
    }

    public int getGoalMetricVolume() {
        //Metric units of volume
        int _default = 0; // 0 = Militros 1 = Litros
        if(!Prefs.contains(GOAL_METRIC_VOLUME)){
            setGoalMetricVolume(_default);
        }

        return Prefs.getInt(GOAL_METRIC_VOLUME, _default);
    }

    public void setGoalValue(float mililiters){ //mililitros
        Prefs.putFloat(GOAL_VALUE, mililiters);
    }

    public void setGoalMetricVolume(int id){
        Prefs.putInt(GOAL_METRIC_VOLUME, id);
    }

    //endregion GOAL AMOUNT

    //region USER INTERVAL
    public int getIntervalValue() {
        int _default = 2700; // Saved in Seconds
        if(!Prefs.contains(INTERVAL_VALUE)){
            setIntervalValue(_default);
        }

        return Prefs.getInt(INTERVAL_VALUE, _default);
    }

    public void setIntervalValue(int seconds){
        Prefs.putInt(INTERVAL_VALUE, seconds);
    }

    //endregion USER INTERVAL

    public void deleteGoalData(){
        Prefs.remove(GOAL_VALUE);
        Prefs.remove(GOAL_METRIC_VOLUME);
        Prefs.remove(INTERVAL_VALUE);
    }


    public String getDrinkNextTime() {
        String _default = "";
        if(!Prefs.contains(DRINK_NEXT_TIME)){
            setDrinkNextTime(_default);
        }

        return Prefs.getString(DRINK_NEXT_TIME, _default);
    }

    public void setDrinkNextTime(String timestamp){
        Prefs.putString(DRINK_NEXT_TIME, timestamp);
    }

    public float getTodayProgress() {
        float _default = 0; // Value in mililiters
        if(!Prefs.contains(DRINK_PROGRESS_AMOUNT)){
            setTodayProgress(_default);
        }
        return Prefs.getFloat(DRINK_PROGRESS_AMOUNT, _default);
    }

    public void setTodayProgress(float currentProgress) {
        Prefs.putFloat(DRINK_PROGRESS_AMOUNT, currentProgress); // Value in mililiters
    }

    public void deleteTodayProgress(){
        Prefs.remove(DRINK_PROGRESS_AMOUNT);
    }

    //region NOTIFICATION SETTINGS

    public int getNotiSkipTime() {
        int _default = 300; // 5MIN 300SEGUNDOS
        if(!Prefs.contains(NOTI_SKIP_TIME)){
            setNotiSkipTime(_default);
        }
        return Prefs.getInt(NOTI_SKIP_TIME, _default);
    }

    public void setNotiSkipTime(int nextTimeInSeconds) {
        Prefs.putInt(NOTI_SKIP_TIME, nextTimeInSeconds);
    }

    public void deleteNotiSkipTime(){
        Prefs.remove(NOTI_SKIP_TIME);
    }



    public float getNotiDrinkAmount() {
        float _default = 100; // In mililiters
        if(!Prefs.contains(NOTI_DRINK_AMOUNT)){
            setNotiDrinkAmount(_default);
        }
        return Prefs.getFloat(NOTI_DRINK_AMOUNT, _default);
    }

    public void setNotiDrinkAmount(float mililiters) {
        Prefs.putFloat(NOTI_DRINK_AMOUNT, mililiters);
    }

    public void deleteNotiDrinkAmount(){
        Prefs.remove(NOTI_DRINK_AMOUNT);
    }

    public boolean isGoalReached() {
        return getTodayProgress() >= getGoalValue();
    }

    //endregion NOTIFICATION SETTINGS

    private static String APP_ALARM_STATUS = "APP_ALARM_STATUS";

    public boolean isAlarmWorking() {
        boolean _default = true;
        if(!Prefs.contains(APP_ALARM_STATUS)){
            setAlarmWorking(_default);
        }
        return Prefs.getBoolean(APP_ALARM_STATUS, _default);
    }

    public void setAlarmWorking(boolean status){
        Prefs.putBoolean(APP_ALARM_STATUS, status);
    }

    public void deleteAlarmWorking(){
        Prefs.remove(APP_ALARM_STATUS);
    }


    private static String APP_FIRST_SETUP = "APP_FIRST_SETUP";

    public boolean isFirstSetupDone() {
        boolean _default = false;
        if(!Prefs.contains(APP_FIRST_SETUP)){
            Prefs.putBoolean(APP_FIRST_SETUP, false);
        }
        return Prefs.getBoolean(APP_FIRST_SETUP, _default);
    }

    public void setAppFirstSetupDone(){
        Prefs.putBoolean(APP_FIRST_SETUP, true);
    }

    public void deleteAppFirstSetupDone(){
        Prefs.remove(APP_FIRST_SETUP);
    }


    private static String PHONE_PROTECTED_APP = "PHONE_PROTECTED_APP";

    public int getProtectedStatus() {
        int _default = 0; // 0 = no añadido a la lista de apps protegidas
        if(!Prefs.contains(PHONE_PROTECTED_APP)){
            setProtectedStatus(_default);
        }
        return Prefs.getInt(PHONE_PROTECTED_APP, _default);
    }

    public void setProtectedStatus(int  status){
        Prefs.putInt(PHONE_PROTECTED_APP, status);
    }

    public void deleteProtectedStatus(){
        Prefs.remove(PHONE_PROTECTED_APP);
    }





    private static String APP_PRIVACY_POLICY = "APP_PRIVACY_POLICY";

    public boolean getPrivacyPolicyStatus() {
        boolean _default = false;
        if(!Prefs.contains(APP_PRIVACY_POLICY)){
            setPrivacyPolicyStatus(_default);
        }
        return Prefs.getBoolean(APP_PRIVACY_POLICY, _default);
    }

    public void setPrivacyPolicyStatus(boolean  status){
        Prefs.putBoolean(APP_PRIVACY_POLICY, status);
    }

    public void deletePrivacyPolicyStatus(){
        Prefs.remove(APP_PRIVACY_POLICY);
    }




    private static String APP_PERMISSION_HELPER = "APP_PERMISSION_HELPER";

    public boolean getPermissionStatus() {
        boolean _default = false;
        if(!Prefs.contains(APP_PERMISSION_HELPER)){
            setPermissionStatus(_default);
        }
        return Prefs.getBoolean(APP_PERMISSION_HELPER, _default);
    }

    public void setPermissionStatus(boolean  status){
        Prefs.putBoolean(APP_PERMISSION_HELPER, status);
    }

    public void deletePermissionStatus(){
        Prefs.remove(APP_PERMISSION_HELPER);
    }



    private static String APP_CONGRATULATIONS_STATUS = "APP_CONGRATULATIONS_STATUS";

    public boolean getContratulationStatus() {
        boolean _default = false;
        if(!Prefs.contains(APP_CONGRATULATIONS_STATUS)){
            setContratulationStatus(_default);
        }
        return Prefs.getBoolean(APP_CONGRATULATIONS_STATUS, _default);
    }

    public void setContratulationStatus(boolean  status){
        Prefs.putBoolean(APP_CONGRATULATIONS_STATUS, status);
    }

    public void deleteContratulationStatus(){
        Prefs.remove(APP_CONGRATULATIONS_STATUS);
    }


    private static String APP_RATE = "APP_RATE";

    public int getAppRated() {
        int _default = 0;
        if(!Prefs.contains(APP_RATE)){
            setAppRated(_default);
        }
        return Prefs.getInt(APP_RATE, _default);
    }

    public void setAppRated(int status){
        Prefs.putInt(APP_RATE, status);
    }

    public void deleteAppRated(){
        Prefs.remove(APP_RATE);
    }


    private static String APP_VERSION = "APP_VERSION";

    public String getAppVersion() {
        if(!Prefs.contains(APP_VERSION)){
            setAppVersion("");
        }
        return Prefs.getString(APP_VERSION, "");
    }

    public void setAppVersion(String version){
        Prefs.putString(APP_VERSION, version);
    }

    public void deleteAppVersion(){
        Prefs.remove(APP_VERSION);
    }



    //GET COUNTRY BY URL
    private static String WS_GET_USER_COUNTRY = "WEB_SERVICE_GET_COUNTRY";

    public String getWSUserCountry() {
        if(!Prefs.contains(WS_GET_USER_COUNTRY)){
           setWSUserCountry("");
        }
        return Prefs.getString(WS_GET_USER_COUNTRY, "");
    }

    public void setWSUserCountry(String countryCode){
        Prefs.putString(WS_GET_USER_COUNTRY, countryCode);
    }

    public void deleteWSUserCountry(){
        Prefs.remove(WS_GET_USER_COUNTRY);
    }

    private static String WS_GET_COUNTRY_LAST_TIMESTAMP = "WEB_SERVICE_GET_COUNTRY_TIMESTAMP";

    public String getWSCountryLastTimestamp() {
        if(!Prefs.contains(WS_GET_COUNTRY_LAST_TIMESTAMP)){
            setWSCountryLastTimestamp("");
        }
        return Prefs.getString(WS_GET_COUNTRY_LAST_TIMESTAMP, "");
    }

    public void setWSCountryLastTimestamp(String timestamp){
        Prefs.putString(WS_GET_COUNTRY_LAST_TIMESTAMP, timestamp);
    }

    public void deleteWSCountryLastTimestamp(){
        Prefs.remove(WS_GET_COUNTRY_LAST_TIMESTAMP);
    }



    private static String CUSTOM_DRINK_AMOUNT = "CUSTOM_DRINK_AMOUNT";

    public float getCustomDrinkAmount() {
        float _default = 0f; // Saved in Mililiters
        if(!Prefs.contains(CUSTOM_DRINK_AMOUNT)){
            setCustomDrinkAmount(_default);
        }

        return Prefs.getFloat(CUSTOM_DRINK_AMOUNT, _default);
    }

    public void setCustomDrinkAmount(float customAmount){
        Prefs.putFloat(CUSTOM_DRINK_AMOUNT, customAmount);
    }

    public void deleteCustomDrinkAmount(){
        Prefs.remove(CUSTOM_DRINK_AMOUNT);
    }





    private static String CUSTOM_BACKGROUND = "CUSTOM_BACKGROUND";

    public String getStringBackground() {
        String _default = "DEFAULT";
        if(!Prefs.contains(CUSTOM_BACKGROUND)){
            setStringBackground(_default);
        }

        return Prefs.getString(CUSTOM_BACKGROUND, _default);
    }

    public void setStringBackground(String value){
        Prefs.putString(CUSTOM_BACKGROUND, value);
    }

    public void deleteStringBackground(){
        Prefs.remove(CUSTOM_BACKGROUND);
    }


    private static String CUSTOM_TEXT_COLOR = "CUSTOM_TEXT_COLOR";

    public int getSelectedTextColor() {
        int _default = 0;
        if(!Prefs.contains(CUSTOM_TEXT_COLOR)){
            setSelectedTextColor(_default);
        }

        return Prefs.getInt(CUSTOM_TEXT_COLOR, _default);
    }

    public void setSelectedTextColor(int colorCode){
        Prefs.putInt(CUSTOM_TEXT_COLOR, colorCode);
    }

    public void deleteSelectedTextColor(){
        Prefs.remove(CUSTOM_TEXT_COLOR);
    }



}
