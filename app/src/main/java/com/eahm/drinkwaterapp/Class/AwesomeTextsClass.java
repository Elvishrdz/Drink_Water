package com.eahm.drinkwaterapp.Class;

import android.content.Context;

import com.eahm.drinkwaterapp.R;
import com.eahm.drinkwaterapp.Utils.Utils;

import java.util.Calendar;

public class AwesomeTextsClass {

    int currentHourOfDay = 0;
    int currentMinute = 0;

    private void updateCurrentTime(){
        Calendar calendar = Calendar.getInstance();
        currentHourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        currentMinute = calendar.get(Calendar.MINUTE);
    }

    //region GET NICE TEXTS
    public String getCoutdownTextDone(Context context){

        String message = "";
        //switch random text here!

        int randomPos1 = new Utils().getRandomNumber(1, 15);   //This gives a random integer between 7 (inclusive) and 0 (exclusive).
        switch (randomPos1){
            case 1:
                message = message + "\n" + context.getString(R.string.coutdown_done_text_01);
                break;
            case 2:
                message = message + "\n" + context.getString(R.string.coutdown_done_text_02);
                break;
            case 3:
                message = message + "\n" + context.getString(R.string.coutdown_done_text_03);
                break;
            case 4:
                message = message + "\n" + context.getString(R.string.coutdown_done_text_04);
                break;
            case 5:
                message = message + "\n" + context.getString(R.string.coutdown_done_text_05);
                break;
            case 6:
                message = message + "\n" + context.getString(R.string.coutdown_done_text_06);
                break;
            case 7:
                message = message + "\n" + context.getString(R.string.coutdown_done_text_07);
                break;
            case 8:
                message = message + "\n" + context.getString(R.string.coutdown_done_text_08);
                break;
            case 9:
                message = message + "\n" + context.getString(R.string.coutdown_done_text_09);
                break;
            case 10:
                message = message + "\n" + context.getString(R.string.coutdown_done_text_10);
                break;
            case 11:
                message = message + "\n" + context.getString(R.string.coutdown_done_text_11);
                break;
            case 12:
                message = message + "\n" + context.getString(R.string.coutdown_done_text_12);
                break;
            case 13:
                message = message + "\n" + context.getString(R.string.coutdown_done_text_13);
                break;
            default:
                    message = message + "\n" + context.getString(R.string.coutdown_done_text_default);
                    break;
        }

        return message;
    }

    public String getTitle(int category, Context context) {
        //<>
        String title = "";
        //switch random text here!

        switch (category){
            case 1:
                //region TITULOS MAÑANEROS
                int randomPos1 = new Utils().getRandomNumber(1, 9);
                switch (randomPos1){
                    case 1:
                        title = context.getString(R.string.title_start_01);
                        break;
                    case 2:
                        title = context.getString(R.string.title_start_02);
                        break;
                    case 3:
                        title = context.getString(R.string.title_start_03);
                        break;
                    case 4:
                        title = context.getString(R.string.title_start_04);
                        break;
                    case 5:
                        title = context.getString(R.string.title_start_05);
                        break;
                    case 6:
                        title = context.getString(R.string.title_start_06);
                        break;
                    case 7:
                        title = context.getString(R.string.title_start_07);
                        break;
                    default:
                        title = context.getString(R.string.title_start_default);
                        break;
                }
                //endregion TITULOS MAÑANEROS
                break;
            case 2:
                //region TITULOS PARA LAST ALARM
                int randomPos2 = new Utils().getRandomNumber(1, 5);
                switch (randomPos2){
                    case 1:
                        title = context.getString(R.string.title_end_01);
                        break;
                    case 2:
                        title = context.getString(R.string.title_end_02);
                        break;
                    case 3:
                        title = context.getString(R.string.title_end_03);
                        break;
                    default:
                        title = context.getString(R.string.title_end_default);
                        break;
                }
                //endregion TITULOS PARA LAST ALARM
                break;
            case 3:
                //region TITULOS DE LA ALARMA
                int randomPos3 = new Utils().getRandomNumber(1, 5);
                switch (randomPos3){
                    case 1:
                        title = context.getString(R.string.title_drink_time_01);
                        break;
                    case 2:
                        title = context.getString(R.string.title_drink_time_03);
                        break;
                    case 3:
                        title = context.getString(R.string.title_drink_time_04);
                        break;
                    default:
                        title = context.getString(R.string.title_drink_time_default);
                        break;
                }
                //endregion TITULOS DE LA ALARMA
                break;
        }

        return title;

    }

