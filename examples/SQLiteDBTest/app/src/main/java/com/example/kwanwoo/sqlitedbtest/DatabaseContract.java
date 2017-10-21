package com.example.kwanwoo.sqlitedbtest;


import android.provider.BaseColumns;

public final class DatabaseContract {
    public static final String DB_NAME="database.db";
    public static final int DATABASE_VERSION = 2;
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private DatabaseContract() {}

    /* Inner class that defines the table contents */
    public static class User implements BaseColumns {
        public static final String TABLE_NAME="User";
        public static final String KEY_ID = "UserID";
        public static final String KEY_PASSWORD = "Password";

        public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                                                    _ID + " INTEGER PRIMARY KEY" + COMMA_SEP +
                                                    KEY_ID + TEXT_TYPE + COMMA_SEP +
                                                    KEY_PASSWORD + TEXT_TYPE +  " )";
        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
}
