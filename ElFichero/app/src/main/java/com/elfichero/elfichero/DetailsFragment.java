package com.elfichero.elfichero;

import android.app.Fragment;
import android.content.ContentUris;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DetailsFragment extends Fragment{

    private TextView textUser, tv_contenido, textCreatedAt, tv_title;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.detail_item, null, false);
        textUser = (TextView) view.findViewById(R.id.detail_item_text_user);
        tv_contenido = (TextView) view.findViewById(R.id.detail_item_text_message);
        textCreatedAt = (TextView) view.findViewById(R.id.detail_item_text_created_at);
        tv_title = (TextView) view.findViewById(R.id.title);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.my_awesome_toolbar);
        toolbar.setTitle("Categoria Pendiente ...");
        toolbar.setTitleTextColor(Color.WHITE);

        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        long id = getActivity().getIntent().getLongExtra(StatusContract.Column.ID, -1);
        updateView(id);
    }


    public void updateView(long id){
        if (id == -1){
            textUser.setText("");
            tv_contenido.setText("");
            textCreatedAt.setText("");
            tv_title.setText("");

            return;
        }

        Uri uri = ContentUris.withAppendedId(StatusContract.CONTENT_URI, id);
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
        if (!cursor.moveToFirst())
            return;
        String user = cursor.getString(cursor.getColumnIndex(StatusContract.Column.USER));
        String contenido = cursor.getString(cursor.getColumnIndex(StatusContract.Column.CONTENIDO));
        String title = cursor.getString(cursor.getColumnIndex(StatusContract.Column.TITLE));

        //long createdAt = cursor.getLong(cursor.getColumnIndex(StatusContract.Column.CREATED_AT));
        String createdAt = cursor.getString(cursor.getColumnIndex(StatusContract.Column.CREATED_AT));
        textUser.setText(user);
        this.tv_contenido.setText(contenido);
        //textCreatedAt.setText(DateUtils.getRelativeTimeSpanString(createdAt));
        textCreatedAt.setText(createdAt);
        tv_title.setText(title);

    }
}
