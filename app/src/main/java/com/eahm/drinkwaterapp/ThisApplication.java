package com.eahm.drinkwaterapp;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.multidex.MultiDex;

import com.crashlytics.android.Crashlytics;
import com.eahm.drinkwaterapp.Models.News;
import com.google.android.gms.ads.MobileAds;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.ArrayList;

import io.fabric.sdk.android.Fabric;

public class ThisApplication extends Application {

    private static ThisApplication instance;

    //region General Variables
    private ArrayList<News> newsList = new ArrayList<>();
    private String languagueCountry = "Español";

   //region GET AND SET
    public String getLanguagueCountry() {
        return languagueCountry;
    }

    public void setLanguageCountry(String languagueCountry) {
        this.languagueCountry = languagueCountry;
    }

    public ArrayList<News> getNewsList() {
        return newsList;
    }

    public void setNewsList(ArrayList<News> newsList) {
        this.newsList = newsList;
    }

    //endregion GET AND SET

    public void clearNewsList() {
        if(newsList != null && newsList.size() > 0) newsList.clear();
    }

    //endregion General Variables


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        if (!BuildConfig.DEBUG) {
            Fabric.with(this, new Crashlytics());
            MobileAds.initialize(this,getResources().getString(R.string.ads_app_id));
        }

        new Prefs.Builder()
                .setContext(this)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(getPackageName())
                .setUseDefaultSharedPreference(true)
                .build();

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            //EMOJIS
            /*FontRequest fontRequest = new FontRequest(
                    "com.example.fontprovider",
                    "com.example",
                    "emoji compat Font Query",
                    CERTIFICATES);

            EmojiCompat.Config config = new FontRequestEmojiCompatConfig(this, fontRequest);
            EmojiCompat.init(config);
*/
        }

        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                activity.setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });



        /*FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user != null){
            FirebaseDatabase.getInstance().getReference().child("users").child("customer").child(user.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        if (dataSnapshot.hasChild("name")){
                            Toast.makeText(ThisApplication.this, dataSnapshot.child("name").getValue().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }*/
    }

    public static synchronized ThisApplication getInstance(){
        return instance;
    }

    private static boolean appActivityVisible;

    public static boolean isActivityVisible(){
        return  appActivityVisible;
    }

    public static void onResume(){
        appActivityVisible = true;
    }

    public static void onPause(){
        appActivityVisible = false;
    }

}
