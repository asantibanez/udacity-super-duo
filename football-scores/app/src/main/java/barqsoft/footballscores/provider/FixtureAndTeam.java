package barqsoft.footballscores.provider;

import android.database.Cursor;

/**
 * Created by asantibanez on 9/19/15.
 */
public class FixtureAndTeam {

    public String matchId;
    public String matchTime;
    public String matchDay;

    public String homeTeamId;
    public String homeTeamName;
    public String homeTeamCrestUrl;
    public int homeTeamGoals;

    public String awayTeamId;
    public String awayTeamName;
    public String awayTeamCrestUrl;
    public int awayTeamGoals;

    public int leagueId;

    public FixtureAndTeam() {
        homeTeamId = homeTeamName = homeTeamCrestUrl = "";
        awayTeamId = awayTeamName = awayTeamCrestUrl = "";
    }

    public boolean homeCrestUrlAvailable() {
        return homeTeamCrestUrl.length() > 0;
    }

    public boolean awayCrestUrlAvailable() {
        return awayTeamCrestUrl.length() > 0;
    }

    public int getHomeTeamGoals() {
        return homeTeamGoals > -1 ? homeTeamGoals : 0;
    }

    public int getAwayTeamGoals() {
        return awayTeamGoals > -1 ? awayTeamGoals : 0;
    }

    public static FixtureAndTeam fromCursor(Cursor cursor) {
        FixtureAndTeam fixtureAndTeam = new FixtureAndTeam();

        //Fixture
        fixtureAndTeam.matchId = cursor.getString(1);
        fixtureAndTeam.matchTime = cursor.getString(2);

        //Home Team
        fixtureAndTeam.homeTeamId = cursor.getString(3);
        fixtureAndTeam.homeTeamName = cursor.getString(4);
        fixtureAndTeam.homeTeamCrestUrl = cursor.getString(5);
        fixtureAndTeam.homeTeamGoals = cursor.getInt(6);

        //Away Team
        fixtureAndTeam.awayTeamId = cursor.getString(7);
        fixtureAndTeam.awayTeamName = cursor.getString(8);
        fixtureAndTeam.awayTeamCrestUrl = cursor.getString(9);
        fixtureAndTeam.awayTeamGoals = cursor.getInt(10);

        //League Id
        fixtureAndTeam.leagueId = cursor.getInt(11);

        //League Id
        fixtureAndTeam.matchDay = cursor.getString(12);

        return fixtureAndTeam;
    }


}
