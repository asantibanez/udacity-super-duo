package barqsoft.footballscores;

import android.util.Log;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Created by yehya khaled on 3/3/2015.
 */
public class Utilities {
    public static final String LOG_TAG = Utilities.class.getSimpleName();

    public static final int BUNDESLIGA1 = 394;
    public static final int BUNDESLIGA2 = 395;
    public static final int BUNDESLIGA3 = 403;
    public static final int LIGUE1 = 396;
    public static final int LIGUE2 = 397;
    public static final int PREMIER_LEAGUE = 398;
    public static final int PRIMERA_DIVISION = 399;
    public static final int SEGUNDA_DIVISION = 400;
    public static final int SERIE_A = 401;
    public static final int PRIMERA_LIGA = 402;
    public static final int EREDIVISIE = 404;

    public static String getLeague(int leagueId) {

        switch (leagueId) {
            case BUNDESLIGA1:
            case BUNDESLIGA2:
            case BUNDESLIGA3:
                return "Bundesliga";

            case PREMIER_LEAGUE:
                return "Premier League";

            case SERIE_A :
                return "Serie A";

            case PRIMERA_DIVISION:
                return "Primera Division";

            case SEGUNDA_DIVISION:
                return "Segunda Division";

            default:
                return "";
        }
    }

    public static String getMatchDay(int match_day,int league_num)
    {
        /*
        if(league_num == CHAMPIONS_LEAGUE)
        {
            if (match_day <= 6)
            {
                return "Group Stages, Matchday : 6";
            }
            else if(match_day == 7 || match_day == 8)
            {
                return "First Knockout round";
            }
            else if(match_day == 9 || match_day == 10)
            {
                return "QuarterFinal";
            }
            else if(match_day == 11 || match_day == 12)
            {
                return "SemiFinal";
            }
            else
            {
                return "Final";
            }
        }
        else
        {
            return "Matchday : " + String.valueOf(match_day);
        }
        */
        return "";
    }

    public static String getScores(int homeTeamGoals,int awayTeamGoals) {
        if(homeTeamGoals < 0 || awayTeamGoals < 0)
            return " - ";
        else
            return String.valueOf(homeTeamGoals) + " - " + String.valueOf(awayTeamGoals);
    }

    public static String getDateMillisForQueryFormat(long dateMillis) {
        LocalDate localDate = new LocalDate(dateMillis);
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd");

        String dateForQueryFormat = fmt.print(localDate);
        Log.d(LOG_TAG, "Date for fragment: " + dateForQueryFormat);

        return dateForQueryFormat;
    }

}
