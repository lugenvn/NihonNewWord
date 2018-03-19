package self.lugen.nihonnewword.fragment;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import self.lugen.nihonnewword.R;
import self.lugen.nihonnewword.datamanager.NewWordDataUtils;
import self.lugen.nihonnewword.utils.Constants;
import self.lugen.nihonnewword.utils.TrackingUtils;
import self.lugen.nihonnewword.view.SessionDialog;
import self.lugen.nihonnewword.view.SettingDialog;

public class NewWordLessonFragment extends BaseFragment implements View.OnClickListener,
        SessionDialog.IOnSelectSessionDone, SettingDialog.IOnSelectSettingDone
{
    private static final String FIRST_INIT = "FIRST_INIT";
    private static final String DATA_UTILS = "DATA_UTILS";
    private static final String CURRENT_VALUE = "CURRENT_VALUE";
    private static final String CURRENT_DISPLAY = "CURRENT_DISPLAY";
    private static final String CURRENT_LESSON = "CURRENT_LESSON";

    TextView tvTitle;
    TextView tvLessonTitle;
    TextView tvContent;
    TextView tvNumberLeft;
    Button btnNext;
    Button btnSession;
    Button btnSetting;
    Button btnKana;
    Button btnKanji;
    ImageButton btnReading;
    Button btnMeaning;
    NewWordDataUtils dataUtils;
    ArrayList<String> mCurrent;
    int mCurrentDisplayPos;
    int mCurrentLesson;
    MediaPlayer mediaPlayer;

    boolean init = true;
    boolean hasAudio = false;
    int prioritySetting = SettingDialog.PRIORITY_RANDOM;

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
        tvNumberLeft = (TextView) view.findViewById(R.id.tv_number_left);
        btnKana = (Button) view.findViewById(R.id.btn_kana);
        btnKanji = (Button) view.findViewById(R.id.btn_kanji);
        btnReading = (ImageButton) view.findViewById(R.id.btn_reading);
        btnMeaning = (Button) view.findViewById(R.id.btn_meaning);
        btnNext = (Button) view.findViewById(R.id.btn_next);
        btnSession = (Button) view.findViewById(R.id.btn_session);
        btnSetting = (Button) view.findViewById(R.id.btn_setting);

        tvLessonTitle.setText(String.format(getString(R.string.lesson_name_full), mCurrentLesson));

        String path = Environment.getExternalStorageDirectory().getPath() + File.separator + Constants.APP_FOLDER + File.separator
                + mCurrentLesson;
        File lessonFolder = new File(path);
        if (lessonFolder.exists()) {
            hasAudio = true;
        } else {
            hasAudio = false;
        }

        btnKana.setOnClickListener(this);
        btnKanji.setOnClickListener(this);
        btnMeaning.setOnClickListener(this);
        btnReading.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        btnSession.setOnClickListener(this);
        btnSetting.setOnClickListener(this);
        if (init) {
            initView(false);
            init = false;
        } else {
            displayValue(mCurrentDisplayPos, false);
        }

        if (!hasAudio) {
            btnReading.setVisibility(View.GONE);
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

        mediaPlayer = new MediaPlayer();
        mCurrentLesson = getArguments().getInt(CURRENT_LESSON, 1);

        if (dataUtils == null) {
            dataUtils = new NewWordDataUtils(getContext(), mCurrentLesson);
        }
        if (mCurrent == null) {
            mCurrent = dataUtils.getCard(getContext());
        }
    }

    private void initView(boolean isNext) {
        int pos = NewWordDataUtils.POS_KANA;
        switch (prioritySetting) {
            case SettingDialog.PRIORITY_RANDOM:
                Random r = new Random();
                pos = r.nextInt((hasAudio) ? 4 : 3);
                if (pos == NewWordDataUtils.POS_KANJI && TextUtils.isEmpty(mCurrent.get(pos))) {
                    int newRand = r.nextInt(hasAudio ? 3 : 2);
                    if (newRand == 0) {
                        pos -= 1;
                    } else if (newRand == 1){
                        pos += 1;
                    } else {
                        pos += 2;
                    }
                }
                break;
            case SettingDialog.PRIORITY_KANA:
                pos = NewWordDataUtils.POS_KANA;
                break;
            case SettingDialog.PRIORITY_KANJI:
                pos = NewWordDataUtils.POS_KANJI;
                break;
            case SettingDialog.PRIORITY_MEANING:
                pos = NewWordDataUtils.POS_MEANING;
                break;
            case SettingDialog.PRIORITY_READING:
                pos = NewWordDataUtils.POS_READING;
                break;
        }
        displayValue(pos, isNext);
    }

    private void displayValue(int pos, boolean isNext) {
        tvNumberLeft.setText(String.format(getString(R.string.number_left), dataUtils.getNumberLeft()));
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
            case NewWordDataUtils.POS_READING:
                readWord(init || isNext);
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
            case R.id.btn_reading:
                readWord(true);
                break;
            case R.id.btn_next:
                next();
                break;
            case R.id.btn_session:
                openSessionDialog();
                break;
            case R.id.btn_setting:
                openSettingDialog();
                break;
        }
    }

    private void openSettingDialog() {
        TrackingUtils.trackingButton(getContext(), TrackingUtils.ID_BUTTON, TrackingUtils.SCREEN_NEW_WORD_DETAIL, TrackingUtils.SETTING);

        SettingDialog dialog = SettingDialog.newInstance(prioritySetting, hasAudio, getClass().getName());
        dialog.show(getFragmentManager(), dialog.getClass().getName());
    }

    private void openSessionDialog() {
        TrackingUtils.trackingButton(getContext(), TrackingUtils.ID_BUTTON, TrackingUtils.SCREEN_NEW_WORD_DETAIL, TrackingUtils.SESSION);
        SessionDialog dialog = SessionDialog.newInstance(dataUtils.getSessionCount(), dataUtils.getItemCountInSession
                (), dataUtils.getCurrentEnableSessions(), getClass().getName());
        dialog.show(getFragmentManager(), dialog.getClass().getName());
    }

    private void next() {
        TrackingUtils.trackingButton(getContext(), TrackingUtils.ID_BUTTON, TrackingUtils.SCREEN_NEW_WORD_DETAIL, TrackingUtils.NEXT);
        mediaPlayer.stop();
        mCurrent = dataUtils.getCard(getContext());
        initView(true);
    }

    private void readWord(boolean isRead) {
        TrackingUtils.trackingButton(getContext(), TrackingUtils.ID_BUTTON, TrackingUtils.SCREEN_NEW_WORD_DETAIL, TrackingUtils.READING);
        showAllData(NewWordDataUtils.POS_READING, R.string.reading, getString(R.string.listen));
        if (isRead) {
            dataUtils.speechWord(dataUtils.getRecentPos(), mCurrentLesson, mediaPlayer);
        }
    }

    private void showMeaning() {
        TrackingUtils.trackingButton(getContext(), TrackingUtils.ID_BUTTON, TrackingUtils.SCREEN_NEW_WORD_DETAIL, TrackingUtils.MEANING);
        showAllData(NewWordDataUtils.POS_MEANING, R.string.meaning, mCurrent.get(NewWordDataUtils.POS_MEANING));
    }

    private void showKanji() {
        TrackingUtils.trackingButton(getContext(), TrackingUtils.ID_BUTTON, TrackingUtils.SCREEN_NEW_WORD_DETAIL, TrackingUtils.KANJI);
        String data;
        if (TextUtils.isEmpty(mCurrent.get(NewWordDataUtils.POS_KANJI))) {
            data = getString(R.string.no_kanji);
        } else {
            data = mCurrent.get(NewWordDataUtils.POS_KANJI);
        }
        showAllData(NewWordDataUtils.POS_KANJI, R.string.kanji, data);
    }

    private void showKana() {
        TrackingUtils.trackingButton(getContext(), TrackingUtils.ID_BUTTON, TrackingUtils.SCREEN_NEW_WORD_DETAIL, TrackingUtils.KANA);
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

    @Override
    public void onResume() {
        super.onResume();
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
    }

    @Override
    public void onSelectSettingDone(int priorityItem) {
        prioritySetting = priorityItem;
    }
}
