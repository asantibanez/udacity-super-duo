package barqsoft.footballscores.provider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Arrays;

/**
 * Created by yehya khaled on 2/25/2015.
 */
public class FootballScoresProvider extends ContentProvider {

    private static final String LOG_TAG = FootballScoresProvider.class.getSimpleName();

    private DatabaseHelper mDatabaseHelper;

    //Uris
    public static final Uri TEAMS_URI = DatabaseContract.BASE_CONTENT_URI.buildUpon().appendPath("teams").build();
    public static final Uri FIXTURES_URI = DatabaseContract.BASE_CONTENT_URI.buildUpon().appendPath("fixtures").build();
    public static final Uri FIXTURES_AND_TEAMS_URI = DatabaseContract.BASE_CONTENT_URI.buildUpon().appendPath("fixtures_teams").build();

    //Uri codes
    private static final int TEAMS_URI_CODE = 100;
    private static final int FIXTURES_URI_CODE = 101;
    private static final int FIXTURES_AND_TEAMS_URI_CODE = 102;

    //Uri matcher
    private UriMatcher mUriMatcher = buildUriMatcher();
    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = DatabaseContract.CONTENT_AUTHORITY;
        matcher.addURI(authority, "teams" , TEAMS_URI_CODE);
        matcher.addURI(authority, "fixtures" , FIXTURES_URI_CODE);
        matcher.addURI(authority, "fixtures_teams" , FIXTURES_AND_TEAMS_URI_CODE);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        mDatabaseHelper = new DatabaseHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Log.v(LOG_TAG, "query(uri=" + uri + ", proj=" + Arrays.toString(projection) + ", selection=" + selection + ", selectionArgs=" + Arrays.toString(selectionArgs) +")");

        final SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();
        Cursor cursor = null;

        final int match = mUriMatcher.match(uri);
        switch (match) {
            case TEAMS_URI_CODE:
                Log.d(LOG_TAG, DatabaseContract.TEAMS_TABLE);
                cursor = db.query(DatabaseContract.TEAMS_TABLE, projection, selection, selectionArgs, null, null, sortOrder);
                break;

            case FIXTURES_URI_CODE:
                Log.d(LOG_TAG, DatabaseContract.FIXTURES_TABLE);
                cursor = db.query(DatabaseContract.FIXTURES_TABLE, projection, selection, selectionArgs, null, null, sortOrder);
                break;

            case FIXTURES_AND_TEAMS_URI_CODE:
                Log.d(LOG_TAG, DatabaseContract.FIXTURES_TEAMS_VIEW);
                cursor = db.query(DatabaseContract.FIXTURES_TEAMS_VIEW, projection, selection, selectionArgs, null, null, sortOrder);
                break;

            default:
                Log.e(LOG_TAG, "No implementation for " + uri);
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;

    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        Log.v(LOG_TAG, "insert(uri=" + uri + ", values=" + contentValues.toString() + ")");

        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();

        long rowId = -1;
        final int match = mUriMatcher.match(uri);

        switch (match) {
            case TEAMS_URI_CODE:
                rowId = db.insertWithOnConflict(DatabaseContract.TEAMS_TABLE, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
                break;
            case FIXTURES_URI_CODE:
                rowId = db.insertWithOnConflict(DatabaseContract.FIXTURES_TABLE, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
                break;
            default:
                Log.e(LOG_TAG, "No implementation for " + uri);
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        if(rowId != -1){
            getContext().getContentResolver().notifyChange(uri, null);
            return ContentUris.withAppendedId(uri, rowId);
        }else{
            return null;
        }
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }

    /*
    private int match_uri(Uri uri)
    {
        String link = uri.toString();
        {
           if(link.contentEquals(DatabaseContract.BASE_CONTENT_URI.toString()))
           {
               return MATCHES;
           }
           else if(link.contentEquals(DatabaseContract.FixturesTable.buildScoreWithDate().toString()))
           {
               return MATCHES_WITH_DATE;
           }
           else if(link.contentEquals(DatabaseContract.FixturesTable.buildScoreWithId().toString()))
           {
               return MATCHES_WITH_ID;
           }
           else if(link.contentEquals(DatabaseContract.FixturesTable.buildScoreWithLeague().toString()))
           {
               return MATCHES_WITH_LEAGUE;
           }
        }
        return -1;
    }



    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs)
    {
        return 0;
    }

    @Override
    public String getType(Uri uri)
    {
        final int match = muriMatcher.match(uri);
        switch (match) {
            case MATCHES:
                return DatabaseContract.FixturesTable.CONTENT_TYPE;
            case MATCHES_WITH_LEAGUE:
                return DatabaseContract.FixturesTable.CONTENT_TYPE;
            case MATCHES_WITH_ID:
                return DatabaseContract.FixturesTable.CONTENT_ITEM_TYPE;
            case MATCHES_WITH_DATE:
                return DatabaseContract.FixturesTable.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri :" + uri );
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder)
    {
        Cursor retCursor;
        //Log.v(FetchScoreTask.LOG_TAG,uri.getPathSegments().toString());
        int match = match_uri(uri);
        //Log.v(FetchScoreTask.LOG_TAG,SCORES_BY_LEAGUE);
        //Log.v(FetchScoreTask.LOG_TAG,selectionArgs[0]);
        //Log.v(FetchScoreTask.LOG_TAG,String.valueOf(match));
        switch (match)
        {
            case MATCHES: retCursor = mDatabaseHelper.getReadableDatabase().query(
                    DatabaseContract.FIXTURES_TABLE,
                    projection,null,null,null,null,sortOrder); break;
            case MATCHES_WITH_DATE:
                    //Log.v(FetchScoreTask.LOG_TAG,selectionArgs[1]);
                    //Log.v(FetchScoreTask.LOG_TAG,selectionArgs[2]);
                    retCursor = mDatabaseHelper.getReadableDatabase().query(
                    DatabaseContract.FIXTURES_TABLE,
                    projection,SCORES_BY_DATE,selectionArgs,null,null,sortOrder); break;
            case MATCHES_WITH_ID: retCursor = mDatabaseHelper.getReadableDatabase().query(
                    DatabaseContract.FIXTURES_TABLE,
                    projection,SCORES_BY_ID,selectionArgs,null,null,sortOrder); break;
            case MATCHES_WITH_LEAGUE: retCursor = mDatabaseHelper.getReadableDatabase().query(
                    DatabaseContract.FIXTURES_TABLE,
                    projection,SCORES_BY_LEAGUE,selectionArgs,null,null,sortOrder); break;
            default: throw new UnsupportedOperationException("Unknown Uri" + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(),uri);
        return retCursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        return null;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values)
    {
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        //db.delete(DatabaseContract.SCORES_TABLE,null,null);
        //Log.v(FetchScoreTask.LOG_TAG,String.valueOf(muriMatcher.match(uri)));
        switch (match_uri(uri))
        {
            case MATCHES:
                db.beginTransaction();
                int returncount = 0;
                try
                {
                    for(ContentValues value : values)
                    {
                        long _id = db.insertWithOnConflict(DatabaseContract.FIXTURES_TABLE, null, value, SQLiteDatabase.CONFLICT_REPLACE);
                        if (_id != -1)
                        {
                            returncount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri,null);
                return returncount;
            default:
                return super.bulkInsert(uri,values);
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }
    */
}
