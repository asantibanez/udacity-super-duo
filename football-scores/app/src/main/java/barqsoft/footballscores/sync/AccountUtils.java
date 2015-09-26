package barqsoft.footballscores.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.PeriodicSync;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

/**
 * Created by Andrés on 9/11/15.
 */
public class AccountUtils {

    public static final String LOG_TAG = AccountUtils.class.getSimpleName();

    public static final String AUTHORITY = "barqsoft.footballscores";
    public static final String ACCOUNT_TYPE = "barqsoft.footballscores";
    public static final String ACCOUNT = "Football Scores";

    public static final long SECONDS_PER_MINUTE = 60L;
    public static final long SYNC_INTERVAL_IN_MINUTES = 60L * 4L;
    public static final long SYNC_INTERVAL = SYNC_INTERVAL_IN_MINUTES * SECONDS_PER_MINUTE;

    public static Account getAccount() {
        return new Account(ACCOUNT, ACCOUNT_TYPE);
    }

    public static Account createSyncAccount(Context context) {

        Account newAccount = AccountUtils.getAccount();
        AccountManager accountManager = (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        if(accountManager.addAccountExplicitly(newAccount, null, null)) {
            ContentResolver.setIsSyncable(newAccount, AUTHORITY, 1);
            ContentResolver.setSyncAutomatically(newAccount, AUTHORITY, true);
            ContentResolver.addPeriodicSync(newAccount, AUTHORITY, Bundle.EMPTY, SYNC_INTERVAL);
        } else {
            List<PeriodicSync> periodicSyncs = ContentResolver.getPeriodicSyncs(newAccount, AUTHORITY);
            for(PeriodicSync periodicSync : periodicSyncs) {
                Log.d(LOG_TAG, "Periodic Sync info: " + periodicSync.toString());
            }
        }

        return newAccount;
    }

    public static boolean isSyncing() {
        return ContentResolver.isSyncActive(getAccount(), AUTHORITY);
    }

}
