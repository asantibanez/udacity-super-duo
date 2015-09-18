package barqsoft.footballscores.provider;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by yehya khaled on 2/25/2015.
 */
public class DatabaseContract {

    //Tables
    public static final String FIXTURES_TABLE = "fixtures_table";
    public static final String TEAMS_TABLE = "teams_table";

    //URIs
    public static final String CONTENT_AUTHORITY = "barqsoft.footballscores";
    public static Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    //Paths
    public static final String FIXTURES_PATH = "fixtures";
    public static final String TEAMS_PATH = "teams";

    //Tables definitions
    //Fixtures
    public static final class FixturesTable implements BaseColumns {

        //Columns
        public static final String LEAGUE_COL = "league";
        public static final String DATE_COL = "date";
        public static final String TIME_COL = "time";
        public static final String HOME_ID_COL = "home_id";
        public static final String HOME_NAME_COL = "home_name";
        public static final String AWAY_ID_COL = "away_id";
        public static final String AWAY_NAME_COL = "away_name";
        public static final String HOME_GOALS_COL = "home_goals";
        public static final String AWAY_GOALS_COL = "away_goals";
        public static final String MATCH_ID = "match_id";
        public static final String MATCH_DAY = "match_day";

        //Types
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + FIXTURES_PATH;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + FIXTURES_PATH;


        public static Uri buildScoreWithLeague() {
            return BASE_CONTENT_URI.buildUpon().appendPath("league").build();
        }

        public static Uri buildScoreWithId() {
            return BASE_CONTENT_URI.buildUpon().appendPath("id").build();
        }

        public static Uri buildScoreWithDate() {
            return BASE_CONTENT_URI.buildUpon().appendPath("date").build();
        }
    }

    //Teams
    public static final class TeamsTable implements BaseColumns {

        //Columns
        public static final String TEAM_ID = "team_id";
        public static final String TEAM_NAME = "team_name";
        public static final String TEAM_CREST_URL = "team_crest_url";

        //Types
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TEAMS_PATH;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TEAMS_PATH;

    }
}
