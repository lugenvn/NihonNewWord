package self.lugen.nihonnewword.datamanager;

import android.content.Context;
import android.os.Parcel;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

import self.lugen.nihonnewword.utils.Constants;
import self.lugen.nihonnewword.utils.Utils;

public class KanaDataUtils extends LessonDataUtilsBase {
    static final String LESSON = "kana_lesson_";

    public static final int POS_KANA = 0;
    public static final int POS_ROMAJI = 1;

    private String lessonId;

    public KanaDataUtils(Context context, String lesson) {
        super();
        this.lessonId = lesson;
        initData(context);
    }

    @Override
    protected void initData(Context context) {
        super.initData(context);
    }

    @Override
    protected int getCurrentListLength(Context context) {
        int id = Utils.getResourceId(context, LESSON + lessonId + Constants.KANA, Constants.STRING_ARRAY, context
                .getApplicationContext
                        ().getPackageName());
        String[] kana = context.getResources().getStringArray(id);
        Log.i(getClass().getName(), "getCurrentListLength = " + kana.length);
        return kana.length;
    }

    @Override
    protected ArrayList<String> getRealData(Context context, int pos) {
        Log.i(getClass().getName(), "getRealData = " + pos);

        ArrayList<String> returnData = new ArrayList<>();
        ArrayList<String> kana = new ArrayList<>();
        ArrayList<String> meaning = new ArrayList<>();

        int id = Utils.getResourceId(context, createFileID(LESSON, lessonId, "", Constants.KANA), Constants
                        .STRING_ARRAY,
                context
                        .getApplicationContext
                                ().getPackageName());
        kana.addAll(Arrays.asList(context.getResources().getStringArray(id)));
        id = Utils.getResourceId(context, createFileID(LESSON, lessonId, "", Constants.ROMAJI), Constants
                        .STRING_ARRAY,
                context
                        .getApplicationContext()
                        .getPackageName());
        meaning.addAll(Arrays.asList(context.getResources().getStringArray(id)));
        returnData.add(kana.get(pos));
        returnData.add(meaning.get(pos));
        return returnData;
    }

    private String createFileID(String lesson, String lessonID, String groupID, String valueType) {
        String ret = lesson + lessonID + groupID + valueType;
        Log.i(getClass().getName(), "createFileID = " + ret);
        return ret;
    }

    // parcelable method
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(lessonId);
    }

    protected KanaDataUtils(Parcel in) {
        super(in);
        lessonId = in.readString();
    }

    public static final Creator<KanaDataUtils> CREATOR = new Creator<KanaDataUtils>() {
        @Override
        public KanaDataUtils createFromParcel(Parcel source) {
            return new KanaDataUtils(source);
        }

        @Override
        public KanaDataUtils[] newArray(int size) {
            return new KanaDataUtils[size];
        }
    };
}
