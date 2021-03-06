package com.elfichero.elfichero;

import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.SimpleCursorAdapter.ViewBinder;
import android.widget.TextView;
import android.widget.Toast;

public class ListFragment extends android.app.ListFragment implements LoaderCallbacks<Cursor>{
    private static final String TAG = ListFragment.class.getSimpleName();
    private static final String FROM[] = {StatusContract.Column.USER, StatusContract.Column.CONTENIDO, StatusContract.Column.CREATED_AT, StatusContract.Column.TITLE};
    private static final int TO[] = { R.id.detail_item_text_user, R.id.detail_item_text_message, R.id.detail_item_text_created_at, R.id.title};


    private static final int LOADER_ID = 42;
    private SimpleCursorAdapter mAdapter;

     @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAdapter = new SimpleCursorAdapter(getActivity(), R.layout.detail_item, null, FROM, TO, 0);
        //mAdapter.setViewBinder(VIEW_BINDER);
        setListAdapter(mAdapter);
        getLoaderManager().initLoader(LOADER_ID, null, this);
    }



    @Override
    public void onListItemClick(ListView l, View v, int position, long id){

        // Get the details fragment
        DetailsFragment fragment = (DetailsFragment) getFragmentManager().findFragmentById(R.id.fragment_details);
        // Is details fragment visible?
        if (fragment != null && fragment.isVisible()) {
            fragment.updateView(id);
        } else {
            startActivity(new Intent(getActivity(), DetailsActivity.class).putExtra(StatusContract.Column.ID, id));
        }
    }




    // --- Loader Callbacks ---
    // Executed on a non-UI thread
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (id != LOADER_ID)
            return null;
        Log.d(TAG, "onCreateLoader");
        return new CursorLoader(getActivity(), StatusContract.CONTENT_URI, null, null, null, StatusContract.DEFAULT_SORT);
    }
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        // Get the details fragment
        DetailsFragment fragment = (DetailsFragment) getFragmentManager()
                .findFragmentById(R.id.fragment_details);
        // Is details fragment visible?
        if (fragment != null && fragment.isVisible() && cursor.getCount() == 0) {
            fragment.updateView(-1);
            Toast.makeText(getActivity(), "No data",Toast.LENGTH_LONG).show();
        }
        Log.d(TAG, "onLoadFinished with cursor: " + cursor.getCount());
        mAdapter.swapCursor(cursor);
    }
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }
}
