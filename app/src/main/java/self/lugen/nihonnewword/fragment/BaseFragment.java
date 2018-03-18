package self.lugen.nihonnewword.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import self.lugen.nihonnewword.MainActivity;
import self.lugen.nihonnewword.R;

public abstract class BaseFragment extends Fragment {

    public void disableSideBar() {
        Activity activity = getActivity();
        if (activity instanceof MainActivity) {
            ((MainActivity) activity).disableSideBar();
        }
    }

    public void enableSideBar() {
        Activity activity = getActivity();
        if (activity instanceof MainActivity) {
            ((MainActivity) activity).enableSideBar();
        }
    }

    protected abstract int getLayoutId();


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        View v = inflater.inflate(getLayoutId(), container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    protected void initView() {
     // TODO Do something in extended class
    }


    public void addFragment(Fragment fragment) {

        FragmentManager frm = getActivity().getSupportFragmentManager();

        FragmentTransaction fmTr = frm.beginTransaction();
        fmTr.replace(R.id.content_main, fragment, fragment.getClass().getName());
        fmTr.addToBackStack(fragment.getClass().getName());
        fmTr.commit();
    }
}
