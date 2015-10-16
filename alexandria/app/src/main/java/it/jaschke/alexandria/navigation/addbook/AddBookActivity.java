package it.jaschke.alexandria.navigation.addbook;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import butterknife.Bind;
import butterknife.ButterKnife;
import it.jaschke.alexandria.R;

public class AddBookActivity extends AppCompatActivity {

    public static final String LAUNCH_SCAN = "launch_scan";

    //Variables
    @Bind(R.id.toolbar) Toolbar mToolbar;

    //Controls
    AddBookFragment mFragment;

    /**
     * Factory
     */
    public static Intent launchIntent(Context context, boolean launchScan) {
        Intent intent = new Intent(context, AddBookActivity.class);
        intent.putExtra(LAUNCH_SCAN, launchScan);

        return intent;
    }


    /**
     * Lifecycle methods
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);
        ButterKnife.bind(this);

        //Setup action bar
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Setup fragment
        if(savedInstanceState != null) {
            mFragment = (AddBookFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        } else {
            mFragment = AddBookFragment.newInstance();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, mFragment).commit();
        }

        //Start scan at start
        boolean launchScan = getIntent().getBooleanExtra(LAUNCH_SCAN, false);
        if(savedInstanceState == null && launchScan) {
            launchScanActivity();
        }

    }

    public void launchScanActivity() {
        Intent intent = new Intent(this, ScanBookActivity.class);
        startActivityForResult(intent, 5000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 5000 && resultCode == 5000) {
            String barcode = data.getStringExtra(ScanBookActivity.BARCODE);
            mFragment.setIsbnNumberAndStartSearch(barcode);
        }

    }

    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        super.onActivityReenter(resultCode, data);
    }

}
