package self.lugen.nihonnewword.utils;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class NewWordLessonUtils implements Parcelable {


    private ArrayList<Integer> mData;

    public ArrayList<Integer> getData() {
        if (mData == null) {
            mData = new ArrayList<>();
            for (int i = 0; i < NativeData.NEW_WORD_LESSON_NUMBER; i++) {
                mData.add(i+1);
            }
        }
        return mData;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.mData);
    }

    public NewWordLessonUtils() {
    }

    private NewWordLessonUtils(Parcel in) {
        this.mData = new ArrayList<>();
        in.readList(this.mData, Integer.class.getClassLoader());
    }

    public static final Parcelable.Creator<NewWordLessonUtils> CREATOR = new Parcelable.Creator<NewWordLessonUtils>() {
        @Override
        public NewWordLessonUtils createFromParcel(Parcel source) {
            return new NewWordLessonUtils(source);
        }

        @Override
        public NewWordLessonUtils[] newArray(int size) {
            return new NewWordLessonUtils[size];
        }
    };

    public boolean checkValid(Context context, int pos) {

        int id = Utils.getResourceId(context, NewWordDataUtils.LESSON + pos + NewWordDataUtils.KANA, NewWordDataUtils.STRING_ARRAY,context.getApplicationContext().getPackageName());

        return id > 0;
    }
}