    public String getText(int category, Context context){
        //<>
        String contentText = "";

        switch (category){
            case 1:
                //region TEXTO MAÑANEROS
                int randomPos1 = new Utils().getRandomNumber(1, 6);
                switch (randomPos1){
                    case 1:
                        String amount = "";
                        float goalValue = new SettingsClass().getGoalValue();
                        if(goalValue % 1 == 0){
                            long integer = (long) goalValue;
                            amount = integer + "mL";
                        }
                        else amount = goalValue + "mL";

                        if(amount.isEmpty()){
                            contentText = context.getString(R.string.text_start_01);
                        }
                        else contentText = context.getString(R.string.text_start_02) + " " + amount + " " + context.getString(R.string.text_start_03);
                        break;
                    case 2:
                        contentText = context.getString(R.string.text_start_04);
                        break;
                    case 3:
                        contentText = context.getString(R.string.text_start_05);
                        break;
                    case 4:
                        contentText = context.getString(R.string.text_start_06);
                        break;
                    default:
                        contentText = context.getString(R.string.text_start_default);
                        break;
                }
                //endregion TEXTO MAÑANEROS
                break;
            case 2:
                //region TEXTO PARA LAST ALARM
                contentText = "";
                int randomPos2 = new Utils().getRandomNumber(1, 4);
                switch (randomPos2){
                    case 1:
                        contentText += context.getString(R.string.text_end_01) + " " + new Utils().getFinalProgressString(false, context) + context.getString(R.string.text_end_02);
                        break;
                    case 2:
                        contentText += context.getString(R.string.text_end_03) + " " + new Utils().getFinalProgressString(true, context) + " " + context.getString(R.string.text_end_04);
                        break;
                    default:
                        contentText += context.getString(R.string.text_end_default);
                        break;
                }
                //endregion TEXTO PARA LAST ALARM
                break;
            case 3:
                //region TEXTO DE LA ALARMA
                int randomPos3 = new Utils().getRandomNumber(1, 5);
                switch (randomPos3){
                    case 1:
                        contentText = context.getString(R.string.text_drink_time_01);
                        break;
                    case 2:
                        contentText = context.getString(R.string.text_drink_time_02);
                        break;
                    case 3:
                        contentText = context.getString(R.string.text_drink_time_03);
                        break;
                    default:
                        contentText = context.getString(R.string.text_drink_time_default);
                        break;
                }
                //endregion TEXTO DE LA ALARMA
                break;
        }

        return contentText;
    }

    public String getBigText(int category, Context context){
        //<>
        updateCurrentTime();

        String bigText =  "";

        switch (category){
            case 1:
                //region TEXTO MAÑANEROS
                int randomPos1 = new Utils().getRandomNumber(1, 9);
                switch (randomPos1){
                    case 1:
                        bigText = context.getString(R.string.big_text_start_01);
                        break;
                    case 2:
                        bigText = context.getString(R.string.big_text_start_02);
                        break;
                    case 3:
                        bigText = context.getString(R.string.big_text_start_03);
                        break;
                    case 4:
                        bigText = context.getString(R.string.big_text_start_04);
                        break;
                    case 5:
                        bigText = context.getString(R.string.big_text_start_05);
                        break;
                    case 6:
                        bigText = context.getString(R.string.big_text_start_06);
                        break;
                    case 7:
                        bigText = context.getString(R.string.big_text_start_07);
                        break;
                    default:
                        bigText = context.getString(R.string.big_text_start_default);
                        break;
                }
                //endregion TEXTO MAÑANEROS
                break;
            case 2:
                //region TEXTO PARA LAST ALARM
                String today = new Utils().getTodayString(context);
                bigText = today + "\n";
                if(!new SettingsClass().isGoalReached()){
                    int randomPos2 = new Utils().getRandomNumber(1, 5);
                    switch (randomPos2){
                        case 1:
                            bigText += context.getString(R.string.big_text_end_01) + " " + new Utils().getFinalProgressString(false, context) + "\n" + context.getString(R.string.big_text_end_02);
                            break;
                        case 2:
                            bigText += new Utils().getFinalProgressString(false, context) + " " + context.getString(R.string.big_text_end_03);
                            break;
                        case 3:
                            bigText += context.getString(R.string.big_text_end_04);
                            break;
                        default:
                            bigText += context.getString(R.string.big_text_end_default_01) + " " + new Utils().getFinalProgressString(true, context) + " " + context.getString(R.string.big_text_end_default_02);
                            break;
                    }
                }
                else {
                    int randomPos2 = new Utils().getRandomNumber(1, 5);
                    switch (randomPos2){
                        case 1:
                            bigText += context.getString(R.string.big_text_end_goal_reached_01) + " " + new Utils().getFinalProgressString(false, context) + " " + context.getString(R.string.big_text_end_goal_reached_02);
                            break;
                        case 2:
                            bigText += context.getString(R.string.big_text_end_goal_reached_03) + " " + new Utils().getFinalProgressString(false, context) + " " + context.getString(R.string.big_text_end_goal_reached_04);
                            break;
                        case 3:
                            bigText += context.getString(R.string.big_text_end_goal_reached_05);
                            break;
                        default:
                            bigText += context.getString(R.string.big_text_end_goal_reached_default_01) + " " + new Utils().getFinalProgressString(true, context) + " " + context.getString(R.string.big_text_end_goal_reached_default_02);
                            break;
                    }
                }


                //endregion TEXTO PARA LAST ALARM
                break;
            case 3:
                //region TEXTO DE LA ALARMA
                int randomPos3 = new Utils().getRandomNumber(1, 8);
                switch (randomPos3){
                    case 1:
                        bigText = context.getString(R.string.big_text_drink_time_01);
                        break;
                    case 2:
                        bigText= context.getString(R.string.big_text_drink_time_02);
                        break;
                    case 3:
                        bigText = context.getString(R.string.big_text_drink_time_03);
                        break;
                    case 4:
                        bigText = context.getString(R.string.big_text_drink_time_04);
                        break;
                    case 5:
                        bigText =  context.getString(R.string.big_text_drink_time_05);
                        break;
                    case 6:
                        bigText =  context.getString(R.string.big_text_drink_time_06);
                        break;
                    case 7:
                        bigText = context.getString(R.string.big_text_drink_time_07);
                        break;
                    default:
                        bigText = context.getString(R.string.big_text_drink_time_default);
                        break;
                }
                //endregion TEXTO DE LA ALARMA
                break;
        }

        /*if (currentHourOfDay >= 5 && currentHourOfDay <= 6){
            bigText = "Un vaso de agua tan temprano ayuda a refrescar y energizar tu cuerpo, bebelo con calma";
        }
        else if(currentHourOfDay > 6){
            bigText = "Recuerda un vaso ahora para lograr tu meta diaria";
        }*/

        return bigText;


    }

    //endregion GET NICE TEXTS
}
