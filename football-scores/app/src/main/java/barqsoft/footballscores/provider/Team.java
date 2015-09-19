package barqsoft.footballscores.provider;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;

import com.google.gson.Gson;


/**
 * Created by asantibanez on 9/18/15.
 */
public class Team {

    public String id;
    public String name;
    public String crestUrl;

    private Team() {
        id = name = crestUrl = "";
    }

    public boolean hasCrestUrl() {
        return crestUrl.length() > 0;
    }

    public String toJson() {
        return new Gson().toJson(this);
    }

    public static Team withId(ContentResolver contentResolver, String id) {
        String[] projection = null;
        String selection = DatabaseContract.TeamsTable.TEAM_ID + " = ? ";
        String[] selectionArgs = new String[]{ id };
        String sortOrder = null;

        Cursor cursor = contentResolver.query(
                FootballScoresProvider.TEAMS_URI,
                projection,
                selection,
                selectionArgs,
                sortOrder
        );

        if(cursor == null || cursor.getCount() == 0)
            return null;

        cursor.moveToFirst();
        return Team.fromCursor(cursor);
    }

    public static Team fromCursor(Cursor cursor) {
        Team team = new Team();
        team.id = cursor.getString(cursor.getColumnIndex(DatabaseContract.TeamsTable.TEAM_ID));
        team.name = cursor.getString(cursor.getColumnIndex(DatabaseContract.TeamsTable.TEAM_NAME));
        team.crestUrl = cursor.getString(cursor.getColumnIndex(DatabaseContract.TeamsTable.TEAM_CREST_URL));

        return team;
    }

    public static void save(ContentResolver contentResolver, String id, String name, String crestUrl) {
        ContentValues teamValues = new ContentValues();
        teamValues.put(DatabaseContract.TeamsTable.TEAM_ID, id);
        teamValues.put(DatabaseContract.TeamsTable.TEAM_NAME, name);
        teamValues.put(DatabaseContract.TeamsTable.TEAM_CREST_URL, crestUrl);

        contentResolver.insert(FootballScoresProvider.TEAMS_URI, teamValues);
    }
}
