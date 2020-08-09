package com.eahm.drinkwaterapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.provider.Settings;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.eahm.drinkwaterapp.Adapters.SelectGlassesAdapter;
import com.eahm.drinkwaterapp.Adapters.WaterAdapter;
import com.eahm.drinkwaterapp.BroadcastReceiver.AndroidActionsReceiver;
import com.eahm.drinkwaterapp.Class.AwesomeTextsClass;
import com.eahm.drinkwaterapp.Class.NotificationClass;
import com.eahm.drinkwaterapp.Class.SettingsClass;
import com.eahm.drinkwaterapp.DB.RecordsContract;
import com.eahm.drinkwaterapp.DB.RecordsDbHelper;
import com.eahm.drinkwaterapp.DB.RecordsContract.RecordsEntry;
import com.eahm.drinkwaterapp.Fragments.NewsFragment;
import com.eahm.drinkwaterapp.Interfaces.Callbacks;
import com.eahm.drinkwaterapp.Models.RecordModel;
import com.eahm.drinkwaterapp.Services.FloatWidgetService;
import com.eahm.drinkwaterapp.Singleton.AlarmApp;
import com.eahm.drinkwaterapp.Singleton.AlarmFirst;
import com.eahm.drinkwaterapp.Singleton.AlarmLast;
import com.eahm.drinkwaterapp.Utils.Constants;
import com.eahm.drinkwaterapp.Utils.RealPathUtil;
import com.eahm.drinkwaterapp.Utils.Utils;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.auth.FirebaseAuth;

import org.joda.time.DateTime;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import pl.bclogic.pulsator4droid.library.PulsatorLayout;

public class WaterDrinkActivity extends AppCompatActivity implements Callbacks {

    //region CREDITS
    /*
    Credits:
    <div>Icons made by <a href="https://www.flaticon.com/authors/smashicons" title="Smashicons">Smashicons</a> from <a href="https://www.flaticon.com/" 			    title="Flaticon">www.flaticon.com</a> is licensed by <a href="http://creativecommons.org/licenses/by/3.0/" 			    title="Creative Commons BY 3.0" target="_blank">CC 3.0 BY</a></div>
    <div>Icons made by <a href="https://www.freepik.com/" title="Freepik">Freepik</a> from <a href="https://www.flaticon.com/" 			    title="Flaticon">www.flaticon.com</a> is licensed by <a href="http://creativecommons.org/licenses/by/3.0/" 			    title="Creative Commons BY 3.0" target="_blank">CC 3.0 BY</a></div>
    <div>Icons made by <a href="https://www.flaticon.com/authors/vectors-market" title="Vectors Market">Vectors Market</a> from <a href="https://www.flaticon.com/" 			    title="Flaticon">www.flaticon.com</a> is licensed by <a href="http://creativecommons.org/licenses/by/3.0/" 			    title="Creative Commons BY 3.0" target="_blank">CC 3.0 BY</a></div>
    <div>Icons made by <a href="https://www.flaticon.com/authors/popcorns-arts" title="Icon Pond">Icon Pond</a> from <a href="https://www.flaticon.com/" 			    title="Flaticon">www.flaticon.com</a> is licensed by <a href="http://creativecommons.org/licenses/by/3.0/" 			    title="Creative Commons BY 3.0" target="_blank">CC 3.0 BY</a></div>
    <div>Icons made by <a href="https://www.flaticon.com/authors/darius-dan" title="Darius Dan">Darius Dan</a> from <a href="https://www.flaticon.com/" 			    title="Flaticon">www.flaticon.com</a> is licensed by <a href="http://creativecommons.org/licenses/by/3.0/" 			    title="Creative Commons BY 3.0" target="_blank">CC 3.0 BY</a></div>
    <div>Icons made by <a href="https://www.flaticon.com/authors/darius-dan" title="Darius Dan">Darius Dan</a> from <a href="https://www.flaticon.com/" 			    title="Flaticon">www.flaticon.com</a> is licensed by <a href="http://creativecommons.org/licenses/by/3.0/" 			    title="Creative Commons BY 3.0" target="_blank">CC 3.0 BY</a></div>
    <div>Icons made by <a href="https://www.freepik.com/" title="Freepik">Freepik</a> from <a href="https://www.flaticon.com/" 			    title="Flaticon">www.flaticon.com</a> is licensed by <a href="http://creativecommons.org/licenses/by/3.0/" 			    title="Creative Commons BY 3.0" target="_blank">CC 3.0 BY</a></div>
    <div>Icons made by <a href="https://www.freepik.com/?__hstc=57440181.bdd6304c040eeae6a353f69e3b28a5ee.1557337823332.1557553785535.1558075767886.6&__hssc=57440181.7.1558075767886&__hsfp=3487993566" title="Freepik">Freepik</a> from <a href="https://www.flaticon.com/" 			    title="Flaticon">www.flaticon.com</a> is licensed by <a href="http://creativecommons.org/licenses/by/3.0/" 			    title="Creative Commons BY 3.0" target="_blank">CC 3.0 BY</a></div>
    <div>Icons made by <a href="https://www.freepik.com/" title="Freepik">Freepik</a> from <a href="https://www.flaticon.com/" 			    title="Flaticon">www.flaticon.com</a> is licensed by <a href="http://creativecommons.org/licenses/by/3.0/" 			    title="Creative Commons BY 3.0" target="_blank">CC 3.0 BY</a></div>

     */
    //endregion CREDITS

    //region VARIABLES
    private final static String TAG_WATER_DRINK_ACT = "TAG_WATER_DRINK_ACT";
    private final static String TAG_BUTTON_DRINK = "TAG_BUTTON_DRINK";
    private final static String TAG_SETTING_HOURS = "TAG_SETTING_HOURS";
    private final static String TAG_SETTING_GOAL = "TAG_SETTING_GOAL";
    private final static String TAG_RESTORE_CHRONO = "TAG_RESTORE_CHRONO";
    private static final String TAG_RECORD = "TAG_RECORD";
    private static final String TAG_NEWS = "TAG_NEWS";
    private static final String TAG_RATE = "TAG_RATE";
    private static final String TAG_ADMOB = "TAG_ADMOB";
    private static final String TAG_WALLPAPER = "TAG_WALLPAPER";
    private static final String TAG_FRAGMENT_NEWS = "NEWS_FRAGMENT";

    private static final String CODE_SET_ALARM = "SET_ALARM";

    private static final int APP_PERMISSION_REQUEST = 102;
    private static final int APP_PICK_IMAGE = 105;

    RelativeLayout container;
    ImageView imageViewBackground;
    LinearLayout layerColor;
    TextView textViewProgress, textViewStats, textViewNextDrink, textViewTips;
    CircleImageView circleImageViewProfilePic;
    PulsatorLayout pulsatorLayout;
    Button buttonDrink;
    Button buttonAlarmStatus;
    AdView adView;

    ImageButton imageButtonRecords;
    ImageButton imageButtonSetNotification;
    ImageButton imageButtonSetDrinks;
    ImageButton imageButtonSetClocks;
    ImageButton imageButtonShare;
    ImageButton imageButtonSetBackground;
    ImageButton imageButtonCredits;

    RelativeLayout layoutFragmentNews;

    //region RATE
    LinearLayout layoutRate;
    ImageButton imageButtonRateClose;
    Button buttonRate;
    //endregion RATE

    //region RECORDS
    CardView includeLayoutRecords;
    ImageButton buttonRecordClose;
    TextView textViewRecordTitle;
    RecyclerView recyclerRecords;
    ProgressBar progressBarRecord;
    TextView textViewRecordMessage;
    Button buttonSwitchDate;

    RecyclerView.LayoutManager mLayourManager;
    WaterAdapter adapterSwitch;
    //endregion RECORDS

    //region SELECT GLASS
    LinearLayout includeLayoutSelectGlasses;
    GridView gridViewSG;
    EditText editTextCustomAmount;
    Button buttonDrinkCustomAmount;

    Button buttonSGCancel;

    SelectGlassesAdapter selectGlassesAdapter;
    //endregion SELECT GLASS

    //region NOTIFICATION SETTINGS
    CardView includeLayoutNotification;
    Spinner spinnerNotiSkipTimes;
    Spinner spinnerNotiDrinkAmount;
    //endregion NOTIFICATION SETTINGS

    //region SET GOAL
    CardView includeLayoutSetGoal;
    CardView includeLayoutSetReminder;

    TextView textViewDayMax;
    EditText editTextDayMax;
    Spinner spinnerDayMax;

    TextView textViewReminder;
    TimePicker timePickerInterval;
    Button buttonTimePickerInterval;

    boolean convertNotSave = false;

    //endregion SET GOAL

    //region LAYOUT SELECT TIME
    CardView includeCardViewSetTime;

    TextView textViewMyStart;
    ImageButton buttonSetStart;

    TextView textViewMyEnd;
    ImageButton buttonSetEnd;

    //endregion LAYOUT SELECT TIME

    //region LAYOUT SET BACKGROUND
    LinearLayout includeLayoutSetBackground;
    RadioGroup radioGroupTextColorOptions;
    ImageView option01;
    ImageView option02;
    ImageButton option03;
    Button buttonSBCancel;
    //endregion LAYOUT SET BACKGROUND

    //region LAYOUT CREDITS
    LinearLayout includeLayoutCredits;
    ImageButton buttonCreditsClose;
    TextView textViewCredits;
    WebView webView;
    //endregion LAYOUT CREDITS

    Utils utils = new Utils();
    NotificationClass app = new NotificationClass();
    SettingsClass settings = new SettingsClass();
    AwesomeTextsClass awesomeTexts = new AwesomeTextsClass();

    CountDownTimerDrink countDownTimer;

    RecordsDbHelper dbHelper;
    SQLiteDatabase dbWrite;
    SQLiteDatabase dbRead;

    boolean startedFromNotification = false;
    boolean closeApp = false;
    boolean isDrinkTimeFromNotification = false;
    boolean openGalleryHelper = false; // Bandera para evitar abrir la galeria multiples veces.

    //endregion VARIABLES

