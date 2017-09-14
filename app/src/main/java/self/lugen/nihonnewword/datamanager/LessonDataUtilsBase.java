package self.lugen.nihonnewword.datamanager;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by lugen on 3/19/17.
 */
public class LessonDataUtilsBase implements Parcelable {

    private ArrayList<Integer> availBank;
    private ArrayList<Integer> sessionList;
    private int sessionCount = 1;
    private int itemCountInSession = 0;
    private int recentPos = 0;

    public LessonDataUtilsBase() {
        availBank = new ArrayList<>();
        sessionList = new ArrayList<>();
    }

    protected void initData(Context context) {
        sessionList.clear();
        availBank.clear();

        initSessionNumber(context);
        initSessionList();
        setup(context);
    }

    private void setup(Context context) {
        availBank.clear();
        int currentLength = getCurrentListLength(context);

        for (int i = 0; i < currentLength; i++) {
            if (isInSessionList(i, currentLength)) {
                Log.i(getClass().getName(), "setup add = " + i);
                availBank.add(i);
            }
        }
    }

    public ArrayList<String> getCard(Context context) {
        if (availBank.isEmpty()) setup(context);
        Random r = new Random();
        int pos = r.nextInt(availBank.size());
        recentPos = availBank.get(pos);
        ArrayList<String> returnValue = getRealData(context, recentPos);
        availBank.remove(pos);
        return returnValue;
    }

    /**
     * get the recent generate item position
     * @return recent position
     */
    public int getRecentPos() {
        return recentPos;
    }

    private boolean isInSessionList(int checkValue, int length) {
        if (checkValue >= length || checkValue < 0) {
            return false;
        }

        for (Integer sess :
                sessionList) {
            int min = itemCountInSession * sess;
            int max = itemCountInSession * sess + itemCountInSession;
            if (min <= checkValue && checkValue < max) {// start from 0

                Log.i(getClass().getName(), "isInSessionList sess = " + sess);
                Log.i(getClass().getName(), "isInSessionList max = " + max + ", min = " + min);
                return true;
            }
        }

        return false;
    }

    public ArrayList<Integer> getCurrentEnableSessions() {
        return sessionList;
    }

    public int getSessionCount() {
        return sessionCount;
    }

    public int getItemCountInSession() {
        return itemCountInSession;
    }

    public void updateSessionList(Context context, ArrayList<Integer> sessionLst) {
        sessionList.clear();
        sessionList.addAll(sessionLst);
        setup(context);
    }

    private void initSessionNumber(Context context) {
        int length = getCurrentListLength(context);
        int maxDiv = length / NativeData.SESSION_MIN;

        if (maxDiv > 2) {
            for (int i = 2; i < maxDiv; i++) {
                for (int j = NativeData.SESSION_MIN; j <= NativeData.SESSION_MAX; j++) {

                    Log.i(getClass().getName(), "initSessionNumber i = " + i + ", j = " + j);
                    int lastSession = length - (i * j);
                    if (lastSession >= NativeData.SESSION_MIN && lastSession <= NativeData.SESSION_MAX) {
                        Log.i(getClass().getName(), "initSessionNumber got count " + (i + 1));
                        sessionCount = i + 1;
                        itemCountInSession = j;
                        return;
                    }
                }
            }
        }
        sessionCount = 1;
        itemCountInSession = length;
    }

    private void initSessionList() {
        sessionList.clear();
        for (int i = 0; i < sessionCount; i++) {
            sessionList.add(i);
        }
    }

    public int getNumberLeft() {
        if (availBank != null)
            return availBank.size();
        else return 0;
    }

    protected int getCurrentListLength(Context context) {
        return 0;
    }

    protected ArrayList<String> getRealData(Context context, int pos) {
        return null;
    }


    // parcelable method

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(sessionCount);
        dest.writeInt(itemCountInSession);
        dest.writeList(sessionList);
        dest.writeList(availBank);
    }

    LessonDataUtilsBase(Parcel in) {
        sessionCount = in.readInt();
        itemCountInSession = in.readInt();

        this.sessionList = new ArrayList<>();
        in.readList(sessionList, Integer.class.getClassLoader());
        this.availBank = new ArrayList<>();
        in.readList(this.availBank, Integer.class.getClassLoader());
    }

    public static final Parcelable.Creator<LessonDataUtilsBase> CREATOR = new Parcelable.Creator<LessonDataUtilsBase>
            () {
        @Override
        public LessonDataUtilsBase createFromParcel(Parcel source) {
            return new LessonDataUtilsBase(source);
        }

        @Override
        public LessonDataUtilsBase[] newArray(int size) {
            return new LessonDataUtilsBase[size];
        }
    };
}
