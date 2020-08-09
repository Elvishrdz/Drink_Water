package com.eahm.drinkwaterapp.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.eahm.drinkwaterapp.DB.RecordsContract.RecordsEntry;
import com.eahm.drinkwaterapp.Models.RecordModel;

import java.util.Calendar;

public class RecordsDbHelper extends SQLiteOpenHelper {

    //region VARIABLES
    private static final String TAG_RECORDS_DBH = "TAG_RECORDS_DBH";

    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "Records.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE IF NOT EXISTS " + RecordsEntry.TABLE_NAME +
                    "(" +
                    RecordsEntry._ID + " INTEGER PRIMARY KEY," +
                    RecordsEntry.COLUMN_NAME_TIMESTAMP + " INTEGER," +
                    RecordsEntry.COLUMN_NAME_DRINK_AMOUNT + " FLOAT," +
                    RecordsEntry.COLUMN_NAME_STATUS_CODE + " INT)";

    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + RecordsEntry.TABLE_NAME;

    //endregion VARIABLES

    //region IMPLEMENT METHODS
    public RecordsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion){
            case 1:
                Log.i(TAG_RECORDS_DBH, "Este dispositivo tiene la version 1 de la base de datos. Lamentablemente para esta version eliminamos los registros previos e iniciamos desde cero sin migrar :(");
                db.execSQL(SQL_DELETE_ENTRIES);
                onCreate(db);
                break;
        }

        //migrateDatabaseToV2(db); //MIgrar datos si hay cambios significativos en la base de datos.

    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
    //endregion IMPLEMENT METHODS

    //region MIGRATIONS
