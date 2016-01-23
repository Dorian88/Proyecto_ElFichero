package com.elfichero.elfichero;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

public class StatusProvider extends ContentProvider {
    public static final String TAG = StatusProvider.class.getSimpleName();
    private DbHelper dbHelper;

    public StatusProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        String where;
        switch (sURIMatcher.match(uri)) {
            case StatusContract.STATUS_DIR:
                //se eliminarán varios registros
                where = (selection == null) ? "1" : selection;
                break;
            case StatusContract.STATUS_ITEM:
                /*se elimiará un solo registro
                However, if the URI does have an ID as part of the path, we need to extract it
                and update our WHERE statement to account for that ID.
                 */
                long id = ContentUris.parseId(uri);
                where = StatusContract.Column.ID
                        + "="
                        + id
                        + (TextUtils.isEmpty(selection) ? "" : " and ( " + selection + " )");
                break;
            default:
                throw new IllegalArgumentException("Illegal uri: " + uri);
        }
        //crea y obtiene BD para escribir
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        //elimina
        int ret = db.delete(StatusContract.TABLE, where, selectionArgs);
        if (ret > 0) {
            //notifica que los datos para este uri han cambiado
            getContext().getContentResolver().notifyChange(uri, null);
        }
        Log.d(TAG, "records borrados: " + ret);
        return ret;
    }

    @Override
    public String getType(Uri uri) {

        switch (sURIMatcher.match(uri)) {
            case StatusContract.STATUS_DIR:
                Log.d(TAG, "gotType: " + StatusContract.STATUS_TYPE_DIR);
                return StatusContract.STATUS_TYPE_DIR;
            case StatusContract.STATUS_ITEM:
                Log.d(TAG, "gotType: " + StatusContract.STATUS_TYPE_ITEM);
                return StatusContract.STATUS_TYPE_ITEM;
            default:
                throw new IllegalArgumentException("Illegal uri: " + uri);

        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Uri ret = null;
        // Confirmar uri
        if (sURIMatcher.match(uri) != StatusContract.STATUS_DIR) {
            throw new IllegalArgumentException("Illegal uri: " + uri);
        }
        //crea y obtiene BD para escribir
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        //inserta
        long rowId = db.insertWithOnConflict(StatusContract.TABLE, null, values, SQLiteDatabase.CONFLICT_IGNORE);

        /*fue la insercion exitosa?
        If anything fails during the insert, the database will return –1. We can then throw
        a runtime exception because this is an error that should never have happened.
        If the insert was successful, we use the ContentUris.withAppendedId() helper
        method to craft a new URI containing the ID of the new record appended to the
        standard provider’s URI
        */
        if (rowId != -1) {
            long id = values.getAsLong(StatusContract.Column.ID);
            ret = ContentUris.withAppendedId(uri, id);
            Log.d(TAG, "uri insertada: " + ret);

            //notifica que la info para esta uri ha cambiado
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return ret;
    }

    @Override
    public boolean onCreate() {
        dbHelper = new DbHelper(getContext());
        Log.d(TAG, "onCreated");
        return true;
    }

    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        //se refiere a la coleccion si la uri no finaliza con un numero
        sURIMatcher.addURI(StatusContract.AUTHORITY, StatusContract.TABLE, StatusContract.STATUS_DIR);
        //se refiere a una fila si la uri finaliza con un numero
        sURIMatcher.addURI(StatusContract.AUTHORITY, StatusContract.TABLE + "/#", StatusContract.STATUS_ITEM);
    }



    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        /*
        Here we use SQLiteQueryBuilder to make it easier to put together a potentially
        complex query statement.SQLiteQueryBuilder makes it easier than
        building a long string.
         */
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        //tabla en la que se trabajará
        qb.setTables(StatusContract.TABLE);


        //Again, we use the matcher to see what type of the URI we got.
        switch (sURIMatcher.match(uri)) {
            case StatusContract.STATUS_DIR:
                break;
            case StatusContract.STATUS_ITEM:
                qb.appendWhere(StatusContract.Column.ID + "=" + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Illegal uri: " + uri);
        }
        //para ordenar los registros a obtener
        String orderBy = (TextUtils.isEmpty(sortOrder) ? StatusContract.DEFAULT_SORT : sortOrder);

        //crea y obtiene BD para LEER
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        //consulta
        Cursor cursor = qb.query(db, projection, selection, selectionArgs, null, null, orderBy);

        // register for uri changes
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        Log.d(TAG, "records consultados: " + cursor.getCount());
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        String where;
        switch (sURIMatcher.match(uri)) {
            case StatusContract.STATUS_DIR:
                where = selection; //se cuentan filas actualizadas
                break;
            case StatusContract.STATUS_ITEM:
                long id = ContentUris.parseId(uri);
                where = StatusContract.Column.ID
                        + "="
                        + id
                        + (TextUtils.isEmpty(selection) ? "" : " and ( " + selection + " )");
                break;
            default:
                throw new IllegalArgumentException("Illegal uri: " + uri);

        }
        //crea y obtiene BD para escribir
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        //update
        int ret = db.update(StatusContract.TABLE, values, where, selectionArgs);
        if (ret > 0) {
            // se notifica que los datos para esta uri han cambiado
            getContext().getContentResolver().notifyChange(uri, null);
        }
        Log.d(TAG, "records actualizados: " + ret);
        return ret;
    }
}
