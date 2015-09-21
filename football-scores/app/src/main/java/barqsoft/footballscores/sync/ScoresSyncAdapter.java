package barqsoft.footballscores.sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncResult;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.bumptech.glide.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;
import java.util.Vector;

import barqsoft.footballscores.Utilities;
import barqsoft.footballscores.provider.DatabaseContract;
import barqsoft.footballscores.R;
import barqsoft.footballscores.endpoints.FootballDataService;
import barqsoft.footballscores.endpoints.GetTeamInformationResponse;
import barqsoft.footballscores.provider.Fixture;
import barqsoft.footballscores.provider.FootballScoresProvider;
import barqsoft.footballscores.provider.Team;

/**
 * Created by Andrés on 9/11/15.
 */
public class ScoresSyncAdapter extends AbstractThreadedSyncAdapter {

    public static final String LOG_TAG = ScoresSyncAdapter.class.getSimpleName();
    public static final boolean DEBUG = true;

    private String mApiKey;

    public ScoresSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    @Override
    public void onPerformSync(Account account, Bundle bundle, String authority, ContentProviderClient contentProviderClient, SyncResult syncResult) {
        //Get api key
        mApiKey = getContext().getString(R.string.api_key);
        if(DEBUG)
            Log.d(LOG_TAG, "Api Key: " + mApiKey);

        //Get next 3 days data including today
        getData("n4");

        //Get previous day data
        getData("p2");

        //Get team logos
        getTeamsCrestsUrls();
    }

