package barqsoft.footballscores;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.PictureDrawable;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import java.io.File;
import java.io.InputStream;

import barqsoft.footballscores.DatabaseContract.ScoresTable;

/**
 * Created by yehya khaled on 2/26/2015.
 */
public class FixturesCursorAdapter extends CursorAdapter {

    public double detail_match_id = 0;
    private String FOOTBALL_SCORES_HASHTAG = "#Football_Scores";

    public FixturesCursorAdapter(Context context, Cursor cursor, int flags) {
        super(context,cursor,flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent)
    {
        View mItem = LayoutInflater.from(context).inflate(R.layout.scores_list_item, parent, false);
        ViewHolder mHolder = new ViewHolder(mItem);
        mItem.setTag(mHolder);
        //Log.v(FetchScoreTask.LOG_TAG,"new View inflated");
        return mItem;
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor)
    {
        final ViewHolder mHolder = (ViewHolder) view.getTag();

        //Match data
        mHolder.match_id = cursor.getDouble(cursor.getColumnIndex(ScoresTable.MATCH_ID));
        mHolder.date.setText(cursor.getString(cursor.getColumnIndex(ScoresTable.TIME_COL)));
        mHolder.score.setText(Utilities.getScores(
                cursor.getInt(cursor.getColumnIndex(ScoresTable.HOME_GOALS_COL)),
                cursor.getInt(cursor.getColumnIndex(ScoresTable.AWAY_GOALS_COL))
        ));
        //Home team
        mHolder.home_name.setText(cursor.getString(cursor.getColumnIndex(ScoresTable.HOME_NAME_COL)));
        //mHolder.home_crest.setImageResource(Utilities.getTeamCrestByTeamName(cursor.getString(cursor.getColumnIndex(ScoresTable.HOME_NAME_COL))));

        String homeTeamId = cursor.getString(cursor.getColumnIndex(ScoresTable.HOME_ID_COL));
        String homeCrestPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + homeTeamId + ".svg";
        SVG.getFromInputStream()

        //Away
        mHolder.away_name.setText(cursor.getString(cursor.getColumnIndex(ScoresTable.AWAY_NAME_COL)));
        mHolder.away_crest.setImageResource(Utilities.getTeamCrestByTeamName(cursor.getString(cursor.getColumnIndex(ScoresTable.AWAY_NAME_COL))));


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
                    context.startActivity(createShareForecastIntent(mHolder.home_name.getText()+" "
                    +mHolder.score.getText()+" "+mHolder.away_name.getText() + " "));
                }
            });
        }
        else
        {
            container.removeAllViews();
        }

    }
    public Intent createShareForecastIntent(String ShareText) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, ShareText + FOOTBALL_SCORES_HASHTAG);
        return shareIntent;
    }

}
