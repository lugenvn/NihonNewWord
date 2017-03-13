package self.lugen.nihonnewword.utils;

import android.content.Context;

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
}
