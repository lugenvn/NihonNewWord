package self.lugen.nihonnewword.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by lugen on 3/11/17.
 */

public class Utils {

    public static int getResourceId(Context context, String pVariableName, String pResourcename, String pPackageName)
    {
        try {
            return context.getResources().getIdentifier(pVariableName, pResourcename, pPackageName);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static void putSharePreferenceBooleanValue(Context context, String key, Boolean value) {
        SharedPreferences pref = context.getApplicationContext().getSharedPreferences(Constants.APP_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static boolean getSharePreferenceBooleanValue(Context context, String key) {
        SharedPreferences pref = context.getApplicationContext().getSharedPreferences(Constants.APP_PREF, Context.MODE_PRIVATE);
        boolean returnValue = pref.getBoolean(key, false);
        return returnValue;
    }
}
