package self.lugen.nihonnewword.fragment;


import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import self.lugen.nihonnewword.R;
import self.lugen.nihonnewword.datamanager.KanjiMainUtils;
import self.lugen.nihonnewword.view.adapter.KanjiMainAdapter;

public class KanjiMainFragment extends BaseFragment implements KanjiMainAdapter.OnClickedListener {
    private static final String LESSON_UTILS = "LESSON_UTILS";

    RecyclerView rvContainer;
    KanjiMainAdapter mAdapter;
    KanjiMainUtils mDataUtils;

    public KanjiMainFragment() {
        // Required empty public constructor
    }

    public static KanjiMainFragment newInstance() {
        KanjiMainFragment fragment = new KanjiMainFragment();
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
            mDataUtils = new KanjiMainUtils();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(LESSON_UTILS, mDataUtils);
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_kanji_main, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvContainer = (RecyclerView) view.findViewById(R.id.rv_lesson_list);

        mAdapter = new KanjiMainAdapter(getContext(), mDataUtils.getData());

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
        addFragment(KanjiLessonFragment.newInstance(s));
    }
}
