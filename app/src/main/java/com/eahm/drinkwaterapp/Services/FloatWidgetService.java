package com.eahm.drinkwaterapp.Services;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.eahm.drinkwaterapp.R;
import com.eahm.drinkwaterapp.Utils.Utils;

public class FloatWidgetService extends Service {

    private WindowManager mWindowManager;
    private View mFloatingWidget;
    private WindowManager.LayoutParams params;

    public FloatWidgetService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mFloatingWidget = LayoutInflater.from(this).inflate(R.layout.layout_floating_widget, null);
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.TOP | Gravity.START;
        params.x = 0;
        params.y = 100;

        mWindowManager.addView(mFloatingWidget, params);

        //region FIND VIEWS
        View root = mFloatingWidget.findViewById(R.id.root);

        final View layoutButtonCongratulations = mFloatingWidget.findViewById(R.id.layoutButtonCongratulations);
        final View layoutCongratulations = mFloatingWidget.findViewById(R.id.layoutCongratulations);

        ImageView imageViewIcon = mFloatingWidget.findViewById(R.id.imageViewIcon);
        ImageView closeButtonCollapsed = mFloatingWidget.findViewById(R.id.imageButtonBCClose);


        final CardView cardViewCongratulations = mFloatingWidget.findViewById(R.id.cardViewCongratulations);
        ImageView closeButtonExpanded = mFloatingWidget.findViewById(R.id.imageButtonLCClose);
        ImageView imageViewTrophy = mFloatingWidget.findViewById(R.id.imageViewTrophy);
        TextView textViewTitle = mFloatingWidget.findViewById(R.id.textViewTitle);
        TextView textViewDescription = mFloatingWidget.findViewById(R.id.textViewDescription);
        TextView textViewAppName = mFloatingWidget.findViewById(R.id.textViewAppName);
        //endregion FIND VIEWS

        //region LISTENERS

