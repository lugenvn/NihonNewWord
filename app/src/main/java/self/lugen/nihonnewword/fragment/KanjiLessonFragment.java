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
import self.lugen.nihonnewword.utils.KanjiDataUtils;
import self.lugen.nihonnewword.utils.KanjiDataUtils;
import self.lugen.nihonnewword.utils.NativeData;

public class KanjiLessonFragment extends BaseFragment implements View.OnClickListener {
    private static final String FIRST_INIT = "FIRST_INIT";
    private static final String DATA_UTILS = "DATA_UTILS";
    private static final String CURRENT_VALUE = "CURRENT_VALUE";
    private static final String CURRENT_DISPLAY = "CURRENT_DISPLAY";
    private static final String CURRENT_LESSON = "CURRENT_LESSON";

    TextView tvTitle;
    TextView tvLessonTitle;
    TextView tvContent;
    Button btnNext;
    Button btnKanji;
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
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_lesson_kanji, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        tvLessonTitle = (TextView) view.findViewById(R.id.tv_lesson_title);
        tvContent = (TextView) view.findViewById(R.id.tv_card_content);
        btnKanji = (Button) view.findViewById(R.id.btn_kanji);
        btnMeaning = (Button) view.findViewById(R.id.btn_meaning);
        btnNext = (Button) view.findViewById(R.id.btn_next);

        tvLessonTitle.setText(String.format(getString(R.string.kanji_lesson_name_full), mCurrentLesson));

        btnKanji.setOnClickListener(this);
        btnMeaning.setOnClickListener(this);
        btnNext.setOnClickListener(this);
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

    private void initView() {
        Random r = new Random();
        int pos = r.nextInt(mCurrent.size());
        displayValue(pos);
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
        }
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
}
