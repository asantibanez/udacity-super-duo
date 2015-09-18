package barqsoft.footballscores;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * Created by yehya khaled on 3/3/2015.
 */
public class Utilities
{
    public static final int SERIE_A = 357;
    public static final int PREMIER_LEGAUE = 354;
    public static final int CHAMPIONS_LEAGUE = 362;
    public static final int PRIMERA_DIVISION = 358;
    public static final int BUNDESLIGA = 351;
    public static String getLeague(int league_num)
    {
        switch (league_num)
        {
            case SERIE_A : return "Seria A";
            case PREMIER_LEGAUE : return "Premier League";
            case CHAMPIONS_LEAGUE : return "UEFA Champions League";
            case PRIMERA_DIVISION : return "Primera Division";
            case BUNDESLIGA : return "Bundesliga";
            default: return "Not known League Please report";
        }
    }
    public static String getMatchDay(int match_day,int league_num)
    {
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
    }

    public static String getScores(int homeTeamGoals,int awayTeamGoals) {
        if(homeTeamGoals < 0 || awayTeamGoals < 0)
            return " - ";
        else
            return String.valueOf(homeTeamGoals) + " - " + String.valueOf(awayTeamGoals);
    }

    public static String getTeamCrestPath(String teamId) {
        return "https://upload.wikimedia.org/wikipedia/de/e/e5/Logo_Arminia_Bielefeld.svg";
        //return "file:// " + Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + teamId + ".svg";
    }



}
