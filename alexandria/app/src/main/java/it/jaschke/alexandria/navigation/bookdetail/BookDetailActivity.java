package it.jaschke.alexandria.navigation.bookdetail;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import butterknife.Bind;
import butterknife.ButterKnife;
import it.jaschke.alexandria.R;
import it.jaschke.alexandria.domain.FullBook;
import it.jaschke.alexandria.provider.AlexandriaContract;
import it.jaschke.alexandria.services.BookService;

public class BookDetailActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    public static final String LOG_TAG = BookDetailActivity.class.getSimpleName();


    //Constants
    private static final String ARGS_BOOK_ID = "book_id";
    private static final int LOADER_ID = 999;

    //Variables
    long mBookId;
    FullBook mFullBook;

    //Controls
    @Bind(R.id.toolbar) Toolbar mToolbar;
    @Bind(R.id.progress_bar) ProgressBar mProgressBar;
    @Bind(R.id.book_cover) ImageView mBookCoverView;
    @Bind(R.id.book_title) TextView mBookTitleView;
    @Bind(R.id.book_author) TextView mBookAuthorView;
    @Bind(R.id.book_category) TextView mBookCategoryView;
    @Bind(R.id.book_description) TextView mBookDescriptionView;


    /**
     * Factory
     */
    public static Intent launchIntent(Context context, long bookId) {
        Intent intent = new Intent(context, BookDetailActivity.class);
        intent.putExtra(ARGS_BOOK_ID, bookId);

        return intent;
    }

    /**
     * Lifecycle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);
        ButterKnife.bind(this);

        //Get Book Id
        mBookId = getIntent().getLongExtra(ARGS_BOOK_ID, -1);
        Log.d(LOG_TAG, "Details for Book: " + mBookId);

        //Prepare Loader
        getSupportLoaderManager().initLoader(LOADER_ID, null, this);

        //Setup action bar
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
    }


    /**
     * Menu methods
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_book_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_delete) {
            BookService.deleteBook(this, Long.toString(mBookId));
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Custom methods
     */
    public void displayBookInfo() {
        //Progress Bar
        mProgressBar.setVisibility(View.GONE);

        //Cover
        if (mFullBook.bookCoverUrl.length() > 0)
            Glide.with(this).load(mFullBook.bookCoverUrl).into(mBookCoverView);

        //Data
        mBookTitleView.setText(mFullBook.bookTitle);
        mBookAuthorView.setText(mFullBook.authorName);
        mBookCategoryView.setText(mFullBook.categoryName);
        mBookDescriptionView.setText(mFullBook.bookDescription);
    }


    /**
     * Loader Callbacks
     */
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(
                this,
                AlexandriaContract.BookEntry.buildFullBookUri(mBookId),
                null,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data == null) {
            finish();
            return;
        }

        data.moveToFirst();
        mFullBook = FullBook.fromCursor(data);

        displayBookInfo();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

}
