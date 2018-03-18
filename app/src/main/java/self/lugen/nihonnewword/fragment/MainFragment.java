package self.lugen.nihonnewword.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;

import butterknife.BindView;
import butterknife.OnClick;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import self.lugen.nihonnewword.R;
import self.lugen.nihonnewword.utils.Constants;
import self.lugen.nihonnewword.utils.Utils;

public class MainFragment extends BaseFragment {

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
    protected int getLayoutId() {
        return R.layout.fragment_main;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAdView = (AdView) view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

    }

    @OnClick({R.id.rl_new_world, R.id.rl_kanji, R.id.rl_kana})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_new_world:
                onClickNewWord();
                break;
            case R.id.rl_kanji:
                onClickKanji();
                break;
            case R.id.rl_kana:
                onClickKana();
                break;
        }
    }

    private void onClickKana() {
        addFragment(KanaMainFragment.newInstance());
    }

    private void onClickKanji() {
        addFragment(KanjiMainFragment.newInstance());
    }

    private void onClickNewWord() {
        addFragment(NewWordFragment.newInstance());
    }
}
