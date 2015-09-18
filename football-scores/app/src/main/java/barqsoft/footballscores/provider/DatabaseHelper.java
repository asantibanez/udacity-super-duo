package barqsoft.footballscores.provider;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import barqsoft.footballscores.provider.DatabaseContract;
import barqsoft.footballscores.provider.DatabaseContract.FixturesTable;
import barqsoft.footballscores.provider.DatabaseContract.TeamsTable;

/**
 * Created by yehya khaled on 2/25/2015.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "football_scores.db";
    private static final int DATABASE_VERSION = 2;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //Fixtures
        final String createFixturesTable = "CREATE TABLE " + DatabaseContract.FIXTURES_TABLE + " ("
                + FixturesTable._ID + " INTEGER PRIMARY KEY,"
                + FixturesTable.DATE_COL + " TEXT NOT NULL,"
                + FixturesTable.TIME_COL + " INTEGER NOT NULL,"
                + FixturesTable.HOME_ID_COL + " TEXT NOT NULL,"
                + FixturesTable.HOME_NAME_COL + " TEXT NOT NULL,"
                + FixturesTable.AWAY_ID_COL + " TEXT NOT NULL,"
                + FixturesTable.AWAY_NAME_COL + " TEXT NOT NULL,"
                + FixturesTable.LEAGUE_COL + " INTEGER NOT NULL,"
                + FixturesTable.HOME_GOALS_COL + " TEXT NOT NULL,"
                + FixturesTable.AWAY_GOALS_COL + " TEXT NOT NULL,"
                + FixturesTable.MATCH_ID + " INTEGER NOT NULL,"
                + FixturesTable.MATCH_DAY + " INTEGER NOT NULL,"
                + " UNIQUE ("+ FixturesTable.MATCH_ID+") ON CONFLICT REPLACE"
                + " );";

        //Teams
        final String createTeamsTable = "CREATE TABLE " + DatabaseContract.TEAMS_TABLE + " ("
                + TeamsTable._ID + " INTEGER PRIMARY KEY,"
                + TeamsTable.TEAM_ID + " TEXT NOT NULL,"
                + TeamsTable.TEAM_NAME + " TEXT NOT NULL,"
                + TeamsTable.TEAM_CREST_URL + " TEXT NOT NULL,"
                + " UNIQUE ("+ TeamsTable.TEAM_ID +") ON CONFLICT REPLACE"
                + " );";


        db.execSQL(createTeamsTable);
        db.execSQL(createFixturesTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Remove old values when upgrading.
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.TEAMS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.FIXTURES_TABLE);
    }

    public static ContentValues buildTeamContentValues(String id, String name, String crestUrl) {
        ContentValues teamValues = new ContentValues();
        teamValues.put(TeamsTable.TEAM_ID, id);
        teamValues.put(TeamsTable.TEAM_NAME, name);
        teamValues.put(TeamsTable.TEAM_CREST_URL, crestUrl);

        return  teamValues;
    }

    public static ContentValues buildFixtureContentValues(String id, String date, String time, String homeTeamId, String homeTeamName, String homeTeamGoals, String awayTeamId, String awayTeamName, String awayTeamGoals, String leagueId, String matchDay) {
        ContentValues fixtureValues = new ContentValues();
        fixtureValues.put(DatabaseContract.FixturesTable.MATCH_ID, id);
        fixtureValues.put(DatabaseContract.FixturesTable.DATE_COL, date);
        fixtureValues.put(DatabaseContract.FixturesTable.TIME_COL, time);
        fixtureValues.put(DatabaseContract.FixturesTable.HOME_ID_COL, homeTeamId);
        fixtureValues.put(DatabaseContract.FixturesTable.HOME_NAME_COL, homeTeamName);
        fixtureValues.put(DatabaseContract.FixturesTable.HOME_GOALS_COL, homeTeamGoals);
        fixtureValues.put(DatabaseContract.FixturesTable.AWAY_ID_COL, awayTeamId);
        fixtureValues.put(DatabaseContract.FixturesTable.AWAY_NAME_COL, awayTeamName);
        fixtureValues.put(DatabaseContract.FixturesTable.AWAY_GOALS_COL, awayTeamGoals);
        fixtureValues.put(DatabaseContract.FixturesTable.LEAGUE_COL, leagueId);
        fixtureValues.put(DatabaseContract.FixturesTable.MATCH_DAY, matchDay);

        return fixtureValues;
    }

}
