package self.lugen.nihonnewword.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import self.lugen.nihonnewword.R;

public class MainFragment extends BaseFragment implements View.OnClickListener {

    private RelativeLayout rlNewWord;
    private RelativeLayout rlKanji;

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
        addFragment(KanjiMainFragment.newInstance());
    }

    private void onClickNewWord() {
        addFragment(NewWordFragment.newInstance());
    }
}
