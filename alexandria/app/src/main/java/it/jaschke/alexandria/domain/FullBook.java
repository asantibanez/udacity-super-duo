package it.jaschke.alexandria.domain;

import android.database.Cursor;

import it.jaschke.alexandria.provider.AlexandriaContract;

/**
 * Created by asantibanez on 9/30/15.
 */
public class FullBook {

    public long bookId;
    public String bookTitle;
    public String bookSubTitle;
    public String bookDescription;
    public String bookCoverUrl;
    public String authorName;
    public String categoryName;

    public FullBook() {
        bookId = 0;
        bookTitle = "";
        bookSubTitle = "";
        bookDescription = "";
        bookCoverUrl = "";
        authorName = "";
        categoryName = "";
    }

    public static FullBook fromCursor(Cursor cursor) {
        FullBook fullBook = new FullBook();
        fullBook.bookId = cursor.getLong(cursor.getColumnIndex(AlexandriaContract.BookEntry._ID));
        fullBook.bookTitle = cursor.getString(cursor.getColumnIndex(AlexandriaContract.BookEntry.TITLE));
        fullBook.bookSubTitle = cursor.getString(cursor.getColumnIndex(AlexandriaContract.BookEntry.SUBTITLE));
        fullBook.bookDescription = cursor.getString(cursor.getColumnIndex(AlexandriaContract.BookEntry.DESC));
        fullBook.bookCoverUrl = cursor.getString(cursor.getColumnIndex(AlexandriaContract.BookEntry.IMAGE_URL));
        fullBook.authorName = cursor.getString(cursor.getColumnIndex(AlexandriaContract.AuthorEntry.AUTHOR));
        fullBook.categoryName = cursor.getString(cursor.getColumnIndex(AlexandriaContract.CategoryEntry.CATEGORY));

        return fullBook;
    }

}