/* Dejamos esta migracion para futuras mejoras de la base de datos.
    private void migrateDatabaseToV2(SQLiteDatabase db) {

         String SQL_TABLE_V1 = "CREATE TABLE " + RecordsEntry.TABLE_TEMP_UPGRADING + "(" +
                                RecordsEntry._ID + " INTEGER PRIMARY KEY," +
                                RecordsEntry.COLUMN_NAME_TIMESTAMP + " TEXT," +
                                RecordsEntry.COLUMN_NAME_DRINK_AMOUNT + " TEXT," +
                                RecordsEntry.COLUMN_NAME_STATUS_CODE + " TEXT)";

        // Crear tabla temporal (replica)
        db.execSQL(SQL_TABLE_V1);
        Log.i(TAG_RECORDS_DBH, "v1: 1. Tabla " + RecordsEntry.TABLE_TEMP_UPGRADING + " creada");

        // Copiar datos a la nueva tabla temporal
        String queryCopy = "INSERT INTO " + RecordsEntry.TABLE_TEMP_UPGRADING + " SELECT * FROM " + RecordsEntry.TABLE_NAME + ";";
        String queryCopy2 = "INSERT INTO temporal_upgrading(timestamp, drinkAmount, statusCode) SELECT timestamp, drinkAmount, statusCode FROM records;";

        db.rawQuery(queryCopy2, null).close();
        Log.i(TAG_RECORDS_DBH, "v1: 2. Tabla copiada a " + RecordsEntry.TABLE_TEMP_UPGRADING);

        //Borrar base de datos vieja...
        db.execSQL(SQL_DELETE_ENTRIES);
        Log.i(TAG_RECORDS_DBH, "v1: Tabla " + RecordsEntry.TABLE_NAME + " eliminada");

        // Crear nueva base de datos version actual:
        onCreate(db);
        Log.i(TAG_RECORDS_DBH, "v1: Tabla ACTUAL CREADA");

        //Obtener todos los datos de la base de datos version 1
        Cursor cursor = db.query(RecordsEntry.TABLE_TEMP_UPGRADING,
                null,
                null,
                null,
                null,
                null, null,
                null);

        Log.i(TAG_RECORDS_DBH, "v1: Cursor obtenido de " + RecordsEntry.TABLE_TEMP_UPGRADING);

        // Recorrer datos y enviarlos a la nueva version.
        if (cursor != null) {
            boolean hasItem = cursor.moveToFirst();
            Log.i(TAG_RECORDS_DBH, "v1: Cursor MoveToFirst: " + hasItem);

            while (hasItem) {
                int id = cursor.getInt(cursor.getColumnIndex(RecordsEntry._ID));
                String timestamp = cursor.getString(cursor.getColumnIndex(RecordsEntry.COLUMN_NAME_TIMESTAMP));
                String drinkAmount = cursor.getString(cursor.getColumnIndex(RecordsEntry.COLUMN_NAME_DRINK_AMOUNT));
                String statusCode = cursor.getString(cursor.getColumnIndex(RecordsEntry.COLUMN_NAME_STATUS_CODE));

                Log.i(TAG_RECORDS_DBH, "v1: datos: " + timestamp + " - " + drinkAmount + " - " + statusCode );

                RecordModel newRecord = new RecordModel(Long.parseLong(timestamp), Float.parseFloat(drinkAmount), Integer.parseInt(statusCode));

                long result = db.insert(RecordsEntry.TABLE_NAME, null, newRecord.toContentValues());

                Log.i(TAG_RECORDS_DBH, "v1: Resultado del insert: " + result);

                hasItem = cursor.moveToNext();
            }
            cursor.close();
        }

        //eliminar tabla temporal
        db.execSQL("DROP TABLE IF EXISTS " + RecordsEntry.TABLE_TEMP_UPGRADING);
        Log.i(TAG_RECORDS_DBH, "v1: Tabla " + RecordsEntry.TABLE_TEMP_UPGRADING + " eliminada...");

        Log.i(TAG_RECORDS_DBH, "v1: Migracion terminada.");
        // Eliminar tabla anterior
    }
*/

    //endregion MIGRATIONS

    //region QUERYS
    public Cursor getAllRecords() {
        return getReadableDatabase()
                .query(
                        RecordsEntry.TABLE_NAME,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null);
    }

    public Cursor getYesterdayRecords() {
        Log.i(TAG_RECORDS_DBH, "getTodayRecords()");

        Calendar rightNow = Calendar.getInstance();
        rightNow.add(Calendar.DATE, -1); // Establecer el dia de ayer!

        rightNow.set(Calendar.HOUR_OF_DAY, 0);
        rightNow.set(Calendar.MINUTE, 0);
        rightNow.set(Calendar.SECOND, 0);
        long startOfToday = rightNow.getTimeInMillis() / 1000;

        rightNow.set(Calendar.HOUR_OF_DAY, 23);
        rightNow.set(Calendar.MINUTE, 59);
        rightNow.set(Calendar.SECOND, 59);
        long endOfToday = rightNow.getTimeInMillis() / 1000;

        Log.i(TAG_RECORDS_DBH, "Start of this day: " + startOfToday);

        String query = "SELECT * FROM " + RecordsEntry.TABLE_NAME +
                " WHERE " +
                RecordsEntry.COLUMN_NAME_TIMESTAMP + " >= " + startOfToday
                + " AND " +
                RecordsEntry.COLUMN_NAME_TIMESTAMP + " <= " + endOfToday;

        return getReadableDatabase().rawQuery(query, null);
    }

    public Cursor getTodayRecords() {
        Log.i(TAG_RECORDS_DBH, "getTodayRecords()");

        Calendar rightNow = Calendar.getInstance();

        rightNow.set(Calendar.HOUR_OF_DAY, 0);
        rightNow.set(Calendar.MINUTE, 0);
        rightNow.set(Calendar.SECOND, 0);
        long startOfToday = rightNow.getTimeInMillis() / 1000;

        rightNow.set(Calendar.HOUR_OF_DAY, 23);
        rightNow.set(Calendar.MINUTE, 59);
        rightNow.set(Calendar.SECOND, 59);
        long endOfToday = rightNow.getTimeInMillis() / 1000;

        Log.i(TAG_RECORDS_DBH, "Start of this day: " + startOfToday);

        String query = "SELECT * FROM " + RecordsEntry.TABLE_NAME +
                " WHERE " +
                RecordsEntry.COLUMN_NAME_TIMESTAMP + " >= " + startOfToday
                + " AND " +
                RecordsEntry.COLUMN_NAME_TIMESTAMP + " <= " + endOfToday;

        return getReadableDatabase().rawQuery(query, null);
    }

    public Cursor getRecordById(String recordId) {
        Cursor c = getReadableDatabase().query(
                RecordsEntry.TABLE_NAME,
                null,
                RecordsEntry._ID + " LIKE ?",
                new String[]{recordId},
                null,
                null,
                null);
        return c;
    }

    //endregion QUERYS

/*
    public long saveRecord(RecordModel newRecord) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        return sqLiteDatabase.insert(RecordsEntry.TABLE_NAME, null, newRecord.toContentValues());
    }

    public int deleteRecord(String recordId) {
        return getWritableDatabase().delete(
                RecordsEntry.TABLE_NAME,
                RecordsEntry._ID + " LIKE ?",
                new String[]{recordId});
    }

    public int updateRecord(RecordModel record, String recordId) {
        return getWritableDatabase().update(
                RecordsEntry.TABLE_NAME,
                record.toContentValues(),
                RecordsEntry._ID + " LIKE ?",
                new String[]{recordId}
        );
    }
*/
}
