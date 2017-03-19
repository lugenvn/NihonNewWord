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
import java.util.Random;

import self.lugen.nihonnewword.R;
import self.lugen.nihonnewword.datamanager.NewWordDataUtils;
import self.lugen.nihonnewword.view.SessionDialog;

public class NewWordLessonFragment extends BaseFragment implements View.OnClickListener,
        SessionDialog.IOnSelectSessionDone
{
    private static final String FIRST_INIT = "FIRST_INIT";
    private static final String DATA_UTILS = "DATA_UTILS";
    private static final String CURRENT_VALUE = "CURRENT_VALUE";
    private static final String CURRENT_DISPLAY = "CURRENT_DISPLAY";
    private static final String CURRENT_LESSON = "CURRENT_LESSON";

    TextView tvTitle;
    TextView tvLessonTitle;
    TextView tvContent;
    Button btnNext;
    Button btnSession;
    Button btnKana;
    Button btnKanji;
    Button btnMeaning;
    NewWordDataUtils dataUtils;
    ArrayList<String> mCurrent;
    int mCurrentDisplayPos;
    int mCurrentLesson;

    boolean init = true;

    public NewWordLessonFragment() {
        // Required empty public constructor
    }

    public static NewWordLessonFragment newInstance(int currentLesson) {
        NewWordLessonFragment fragment = new NewWordLessonFragment();
        Bundle args = new Bundle();
        args.putInt(CURRENT_LESSON, currentLesson);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_lesson_new_word, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvTitle = (TextView) view.findViewById(R.id.tv_type_title);
        tvLessonTitle = (TextView) view.findViewById(R.id.tv_lesson_title);
        tvContent = (TextView) view.findViewById(R.id.tv_card_content);
        btnKana = (Button) view.findViewById(R.id.btn_kana);
        btnKanji = (Button) view.findViewById(R.id.btn_kanji);
        btnMeaning = (Button) view.findViewById(R.id.btn_meaning);
        btnNext = (Button) view.findViewById(R.id.btn_next);
        btnSession = (Button) view.findViewById(R.id.btn_session);

        tvLessonTitle.setText(String.format(getString(R.string.lesson_name_full), mCurrentLesson));

        btnKana.setOnClickListener(this);
        btnKanji.setOnClickListener(this);
        btnMeaning.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        btnSession.setOnClickListener(this);
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
            mCurrentDisplayPos = savedInstanceState.getInt(CURRENT_DISPLAY, NewWordDataUtils.POS_KANA);
        }

        mCurrentLesson = getArguments().getInt(CURRENT_LESSON, 1);

        if (dataUtils == null) {
            dataUtils = new NewWordDataUtils(getContext(), mCurrentLesson);
        }
        if (mCurrent == null) {
            mCurrent = dataUtils.getCard(getContext());
        }
    }

    private void initView() {
        Random r = new Random();
        int pos = r.nextInt(mCurrent.size());
        if (pos == NewWordDataUtils.POS_KANJI && TextUtils.isEmpty(mCurrent.get(pos))) {
            int newRand = r.nextInt(2);
            if (newRand == 0) {
                pos += 1;
            } else {
                pos -= 1;
            }
        }
        displayValue(pos);
    }

    private void displayValue(int pos) {
        switch (pos) {
            case NewWordDataUtils.POS_KANA:
                showKana();
                break;
            case NewWordDataUtils.POS_KANJI:
                showKanji();
                break;
            case NewWordDataUtils.POS_MEANING:
                showMeaning();
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_kana:
                showKana();
                break;
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
        }
    }

    private void openSessionDialog() {
        SessionDialog dialog = SessionDialog.newInstance(dataUtils.getSessionCount(), dataUtils.getItemCountInSession
                (), dataUtils.getCurrentEnableSessions(), getClass().getName());
        dialog.show(getFragmentManager(), dialog.getClass().getName());
    }

    private void next() {
        mCurrent = dataUtils.getCard(getContext());
        initView();
    }

    private void showMeaning() {
        mCurrentDisplayPos = NewWordDataUtils.POS_MEANING;
        tvTitle.setText(R.string.meaning);
        tvContent.setText(mCurrent.get(NewWordDataUtils.POS_MEANING));
    }

    private void showKanji() {
        String data;
        if (TextUtils.isEmpty(mCurrent.get(NewWordDataUtils.POS_KANJI))) {
            data = getString(R.string.no_kanji);
        } else {
            data = mCurrent.get(NewWordDataUtils.POS_KANJI);
        }
        showAllData(NewWordDataUtils.POS_KANJI, R.string.kanji, data);
    }

    private void showKana() {
        showAllData(NewWordDataUtils.POS_KANA, R.string.kana, mCurrent.get(NewWordDataUtils.POS_KANA));
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
