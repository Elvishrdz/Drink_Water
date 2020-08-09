package com.eahm.drinkwaterapp.DB;

import android.provider.BaseColumns;

public final class RecordsContract {

    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private RecordsContract() {}

    /* Inner class that defines the table contents */
    public static class RecordsEntry implements BaseColumns {
        public static final String TABLE_NAME ="records";

        public static final String COLUMN_NAME_TIMESTAMP = "timestamp";
        public static final String COLUMN_NAME_DRINK_AMOUNT = "drinkAmount";
        public static final String COLUMN_NAME_STATUS_CODE= "statusCode";


        public static final String TABLE_TEMP_UPGRADING = "temporal_upgrading";
    }

}
