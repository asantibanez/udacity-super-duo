package barqsoft.footballscores;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import barqsoft.footballscores.provider.DatabaseContract;
import barqsoft.footballscores.provider.FixtureAndTeam;
import barqsoft.footballscores.provider.FootballScoresProvider;
import barqsoft.footballscores.sync.AccountUtils;
import butterknife.Bind;
import butterknife.ButterKnife;

public class FixturesFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemClickListener {

    public static final String LOG_TAG = FixturesFragment.class.getSimpleName();

    //Constants
    private static final String ARGS_DATE_MILLIS = "date_millis";

    //Variables
    public long mDateMillis;
    public FixturesCursorAdapter mAdapter;
    public static final int LOADER_ID = 2000;

    //Controls
    @Bind(R.id.progress_bar) ProgressBar mProgressBarView;
    @Bind(R.id.list) ListView mListView;
    @Bind(R.id.error_view) LinearLayout mErrorView;
    @Bind(R.id.error_image) ImageView mErrorImage;
    @Bind(R.id.error_message) TextView mErrorMessage;


    /**
     * Constructors and factories
     */
    public static FixturesFragment newInstance(long dateMillis) {
        FixturesFragment fragment = new FixturesFragment();
        Bundle args = new Bundle();
        args.putLong(ARGS_DATE_MILLIS, dateMillis);
        fragment.setArguments(args);

        return fragment;
    }

    public FixturesFragment() {}


    /**
     * Lifecycle methods
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDateMillis = getArguments().getLong(ARGS_DATE_MILLIS);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_fixtures, container, false);
        ButterKnife.bind(this, rootView);

        mAdapter = new FixturesCursorAdapter(getActivity(), null, 0);
        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(this);

        getLoaderManager().initLoader(LOADER_ID, null, this);

        return rootView;
    }




    /**
     * Cursor callbacks
     */
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        mErrorView.setVisibility(View.GONE);
        mProgressBarView.setVisibility(View.VISIBLE);

        return new CursorLoader(
                getActivity(),
                FootballScoresProvider.FIXTURES_AND_TEAMS_URI,
                DatabaseContract.FixturesAndTeamsView.projection,
                DatabaseContract.FixturesTable.DATE_COL + " = ?",
                new String[]{ Utilities.getDateMillisForQueryFormat(mDateMillis) },
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        mProgressBarView.setVisibility(View.GONE);

        mAdapter.swapCursor(cursor);

        //Cursor is available
        if(cursor != null) {
            cursor.setNotificationUri(getContext().getContentResolver(), FootballScoresProvider.FIXTURES_URI);
            cursor.setNotificationUri(getContext().getContentResolver(), FootballScoresProvider.TEAMS_URI);

            //No data found
            if(cursor.getCount() > 0) {
                mErrorView.setVisibility(View.GONE);
            } else {
                mErrorImage.setImageResource(R.drawable.ic_no_fixtures_for_day);
                mErrorMessage.setText(R.string.no_fixtures_for_day);
                mErrorView.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mAdapter.swapCursor(null);
    }


    /**
     * Item click callbacks
     */
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        FixtureAndTeam fixtureAndTeam = mAdapter.getItem(position);

        if(fixtureAndTeam == null)
            return;

        String FOOTBALL_SCORES_HASHTAG = "#Football_Scores";

        String shareText =
                fixtureAndTeam.homeTeamName + " " +
                "(" + fixtureAndTeam.getHomeTeamGoals() + " - " + fixtureAndTeam.getAwayTeamGoals() + ") " +
                fixtureAndTeam.awayTeamName + " " +
                FOOTBALL_SCORES_HASHTAG;

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
        getContext().startActivity(Intent.createChooser(shareIntent, "Compartir"));
    }
}
