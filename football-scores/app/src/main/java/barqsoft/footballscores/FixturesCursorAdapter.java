package barqsoft.footballscores;

import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.PictureDrawable;
import android.net.Uri;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.GenericRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.StreamEncoder;
import com.bumptech.glide.load.resource.file.FileToStreamDecoder;
import com.bumptech.glide.samples.svg.SvgDecoder;
import com.bumptech.glide.samples.svg.SvgDrawableTranscoder;
import com.bumptech.glide.samples.svg.SvgSoftwareLayerSetter;
import com.caverock.androidsvg.SVG;

import java.io.InputStream;

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

    public GenericRequestBuilder<Uri, InputStream, SVG, PictureDrawable> getRequestBuilder(Context context) {
        return Glide.with(context)
                .using(Glide.buildStreamModelLoader(Uri.class, context), InputStream.class)
                .from(Uri.class)
                .as(SVG.class)
                .transcode(new SvgDrawableTranscoder(), PictureDrawable.class)
                .sourceEncoder(new StreamEncoder())
                .cacheDecoder(new FileToStreamDecoder<SVG>(new SvgDecoder()))
                .decoder(new SvgDecoder())
                .listener(new SvgSoftwareLayerSetter<Uri>());
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
        mHolder.leagueName.setText(Utilities.getLeague(context, fixtureAndTeam.leagueId));
        mHolder.matchTime.setText(fixtureAndTeam.matchTime);
        mHolder.matchScore.setText(Utilities.getScores(fixtureAndTeam.homeTeamGoals, fixtureAndTeam.awayTeamGoals));

        //Home team data
        mHolder.homeTeamName.setText(fixtureAndTeam.homeTeamName);
        mHolder.homeTeamCrest.setContentDescription(fixtureAndTeam.homeTeamName);
        if(fixtureAndTeam.homeCrestUrlAvailable())
            getRequestBuilder(context)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .load(Uri.parse(fixtureAndTeam.homeTeamCrestUrl))
                    .placeholder(R.drawable.placeholder_crest)
                    .error(R.drawable.placeholder_crest)
                    .into(mHolder.homeTeamCrest);

        //Away team data
        mHolder.awayTeamName.setText(fixtureAndTeam.awayTeamName);
        mHolder.awayTeamCrest.setContentDescription(fixtureAndTeam.awayTeamName);
        if(fixtureAndTeam.awayCrestUrlAvailable())
            getRequestBuilder(context)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .load(Uri.parse(fixtureAndTeam.awayTeamCrestUrl))
                    .placeholder(R.drawable.placeholder_crest)
                    .error(R.drawable.placeholder_crest)
                    .into(mHolder.awayTeamCrest);

        /*
        LayoutInflater vi = (LayoutInflater) context.getApplicationContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = vi.inflate(R.layout.detail_fragment, null);
        ViewGroup container = (ViewGroup) view.findViewById(R.id.details_fragment_container);
        if(mHolder.match_id == detail_match_id)
        {
            //Log.v(FetchScoreTask.LOG_TAG,"will insert extraView");

            container.addView(v, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                    , ViewGroup.LayoutParams.MATCH_PARENT));
            TextView match_day = (TextView) v.findViewById(R.id.matchday_textview);
            match_day.setText(Utilities.getMatchDay(cursor.getInt(cursor.getColumnIndex(ScoresTable.MATCH_DAY)),
                    cursor.getInt(cursor.getColumnIndex(ScoresTable.LEAGUE_COL))));
            TextView league = (TextView) v.findViewById(R.id.league_textview);
            league.setText(Utilities.getLeague(cursor.getInt(cursor.getColumnIndex(ScoresTable.LEAGUE_COL))));
            Button share_button = (Button) v.findViewById(R.id.share_button);
            share_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    //add Share Action
                    context.startActivity(createShareForecastIntent(mHolder.mAwayTeamName.getText()+" "
                    +mHolder.mMatchScore.getText()+" "+mHolder.away_name.getText() + " "));
                }
            });
        }
        else
        {
            container.removeAllViews();
        }
        */

    }

    /*
    public Intent createShareForecastIntent(String ShareText) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        String FOOTBALL_SCORES_HASHTAG = "#Football_Scores";
        shareIntent.putExtra(Intent.EXTRA_TEXT, ShareText + FOOTBALL_SCORES_HASHTAG);
        return shareIntent;
    }
    */


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
        @Bind(R.id.league_name) TextView leagueName;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
