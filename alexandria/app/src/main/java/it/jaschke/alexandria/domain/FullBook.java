package it.jaschke.alexandria.domain;

import android.database.Cursor;

import it.jaschke.alexandria.provider.AlexandriaContract;

/**
 * Created by asantibanez on 9/30/15.
 */
public class FullBook {

    public String coverUrl;
    public String bookTitle;
    public String bookSubTitle;
    public String authorName;

    public FullBook() {
        coverUrl = "";
        bookTitle = "";
        bookSubTitle = "";
        authorName = "";
    }

    public static FullBook fromCursor(Cursor cursor) {
        FullBook fullBook = new FullBook();
        fullBook.coverUrl = cursor.getString(cursor.getColumnIndex(AlexandriaContract.BookEntry.IMAGE_URL));
        fullBook.bookTitle = cursor.getString(cursor.getColumnIndex(AlexandriaContract.BookEntry.TITLE));
        fullBook.bookSubTitle = cursor.getString(cursor.getColumnIndex(AlexandriaContract.BookEntry.SUBTITLE));
        fullBook.authorName = "Tom Clancy";//cursor.getString(cursor.getColumnIndex(AlexandriaContract.AuthorEntry.AUTHOR));

        return fullBook;
    }

}
