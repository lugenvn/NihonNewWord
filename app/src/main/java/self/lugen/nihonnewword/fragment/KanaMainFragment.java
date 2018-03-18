package self.lugen.nihonnewword.fragment;


import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import butterknife.BindView;
import self.lugen.nihonnewword.R;
import self.lugen.nihonnewword.datamanager.KanaMainUtils;
import self.lugen.nihonnewword.datamanager.KanjiMainUtils;
import self.lugen.nihonnewword.view.adapter.KanaMainAdapter;
import self.lugen.nihonnewword.view.adapter.KanjiMainAdapter;

public class KanaMainFragment extends BaseFragment implements KanaMainAdapter.OnClickedListener {
    private static final String LESSON_UTILS = "LESSON_UTILS";

    @BindView(R.id.rv_lesson_list)
    RecyclerView rvContainer;
    KanaMainAdapter mAdapter;
    KanaMainUtils mDataUtils;

    public KanaMainFragment() {
        // Required empty public constructor
    }

    public static KanaMainFragment newInstance() {
        KanaMainFragment fragment = new KanaMainFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mDataUtils = savedInstanceState.getParcelable(LESSON_UTILS);
        }
        if (mDataUtils == null) {
            mDataUtils = new KanaMainUtils();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(LESSON_UTILS, mDataUtils);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_kana_main;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAdapter = new KanaMainAdapter(getContext(), mDataUtils.getData());

        int orientation = getResources().getConfiguration().orientation;

        GridLayoutManager manager = new GridLayoutManager(getContext(), (orientation == Configuration.ORIENTATION_LANDSCAPE) ? 3 : 2);

        rvContainer.setLayoutManager(manager);
        rvContainer.setAdapter(mAdapter);
        mAdapter.setListener(this);
    }

    @Override
    public void itemClicked(int pos) {
        if (mDataUtils.checkValid(getContext(), mDataUtils.getData().get(pos))){
            openLessonFragment(mDataUtils.getData().get(pos));
        } else {
            Toast.makeText(getContext(), "no data", Toast.LENGTH_SHORT).show();
        }
    }

    private void openLessonFragment(String s) {
        addFragment(KanaLessonFragment.newInstance(s));
    }
}
