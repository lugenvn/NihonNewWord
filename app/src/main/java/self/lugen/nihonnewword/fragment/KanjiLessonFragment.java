package self.lugen.nihonnewword.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import self.lugen.nihonnewword.R;
import self.lugen.nihonnewword.datamanager.KanjiDataUtils;
import self.lugen.nihonnewword.datamanager.NativeData;
import self.lugen.nihonnewword.view.GroupDialog;
import self.lugen.nihonnewword.view.SessionDialog;

public class KanjiLessonFragment extends BaseFragment implements View.OnClickListener,
        SessionDialog.IOnSelectSessionDone, GroupDialog.IOnSelectGroupDone
{
    private static final String FIRST_INIT = "FIRST_INIT";
    private static final String DATA_UTILS = "DATA_UTILS";
    private static final String CURRENT_VALUE = "CURRENT_VALUE";
    private static final String CURRENT_DISPLAY = "CURRENT_DISPLAY";
    private static final String CURRENT_LESSON = "CURRENT_LESSON";

    @BindView(R.id.tv_type_title)
    TextView tvTitle;
    @BindView(R.id.tv_lesson_title)
    TextView tvLessonTitle;
    @BindView(R.id.tv_card_content)
    TextView tvContent;
    @BindView(R.id.btn_next)
    Button btnNext;
    @BindView(R.id.btn_session)
    Button btnSession;
    @BindView(R.id.btn_group)
    Button btnGroup;
    @BindView(R.id.btn_kanji)
    Button btnKanji;
    @BindView(R.id.btn_meaning)
    Button btnMeaning;
    KanjiDataUtils dataUtils;
    ArrayList<String> mCurrent;
    int mCurrentDisplayPos;
    String mCurrentLesson;

    boolean init = true;

    public KanjiLessonFragment() {
        // Required empty public constructor
    }

    public static KanjiLessonFragment newInstance(String currentLesson) {
        KanjiLessonFragment fragment = new KanjiLessonFragment();
        Bundle args = new Bundle();
        args.putString(CURRENT_LESSON, currentLesson);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_lesson_kanji;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        tvLessonTitle.setText(String.format(getString(R.string.kanji_lesson_name_full), mCurrentLesson));

        btnKanji.setOnClickListener(this);
        btnMeaning.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        btnSession.setOnClickListener(this);
        btnGroup.setOnClickListener(this);
        if (init) {
            initData();
            init = false;
        } else {
            displayValue(mCurrentDisplayPos);
        }

        if (dataUtils.getGroupCount() <=1) {
            btnGroup.setVisibility(View.GONE);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(FIRST_INIT, init);
        outState.putParcelable(DATA_UTILS, dataUtils);
        outState.putStringArrayList(CURRENT_VALUE, mCurrent);
        outState.putInt(CURRENT_DISPLAY, mCurrentDisplayPos);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            init = savedInstanceState.getBoolean(FIRST_INIT, true);
            dataUtils = savedInstanceState.getParcelable(DATA_UTILS);
            mCurrent = savedInstanceState.getStringArrayList(CURRENT_VALUE);
            mCurrentDisplayPos = savedInstanceState.getInt(CURRENT_DISPLAY, KanjiDataUtils.POS_KANJI);
        }

        mCurrentLesson = getArguments().getString(CURRENT_LESSON, NativeData.KANJI_N5);

        if (dataUtils == null) {
            dataUtils = new KanjiDataUtils(getContext(), mCurrentLesson);
        }
        if (mCurrent == null) {
            mCurrent = dataUtils.getCard(getContext());
        }
    }

    protected void initData() {
        displayValue(KanjiDataUtils.POS_KANJI);
    }

    private void displayValue(int pos) {
        switch (pos) {
            case KanjiDataUtils.POS_KANJI:
                showKanji();
                break;
            case KanjiDataUtils.POS_MEANING:
                showMeaning();
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_kanji:
                showKanji();
                break;
            case R.id.btn_meaning:
                showMeaning();
                break;
            case R.id.btn_next:
                next();
                break;
            case R.id.btn_session:
                openSessionDialog();
                break;
            case R.id.btn_group:
                openGroupDialog();
                break;
        }
    }

    private void openGroupDialog() {
        GroupDialog dialog = GroupDialog.newInstance(dataUtils.getGroupCount(),
                dataUtils.getGroupList(), getClass().getName());
        dialog.show(getFragmentManager(), dialog.getClass().getName());
    }

    private void openSessionDialog() {
        SessionDialog dialog = SessionDialog.newInstance(dataUtils.getSessionCount(), dataUtils.getItemCountInSession(),
                dataUtils.getCurrentEnableSessions(), getClass().getName());
        dialog.show(getFragmentManager(), dialog.getClass().getName());
    }

    private void next() {
        mCurrent = dataUtils.getCard(getContext());
        initView();
    }

    private void showMeaning() {
        mCurrentDisplayPos = KanjiDataUtils.POS_MEANING;
        tvTitle.setText(R.string.meaning);
        tvContent.setText(mCurrent.get(KanjiDataUtils.POS_MEANING));
    }

    private void showKanji() {
        String data;
        if (TextUtils.isEmpty(mCurrent.get(KanjiDataUtils.POS_KANJI))) {
            data = getString(R.string.no_kanji);
        } else {
            data = mCurrent.get(KanjiDataUtils.POS_KANJI);
        }
        showAllData(KanjiDataUtils.POS_KANJI, R.string.kanji, data);
    }

    private void showAllData(int pos, int titleID, String contentData) {
        mCurrentDisplayPos = pos;
        tvTitle.setText(titleID);
        tvContent.setText(contentData);
    }

    @Override
    public void onSelectSessionDone(ArrayList<Integer> sessionList) {
        dataUtils.updateSessionList(getContext(), sessionList);
        next();
    }

    @Override
    public void onSelectGroupDone(ArrayList<Character> groupList) {
        dataUtils.updateGroupList(getContext(), groupList);
        next();
    }
}
