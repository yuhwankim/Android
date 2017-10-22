package com.example.kwanwoo.sqlitedbtest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {
    final static String TAG="SQLiteDBTest";

    public DBHelper(Context context) {
        super(context, UserContract.DB_NAME, null, UserContract.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG,getClass().getName()+".onCreate()");
        db.execSQL(UserContract.Users.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        Log.i(TAG,getClass().getName() +".onUpgrade()");
        db.execSQL(UserContract.Users.DELETE_TABLE);
        onCreate(db);
    }

    public void insertDataBySQL(String id, String password) {
        try {
            String sql = String.format (
                    "INSERT INTO %s (%s, %s, %s) VALUES (NULL, '%s', '%s')",
                    UserContract.Users.TABLE_NAME,
                    UserContract.Users._ID,
                    UserContract.Users.KEY_ACCOUNT,
                    UserContract.Users.KEY_PASSWORD,
                    id,
                    password);

            getWritableDatabase().execSQL(sql);
        } catch (SQLException e) {
            Log.e(TAG,"Error in deleting recodes");
        }
    }

    public Cursor getAllDataBySQL() {
        String sql = "Select * FROM " + UserContract.Users.TABLE_NAME;
        return getReadableDatabase().rawQuery(sql,null);
    }

    public void deleteDataBySQL(String _id) {
        try {
            String sql = String.format (
                    "DELETE FROM %s WHERE %s = %s",
                    UserContract.Users.TABLE_NAME,
                    UserContract.Users._ID,
                    _id);
            getWritableDatabase().execSQL(sql);
        } catch (SQLException e) {
            Log.e(TAG,"Error in deleting recodes");
        }
    }

    public void updateDataBySQL(String _id, String userid, String password) {
        try {
            String sql = String.format (
                    "UPDATE  %s SET %s = '%s', %s = '%s' WHERE %s = %s",
                    UserContract.Users.TABLE_NAME,
                    UserContract.Users.KEY_ACCOUNT, userid,
                    UserContract.Users.KEY_PASSWORD, password,
                    UserContract.Users._ID, _id) ;
            getWritableDatabase().execSQL(sql);
        } catch (SQLException e) {
            Log.e(TAG,"Error in updating recodes");
        }
    }

    public long insertDataByMethod(String id, String password) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(UserContract.Users.KEY_ACCOUNT, id);
        values.put(UserContract.Users.KEY_PASSWORD,password);

        return db.insert(UserContract.Users.TABLE_NAME,null,values);
    }

    public Cursor getAllDataByMethod() {
        SQLiteDatabase db = getReadableDatabase();
        return db.query(UserContract.Users.TABLE_NAME,null,null,null,null,null,null);
    }

    public long deleteDataByMethod(String _id) {
        SQLiteDatabase db = getWritableDatabase();

        String whereClause = UserContract.Users._ID +" = ?";
        String[] whereArgs ={_id};
        return db.delete(UserContract.Users.TABLE_NAME, whereClause, whereArgs);
    }

    public long updateDataByMethod(String _id, String userid, String password) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(UserContract.Users.KEY_ACCOUNT, userid);
        values.put(UserContract.Users.KEY_PASSWORD,password);

        String whereClause = UserContract.Users._ID +" = ?";
        String[] whereArgs ={_id};

        return db.update(UserContract.Users.TABLE_NAME, values, whereClause, whereArgs);
    }

}
