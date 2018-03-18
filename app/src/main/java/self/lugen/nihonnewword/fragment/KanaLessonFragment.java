package self.lugen.nihonnewword.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import self.lugen.nihonnewword.R;
import self.lugen.nihonnewword.datamanager.KanaDataUtils;
import self.lugen.nihonnewword.datamanager.NativeData;
import self.lugen.nihonnewword.view.SessionDialog;

public class KanaLessonFragment extends BaseFragment implements
        SessionDialog.IOnSelectSessionDone
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
    KanaDataUtils dataUtils;
    ArrayList<String> mCurrent;
    int mCurrentDisplayPos;
    String mCurrentLesson;

    boolean init = true;

    public KanaLessonFragment() {
        // Required empty public constructor
    }

    public static KanaLessonFragment newInstance(String currentLesson) {
        KanaLessonFragment fragment = new KanaLessonFragment();
        Bundle args = new Bundle();
        args.putString(CURRENT_LESSON, currentLesson);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_lesson_kana;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        tvLessonTitle.setText(String.format(getString(R.string.kanji_lesson_name_full), mCurrentLesson));

        if (init) {
            initView();
            init = false;
        } else {
            displayValue(mCurrentDisplayPos);
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
            mCurrentDisplayPos = savedInstanceState.getInt(CURRENT_DISPLAY, KanaDataUtils.POS_KANA);
        }

        mCurrentLesson = getArguments().getString(CURRENT_LESSON, NativeData.HIRAGANA);

        if (dataUtils == null) {
            dataUtils = new KanaDataUtils(getContext(), mCurrentLesson);
        }
        if (mCurrent == null) {
            mCurrent = dataUtils.getCard(getContext());
        }
    }

    @Override
    protected void initView() {
        displayValue(KanaDataUtils.POS_KANA);
    }

    private void displayValue(int pos) {
        switch (pos) {
            case KanaDataUtils.POS_KANA:
                showKana();
                break;
            case KanaDataUtils.POS_ROMAJI:
                showRomaji();
                break;
        }
    }

    @OnClick({R.id.btn_kana, R.id.btn_romaji, R.id.btn_next, R.id.btn_session})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_kana:
                showKana();
                break;
            case R.id.btn_romaji:
                showRomaji();
                break;
            case R.id.btn_next:
                next();
                break;
            case R.id.btn_session:
                openSessionDialog();
                break;
        }
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

    private void showRomaji() {
        mCurrentDisplayPos = KanaDataUtils.POS_ROMAJI;
        tvTitle.setText(R.string.romaji);
        tvContent.setText(mCurrent.get(KanaDataUtils.POS_ROMAJI));
    }

    private void showKana() {
        String data;
        data = mCurrent.get(KanaDataUtils.POS_KANA);
        showAllData(KanaDataUtils.POS_KANA, R.string.kana, data);
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

}
