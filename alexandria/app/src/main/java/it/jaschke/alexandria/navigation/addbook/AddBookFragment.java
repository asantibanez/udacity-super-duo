package it.jaschke.alexandria.navigation.addbook;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import butterknife.Bind;
import butterknife.ButterKnife;
import it.jaschke.alexandria.R;
import it.jaschke.alexandria.navigation.Navigator;
import it.jaschke.alexandria.services.BookService;

public class AddBookFragment extends Fragment {

    public static final String LOG_TAG = AddBookFragment.class.getSimpleName();

    //Variables
    BroadcastReceiver mBroadcastReceiver;
    boolean mSearchRunning;

    //Controls
    @Bind(R.id.isbn_number) EditText mIsbnNumberView;
    @Bind(R.id.progress_bar) ProgressBar mProgressBar;
    @Bind(R.id.confirm_button) Button mConfirmButton;

    /**
     * Factory and constructor
     */
    public static AddBookFragment newInstance() {
        AddBookFragment fragment = new AddBookFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);

        return fragment;
    }

    public AddBookFragment() {}


    /**
     * Lifecycle methods
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        registerBroadcastForBookSearch();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_book_by_isbn, container, false);
        ButterKnife.bind(this, view);

        //Register confirm button action
        mConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mSearchRunning)
                    return;

                String isbnNumber = mIsbnNumberView.getText().toString();
                if (isbnNumber.length() != 10 && isbnNumber.length() != 13) {
                    Snackbar.make(getView(), R.string.error_invalid_isbn_number, Snackbar.LENGTH_LONG).show();
                    return;
                }

                //Add ean 13 digits
                if(isbnNumber.length() == 10 && !isbnNumber.startsWith("978")){
                    isbnNumber = "978" + isbnNumber;
                }

                BookService.fetchBook(getContext(), isbnNumber);
                mSearchRunning = true;
                showProgressBar(true);
            }
        });

        //Check if search running
        if (mSearchRunning)
            showProgressBar(true);

        return view;
    }

    private void showProgressBar(boolean show) {
        if (show)
            mProgressBar.setVisibility(View.VISIBLE);
        else
            mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterBroadcastForBookSearch();
    }

    /**
     * Custom methods
     */
    private class BookSearchBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            long bookId = intent.getLongExtra(BookService.SEARCH_EVENT_BOOK_ID, 0);
            String message = intent.getStringExtra(BookService.SEARCH_EVENT_MESSAGE);
            String status = intent.getStringExtra(BookService.SEARCH_EVENT_STATUS);

            Log.d(LOG_TAG, status + " : " + message + " : " + bookId);

            mSearchRunning = false;
            showProgressBar(false);

            //Book not found, Book already added
            if (status.equals(BookService.BOOK_NOT_FOUND))
                Snackbar.make(getActivity().findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();

            //Book already added
            if (status.equals(BookService.BOOK_ALREADY_ADDED)) {
                Navigator.goToBookDetail(getActivity(), bookId);
                getActivity().finish();
            }

            //Book found
            if (status.equals(BookService.BOOK_FOUND)) {
                Navigator.goToBookDetail(getActivity(), bookId);
                getActivity().finish();
            }

        }
    }

    public void registerBroadcastForBookSearch() {
        mBroadcastReceiver = new BookSearchBroadcastReceiver();
        IntentFilter filter = new IntentFilter(BookService.SEARCH_EVENT);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mBroadcastReceiver,filter);
    }

    public void unregisterBroadcastForBookSearch() {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mBroadcastReceiver);
    }

}
