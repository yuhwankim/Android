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
        super(context, DatabaseContract.DB_NAME, null, DatabaseContract.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG,getClass().getName()+".onCreate()");
        db.execSQL(DatabaseContract.User.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        Log.i(TAG,getClass().getName() +".onUpgrade()");
        db.execSQL(DatabaseContract.User.DELETE_TABLE);
        onCreate(db);
    }

    public void insertDataBySQL(String id, String password) {
        try {
            String sql = String.format (
                    "INSERT INTO %s (%s, %s, %s) VALUES (NULL, '%s', '%s')",
                    DatabaseContract.User.TABLE_NAME,
                    DatabaseContract.User._ID,
                    DatabaseContract.User.KEY_ID,
                    DatabaseContract.User.KEY_PASSWORD,
                    id,
                    password);

            getWritableDatabase().execSQL(sql);
        } catch (SQLException e) {
            Log.e(TAG,"Error in deleting recodes");
        }
    }

    public Cursor getAllDataBySQL() {
        String sql = "Select * FROM " + DatabaseContract.User.TABLE_NAME;
        return getReadableDatabase().rawQuery(sql,null);
    }

    public void deleteDataBySQL(String _id) {
        try {
            String sql = String.format (
                    "DELETE FROM %s WHERE %s = %s",
                    DatabaseContract.User.TABLE_NAME,
                    DatabaseContract.User._ID,
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
                    DatabaseContract.User.TABLE_NAME,
                    DatabaseContract.User.KEY_ID, userid,
                    DatabaseContract.User.KEY_PASSWORD, password,
                    DatabaseContract.User._ID, _id) ;
            getWritableDatabase().execSQL(sql);
        } catch (SQLException e) {
            Log.e(TAG,"Error in updating recodes");
        }
    }

    public long insertDataByMethod(String id, String password) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.User.KEY_ID, id);
        values.put(DatabaseContract.User.KEY_PASSWORD,password);

        return db.insert(DatabaseContract.User.TABLE_NAME,null,values);
    }

    public Cursor getAllDataByMethod() {
        SQLiteDatabase db = getReadableDatabase();
        return db.query(DatabaseContract.User.TABLE_NAME,null,null,null,null,null,null);
    }

    public long deleteDataByMethod(String _id) {
        SQLiteDatabase db = getWritableDatabase();

        String whereClause = DatabaseContract.User._ID +" = ?";
        String[] whereArgs ={_id};
        return db.delete(DatabaseContract.User.TABLE_NAME, whereClause, whereArgs);
    }

    public long updateDataByMethod(String _id, String userid, String password) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabaseContract.User.KEY_ID, userid);
        values.put(DatabaseContract.User.KEY_PASSWORD,password);

        String whereClause = DatabaseContract.User._ID +" = ?";
        String[] whereArgs ={_id};

        return db.update(DatabaseContract.User.TABLE_NAME, values, whereClause, whereArgs);
    }

}
