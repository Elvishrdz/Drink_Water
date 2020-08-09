package com.eahm.drinkwaterapp.Models;

import android.content.ContentValues;

import com.eahm.drinkwaterapp.DB.RecordsContract.RecordsEntry;

public class RecordModel {

    private int id;
    private long timestamp;
    private float drinkAmount;
    private int statusCode;

    /*
    1 = bebio dentro de la app
    2 = bebio desde la notificacion
    3 = saltado Entro a la app desde la notificacion pero no bebio
    4 = saltado Desde la notificacion
    5 = saltado Notificacion cerrada
    6 = saltado El cronometro termino y es hora de beber pero salio.
     */

    public RecordModel() {
    }

    public RecordModel(long timestamp, float drinkAmount, int statusCode) {
        this.id = id;
        this.timestamp = timestamp;
        this.drinkAmount = drinkAmount;
        this.statusCode = statusCode;
    }

    //region GET AND SET

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public float getDrinkAmount() {
        return drinkAmount;
    }

    public void setDrinkAmount(float drinkAmount) {
        this.drinkAmount = drinkAmount;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }


    //endregion GET AND SET

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(RecordsEntry.COLUMN_NAME_TIMESTAMP, timestamp);
        values.put(RecordsEntry.COLUMN_NAME_DRINK_AMOUNT, String.valueOf(drinkAmount));
        values.put(RecordsEntry.COLUMN_NAME_STATUS_CODE, String.valueOf(statusCode));

        return values;
    }
}
