package it.jaschke.alexandria.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by saj on 22/12/14.
 */
public class AlexandriaDatabaseHelper extends SQLiteOpenHelper {

    //Constants
    private static final String LOG_TAG = AlexandriaDatabaseHelper.class.getSimpleName();

    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "alexandria.db";

    /**
     * Constructor
     */
    public AlexandriaDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    /**
     * Database creation
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_BOOK_TABLE = "CREATE TABLE " + AlexandriaContract.BookEntry.TABLE_NAME + " ("+
                AlexandriaContract.BookEntry._ID + " INTEGER PRIMARY KEY," +
                AlexandriaContract.BookEntry.TITLE + " TEXT NOT NULL," +
                AlexandriaContract.BookEntry.SUBTITLE + " TEXT ," +
                AlexandriaContract.BookEntry.DESC + " TEXT ," +
                AlexandriaContract.BookEntry.IMAGE_URL + " TEXT, " +
                "UNIQUE ("+ AlexandriaContract.BookEntry._ID +") ON CONFLICT IGNORE)";

        final String SQL_CREATE_AUTHOR_TABLE = "CREATE TABLE " + AlexandriaContract.AuthorEntry.TABLE_NAME + " ("+
                AlexandriaContract.AuthorEntry._ID + " INTEGER," +
                AlexandriaContract.AuthorEntry.AUTHOR + " TEXT," +
                " FOREIGN KEY (" + AlexandriaContract.AuthorEntry._ID + ") REFERENCES " +
                AlexandriaContract.BookEntry.TABLE_NAME + " (" + AlexandriaContract.BookEntry._ID + "))";

        final String SQL_CREATE_CATEGORY_TABLE = "CREATE TABLE " + AlexandriaContract.CategoryEntry.TABLE_NAME + " ("+
                AlexandriaContract.CategoryEntry._ID + " INTEGER," +
                AlexandriaContract.CategoryEntry.CATEGORY + " TEXT," +
                " FOREIGN KEY (" + AlexandriaContract.CategoryEntry._ID + ") REFERENCES " +
                AlexandriaContract.BookEntry.TABLE_NAME + " (" + AlexandriaContract.BookEntry._ID + "))";


        Log.d(LOG_TAG,SQL_CREATE_BOOK_TABLE);
        db.execSQL(SQL_CREATE_BOOK_TABLE);

        Log.d(LOG_TAG,SQL_CREATE_AUTHOR_TABLE);
        db.execSQL(SQL_CREATE_AUTHOR_TABLE);

        Log.d(LOG_TAG,SQL_CREATE_CATEGORY_TABLE);
        db.execSQL(SQL_CREATE_CATEGORY_TABLE);

    }


    /**
     * Database upgrade
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
