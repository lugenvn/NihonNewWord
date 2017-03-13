package self.lugen.nihonnewword.utils;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Random;

public class KanjiDataUtils implements Parcelable {
    static final String LESSON = "kanji_lesson_";
    static final String KANJI = "_kanji";
    static final String MEANING = "_meaning";

    static final String STRING_ARRAY = "array";

    private ArrayList<Integer> availBank;
    private String lessonId;

    public static final int POS_KANJI = 0;
    public static final int POS_MEANING = 1;

    public KanjiDataUtils(Context context, String lesson) {
        this.lessonId = lesson;
        availBank = new ArrayList<>();
        setup(context);
    }

    public ArrayList<String> getCard(Context context) {
        if (availBank.isEmpty()) setup(context);
        Random r = new Random();
        int pos = r.nextInt(availBank.size());
        ArrayList<String> returnValue = getRealData(context, availBank.get(pos));
        availBank.remove(pos);
        return returnValue;
    }

    private void setup(Context context){
        int id = Utils.getResourceId(context, LESSON + lessonId + KANJI, STRING_ARRAY,context.getApplicationContext().getPackageName());
        String[] kana = context.getResources().getStringArray(id);

        for (int i = 0; i < kana.length; i++) {
            availBank.add(i);
        }
    }

    private ArrayList<String> getRealData(Context context, int pos){
        ArrayList<String> returnData = new ArrayList<>();

        int id = Utils.getResourceId(context, LESSON + lessonId + KANJI, STRING_ARRAY,context.getApplicationContext().getPackageName());
        String[] kanji = context.getResources().getStringArray(id);

        id = Utils.getResourceId(context, LESSON + lessonId + MEANING, STRING_ARRAY,context.getApplicationContext().getPackageName());
        String[] meaning = context.getResources().getStringArray(id);

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
        dest.writeString(lessonId);
        dest.writeList(this.availBank);
    }

    private KanjiDataUtils(Parcel in) {
        lessonId = in.readString();
        this.availBank = new ArrayList<>();
        in.readList(this.availBank, Integer.class.getClassLoader());
    }

    public static final Creator<KanjiDataUtils> CREATOR = new Creator<KanjiDataUtils>() {
        @Override
        public KanjiDataUtils createFromParcel(Parcel source) {
            return new KanjiDataUtils(source);
        }

        @Override
        public KanjiDataUtils[] newArray(int size) {
            return new KanjiDataUtils[size];
        }
    };
}
