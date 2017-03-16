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

    public static final int POS_KANA = 0;
    public static final int POS_KANJI = 1;
    public static final int POS_MEANING = 2;

    private ArrayList<Integer> availBank;
    private ArrayList<Integer> sessionList;
    private int sessionNumber = 1;
    private int lessonId;

    public NewWordDataUtils(Context context, int lesson) {
        this.lessonId = lesson;
        availBank = new ArrayList<>();
        sessionNumber = getSessionNumber(context);
        sessionList = new ArrayList<>();
        initSessionList();
        setup(context);
    }

    private void initSessionList() {
        sessionList.clear();
        for (int i = 0; i < sessionNumber; i++) {
            sessionList.add(i);
        }
    }

    public ArrayList<Integer> getCurrentEnableSessions() {
        return new ArrayList<>(sessionList);
    }

    public int getSessionNumber() {
        return sessionNumber;
    }
    public void updateSessionList(Context context, ArrayList<Integer> sessionLst) {
        sessionList.clear();
        sessionList.addAll(sessionLst);
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

    public int getNumberInsession(Context context) {
        int currentLength = getCurrentListLength(context);
        int numberInSession = currentLength / sessionNumber;
        return numberInSession;
    }

    private int getCurrentListLength(Context context) {
        int id = Utils.getResourceId(context, LESSON + lessonId + KANA, STRING_ARRAY,context.getApplicationContext().getPackageName());
        String[] kana = context.getResources().getStringArray(id);
        return kana.length;
    }

    private int getSessionNumber(Context context) {
        int length = getCurrentListLength(context);
        int maxDiv = length / NativeData.SESSION_MIN;

        if (maxDiv > 2) {
            for (int i = 2; i < maxDiv; i++) {
                for (int j = NativeData.SESSION_MIN; j <= NativeData.SESSION_MAX; j++) {
                    int lastSession = length - (i*j);
                    if (lastSession >= NativeData.SESSION_MIN && lastSession <= NativeData.SESSION_MAX) {
                        return i+1;
                    }
                }
            }
        }
        return 1;
    }

    private void setup(Context context){
        availBank.clear();
        int currentLength = getCurrentListLength(context);

        for (int i = 0; i < currentLength; i++) {
            if (isInSessionList(i, currentLength)) {
                availBank.add(i);
            }
        }
    }

    private boolean isInSessionList(int checkValue, int length) {
        if (checkValue >= length || checkValue < 0) {
            return false;
        }

        int numberInSession = length / sessionNumber;
        for (Integer sess :
                sessionList) {
            int min = numberInSession * sess;
            int max = numberInSession * sess + numberInSession;
            if (min <= checkValue && checkValue < max) {// start from 0
                return true;
            }
        }

        return false;
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
        dest.writeInt(sessionNumber);
        dest.writeList(this.sessionList);
        dest.writeList(this.availBank);
    }

    private NewWordDataUtils(Parcel in) {
        lessonId = in.readInt();
        sessionNumber = in.readInt();

        this.sessionList = new ArrayList<>();
        in.readList(sessionList, Integer.class.getClassLoader());
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
