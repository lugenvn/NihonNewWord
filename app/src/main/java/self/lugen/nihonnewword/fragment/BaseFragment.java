package self.lugen.nihonnewword.fragment;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

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

    public void addFragment(Fragment fragment) {

        FragmentManager frm = getActivity().getSupportFragmentManager();

        FragmentTransaction fmTr = frm.beginTransaction();
        fmTr.replace(R.id.content_main, fragment, fragment.getClass().getName());
        fmTr.addToBackStack(fragment.getClass().getName());
        fmTr.commit();
    }
}
