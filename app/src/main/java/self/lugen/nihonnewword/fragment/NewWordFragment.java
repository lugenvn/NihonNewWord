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
import self.lugen.nihonnewword.datamanager.NewWordLessonUtils;
import self.lugen.nihonnewword.view.adapter.LessonAdapter;

public class NewWordFragment extends BaseFragment implements LessonAdapter.OnClickedListener {
    private static final String LESSON_UTILS = "LESSON_UTILS";


    RecyclerView rvLesson;
    LessonAdapter mAdapter;
    NewWordLessonUtils lessonUtils;

    public NewWordFragment() {
        // Required empty public constructor
    }

    public static NewWordFragment newInstance() {
        NewWordFragment fragment = new NewWordFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            lessonUtils = savedInstanceState.getParcelable(LESSON_UTILS);
        }
        if (lessonUtils == null) {
            lessonUtils = new NewWordLessonUtils();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(LESSON_UTILS, lessonUtils);
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_word, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvLesson = (RecyclerView) view.findViewById(R.id.rv_lesson_list);

        mAdapter = new LessonAdapter(getContext(), lessonUtils.getData());
        mAdapter.setListener(this);

        int orientation = getResources().getConfiguration().orientation;

        GridLayoutManager manager = new GridLayoutManager(getContext(), (orientation == Configuration.ORIENTATION_LANDSCAPE) ? 5 : 3);

        rvLesson.setLayoutManager(manager);
        rvLesson.setAdapter(mAdapter);
    }

    @Override
    public void itemClicked(int pos) {
        if (lessonUtils.checkValid(getContext(), lessonUtils.getData().get(pos))){
            openLessonFragment(lessonUtils.getData().get(pos));
        } else {
            Toast.makeText(getContext(), "no data", Toast.LENGTH_SHORT).show();
        }
    }

    private void openLessonFragment(int lesson) {
        addFragment(NewWordLessonFragment.newInstance(lesson));
    }

}
