package self.lugen.nihonnewword.datamanager;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import self.lugen.nihonnewword.utils.Constants;
import self.lugen.nihonnewword.utils.Utils;

public class KanjiDataUtils extends LessonDataUtilsBase {
    static final String LESSON = "kanji_lesson_";

    public static final int POS_KANJI = 0;
    public static final int POS_MEANING = 1;

    private String lessonId;
    private int groupCount = 1;
    private ArrayList<Character> groupList;

    public KanjiDataUtils(Context context, String lesson) {
        super();
        this.lessonId = lesson;
        initData(context);
    }

    @Override
    protected void initData(Context context) {
        groupList = new ArrayList<>();
        initGroupList(context);
        super.initData(context);
    }

    @Override
    protected int getCurrentListLength(Context context) {
        if (groupCount > 1) {
            int length = 0;
            for (Character c :
                    groupList) {
                int id = Utils.getResourceId(context, LESSON + lessonId + getGroupID(context, c) + Constants.KANJI,
                        Constants.STRING_ARRAY, context.getApplicationContext
                                ().getPackageName());
                length += context.getResources().getStringArray(id).length;
            }
            Log.i(getClass().getName(), "getCurrentListLength = " + length);
            return length;
        } else {
            int id = Utils.getResourceId(context, LESSON + lessonId + Constants.KANJI, Constants.STRING_ARRAY, context
                    .getApplicationContext
                            ().getPackageName());
            String[] kana = context.getResources().getStringArray(id);
            Log.i(getClass().getName(), "getCurrentListLength = " + kana.length);
            return kana.length;
        }
    }

    @Override
    protected ArrayList<String> getRealData(Context context, int pos) {
        Log.i(getClass().getName(), "getRealData = " + pos);

        ArrayList<String> returnData = new ArrayList<>();
        ArrayList<String> kanji = new ArrayList<>();
        ArrayList<String> meaning = new ArrayList<>();
        if (groupCount > 1) {
            for (Character c :
                    groupList) {
                int id = Utils.getResourceId(context, createFileID(LESSON, lessonId, getGroupID(context, c),
                        Constants.KANJI),
                        Constants.STRING_ARRAY, context.getApplicationContext
                                ().getPackageName());
                kanji.addAll(Arrays.asList(context.getResources().getStringArray(id)));

                id = Utils.getResourceId(context, createFileID(LESSON, lessonId, getGroupID(context, c), Constants
                        .MEANING),
                        Constants.STRING_ARRAY, context.getApplicationContext()
                                .getPackageName());
                meaning.addAll(Arrays.asList(context.getResources().getStringArray(id)));
            }
        } else {

            int id = Utils.getResourceId(context, createFileID(LESSON, lessonId, "", Constants.KANJI), Constants
                    .STRING_ARRAY,
                    context
                            .getApplicationContext
                                    ().getPackageName());
            kanji.addAll(Arrays.asList(context.getResources().getStringArray(id)));

            id = Utils.getResourceId(context, createFileID(LESSON, lessonId, "", Constants.MEANING), Constants
                    .STRING_ARRAY,
                    context
                            .getApplicationContext()
                            .getPackageName());
            meaning.addAll(Arrays.asList(context.getResources().getStringArray(id)));
        }
        returnData.add(kanji.get(pos));
        returnData.add(meaning.get(pos));
        return returnData;
    }

    private String createFileID(String lesson, String lessonID, String groupID, String valueType) {
        String ret = lesson + lessonID + groupID + valueType;
        Log.i(getClass().getName(), "createFileID = " + ret);
        return ret;
    }

    // group stub

    public void updateGroupList(Context context, ArrayList<Character> newList) {
        groupList = newList;
        super.initData(context);
    }

    private void initGroupList(Context context) {
        groupList.clear();
        if (hasGroup(context)) {
            groupCount = getResourceGroupCount(context);
            groupList.add(Constants.FIRST_CHARACTER_A);
        } else {
            groupCount = 1;
        }

        Log.i(getClass().getName(), "initGroupList group count = " + groupCount);
    }

    private boolean hasGroup(Context context) {
        int id = Utils.getResourceId(context, createFileID(LESSON, lessonId, "_" + Constants.FIRST_CHARACTER_A,
                Constants.KANJI),
                Constants.STRING_ARRAY, context.getApplicationContext().getPackageName());
        Log.i(getClass().getName(), "hasGroup = " + (id > 0));
        return id > 0;
    }

    private int getResourceGroupCount(Context context) {
        Log.i(getClass().getName(), "getResourceGroupCount start");
        int count = 0;
        for (char c = Constants.FIRST_CHARACTER_A; c <= Constants.LAST_CHARACTER_Z; c++) {
            int id = Utils.getResourceId(context, createFileID(LESSON, lessonId, getGroupID(context, c), Constants
                    .KANJI),
                    Constants.STRING_ARRAY,
                    context
                            .getApplicationContext().getPackageName());
            Log.i(getClass().getName(), "getResourceGroupCount id " + id);
            Log.i(getClass().getName(), "getResourceGroupCount id " + id);
            if (id > 0) {
                count++;
            } else {
                break;
            }
        }
        Log.i(getClass().getName(), "getResourceGroupCount group count = " + count);
        return count;
    }

    private String getGroupID(Context context, Character groupID) {
        if (!hasGroup(context)) return "";
        String ret = "_";
        ret = ret + groupID;
        Log.i(getClass().getName(), "getGroupID group id = " + ret);
        return ret;
    }

    public int getGroupCount() {
        return groupCount;
    }

    public ArrayList<Character> getGroupList() {
        return groupList;
    }

    // parcelable method
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(lessonId);
        dest.writeInt(groupCount);
        dest.writeList(groupList);
    }

    protected KanjiDataUtils(Parcel in) {
        super(in);
        lessonId = in.readString();
        groupCount = in.readInt();
        this.groupList = new ArrayList<>();
        in.readList(this.groupList, Character.class.getClassLoader());
    }

    public static final Parcelable.Creator<KanjiDataUtils> CREATOR = new Parcelable.Creator<KanjiDataUtils>() {
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
