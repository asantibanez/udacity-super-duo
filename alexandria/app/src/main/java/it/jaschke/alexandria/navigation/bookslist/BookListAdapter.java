package it.jaschke.alexandria.navigation.bookslist;


import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.Bind;
import butterknife.ButterKnife;
import it.jaschke.alexandria.R;
import it.jaschke.alexandria.domain.FullBook;

/**
 * Created by saj on 11/01/15.
 */
public class BookListAdapter extends CursorAdapter {

    public static final String LOG_TAG = BookListAdapter.class.getSimpleName();

    //ViewHolder
    public static class ViewHolder {
        @Bind(R.id.noBookCoverInfo) TextView noBookCoverInfo;
        @Bind(R.id.fullBookCover) ImageView bookCover;
        @Bind(R.id.listBookTitle) TextView bookTitle;
        @Bind(R.id.author_name) TextView bookAuthor;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }


    /**
     * Constructor
     */
    public BookListAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }


    /**
     * Adapter methods
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();

        FullBook fullBook = FullBook.fromCursor(cursor);

        if(fullBook.bookCoverUrl.length() > 0) {
            Glide.with(context).load(fullBook.bookCoverUrl).into(viewHolder.bookCover);
            viewHolder.noBookCoverInfo.setVisibility(View.GONE);
        }
        else {
            viewHolder.bookCover.setImageBitmap(null);
            viewHolder.noBookCoverInfo.setVisibility(View.VISIBLE);
        }

        viewHolder.bookCover.setContentDescription(fullBook.bookTitle);
        viewHolder.bookTitle.setText(fullBook.bookTitle);
        viewHolder.bookAuthor.setText(fullBook.authorName);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.book_list_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

}
