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
import android.widget.TextView;

import java.util.ArrayList;

import self.lugen.nihonnewword.R;
import self.lugen.nihonnewword.view.adapter.SessionAdapter;

public class SessionDialog extends DialogFragment {
    private static final String SESSION_NUMBER = "SESSION_NUMBER";
    private static final String NUMBER_IN_SESSION = "NUMBER_IN_SESSION";
    private static final String CURRENT_LIST = "CURRENT_LIST";
    private static final String CALLBACK_TAG = "CALLBACK_TAG";

    private int sessionNumber;
    private int numberInSession;
    private ArrayList<Integer> currentList;
    private String callbackTag;
    private TextView tvNumberInSession;
    private RecyclerView rvSession;
    private Button btnOK;
    private Button btnSelectAll;
    private Button btnDeselectAll;
    private SessionAdapter mAdapter;

    public static SessionDialog newInstance(int sessionNumber, int numberInSession, ArrayList<Integer> currentList, String callbackTag) {
        SessionDialog dialog = new SessionDialog();
        Bundle b = new Bundle();
        b.putInt(SESSION_NUMBER, sessionNumber);
        b.putInt(NUMBER_IN_SESSION, numberInSession);
        b.putIntegerArrayList(CURRENT_LIST, currentList);
        b.putString(CALLBACK_TAG, callbackTag);
        dialog.setArguments(b);
        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            sessionNumber = getArguments().getInt(SESSION_NUMBER);
            numberInSession = getArguments().getInt(NUMBER_IN_SESSION);
            currentList = getArguments().getIntegerArrayList(CURRENT_LIST);
            callbackTag = getArguments().getString(CALLBACK_TAG);
        }
        setStyle(DialogFragment.STYLE_NO_TITLE , android.R.style.Theme_Holo_Light_Dialog);
    }

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.session_dialog, container, false);
        tvNumberInSession = (TextView) v.findViewById(R.id.dl_tv_session_number);
        rvSession = (RecyclerView) v.findViewById(R.id.dl_rv_session);
        btnOK = (Button) v.findViewById(R.id.dl_btn_ok);
        btnSelectAll = (Button) v.findViewById(R.id.dl_btn_select_all);
        btnDeselectAll = (Button) v.findViewById(R.id.dl_btn_deselect_all);

        tvNumberInSession.setText(String.format(getString(R.string.dl_number_in_session), numberInSession));

        mAdapter = new SessionAdapter(getContext(), sessionNumber, currentList);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());

        rvSession.setLayoutManager(manager);
        rvSession.setAdapter(mAdapter);

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = getFragmentManager().findFragmentByTag(callbackTag);
                if (fragment != null && fragment instanceof IOnSelectSessionDone) {
                    ((IOnSelectSessionDone)fragment).onSelectSessionDone(new ArrayList<>(currentList));
                }
                dismiss();
            }
        });

        btnSelectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAdapter.selectAll();
            }
        });

        btnDeselectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAdapter.deselectAll();
            }
        });

        mAdapter.setListenner(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (currentList.isEmpty()) {
                    btnDeselectAll.setEnabled(false);
                    btnOK.setEnabled(false);
                    btnSelectAll.setEnabled(true);
                } else {
                    btnOK.setEnabled(true);
                    btnDeselectAll.setEnabled(true);
                    if (currentList.size() == sessionNumber) {
                        btnSelectAll.setEnabled(false);
                    } else {
                        btnSelectAll.setEnabled(true);
                    }

                }
            }
        });

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public interface IOnSelectSessionDone{
        void onSelectSessionDone(ArrayList<Integer> sessionList);
    }
}
