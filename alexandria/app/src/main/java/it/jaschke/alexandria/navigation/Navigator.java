package it.jaschke.alexandria.navigation;

import android.content.Context;

import it.jaschke.alexandria.navigation.bookdetail.BookDetailActivity;
import it.jaschke.alexandria.navigation.addbook.AddBookActivity;

/**
 * Created by asantibanez on 10/1/15.
 */
public class Navigator {

    public static void goToIsbnRegistration(Context context) {
        context.startActivity(AddBookActivity.launchIntent(context));
    }

    public static void goToBookDetail(Context context, long bookId) {
        context.startActivity(BookDetailActivity.launchIntent(context, bookId));
    }

}
