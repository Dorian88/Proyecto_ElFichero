package com.elfichero.elfichero;

import android.net.Uri;
import android.provider.BaseColumns;

public class StatusContract {
    public static final String DB_NAME= "elfichero.db";
    public static final int DB_VERSION= 1;
    public static final String TABLE= "articulos";
    static final String DEFAULT_SORT = Column.CREATED_AT + " DESC ";

    //contentProvider
    public static final String AUTHORITY="com.elfichero.elfichero.StatusProvider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE);
    public static final int STATUS_ITEM = 1;
    public static final int STATUS_DIR = 2;
    //MIME type para una fila
    public static final String STATUS_TYPE_ITEM="vnd.android.cursor.item/vnd.com.elfichero.elfichero.provider.articulos";
    //MIME type para la coleccion
    public static final String STATUS_TYPE_DIR="vnd.android.cursor.dir/vnd.com.elfichero.elfichero.provider.articulos";

    //columnas
    public class Column{
        public static final String ID = BaseColumns._ID;
        public static final String USER = "user";
        public static final String MESSAGE = "message";
        public static final String CREATED_AT = "created_at";
        public static final String TITLE = "title";
        public static final String CONTENIDO = "contenido";
        public static final String LINK = "link";

    }
}
