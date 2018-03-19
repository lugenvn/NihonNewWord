package self.lugen.nihonnewword.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.analytics.FirebaseAnalytics;

import self.lugen.nihonnewword.R;
import self.lugen.nihonnewword.utils.Constants;
import self.lugen.nihonnewword.utils.TrackingUtils;
import self.lugen.nihonnewword.utils.Utils;

public class MainFragment extends BaseFragment implements View.OnClickListener {

    private RelativeLayout rlNewWord;
    private RelativeLayout rlKanji;
    private AdView mAdView;

    public MainFragment() {
        // Required empty public constructor
    }

    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        disableSideBar();
    }

    @Override
    public void onPause() {
        super.onPause();
        enableSideBar();
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rlNewWord = (RelativeLayout) view.findViewById(R.id.rl_new_world);
        rlKanji = (RelativeLayout) view.findViewById(R.id.rl_kanji);

        rlNewWord.setOnClickListener(this);
        rlKanji.setOnClickListener(this);

        mAdView = (AdView) view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_new_world:
                onClickNewWord();
                break;
            case R.id.rl_kanji:
                onClickKanji();
                break;
        }
    }

    private void onClickKanji() {
        TrackingUtils.trackingButton(getContext(), TrackingUtils.ID_BUTTON, TrackingUtils.SCREEN_MAIN, TrackingUtils.MAIN_KANJI);
        addFragment(KanjiMainFragment.newInstance());
    }

    private void onClickNewWord() {
        TrackingUtils.trackingButton(getContext(), TrackingUtils.ID_BUTTON, TrackingUtils.SCREEN_MAIN, TrackingUtils.MAIN_NEW_WORD);
        addFragment(NewWordFragment.newInstance());
    }
}
