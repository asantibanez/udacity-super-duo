package barqsoft.footballscores.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by yehya khaled on 3/2/2015.
 */
public class ScoresSyncService extends Service
{
    public static final String LOG_TAG = ScoresSyncService.class.getSimpleName();

    private static ScoresSyncAdapter mSyncAdapter = null;
    private static final Object mSyncAdapterLock = new Object();

    @Override
    public void onCreate() {
        synchronized (mSyncAdapterLock) {
            if (mSyncAdapter == null) {
                mSyncAdapter = new ScoresSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mSyncAdapter.getSyncAdapterBinder();
    }
}

