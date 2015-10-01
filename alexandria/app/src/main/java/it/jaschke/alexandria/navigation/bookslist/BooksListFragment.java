package it.jaschke.alexandria.navigation.bookslist;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import butterknife.Bind;
import butterknife.ButterKnife;
import it.jaschke.alexandria.MainActivity;
import it.jaschke.alexandria.R;
import it.jaschke.alexandria.provider.AlexandriaContract;


public class BooksListFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor>,
        AdapterView.OnItemClickListener {

    //Constants
    private static final int LOADER_ID = 1000;

    //Variables
    private BookListAdapter mAdapter;

    //Controls
    @Bind(R.id.toolbar) Toolbar mToolbar;
    @Bind(R.id.books_grid) GridView mBooksGridView;


    /**
     * Constructor and factory
     */
    public static BooksListFragment newInstance() {
        BooksListFragment fragment = new BooksListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public BooksListFragment() {}


    /**
     * Lifecycle methods
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_books_list, container, false);
        ButterKnife.bind(this, view);

        //Setup toolbar
        ((MainActivity) getActivity()).setSupportActionBar(mToolbar);

        //Setup list adapter
        mAdapter = new BookListAdapter(getActivity(), null, 0);
        mBooksGridView.setAdapter(mAdapter);

        //Setup click listener for list
        mBooksGridView.setOnItemClickListener(this);

        //Start cursor
        getLoaderManager().initLoader(LOADER_ID, null, this);

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }


    /**
     * Loader callbacks
     */
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        return new CursorLoader(
                getActivity(),
                AlexandriaContract.BookEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }


    /**
     * Item click callbacks
     */
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

}