    @Override
    public void onBackPressed() {

        if (layoutFragmentNews.getVisibility() == View.VISIBLE     ) {
            //Fragment fragment = getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT_NEWS);
            //fragment instanceof NewsFragment
            layoutFragmentNews.setVisibility(View.GONE);
            super.onBackPressed();
            requestExtraSettings();
        }
        else if(includeLayoutCredits.getVisibility() == View.VISIBLE){
            includeLayoutCredits.setVisibility(View.GONE);
        }
        else if(includeLayoutSetBackground.getVisibility() == View.VISIBLE){
            includeLayoutSetBackground.setVisibility(View.GONE);
        }
        else if(includeLayoutRecords.getVisibility() == View.VISIBLE){
            includeLayoutRecords.setVisibility(View.GONE);
        }
        else if(includeLayoutSelectGlasses.getVisibility() == View.VISIBLE){
            includeLayoutSelectGlasses.setVisibility(View.GONE);
        }
        else if(includeLayoutNotification.getVisibility() == View.VISIBLE){
            includeLayoutNotification.setVisibility(View.GONE);
        }
        else if(includeLayoutSetGoal.getVisibility() == View.VISIBLE){
            includeLayoutSetGoal.setVisibility(View.GONE);
            includeLayoutSetReminder.setVisibility(View.GONE);
        }
        else if(includeCardViewSetTime.getVisibility() == View.VISIBLE){
            includeCardViewSetTime.setVisibility(View.GONE);
        }
        else if(!settings.isAlarmWorking()){
            utils.displayAlertDialogYesNo(this, this, CODE_SET_ALARM,
                    getString(R.string.dialog_text_reactivate_alarm),
                    getString(R.string.dialog_exit), getString(R.string.dialog_extend_hours), true);
        }
        else if(!closeApp && settings.isAlarmWorking()){
            closeApp = true;
            Toast.makeText(this, getResources().getString(R.string.exit_press_again), Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(()-> closeApp = false, 2500);
        }
        else{

            if(countDownTimer == null && settings.isAlarmWorking()){
                // Si es hora de tomar agua (alarma nula) y el usuario abandona la app, vamos a poner una alarma en 5min,
                // cuando se active la alarma (si aun esta la app en horas activas) entonces lanzamos notificacion.
                if(!isDrinkTimeFromNotification){
                    Log.i(TAG_RECORD, "Debes tomar agua pero salista intencionalmente aun asi... registrar 6");
                    recordDrink(0, 6);
                }
            }

            super.onBackPressed();
        }

    }

    @Override
    protected void onNewIntent(Intent intent) {
       String receivedFrom = "";

        if(intent != null && intent.getExtras() != null){
            receivedFrom = intent.getExtras().getString("FROM", "");
        }

        switch (receivedFrom){
            case "CLOSED":
                Log.i(TAG_WATER_DRINK_ACT, "onNewIntent: CLOSED recibido");
                break;
            case "SKIP":
                Log.i(TAG_WATER_DRINK_ACT, "onNewIntent: SKIP recibido");
                break;
            case "DRINK":
                Log.i(TAG_WATER_DRINK_ACT, "onNewIntent: DRINK recibido");
                break;
            case "MORNING":
                Log.i(TAG_WATER_DRINK_ACT, "onNewIntent: MORNING recibido");
                setAppStyle();
                break;
            case "LAST":
                Log.i(TAG_WATER_DRINK_ACT, "onNewIntent: LAST recibido");
                isDrinkTimeFromNotification = false;
                //Ajustar app a modo fin de dia!
                setAppStyle();
                if(countDownTimer != null) countDownTimer.onFinish();
                String text = getString(R.string.text_drinking_over) + textViewNextDrink.getText();
                textViewNextDrink.setText(text);
                showReportFromLastNotification(utils.getCurrentTimestamp()); // Si recibimos esto es porque en este momento seguimos en el mismo dia
                break;
            case Intent.ACTION_SCREEN_OFF:
                Log.i(TAG_WATER_DRINK_ACT, "Pantalla apagada");

                //inicializar en onCreate si deseas usar este listener
                /* INITIALIZE RECEIVER
                IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
                filter.addAction(Intent.ACTION_SCREEN_OFF);
                BroadcastReceiver mReceiver = new AndroidActionsReceiver();
                registerReceiver(mReceiver, filter);
                */
                break;
        }

        Log.i(TAG_WATER_DRINK_ACT, "onNewIntent Invocado! " + receivedFrom);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_drink);
        logFabricUser();
        //Prefs.clear();
        //settings.deleteTimeData();

        AlarmApp.getInstance().initialize(getBaseContext());

        if(getIntent() != null && getIntent().getExtras() != null){
            startedFromNotification = getIntent().getExtras().getBoolean("IS_FROM_NOTIFICATION");
            if(startedFromNotification && getIntent().getExtras().getBoolean("IS_DRINK_TIME")){
                isDrinkTimeFromNotification = true;
            }
        }

        //region FIND VIEWS
        container = findViewById(R.id.container);
        imageViewBackground = findViewById(R.id.imageViewBackground);
        layerColor = findViewById(R.id.layerColor);
        textViewProgress = findViewById(R.id.textViewProgress);
        textViewStats = findViewById(R.id.textViewStats);
        textViewNextDrink = findViewById(R.id.textViewNextDrink);
        textViewTips = findViewById(R.id.textViewTips);
        circleImageViewProfilePic = findViewById(R.id.circleImageViewProfilePic);
        pulsatorLayout = findViewById(R.id.pulsatorLayout);
        buttonDrink = findViewById(R.id.buttonDrink);
        buttonAlarmStatus = findViewById(R.id.buttonAlarmStatus);
        imageButtonRecords = findViewById(R.id.imageButtonRecords);
        imageButtonSetNotification = findViewById(R.id.imageButtonSetNotification);
        imageButtonSetDrinks = findViewById(R.id.imageButtonSetDrinks);
        imageButtonSetClocks = findViewById(R.id.imageButtonSetClocks);
        imageButtonShare = findViewById(R.id.imageButtonShare);
        imageButtonSetBackground = findViewById(R.id.imageButtonSetBackground);
        imageButtonCredits = findViewById(R.id.imageButtonCredits);

        layoutFragmentNews = findViewById(R.id.layoutFragmentNews);

        //region RATE
        layoutRate = findViewById(R.id.layoutRate);
        imageButtonRateClose = findViewById(R.id.imageButtonRateClose);
        buttonRate = findViewById(R.id.buttonRate);
        //endregion RATE

        //region RECORDS
        includeLayoutRecords = findViewById(R.id.includeLayoutRecords);
        buttonRecordClose = findViewById(R.id.buttonRecordClose);
        textViewRecordTitle = findViewById(R.id.textViewRecordTitle);
        recyclerRecords = findViewById(R.id.recyclerRecords);
        progressBarRecord = findViewById(R.id.progressBarRecord);
        textViewRecordMessage = findViewById(R.id.textViewRecordMessage);
        buttonSwitchDate = findViewById(R.id.buttonSwitchDate);
        //endregion RECORDS

        //region SELECT GLASS
        includeLayoutSelectGlasses = findViewById(R.id.includeLayoutSelectGlasses);
        gridViewSG = findViewById(R.id.gridViewSG);
        editTextCustomAmount = findViewById(R.id.editTextCustomAmount);
        buttonDrinkCustomAmount = findViewById(R.id.buttonDrinkCustomAmount);
        buttonSGCancel = findViewById(R.id.buttonSGCancel);
        //endregion SELECT GLASS

        //region NOTIFICATION SETTINGS
        includeLayoutNotification = findViewById(R.id.includeLayoutNotification);
        spinnerNotiSkipTimes = findViewById(R.id.spinnerNotiSkipTimes);
        spinnerNotiDrinkAmount = findViewById(R.id.spinnerNotiDrinkAmount);

        //endregion NOTIFICATION SETTINGS

        //region SET GOAL
        includeLayoutSetGoal = findViewById(R.id.includeLayoutSetGoal);
        includeLayoutSetReminder = findViewById(R.id.includeLayoutSetReminder);

        textViewDayMax = findViewById(R.id.textViewDayMax);
        textViewReminder = findViewById(R.id.textViewReminder);
        editTextDayMax = findViewById(R.id.editTextDayMax);
        spinnerDayMax = findViewById(R.id.spinnerDayMax);

        textViewReminder = findViewById(R.id.textViewReminder);
        timePickerInterval = findViewById(R.id.timePickerInterval);
        buttonTimePickerInterval = findViewById(R.id.buttonTimePickerInterval);

        //endregion SET GOAL

        //region LAYOUT SELECT TIME
        includeCardViewSetTime = findViewById(R.id.includeSetTime);

        textViewMyStart = findViewById(R.id.textViewMyStart);
        buttonSetStart = findViewById(R.id.buttonSetStart);
        textViewMyEnd = findViewById(R.id.textViewMyEnd);
        buttonSetEnd = findViewById(R.id.buttonSetEnd);

        //endregion LAYOUT SELECT TIME

        //region LAYOUT SET BACKGROUND
        includeLayoutSetBackground = findViewById(R.id.includeLayoutSetBackground);
        radioGroupTextColorOptions = findViewById(R.id.radioGroupTextColorOptions);
        option01 = findViewById(R.id.option_01);
        option02 = findViewById(R.id.option_02);
        option03 = findViewById(R.id.option_03);
        buttonSBCancel = findViewById(R.id.buttonSBCancel);
        //endregion LAYOUT SET BACKGROUND

        //region LAYOUT CREDITS
        includeLayoutCredits = findViewById(R.id.includeLayoutCredits);
        buttonCreditsClose = findViewById(R.id.buttonCreditsClose);
        textViewCredits = findViewById(R.id.textViewCredits);
        webView = findViewById(R.id.webView);
        //endregion LAYOUT CREDITS

        adView = findViewById(R.id.adView);

        //endregion FIND VIEWS

        //region ADMOB
        initializeAdmob();
        //endregion ADMOB

        //region LISTENERS

        //region RATE
        imageButtonRateClose.setOnClickListener(v -> {
            layoutRate.setVisibility(View.GONE);
            settings.setAppRated(0);
        });

        buttonRate.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse( "https://play.google.com/store/apps/details?id=" + getPackageName()));
            intent.setPackage("com.android.vending");
            startActivity(intent);
            settings.setAppRated(8);
            if(layoutRate.getVisibility() == View.VISIBLE) layoutRate.setVisibility(View.GONE);
        });
        //endregion RATE

        //region BUTTONS
        imageButtonRecords.setOnClickListener(v -> {
            //Toast.makeText(this, "Preparar reporte eh!", Toast.LENGTH_SHORT).show();
            if(includeLayoutRecords.getVisibility() == View.VISIBLE){
                includeLayoutRecords.setVisibility(View.GONE);
            }
            else{
                includeLayoutRecords.setVisibility(View.VISIBLE);
                ViewCompat.setElevation(includeLayoutRecords, Utils.convertDpToPx(7, WaterDrinkActivity.this));
                buttonSwitchDate.setText(getResources().getString(R.string.text_record_yesterday));
                initializeReport(0);
            }
            //Crashlytics.log("Mostrando reporte!");
            //Crashlytics.setString("FABRIC_REPORT", "mostrado");

            //throw new RuntimeException("This is a crash, hi FABRIC!");

        });

        imageButtonSetNotification.setOnClickListener(v -> {
            if(includeLayoutNotification.getVisibility() == View.VISIBLE){
                includeLayoutNotification.setVisibility(View.GONE);
            }
            else{
                includeLayoutNotification.setVisibility(View.VISIBLE);
            }
        });

        imageButtonSetDrinks.setOnClickListener(v -> {
            if(includeLayoutSetGoal.getVisibility() == View.VISIBLE){
                includeLayoutSetGoal.setVisibility(View.GONE);
                includeLayoutSetReminder.setVisibility(View.GONE);
            }
            else{
                includeLayoutSetGoal.setVisibility(View.VISIBLE);
                includeLayoutSetReminder.setVisibility(View.VISIBLE);
            }
        });

        imageButtonSetClocks.setOnClickListener(v -> {
            if(includeCardViewSetTime.getVisibility() == View.VISIBLE){
                includeCardViewSetTime.setVisibility(View.GONE);
            }
            else{
                Log.i(TAG_SETTING_HOURS, settings.getStartHour() + ":" + settings.getStartMin());
                Log.i(TAG_SETTING_HOURS, settings.getEndHour() + ":" + settings.getEndMin());

                setSettingsValues(settings.getStartHour(), settings.getStartMin(), true);
                setSettingsValues(settings.getEndHour(), settings.getEndMin(), false);

                includeCardViewSetTime.setVisibility(View.VISIBLE);
            }
        });

        imageButtonShare.setOnClickListener(v -> {

            String dynamicLink = "https://drinkwaterapp.page.link/isR1"; // En Ingles Default

            String countryLanguague = ((ThisApplication) getApplication()).getLanguagueCountry();

            if(countryLanguague.equalsIgnoreCase("Español")){
                dynamicLink = "https://drinkwaterapp.page.link/Tn65"; // En español
            }
            else if(countryLanguague.equalsIgnoreCase("English")){
                dynamicLink = "https://drinkwaterapp.page.link/isR1"; // En Ingles
            }
            else if(countryLanguague.equalsIgnoreCase("Deutsch")){
                dynamicLink = "https://drinkwaterapp.page.link/b7pQ"; // En Alemán
            }

            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.text_share_content) + "\n\n" + dynamicLink);
            sendIntent.putExtra(Intent.EXTRA_SUBJECT, R.string.text_share_subject);
            sendIntent.setType("text/plain");
            startActivity(Intent.createChooser(sendIntent,
                    getResources().getText(R.string.text_share_title)));
        });

        imageButtonSetBackground.setOnClickListener(v -> {
            if(includeLayoutSetBackground.getVisibility() == View.VISIBLE){
                includeLayoutSetBackground.setVisibility(View.GONE);
            }
            else {
                if(settings.isAlarmWorking()){
                    option01.setImageResource(R.color.white);
                }
                else {
                    option01.setImageResource(R.color.black);
                }

                includeLayoutSetBackground.setVisibility(View.VISIBLE);
            }
        });

        imageButtonCredits.setOnClickListener(v -> {
            if(includeLayoutCredits.getVisibility() == View.VISIBLE){
                includeLayoutCredits.setVisibility(View.GONE);
            }
            else {
                includeLayoutCredits.setVisibility(View.VISIBLE);
            }
        });

        //endregion BUTTONS

        //region RECORDS
        buttonRecordClose.setOnClickListener(v -> {
            includeLayoutRecords.setVisibility(View.GONE);
        });

        buttonSwitchDate.setOnClickListener(v -> {
            if(buttonSwitchDate.getText().equals(getResources().getString(R.string.text_record_yesterday))){
                ViewCompat.setElevation(includeLayoutRecords, Utils.convertDpToPx(7, WaterDrinkActivity.this));
                buttonSwitchDate.setText(getResources().getString(R.string.text_record_today));
                initializeReport(1);
            }
            else {
                ViewCompat.setElevation(includeLayoutRecords, Utils.convertDpToPx(7, WaterDrinkActivity.this));
                buttonSwitchDate.setText(getResources().getString(R.string.text_record_yesterday));
                initializeReport(0);
            }


        });
        //endregion RECORDS

        //region NOTIFICATION SETTINGS
        spinnerNotiSkipTimes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
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
                if(skipSeconds == 0) return;
                settings.setNotiSkipTime(skipSeconds);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerNotiDrinkAmount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
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
                if(drinkAmount == 0) return;
                settings.setNotiDrinkAmount(drinkAmount);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //endregion NOTIFICATION SETTINGS

        //region SET GOAL
        editTextDayMax.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Log.i(TAG_SETTING_GOAL, "before text changed " + s.toString());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Log.i(TAG_SETTING_GOAL, "on text changed " + s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                //Log.i(TAG_SETTING_GOAL, "after text changed " + s.toString());
                String newAmount = s.toString();
                float newGoal = 0;

                if(!newAmount.isEmpty()){
                    try{
                        newGoal = Float.parseFloat(newAmount);
                    }
                    catch (Exception ignore){ }
                }

                if(!convertNotSave && !newAmount.isEmpty() && !newAmount.equalsIgnoreCase("0.0") && newGoal > 0){

                    String goal = "";

                    switch (spinnerDayMax.getSelectedItemPosition()){
                        case 0: // mililiters
                            goal = getResources().getString(R.string.text_mililiters);
                            break;
                        case 1: // liters 1-1000
                            goal = getResources().getString(R.string.text_liters);;
                            float convertion = Float.parseFloat(newAmount) * 1000;
                            newAmount = String.valueOf(convertion); // Convert to mililiters
                            break;
                    }
                    Log.i(TAG_SETTING_GOAL, "Saving goal: " + s.toString());
                    settings.setGoalValue(Float.valueOf(newAmount));

                    if(Float.parseFloat(s.toString()) == 1) goal = goal.substring(0, goal.length()-1);
                    goal = getString(R.string.text_your_goal_01) + " " + s.toString() + " " + goal + " " + getString(R.string.text_your_goal_02);
                    textViewStats.setText(goal);
                    updateProgress();
                    setAppStyle();
                }
                else if(convertNotSave) convertNotSave = false;

                if(s.toString().isEmpty() || s.toString().equalsIgnoreCase("0") || newGoal <= 0 ){
                    switch (spinnerDayMax.getSelectedItemPosition()){
                        case 0: // mililiters
                            editTextDayMax.setText("1000");
                            break;
                        case 1: // liters 1-1000
                            editTextDayMax.setText("1");
                            break;
                    }

                }

            }
        });

        spinnerDayMax.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i(TAG_SETTING_GOAL, "on item selected " + position);

                settings.setGoalMetricVolume(position);
                setGoalMetricVolumeConverted(position);
                updateProgress();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.i(TAG_SETTING_GOAL, "on nothing selected");
            }
        });

        /*
        timePickerInterval.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                Log.i(TAG_SETTING_GOAL, "Del selector obtuvimos: " + hourOfDay + ":" + minute);

                if((hourOfDay == 0 && minute == 0) || (hourOfDay == 0 && minute < 5)){
                    Toast.makeText(WaterDrinkActivity.this, "El intervalo es demasiado corto", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(hourOfDay >= 12){
                    Toast.makeText(WaterDrinkActivity.this, "El intervalo es demasiado grande", Toast.LENGTH_SHORT).show();
                    return;
                }

                String textReminder = "Recordar cada:\n" + utils.getTimeString(hourOfDay, minute, 0);

                textViewReminder.setText(textReminder);

                int newTime = utils.getSeconds(hourOfDay, minute);
                Log.i(TAG_SETTING_GOAL, "Interval value SAVED: " + newTime);
                settings.setIntervalValue(newTime);

            }
        });
        */

        buttonTimePickerInterval.setOnClickListener(v -> {

            int allSeconds = settings.getIntervalValue();
            String result = utils.convertSecondTo_HHmmss_String(allSeconds);

            String[] parts = result.split(":");
            int hours = Integer.parseInt( parts[0] );
            int minutes = Integer.parseInt( parts[1] );
            int secs = Integer.parseInt( parts[2] );


            TimePickerDialog.OnTimeSetListener timeSet = (view, hourOfDay, minute) -> {
                Log.i(TAG_SETTING_GOAL, "Del selector obtuvimos: " + hourOfDay + ":" + minute);

                if((hourOfDay == 0 && minute == 0) || (hourOfDay == 0 && minute < 5)){
                    Toast.makeText(WaterDrinkActivity.this, getResources().getString(R.string.text_interval_too_short), Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(hourOfDay >= 12){
                    Toast.makeText(WaterDrinkActivity.this, getResources().getString(R.string.text_interval_too_big), Toast.LENGTH_SHORT).show();
                    return;
                }

                String textReminder =  getString(R.string.text_remember_each) + "\n" + utils.getTimeString(hourOfDay, minute, 0, WaterDrinkActivity.this);

                textViewReminder.setText(textReminder);

                int newTime = utils.getSeconds(hourOfDay, minute);
                Log.i(TAG_SETTING_GOAL, "Interval value SAVED: " + newTime);
                settings.setIntervalValue(newTime);
            };

            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    AlertDialog.THEME_HOLO_LIGHT, timeSet, hours, minutes, true);

            // Set the newly created TextView as a custom tile of DatePickerDialog
            timePickerDialog.setCustomTitle(utils.createCustomTitle(this,
                    getString(R.string.text_timepicker_title),
                    getResources().getColor(R.color.com_facebook_blue),
                    getResources().getColor(R.color.white),
                    20));

            timePickerDialog.show();
        });

        //endregion SET GOAL

        //region LAYOUT SELECT TIME
        buttonSetStart.setOnClickListener(v -> {
            getHourTime(settings.getStartHour(), settings.getStartMin(),true);
        });

        buttonSetEnd.setOnClickListener(v -> {
            getHourTime(settings.getEndHour(),settings.getEndMin(),false);
        });

        //endregion LAYOUT SELECT TIME

        //region SELECT GLASS
        buttonDrinkCustomAmount.setOnClickListener(v -> {

            String stringCA = editTextCustomAmount.getText().toString();

            //region VALIDATING INPUT PT1
            if(stringCA.isEmpty()){
                Toast.makeText(this, getString(R.string.text_amount_empty), Toast.LENGTH_SHORT).show();
                return;
            }

            if(stringCA.equals(".")){
                Toast.makeText(this, getString(R.string.text_enter_a_valid_amout), Toast.LENGTH_SHORT).show();
                return;
            }

            /*// Aqui solo numeros enteros no admite punto decimal
            if(!stringCA.matches("[0-9]+")) {
               //Solo se admiten numeros
                Toast.makeText(this, getString(R.string.text_amount_just_numbers), Toast.LENGTH_SHORT).show();
                return;
            }*/
            //endregion VALIDATING INPUT PT1

            float floatCA = Float.parseFloat(stringCA);

            //region VALIDATING INPUT PT2
            if(floatCA <= 10){
                //El numero debe ser mayor a cero
                Toast.makeText(this, getString(R.string.text_amount_greater_than), Toast.LENGTH_SHORT).show();
                return;
            }
            //endregion VALIDATING INPUT PT2

            Utils.hideKeyboard(this);
            settings.setCustomDrinkAmount(Float.valueOf(editTextCustomAmount.getText().toString()));
            addDrinkAmount(floatCA);

        });

        buttonSGCancel.setOnClickListener(v -> {
            Utils.hideKeyboard(this);
            buttonDrink.callOnClick();
        });
        //endregion SELECT GLASS

        //region LAYOUT SET BACKGROUND
        radioGroupTextColorOptions.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                settings.setSelectedTextColor(checkedId); // Save current selected item
                setTextColor(checkedId);
            }
        });

        option01.setOnClickListener(v -> {
            // Set white view
            settings.setStringBackground("");
            restoreAppBackground();
            imageButtonSetBackground.callOnClick(); // Hide View
        });

        option02.setOnClickListener(v -> {
            // Set default background
            settings.setStringBackground("DEFAULT");
            restoreAppBackground();
            imageButtonSetBackground.callOnClick(); // Hide View
        });

        option03.setOnClickListener(v -> {
            if(!openGalleryHelper){
                openGalleryHelper = true;
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(gallery, APP_PICK_IMAGE);
            }
        });

        buttonSBCancel.setOnClickListener(v -> {
            Utils.hideKeyboard(this);
            imageButtonSetBackground.callOnClick();
        });
        //endregion LAYOUT SET BACKGROUND

        //region LAYOUT CREDITS
        buttonCreditsClose.setOnClickListener(v -> {
            imageButtonCredits.callOnClick();
        });
        //endregion LAYOUT CREDITS

        buttonDrink.setOnClickListener(v -> {
            //Toast.makeText(this, "Bien Hecho! te aviso para tomar agua mas tarde... ", Toast.LENGTH_LONG).show();
            if(includeLayoutSelectGlasses.getVisibility() == View.VISIBLE){
                includeLayoutSelectGlasses.setVisibility(View.GONE);
            }
            else{
                if(editTextCustomAmount.getText().toString().isEmpty()){
                    restoreCustomAmount();
                }

                includeLayoutSelectGlasses.setVisibility(View.VISIBLE);
                ViewCompat.setElevation(includeLayoutSelectGlasses, Utils.convertDpToPx(7, this));
            }
        });

        buttonAlarmStatus.setOnClickListener(v -> {
            /*boolean alarmStatus = !settings.isAlarmWorking();
            settings.setAlarmWorking(alarmStatus);
            if(alarmStatus){
                buttonAlarmStatus.setTextColor(getResources().getColor(R.color.red_indio));
                buttonAlarmStatus.setText("Desactivar Alarma");
            }
            else {
                buttonAlarmStatus.setTextColor(getResources().getColor(R.color.green));
                buttonAlarmStatus.setText("Activar Alarma");
            }*/
        });

        //endregion LISTENERS

        //region RESTORE ALARMS
        Log.i(Constants.LOG_MORNING_ALARM, "Administrar la alarma mañanera...");
        AlarmFirst.getInstance().adminFirstAlarm(getBaseContext());

        Log.i(Constants.LOG_LAST_ALARM, "Administrar ultima alarma del dia...");
        AlarmLast.getInstance().adminLastAlarm(getBaseContext());

        activateAlarmOnRestartPhone(true);
        //endregion RESTORE ALARMS

        //region INITIALIZE
        // Establecer el color de texto seleccionado por el usuario
        int id = settings.getSelectedTextColor();
        id = (id == 0 ? R.id.radioButtonWhite : id);
        setTextColor(id);
        radioGroupTextColorOptions.check(id);

        restoreAppBackground(); // Recuperar el fondo de pantalla seleccionado por el usuario

        textViewTips.setText("");
        buttonAlarmStatus.setVisibility(View.GONE); // En progreso
        setAppStyle();

        //algunos telefonos muestran el selector muy pequeño... usar boton
        timePickerInterval.setVisibility(View.GONE);
        buttonTimePickerInterval.setVisibility(View.VISIBLE);

        // OpenDatabase in another Thread
        openDatabase.execute();

        if(FirebaseAuth.getInstance().getCurrentUser() == null){
            circleImageViewProfilePic.setVisibility(View.GONE);
            //imageButtonRecords.setVisibility(View.GONE);
        }

        restoreCustomAmount();
        restoreNotificationValues();
        restoreGoalValues();

        updateProgress();

        setAppStyle();

        //Cerrar la notificacion en caso de estar activa
        Log.i(TAG_WATER_DRINK_ACT, "onCreate: Cerrar notificacion en caso que exista");
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(WaterDrinkActivity.this);
        notificationManager.cancel(Constants.NOTIFICATION_ID_APP);

        if(startedFromNotification){
            // Entramos a la app presionando la notificacion
            startedFromNotification = false;
            Log.i(TAG_WATER_DRINK_ACT, "Activity iniciada desde la notificacion");
            setPulsatorLayout(true);

            if(getIntent() != null && getIntent().getExtras() != null &&
                    getIntent().getExtras().getBoolean("SHOW_REPORT") ||
              getIntent() != null && getIntent().getExtras() != null &&
                    getIntent().getExtras().getBoolean("IS_LAST_NOTIFICATION")){
                Log.i(TAG_WATER_DRINK_ACT, "Mostramos la notificacion final y entramos a ver el registro");

                isDrinkTimeFromNotification = false;
                String timestamp = getIntent().getExtras().getString("TIMESTAMP");

                showReportFromLastNotification(timestamp);
            }

        }
        else {
            //normal start
            Log.i(TAG_WATER_DRINK_ACT, "Activity onCreate() Entrada normal");
        }

        selectGlassesAdapter = new SelectGlassesAdapter(this);
        gridViewSG.setAdapter(selectGlassesAdapter);
        gridViewSG.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String text = parent.getItemAtPosition(position).toString();
                addDrinkAmount(Float.parseFloat(text));
            }
        });

        //region SET CREDITS
        // displaying content in WebView from html file that stored in assets folder
        //webView.loadUrl("file:///android_asset/credits.html");
        String content = utils.LoadData(WaterDrinkActivity.this, "credits.html");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            textViewCredits.setText(Html.fromHtml(content, Html.FROM_HTML_MODE_LEGACY));
        }
        else textViewCredits.setText(Html.fromHtml(content));

        textViewCredits.setMovementMethod(LinkMovementMethod.getInstance());

        //endregion SET CREDITS

        //endregion INITIALIZE

        //region RESTORE CHRONO

        String timestamp = settings.getDrinkNextTime();

        if(timestamp.isEmpty()){
            Log.i(TAG_RESTORE_CHRONO, "No hay tiempo siguiente guardado.");
            setPulsatorLayout(true);
        }
        else if(timestamp.equalsIgnoreCase("OVER")){
            Log.i(TAG_RESTORE_CHRONO, "No hay tiempo siguiente guardado.");
            setPulsatorLayout(true);
        }
        else {
            Log.i(TAG_RESTORE_CHRONO, "Tiempo siguiente guardado: " + timestamp);

            long timeInMillis = utils.getMillis(timestamp);
            Log.i(TAG_RESTORE_CHRONO, "Timestamp nextDrink en millis: " + timeInMillis);

            long currentTimestamp = System.currentTimeMillis()/1000;
            Log.i(TAG_RESTORE_CHRONO, "Timestamp actual en millis: " + timeInMillis);

            timeInMillis = Long.parseLong(timestamp);

            if(timeInMillis <= currentTimestamp){
                Log.i(TAG_RESTORE_CHRONO, "Beber agua, (ya se paso la hora definida)");
                textViewTips.setText(awesomeTexts.getCoutdownTextDone(WaterDrinkActivity.this));
                setPulsatorLayout(true);
            }
            else {
                Log.i(TAG_RESTORE_CHRONO, "Siguiente bebida en: " + timestamp);

                // update the time in the chronometer hehe... by elvis :v
                int secondsRemaining = (int) (timeInMillis - currentTimestamp);
                Log.i(TAG_RESTORE_CHRONO, "Segundos restantes: " + secondsRemaining);

                timeInMillis = secondsRemaining * 1000; // convert in millis
                Log.i(TAG_RESTORE_CHRONO, "Restantes en millis: " + timeInMillis);

                if(countDownTimer != null){
                    long remainingTimeInMillis = countDownTimer.getTimeInMillis();
                    Log.i(TAG_RESTORE_CHRONO, "Obtener tiempo restante de la anterior alarma. Restante en millis: " + remainingTimeInMillis);

                    int remainingHours = utils.getHours(remainingTimeInMillis);
                    int remainingMinutes = utils.getMinutes(remainingTimeInMillis);
                    int remainingSeconds = utils.getSeconds(remainingTimeInMillis);

                    String stats = getString(R.string.text_stats_01) + " " + utils.getTimeString(remainingHours, remainingMinutes, remainingSeconds, WaterDrinkActivity.this) + " " + getString(R.string.text_stats_02);

                    textViewStats.setText(stats);
                    countDownTimer.cancel();
                }

                Log.i(TAG_RESTORE_CHRONO, "Cronometro con: " + timeInMillis + " millisegundos");
                countDownTimer = new CountDownTimerDrink(timeInMillis,1000, WaterDrinkActivity.this);
                countDownTimer.start();

                buttonDrink.setText(getString(R.string.text_drink_water_now));

            }
        }
        //endregion RESTORE CHRONO

        showNews();

        //region RATE
        layoutRate.setVisibility(View.GONE);

        if(settings.getAppVersion().equalsIgnoreCase(BuildConfig.VERSION_NAME)){
            //Esta version es la actual!
            if(settings.getAppRated() >= 8){
                //Ya calificamos la app, no hay problema!
                // NO HACEMOS NADA!
            }
            else if(settings.getAppRated() >= 7){
                layoutRate.setVisibility(View.VISIBLE);
                Log.i(TAG_RATE, "Mostrando pregunta para calificar la app");
            }
            else{
                settings.setAppRated(settings.getAppRated() + 1);
            }
        }
        else {
            //La version ha cambiado!
            settings.setAppRated(0); // volver a contar para pedir una calificacion a la nueva version.
            settings.setAppVersion(BuildConfig.VERSION_NAME); // Actualizar a la nueva version, asi no volver a pedir calificar
            Log.i(TAG_RATE, "Rating: Nueva version instalada!");
        }
        Log.i(TAG_RATE, settings.getAppVersion() + " - Contador de rated: " + settings.getAppRated());
        //endregion RATE

    }

    private void setTextColor(int checkedId) {
        int color = getResources().getColor(R.color.white);

        switch (checkedId){
            case R.id.radioButtonWhite:
                // WHITE
                color = getResources().getColor(R.color.white);
                break;
            case R.id.radioButtonBlack:
                // BLACK
                color = getResources().getColor(R.color.black);
                break;
            case R.id.radioButtonYellow:
                // YELLOW
                color = getResources().getColor(R.color.yellow);
                break;
            case R.id.radioButtonDarkPurple:
                // DARK PURPLE
                color = getResources().getColor(R.color.dark_purple);
                break;
        }

        textViewStats.setTextColor(color);
        textViewNextDrink.setTextColor(color);
        textViewTips.setTextColor(color);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG_WATER_DRINK_ACT, "onActivityResult: " + requestCode + " " + resultCode);

        if(requestCode == APP_PICK_IMAGE){
            if(resultCode == RESULT_OK && data != null){
                Log.i(TAG_WALLPAPER, "Establecer fondo");
                Uri imageUri = data.getData();
                imageViewBackground.setImageURI(imageUri);

                // Guardar path de la imagen
                String val = RealPathUtil.getUriRealPath(WaterDrinkActivity.this, imageUri);
                settings.setStringBackground(val);

                Log.i(TAG_WALLPAPER, "STRING: " + val);

                imageButtonSetBackground.callOnClick(); // HideView
            }
            else {
                Toast.makeText(this, "Error intentando cambiar el fondo", Toast.LENGTH_LONG).show();
            }

            openGalleryHelper = false; // Flag!
        }
    }

    @SuppressLint("StaticFieldLeak")
    AsyncTask<Void, Void, Boolean> openDatabase = new AsyncTask<Void, Void, Boolean>(){
        @Override
        protected Boolean doInBackground(Void... voids) {
            dbHelper = new RecordsDbHelper(WaterDrinkActivity.this);
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if(aBoolean) Log.i(TAG_RECORD, "base de datos inicializada");
            else  Log.i(TAG_RECORD, "ERROR en la iniciacion de la base de datos ");
        }
    };

    private void restoreAppBackground() {
        // Restaurar el fondo de pantalla seleccionado
        String val = settings.getStringBackground();
        if(val.isEmpty()){
            Log.i(TAG_WALLPAPER, "fondo de pantalla vacio");
            if(settings.isAlarmWorking()){
                imageViewBackground.setBackgroundColor(getResources().getColor(R.color.white));
            }
            else {
                imageViewBackground.setBackgroundColor(getResources().getColor(R.color.black));
            }
        }
        if(val.equalsIgnoreCase("DEFAULT")){
            Log.i(TAG_WALLPAPER, "fondo de pantalla por defecto");
            imageViewBackground.setImageResource(R.drawable.custom_background);
        }
        else {
            try{
                imageViewBackground.setImageBitmap(BitmapFactory.decodeFile(val));
                Log.i(TAG_WALLPAPER, "fondo de pantalla establecido!!");
            }
            catch (Exception e){
                Log.i(TAG_WALLPAPER, "No se restauro la imagen. Exception: " + e.toString());
                Toast.makeText(this, "No se pudo encontrar el fondo de pantalla en tu telefono", Toast.LENGTH_LONG).show();

                settings.setStringBackground("DEFAULT");
                restoreAppBackground();
            }
        }
    }

    private void showReportFromLastNotification(String timestamp) {
        String message = getString(R.string.text_your_record_so_far);

        imageButtonRecords.callOnClick();

        if(timestamp != null && !timestamp.isEmpty() && !Utils.isDateToday(Long.parseLong(timestamp) * 1000L)){
            Log.i(TAG_WATER_DRINK_ACT, "Mostrar registro de ayer");
            message = getString(R.string.text_your_record_yesterday);
            buttonSwitchDate.callOnClick();
        }

        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private void restoreCustomAmount() {
        // Restableciendo valor guardado de la ultima bebida personalizada
        float floatCustomDrink = settings.getCustomDrinkAmount();
        String stringCustomDrink = String.valueOf(floatCustomDrink);
        if (floatCustomDrink % 1 == 0){
            long integer = (long) floatCustomDrink;
            stringCustomDrink = String.valueOf(integer);
        }
        editTextCustomAmount.setText(stringCustomDrink);
    }

    private void initializeAdmob() {
        if (BuildConfig.DEBUG){
            adView.setVisibility(View.GONE);
            return;
        }

        Log.i(TAG_ADMOB, "inicializando");
        AdRequest adRequest = new AdRequest.Builder()
                //.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();

        adView.loadAd(adRequest);
        ViewCompat.setElevation(adView, Utils.convertDpToPx(7, this));
        adView.setAdListener(new AdListener(){
            @Override
            public void onAdClosed() {
                Log.i(TAG_ADMOB, "onAdClosed");
                super.onAdClosed();
            }

            @Override
            public void onAdFailedToLoad(int i) {
                Log.i(TAG_ADMOB, "onAdFailedToLoad");
                super.onAdFailedToLoad(i);
            }

            @Override
            public void onAdLeftApplication() {
                Log.i(TAG_ADMOB, "onAdLeftApplication");
                super.onAdLeftApplication();
            }

            @Override
            public void onAdOpened() {
                Log.i(TAG_ADMOB, "onAdLoaded");
                super.onAdOpened();
            }

            @Override
            public void onAdLoaded() {
                Log.i(TAG_ADMOB, "onAdLoaded");
                super.onAdLoaded();
            }

            @Override
            public void onAdClicked() {
                Log.i(TAG_ADMOB, "onAdClicked");
                super.onAdClicked();
            }

            @Override
            public void onAdImpression() {
                Log.i(TAG_ADMOB, "onAdImpression");
                super.onAdImpression();
            }
        });
    }

    private void addDrinkAmount(float value){
        float goal = settings.getGoalValue();

        float currentProgress = settings.getTodayProgress();
        currentProgress = currentProgress + value;

        settings.setTodayProgress(currentProgress); // progress always in mililiters
        String currentProgressString = String.valueOf(currentProgress);

        if(currentProgress % 1 == 0){
            long integer = (long) currentProgress;
            currentProgressString = String.valueOf(integer);
        }

        String stringValue = String.valueOf(value);
        if (value % 1 == 0){
            long integer = (long) value;
            stringValue = String.valueOf(integer);
        }

        if(currentProgress>= goal && !settings.getContratulationStatus()){
            settings.setContratulationStatus(true);
            textViewTips.setText(getString(R.string.text_goal_reached_01));

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if(Settings.canDrawOverlays(WaterDrinkActivity.this)){
                    startService(new Intent(WaterDrinkActivity.this, FloatWidgetService.class));
                }
                else {
                    //No tenemos activado el permiso de mostrar VIEWS encima de otras apps
                }
            }

            Toast.makeText(WaterDrinkActivity.this, getString(R.string.text_goal_reached_02), Toast.LENGTH_LONG).show();

        }
        else Toast.makeText(WaterDrinkActivity.this, getString(R.string.text_hurra) + " +" + stringValue, Toast.LENGTH_SHORT).show();

        DecimalFormat formatDecimal = new DecimalFormat("#.##");
        String progress = getString(R.string.text_progress_01) + " " + currentProgressString + " " + getResources().getString(R.string.text_mililiters) + ", " + getString(R.string.text_progress_02) + " " + formatDecimal.format(goal) + " " + getResources().getString(R.string.text_mililiters);

        textViewStats.setText(progress);
        setAppStyle();
        updateProgress();
        recordDrink(value, 1);
        isDrinkTimeFromNotification = false;

        buttonDrink();
        buttonDrink.callOnClick();

        //Toast.makeText(WaterDrinkActivity.this, "Seleccionado: " + text, Toast.LENGTH_SHORT).show();
    }

    private void requestExtraSettings(){

        if("huawei".equalsIgnoreCase(Build.MANUFACTURER) && settings.getProtectedStatus() != 2 ) {
            // Solicitar activar notificaciones en telefonos HUAWEI
            AlertDialog.Builder builder  = new AlertDialog.Builder(this);
            builder.setTitle(getString(R.string.dialog_huawei_title)).setMessage(getString(R.string.dialog_huawei_message))
                    .setPositiveButton(getString(R.string.text_proceed), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent();
                            ComponentName cn = new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity");

                            try{
                                intent.setComponent(cn);
                                startActivity(intent);
                            }
                            catch (Exception e){
                               //ignore, component not founded
                            }

                            settings.setProtectedStatus(1);
                        }
                    });

            if(settings.getProtectedStatus() == 1){
                builder.setNegativeButton(getString(R.string.text_its_done), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        settings.setProtectedStatus(2);
                    }
                });
            }

            builder.create().show();
        }
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(WaterDrinkActivity.this)) {
            // Solicitar habilitar a la aplicacion para escribir sobre otras apps
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, APP_PERMISSION_REQUEST);
        }
    }

    private void showNews(){

        if(((ThisApplication) getApplication()).getNewsList() != null &&
                ((ThisApplication) getApplication()).getNewsList().size() > 0){

            Log.i(TAG_NEWS, "Tenemos noticias... mostrar.");

            //Bundle bundle = new Bundle();
            //bundle.putString("key","value");

            NewsFragment newsFragment = new NewsFragment();
            //newsFragment.setArguments(bundle);

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.layoutFragmentNews, newsFragment, TAG_FRAGMENT_NEWS).addToBackStack("");
            fragmentTransaction.commit();

            RelativeLayout layoutFragmentNews = findViewById(R.id.layoutFragmentNews);
            layoutFragmentNews.setVisibility(View.VISIBLE);
            Log.i(TAG_NEWS, "MOSTRAR layout");


            /*
            try{
                FragmentTransaction fm = getSupportFragmentManager().beginTransaction();
                fm.remove(getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT_NEWS));
                fm.commit();
            }
            catch (Exception e){
                //ignore
            }

            */

            //Mostrar noticias

        }
        else{
            Log.i(TAG_NEWS, "No hay noticias :(");
            requestExtraSettings();
        }
    }

    private void initializeReport(int when) {
        Log.i(TAG_RECORD, "Iniciando el reporte: " + when);

        String title = getString(R.string.text_todays_records);
        String stringTodayDate = "";

        if(when == 0){ //TODAY
            DateTime datetime = Utils.getDateTimeByTimestamp(utils.getCurrentTimestamp());

            stringTodayDate = Utils.getDayName(datetime.getDayOfWeek(), WaterDrinkActivity.this) + " " +
                    datetime.getDayOfMonth() + " " + getString(R.string.text_of) + " " +
                    Utils.getMonthName(datetime.getMonthOfYear(), WaterDrinkActivity.this) + " " +
                    datetime.getYear();

            //title = getString(R.string.text_today);
            Log.i(TAG_RECORD, "0 -- " + stringTodayDate);
        }
        else if(when == 1){ //YESTERDAY
            Calendar rightNow = Calendar.getInstance();
            rightNow.add(Calendar.DATE, -1); // Establecer el dia de ayer!

            rightNow.set(Calendar.HOUR_OF_DAY, 0);
            rightNow.set(Calendar.MINUTE, 0);
            rightNow.set(Calendar.SECOND, 0);
            long startOfYesterday = rightNow.getTimeInMillis() / 1000;

            DateTime datetime = Utils.getDateTimeByTimestamp(String.valueOf(startOfYesterday));

            stringTodayDate = Utils.getDayName(datetime.getDayOfWeek(), WaterDrinkActivity.this) + " " +
                    datetime.getDayOfMonth() + " " + getString(R.string.text_of) + " " +
                    Utils.getMonthName(datetime.getMonthOfYear(), WaterDrinkActivity.this) + " " +
                    datetime.getYear();

            title = getString(R.string.text_report_title) + stringTodayDate;
            Log.i(TAG_RECORD, "1 -- " + stringTodayDate);
        }


        textViewRecordTitle.setText(title);

        recyclerRecords.setVisibility(View.GONE);
        progressBarRecord.setVisibility(View.VISIBLE);
        textViewRecordMessage.setText(getString(R.string.text_report_loading));

        if(recyclerRecords == null || adapterSwitch == null){
            mLayourManager = new LinearLayoutManager(this);
            ((LinearLayoutManager) mLayourManager).setOrientation(RecyclerView.VERTICAL);
            recyclerRecords.setLayoutManager(mLayourManager);
            adapterSwitch = new WaterAdapter(this);
            recyclerRecords.setAdapter(adapterSwitch);
        }

        recyclerRecords.setHasFixedSize(true);

        Log.i(TAG_RECORD, "Ejecutar codigo en un hilo aparte");
        new Task().execute(when);
    }
  
    private void setAppStyle() {

        Calendar rightNow = Calendar.getInstance();
        int currentHourIn24Format = rightNow.get(Calendar.HOUR_OF_DAY);
        int currentMinute= rightNow.get(Calendar.MINUTE);

        int currentSeconds = ((currentHourIn24Format * 60) + currentMinute) * 60;
        int startSeconds = ((settings.getStartHour() * 60) + settings.getStartMin()) * 60;
        int endSeconds = ((settings.getEndHour() * 60) + settings.getEndMin()) * 60;

        if ((currentSeconds  >= startSeconds) && (currentSeconds <= endSeconds)){
            Log.i(TAG_WATER_DRINK_ACT, "La alarma esta activa porque estamos en horas de trabajo");

            settings.setAlarmWorking(true);
            buttonAlarmStatus.setTextColor(getResources().getColor(R.color.red_indio));
            buttonAlarmStatus.setText(getResources().getString(R.string.text_disable_alarm));

            if(utils.getRandomNumber(1,10) > 5){
                textViewNextDrink.setText(getString(R.string.text_first_drink_01));
            }
            else textViewNextDrink.setText(getString(R.string.text_first_drink_02));

        }
        else {
            Log.i(TAG_WATER_DRINK_ACT, "La alarma esta fuera de horas laborales");
            settings.setAlarmWorking(false);
            settings.setDrinkNextTime("OVER");

            buttonAlarmStatus.setTextColor(getResources().getColor(R.color.green));
            buttonAlarmStatus.setText(getResources().getString(R.string.text_enable_alarm));

            if(utils.getRandomNumber(1,10) > 5){
                textViewNextDrink.setText(getString(R.string.text_break_hours_01));
            }
            else textViewNextDrink.setText(getString(R.string.text_break_hours_02));

        }

        if(settings.isAlarmWorking()){
            layerColor.setBackgroundColor(getResources().getColor(R.color.white));
            layerColor.setAlpha(0);

            if(settings.isGoalReached()){
                //Estilo de la app cuando hemos alcanzado la meta y la alarma esta activa
                buttonDrink.setTextColor(getResources().getColor(R.color.black));
                buttonDrink.setBackground(getResources().getDrawable(R.drawable.button_background_02_selector));

                pulsatorColor = R.color.yellow;
                pulsatorInterpolator = PulsatorLayout.INTERP_ACCELERATE_DECELERATE;
                pulsatorDuration = 7000;
                pulsatorCount = 3;
            }
            else {
                //Estilo por defecto de la aplicacion
                buttonDrink.setTextColor(getResources().getColor(R.color.white));
                buttonDrink.setBackground(getResources().getDrawable(R.drawable.button_background_01_selector));

                pulsatorColor = R.color.blue_water;
                pulsatorInterpolator = PulsatorLayout.INTERP_ACCELERATE_DECELERATE;
                pulsatorDuration = 5000;
                pulsatorCount = 4;
            }
        }
        else { // alarmNotWorking
            layerColor.setBackgroundColor(getResources().getColor(R.color.black));
            layerColor.setAlpha(0.5f);

            if(settings.isGoalReached()){
                //Estilo de la app cuando hemos alcanzado la meta y la alarma esta NO activa
                buttonDrink.setTextColor(getResources().getColor(R.color.purple));
                buttonDrink.setBackground(getResources().getDrawable(R.drawable.button_background_02_selector));

                pulsatorColor = R.color.white;
                pulsatorInterpolator = PulsatorLayout.INTERP_DECELERATE;
                pulsatorDuration = 8000;
                pulsatorCount = 2;
            }
            else {
                //Estilo de la app cuando no hay alarmas, la hora de finalizar esta activa
                buttonDrink.setTextColor(getResources().getColor(R.color.white));
                buttonDrink.setBackground(getResources().getDrawable(R.drawable.button_background_04_selector));

                pulsatorColor = R.color.purple;
                pulsatorInterpolator = PulsatorLayout.INTERP_DECELERATE;
                pulsatorDuration = 8000;
                pulsatorCount = 2;
            }
        }


        String timestamp = settings.getDrinkNextTime();

        if(timestamp.isEmpty()){
            Log.i(TAG_RESTORE_CHRONO, "No hay tiempo siguiente guardado.");
            setPulsatorLayout(true);

        }
        else if(timestamp.equalsIgnoreCase("OVER")){
            Log.i(TAG_RESTORE_CHRONO, "Horas de descanso");
             setPulsatorLayout(true);
        }
        else {
            setPulsatorLayout(false);
        }

    }

    private void updateProgress(){

        final String simbolMililiters = getString(R.string.text_vol_unit_mililiters);
        final String simbolLiters = getString(R.string.text_vol_unit_liters);

        float todayProgress = settings.getTodayProgress();
        String currentAmount = String.valueOf(todayProgress) + simbolMililiters;
        if(todayProgress % 1 == 0){
            long integer = (long) todayProgress;
            currentAmount = String.valueOf(integer) + simbolMililiters;
        }

        int metricVolume = settings.getGoalMetricVolume();
        float goalValue = settings.getGoalValue();
        String goalAmount = String.valueOf( goalValue );
        switch (metricVolume){
            case 0:
                if(goalValue % 1 == 0){
                    long integer = (long) goalValue;
                    goalAmount = String.valueOf(integer) + simbolMililiters;
                }
                else{
                    DecimalFormat formatDecimal = new DecimalFormat("#.##");
                    goalAmount = formatDecimal.format(goalValue) + simbolMililiters;
                }
                break;
            case 1:
                goalValue = goalValue/1000;
                if(goalValue % 1 == 0){
                    long integer = (long) goalValue;
                    goalAmount = String.valueOf(integer) + simbolLiters;
                }
                else{
                    DecimalFormat formatDecimal = new DecimalFormat("#.##");
                    goalAmount = formatDecimal.format(goalValue) + simbolLiters;
                }
                break;
        }

        String progress =  getString(R.string.text_you_have) + " " + currentAmount + " " + getResources().getString(R.string.text_of)  + " "+ goalAmount;

        textViewProgress.setText(progress);

    }

    private void logFabricUser() {
        if(!BuildConfig.DEBUG){
            // TODO: Use the current user's information
            // You can call any combination of these three methods
            Crashlytics.setUserIdentifier(utils.getDeviceName(WaterDrinkActivity.this));
            Crashlytics.setUserEmail("Aun no disponible");
            Crashlytics.setUserName("Aun no disponible");

            //Custom Keys
            Crashlytics.setString("version", settings.getAppVersion());
            Crashlytics.setInt("rate_value", settings.getAppRated());
            Crashlytics.setFloat("goal", settings.getGoalValue());
            Crashlytics.setFloat("today_progress", settings.getTodayProgress());
            Crashlytics.setString("country", settings.getWSUserCountry());
            Crashlytics.setString("device", utils.getDeviceName(WaterDrinkActivity.this));

            //Logging
            Crashlytics.log("Registro en caso de fallo! hora de inicio en la app: " + utils.getCurrentTimestamp());
        }
    }

    private void buttonDrink(){
        if(countDownTimer != null){
            long remainingTimeInMillis = countDownTimer.getTimeInMillis();
            Log.i(TAG_BUTTON_DRINK, "Obtener tiempo restante de la anterior alarma. Restante en millis: " + remainingTimeInMillis);

            int remainingHours = utils.getHours(remainingTimeInMillis);
            int remainingMinutes = utils.getMinutes(remainingTimeInMillis);
            int remainingSeconds = utils.getSeconds(remainingTimeInMillis);

            String stats = getString(R.string.text_stats_01) + " " + utils.getTimeString(remainingHours, remainingMinutes, remainingSeconds, WaterDrinkActivity.this) + " " + getString(R.string.text_stats_02);

            /*if(remainingHours > 0){ // Mas de 60 Minutos
                stats = "Te has adelantado por " + remainingHours + " hora " + remainingMinutes + " minutos " + remainingSeconds + " segundos a tu tiempo de agua";
            }
            else if(remainingMinutes > 0){ // Mas de 60 segundos
                stats = "Te has adelantado por " + remainingMinutes + " minutos " + remainingSeconds + " segundos a tu tiempo de agua";
            }
            else stats = "Te has adelantado por " + remainingSeconds + " segundos a tu tiempo de agua";
*/
            textViewStats.setText(stats);
            countDownTimer.cancel();
        }

        if(settings.isAlarmWorking()){
            long timeInMillis = utils.getMillis(settings.getIntervalValue());
            long alarmNextTime = SystemClock.elapsedRealtime() + timeInMillis; // cada X minutos

            AlarmApp.getInstance().setNextDrinkingValue();
            AlarmApp.getInstance().updateAlarm(alarmNextTime);

            setPulsatorLayout(false);
            countDownTimer = new CountDownTimerDrink(timeInMillis, 1000, WaterDrinkActivity.this);
            countDownTimer.start();
        }
        else{
            setPulsatorLayout(true);
            textViewNextDrink.setText("");
            textViewTips.setText(awesomeTexts.getCoutdownTextDone(WaterDrinkActivity.this));
        }

        buttonDrink.setText(getString(R.string.text_well_done));
    }

    private void restoreNotificationValues(){
        int position = 0;
        int skipSeconds =  settings.getNotiSkipTime();
        switch (skipSeconds){
            case 300: //5min * 60
                position = 0;
                break;
            case 600: //10
                position = 1;
                break;
            case 900: //15
                position = 2;
                break;
            case 1200: //20
                position = 3;
                break;
        }
        spinnerNotiSkipTimes.setSelection(position);

        position = 0;
        float drinkAmount = settings.getNotiDrinkAmount();
        if (drinkAmount == 10f) {
            position = 0;
        } else if (drinkAmount == 50f) {
            position = 1;
        } else if (drinkAmount == 100f) {
            position = 2;
        } else if (drinkAmount == 150f) {
            position = 3;
        }
        spinnerNotiDrinkAmount.setSelection(position);

    }

    private void restoreGoalValues() {
        //settings.deleteGoalData();

        int selection = settings.getGoalMetricVolume();
        Log.i(TAG_SETTING_GOAL, "Goal Metric: " + selection);

        spinnerDayMax.setSelection(selection);

        setGoalMetricVolumeConverted(selection);

        int allSeconds = settings.getIntervalValue();
        Log.i(TAG_SETTING_GOAL, "Interval value: " + allSeconds);

        String result = utils.convertSecondTo_HHmmss_String(allSeconds);

        String[] parts = result.split(":");
        int hours = Integer.parseInt( parts[0] );
        int minutes = Integer.parseInt( parts[1] );
        int secs = Integer.parseInt( parts[2] );

        // set the value for current hours
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            timePickerInterval.setHour(hours); // from api level 23
            timePickerInterval.setMinute(minutes);
        }
        else {
            timePickerInterval.setCurrentHour(hours); // before api level 23
            timePickerInterval.setCurrentMinute(minutes);
        }

        timePickerInterval.setIs24HourView(true);
        timePickerInterval.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS); // Disable edit, enable just spinner mode

        String textReminder =  getString(R.string.text_remember_each) + "\n" + utils.getTimeString(hours, minutes, 0, WaterDrinkActivity.this);

        textViewReminder.setText(textReminder);

    }

    private void setGoalMetricVolumeConverted(int position){
        float mililiters = settings.getGoalValue();

        Log.i(TAG_SETTING_GOAL,"Goal Value: " + mililiters);

        String goal = "";
        switch (position){
            case 0:
                goal = getResources().getString(R.string.text_mililiters);
                break;
            case 1:
                goal = getResources().getString(R.string.text_liters);
                mililiters = (mililiters/1000);
                break;
        }

        Log.i(TAG_SETTING_GOAL, "Goal Value After Switch: " + mililiters);

        convertNotSave = true;
        String amount = String.valueOf(mililiters);

        if(mililiters % 1 == 0){
            long integer = (long) mililiters;
            amount = String.valueOf(integer);
        }
        else{
            DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.ENGLISH);
            otherSymbols.setDecimalSeparator('.');
            otherSymbols.setGroupingSeparator('.');

            DecimalFormat formatDecimal = new DecimalFormat("#.##", otherSymbols);
            amount = formatDecimal.format(mililiters);
        }

        editTextDayMax.setText(amount);

        goal = getString(R.string.text_your_goal_01) + " " + amount + " " + goal + " " + getString(R.string.text_your_goal_02);
        textViewStats.setText(goal);
    }

    private void getHourTime(int defaultHour, int defaultMinute, boolean isStartTime){

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view, hourOfDay, minute) -> {
            Log.i(TAG_SETTING_HOURS, "Del selector obtuvimos: " + hourOfDay + ":" + minute + " es hora inicial? " + isStartTime);

            int selectedTimeSecs = ((hourOfDay * 60) + minute) * 60;

            if(isStartTime){
                //entre 01:00 a 12:00
                int rangeStartHourStartInSecs = ((1 * 60) + 0) * 60;
                int rangeStartHourEndInSecs = ((12 * 60) + 0) * 60;

                if(selectedTimeSecs >= rangeStartHourStartInSecs  && selectedTimeSecs <= rangeStartHourEndInSecs){
                    settings.setStartHour(hourOfDay);
                    settings.setStartMin(minute);
                    setSettingsValues(hourOfDay, minute, true);
                    Log.i(TAG_SETTING_HOURS, "Horas iniciales establecidas");
                }
                else {
                    Log.i(TAG_SETTING_HOURS, "Hora inicial no esta dentro del rango valido");
                    Toast.makeText(this, getResources().getString(R.string.text_out_of_range_hour), Toast.LENGTH_LONG).show();
                }
            }
            else{
                //entre las 18:00 a 23:59
                int rangeEndHourStartInSecs= ((18 * 60) + 0) * 60;
                int rangeEndHourEndInSecs = ((23 * 60) + 59) * 60;

                if(selectedTimeSecs >= rangeEndHourStartInSecs  && selectedTimeSecs <= rangeEndHourEndInSecs){
                    settings.setEndHour(hourOfDay);
                    settings.setEndMin(minute);
                    setAppStyle();
                    setSettingsValues(hourOfDay, minute, false);
                    Log.i(TAG_SETTING_HOURS, "Hora final establecida");
                }
                else {
                    Log.i(TAG_SETTING_HOURS, "Hora final no esta dentro del rango valido");
                    Toast.makeText(this, getResources().getString(R.string.text_out_of_range_hour), Toast.LENGTH_LONG).show();
                }
            }

        }, defaultHour, defaultMinute, false);


        if(isStartTime){
            timePickerDialog.setCustomTitle(utils.createCustomTitle(this,
                    getString(R.string.text_timepicker_title_start_hours),
                    getResources().getColor(R.color.com_facebook_blue),
                    getResources().getColor(R.color.white),
                    18));
        }
        else {
            timePickerDialog.setCustomTitle(utils.createCustomTitle(this,
                    getString(R.string.text_timepicker_title_end_hours),
                    getResources().getColor(R.color.com_facebook_blue),
                    getResources().getColor(R.color.white),
                    18));
        }

        timePickerDialog.show();
    }

    private void setSettingsValues(int hourOfDay, int minute, boolean isStartTime){
        Log.i(TAG_SETTING_HOURS, "Establecer: " + hourOfDay + ":" + minute + " - es inicio? " + isStartTime);
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
        String pickedHour = formattedHour + ":" + formattedMinute + " " + AM_PM;

        if(isStartTime){
            String startText = getString(R.string.text_start_at) + " " + pickedHour;
            textViewMyStart.setText(startText);
            AlarmFirst.getInstance().adminFirstAlarm(WaterDrinkActivity.this);
        }
        else{
            String endText = getString(R.string.text_end_at) + " " + pickedHour;
            textViewMyEnd.setText(endText);
            AlarmLast.getInstance().adminLastAlarm(WaterDrinkActivity.this);
        }
    }

    private void activateAlarmOnRestartPhone(boolean enabled) {
        int state = PackageManager.COMPONENT_ENABLED_STATE_DISABLED;

        if(enabled){
            state = PackageManager.COMPONENT_ENABLED_STATE_ENABLED;
        }

        ComponentName receiver = new ComponentName(WaterDrinkActivity.this, AndroidActionsReceiver.class);
        PackageManager pm = WaterDrinkActivity.this.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                state,
                PackageManager.DONT_KILL_APP);

    }

    //region PULSATOR
    int pulsatorColor = R.color.blue_water;
    int pulsatorInterpolator = PulsatorLayout.INTERP_DECELERATE;
    int pulsatorDuration = 900;
    int pulsatorCount = 3;

    private void setPulsatorLayout(boolean activate){
        if(activate){
            pulsatorLayout.setColor(getResources().getColor(pulsatorColor));
            pulsatorLayout.setInterpolator(pulsatorInterpolator);
            pulsatorLayout.setDuration(pulsatorDuration);
            pulsatorLayout.setCount(pulsatorCount);

            if(!pulsatorLayout.isStarted())
                pulsatorLayout.start();
        }
        else{
            pulsatorLayout.stop();
        }
    }
    //endregion PULSATOR

    //region ACTIVITY CALLBACKS

    @Override
    protected void onStop() {
        Log.i(TAG_WATER_DRINK_ACT, "onStop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.i(TAG_WATER_DRINK_ACT, "onDestroy");
        if(dbWrite != null) dbWrite.close();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        Log.i(TAG_WATER_DRINK_ACT, "onResume");
        super.onResume();
        ThisApplication.onResume();
        updateProgress();

        //Cerrar la notificacion en caso de estar activa
        Log.i(TAG_WATER_DRINK_ACT, "Cerrar notificacion en caso que exista");
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(WaterDrinkActivity.this);
        notificationManager.cancel(Constants.NOTIFICATION_ID_APP);
    }

    @Override
    protected void onPause() {
        Log.i(TAG_WATER_DRINK_ACT, "onPause");
        super.onPause();
        ThisApplication.onPause();

        if(layoutRate.getVisibility() == View.VISIBLE){
            // Si salimos y el rating esta visible, entonces reiniciamos
            if(settings.getAppRated() != 8){
                settings.setAppRated(0);
                Log.i(TAG_RATE, settings.getAppVersion() + " - Contador de rated: " + settings.getAppRated());
            }
            // Pero si regresa (aun tendremos cero) y entonces califica la app, establecemos 8 y ya no necesitamos
            // volver a ponerlo en cero
        }

        if(countDownTimer == null && settings.isAlarmWorking()){

            // Si es hora de tomar agua (alarma nula) y el usuario abandona la app, vamos a poner una alarma en 5min,
            // cuando se active la alarma (si aun esta la app en horas activas) entonces lanzamos notificacion.

            Log.i(TAG_WATER_DRINK_ACT, "La alarma esta funcionando: Salio de la app, alarma en: 5 mins");
            long nextReminder = SystemClock.elapsedRealtime() + ((5 * 60) * 1000); // cada 5 minutos
            AlarmApp.getInstance().updateAlarm(nextReminder);
        }

        if(isDrinkTimeFromNotification){
            Log.i(TAG_RECORD, "Entraste a la app desde la notificacion y no tomaste agua. Registrar 3");
            recordDrink(0,3);
            isDrinkTimeFromNotification = false;
        }
    }



    //endregion ACTIVITY CALLBACKS

    //region COUNTDOWN
    private class CountDownTimerDrink extends CountDownTimer{

        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */

        Context context;
        private long remainingTimeInMillis;

        public CountDownTimerDrink(long millisInFuture, long countDownInterval, Context context) {
            super(millisInFuture, countDownInterval);
            this.context = context;
        }

        @Override
        public void onTick(long millisUntilFinished) {
            remainingTimeInMillis = millisUntilFinished;

            int hours = utils.getHours(remainingTimeInMillis);
            long minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60;
            long seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60;
            String text = "";

            if(hours > 0 ){
                text = String.format(Locale.getDefault(), getString(R.string.text_next_in) + " %2d " + getString(R.string.text_hours) + " %2d " + getString(R.string.text_minutes) + " %2d " + getString(R.string.text_seconds),
                        hours,
                        minutes,
                        seconds);
            }
            else if(minutes > 0){
                text = String.format(Locale.getDefault(), getString(R.string.text_next_in) + " %2d " + getString(R.string.text_minutes) + " %2d " + getString(R.string.text_seconds),
                        minutes,
                        seconds);
            }
            else {
                text = String.format(Locale.getDefault(), getString(R.string.text_next_in) + " %2d " + getString(R.string.text_seconds),
                        seconds);
            }

            textViewNextDrink.setText(text);
        }

        @Override
        public void onFinish() {
            textViewNextDrink.setText("");
            textViewTips.setText(awesomeTexts.getCoutdownTextDone(context));
            //textViewStats.setText("");
            buttonDrink.setText(getString(R.string.text_i_drink));

            setAppStyle();
            setPulsatorLayout(true);
            if(ThisApplication.isActivityVisible())
                AlarmApp.getInstance().cancelAlarm(); //Cancelar alarma si estamos en la app

        }

        public long getTimeInMillis(){
            return remainingTimeInMillis;
        }


    }
    //endregion COUNTDOWN

    //region CALLBACKS
    @Override
    public void onReturnCallback(boolean status, String message, int code) {

    }

    @Override
    public void onYesResponse(String code, String message) {
        if(code.equalsIgnoreCase(CODE_SET_ALARM)){
            finish();
        }
    }

    @Override
    public void onNoResponse(String code, String message) {
        if(code.equalsIgnoreCase(CODE_SET_ALARM)){
            buttonSetEnd.callOnClick();
        }
    }
    //endregion CALLBACKS

    private void getNextTimeCalculation(){

        //1 dia = 1440 minutos
        //01 litro = 1000ml

        float userGoal = 1500; // 1500 mililitros = 1.5 litros
        float currentDrink = 200; //200 mililitros
        float userReminderInterval = 45; //45 minutos

        //Hora de inicio segun usuario
        int startHourOfDay = 6; //6am
        int startMinute = 0; //
        int startTimeInSeconds = utils.getSeconds(startHourOfDay, startMinute);


        //Hora de finalizar segun usuario
        int endHourOfDay = 20; //8pm
        int endMinute = 0; //
        int endTimeInSeconds = utils.getSeconds(endHourOfDay, endMinute);


        //Hora de finalizar maxima
        int endMaxHourOfDay = 23; //Medianoche
        int endMaxMinute = 59; //
        int endMaxTimeInSeconds = utils.getSeconds(endMaxHourOfDay, endMaxMinute);


        //hora actual
        Calendar calendar = Calendar.getInstance();
        int currentHourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMinute = calendar.get(Calendar.MINUTE);
        int currentTimeInSeconds = utils.getSeconds(currentHourOfDay, currentMinute);

        //1 dia - 24hras

        //calcular t0do normal
        /*
             Hora que tomas agua y presionas el boton = 6am

        20 - 6 = 14horas
               = (1500 mililitros - currentDrink) en 14 horas
               = (1500 - 200)/14 (14 horas en segundos = 50400)
               =  1300 / 14
               1300 mililitros de agua a tomar en las proximas 14 horas...

               -- digamos que yo puedo tomar maximo 100ml de agua en cada intervalo...

                100 / 14


         */

        //TODO: CALCULEMOS PUES!

        // 0:45 min para tomar agua
        float nextTimeInSeconds = 2700; // 45 minutos
        nextTimeInSeconds = userGoal/(startTimeInSeconds - endTimeInSeconds);


        /*
         1500 - 200 = 1300 ml --- 6am





          */


    }

    private void recordDrink(float drinkAmount, int statusCode) {
        // Gets the data repository in write mode
        dbWrite = dbHelper.getWritableDatabase();

        String timestamp = utils.getCurrentTimestamp();
        RecordModel newRecord = new RecordModel(Long.parseLong(timestamp), drinkAmount, statusCode);

        // Insert the new row, returning the primary key value of the new row
        long newRowId = dbWrite.insert(RecordsContract.RecordsEntry.TABLE_NAME,
                null,
                newRecord.toContentValues()
        );

        dbWrite.close();
        Log.i(TAG_RECORD, "Registramos " + timestamp + " con estado: " + statusCode + ". Resultado: " + (newRowId == -1 ? "FALLO al guardar" : "Registrado!"));

    }

    private void deletePreviousRecord() {

        // Gets the data repository in write mode
        dbWrite = dbHelper.getWritableDatabase();

        // Eliminar ultimo registro si es status = 6 (abandono sin beber) ;
        // Eliminar ultimo registro si es status = 3 (no tomo agua entro a la notificacion);
        try{
            dbWrite.execSQL("DELETE FROM "
                    + RecordsEntry.TABLE_NAME +
                    " WHERE "
                    + RecordsEntry._ID +
                    " = (SELECT MAX("
                    + RecordsEntry._ID +
                    ") FROM "
                    + RecordsEntry.TABLE_NAME +
                    " ) AND ("
                    + RecordsEntry.COLUMN_NAME_STATUS_CODE +
                    " = 3 OR "
                    + RecordsEntry.COLUMN_NAME_STATUS_CODE +
                    " = 6)");
        }
        catch (Exception e){
            //Toast.makeText(this, "Error en base de datos", Toast.LENGTH_SHORT).show();
            Log.i(TAG_RECORD, "Error:\n\n" + e.toString() );
        }

        dbWrite.close();
        Log.i(TAG_RECORD, "Borrado del ultimo registro terminado...");

    }


    @SuppressLint("StaticFieldLeak")
    private class Task extends AsyncTask<Integer, Void, List<RecordModel>>{

        //region INFORMATION
        /*
        EJ: private class MiTareaAsincrona extends AsyncTask<Void, Integer, Boolean>

        En nuestro caso de ejemplo, extenderemos de AsyncTask indicando los
         tipos Void, Integer y Boolean respectivamente, lo que se traducirá en que:

        1. doInBackground() no recibirá ningún parámetro de entrada (Void).
        2. publishProgress() y onProgressUpdate() recibirán como parámetros datos de tipo entero (Integer).
        3. doInBackground() devolverá como retorno un dato de tipo booleano y onPostExecute()
        también recibirá como parámetro un dato del dicho tipo (Boolean).
        */
        //endregion INFORMATION

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // Eliminar registros (si existen)
            adapterSwitch.removeAll();
            adapterSwitch.notifyDataSetChanged();
        }

        @Override
        protected List<RecordModel> doInBackground(Integer... params) {

            int when = params[0];

            //read from db
            dbRead = dbHelper.getReadableDatabase();

            //region QUERY EXAMPLES
            // Define a projection that specifies which columns from the database you will actually use after this query.
        /*
        String[] projection = {
                BaseColumns._ID,
                RecordsEntry.COLUMN_NAME_TIMESTAMP,
                RecordsEntry.COLUMN_NAME_DRINK_AMOUNT,
                RecordsEntry.COLUMN_NAME_STATUS_CODE
        };

        // Filter results WHERE "drinkAmount" = '100'
        String selection = RecordsEntry.COLUMN_NAME_DRINK_AMOUNT + " = ?";
        String[] selectionArgs = { "100" };

        // How you want the results sorted in the resulting Cursor
        String sortOrder = RecordsEntry.COLUMN_NAME_TIMESTAMP + " DESC";

        Cursor cursor = dbRead.query(
                RecordsEntry.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
        );

        List itemIds = new ArrayList<>();

        while(cursor.moveToNext()) {
            long itemId = cursor.getLong(cursor.getColumnIndexOrThrow(RecordsEntry._ID));
            itemIds.add(itemId);
        }
        cursor.close();
*/
            //endregion QUERY EXAMPLES

            Cursor records = null;
            //Cursor records = dbHelper.getAllRecords();
            if(when == 0){
                records = dbHelper.getTodayRecords();
                Log.i(TAG_RECORD, "0 -- Records!");
            }
            else if(when == 1){
                records = dbHelper.getYesterdayRecords();
                Log.i(TAG_RECORD, "1 -- Records!");
            }

            if(records != null && records.moveToFirst()){
                Log.i(TAG_RECORD, "Registros encontrados! ");

                List<RecordModel> receivedData = new ArrayList<>();

                do {
                    Log.i(TAG_RECORD, "entramos al while..!");
                    RecordModel newRecord = new RecordModel();
                    newRecord.setId(Integer.parseInt(records.getString(0)));
                    newRecord.setTimestamp(Long.parseLong(records.getString(1)));
                    newRecord.setDrinkAmount(Float.parseFloat(records.getString(2)));
                    newRecord.setStatusCode(Integer.parseInt(records.getString(3)));

                    Log.i(TAG_RECORD, "Añadido: " + newRecord.getTimestamp() + " " + newRecord.getDrinkAmount() + " " + newRecord.getStatusCode());
                    receivedData.add(newRecord);
                }
                while (records.moveToNext());

                Log.i(TAG_RECORD, "SALIMOS DEL while..! " + receivedData.size() + " " + records.getCount());

                //region SAMPLE DATA
                /*
                // Registros cargados
                List<RecordModel> receivedData = new ArrayList<>();

                receivedData.add(new RecordModel(0, "1557480446", 100));
                receivedData.add(new RecordModel(0, "1557480461",30));
                receivedData.add(new RecordModel(0, "1557481044",0));
                receivedData.add(new RecordModel(0, "1557481061",70));

                //fron notification:
                receivedData.add(new RecordModel(0, "1557501828",true, true));
                receivedData.add(new RecordModel(0, "1557501843",95, true));
                receivedData.add(new RecordModel(0, "1557505213",true, true));
                */
                //endregion SAMPLE DATA

                Collections.reverse(receivedData);
                adapterSwitch.setCustomStyle(when);
                records.close();

                return receivedData;
            }
            else {
                Log.i(TAG_RECORD, "Sin registros");
                if(records != null) records.close();
                return null;
            }

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onCancelled() {
            Toast.makeText(WaterDrinkActivity.this, "Se cancelo la operacion :(", Toast.LENGTH_SHORT).show();
            super.onCancelled();
        }

        @Override
        protected void onPostExecute(List<RecordModel> receivedData) {
            super.onPostExecute(receivedData);

            if(receivedData != null && receivedData.size() > 0){
                Log.i(TAG_RECORD, "La ejecucion finalizo con exito");
                adapterSwitch.overrideList(receivedData);
                adapterSwitch.notifyDataSetChanged();

                textViewRecordMessage.setText("");
                recyclerRecords.setVisibility(View.VISIBLE);
                progressBarRecord.setVisibility(View.GONE);

            }
            else {
                Log.i(TAG_RECORD, "La ejecucion retorno FALSE");
                textViewRecordMessage.setText(getString(R.string.text_report_no_records));
                recyclerRecords.setVisibility(View.GONE);
                progressBarRecord.setVisibility(View.GONE);
            }
        }
    }
}
