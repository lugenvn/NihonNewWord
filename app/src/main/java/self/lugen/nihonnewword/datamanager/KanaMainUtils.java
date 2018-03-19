package self.lugen.nihonnewword.datamanager;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

import self.lugen.nihonnewword.utils.Constants;
import self.lugen.nihonnewword.utils.Utils;

public class KanaMainUtils implements Parcelable {


    private ArrayList<String> mData;

    public ArrayList<String> getData() {
        if (mData == null) {
            mData = new ArrayList<>();
            for (int i = 0; i < NativeData.KANA_MAIN_LESSON_NUMBER; i++) {
                mData.add(getData(i));
            }
        }
        return mData;
    }

    private String getData(int pos) {
        switch (pos) {
            case 0:
                return NativeData.HIRAGANA;
            case 1:
                return NativeData.KATAKANA;
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

    public KanaMainUtils() {
    }

    private KanaMainUtils(Parcel in) {
        this.mData = new ArrayList<>();
        in.readList(this.mData, String.class.getClassLoader());
    }

    public static final Creator<KanaMainUtils> CREATOR = new Creator<KanaMainUtils>() {
        @Override
        public KanaMainUtils createFromParcel(Parcel source) {
            return new KanaMainUtils(source);
        }

        @Override
        public KanaMainUtils[] newArray(int size) {
            return new KanaMainUtils[size];
        }
    };

    public boolean checkValid(Context context, String pos) {

        int id = Utils.getResourceId(context, KanaDataUtils.LESSON + pos + Constants.KANA, Constants
                .STRING_ARRAY, context.getApplicationContext().getPackageName());
        if (id <= 0) {
            id = Utils.getResourceId(context, KanaDataUtils.LESSON + pos + "_" + Constants.FIRST_CHARACTER_A +
                    Constants.KANJI, Constants.STRING_ARRAY, context.getApplicationContext().getPackageName());
        }

        return id > 0;
    }
}
