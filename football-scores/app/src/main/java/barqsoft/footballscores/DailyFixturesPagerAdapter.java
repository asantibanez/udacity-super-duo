package barqsoft.footballscores;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import org.joda.time.LocalDate;

/**
 * Created by Andrés on 9/11/15.
 */
public class DailyFixturesPagerAdapter extends FragmentStatePagerAdapter {

    public static final String LOG_TAG = DailyFixturesPagerAdapter.class.getSimpleName();

    public static final int NUM_PAGES = 5;

    LocalDate mLocalDate;

    public DailyFixturesPagerAdapter(FragmentManager fm) {
        super(fm);
        mLocalDate = new LocalDate();
    }

    @Override
    public Fragment getItem(int position) {
        long millis = getLocalDateForItem(position).toDateTimeAtStartOfDay().getMillis();
        return FixturesFragment.newInstance(millis);
    }

    @Override
    public int getCount() {
        return NUM_PAGES;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch(position) {
            case 0:
                return "Yesterday";

            case 1:
                return "Today";

            case 2:
                return "Tomorrow";

            default:
                return "Other day";
        }
    }

    public LocalDate getLocalDateForItem(int position) {
        LocalDate localDate = new LocalDate();
        localDate = localDate.plusDays(position - 1);

        Log.d(LOG_TAG, "Position: " + position + " / " + localDate.toString());

        return localDate;
    }
}
