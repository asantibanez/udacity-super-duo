package barqsoft.footballscores.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.PeriodicSync;
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

    public static Account getAccount(Context context) {
        return  new Account(ACCOUNT, ACCOUNT_TYPE);
    }

    public static Account createSyncAccount(Context context) {
        ContentResolver contentResolver = context.getContentResolver();

        Account newAccount = AccountUtils.getAccount(context);
        AccountManager accountManager = (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        if(accountManager.addAccountExplicitly(newAccount, null, null)) {
            contentResolver.setIsSyncable(newAccount, AUTHORITY, 1);
            contentResolver.setSyncAutomatically(newAccount, AUTHORITY, true);
        } else {
            List<PeriodicSync> periodicSyncs = contentResolver.getPeriodicSyncs(newAccount, AUTHORITY);
            for(PeriodicSync periodicSync : periodicSyncs) {
                Log.d(LOG_TAG, "Periodic Sync info: " + periodicSync.toString());
            }
        }

        return newAccount;
    }

    public static boolean isSyncing(Context context) {
        return context.getContentResolver().isSyncActive(getAccount(context), AUTHORITY);
    }

}