        closeButtonCollapsed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopSelf();
            }
        });
        closeButtonExpanded.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //collapsedView.setVisibility(View.VISIBLE);
                //expandedView.setVisibility(View.GONE);
                closeButtonCollapsed.callOnClick();
            }
        });

        root.setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialX = params.x;
                        initialY = params.y;
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        return true;
                    case MotionEvent.ACTION_UP:
                        int Xdiff = (int) (event.getRawX() - initialTouchX);
                        int Ydiff = (int) (event.getRawY() - initialTouchY);
                        if (Xdiff < 10 && Ydiff < 10) {
                            if (isViewCollapsed()) {
                                if (layoutButtonCongratulations.getVisibility() == View.VISIBLE) {

                                    layoutButtonCongratulations.setVisibility(View.GONE);


                                    params = new WindowManager.LayoutParams(
                                            WindowManager.LayoutParams.MATCH_PARENT,
                                            WindowManager.LayoutParams.WRAP_CONTENT,
                                            WindowManager.LayoutParams.TYPE_PHONE,
                                            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                                            PixelFormat.TRANSLUCENT);

                                    params.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
                                    params.x = 0;
                                    params.y = 0;

                                    mWindowManager.updateViewLayout(mFloatingWidget, params);

                                    layoutCongratulations.setVisibility(View.VISIBLE);
                                }
                            }

                        }
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        params.x = initialX + (int) (event.getRawX() - initialTouchX);
                        params.y = initialY + (int) (event.getRawY() - initialTouchY);
                        mWindowManager.updateViewLayout(mFloatingWidget, params);
                        return true;
                }
                return false;
            }
        });

        //endregion LISTENERS

        layoutButtonCongratulations.setVisibility(View.VISIBLE);
        layoutCongratulations.setVisibility(View.GONE);

        //region CUSTOM STYLE
        String description = getString(R.string.text_congratulations_01) + " " + new Utils().getFinalProgressString(false, this) + " " + getString(R.string.text_congratulations_02) + " " + new Utils().getGoalString(false, this) + " " + getString(R.string.text_congratulations_03);

        int random = new Utils().getRandomNumber(1, 6);
        switch (random){
            case 1:
                imageViewIcon.setImageDrawable(getResources().getDrawable(R.drawable.icon_glass_of_water_01));
                cardViewCongratulations.setCardBackgroundColor(getResources().getColor(R.color.blue_water));
                textViewAppName.setTextColor(getResources().getColor(R.color.white));
                imageViewTrophy.setImageResource(R.drawable.icon_trophy_01);
                textViewTitle.setTextColor(getResources().getColor(R.color.white));
                textViewTitle.setText(getString(R.string.text_congratulations));
                textViewDescription.setTextColor(getResources().getColor(R.color.black));
                textViewDescription.setText(description);
                break;
            case 2:
                imageViewIcon.setImageDrawable(getResources().getDrawable(R.drawable.icon_glass_of_water_01));
                cardViewCongratulations.setCardBackgroundColor(getResources().getColor(R.color.orange));
                textViewAppName.setTextColor(getResources().getColor(R.color.white));
                imageViewTrophy.setImageResource(R.drawable.icon_trophy_02);
                textViewTitle.setTextColor(getResources().getColor(R.color.white));
                textViewTitle.setText(getString(R.string.text_congratulations));
                textViewDescription.setTextColor(getResources().getColor(R.color.black));
                textViewDescription.setText(description);
                break;
            case 3:
                imageViewIcon.setImageDrawable(getResources().getDrawable(R.drawable.icon_glass_of_water_01));
                cardViewCongratulations.setCardBackgroundColor(getResources().getColor(R.color.dark_purple));
                textViewAppName.setTextColor(getResources().getColor(R.color.white));
                imageViewTrophy.setImageResource(R.drawable.icon_trophy_03);
                textViewTitle.setTextColor(getResources().getColor(R.color.white));
                textViewTitle.setText(getString(R.string.text_congratulations));
                textViewDescription.setTextColor(getResources().getColor(R.color.black));
                textViewDescription.setText(description);
                break;
            case 4:
                imageViewIcon.setImageDrawable(getResources().getDrawable(R.drawable.icon_glass_of_water_01));
                cardViewCongratulations.setCardBackgroundColor(getResources().getColor(R.color.green_2));
                textViewAppName.setTextColor(getResources().getColor(R.color.white));
                imageViewTrophy.setImageResource(R.drawable.icon_trophy_04);
                textViewTitle.setTextColor(getResources().getColor(R.color.white));
                textViewTitle.setText(getString(R.string.text_congratulations));
                textViewDescription.setTextColor(getResources().getColor(R.color.black));
                textViewDescription.setText(description);
                break;
            case 5:
                imageViewIcon.setImageDrawable(getResources().getDrawable(R.drawable.icon_glass_of_water_01));
                cardViewCongratulations.setCardBackgroundColor(getResources().getColor(R.color.black));
                textViewAppName.setTextColor(getResources().getColor(R.color.white));
                imageViewTrophy.setImageResource(R.drawable.icon_trophy_05);
                textViewTitle.setTextColor(getResources().getColor(R.color.white));
                textViewTitle.setText(getString(R.string.text_congratulations));
                textViewDescription.setTextColor(getResources().getColor(R.color.grey));
                textViewDescription.setText(description);
                break;
            case 6:
                imageViewIcon.setImageDrawable(getResources().getDrawable(R.drawable.icon_glass_of_water_01));
                cardViewCongratulations.setCardBackgroundColor(getResources().getColor(R.color.colorAccent));
                textViewAppName.setTextColor(getResources().getColor(R.color.white));
                imageViewTrophy.setImageResource(R.drawable.icon_trophy_06);
                textViewTitle.setTextColor(getResources().getColor(R.color.white));
                textViewTitle.setText(getString(R.string.text_congratulations));
                textViewDescription.setTextColor(getResources().getColor(R.color.white));
                textViewDescription.setText(description);
                break;
        }
        //endregion CUSTOM STYLE

    }


    private boolean isViewCollapsed() {
        return mFloatingWidget == null || mFloatingWidget.findViewById(R.id.layoutButtonCongratulations).getVisibility() == View.VISIBLE;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mFloatingWidget != null) mWindowManager.removeView(mFloatingWidget);
    }

}