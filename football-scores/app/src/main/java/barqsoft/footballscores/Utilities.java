package barqsoft.footballscores;

import android.content.Context;
import android.graphics.drawable.PictureDrawable;
import android.net.Uri;
import android.util.Log;

import com.bumptech.glide.GenericRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.StreamEncoder;
import com.bumptech.glide.load.resource.file.FileToStreamDecoder;
import com.bumptech.glide.samples.svg.SvgDecoder;
import com.bumptech.glide.samples.svg.SvgDrawableTranscoder;
import com.bumptech.glide.samples.svg.SvgSoftwareLayerSetter;
import com.caverock.androidsvg.SVG;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.InputStream;

/**
 * Created by yehya khaled on 3/3/2015.
 */
public class Utilities {
    public static final String LOG_TAG = Utilities.class.getSimpleName();

    public static final int BUNDESLIGA1 = 394;
    public static final int BUNDESLIGA2 = 395;
    public static final int BUNDESLIGA3 = 403;
    public static final int LIGUE1 = 396;
    public static final int LIGUE2 = 397;
    public static final int PREMIER_LEAGUE = 398;
    public static final int PRIMERA_DIVISION = 399;
    public static final int SEGUNDA_DIVISION = 400;
    public static final int SERIE_A = 401;
    public static final int PRIMERA_LIGA = 402;
    public static final int EREDIVISIE = 404;

    public static String getLeague(Context context, int leagueId) {

        switch (leagueId) {
            case BUNDESLIGA1:
            case BUNDESLIGA2:
            case BUNDESLIGA3:
                return context.getString(R.string.bundesliga);

            case PREMIER_LEAGUE:
                return context.getString(R.string.premier_league);

            case SERIE_A :
                return context.getString(R.string.seria_a);

            case PRIMERA_DIVISION:
                return context.getString(R.string.primera_division);

            case SEGUNDA_DIVISION:
                return context.getString(R.string.segunda_division);

            default:
                return "";
        }
    }

    public static String getScores(int homeTeamGoals,int awayTeamGoals) {
        if(homeTeamGoals < 0 || awayTeamGoals < 0)
            return " - ";
        else
            return String.valueOf(homeTeamGoals) + " - " + String.valueOf(awayTeamGoals);
    }

    public static String getDateMillisForQueryFormat(long dateMillis) {
        LocalDate localDate = new LocalDate(dateMillis);
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd");

        String dateForQueryFormat = fmt.print(localDate);
        Log.d(LOG_TAG, "Date for fragment: " + dateForQueryFormat);

        return dateForQueryFormat;
    }

    public static GenericRequestBuilder<Uri, InputStream, SVG, PictureDrawable> getRequestBuilder(Context context) {
        return Glide.with(context)
                .using(Glide.buildStreamModelLoader(Uri.class, context), InputStream.class)
                .from(Uri.class)
                .as(SVG.class)
                .transcode(new SvgDrawableTranscoder(), PictureDrawable.class)
                .sourceEncoder(new StreamEncoder())
                .cacheDecoder(new FileToStreamDecoder<SVG>(new SvgDecoder()))
                .decoder(new SvgDecoder())
                .listener(new SvgSoftwareLayerSetter<Uri>());
    }
}