    private void getData (String timeFrame) {
        //Creating fetch URL
        final String BASE_URL = "http://api.football-data.org/alpha/fixtures"; //Base URL
        final String QUERY_TIME_FRAME = "timeFrame"; //Time Frame parameter to determine days
        //final String QUERY_MATCH_DAY = "matchday";

        Uri fetch_build = Uri.parse(BASE_URL).buildUpon().
                appendQueryParameter(QUERY_TIME_FRAME, timeFrame).build();
        if(DEBUG)
            Log.d(LOG_TAG, "The url we are looking at is: " + fetch_build.toString()); //log spam

        HttpURLConnection connection = null;
        BufferedReader reader = null;
        String jsonData = null;
        //Opening Connection
        try {
            URL fetch = new URL(fetch_build.toString());
            connection = (HttpURLConnection) fetch.openConnection();
            connection.setRequestMethod("GET");

            //Api key for service in string resource
            connection.addRequestProperty("X-Auth-Token", mApiKey);

            //Connect to api
            connection.connect();

            // Read the input stream into a String
            InputStream inputStream = connection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }
            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return;
            }
            jsonData = buffer.toString();
            if(DEBUG)
                Log.d(LOG_TAG, jsonData);
        }
        catch (Exception e)
        {
            Log.e(LOG_TAG,"Exception here: " + e.getMessage());
            e.printStackTrace();
        }
        finally {
            if(connection != null)
            {
                connection.disconnect();
            }
            if (reader != null)
            {
                try {
                    reader.close();
                }
                catch (IOException e)
                {
                    Log.e(LOG_TAG,"Error Closing Stream");
                }
            }
        }
        try {
            if (jsonData != null) {
                //This bit is to check if the data contains any matches. If not, we call processJson on the dummy data
                JSONArray matches = new JSONObject(jsonData).getJSONArray("fixtures");
                if (matches.length() == 0) {
                    //if there is no data, call the function on dummy data
                    //this is expected behavior during the off season.
                    processJSONdata(getContext().getString(R.string.dummy_data), getContext(), false);
                    return;
                }


                processJSONdata(jsonData, getContext(), true);
            } else {
                //Could not Connect
                Log.d(LOG_TAG, "Could not connect to server.");
            }
        }
        catch(Exception e)
        {
            Log.e(LOG_TAG, "Json parsing error: " + e.getMessage());
        }
    }
    private void processJSONdata (String JSONdata,Context mContext, boolean isReal)
    {
        //JSON data
        // This set of league codes is for the 2015/2016 season. In fall of 2016, they will need to
        // be updated. Feel free to use the codes
        final String BUNDESLIGA1 = "394";
        final String BUNDESLIGA2 = "395";
        final String LIGUE1 = "396";
        final String LIGUE2 = "397";
        final String PREMIER_LEAGUE = "398";
        final String PRIMERA_DIVISION = "399";
        final String SEGUNDA_DIVISION = "400";
        final String SERIE_A = "401";
        final String PRIMERA_LIGA = "402";
        final String Bundesliga3 = "403";
        final String EREDIVISIE = "404";


        //Response Tokens
        //Response links
        final String SEASON_LINK = "http://api.football-data.org/alpha/soccerseasons/";
        final String MATCH_LINK = "http://api.football-data.org/alpha/fixtures/";
        final String HOME_TEAM_LINK = "http://api.football-data.org/alpha/teams/";
        final String AWAY_TEAM_LINK = "http://api.football-data.org/alpha/teams/";
        //Link objects
        final String FIXTURES_OBJECT = "fixtures";
        final String LINKS_OBJECT = "_links";
        final String SOCCER_SEASON_OBJECT = "soccerseason";
        final String SELF_OBJECT = "self";
        final String HOME_TEAM_OBJECT = "homeTeam";
        final String AWAY_TEAM_OBJECT = "awayTeam";
        //JSON data tokens
        final String MATCH_DATE = "date";
        final String HOME_TEAM_NAME = "homeTeamName";
        final String AWAY_TEAM_NAME = "awayTeamName";
        final String RESULT = "result";
        final String HOME_TEAM_GOALS = "goalsHomeTeam";
        final String AWAY_TEAM_GOALS = "goalsAwayTeam";
        final String MATCH_DAY = "matchday";


        //Match data
        String match_id = null;
        String match_day = null;
        String League = null;
        String mDate = null;
        String mTime = null;
        //Home team data
        String homeId = null;
        String homeName = null;
        String homeGoals = null;
        //Away tema data
        String awayId = null;
        String awayName = null;
        String awayGoals = null;



        try {
            JSONArray matches = new JSONObject(JSONdata).getJSONArray(FIXTURES_OBJECT);


            //ContentValues to be inserted
            Vector<ContentValues> allMatchesValues = new Vector <ContentValues> (matches.length());
            for(int i = 0;i < matches.length();i++)
            {

                JSONObject match_data = matches.getJSONObject(i);
                League = match_data.getJSONObject(LINKS_OBJECT).getJSONObject(SOCCER_SEASON_OBJECT).
                        getString("href");
                League = League.replace(SEASON_LINK,"");
                //This if statement controls which leagues we're interested in the data from.
                //add leagues here in order to have them be added to the DB.
                // If you are finding no data in the app, check that this contains all the leagues.
                // If it doesn't, that can cause an empty DB, bypassing the dummy data routine.
                int leagueId = Integer.parseInt(League);
                if(     leagueId == Utilities.PREMIER_LEAGUE    ||
                        leagueId == Utilities.SERIE_A           ||
                        leagueId == Utilities.BUNDESLIGA1       ||
                        leagueId == Utilities.BUNDESLIGA2       ||
                        leagueId == Utilities.PRIMERA_DIVISION    )
                {
                    match_id = match_data.getJSONObject(LINKS_OBJECT).getJSONObject(SELF_OBJECT).
                            getString("href");
                    match_id = match_id.replace(MATCH_LINK, "");
                    if(!isReal){
                        //This if statement changes the match ID of the dummy data so that it all goes into the database
                        match_id=match_id+Integer.toString(i);
                    }

                    mDate = match_data.getString(MATCH_DATE);
                    mTime = mDate.substring(mDate.indexOf("T") + 1, mDate.indexOf("Z"));
                    mDate = mDate.substring(0,mDate.indexOf("T"));
                    SimpleDateFormat match_date = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");
                    match_date.setTimeZone(TimeZone.getTimeZone("UTC"));
                    try {
                        Date parseddate = match_date.parse(mDate+mTime);
                        SimpleDateFormat new_date = new SimpleDateFormat("yyyy-MM-dd:HH:mm");
                        new_date.setTimeZone(TimeZone.getDefault());
                        mDate = new_date.format(parseddate);
                        mTime = mDate.substring(mDate.indexOf(":") + 1);
                        mDate = mDate.substring(0,mDate.indexOf(":"));

                        if(!isReal){
                            //This if statement changes the dummy data's date to match our current date range.
                            Date fragmentdate = new Date(System.currentTimeMillis()+((i-2)*86400000));
                            SimpleDateFormat mformat = new SimpleDateFormat("yyyy-MM-dd");
                            mDate=mformat.format(fragmentdate);
                        }
                    }
                    catch (Exception e)
                    {
                        Log.d(LOG_TAG, "error here!");
                        Log.e(LOG_TAG,e.getMessage());
                    }

                    //Extract match data
                    //Home team
                    homeId = match_data.getJSONObject(LINKS_OBJECT).getJSONObject(HOME_TEAM_OBJECT).getString("href").replace(HOME_TEAM_LINK, "");
                    homeName = match_data.getString(HOME_TEAM_NAME);
                    homeGoals = match_data.getJSONObject(RESULT).getString(HOME_TEAM_GOALS);
                    //Away team
                    awayId = match_data.getJSONObject(LINKS_OBJECT).getJSONObject(AWAY_TEAM_OBJECT).getString("href").replace(AWAY_TEAM_LINK, "");
                    awayName = match_data.getString(AWAY_TEAM_NAME);
                    awayGoals = match_data.getJSONObject(RESULT).getString(AWAY_TEAM_GOALS);
                    //Match
                    match_day = match_data.getString(MATCH_DAY);
                    if(DEBUG) {
                        Log.v(LOG_TAG, "******************************");
                        Log.v(LOG_TAG, "Match: " + match_id);
                        Log.v(LOG_TAG, "Date: " + mDate);
                        Log.v(LOG_TAG, "Time: " + mTime);
                        Log.v(LOG_TAG, "Home: " + homeId + " " + homeName);
                        Log.v(LOG_TAG, "Away: " + awayId + " " + awayName);
                        Log.v(LOG_TAG, "Goals: " + homeGoals + " - " + awayGoals);
                    }

                    //Save teams if required
                    Team team;
                    ContentResolver contentResolver = getContext().getContentResolver();
                    //Home team
                    team = Team.withId(contentResolver, homeId);
                    if(team == null)
                        Team.save(contentResolver, homeId, homeName, "");

                    //Away team
                    team = Team.withId(contentResolver, awayId);
                    if(team == null)
                        Team.save(contentResolver, awayId, awayName, "");

                    //Save fixture
                    Fixture.save(
                            contentResolver, match_id, mDate, mTime,
                            homeId, homeName, homeGoals, awayId,
                            awayName, awayGoals, League, match_day
                    );

                }
            }

        }
        catch (JSONException e)
        {
            Log.e(LOG_TAG,e.getMessage());
        }

    }

    public void getTeamsCrestsUrls() {
        Cursor cursor = getContext().getContentResolver().query(
                FootballScoresProvider.TEAMS_URI,
                null,
                DatabaseContract.TeamsTable.TEAM_CREST_URL + " = '' ",
                null,
                null
        );

        if(cursor == null)
            return;

        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            Team team = Team.fromCursor(cursor);

            try {
                GetTeamInformationResponse response = new FootballDataService().getTeamInformation(mApiKey, team.id);
                if(response != null && response.crestUrl != null && response.crestUrl.length() > 0) {
                    Team.save(getContext().getContentResolver(), team.id, team.name, response.crestUrl);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            cursor.moveToNext();
        }

        cursor.close();
    }

    public static void syncImmediately(Context context) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(AccountUtils.createSyncAccount(context), AccountUtils.AUTHORITY, bundle);
    }

}
