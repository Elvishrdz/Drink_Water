package com.eahm.drinkwaterapp.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.eahm.drinkwaterapp.Models.RecordModel;
import com.eahm.drinkwaterapp.R;
import com.eahm.drinkwaterapp.Utils.Utils;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

public class WaterAdapter extends RecyclerView.Adapter<WaterAdapter.ViewHolder> {

    private static final String TAG_REPORT = "TAG_REPORT";
    private Context context;
    private List<RecordModel> dataset = new ArrayList<>();
    Utils utils = new Utils();

    public WaterAdapter(Context context) {
        this.context = context;
    }

    public void add(RecordModel newRecord){
        if(newRecord == null) return;
        dataset.add(newRecord);
    }

    public void addAtPosition(int position, RecordModel newRecord){
        if(newRecord == null) return;

        Log.i("DATASNAPSHOT_RECEIVED", position + " - " + dataset.size());
        if( position < 0 || ( position != 0 && position > (dataset.size() - 1 ))) return;


        dataset.add(position, newRecord);
    }

    public void remove(int position){
        if(dataset.isEmpty() || position < 0 || position > (dataset.size() - 1)) return;
        dataset.remove(position);
    }

    public void removeAll(){
        if(dataset != null && !dataset.isEmpty()) dataset.clear();
    }


    public void overrideList(List<RecordModel> value){
        if(value == null || value.size() <= 0) return;
        if(dataset.size() > 0) dataset.clear();
        dataset = value;
    }

