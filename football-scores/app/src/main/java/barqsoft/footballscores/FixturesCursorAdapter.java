package barqsoft.footballscores;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;

import barqsoft.footballscores.provider.FixtureAndTeam;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by yehya khaled on 2/26/2015.
 */
public class FixturesCursorAdapter extends CursorAdapter {

    public static final String LOG_TAG = FixturesCursorAdapter.class.getSimpleName();


    public FixturesCursorAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
    }

    @Override
    public FixtureAndTeam getItem(int position) {
        Cursor cursor = getCursor();
        if(cursor == null || cursor.getCount() == 0)
            return null;

        return FixtureAndTeam.fromCursor(cursor);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_fixture, parent, false);
        ViewHolder mHolder = new ViewHolder(view);
        view.setTag(mHolder);

        return view;
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        final ViewHolder mHolder = (ViewHolder) view.getTag();

        FixtureAndTeam fixtureAndTeam = FixtureAndTeam.fromCursor(cursor);

        //Match data
        String leagueAndMatchDay = Utilities.getLeague(context, fixtureAndTeam.leagueId) + "\n" + context.getString(R.string.match_day) + ": " + fixtureAndTeam.matchDay;
        mHolder.leagueAndMatchDay.setText(leagueAndMatchDay);
        mHolder.matchTime.setText(fixtureAndTeam.matchTime);
        mHolder.matchScore.setText(Utilities.getScores(fixtureAndTeam.homeTeamGoals, fixtureAndTeam.awayTeamGoals));

        //Home team data
        mHolder.homeTeamName.setText(fixtureAndTeam.homeTeamName);
        mHolder.homeTeamCrest.setContentDescription(fixtureAndTeam.homeTeamName);
        if(fixtureAndTeam.homeCrestUrlAvailable())
            Utilities.getRequestBuilder(context)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .load(Uri.parse(fixtureAndTeam.homeTeamCrestUrl))
                    .placeholder(R.drawable.placeholder_crest)
                    .error(R.drawable.placeholder_crest)
                    .into(mHolder.homeTeamCrest);

        //Away team data
        mHolder.awayTeamName.setText(fixtureAndTeam.awayTeamName);
        mHolder.awayTeamCrest.setContentDescription(fixtureAndTeam.awayTeamName);
        if(fixtureAndTeam.awayCrestUrlAvailable())
            Utilities.getRequestBuilder(context)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .load(Uri.parse(fixtureAndTeam.awayTeamCrestUrl))
                    .placeholder(R.drawable.placeholder_crest)
                    .error(R.drawable.placeholder_crest)
                    .into(mHolder.awayTeamCrest);
    }


    /**
     * ViewHolder
     */
    public static class ViewHolder {

        @Bind(R.id.home_team_name) TextView homeTeamName;
        @Bind(R.id.away_team_name) TextView awayTeamName;
        @Bind(R.id.match_score) TextView matchScore;
        @Bind(R.id.match_date) TextView matchTime;
        @Bind(R.id.home_team_crest) ImageView homeTeamCrest;
        @Bind(R.id.away_team_crest) ImageView awayTeamCrest;
        @Bind(R.id.league_and_match_day) TextView leagueAndMatchDay;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
