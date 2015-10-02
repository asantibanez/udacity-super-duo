package it.jaschke.alexandria.navigation.addbook;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import butterknife.Bind;
import butterknife.ButterKnife;
import it.jaschke.alexandria.R;

public class AddBookActivity extends AppCompatActivity {

    //Variables
    @Bind(R.id.toolbar) Toolbar mToolbar;

    //Controls
    AddBookFragment mFragment;

    /**
     * Factory
     */
    public static Intent launchIntent(Context context) {
        return new Intent(context, AddBookActivity.class);
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
            AddBookFragment mFragment = AddBookFragment.newInstance();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, mFragment).commit();
        }

    }

}