    int style = 0;
    public void setCustomStyle(int code){
        style = code;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        CardView card = (CardView) LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_record, viewGroup, false);
        return new WaterAdapter.ViewHolder(card);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {

        RecordModel recordModel = dataset.get(position);
        String timestamp = String.valueOf(recordModel.getTimestamp());

        Log.i(TAG_REPORT, "timestamp: " + timestamp);

        DateTime datetime = Utils.getDateTimeByTimestamp(timestamp);

        String stringDate = Utils.getDayName(datetime.getDayOfWeek(),context) + " " +
                            datetime.getDayOfMonth() + " " + context.getResources().getString(R.string.text_of) + " " +
                            Utils.getMonthName(datetime.getMonthOfYear(), context) + " " +
                            datetime.getYear();
        Log.i(TAG_REPORT, "string date: " + stringDate);

        //String stringTime = datetime.getHourOfDay() + ":" + datetime.getMinuteOfHour() + " " + datetime.getSecondOfMinute();
        Log.i(TAG_REPORT, "get some: year: " + datetime.getYear() + " hour: " + datetime.getHourOfDay() + " minute: " + datetime.getMinuteOfHour() + " seconds: " + datetime.getSecondOfMinute());

        String stringTime = utils.getFormattedTimeAMPM(datetime.getHourOfDay(), datetime.getMinuteOfHour(), datetime.getSecondOfMinute(), true,true);
        Log.i(TAG_REPORT, "string Time: " + stringTime);

        viewHolder.textViewDate.setText(stringDate);
        viewHolder.textViewTime.setText(stringTime);

        float drinkValue = recordModel.getDrinkAmount();
        String amountString = String.valueOf(drinkValue);

        if(drinkValue % 1 == 0){
            long integer = (long) drinkValue;
            amountString = String.valueOf(integer);
        }

        final String amountText = "+" + amountString + " " + context.getResources().getString(R.string.text_vol_unit_mililiters);

        /*
        1 = bebio dentro de la app
        2 = bebio desde la notificacion
        3 = saltado, entro a la app desde la notificacion pero no bebio
        4 = saltado desde la notificacion
        5 = saltado notificacion cerrada
        6 = saltado El cronometro termino y es hora de beber pero salio.
         */

        switch (style){
            case 0: // TODAY
                viewHolder.textViewDate.setText("");
                viewHolder.textViewDate.setBackgroundColor(context.getResources().getColor(R.color.blue_water));
                viewHolder.textViewTime.setTextColor(context.getResources().getColor(R.color.dark_purple));
                break;
            case 1: // YESTERDAY
                viewHolder.textViewDate.setText(stringDate);
                viewHolder.textViewDate.setBackgroundColor(context.getResources().getColor(R.color.green));
                viewHolder.textViewTime.setTextColor(context.getResources().getColor(R.color.green));

                break;
        }

        switch (recordModel.getStatusCode()){
            case 1:
                //region BEBIO DENTRO DE LA APP
                int random = utils.getRandomNumber(1,10);

                if(random > 5){
                    //Vista: BEBER agua desde la app sin imagen y sin texto
                    viewHolder.imageViewStatus.setVisibility(View.VISIBLE);
                    viewHolder.textViewMessage.setVisibility(View.GONE);
                    viewHolder.textViewMessage.setTextColor(context.getResources().getColor(R.color.grey));
                    viewHolder.textViewMessage.setText("");
                    viewHolder.textViewAmount.setVisibility(View.VISIBLE);
                    viewHolder.textViewAmount.setText(amountText);
                    viewHolder.textViewDate.setVisibility(View.VISIBLE);
                    viewHolder.cardViewHolder.setCardBackgroundColor(context.getResources().getColor(R.color.white));
                }
                else {
                    //vista: BEBER agua desde la app con IMAGEN y mensaje
                    viewHolder.imageViewStatus.setVisibility(View.VISIBLE);
                    viewHolder.textViewMessage.setVisibility(View.GONE);
                    viewHolder.textViewMessage.setTextColor(context.getResources().getColor(R.color.grey));
                    viewHolder.textViewMessage.setText("");
                    viewHolder.textViewAmount.setVisibility(View.VISIBLE);
                    viewHolder.textViewAmount.setText(amountText);
                    viewHolder.textViewDate.setVisibility(View.VISIBLE);
                    viewHolder.cardViewHolder.setCardBackgroundColor(context.getResources().getColor(R.color.white));
                }
                //endregion BEBIO DENTRO DE LA APP
                break;
            case 2:
                //region BEBER agua desde la notificacion
                viewHolder.imageViewStatus.setVisibility(View.VISIBLE);
                viewHolder.textViewMessage.setVisibility(View.VISIBLE);
                viewHolder.textViewMessage.setTextColor(context.getResources().getColor(R.color.green));
                viewHolder.textViewMessage.setText(context.getString(R.string.text_notification_case_02));
                viewHolder.textViewAmount.setVisibility(View.VISIBLE);
                viewHolder.textViewAmount.setText(amountText);
                viewHolder.textViewDate.setVisibility(View.VISIBLE);
                viewHolder.cardViewHolder.setCardBackgroundColor(context.getResources().getColor(R.color.white));
                //endregion BEBER agua desde la notificacion
                break;
            case 3:
                //region SALTAR entro a la notificacion y salio de la app sin beber
                viewHolder.imageViewStatus.setVisibility(View.GONE);
                viewHolder.textViewMessage.setVisibility(View.VISIBLE);
                viewHolder.textViewMessage.setTextColor(context.getResources().getColor(R.color.white));
                viewHolder.textViewMessage.setText(context.getString(R.string.text_notification_case_03));
                viewHolder.textViewAmount.setVisibility(View.GONE);
                viewHolder.textViewAmount.setText(amountText);
                viewHolder.textViewDate.setVisibility(View.GONE);
                //viewHolder.textViewTime.setTextColor(context.getResources().getColor(R.color.white));
                viewHolder.cardViewHolder.setCardBackgroundColor(context.getResources().getColor(R.color.grey_3));
                //endregion SALTAR entro a la notificacion y salio de la app sin beber
                break;
            case 4:
                //region SALTAR desde notificacion
                viewHolder.imageViewStatus.setVisibility(View.GONE);
                viewHolder.textViewMessage.setVisibility(View.VISIBLE);
                viewHolder.textViewMessage.setTextColor(context.getResources().getColor(R.color.black));
                viewHolder.textViewMessage.setText(context.getString(R.string.text_notification_case_04));
                viewHolder.textViewAmount.setVisibility(View.GONE);
                viewHolder.textViewDate.setVisibility(View.GONE);
                //viewHolder.textViewTime.setTextColor(context.getResources().getColor(R.color.white));
                viewHolder.cardViewHolder.setCardBackgroundColor(context.getResources().getColor(R.color.grey_2));
                //endregion SALTAR desde notificacion
                break;
            case 5:
                //region SALTAR notificacion fue cerrada, 5min de tiempo extra
                viewHolder.imageViewStatus.setVisibility(View.GONE);
                viewHolder.textViewMessage.setVisibility(View.VISIBLE);
                viewHolder.textViewMessage.setTextColor(context.getResources().getColor(R.color.dark_purple));
                viewHolder.textViewMessage.setText(context.getString(R.string.text_notification_case_05));
                viewHolder.textViewAmount.setVisibility(View.GONE);
                viewHolder.textViewAmount.setText(amountText);
                viewHolder.textViewDate.setVisibility(View.GONE);
                //viewHolder.textViewTime.setTextColor(context.getResources().getColor(R.color.white));
                viewHolder.cardViewHolder.setCardBackgroundColor(context.getResources().getColor(R.color.grey));
                //endregion SALTAR notificacion fue cerrada, 5min de tiempo extra
                break;
            case 6:
                //region SALTAR el cronometro termino y es hora de beber pero salio de la app
                viewHolder.imageViewStatus.setVisibility(View.GONE);
                viewHolder.textViewMessage.setVisibility(View.VISIBLE);
                viewHolder.textViewMessage.setTextColor(context.getResources().getColor(R.color.yellow));
                viewHolder.textViewMessage.setText(context.getString(R.string.text_notification_case_06));
                viewHolder.textViewAmount.setVisibility(View.GONE);
                viewHolder.textViewAmount.setText(amountText);
                viewHolder.textViewDate.setVisibility(View.GONE);
                //viewHolder.textViewTime.setTextColor(context.getResources().getColor(R.color.white));
                viewHolder.cardViewHolder.setCardBackgroundColor(context.getResources().getColor(R.color.grey_4));
                //endregion SALTAR el cronometro termino y es hora de beber pero salio de la app
                break;

        }

        //set icon with time:
        //Calendar now = Calendar.getInstance();
        // int currentSeconds = ((now.get(Calendar.HOUR_OF_DAY) * 60) + now.get(Calendar.MINUTE)) * 60;

        int currentSeconds =  ((datetime.getHourOfDay() * 60) + datetime.getMinuteOfHour()) * 60;
        Log.i(TAG_REPORT, "currentSeconds: " + currentSeconds);

        //<>
        int startMadrugada = ((1  * 60) + 0) * 60; //1am
        int startManana =    ((5  * 60) + 0) * 60; //5am
        int startDay =       ((10 * 60) + 0) * 60; //10am
        int startTarde =     ((15 * 60) + 0) * 60; //3pm
        int startNight =     ((19 * 60) + 0) * 60; //7pm

        // de 01:00 a 04:59 - Madrugada
        if(currentSeconds >= startMadrugada && currentSeconds < startManana){
            viewHolder.imageViewStatus.setImageResource(R.drawable.icon_r_madrugada);
        }
        // de 05:00 a 09:59 - Mañana
        else if(currentSeconds >= startManana && currentSeconds < startDay){
            viewHolder.imageViewStatus.setImageResource(R.drawable.icon_r_manana);
        }
        // de 10:00 a 02:59 - Dia
        else if(currentSeconds >= startDay && currentSeconds < startTarde){
            viewHolder.imageViewStatus.setImageResource(R.drawable.icon_r_dia);
        }
        // de 03:00 a 06:59 - Tarde
        else if(currentSeconds >= startTarde && currentSeconds < startNight){
            viewHolder.imageViewStatus.setImageResource(R.drawable.icon_r_tarde);
        }
        // de 07:00 a 00:59 - Tarde
        else {
            //if(currentSeconds >= startNight && currentSeconds < dayEnd){
            viewHolder.imageViewStatus.setImageResource(R.drawable.icon_r_noche);
        }

    }

    @Override
    public int getItemCount() {
        return dataset != null ? dataset.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardViewHolder;
        TextView textViewDate;
        TextView textViewTime;
        TextView textViewAmount;
        TextView textViewMessage;
        ImageView imageViewStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardViewHolder = itemView.findViewById(R.id.cardViewHolder);

            textViewDate = itemView.findViewById(R.id.textViewDate);
            textViewTime = itemView.findViewById(R.id.textViewTime);
            textViewAmount = itemView.findViewById(R.id.textViewAmount);
            textViewMessage = itemView.findViewById(R.id.textViewMessage);
            imageViewStatus = itemView.findViewById(R.id.imageViewStatus);

        }
    }
}
