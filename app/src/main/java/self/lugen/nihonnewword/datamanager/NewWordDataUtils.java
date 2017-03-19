package self.lugen.nihonnewword.datamanager;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Random;

import self.lugen.nihonnewword.utils.Constants;
import self.lugen.nihonnewword.utils.Utils;

public class NewWordDataUtils extends LessonDataUtilsBase{
    static final String LESSON = "lesson";

    public static final int POS_KANA = 0;
    public static final int POS_KANJI = 1;
    public static final int POS_MEANING = 2;

    private int lessonId;

    public NewWordDataUtils(Context context, int lesson) {
        super();
        this.lessonId = lesson;
        initData(context);
    }

    @Override
    protected int getCurrentListLength(Context context) {
        int id = Utils.getResourceId(context, LESSON + lessonId + Constants.KANA, Constants.STRING_ARRAY,context.getApplicationContext().getPackageName());
        String[] kana = context.getResources().getStringArray(id);
        return kana.length;
    }

    @Override
    protected ArrayList<String> getRealData(Context context, int pos){
        ArrayList<String> returnData = new ArrayList<>();

        int id = Utils.getResourceId(context, LESSON + lessonId + Constants.KANA, Constants.STRING_ARRAY,context.getApplicationContext().getPackageName());
        String[] kana = context.getResources().getStringArray(id);

        id = Utils.getResourceId(context, LESSON + lessonId + Constants.KANJI, Constants.STRING_ARRAY,context.getApplicationContext().getPackageName());
        String[] kanji = context.getResources().getStringArray(id);

        id = Utils.getResourceId(context, LESSON + lessonId + Constants.MEANING, Constants.STRING_ARRAY,context.getApplicationContext().getPackageName());
        String[] meaning = context.getResources().getStringArray(id);

        returnData.add(kana[pos]);
        returnData.add(kanji[pos]);
        returnData.add(meaning[pos]);
        return returnData;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(lessonId);
    }

    private NewWordDataUtils(Parcel in) {
        super(in);
        lessonId = in.readInt();
    }

    public static final Parcelable.Creator<NewWordDataUtils> CREATOR = new Parcelable.Creator<NewWordDataUtils>() {
        @Override
        public NewWordDataUtils createFromParcel(Parcel source) {
            return new NewWordDataUtils(source);
        }

        @Override
        public NewWordDataUtils[] newArray(int size) {
            return new NewWordDataUtils[size];
        }
    };
}
