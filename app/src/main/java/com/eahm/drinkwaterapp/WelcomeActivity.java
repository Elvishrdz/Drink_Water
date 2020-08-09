package com.eahm.drinkwaterapp;

import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.eahm.drinkwaterapp.Class.SettingsClass;
import com.eahm.drinkwaterapp.Utils.Utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class WelcomeActivity extends AppCompatActivity {

    //region CREDITS
    //<div>Icons made by <a href="https://www.freepik.com/" title="Freepik">Freepik</a> from <a href="https://www.flaticon.com/" 			    title="Flaticon">www.flaticon.com</a> is licensed by <a href="http://creativecommons.org/licenses/by/3.0/" 			    title="Creative Commons BY 3.0" target="_blank">CC 3.0 BY</a></div>
    //<div>Icons made by <a href="https://www.freepik.com/" title="Freepik">Freepik</a> from <a href="https://www.flaticon.com/" 			    title="Flaticon">www.flaticon.com</a> is licensed by <a href="http://creativecommons.org/licenses/by/3.0/" 			    title="Creative Commons BY 3.0" target="_blank">CC 3.0 BY</a></div>

    //TODO calculo de ingesta de agua segun peso
    //Fuente:   https://www.umsystem.edu/totalrewards/wellness/how-to-calculate-how-much-water-you-should-drink/
    //<div>Icons made by <a href="https://www.flaticon.com/authors/pixel-perfect" title="Pixel perfect">Pixel perfect</a> from <a href="https://www.flaticon.com/" 			    title="Flaticon">www.flaticon.com</a> is licensed by <a href="http://creativecommons.org/licenses/by/3.0/" 			    title="Creative Commons BY 3.0" target="_blank">CC 3.0 BY</a></div>

    //endregion CREDITS

    //region VARIABLES
    private final static String TAG_ANIMATION = "TAG_ANIMATION";
    private final static String TAG_SET_GOAL = "TAG_SET_GOAL";

    RelativeLayout layoutWelcome;
    RelativeLayout layoutGetUserData;
    RelativeLayout layoutAmountResults;
    RelativeLayout layoutSetGoal;
    RelativeLayout layoutSetReminderInterval;
    RelativeLayout layoutSetMorningTime;
    RelativeLayout layoutSetLastTime;
    RelativeLayout layoutNotificationInformation;
    RelativeLayout layoutSetNotificationSkip;
    RelativeLayout layoutSetNotificationDrink;
    RelativeLayout layoutFinished;

    //region LAYOUT WELCOME
    ImageButton buttonWContinue;
    //endregion LAYOUT WELCOME

    //region LAYOUT GET USER DATA
    ImageButton buttonGUDContinue;
    Button buttonGUDSetGoalManually;

    EditText editTextGUDWeight;
    Spinner spinnerGUD;

    //endregion LAYOUT GET USER DATA

    //region LAYOUT AMOUNT RESULTS
    Button buttonARUseCalculatedAmount;
    Button buttonARSetGoalManually;
    TextView textViewAR;
    //endregion LAYOUT AMOUNT RESULTS

    //region LAYOUT SET GOAL
    ImageButton buttonSGContinue;
    ImageButton buttonSGBack;
    EditText editTextSGAmount;
    Spinner spinnerSG;
    //endregion LAYOUT SET GOAL

    //region LAYOUT SET REMINDER INTERVAL
    ImageButton buttonSRIContinue;
    ImageButton buttonSRIBack;
    TimePicker timePickerSRI;
    //endregion LAYOUT SET REMINDER INTERVAL

    //region LAYOUT SET MORNING TIME
    ImageButton buttonSMTContinue;
    ImageButton buttonSMTBack;
    TimePicker timePickerSMT;
    //endregion LAYOUT SET MORNING TIME

    //region LAYOUT SET LAST TIME
    ImageButton buttonSLTContinue;
    ImageButton buttonSLTBack;
    TimePicker timePickerSLT;
    //endregion LAYOUT SET LAST TIME

    //region LAYOUT NOTIFICATION INFORMATION
    ImageButton buttonNIContinue;
    ImageButton buttonNIBack;
    //endregion LAYOUT NOTIFICATION INFORMATION

    //region LAYOUT SET NOTIFICATION SKIP
    ImageButton buttonSNSContinue;
    ImageButton buttonSNSBack;
    Spinner spinnerSNS;
    //endregion LAYOUT SET NOTIFICATION SKIP

    //region LAYOUT SET NOTIFICATION DRINK
    ImageButton buttonSNDContinue;
    ImageButton buttonSNDBack;
    Spinner spinnerSND;
    //endregion LAYOUT SET NOTIFICATION DRINK

    // region LAYOUT FINISHED
    TextView textViewWelcomeMyConfig;
    Button buttonFStart;
    //endregion LAYOUT FINISHED

    Utils utils = new Utils();
    SettingsClass settings = new SettingsClass();

    boolean closeApp = false;
    boolean ignoreFirstSGMVC = false;
    double mililiters = 0;

    //endregion VARIABLES

    @Override
    public void onBackPressed() {
        if(layoutWelcome.getVisibility() == View.VISIBLE && !closeApp){
            closeApp = true;
            Toast.makeText(this, getResources().getString(R.string.exit_press_again), Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(()->{
                closeApp = false;
            }, 2500);
        }
        else if(!closeApp){
            showPrevious();
        }
        else super.onBackPressed();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(settings.isFirstSetupDone()){
            startDrinkWaterAppMain();
        }
        else{
            //clean app data
            settings.deleteGoalData();
            settings.deleteNotiDrinkAmount();
            settings.deleteNotiSkipTime();
            settings.deleteTimeData();
            settings.deleteTodayProgress();
            settings.deleteCustomDrinkAmount();

            settings.deletePermissionStatus();
            settings.deleteAlarmWorking();
            settings.deleteProtectedStatus();
            settings.deleteAppFirstSetupDone();
            //settings.deletePrivacyPolicyStatus();

            settings.deleteContratulationStatus();
            settings.deleteAppRated();
            settings.deleteAppVersion();

            settings.deleteStringBackground();
            settings.deleteSelectedTextColor();

        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        //region FIND IDS
        layoutWelcome = findViewById(R.id.layoutWelcome);
        layoutGetUserData = findViewById(R.id.layoutGetUserData);
        layoutAmountResults = findViewById(R.id.layoutAmountResults);
        layoutSetGoal = findViewById(R.id.layoutSetGoal);
        layoutSetReminderInterval = findViewById(R.id.layoutSetReminderInterval);
        layoutSetMorningTime = findViewById(R.id.layoutSetMorningTime);
        layoutSetLastTime = findViewById(R.id.layoutSetLastTime);
        layoutNotificationInformation = findViewById(R.id.layoutNotificationInformation);
        layoutSetNotificationSkip = findViewById(R.id.layoutSetNotificationSkip);
        layoutSetNotificationDrink = findViewById(R.id.layoutSetNotificationDrink);
        layoutFinished = findViewById(R.id.layoutFinished);

        //region LAYOUT WELCOME
        buttonWContinue = findViewById(R.id.buttonWContinue);
        //endregion LAYOUT WELCOME

        //region LAYOUT GET USER DATA
        buttonGUDContinue = findViewById(R.id.buttonGUDContinue);
        buttonGUDSetGoalManually = findViewById(R.id.buttonGUDSetGoalManually);
        editTextGUDWeight = findViewById(R.id.editTextGUDWeight);
        spinnerGUD = findViewById(R.id.spinnerGUD);
        //endregion LAYOUT GET USER DATA

        //region LAYOUT AMOUNT RESULTS
        buttonARUseCalculatedAmount = findViewById(R.id.buttonARUseCalculatedAmount);
        buttonARSetGoalManually = findViewById(R.id.buttonARSetGoalManually);
        textViewAR = findViewById(R.id.textViewAR);
        //endregion LAYOUT AMOUNT RESULTS

        //region LAYOUT SET GOAL
        buttonSGContinue = findViewById(R.id.buttonSGContinue);
        buttonSGBack = findViewById(R.id.buttonSGBack);
        editTextSGAmount = findViewById(R.id.editTextSGAmount);
        spinnerSG = findViewById(R.id.spinnerSG);
        //endregion LAYOUT SET GOAL

        //region LAYOUT SET REMINDER INTERVAL
        buttonSRIContinue = findViewById(R.id.buttonSRIContinue);
        buttonSRIBack = findViewById(R.id.buttonSRIBack);
        timePickerSRI = findViewById(R.id.timePickerSRI);
        //endregion LAYOUT SET REMINDER INTERVAL

        //region LAYOUT SET MORNING TIME
        buttonSMTContinue = findViewById(R.id.buttonSMTContinue);
        buttonSMTBack = findViewById(R.id.buttonSMTBack);
        timePickerSMT = findViewById(R.id.timePickerSMT);
        //endregion LAYOUT SET MORNING TIME

        //region LAYOUT SET LAST TIME
        buttonSLTContinue = findViewById(R.id.buttonSLTContinue);
        buttonSLTBack = findViewById(R.id.buttonSLTBack);
        timePickerSLT = findViewById(R.id.timePickerSLT);
        //endregion LAYOUT SET LAST TIME

        //region LAYOUT NOTIFICATION INFORMATION
        buttonNIContinue = findViewById(R.id.buttonNIContinue);
        buttonNIBack = findViewById(R.id.buttonNIBack);
        //endregion LAYOUT NOTIFICATION INFORMATION

        //region LAYOUT SET NOTIFICATION SKIP
        buttonSNSContinue = findViewById(R.id.buttonSNSContinue);
        buttonSNSBack = findViewById(R.id.buttonSNSBack);
        spinnerSNS = findViewById(R.id.spinnerSNS);
        //endregion LAYOUT SET NOTIFICATION SKIP

        //region LAYOUT SET NOTIFICATION DRINK
        buttonSNDContinue = findViewById(R.id.buttonSNDContinue);
        buttonSNDBack = findViewById(R.id.buttonSNDBack);
        spinnerSND = findViewById(R.id.spinnerSND);
        //endregion LAYOUT SET NOTIFICATION DRINK

        // region LAYOUT FINISHED
        textViewWelcomeMyConfig = findViewById(R.id.textViewWelcomeMyConfig);
        buttonFStart = findViewById(R.id.buttonFStart);
        //endregion LAYOUT FINISHED

        //endregion FIND IDS

        //region LISTENERS

        //region LAYOUT WELCOME
        buttonWContinue.setOnClickListener(v -> {
            editTextGUDWeight.clearFocus();
            editTextSGAmount.requestFocus();
            showNext();
        });
        //endregion LAYOUT WELCOME

        //region LAYOUT GET USER DATA
        buttonGUDContinue.setOnClickListener(v -> {

            String temp = editTextGUDWeight.getText().toString();

            if(temp.isEmpty() || temp.equalsIgnoreCase(".")){
                Toast.makeText(this, getString(R.string.text_enter_a_valid_amout), Toast.LENGTH_SHORT).show();
                return;
            }

            Double amount = Double.valueOf(editTextGUDWeight.getText().toString());

            switch (spinnerGUD.getSelectedItemPosition()){
                case 0: // Libras
                    if(amount < 10){
                        Toast.makeText(this, getString(R.string.text_enter_an_amount_greater_lbs), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    break;
                case 1: // Kilogramos convertido a libras
                    amount =   2.2046 * amount;
                    if(amount < 5){ //4.535970244035199
                        Toast.makeText(this, getString(R.string.text_enter_an_amount_greater_kg), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    break;
            }

            Utils.hideKeyboard(this);

            // convertir a onzas. Amount now are in libras.
            double ozOfWaterPerDay = amount * 0.5;

            //Convert onzas to mililitros y litros
            //Using UK oz
            double liters = ozOfWaterPerDay/35.195;
            mililiters = liters * 1000;

            DecimalFormat formatDecimal = new DecimalFormat("#.##");

            String formatLiters = formatDecimal.format(liters);
            String formatMililiters = formatDecimal.format(mililiters);

            temp = formatLiters + " " + getResources().getString(R.string.text_liters) + "\n" + formatMililiters + " " + getResources().getString(R.string.text_mililiters);

            textViewAR.setText(temp);

            editTextGUDWeight.setText("0");
            showNext();
        });

        buttonGUDSetGoalManually.setOnClickListener(v -> {
            editTextGUDWeight.setText("0");
            editTextSGAmount.clearFocus();
            editTextGUDWeight.requestFocus();

            Animation animationIn = AnimationUtils.loadAnimation(this, R.anim.show);
            layoutSetGoal.startAnimation(animationIn);
            layoutSetGoal.setVisibility(View.VISIBLE);

            Animation animationOut = AnimationUtils.loadAnimation(this, R.anim.hide);
            layoutGetUserData.startAnimation(animationOut);
            layoutGetUserData.setVisibility(View.GONE);
        });

        //editTextGUDWeight
        //spinnerGUD
        //endregion LAYOUT GET USER DATA

        //region LAYOUT AMOUNT RESULTS
        buttonARUseCalculatedAmount.setOnClickListener(v -> {

            if(mililiters == 0){
                Toast.makeText(this, getString(R.string.text_something_isnt_correct), Toast.LENGTH_LONG).show();
                return;
            }

            DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.ENGLISH);
            otherSymbols.setDecimalSeparator('.');
            otherSymbols.setGroupingSeparator('.');

            DecimalFormat formatDecimal = new DecimalFormat("#.##",otherSymbols);
            String formatMililiters = formatDecimal.format(mililiters);

            float floatvalue = Float.parseFloat(formatMililiters);

            Log.i(TAG_SET_GOAL, "String: " + formatMililiters + " - Float: " + floatvalue);
            Log.i(TAG_SET_GOAL, "Calculated Amount: formated: " + formatMililiters + " milimetros: " + mililiters);

            settings.setGoalValue(floatvalue);

            Animation animationIn = AnimationUtils.loadAnimation(this, R.anim.show);
            layoutSetReminderInterval.startAnimation(animationIn);
            layoutSetReminderInterval.setVisibility(View.VISIBLE);

            Animation animationOut = AnimationUtils.loadAnimation(this, R.anim.hide);
            layoutAmountResults.startAnimation(animationOut);
            layoutAmountResults.setVisibility(View.GONE);
        });

        buttonARSetGoalManually.setOnClickListener(v -> {
            mililiters = 0;

            Animation animationIn = AnimationUtils.loadAnimation(this, R.anim.show);
            layoutSetGoal.startAnimation(animationIn);
            layoutSetGoal.setVisibility(View.VISIBLE);

            Animation animationOut = AnimationUtils.loadAnimation(this, R.anim.hide);
            layoutAmountResults.startAnimation(animationOut);
            layoutAmountResults.setVisibility(View.GONE);
        });

        //endregion LAYOUT AMOUNT RESULTS

        //region LAYOUT SET GOAL
        buttonSGContinue.setOnClickListener(v -> {
            String amount = editTextSGAmount.getText().toString();
            if(amount.isEmpty() || amount.equalsIgnoreCase(".")){
                Toast.makeText(this, getResources().getString(R.string.text_enter_a_valid_amout), Toast.LENGTH_SHORT).show();
                return;
            }

            float goal = 0;

            try{
                goal = Float.parseFloat(amount);
            }
            catch (Exception ignore){ }

            if(goal <= 0){
                Toast.makeText(this, getResources().getString(R.string.text_enter_a_valid_amout), Toast.LENGTH_SHORT).show();
                return;
            }

            switch (spinnerSG.getSelectedItemPosition()){
                case 0:
                    break;
                case 1: // liters
                    goal = goal * 1000;
                    break;
            }

            settings.setGoalValue(goal);
            showNext();
        });

        buttonSGBack.setOnClickListener(v -> {
            showPrevious();
        });

        spinnerSG.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                settings.setGoalMetricVolume(position);
                setGoalMetricVolumeConverted(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //editTextSGAmount
        //endregion LAYOUT SET GOAL

        //region LAYOUT SET REMINDER INTERVAL
        buttonSRIContinue.setOnClickListener(v -> {
            int hourOfDay = 0, minute = 0;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                hourOfDay = timePickerSRI.getHour();
            }
            else hourOfDay = timePickerSRI.getCurrentHour();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                minute = timePickerSRI.getMinute();
            }
            else minute = timePickerSRI.getCurrentMinute();

            if((hourOfDay == 0 && minute == 0) || (hourOfDay == 0 && minute < 5)){
                Toast.makeText(WelcomeActivity.this, getString(R.string.text_interval_too_short), Toast.LENGTH_SHORT).show();
                return;
            }
            else if(hourOfDay >= 12){
                Toast.makeText(WelcomeActivity.this, getString(R.string.text_interval_too_big), Toast.LENGTH_SHORT).show();
                return;
            }

            int totalSecs = ((hourOfDay * 60) + minute) * 60;

            settings.setIntervalValue(totalSecs);

            showNext();
        });

        buttonSRIBack.setOnClickListener(v -> {
            Animation animationIn = AnimationUtils.loadAnimation(this, R.anim.show);
            layoutGetUserData.startAnimation(animationIn);
            layoutGetUserData.setVisibility(View.VISIBLE);

            Animation animationOut = AnimationUtils.loadAnimation(this, R.anim.hide);
            layoutSetReminderInterval.startAnimation(animationOut);
            layoutSetReminderInterval.setVisibility(View.GONE);
        });

        //timePickerSRI
        //endregion LAYOUT SET REMINDER INTERVAL

        //region LAYOUT SET MORNING TIME
        buttonSMTContinue.setOnClickListener(v -> {
            int hours = 0, minutes = 0;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                hours = timePickerSMT.getHour();
            }
            else hours = timePickerSMT.getCurrentHour();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                minutes = timePickerSMT.getMinute();
            }
            else minutes = timePickerSMT.getCurrentMinute();

            int selectedTimeSecs = ((hours * 60) + minutes) * 60;

            //entre 01:00 a 12:00
            int rangeStartHourStartInSecs = ((1 * 60) + 0) * 60; // 1:00am
            int rangeStartHourEndInSecs = ((12 * 60) + 0) * 60;  // 12:00pm

            if(selectedTimeSecs >= rangeStartHourStartInSecs  && selectedTimeSecs <= rangeStartHourEndInSecs){
                settings.setStartHour(hours);
                settings.setStartMin(minutes);
                showNext();
            }
            else {
                Toast.makeText(this, getString(R.string.text_out_of_range_hour), Toast.LENGTH_LONG).show();
            }

        });

        buttonSMTBack.setOnClickListener(v -> {
            showPrevious();
        });

        //timePickerSMT
        //endregion LAYOUT SET MORNING TIME

        //region LAYOUT SET LAST TIME
        buttonSLTContinue.setOnClickListener(v -> {
            int hours = 0, minutes = 0;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                hours = timePickerSLT.getHour();
            }
            else hours = timePickerSLT.getCurrentHour();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                minutes = timePickerSLT.getMinute();
            }
            else minutes = timePickerSLT.getCurrentMinute();

            int selectedTimeSecs = ((hours * 60) + minutes) * 60;

            //entre las 18:00 a 23:59
            int rangeEndHourStartInSecs= ((18 * 60) + 0) * 60; //6:00pm
            int rangeEndHourEndInSecs = ((23 * 60) + 59) * 60; //11:59pm

            if(selectedTimeSecs >= rangeEndHourStartInSecs  && selectedTimeSecs <= rangeEndHourEndInSecs){
                settings.setEndHour(hours);
                settings.setEndMin(minutes);
                showNext();
            }
            else {
                Toast.makeText(this, getString(R.string.text_out_of_range_hour), Toast.LENGTH_LONG).show();
            }
        });

        buttonSLTBack.setOnClickListener(v -> {
            showPrevious();
        });

        //timePickerSLT
        //endregion LAYOUT SET LAST TIME

        //region LAYOUT NOTIFICATION INFORMATION
        buttonNIContinue.setOnClickListener(v -> {
            showNext();
        });

        buttonNIBack.setOnClickListener(v -> {
            showPrevious();
        });
        //endregion LAYOUT NOTIFICATION INFORMATION

        //region LAYOUT SET NOTIFICATION SKIP
        buttonSNSContinue.setOnClickListener(v -> {
            int position = spinnerSNS.getSelectedItemPosition();
            int skipSeconds = 0;
            switch (position){
                case 0: //5min * 60
                    skipSeconds = 300;
                    break;
                case 1: //10
                    skipSeconds = 600;
                    break;
                case 2: //15
                    skipSeconds = 900;
                    break;
                case 3: //20
                    skipSeconds = 1200;
                    break;
            }

            settings.setNotiSkipTime(skipSeconds);

            showNext();
        });

        buttonSNSBack.setOnClickListener(v -> {
            showPrevious();
        });

        //spinnerSNS
        //endregion LAYOUT SET NOTIFICATION SKIP

        //region LAYOUT SET NOTIFICATION DRINK
        buttonSNDContinue.setOnClickListener(v -> {
            int position = spinnerSND.getSelectedItemPosition();
            float drinkAmount = 0;
            switch (position){
                case 0: //10ml
                    drinkAmount = 10;
                    break;
                case 1: //50
                    drinkAmount = 50;
                    break;
                case 2: //100ml
                    drinkAmount = 100;
                    break;
                case 3: //150
                    drinkAmount = 150;
                    break;
            }

            settings.setNotiDrinkAmount(drinkAmount);


            int seconds = settings.getIntervalValue();
            String reminderTimeString = utils.getTimeString(utils.getHours(seconds * 1000L),
                    utils.getMinutes(seconds * 1000L),
                    0, WelcomeActivity.this);

            String goalMililitersString = "3,500 mililitros";
            float goalValueMililiters = settings.getGoalValue();
            if(goalValueMililiters % 1 == 0){
                long integer = (long) goalValueMililiters;
                goalMililitersString = String.valueOf(integer) + " " + getResources().getString(R.string.text_mililiters);
            }
            else{
                DecimalFormat formatDecimal = new DecimalFormat("#.##");
                goalMililitersString = formatDecimal.format(goalValueMililiters) + " " + getResources().getString(R.string.text_mililiters);
            }

            String goalLitersString = "3.5 litros";
            float goalValueLiters = goalValueMililiters / 1000;
            if(goalValueLiters % 1 == 0){
                long integer = (long) goalValueLiters;
                goalLitersString = String.valueOf(integer) + " " + getResources().getString(R.string.text_liters);
            }
            else{
                DecimalFormat formatDecimal = new DecimalFormat("#.##");
                goalLitersString = formatDecimal.format(goalValueLiters) + " " + getResources().getString(R.string.text_liters);
            }

            String myWelcomeAndStats = getString(R.string.text_welcome_stats_01) + " " + reminderTimeString + " " +
                    getString(R.string.text_welcome_stats_02) + " " + goalLitersString  + " " +
                    getString(R.string.text_welcome_stats_03) + " (" + goalMililitersString + ")";

            textViewWelcomeMyConfig.setText(myWelcomeAndStats);

            showNext();
        });

        buttonSNDBack.setOnClickListener(v -> {
            showPrevious();
        });

        //spinnerSND
        //endregion LAYOUT SET NOTIFICATION DRINK

        // region LAYOUT FINISHED
        buttonFStart.setOnClickListener(v -> {
            startDrinkWaterAppMain();
        });
        //endregion LAYOUT FINISHED

        //endregion LISTENERS

        //region INITIALIZE
        layoutWelcome.setVisibility(View.GONE);
        layoutGetUserData.setVisibility(View.GONE);
        layoutAmountResults.setVisibility(View.GONE);
        layoutSetGoal.setVisibility(View.GONE);
        layoutSetReminderInterval.setVisibility(View.GONE);
        layoutSetMorningTime.setVisibility(View.GONE);
        layoutSetLastTime.setVisibility(View.GONE);
        layoutNotificationInformation.setVisibility(View.GONE);
        layoutSetNotificationSkip.setVisibility(View.GONE);
        layoutSetNotificationDrink.setVisibility(View.GONE);
        layoutFinished.setVisibility(View.GONE);


        //region SET REMINDER INTERVAL SETUP
        timePickerSRI.setIs24HourView(true);

        long sriMillis = settings.getIntervalValue() * 1000L;
        int sriHour = utils.getHours(sriMillis);
        int sriMinute = utils.getMinutes(sriMillis);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            timePickerSRI.setHour(sriHour);
        }
        else timePickerSRI.setCurrentHour(sriHour);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            timePickerSRI.setMinute(sriMinute);
        }
        else timePickerSRI.setCurrentMinute(sriMinute);


        //endregion SET REMINDER INTERVAL SETUP

        //region SET MORNING TIME SETUP
        int smtHour =  settings.getStartHour();
        int smtMinute =  settings.getStartMin();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            timePickerSMT.setHour(smtHour);
        }
        else timePickerSMT.setCurrentHour(smtHour);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            timePickerSMT.setMinute(smtMinute);
        }
        else timePickerSMT.setCurrentMinute(smtMinute);
        //endregion SET MORNING TIME SETUP

        //region SET LAST TIME SETUP

        int sltHour =  settings.getEndHour();
        int sltMinute =  settings.getEndMin();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            timePickerSLT.setHour(sltHour);
        }
        else timePickerSLT.setCurrentHour(sltHour);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            timePickerSLT.setMinute(sltMinute);
        }
        else timePickerSLT.setCurrentMinute(sltMinute);

        //endregion SET LAST TIME SETUP

        //region SET USER DATA
        float mililiterGoal = settings.getGoalValue();
        String sgAmount = String.valueOf(mililiterGoal);
        if(mililiterGoal % 1 == 0){
            long integer = (long) mililiterGoal;
            sgAmount = String.valueOf(integer);
        }

        editTextSGAmount.setText(sgAmount);
        spinnerSG.setSelection(1);
        //endregion SET USER DATA

        showNext();
        //endregion INITIALIZE

    }

    private void setGoalMetricVolumeConverted(int position){
        if(ignoreFirstSGMVC){
            ignoreFirstSGMVC = true;
            return;
        }
        //float mililiters = settings.getGoalValue();
        String value = editTextSGAmount.getText().toString();
        if (value.isEmpty()){
            return;
        }

        float mililiters = Float.parseFloat(value);

        Log.i(TAG_SET_GOAL, "Mililitros del edittext: " + mililiters);

        switch (position){
            case 0:
               //mililitros = litros * 1000;
                mililiters = mililiters * 1000;
                break;
            case 1:
                //litros = mili/1000
                mililiters = (mililiters/1000);
                break;
        }

        String amount = String.valueOf(mililiters);
        Log.i(TAG_SET_GOAL, "Texto despues del switch " + position + ": " + mililiters);

        if(mililiters % 1 == 0){
            long integer = (long) mililiters;
            amount = String.valueOf(integer);
        }

        editTextSGAmount.setText(amount);
        Log.i(TAG_SET_GOAL, "set Amount en: " + amount);
    }

    private void startDrinkWaterAppMain(){
        settings.setAppFirstSetupDone();

        Intent intent = new Intent(this, WaterDrinkActivity.class);

        startActivity(intent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAfterTransition();
        }
        else finish();
    }

    private void showNext(){

        if(layoutSetNotificationDrink.getVisibility() == View.VISIBLE){
            Log.i(TAG_ANIMATION, "show condition 1");
            Animation animationIn = AnimationUtils.loadAnimation(this, R.anim.show);
            layoutFinished.startAnimation(animationIn);
            layoutFinished.setVisibility(View.VISIBLE);

            Animation animationOut = AnimationUtils.loadAnimation(this, R.anim.hide);
            layoutSetNotificationDrink.startAnimation(animationOut);
            layoutSetNotificationDrink.setVisibility(View.GONE);
        }
        else if(layoutSetNotificationSkip.getVisibility() == View.VISIBLE){
            Log.i(TAG_ANIMATION, "show condition 2");
            Animation animationIn = AnimationUtils.loadAnimation(this, R.anim.show);
            layoutSetNotificationDrink.startAnimation(animationIn);
            layoutSetNotificationDrink.setVisibility(View.VISIBLE);

            Animation animationOut = AnimationUtils.loadAnimation(this, R.anim.hide);
            layoutSetNotificationSkip.startAnimation(animationOut);
            layoutSetNotificationSkip.setVisibility(View.GONE);
        }
        else if(layoutNotificationInformation.getVisibility() == View.VISIBLE){
            Log.i(TAG_ANIMATION, "show condition 3");
            Animation animationIn = AnimationUtils.loadAnimation(this, R.anim.show);
            layoutSetNotificationSkip.startAnimation(animationIn);
            layoutSetNotificationSkip.setVisibility(View.VISIBLE);

            Animation animationOut = AnimationUtils.loadAnimation(this, R.anim.hide);
            layoutNotificationInformation.startAnimation(animationOut);
            layoutNotificationInformation.setVisibility(View.GONE);
        }
        else if(layoutSetLastTime.getVisibility() == View.VISIBLE){
            Log.i(TAG_ANIMATION, "show condition 4");
            Animation animationIn = AnimationUtils.loadAnimation(this, R.anim.show);
            layoutNotificationInformation.startAnimation(animationIn);
            layoutNotificationInformation.setVisibility(View.VISIBLE);

            Animation animationOut = AnimationUtils.loadAnimation(this, R.anim.hide);
            layoutSetLastTime.startAnimation(animationOut);
            layoutSetLastTime.setVisibility(View.GONE);
        }
        else if(layoutSetMorningTime.getVisibility() == View.VISIBLE){
            Log.i(TAG_ANIMATION, "show condition 5");
            Animation animationIn = AnimationUtils.loadAnimation(this, R.anim.show);
            layoutSetLastTime.startAnimation(animationIn);
            layoutSetLastTime.setVisibility(View.VISIBLE);

            Animation animationOut = AnimationUtils.loadAnimation(this, R.anim.hide);
            layoutSetMorningTime.startAnimation(animationOut);
            layoutSetMorningTime.setVisibility(View.GONE);
        }
        else if(layoutSetReminderInterval.getVisibility() == View.VISIBLE){
            Log.i(TAG_ANIMATION, "show condition 6");
            Animation animationIn = AnimationUtils.loadAnimation(this, R.anim.show);
            layoutSetMorningTime.startAnimation(animationIn);
            layoutSetMorningTime.setVisibility(View.VISIBLE);

            Animation animationOut = AnimationUtils.loadAnimation(this, R.anim.hide);
            layoutSetReminderInterval.startAnimation(animationOut);
            layoutSetReminderInterval.setVisibility(View.GONE);
        }
        else if(layoutSetGoal.getVisibility() == View.VISIBLE){
            Log.i(TAG_ANIMATION, "show condition 7");
            Animation animationIn = AnimationUtils.loadAnimation(this, R.anim.show);
            layoutSetReminderInterval.startAnimation(animationIn);
            layoutSetReminderInterval.setVisibility(View.VISIBLE);

            Animation animationOut = AnimationUtils.loadAnimation(this, R.anim.hide);
            layoutSetGoal.startAnimation(animationOut);
            layoutSetGoal.setVisibility(View.GONE);
        }
        else if(layoutAmountResults.getVisibility() == View.VISIBLE){
            Log.i(TAG_ANIMATION, "show condition 8");
            Animation animationIn = AnimationUtils.loadAnimation(this, R.anim.show);
            layoutSetGoal.startAnimation(animationIn);
            layoutSetGoal.setVisibility(View.VISIBLE);

            Animation animationOut = AnimationUtils.loadAnimation(this, R.anim.hide);
            layoutAmountResults.startAnimation(animationOut);
            layoutAmountResults.setVisibility(View.GONE);
        }
        else if(layoutGetUserData.getVisibility() == View.VISIBLE){
            Log.i(TAG_ANIMATION, "show condition 9");
            Animation animationIn = AnimationUtils.loadAnimation(this, R.anim.show);
            layoutAmountResults.startAnimation(animationIn);
            layoutAmountResults.setVisibility(View.VISIBLE);

            Animation animationOut = AnimationUtils.loadAnimation(this, R.anim.hide);
            layoutGetUserData.startAnimation(animationOut);
            layoutGetUserData.setVisibility(View.GONE);
        }
        else if(layoutWelcome.getVisibility() == View.VISIBLE){
            Log.i(TAG_ANIMATION, "show condition 10");
            Animation animationIn = AnimationUtils.loadAnimation(this, R.anim.show);
            layoutGetUserData.startAnimation(animationIn);
            layoutGetUserData.setVisibility(View.VISIBLE);

            Animation animationOut = AnimationUtils.loadAnimation(this, R.anim.hide);
            layoutWelcome.startAnimation(animationOut);
            layoutWelcome.setVisibility(View.GONE);
        }
        else if(layoutFinished.getVisibility() != View.VISIBLE) {
            Log.i(TAG_ANIMATION, "show condition Default");
            //default
            Animation animationIn = AnimationUtils.loadAnimation(this, R.anim.show);
            layoutWelcome.startAnimation(animationIn);
            layoutWelcome.setVisibility(View.VISIBLE);
        }

    }

    private void showPrevious(){
        if(layoutFinished.getVisibility() == View.VISIBLE){
            Log.i(TAG_ANIMATION, "prev condition 1");
            Animation animationIn = AnimationUtils.loadAnimation(this, R.anim.show);
            layoutSetNotificationDrink.startAnimation(animationIn);
            layoutSetNotificationDrink.setVisibility(View.VISIBLE);

            Animation animationOut = AnimationUtils.loadAnimation(this, R.anim.hide);
            layoutFinished.startAnimation(animationOut);
            layoutFinished.setVisibility(View.GONE);
        }
        else if(layoutSetNotificationDrink.getVisibility() == View.VISIBLE){
            Log.i(TAG_ANIMATION, "prev condition 2");
            Animation animationIn = AnimationUtils.loadAnimation(this, R.anim.show);
            layoutSetNotificationSkip.startAnimation(animationIn);
            layoutSetNotificationSkip.setVisibility(View.VISIBLE);

            Animation animationOut = AnimationUtils.loadAnimation(this, R.anim.hide);
            layoutSetNotificationDrink.startAnimation(animationOut);
            layoutSetNotificationDrink.setVisibility(View.GONE);
        }
        else if(layoutSetNotificationSkip.getVisibility() == View.VISIBLE){
            Log.i(TAG_ANIMATION, "prev condition 2");
            Animation animationIn = AnimationUtils.loadAnimation(this, R.anim.show);
            layoutNotificationInformation.startAnimation(animationIn);
            layoutNotificationInformation.setVisibility(View.VISIBLE);

            Animation animationOut = AnimationUtils.loadAnimation(this, R.anim.hide);
            layoutSetNotificationSkip.startAnimation(animationOut);
            layoutSetNotificationSkip.setVisibility(View.GONE);
        }
        else if(layoutNotificationInformation.getVisibility() == View.VISIBLE){
            Log.i(TAG_ANIMATION, "prev condition 2");
            Animation animationIn = AnimationUtils.loadAnimation(this, R.anim.show);
            layoutSetLastTime.startAnimation(animationIn);
            layoutSetLastTime.setVisibility(View.VISIBLE);

            Animation animationOut = AnimationUtils.loadAnimation(this, R.anim.hide);
            layoutNotificationInformation.startAnimation(animationOut);
            layoutNotificationInformation.setVisibility(View.GONE);
        }
        else if(layoutSetLastTime.getVisibility() == View.VISIBLE){
            Log.i(TAG_ANIMATION, "prev condition 2");
            Animation animationIn = AnimationUtils.loadAnimation(this, R.anim.show);
            layoutSetMorningTime.startAnimation(animationIn);
            layoutSetMorningTime.setVisibility(View.VISIBLE);

            Animation animationOut = AnimationUtils.loadAnimation(this, R.anim.hide);
            layoutSetLastTime.startAnimation(animationOut);
            layoutSetLastTime.setVisibility(View.GONE);
        }
        else if(layoutSetMorningTime.getVisibility() == View.VISIBLE){
            Log.i(TAG_ANIMATION, "prev condition 2");
            Animation animationIn = AnimationUtils.loadAnimation(this, R.anim.show);
            layoutSetReminderInterval.startAnimation(animationIn);
            layoutSetReminderInterval.setVisibility(View.VISIBLE);

            Animation animationOut = AnimationUtils.loadAnimation(this, R.anim.hide);
            layoutSetMorningTime.startAnimation(animationOut);
            layoutSetMorningTime.setVisibility(View.GONE);
        }
        else if(layoutSetReminderInterval.getVisibility() == View.VISIBLE){
            Log.i(TAG_ANIMATION, "prev condition 2");
            Animation animationIn = AnimationUtils.loadAnimation(this, R.anim.show);
            layoutGetUserData.startAnimation(animationIn);
            layoutGetUserData.setVisibility(View.VISIBLE);

            Animation animationOut = AnimationUtils.loadAnimation(this, R.anim.hide);
            layoutSetReminderInterval.startAnimation(animationOut);
            layoutSetReminderInterval.setVisibility(View.GONE);
        }
        else if(layoutSetGoal.getVisibility() == View.VISIBLE){
            Log.i(TAG_ANIMATION, "prev condition 2");
            Animation animationIn = AnimationUtils.loadAnimation(this, R.anim.show);
            layoutGetUserData.startAnimation(animationIn);
            layoutGetUserData.setVisibility(View.VISIBLE);

            Animation animationOut = AnimationUtils.loadAnimation(this, R.anim.hide);
            layoutSetGoal.startAnimation(animationOut);
            layoutSetGoal.setVisibility(View.GONE);
        }
        else if(layoutAmountResults.getVisibility() == View.VISIBLE){
            Log.i(TAG_ANIMATION, "prev condition 2");
            Animation animationIn = AnimationUtils.loadAnimation(this, R.anim.show);
            layoutGetUserData.startAnimation(animationIn);
            layoutGetUserData.setVisibility(View.VISIBLE);

            Animation animationOut = AnimationUtils.loadAnimation(this, R.anim.hide);
            layoutAmountResults.startAnimation(animationOut);
            layoutAmountResults.setVisibility(View.GONE);
        }
        else if(layoutGetUserData.getVisibility() == View.VISIBLE){
            Log.i(TAG_ANIMATION, "prev condition 3");
            Animation animationIn = AnimationUtils.loadAnimation(this, R.anim.show);
            layoutWelcome.startAnimation(animationIn);
            layoutWelcome.setVisibility(View.VISIBLE);

            Animation animationOut = AnimationUtils.loadAnimation(this, R.anim.hide);
            layoutGetUserData.startAnimation(animationOut);
            layoutGetUserData.setVisibility(View.GONE);
        }

    }

}
