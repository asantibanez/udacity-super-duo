package barqsoft.footballscores;

import android.accounts.Account;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import barqsoft.footballscores.endpoints.FootballDataService;
import barqsoft.footballscores.sync.AccountUtils;
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
    private PagerFragment my_main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mAccount = AccountUtils.createSyncAccount(this);
        AccountUtils.setSyncAutomatically(mAccount, getContentResolver());
        getContentResolver().requestSync(mAccount, AccountUtils.AUTHORITY, new Bundle());

        setSupportActionBar(mToolbarView);

        mAdapter = new DailyScoresFragmentPagerAdapter(this, getSupportFragmentManager());
        mPager.setAdapter(mAdapter);
        mTabs.setupWithViewPager(mPager);

        /*
        if (savedInstanceState == null) {
            my_main = new PagerFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, my_main)
                    .commit();
        }
        */
    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about)
        {
            Intent start_about = new Intent(this,AboutActivity.class);
            startActivity(start_about);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        Log.v(save_tag,"will save");
        Log.v(save_tag,"fragment: "+String.valueOf(my_main.mPagerHandler.getCurrentItem()));
        Log.v(save_tag,"selected id: "+selected_match_id);
        outState.putInt("Pager_Current",my_main.mPagerHandler.getCurrentItem());
        outState.putInt("Selected_match",selected_match_id);
        getSupportFragmentManager().putFragment(outState,"my_main",my_main);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        Log.v(save_tag,"will retrive");
        Log.v(save_tag,"fragment: "+String.valueOf(savedInstanceState.getInt("Pager_Current")));
        Log.v(save_tag,"selected id: "+savedInstanceState.getInt("Selected_match"));
        current_fragment = savedInstanceState.getInt("Pager_Current");
        selected_match_id = savedInstanceState.getInt("Selected_match");
        my_main = (PagerFragment) getSupportFragmentManager().getFragment(savedInstanceState,"my_main");
        super.onRestoreInstanceState(savedInstanceState);
    }
    */
}
