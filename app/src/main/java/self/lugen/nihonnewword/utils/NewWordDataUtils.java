package self.lugen.nihonnewword.utils;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Random;

public class NewWordDataUtils implements Parcelable {
    static final String LESSON = "lesson";
    static final String KANA = "_kana";
    static final String KANJI = "_kanji";
    static final String MEANING = "_meaning";

    static final String STRING_ARRAY = "array";

    private ArrayList<Integer> availBank;
    private int lessonId;

    public static final int POS_KANA = 0;
    public static final int POS_KANJI = 1;
    public static final int POS_MEANING = 2;

    public NewWordDataUtils(Context context, int lesson) {
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
        int id = Utils.getResourceId(context, LESSON + lessonId + KANA, STRING_ARRAY,context.getApplicationContext().getPackageName());
        String[] kana = context.getResources().getStringArray(id);

        for (int i = 0; i < kana.length; i++) {
            availBank.add(i);
        }
    }

    private ArrayList<String> getRealData(Context context, int pos){
        ArrayList<String> returnData = new ArrayList<>();

        int id = Utils.getResourceId(context, LESSON + lessonId + KANA, STRING_ARRAY,context.getApplicationContext().getPackageName());
        String[] kana = context.getResources().getStringArray(id);

        id = Utils.getResourceId(context, LESSON + lessonId + KANJI, STRING_ARRAY,context.getApplicationContext().getPackageName());
        String[] kanji = context.getResources().getStringArray(id);

        id = Utils.getResourceId(context, LESSON + lessonId + MEANING, STRING_ARRAY,context.getApplicationContext().getPackageName());
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
        dest.writeInt(lessonId);
        dest.writeList(this.availBank);
    }

    private NewWordDataUtils(Parcel in) {
        lessonId = in.readInt();
        this.availBank = new ArrayList<>();
        in.readList(this.availBank, Integer.class.getClassLoader());
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
