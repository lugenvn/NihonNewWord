package self.lugen.nihonnewword.view;

import android.os.Bundle;
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

import java.util.ArrayList;

import self.lugen.nihonnewword.R;
import self.lugen.nihonnewword.view.adapter.GroupAdapter;

public class GroupDialog extends DialogFragment {
    private static final String SESSION_NUMBER = "SESSION_NUMBER";
    private static final String CURRENT_LIST = "CURRENT_LIST";
    private static final String CALLBACK_TAG = "CALLBACK_TAG";

    private int groupNumber;
    private ArrayList<Character> currentList;
    private String callbackTag;
    private RecyclerView rvGroup;
    private Button btnOK;
    private GroupAdapter mAdapter;

    public static GroupDialog newInstance(int sessionNumber, ArrayList<Character> currentList, String callbackTag) {
        GroupDialog dialog = new GroupDialog();
        Bundle b = new Bundle();
        b.putInt(SESSION_NUMBER, sessionNumber);
        b.putCharArray(CURRENT_LIST, toCharArray(currentList));
        b.putString(CALLBACK_TAG, callbackTag);
        dialog.setArguments(b);
        return dialog;
    }

    private static char[] toCharArray(ArrayList<Character> input) {
        char[] ret = new char[input.size()];
        for (int i = 0; i < input.size(); i++) {
            ret[i] = input.get(i);
        }
        return ret;
    }

    private static ArrayList<Character> toCharList(char[] input) {
        ArrayList<Character> ret = new ArrayList<>();
        for (int i = 0; i < input.length; i++) {
            ret.add(input[i]);
        }
        return ret;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            groupNumber = getArguments().getInt(SESSION_NUMBER);
            currentList = toCharList(getArguments().getCharArray(CURRENT_LIST));
            callbackTag = getArguments().getString(CALLBACK_TAG);
        }
        setStyle(DialogFragment.STYLE_NO_TITLE , android.R.style.Theme_Holo_Light_Dialog);
    }

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.group_dialog, container, false);
        rvGroup = (RecyclerView) v.findViewById(R.id.dl_rv_group);
        btnOK = (Button) v.findViewById(R.id.dl_btn_ok);

        mAdapter = new GroupAdapter(getContext(), groupNumber, currentList);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());

        rvGroup.setLayoutManager(manager);
        rvGroup.setAdapter(mAdapter);

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = getFragmentManager().findFragmentByTag(callbackTag);
                if (fragment != null && fragment instanceof IOnSelectGroupDone) {
                    ((IOnSelectGroupDone)fragment).onSelectGroupDone(new ArrayList<>(currentList));
                }
                dismiss();
            }
        });
        mAdapter.setListenner(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (currentList.isEmpty()) {
                    btnOK.setEnabled(false);
                } else {
                    btnOK.setEnabled(true);
                }
            }
        });

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public interface IOnSelectGroupDone {
        void onSelectGroupDone(ArrayList<Character> groupList);
    }
}
