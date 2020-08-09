package com.eahm.drinkwaterapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.eahm.drinkwaterapp.Class.SettingsClass;
import com.eahm.drinkwaterapp.Interfaces.Callbacks;
import com.eahm.drinkwaterapp.Models.News;
import com.eahm.drinkwaterapp.Models.NewsMedia;
import com.eahm.drinkwaterapp.Models.UserModel;
import com.eahm.drinkwaterapp.Utils.UserManagement;
import com.eahm.drinkwaterapp.Utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.treebo.internetavailabilitychecker.InternetAvailabilityChecker;
import com.treebo.internetavailabilitychecker.InternetConnectivityListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SplashScreen extends AppCompatActivity implements Callbacks, InternetConnectivityListener {

    //region VARIABLES
    private static final String TAG_SS = "TAG_SS";
    private final static String TAG_CONNECTED = "TAG_CONNECTED";
    private final static String TAG_NEWS = "TAG_NEWS";
    private static final String TAG_SET_APP_INFO = "TAG_SET_APP_INFO";

    private static final String UPDATE_APP = "UPDATE_APP";
    private static final String UPDATE_APP_FORCED = "UPDATE_APP_FORCED";
    private static final String DISABLED_APP = "DISABLED_APP";

    String languagePhone = "Español"; //Lenguaje que tiene definido en el telefono
    String languageCountry = "Español"; //Lenguaje del pais donde esta actualmente

    boolean worksOffline = true; // Esta app funciona sin internet?
    boolean weHaveInternet = true; // conectividad a internet

    private ImageView imageViewMarkerDesign;
    private TextView textViewExtraInfo;
    private TextView textViewLoadingApp;
    private VideoView videoViewSplashVideo;

    Utils utils = new Utils();

    private boolean stopFadeAnimation = false;

    UserManagement userManager = new UserManagement();
    private UserModel cUser = new UserModel("","","","","","","","", null);
    private FirebaseAuth auth;


    boolean fullscreen = false;

    //region GET APP INFORMATION
    boolean appInfoDone = false;

    DatabaseReference dbRefAppInfo;
    ValueEventListener veListenerAppInfo;

    boolean appEnabled = false;
    String appOnDisableMessage = "";
    String privacyPolicyURL = "";
    boolean updateForce = true;
    String updateCurrentVersion = "NONE";
    String updateOnNewVersionMessage = "";

    //endregion GET APP INFORMATION

    boolean appGetCountry = false;
    boolean appNews = false;
    boolean appUser = false;

    boolean videoSplashScreenDone = false;
    boolean closeApp = false;
    boolean started = false;
    boolean updateLater = false;


    //region PRIVACY POLICY
    CardView includeLayoutPrivacyPolicy;
    TextView textViewPPContent;
    TextView textViewPPInformation;
    CheckBox checkBoxPP;
    Button buttonPPContinue;
    //endregion PRIVACY POLICY

    //endregion

    SettingsClass settings = new SettingsClass();

    InternetAvailabilityChecker iaChecker;

    @Override
    public void onBackPressed() {
        if(includeLayoutPrivacyPolicy.getVisibility() == View.VISIBLE){
            if(!closeApp){
                closeApp = true;
                Toast.makeText(this, getString(R.string.exit_press_again), Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(()->{
                    closeApp = false;
                }, 2500);
            }
            else super.onBackPressed();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(fullscreen){
            ((AnimationDrawable) getWindow().getDecorView().getBackground()).start();
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        languagePhone = Utils.convertFirstLetterToUpperCase(Locale.getDefault().getDisplayLanguage());
        Log.i(TAG_SS, "Lenguaje del telefono: " + languagePhone);

        setContentView(R.layout.activity_splash_screen);

        //region FIND IDS
        imageViewMarkerDesign = findViewById(R.id.imageViewLoadingDesign);
        textViewExtraInfo = findViewById(R.id.textViewExtraInfo);
        textViewLoadingApp = findViewById(R.id.textViewLoadingApp);

        //region PRIVACY POLICY
        includeLayoutPrivacyPolicy = findViewById(R.id.includeLayoutPrivacyPolicy);
        textViewPPContent = findViewById(R.id.textViewPPContent);
        textViewPPInformation = findViewById(R.id.textViewPPInformation);
        checkBoxPP = findViewById(R.id.checkBoxPP);
        buttonPPContinue = findViewById(R.id.buttonPPContinue);
        //endregion PRIVACY POLICY

        //endregion FIND IDS

        //region LISTENERS
        checkBoxPP.setOnCheckedChangeListener((buttonView, isChecked) -> textViewPPInformation.setVisibility(View.INVISIBLE));

        buttonPPContinue.setOnClickListener(v -> {
            if(checkBoxPP.isChecked()){
                settings.setPrivacyPolicyStatus(true);
                textViewPPInformation.setVisibility(View.INVISIBLE);
                includeLayoutPrivacyPolicy.setVisibility(View.GONE);
                setAppInformation();
            }
            else {
                textViewPPInformation.setVisibility(View.VISIBLE);
            }
        });
        //endregion LISTENERS

        //Ocultar politicas de privacidad, las mostraremos mas tarde si es necesario...
        textViewPPInformation.setVisibility(View.INVISIBLE);

        //initialize and wait a response since the splash screen is in progress...
        textViewLoadingApp.setText(getString(R.string.ss_loading_content));

        //setAnimation while we wait...
        imageViewMarkerDesign.setAlpha(1f);
        fadeOut(imageViewMarkerDesign, 1000);

        //Esperamos al callback OnInternetConnectivityChanged() para continuar...
        // Check internet flow is active. (after the ids are initialized
        InternetAvailabilityChecker.init(this);
        iaChecker = InternetAvailabilityChecker.getInstance();
        iaChecker.addInternetConnectivityListener(this);

        //region OLD METHOD
        /*
        // 0: Without(Testing) 1: Normal  2: Video
        switch (Constants.SPLASH_SCREEN_MODE){
            case "0":
                //region WITHOUT
                getAppInformation();
                getAppNewsCheckAvailabilityAndPrivacyPolicy();
                getAppCurrentUser();
                //endregion WITHOUT
                break;
            case "1":
              //norma was here
                break;
            case "2":
                //region VIDEO
                setContentView(R.layout.activity_splash_screen);
                imageViewMarkerDesign = findViewById(R.id.imageViewLoadingDesign);
                textViewLoadingApp = findViewById(R.id.textViewLoadingApp);
                videoViewSplashVideo = findViewById(R.id.videoViewSplashVideo);

                imageViewMarkerDesign.setVisibility(View.GONE);
                textViewLoadingApp.setVisibility(View.GONE);
                videoViewSplashVideo.setVisibility(View.VISIBLE);

                try {
                    Uri video = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.splash_screen_video);
                    videoViewSplashVideo.setVideoURI(video);

                    videoViewSplashVideo.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        public void onCompletion(MediaPlayer mp) {
                            if (isFinishing()) return;

                            textViewLoadingApp.setVisibility(View.VISIBLE);
                            //textViewLoadingApp.setTextColor(getResources().getColor(R.color.white));
                            videoSplashScreenDone = true;
                            setAppInformation();
                        }
                    });

                    getAppInformation();
                    getAppNewsCheckAvailabilityAndPrivacyPolicy();
                    getAppCurrentUser();

                    videoViewSplashVideo.start();

                } catch (Exception ex) {
                    if (isFinishing()) return;
                    startMainApp();
                }
                //endregion VIDEO
                break;
            case "3":
                break;
        }
        */
        //endregion OLD METHOD
    }

    //region ANIMATION
    public void fadeOut(View view, long startDelay){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.animate().alpha(0).setDuration(2500).setStartDelay(startDelay).withEndAction(()-> {
                if (!stopFadeAnimation) fadeIn(view,0);
            });
        }
    }

    public void fadeIn(View view, long startDelay){
        view.setAlpha(0f);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.animate().alpha(1).setDuration(2500).setStartDelay(startDelay).withEndAction(()-> {
                if (!stopFadeAnimation) fadeOut(view,0);
            });
        }
    }
    //endregion

    //region GET APP CONFIGURATION

    boolean appInfoFromServer = false;

    private void getAppInformation(){
        // Establecemos valores por defecto
        appInfoFromServer = false;

        appEnabled = false;
        appOnDisableMessage = getString(R.string.message_app_disabled);
        updateForce = true;
        updateCurrentVersion = "NONE";
        updateOnNewVersionMessage = getString(R.string.message_update_new_version);

        dbRefAppInfo = FirebaseDatabase.getInstance().getReference("app");

        veListenerAppInfo = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){
                    if (dataSnapshot.hasChild("application")){
                        if (dataSnapshot.child("application").hasChild("enabled"))
                            appEnabled = dataSnapshot.child("application").child("enabled").getValue(true).toString().equalsIgnoreCase("true");

                        if (dataSnapshot.child("application").hasChild("onDisableMessage")){

                            if(dataSnapshot.child("application").child("onDisableMessage").hasChild(languagePhone)){
                                appOnDisableMessage = dataSnapshot.child("application").child("onDisableMessage").child(languagePhone).getValue().toString();
                            }
                        }

                        if (dataSnapshot.child("application").hasChild("privacyPolicyUrl")){
                            privacyPolicyURL = dataSnapshot.child("application").child("privacyPolicyUrl").getValue().toString();
                        }
                    }

                    if (dataSnapshot.hasChild("updates")){
                        if (dataSnapshot.child("updates").hasChild("enabledVersion"))
                            updateCurrentVersion  = dataSnapshot.child("updates").child("enabledVersion").getValue(true).toString();

                        if (dataSnapshot.child("updates").hasChild("forceUpdate"))
                            updateForce = dataSnapshot.child("updates").child("forceUpdate").getValue(true).toString() == "true";

                        if (dataSnapshot.child("updates").hasChild("onNewUpdateMessage")) {

                            if(dataSnapshot.child("updates").child("onNewUpdateMessage").hasChild(languagePhone)){
                                updateOnNewVersionMessage = dataSnapshot.child("updates").child("onNewUpdateMessage").child(languagePhone).getValue().toString();
                            }
                        }
                    }
                    appInfoFromServer = true;
                }

                appInfoDone = true;
                setAppInformation();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.i(TAG_SET_APP_INFO, "onCancelled Called, database error: " + databaseError.toString());

                appInfoDone = true;
                setAppInformation();
            }
        };

        dbRefAppInfo.addListenerForSingleValueEvent(veListenerAppInfo);
    }

    DatabaseReference drNewsCheck;
    ValueEventListener velNewsCheck;

    String privacyPolicy = "";

    private final static String COUNTRY_WEB_SERVICE_URL = "http://ip-api.com/json"; //TODO The limit is 150 requests per minute from an IP address. If you go over this limit your IP address will be blackholed. You can unban here. If you need unlimited queries, please see our pro service.
    RequestQueue requestQueue;

    private void getCountry() {

        //region CHECK TO QUERY AGAIN
        String userCountryCode = settings.getWSUserCountry();
        String lastCountryCheckTimestamp = settings.getWSCountryLastTimestamp();

        boolean weCanCheck = false; // Si ya paso mas de 1 minutos desde la ultima vez q revisamos el pais, entonces podemos volver a revisar
        Log.i(TAG_NEWS, "Revisar: CountryCode: " + userCountryCode + " LastTimestamp: " + lastCountryCheckTimestamp);

        if(lastCountryCheckTimestamp.isEmpty()){
            Log.i(TAG_NEWS, "No hay ultima vez consultada");
            weCanCheck = true;
        }
        else {
            try{
                Long current = Long.parseLong(utils.getCurrentTimestamp());
                Long previous = Long.parseLong(lastCountryCheckTimestamp);

                long difference = Math.abs(current - previous);
                Log.i(TAG_NEWS, "Diferencia entre la ultima vez: " + current + " - " + previous + " = " + difference);

                if(difference > 60){
                    //Ya paso mas de 1 minuto desde la ultima vez que revisamos el pais. Ya podemos volver a consultar.
                    weCanCheck = true;
                    Log.i(TAG_NEWS, "Consultar Pais");
                }
            }
            catch (Exception e){
                Log.i(TAG_NEWS, "Error al convertir String a Long: " + e.toString());
                weCanCheck = true;
            }
        }

        //endregion CHECK TO QUERY AGAIN

        if (userCountryCode.isEmpty() || weCanCheck) {
            Log.i(TAG_NEWS, "Preparando consulta a webservice");

            requestQueue = Volley.newRequestQueue(this);

            StringRequest stringRequest = new StringRequest(Request.Method.GET,
                    COUNTRY_WEB_SERVICE_URL,
                    onResponseListener,
                    onResponseErrorListener);

            requestQueue.add(stringRequest);
            requestQueue.start();

        }
        else {
            Log.i(TAG_NEWS, "Pais actual guardado: " + userCountryCode);

            // Firebase Messaging THEME
            subscribeToNotificationTheme(userCountryCode);

            appGetCountry = true;
            setAppInformation();
        }

    }

    int attempts = 0;

    Response.ErrorListener onResponseErrorListener = new Response.ErrorListener(){
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.i(TAG_NEWS, "Volley error: " + error.toString());

            if(attempts >= 4){
                Log.i(TAG_NEWS, "Ya intentamos 4 veces y no funciono!");
                attempts = 0;
                onGetCountryError();
            }
            else {
                attempts++;
                getCountry();
            }
        }
    };

    private void onGetCountryError() {
        //Guardar este pais segun la tarjeta SIM. Este no es muy confiable por eso lo dejamos como ultimo recurso
        final String myCountry = Utils.getUserCountry(SplashScreen.this);
        settings.setWSUserCountry(myCountry);
        settings.setWSCountryLastTimestamp(utils.getCurrentTimestamp());

        appGetCountry = true;
        setAppInformation();
    }

    Response.Listener<String> onResponseListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            Log.i(TAG_NEWS, "Respuesta recibida");
            try {
                JSONObject respuesta = new JSONObject(response);
                if(respuesta.has("status") && respuesta.get("status").toString().equalsIgnoreCase("success")){
                    String countryCode = respuesta.get("countryCode").toString();
                    String country = respuesta.get("country").toString();

                    settings.setWSUserCountry(countryCode);
                    settings.setWSCountryLastTimestamp(utils.getCurrentTimestamp());

                    Log.i(TAG_NEWS, "Pais: " + country + " CODIGO: " + countryCode);

                    // Firebase Messaging THEME
                    subscribeToNotificationTheme(countryCode);

                    appGetCountry = true;
                    setAppInformation();
                }
                else {
                    Log.i(TAG_NEWS, "Web service devolvio error... seguir sin webservice...");
                    onGetCountryError();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.i(TAG_NEWS, "Un error ocurrio al obtener el json: " + e.toString());

                onGetCountryError();
            }
        }
    };

    private void subscribeToNotificationTheme(String countryCode) {
        if(!countryCode.isEmpty()){
            FirebaseMessaging.getInstance().subscribeToTopic(countryCode)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Log.i(TAG_NEWS, "Suscrito a el tema: " + countryCode + " en FIREBASE Messaging");
                            }
                            else {
                                Log.i(TAG_NEWS, "No se pudo suscribir al tema: " + countryCode);
                            }
                        }
                    });
        }
    }

    private void getAppNewsCheckAvailabilityAndPrivacyPolicy() {

        String countryCode = settings.getWSUserCountry();

        //region VALIDATE PER COUNTRY

        //Crear funcion para devolver el lenguaje segun el pais en nodo: communication

        if(
            countryCode .equalsIgnoreCase("DE")
           ){
            languageCountry = "Deutsch";
        }
        else if(
            countryCode .equalsIgnoreCase("US") ||
            countryCode .equalsIgnoreCase("EN")
                ){
            languageCountry = "English";
        }
        else if(
            countryCode .equalsIgnoreCase("NI") ||
            countryCode .equalsIgnoreCase("ES") ||
            countryCode .equalsIgnoreCase("HN")
                ){
            languageCountry = "Español";
        }
        else {
            languageCountry = "NOT_FOUND";
        }

        ((ThisApplication) getApplication()).setLanguageCountry(languageCountry.equals("NOT_FOUND") ? languagePhone : languageCountry);
        Log.i(TAG_NEWS, languageCountry.equals("NOT_FOUND") ? "Lenguaje no encontrado, asignado del telefono: " + languagePhone : "Lenguaje encontrado, asignado: " + languageCountry);

        //endregion VALIDATE PER COUNTRY

        if(languageCountry.equals("NOT_FOUNDED")){
            Log.i(TAG_NEWS, "Lenguaje no encontrado. Sin noticias");
            getAppNewsDone();
        }
        else {
            drNewsCheck =  FirebaseDatabase.getInstance().getReference().child("communication").child(languageCountry);
            velNewsCheck = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists() && dataSnapshot.hasChild("countries")) {

                        boolean countryAvailable = false;

                        for (DataSnapshot countries : dataSnapshot.child("countries").getChildren()) {
                            String key = countries.getKey();
                            Log.i(TAG_NEWS, key + " == " + countryCode);

                            if (key != null && key.equalsIgnoreCase(countryCode)) {
                                Log.i(TAG_NEWS, "Pais disponible");
                                countryAvailable = true;
                            }
                        }

                        if(dataSnapshot.hasChild("privacyPolicy") ){
                            privacyPolicy = dataSnapshot.child("privacyPolicy").getValue().toString();
                        }

                        if(countryAvailable) {
                            getAppNewsByCountry();
                            return;
                        }
                    }

                    getAppNewsDone();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    //Toast.makeText(SplashScreen.this, "1. Error: " + databaseError.toString(), Toast.LENGTH_SHORT).show();
                    Log.i(TAG_NEWS, "getAppNewsCheckAvailabilityAndPrivacyPolicy() Hay un problema: " + databaseError.toString());
                    getAppNewsDone();
                }
            };
            drNewsCheck.addListenerForSingleValueEvent(velNewsCheck);
        }
    }

    Query queryNewsByCountry;
    ValueEventListener velNewsByCountry;

    private void getAppNewsByCountry(){
        int limitMaxOfNewsTo = 4;

        queryNewsByCountry = FirebaseDatabase.getInstance().getReference("communication").child(languageCountry).child("news")
                .orderByChild("enabled").equalTo(true)
                .limitToLast(limitMaxOfNewsTo);

        Log.i(TAG_NEWS, "GET NEWS");
        velNewsByCountry = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){

                    if(!((ThisApplication) getApplication()).getNewsList().isEmpty()){
                        ((ThisApplication) getApplication()).clearNewsList();
                    }

                    for (DataSnapshot item : dataSnapshot.getChildren()) {

                        News newsModelItem = new News();

                        if (item.hasChild("enabled")) newsModelItem.setEnabled(item.child("enabled").getValue(true).toString().equalsIgnoreCase("true"));
                        if (item.hasChild("publishedTimestamp")) newsModelItem.setPublishedTimestamp(item.child("publishedTimestamp").getValue(true).toString());


                        if (item.hasChild("title")) newsModelItem.setTitle(item.child("title").getValue(true).toString());
                        if (item.hasChild("content")) newsModelItem.setContent(item.child("content").getValue(true).toString());
                        if (item.hasChild("contentUrl")) newsModelItem.setContentUrl(item.child("contentUrl").getValue(true).toString());

                        if (item.hasChild("media")){
                            List<NewsMedia> newsMediaList = new ArrayList<>();

                            for (DataSnapshot itemMedia: item.child("media").getChildren()) {

                                NewsMedia newsMediaModelItem = new NewsMedia();
                                if (itemMedia.hasChild("enabled")) newsMediaModelItem.setEnabled(itemMedia.child("enabled").getValue(true).toString().equalsIgnoreCase("true"));
                                if (itemMedia.hasChild("isVideo")) newsMediaModelItem.setVideo(itemMedia.child("isVideo").getValue(true).toString().equalsIgnoreCase("true"));

                                if (itemMedia.hasChild("coverImageUrl")) newsMediaModelItem.setCoverImageUrl(itemMedia.child("coverImageUrl").getValue(true).toString());
                                if (itemMedia.hasChild("coverImageScaleType")) newsMediaModelItem.setCoverImageScaleType(itemMedia.child("coverImageScaleType").getValue(true).toString());
                                if (itemMedia.hasChild("mediaUrl")) newsMediaModelItem.setMediaUrl(itemMedia.child("mediaUrl").getValue(true).toString());
                                if (itemMedia.hasChild("mediaOnClickUrl")) newsMediaModelItem.setMediaOnClickUrl(itemMedia.child("mediaOnClickUrl").getValue(true).toString());

                                newsMediaList.add(newsMediaModelItem);
                            }

                            newsModelItem.setMedia(newsMediaList);
                        }
                        else newsModelItem.setMedia(null);

                        if (item.hasChild("titleTextSize")) newsModelItem.setTitleTextSize(Integer.parseInt(item.child("titleTextSize").getValue(true).toString()));
                        if (item.hasChild("colorPrimaryRGB")) newsModelItem.setColorPrimaryRGB(item.child("colorPrimaryRGB").getValue(true).toString());
                        if (item.hasChild("colorSecondaryRGB")) newsModelItem.setColorSecondaryRGB(item.child("colorSecondaryRGB").getValue(true).toString());
                        if (item.hasChild("colorTextRGB")) newsModelItem.setColorTextRGB(item.child("colorTextRGB").getValue(true).toString());

                        ((ThisApplication) getApplication()).getNewsList().add(newsModelItem);
                    }
                }

                Log.i(TAG_NEWS, "Cantidad de noticias: " +   ((ThisApplication) getApplication()).getNewsList().size());

                getAppNewsDone();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(SplashScreen.this, "2. Error: " + databaseError.toString(), Toast.LENGTH_SHORT).show();
                getAppNewsDone();
            }
        };
        queryNewsByCountry.addListenerForSingleValueEvent(velNewsByCountry);

    }
    private void getAppNewsDone(){
        appNews = true;
        setAppInformation();
    }

    Task<SignInMethodQueryResult> fetch;
    DatabaseReference drCurrentUserDBVerification;
    ValueEventListener velCurrentUserDBVerification;


    private void getAppCurrentUser() {

        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null){

            fetch = auth.fetchSignInMethodsForEmail(auth.getCurrentUser().getEmail() == null ? "" : auth.getCurrentUser().getEmail());
            fetch.addOnSuccessListener(new OnSuccessListener<SignInMethodQueryResult>() {
                @Override
                public void onSuccess(SignInMethodQueryResult signInMethodQueryResult) {

                    if(signInMethodQueryResult.getSignInMethods().isEmpty()){
                        // theres no Email, is deleted or invalid, user do not exist
                        auth.signOut();
                        appUser = true;
                        setAppInformation();
                    }
                    else {
                        Log.i("VALIDATION", "USUARIO EXISTE");
                        //USUARIO EXISTE signInMethodQueryResult.getSignInMethods() is not empty (user list)
                        String reference = "";

                        cUser = userManager.getCustomerData();
                        reference = cUser.getType();

                        drCurrentUserDBVerification = FirebaseDatabase.getInstance().getReference().child("users").child("customer").child(auth.getCurrentUser().getUid());
                        velCurrentUserDBVerification = new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {

                                    String name = "", secondName = "", lastName = "", secondLastName = "", phone = "", type = "", profileImageUrl = "", currentDevice = "";

                                    if (dataSnapshot.hasChild("name"))
                                        name = dataSnapshot.child("name").getValue(true).toString();
                                    if (dataSnapshot.hasChild("secondName"))
                                        secondName = dataSnapshot.child("secondName").getValue(true).toString();
                                    if (dataSnapshot.hasChild("lastName"))
                                        lastName = dataSnapshot.child("lastName").getValue(true).toString();
                                    if (dataSnapshot.hasChild("secondLastName"))
                                        secondLastName = dataSnapshot.child("secondLastName").getValue(true).toString();
                                    if (dataSnapshot.hasChild("phone"))
                                        phone = dataSnapshot.child("phone").getValue(true).toString();
                                    if (dataSnapshot.hasChild("type"))
                                        type = dataSnapshot.child("type").getValue(true).toString();
                                    if (dataSnapshot.hasChild("profileImageUrl"))
                                        profileImageUrl = dataSnapshot.child("profileImageUrl").getValue(true).toString();
                                    if (dataSnapshot.hasChild("currentDevice"))
                                        currentDevice = dataSnapshot.child("currentDevice").getValue(true).toString();

                                    userManager.setCustomerData(new UserModel(name, secondName, lastName, secondLastName, phone, type, profileImageUrl, currentDevice, null));

                                    //getAppCurrentUser has done correctly!
                                    appUser = true;
                                    setAppInformation();
                                    //done!
                                }
                                else {
                                    //DATASNAP NO EXISTE, DEBEMOS SALIR!
                                    userManager.removeCustomerData();
                                    cUser = userManager.getCustomerData();
                                    auth.signOut();
                                    appUser = true;
                                    setAppInformation();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Toast.makeText(SplashScreen.this, "Get App User: Fail: " + databaseError.toString(), Toast.LENGTH_SHORT).show();
                                userManager.removeCustomerData();
                                cUser = userManager.getCustomerData();
                                auth.signOut();
                                appUser = true;
                                setAppInformation();
                            }
                        };
                        drCurrentUserDBVerification.addListenerForSingleValueEvent(velCurrentUserDBVerification);

                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.i("VALIDATION","fetchSignInMethodsForEmail: Error: " + e.toString());
                    if(worksOffline){

                    }
                    else {
                        // Usuario no existe!
                        auth.signOut();
                        appUser = true;
                        setAppInformation();
                    }
                }
            });

        }
        else {
            // no hay usuario guardado, enviar a login
            auth.signOut();
            appUser = true;
            setAppInformation();
        }
    }

    private void cancelAllEventListeners() {
        Log.i(TAG_SET_APP_INFO, "Cancelando listeners...");

        if(dbRefAppInfo != null && veListenerAppInfo != null) dbRefAppInfo.removeEventListener(veListenerAppInfo); //getAppInformation();
        if(drNewsCheck != null && velNewsCheck != null) drNewsCheck.removeEventListener(velNewsCheck); //getAppNewsCheckAvailabilityAndPrivacyPolicy();
        if(queryNewsByCountry != null && velNewsByCountry != null) queryNewsByCountry.removeEventListener(velNewsByCountry); //getAppNewsByCountry();

        //fetch Sign In Methods, we should find a way to cancel this in getAppCurrentUser();
        if(fetch != null) fetch = null;
        if(drCurrentUserDBVerification != null && velCurrentUserDBVerification != null) drCurrentUserDBVerification.removeEventListener(velCurrentUserDBVerification); //getAppCurrentUser();

        if(requestQueue != null){
            // Remover listeners de getCountry()
            requestQueue.cancelAll(new RequestQueue.RequestFilter() {
                @Override
                public boolean apply(Request<?> request) {
                    return true;
                }
            });
            requestQueue.stop();
            requestQueue = null;
        }

    }

    private void setAppInformation(){
        textViewLoadingApp.setText(getString(R.string.ss_getting_content));

        // Cancelar todos los listener (si acaso estan ejecutandose) para volverlos a usar si es requerido.
        cancelAllEventListeners();

        // Casos:
        //       1.  Funcionar solo con internet       2.  Funcionar sin internet
        //       1.1 Tengo internet (CORRE)            2.1 Tengo internet (CORRE)
        //       1.2 No tengo internet (NO CORRE)      2.2 No tengo internet (CORRE)

        //region REVISION DE INTERNET
        Log.i(TAG_SET_APP_INFO, "Revisando internet 1");
        //Revision constante de internet al terminar los listeners
        if(!worksOffline && !weHaveInternet) {
            getAppInfoAgain = true; // Permitir continuar el  proceso de inicio cuando onInternetConnectivityChanged() sea llamado otra vez.
            textViewLoadingApp.setText(getString(R.string.ss_not_connected));
            return;
        }
        //endregion REVISION DE INTERNET

        //region GET APPLICATION INFORMATION

        if (weHaveInternet && !appInfoDone) {
            Log.i(TAG_SET_APP_INFO, "Obtener datos de la app en el servidor");
            textViewLoadingApp.setText(getString(R.string.ss_getting_info));
            getAppInformation();
            return;
        }

        //region REVISION DE INTERNET
        Log.i(TAG_SET_APP_INFO, "Revisando internet 2: en get application information");
        //Revision en: Get Application information
        if(!worksOffline && !weHaveInternet) {
            getAppInfoAgain = true; // Permitir continuar el  proceso de inicio cuando onInternetConnectivityChanged() sea llamado otra vez.
            textViewLoadingApp.setText(getString(R.string.ss_not_connected));
            return;
        }
        //endregion REVISION DE INTERNET

        Log.i(TAG_SET_APP_INFO, "Si ya tenemos datos del servidor, vamos a revisarlos...");
        if(appInfoFromServer){
            textViewLoadingApp.setText(getString(R.string.text_ss_validating_config));

            //region GET CURRENT APP VERSION INSTALLED
            //Obtenemos datos para ver si hay alguna nueva actualizacion...
            int versionNumber = -1;
            String versionName = "";

            try {
                PackageInfo pinfo = getPackageManager().getPackageInfo(getPackageName(), 0);

                versionNumber = pinfo.versionCode;
                versionName = pinfo.versionName;
            }
            catch (Exception e) {
                Log.i(TAG_SET_APP_INFO, "Error obteniendo version: " + e.toString());
                //PackageManager.NameNotFoundException
                e.printStackTrace();
            }

            //endregion GET CURRENT APP VERSION INSTALLED

            if(worksOffline && !weHaveInternet){
                //region WORK OFFLINE
                Log.i(TAG_SET_APP_INFO, "Revisar offline. " + versionName + " = " + updateCurrentVersion + " (updateLater = " + updateLater + ")");
                if (!updateLater && !versionName.equalsIgnoreCase(updateCurrentVersion) && !updateCurrentVersion.equalsIgnoreCase("NONE")) {
                    showOptionalUpdateDialog();
                    return;
                }
                //endregion WORK OFFLINE
            }
            else {
                //region CHECK APP INFORMATION
                if (appEnabled) {
                    Log.i(TAG_SET_APP_INFO, "App activada\nRevisar online. " + versionName + " = " + updateCurrentVersion + " (updateLater = " + updateLater + ")");
                    if (!versionName.equalsIgnoreCase(updateCurrentVersion) && !updateCurrentVersion.equalsIgnoreCase("NONE")) {
                        if(updateForce){
                            textViewLoadingApp.setText(getResources().getString(R.string.text_hurra));

                            if(builder != null && builder.isShowing()){
                                Log.i(TAG_SET_APP_INFO, "Un dialogo esta en pantalla");
                                return;
                            }

                            builder = new AlertDialog.Builder(this).create();
                            builder.setTitle(getResources().getString(R.string.text_hurra));

                            builder.setMessage(updateOnNewVersionMessage);

                            builder.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.text_download), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    onYesResponse(UPDATE_APP_FORCED,"");
                                }
                            });

                            builder.setButton(AlertDialog.BUTTON_NEGATIVE, getResources().getString(R.string.text_exit), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    onNoResponse(UPDATE_APP_FORCED, "");
                                }
                            });

                            builder.setCancelable(false);
                            builder.show();

                            /*utils.displayAlertDialogYesNo(
                                    this,
                                    this,
                                    UPDATE_APP_FORCED,
                                    updateOnNewVersionMessage,
                                    getString(R.string.text_download),
                                    getString(R.string.text_exit),
                                    false);*/
                            return;
                        }
                        else if(!updateLater) {
                            showOptionalUpdateDialog();
                            return;
                        }
                    }
                }
                else {
                    Log.i(TAG_SET_APP_INFO, "App desactivada desde el servidor");
                    textViewLoadingApp.setText(getString(R.string.text_ouch));

                    if(builder != null && builder.isShowing()){
                        Log.i(TAG_SET_APP_INFO, "Un dialogo esta en pantalla");
                        return;
                    }

                    builder = new AlertDialog.Builder(this).create();
                    builder.setTitle(getResources().getString(R.string.text_ouch));

                    builder.setMessage(appOnDisableMessage);

                    builder.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.text_accept), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            onYesResponse(DISABLED_APP,"");
                        }
                    });

                    builder.setCancelable(false);
                    builder.show();

                    /*utils.displayAlertDialogOneOption(
                          this,
                          this,
                            DISABLED_APP,
                          appOnDisableMessage,
                          getString(R.string.text_accept),
                          false);*/
                    return;
                }
                //endregion CHECK APP INFORMATION
            }
        }
        //endregion GET APPLICATION INFORMATION

        //region REVISION DE INTERNET
        Log.i(TAG_SET_APP_INFO, "Revisando internet 3");
        //Revision constante de internet
        if(!worksOffline && !weHaveInternet) {
            getAppInfoAgain = true; // Permitir continuar el  proceso de inicio cuando onInternetConnectivityChanged() sea llamado otra vez.
            textViewLoadingApp.setText(getString(R.string.ss_not_connected));
            return;
        }
        //endregion REVISION DE INTERNET

        //region GET COUNTRY
        if (weHaveInternet && !appGetCountry) {
            Log.i(TAG_SET_APP_INFO, "Obtener pais actual del usuario");
            textViewLoadingApp.setText(getString(R.string.text_ss_getting_language));
            attempts = 0; // Establecer en cero la primera vez que se llama desde aqui
            getCountry();
            return;
        }
        //endregion GET COUNTRY

        //region REVISION DE INTERNET
        Log.i(TAG_SET_APP_INFO, "Revisando internet 4");
        //Revision constante de internet
        if(!worksOffline && !weHaveInternet) {
            getAppInfoAgain = true; // Permitir continuar el  proceso de inicio cuando onInternetConnectivityChanged() sea llamado otra vez.
            textViewLoadingApp.setText(getString(R.string.ss_not_connected));
            return;
        }
        //endregion REVISION DE INTERNET

        //region GET NEWS COMMUNICATION
        if (weHaveInternet && !appNews) {
            Log.i(TAG_SET_APP_INFO, "Obtener datos de comunicacion en el servidor");
            textViewLoadingApp.setText(getString(R.string.ss_checking_news));
            getAppNewsCheckAvailabilityAndPrivacyPolicy();
            return;
        }
        //endregion GET NEWS COMMUNICATION

        //region REVISION DE INTERNET
        Log.i(TAG_SET_APP_INFO, "Revisando internet 5");
        //Revision constante de internet
        if(!worksOffline && !weHaveInternet) {
            getAppInfoAgain = true; // Permitir continuar el  proceso de inicio cuando onInternetConnectivityChanged() sea llamado otra vez.
            textViewLoadingApp.setText(getString(R.string.ss_not_connected));
            return;
        }
        //endregion REVISION DE INTERNET

        //region CHECK PRIVACY POLICY
        if(!settings.getPrivacyPolicyStatus()){
            Log.i(TAG_SET_APP_INFO, "Mostrar politicas de privacidad!");
            textViewLoadingApp.setText(getString(R.string.text_ss_showing_pp));

            if(privacyPolicy.isEmpty()){
                //Mostrar politicas guardadas localmente si no hay internet
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    //getResources()
                    textViewPPContent.setText(Html.fromHtml(getResources().getString(R.string.privacy_policy_local_content), Html.FROM_HTML_MODE_LEGACY));
                } else {
                    textViewPPContent.setText(Html.fromHtml(getResources().getString(R.string.privacy_policy_local_content)));
                }
            }
            else {
                //Obtuvimos las politicas desde el servidor
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    textViewPPContent.setText(Html.fromHtml(privacyPolicy, Html.FROM_HTML_MODE_LEGACY));
                } else {
                    textViewPPContent.setText(Html.fromHtml(privacyPolicy));
                }
            }

            includeLayoutPrivacyPolicy.setVisibility(View.VISIBLE);
            return;
        }
        else if(includeLayoutPrivacyPolicy.getVisibility() == View.VISIBLE) return;
        //endregion CHECK PRIVACY POLICY

        //region REVISION DE INTERNET
        Log.i(TAG_SET_APP_INFO, "Revisando internet 6");
        //Revision constante de internet
        if(!worksOffline && !weHaveInternet) {
            getAppInfoAgain = true; // Permitir continuar el  proceso de inicio cuando onInternetConnectivityChanged() sea llamado otra vez.
            textViewLoadingApp.setText(getString(R.string.ss_not_connected));
            return;
        }
        //endregion REVISION DE INTERNET

        //region GET CURRENT USER
        if (!appUser) {
            textViewLoadingApp.setText(getString(R.string.ss_verifying_user));
            getAppCurrentUser();
            return;
        }
        //endregion GET CURRENT USER

        //region REVISION DE INTERNET
        Log.i(TAG_SET_APP_INFO, "Revisando internet 7");
        //Revision constante de internet
        if(!worksOffline && !weHaveInternet) {
            getAppInfoAgain = true; // Permitir continuar el  proceso de inicio cuando onInternetConnectivityChanged() sea llamado otra vez.
            textViewLoadingApp.setText(getString(R.string.ss_not_connected));
            return;
        }
        //endregion REVISION DE INTERNET

        // En este punto, ya no requerimos de internet en esta activity. asi que cancelamos el listener para no hacer mas llamadas a este metodo. Revisar los permisos debe pasar con o sin internet
        if(iaChecker != null) iaChecker.removeInternetConnectivityChangeListener(this);
        textViewLoadingApp.setText(getString(R.string.ss_loading_content));

        //region APP PERMISSIONS
        settings.setPermissionStatus(false); // establecemos falsos, vamos a revisar si alguno no esta aprobado aun
        getInitialAppPermissions();
        //endregion APP PERMISSIONS

    }

    private void showOptionalUpdateDialog(){
        textViewLoadingApp.setText(getResources().getString(R.string.text_hurra));

        if(builder != null && builder.isShowing()){
            Log.i(TAG_SET_APP_INFO, "Un dialogo esta en pantalla");
            return;
        }

        builder = new AlertDialog.Builder(this).create();
        builder.setTitle(getResources().getString(R.string.text_hurra));

        builder.setMessage(updateOnNewVersionMessage);

        builder.setButton(AlertDialog.BUTTON_POSITIVE,  getString(R.string.text_download), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                onYesResponse(UPDATE_APP,"");
            }
        });

        builder.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.text_later), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                onNoResponse(UPDATE_APP, "");
            }
        });

        builder.setCancelable(false);
        builder.show();

        /*utils.displayAlertDialogYesNo(
                this,
                this,
                UPDATE_APP,
                updateOnNewVersionMessage,
                getString(R.string.text_download),
                getString(R.string.text_later),
                false);*/
    }


    //endregion

    //region REQUEST PERMISSIONS

    long startDelay = 0;
    AlertDialog builder;

    private void showPermissionDialog(){

        textViewLoadingApp.setText(getString(R.string.ss_confirm_permissions_1));

        if(builder != null){
            builder.dismiss();
            builder = null;
        }

        builder = new AlertDialog.Builder(this).create();
        builder.setTitle(getResources().getString(R.string.text_permissions));

        if(privacyPolicyURL.isEmpty()){
            builder.setMessage(getResources().getString(R.string.permission_request_01));
        }
        else {
            builder.setMessage(getResources().getString(R.string.permission_request_02));

            builder.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.privacy_policy_title), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User cancelled the dialog
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(privacyPolicyURL));
                    startActivity(browserIntent);
                    privacyPolicyURL = "";
                    getInitialAppPermissions();
                }
            });
        }

        builder.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.text_continue), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //Prefs.putString(device, "DONE");
                settings.setPermissionStatus(true);
                getInitialAppPermissions();
            }
        });

        builder.setCancelable(false);
        builder.show();

        //endregion
    }

    private void getInitialAppPermissions() {

        //region SYSTEM ALERT WINDOW
/*
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(this, Manifest.permission.SYSTEM_ALERT_WINDOW) != PackageManager.PERMISSION_GRANTED) {
            if(!settings.getPermissionStatus()){
                showPermissionDialog();
                return;
            }

            startDelay = 1500;

            requestPermissionSystemAlertWindow();
            return;
        }
*/
        //endregion SYSTEM ALERT WINDOW

        //region READ_EXTERNAL_STORAGE
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN && ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if(!settings.getPermissionStatus()){
                showPermissionDialog();
                return;
            }


            startDelay = 1500;
            // Check in manifest:  <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
            requestPermissionReadExternalStorage();
            return;
        }
        //endregion READ_EXTERNAL_STORAGE

        //region WRITE_EXTERNAL_STORAGE
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if(!settings.getPermissionStatus()){
                showPermissionDialog();
                return;
            }


            startDelay = 1500;
            // Check in manifest:  <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
            requestPermissionWriteExternalStorage();
            return;
        }
        //endregion WRITE_EXTERNAL_STORAGE

        //Terminamos la verificacion de permisos.. t0d0 en orden! dejamos la variable en true (si habian permisos) la proxima vez la volvemos a poner falso.

        if (startDelay > 0){
            textViewLoadingApp.setText(getString(R.string.ss_starting));
            textViewExtraInfo.setText("");
        }

        new Handler().postDelayed(this::startMainApp,startDelay);

    }

    final int CODE_SYSTEM_ALERT_WINDOW = 123;
    final int CODE_READ_EXTERNAL_STORAGE = 321;
    final int CODE_WRITE_EXTERNAL_STORAGE = 357;

    boolean STATUS_SYSTEM_ALERT_WINDOW = true;
    boolean STATUS_READ_EXTERNAL_STORAGE = true;
    boolean STATUS_WRITE_EXTERNAL_STORAGE = true;

    //region SYSTEM_ALERT_WINDOW

    private void requestPermissionSystemAlertWindow() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SYSTEM_ALERT_WINDOW)){

            new AlertDialog.Builder(this).setTitle(getString(R.string.permission_system_alert_windows)).setMessage(getString(R.string.permission_system_alert_windows_explanation))
                    .setPositiveButton(getString(R.string.permission_grant), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(SplashScreen.this, new String[]{Manifest.permission.SYSTEM_ALERT_WINDOW}, CODE_SYSTEM_ALERT_WINDOW);
                        }
                    })
                    .create()
                    .show();

        }
        else {
            ActivityCompat.requestPermissions(SplashScreen.this, new String[]{Manifest.permission.SYSTEM_ALERT_WINDOW}, CODE_SYSTEM_ALERT_WINDOW);
        }


    }
    //endregion SYSTEM_ALERT_WINDOW

    // region READ_EXTERNAL_STORAGE

    private void requestPermissionReadExternalStorage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

                    new AlertDialog.Builder(this).setTitle(R.string.permission_read_external_title).setMessage(getString(R.string.permission_read_external_explanation))
                            .setPositiveButton(getString(R.string.permission_grant), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ActivityCompat.requestPermissions(SplashScreen.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, CODE_READ_EXTERNAL_STORAGE);
                                }
                            })
                            .create()
                            .show();

                } else {
                    ActivityCompat.requestPermissions(SplashScreen.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, CODE_READ_EXTERNAL_STORAGE);
                }

            }
        }
        else getInitialAppPermissions();

    }
    //endregion READ_EXTERNAL_STORAGE

    // region WRITE_EXTERNAL_STORAGE

    private void requestPermissionWriteExternalStorage() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)){

                new AlertDialog.Builder(this).setTitle(getString(R.string.permission_write_external_title)).setMessage(getString(R.string.permission_write_external_explanation))
                        .setPositiveButton(getString(R.string.permission_grant), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(SplashScreen.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, CODE_WRITE_EXTERNAL_STORAGE);
                            }
                        })
                        .create()
                        .show();

            }
            else {
                ActivityCompat.requestPermissions(SplashScreen.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, CODE_WRITE_EXTERNAL_STORAGE);
            }
        }
    }
    //endregion WRITE_EXTERNAL_STORAGE

    @SuppressLint("SetTextI18n")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case CODE_SYSTEM_ALERT_WINDOW:{
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.SYSTEM_ALERT_WINDOW) == PackageManager.PERMISSION_GRANTED){
                        //WE NOW HAVE THE PERSMISSIONS TO SHOW LAYOUTS IN THE PHONE SCREEN
                    }
                    textViewExtraInfo.setText("");
                    textViewLoadingApp.setText(getString(R.string.ss_confirm_permissions_2));
                }
                else if(STATUS_SYSTEM_ALERT_WINDOW) {
                    textViewExtraInfo.setText(getString(R.string.permission_system_alert_window));
                    textViewLoadingApp.setText(getString(R.string.ss_confirm_permissions_3));
                    STATUS_SYSTEM_ALERT_WINDOW = false;
                }
                getInitialAppPermissions();
                break;
            }
            case CODE_READ_EXTERNAL_STORAGE:{
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                        //WE NOW HAVE THE PERSMISSIONS TO USE MAP LOCATION!
                    }
                    textViewExtraInfo.setText("");
                    textViewLoadingApp.setText(getString(R.string.ss_confirm_permissions_2));
                }
                else if (STATUS_READ_EXTERNAL_STORAGE) {
                    textViewExtraInfo.setText(getString(R.string.permission_read_external));
                    textViewLoadingApp.setText(getString(R.string.ss_confirm_permissions_3));
                    STATUS_READ_EXTERNAL_STORAGE = false;
                }
                getInitialAppPermissions();
                break;
            }
            case CODE_WRITE_EXTERNAL_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        //WE NOW HAVE THE PERSMISSIONS TO USE MAP LOCATION!
                    }
                    textViewExtraInfo.setText("");
                    textViewLoadingApp.setText(getString(R.string.ss_confirm_permissions_2));
                } else if (STATUS_WRITE_EXTERNAL_STORAGE) {
                    textViewExtraInfo.setText(getString(R.string.permission_write_external));
                    textViewLoadingApp.setText(getString(R.string.ss_confirm_permissions_3));
                    STATUS_WRITE_EXTERNAL_STORAGE = false;
                }
                getInitialAppPermissions();
                break;
            }
        }
    }

    //endregion


    private void startMainApp () {
        // Single call to this method
        if(started) return;
        started = true;

        Intent intent = new Intent(SplashScreen.this, WelcomeActivity.class); //send to login;
        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);

        if(auth == null){
            //worksOffline
           // if(worksOffline) intent = new Intent(SplashScreen.this, PrevWaterActivity.class);
            if(worksOffline) intent = new Intent(SplashScreen.this, WelcomeActivity.class);
        }
        else if (auth.getCurrentUser() != null){

            cUser = userManager.getCustomerData();

            if (cUser.getCurrentDevice().equalsIgnoreCase(utils.getDeviceName(SplashScreen.this))) {
                //AUTENTICADO
                //intent = new Intent(SplashScreen.this, PrevWaterActivity.class);
                intent = new Intent(SplashScreen.this, WelcomeActivity.class);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            }
            else if (!cUser.getCurrentDevice().equalsIgnoreCase(utils.getDeviceName(SplashScreen.this))){
                Toast.makeText(this, getString(R.string.text_session_in_other_device), Toast.LENGTH_SHORT).show();
                auth.signOut();
                userManager.removeCustomerData();
            }
        }
        else {
            // Without user
            auth.signOut();
            userManager.removeCustomerData();
        }

        startActivity(intent);

        Log.i("APPLICATION", "SPLASH SCREEN ACTIVITY DONE!");
        finish();
    }

    //region TODO: CUSTOM CALLBACK

    @Override
    public void onReturnCallback(boolean status, String message, int code) {}

    @Override
    public void onYesResponse(String code, String message) {
        if (code.equalsIgnoreCase(UPDATE_APP_FORCED) || code.equalsIgnoreCase(UPDATE_APP)){
            //go to playstore
            final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
            } catch (android.content.ActivityNotFoundException anfe) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
            }
            System.exit(0);
        }
        else if (code.equalsIgnoreCase(DISABLED_APP)){
            //close app
            System.exit(0);
        }
    }

    @Override
    public void onNoResponse(String code, String message) {
        if (code.equalsIgnoreCase(UPDATE_APP_FORCED)){
            //close app
            System.exit(0);
        }
        else if(code.equalsIgnoreCase(UPDATE_APP)){
            // Just continue, ill update the app later...
            updateLater = true;
            setAppInformation();
        }
    }
 
    //endregion

    //region FIREBASE CONNECTION STATUS

    Handler setApp = null;
    Runnable run = this::setAppInformation;

    //@Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        // La primera vez devuelve FALSE. Si esta conectado a internet primero regresa FALSE Y despues TRUE (conectado)
        try{
            boolean connected = dataSnapshot.getValue(Boolean.class);
            if(connected) {
                Log.i(TAG_CONNECTED, "ESTA CONECTADO A INTERNET");
                weHaveInternet = true;
            }
            else {
                //OFFLINE
                Log.i(TAG_CONNECTED, "SE DESCONECTO EL INTERNET");
                weHaveInternet = false;
            }
        }
        catch (Exception ignore){
            //OFFLINE
            Log.i(TAG_CONNECTED, "SE DESCONECTO EL INTERNET por exception");
            weHaveInternet = false;
        }

        if(setApp != null) setApp.removeCallbacks(run);
        setApp = new Handler();
        setApp.postDelayed(run,1000);
    }

    //@Override
    public void onCancelled(@NonNull DatabaseError databaseError) {
        // do nothing here
    }


    //endregion FIREBASE CONNECTION STATUS


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(iaChecker != null) iaChecker.removeInternetConnectivityChangeListener(this);
    }


    boolean getAppInfoAgain = true; // Inicia el proceso de obtener la informacion de la app si es que fue detenido. Tambien corre la primera vez que tenemos llamada al callback onInternetConnectivityChanged()

    @Override
    public void onInternetConnectivityChanged(boolean isConnected) {
        weHaveInternet = isConnected;
        Log.i(TAG_CONNECTED, isConnected ? "OICC: Tienes conexion a INTERNET" : "OICC: SE DESCONECTO A INTERNET");

        if(!worksOffline && !weHaveInternet) {
            textViewLoadingApp.setText(getString(R.string.ss_not_connected));
            return;
        }

        // si se desconecta y estabamos obteniendo datos, debemos cancelar esa peticion y entrar offline
        //wehaveInternet && !worksoffline
        if(getAppInfoAgain || (!weHaveInternet && worksOffline)){
            Log.i(TAG_CONNECTED, "OICC: SetAppInformation() invocado! (getAppInfoAgain = " + getAppInfoAgain + ") (weHaveInternet = " + weHaveInternet + ") (worksOffline = " + worksOffline + ")");
            getAppInfoAgain = false;
            setAppInformation();
            // - La primera vez que se llama este callback siempre iniciaremos setAppInformation()
            // - Despues puede volver a ser llamado si antes (en el proceso de obtener la info) el usuario se desconecto, y
            // recupero el flujo de internet, entonces ahi llamamos denuevo el metodo para reanudar el inicio de la app.
        }

    }

}
