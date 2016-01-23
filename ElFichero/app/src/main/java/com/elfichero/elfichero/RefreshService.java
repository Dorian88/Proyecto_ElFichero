package com.elfichero.elfichero;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.util.Log;


import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

public class RefreshService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    public static final String ACTION_FOO = "com.elfichero.elfichero.action.FOO";
    public static final String ACTION_BAZ = "com.elfichero.elfichero.action.BAZ";

    // TODO: Rename parameters
    public static final String EXTRA_PARAM1 = "com.elfichero.elfichero.extra.PARAM1";
    public static final String EXTRA_PARAM2 = "com.elfichero.elfichero.extra.PARAM2";

    private static final String TAG= RefreshService.class.getSimpleName();

    //private static final String URL = "http://rss.jobsearch.monster.com/rssquery.ashx?q=Web%20Development";
    private static final String URL = "http://rss.jobsearch.monster.com/rssquery.ashx?q=";
    private static final String CLASSTAG = RefreshService.class.getSimpleName();
    String url_completa;



    int contItems;
    int[] id= new int[500];
    String[] creador = new String[500];
    String[] descripcion= new String[500];
    String[] fecha= new String[500];
    String[] title= new String[500];
    String[] link= new String[500];
    String[] contenido= new String[500];





    @Override
    public void onCreate(){
        super.onCreate();
        Log.d(TAG, "onCreated");
    }



    public RefreshService() {
        super("RefreshService");
    }



    @Override
    protected void onHandleIntent(Intent intent) {
        checkfornews();
    }



    private void checkfornews() {
        final SAXBuilder builder = new SAXBuilder();
        new Thread(new Runnable() {
            public void run() {
                try {
                    Document doc = builder.build("http://www.elfichero.com/feed");
                    Element root = doc.getRootElement();
                    readnews(root);
                }
                catch (JDOMException e) {  }
                catch (IOException e) { }
            }
        }).start();
    }

    private void readnews(Element root) {
        List<Element> children = root.getChildren();
        Iterator<Element> iterator_children = children.iterator();

        while(iterator_children.hasNext()) {
            Element child = iterator_children.next();
            List<Element> grandchildren = child.getChildren();
            Iterator<Element> iterator_grandchildren = grandchildren.iterator();




            int i=0;
            while(iterator_grandchildren.hasNext()) {
                Element siguiente_item = iterator_grandchildren.next();
                String name = siguiente_item.getName();



                if(name.equals("item") ) {
                    List<Element> lista_hijos = siguiente_item.getChildren();
                    Iterator<Element> iterator_lista_hijos= lista_hijos.iterator();
                    while(iterator_lista_hijos.hasNext()) {
                        Element siguiente = iterator_lista_hijos.next();

                        if((siguiente.getName()).equals("title")) {
                            title[i]=siguiente.getText();
                            id[i]= siguiente.getText().length();
                        }
                        if(siguiente.getName().equals("creator")) {
                            creador[i]=siguiente.getText();
                        }
                        if(siguiente.getName().equals("description")) {
                            descripcion[i]=siguiente.getText();
                        }
                        if(siguiente.getName().equals("pubDate")) {
                            fecha[i]=siguiente.getText();
                        }
                        if(siguiente.getName().equals("encoded")) {

                            String contenido_temp= siguiente.getText();
                            contenido[i]= String.valueOf(Html.fromHtml(contenido_temp));
                        }
                        if(siguiente.getName().equals("link")) {
                            link[i]=siguiente.getText();
                        }

                    }
                }





                i++;
            }
        }
        ContentValues values = new ContentValues();
        for (int i = 0; i<= 499; i++) {
            values.clear();
            values.put(StatusContract.Column.ID, id[i]);
            System.out.println("ID: " + id[i]);
            values.put(StatusContract.Column.USER, creador[i]);
            System.out.println("LINK: " + creador[i]);
            values.put(StatusContract.Column.MESSAGE, descripcion[i]);
            System.out.println("DESCRIPCION: " + descripcion[i]);
            values.put(StatusContract.Column.CREATED_AT, fecha[i]);
            System.out.println("FECHA: " + fecha[i]);
            values.put(StatusContract.Column.TITLE, title[i]);
            System.out.println("TITLE: " + title[i]);
            values.put(StatusContract.Column.CONTENIDO, contenido[i]);
            System.out.println("CONTENIDO: " + contenido[i]);
            values.put(StatusContract.Column.LINK, link[i]);
            System.out.println("LINK: " + link[i]);

            Uri uri = getContentResolver().insert(StatusContract.CONTENT_URI, values);
            if (uri != null) {
            }
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroyed");
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo(String param1, String param2) {
        // TODO: Handle action Foo
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
