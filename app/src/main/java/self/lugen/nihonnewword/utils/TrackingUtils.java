package self.lugen.nihonnewword.utils;

import android.content.Context;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

/**
 * Created by hungnl on 10/12/2017.
 */

public class TrackingUtils {
    public static final String ID_BUTTON = "button";
    public static final String ID_SCREEN = "screen";

    public static final String SCREEN_MAIN = "main";
    public static final String SCREEN_NEW_WORD_LIST = "new word list";
    public static final String SCREEN_NEW_WORD_DETAIL = "new word detail";
    public static final String SCREEN_KANJI_LIST = "kanji list";
    public static final String SCREEN_KANJI_DETAIL = "kanji detail";
    public static final String DIALOG_SESSION = "session";
    public static final String DIALOF_GROUP = "main";

    public static final String MAIN_NEW_WORD =  "new word";
    public static final String MAIN_KANJI =  "kanji";
    public static final String LESSON_NEW_WORD = "lesson new word ";
    public static final String LESSON_KANJI = "lesson kanji ";
    public static final String NEXT = "next";
    public static final String SESSION = "session";
    public static final String GROUP = "group";
    public static final String MEANING = "meaning";
    public static final String KANJI = "kanji";
    public static final String KANA = "kana";
    public static final String READING = "reading";
    public static final String SETTING = "setting";

    public static void trackingButton(Context context, String id, String screen, String itemName) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, id);
        bundle.putString(FirebaseAnalytics.Param.LOCATION, screen);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, itemName);
        FirebaseAnalytics.getInstance(context).logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }
    public static void trackingDialogButton(Context context, String id, String screen, String dialog, String itemName,String content) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, id);
        bundle.putString(FirebaseAnalytics.Param.LOCATION, screen);
        bundle.putString(FirebaseAnalytics.Param.DESTINATION, dialog);
        bundle.putString(FirebaseAnalytics.Param.CONTENT, content);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, itemName);
        FirebaseAnalytics.getInstance(context).logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }
}
