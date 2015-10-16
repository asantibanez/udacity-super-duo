package it.jaschke.alexandria.navigation.bookslist;

import android.content.Intent;
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
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import butterknife.Bind;
import butterknife.ButterKnife;
import it.jaschke.alexandria.R;
import it.jaschke.alexandria.domain.FullBook;
import it.jaschke.alexandria.navigation.Navigator;
import it.jaschke.alexandria.navigation.addbook.ScanBookActivity;
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
    @Bind(R.id.no_books_message) TextView mNoBooksMessageView;
    @Bind(R.id.books_grid) GridView mBooksGridView;
    @Bind(R.id.fab) FloatingActionMenu mFab;
    @Bind(R.id.fab_add) FloatingActionButton mFabAdd;
    @Bind(R.id.fab_scan) FloatingActionButton mFabScan;


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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_books_list, container, false);
        ButterKnife.bind(this, view);

        //Setup toolbar
        ((BooksListActivity) getActivity()).setSupportActionBar(mToolbar);

        //Setup fab options
        mFabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigator.goToIsbnRegistration(getActivity(), false);
            }
        });
        mFabScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigator.goToIsbnRegistration(getActivity(), true);
            }
        });

        //Setup list adapter
        mAdapter = new BookListAdapter(getActivity(), null, 0);
        mBooksGridView.setAdapter(mAdapter);

        //Setup item click listener for list
        mBooksGridView.setOnItemClickListener(this);

        //Start cursor
        getLoaderManager().initLoader(LOADER_ID, null, this);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        getLoaderManager().restartLoader(LOADER_ID, null, this);
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
                AlexandriaContract.BookEntry.FULL_CONTENT_URI,
                null,
                null,
                null,
                null
        );

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);

        if (data == null)
            return;

        if (data.getCount() > 0)
            mNoBooksMessageView.setVisibility(View.GONE);
        else
            mNoBooksMessageView.setVisibility(View.VISIBLE);
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
        Cursor cursor = mAdapter.getCursor();
        cursor.moveToPosition(i);

        FullBook fullBook = FullBook.fromCursor(cursor);
        Navigator.goToBookDetail(getActivity(), fullBook.bookId);
    }

}
