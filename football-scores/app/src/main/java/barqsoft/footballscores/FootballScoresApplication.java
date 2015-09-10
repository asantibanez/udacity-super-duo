package barqsoft.footballscores;

import android.app.Application;

import net.danlew.android.joda.JodaTimeAndroid;

/**
 * Created by Andrés on 9/10/15.
 */
public class FootballScoresApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        JodaTimeAndroid.init(this);
    }

}
