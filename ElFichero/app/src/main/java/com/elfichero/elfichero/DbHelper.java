package com.elfichero.elfichero;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbHelper extends  SQLiteOpenHelper {

    private static final String TAG = DbHelper.class.getSimpleName();


    public DbHelper(Context context) {
        super(context, StatusContract.DB_NAME, null, StatusContract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //sentencia para crear tabla status
        String sql = String.format("create table %s ( %s int primary key, %s text, %s text, %s text, %s text, %s text, %s text)", StatusContract.TABLE,
                StatusContract.Column.ID, StatusContract.Column.USER, StatusContract.Column.MESSAGE, StatusContract.Column.CREATED_AT, StatusContract.Column.TITLE, StatusContract.Column.CONTENIDO, StatusContract.Column.LINK );
        Log.d(TAG, "onCreate with SQL: " + sql);
        //ejecutar sentencia crear tabla status
        db.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
