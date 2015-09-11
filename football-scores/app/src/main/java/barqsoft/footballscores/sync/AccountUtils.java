package barqsoft.footballscores.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;

/**
 * Created by Andrés on 9/11/15.
 */
public class AccountUtils {

    public static final String AUTHORITY = "barqsoft.footballscores";
    public static final String ACCOUNT_TYPE = "barqsoft.footballscores";
    public static final String ACCOUNT = "Football Scores";

    public static Account createSyncAccount(Context context) {

        Account newAccount = new Account(ACCOUNT, ACCOUNT_TYPE);

        AccountManager accountManager = (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);
        accountManager.addAccountExplicitly(newAccount, null, null);

        return newAccount;
    }

    public static void setSyncAutomatically(Account account, ContentResolver contentResolver) {
        contentResolver.setSyncAutomatically(account, AUTHORITY, true);
    }

}
