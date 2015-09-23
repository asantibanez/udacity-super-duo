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

    //Views
    public static final String FIXTURES_TEAMS_VIEW =
            FIXTURES_TABLE + " " + FixturesAndTeamsView.FIXTURE_ALIAS +
            " INNER JOIN " + TEAMS_TABLE + " " + FixturesAndTeamsView.HOME_TEAM_ALIAS +
                    " ON " + FixturesAndTeamsView.HOME_TEAM_ID + " = " + FixturesAndTeamsView.FIXTURE_HOME_TEAM_ID +
            " INNER JOIN " + TEAMS_TABLE + " " + FixturesAndTeamsView.AWAY_TEAM_ALIAS +
                    " ON " + FixturesAndTeamsView.AWAY_TEAM_ID + " = " + FixturesAndTeamsView.FIXTURE_AWAY_TEAM_ID
            ;

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

    }

    //Teams
    public static final class TeamsTable implements BaseColumns {

        //Columns
        public static final String TEAM_ID = "team_id";
        public static final String TEAM_NAME = "team_name";
        public static final String TEAM_CREST_URL = "team_crest_url";

    }


    //Views definitions
    //FixturesAndTeams
    public static final class FixturesAndTeamsView {

        //Aliases
        public static final String FIXTURE_ALIAS = "fixture";
        public static final String HOME_TEAM_ALIAS = "home";
        public static final String AWAY_TEAM_ALIAS = "away";

        //Columns
        //Fixture
        public static final String FIXTURE_ID = FIXTURE_ALIAS + "." + FixturesTable._ID;
        public static final String FIXTURE_MATCH_ID = FIXTURE_ALIAS + "." + FixturesTable.MATCH_ID;
        public static final String FIXTURE_MATCH_TIME = FIXTURE_ALIAS + "." + FixturesTable.TIME_COL;
        public static final String FIXTURE_HOME_TEAM_ID = FIXTURE_ALIAS + "." + FixturesTable.HOME_ID_COL;
        public static final String FIXTURE_AWAY_TEAM_ID = FIXTURE_ALIAS + "." + FixturesTable.AWAY_ID_COL;
        public static final String HOME_TEAM_GOALS = FIXTURE_ALIAS + "." + FixturesTable.HOME_GOALS_COL;
        public static final String AWAY_TEAM_GOALS = FIXTURE_ALIAS + "." + FixturesTable.AWAY_GOALS_COL;
        public static final String FIXTURE_LEAGUE_ID = FIXTURE_ALIAS + "." + FixturesTable.LEAGUE_COL;
        public static final String FIXTURE_MATCH_DAY = FIXTURE_ALIAS + "." + FixturesTable.MATCH_DAY;

        //Home Team
        public static final String HOME_TEAM_ID = HOME_TEAM_ALIAS + "." + TeamsTable.TEAM_ID;
        public static final String HOME_TEAM_NAME = HOME_TEAM_ALIAS + "." + TeamsTable.TEAM_NAME;

        public static final String HOME_TEAM_CREST_URL = HOME_TEAM_ALIAS + "." + TeamsTable.TEAM_CREST_URL;
        //Away Team
        public static final String AWAY_TEAM_ID = AWAY_TEAM_ALIAS + "." + TeamsTable.TEAM_ID;
        public static final String AWAY_TEAM_NAME = AWAY_TEAM_ALIAS + "." + TeamsTable.TEAM_NAME;
        public static final String AWAY_TEAM_CREST_URL = AWAY_TEAM_ALIAS + "." + TeamsTable.TEAM_CREST_URL;

        //Projection
        public static final String[] projection = new String[]{
                FIXTURE_ID,
                FIXTURE_MATCH_ID,
                FIXTURE_MATCH_TIME,
                HOME_TEAM_ID,
                HOME_TEAM_NAME,
                HOME_TEAM_CREST_URL,
                HOME_TEAM_GOALS,
                AWAY_TEAM_ID,
                AWAY_TEAM_NAME,
                AWAY_TEAM_CREST_URL,
                AWAY_TEAM_GOALS,
                FIXTURE_LEAGUE_ID,
                FIXTURE_MATCH_DAY
        };

    }
}
