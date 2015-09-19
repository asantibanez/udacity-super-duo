package barqsoft.footballscores;

import android.accounts.Account;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import barqsoft.footballscores.sync.AccountUtils;
import barqsoft.footballscores.sync.ScoresSyncAdapter;
import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
{
    public static String LOG_TAG = MainActivity.class.getSimpleName();
    public static final boolean DEBUG = true;

    //Variables
    Account mAccount;
    DailyScoresFragmentPagerAdapter mAdapter;

    //Controls
    @Bind(R.id.toolbar) Toolbar mToolbarView;
    @Bind(R.id.tabs) TabLayout mTabs;
    @Bind(R.id.pager) ViewPager mPager;

    public static int selected_match_id;
    public static int current_fragment = 2;

    private final String save_tag = "Save Test";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mAccount = AccountUtils.createSyncAccount(this);
        ScoresSyncAdapter.syncImmediately(this);

        setSupportActionBar(mToolbarView);

        mAdapter = new DailyScoresFragmentPagerAdapter(this, getSupportFragmentManager());
        mPager.setAdapter(mAdapter);
        mTabs.setupWithViewPager(mPager);

        if(savedInstanceState == null) {
            //Get Today's fixtures
            mTabs.getTabAt(1).select();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_about) {
            Intent start_about = new Intent(this, AboutActivity.class);
            startActivity(start_about);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
