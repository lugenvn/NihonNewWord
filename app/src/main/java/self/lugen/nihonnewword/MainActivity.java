package self.lugen.nihonnewword;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.ads.MobileAds;

import io.fabric.sdk.android.Fabric;
import self.lugen.nihonnewword.fragment.KanjiMainFragment;
import self.lugen.nihonnewword.fragment.MainFragment;
import self.lugen.nihonnewword.fragment.NewWordFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private DrawerLayout mDrawerLayout;
    private RelativeLayout mDrawerPane;
    private TextView tvMenuTitle;
    private RelativeLayout rlMenuNewWord;
    private RelativeLayout rlMenuKanji;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Fabric.with(this, new Crashlytics());


        // Populate the Navigtion Drawer with options
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerPane = (RelativeLayout) findViewById(R.id.drawer_pane);
        initSlideMenuView();
        MobileAds.initialize(this, getString(R.string.ad_app_id));
    }

    private void initSlideMenuView() {
        tvMenuTitle = (TextView) findViewById(R.id.tv_menu_title);
        rlMenuNewWord = (RelativeLayout) findViewById(R.id.rl_menu_new_word);
        rlMenuKanji = (RelativeLayout) findViewById(R.id.rl_menu_kanji);

        rlMenuNewWord.setOnClickListener(this);
        rlMenuKanji.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.rl_menu_new_word:
                mDrawerLayout.closeDrawers();
                openFragment(NewWordFragment.newInstance());
                break;
            case R.id.rl_menu_kanji:
                mDrawerLayout.closeDrawers();
                openFragment(KanjiMainFragment.newInstance());
                break;
        }
    }

    public void disableSideBar() {
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    public void enableSideBar() {
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }

    private void openFragment(Fragment fragment) {
        if (isCurrentFragment(fragment)) return;
        FragmentManager frm = getSupportFragmentManager();
        frm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        FragmentTransaction fmTr = frm.beginTransaction();
        fmTr.replace(R.id.content_main, fragment, fragment.getClass().getName());
        fmTr.addToBackStack(fragment.getClass().getName());
        fmTr.commit();
    }

    private boolean isCurrentFragment(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        Fragment current = manager.findFragmentByTag(fragment.getClass().getName());
        if (current != null && current.isVisible()) {
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        FragmentManager frm = getSupportFragmentManager();
        if (frm.getFragments().size() <= 1) {
            finish();
        } else {
            super.onBackPressed();
        }
    }
}
