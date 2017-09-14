package self.lugen.nihonnewword.view;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;

import self.lugen.nihonnewword.R;
import self.lugen.nihonnewword.view.adapter.SessionAdapter;

public class SettingDialog extends DialogFragment {
    private static final String CURRENT_STATE = "CURRENT_STATE";
    private static final String HAS_AUDIO = "HAS_AUDIO";
    private static final String CALLBACK_TAG = "CALLBACK_TAG";

    public static final int PRIORITY_RANDOM = 0;
    public static final int PRIORITY_KANA = 1;
    public static final int PRIORITY_KANJI = 2;
    public static final int PRIORITY_MEANING = 3;
    public static final int PRIORITY_READING = 4;

    private int currentState;
    private String callbackTag;
    private Button btnOK;
    private Button btnCancel;
    private RadioGroup settingGroup;
    private int chosenID = PRIORITY_RANDOM;
    private boolean hasAudio;

    public static SettingDialog newInstance(int currentState, boolean hasAudio, String callbackTag) {
        SettingDialog dialog = new SettingDialog();
        Bundle b = new Bundle();
        b.putInt(CURRENT_STATE, currentState);
        b.putBoolean(HAS_AUDIO, hasAudio);
        b.putString(CALLBACK_TAG, callbackTag);
        dialog.setArguments(b);
        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            currentState = getArguments().getInt(CURRENT_STATE);
            hasAudio = getArguments().getBoolean(HAS_AUDIO);
            callbackTag = getArguments().getString(CALLBACK_TAG);
        }
        setStyle(DialogFragment.STYLE_NO_TITLE , android.R.style.Theme_Holo_Light_Dialog);
    }

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.dialog_new_word_setting, container, false);
        settingGroup = (RadioGroup) v.findViewById(R.id.group_priority);
        btnOK = (Button) v.findViewById(R.id.dl_btn_ok);
        btnCancel = (Button) v.findViewById(R.id.dl_btn_cancel);

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = getFragmentManager().findFragmentByTag(callbackTag);
                if (fragment != null && fragment instanceof IOnSelectSettingDone) {
                    ((IOnSelectSettingDone)fragment).onSelectSettingDone(chosenID);
                }
                dismiss();
            }
        });
        if (!hasAudio) {
            View radioReading = v.findViewById(R.id.radio_reading);
            radioReading.setVisibility(View.GONE);
        }

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        settingGroup.setOnCheckedChangeListener(
                new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup,
                                                 @IdRes int i) {
                        switch (i) {
                            case R.id.radio_kana:
                                chosenID = PRIORITY_KANA;
                                break;
                            case R.id.radio_kanji:
                                chosenID = PRIORITY_KANJI;
                                break;
                            case R.id.radio_meaning:
                                chosenID = PRIORITY_MEANING;
                                break;
                            case R.id.radio_reading:
                                chosenID = PRIORITY_READING;
                                break;
                            case R.id.radio_random:
                                chosenID = PRIORITY_RANDOM;
                                break;
                        }
                    }
                });

        switch (currentState) {
            case PRIORITY_RANDOM:
                settingGroup.check(R.id.radio_random);
                break;
            case PRIORITY_KANA:
                settingGroup.check(R.id.radio_kana);
                break;
            case PRIORITY_KANJI:
                settingGroup.check(R.id.radio_kanji);
                break;
            case PRIORITY_MEANING:
                settingGroup.check(R.id.radio_meaning);
                break;
            case PRIORITY_READING:
                settingGroup.check(R.id.radio_reading);
                break;
        }

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public interface IOnSelectSettingDone{
        void onSelectSettingDone(int priorityItem);
    }
}
