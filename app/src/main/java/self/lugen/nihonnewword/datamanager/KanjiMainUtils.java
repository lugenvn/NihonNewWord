package self.lugen.nihonnewword.datamanager;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

import self.lugen.nihonnewword.utils.Constants;
import self.lugen.nihonnewword.utils.Utils;

public class KanjiMainUtils implements Parcelable {


    private ArrayList<String> mData;

    public ArrayList<String> getData() {
        if (mData == null) {
            mData = new ArrayList<>();
            for (int i = 0; i < NativeData.KANJI_MAIN_LESSON_NUMBER; i++) {
                mData.add(getData(i));
            }
        }
        return mData;
    }

    private String getData(int pos) {
        switch (pos) {
            case 0:
                return NativeData.KANJI_N5;
            case 1:
                return NativeData.KANJI_N4;
            case 2:
                return NativeData.KANJI_N3;
            case 3:
                return NativeData.KANJI_N2;
            case 4:
                return NativeData.KANJI_N1;
            default:
                return "";
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.mData);
    }

    public KanjiMainUtils() {
    }

    private KanjiMainUtils(Parcel in) {
        this.mData = new ArrayList<>();
        in.readList(this.mData, String.class.getClassLoader());
    }

    public static final Creator<KanjiMainUtils> CREATOR = new Creator<KanjiMainUtils>() {
        @Override
        public KanjiMainUtils createFromParcel(Parcel source) {
            return new KanjiMainUtils(source);
        }

        @Override
        public KanjiMainUtils[] newArray(int size) {
            return new KanjiMainUtils[size];
        }
    };

    public boolean checkValid(Context context, String pos) {

        int id = Utils.getResourceId(context, KanjiDataUtils.LESSON + pos + Constants.KANJI, Constants
                .STRING_ARRAY, context.getApplicationContext().getPackageName());
        if (id <= 0) {
            id = Utils.getResourceId(context, KanjiDataUtils.LESSON + pos + "_" + Constants.FIRST_CHARACTER_A +
                    Constants.KANJI, Constants.STRING_ARRAY, context.getApplicationContext().getPackageName());
        }

        return id > 0;
    }
}
