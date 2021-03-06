package in.ashwanik.udacitystockhawk.activity;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.joanzapata.iconify.fonts.MaterialIcons;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.ButterKnife;
import in.ashwanik.udacitystockhawk.R;
import in.ashwanik.udacitystockhawk.events.DisplayChangeEvent;
import in.ashwanik.udacitystockhawk.fragments.GraphActivityFragment;
import in.ashwanik.udacitystockhawk.utils.FontIconHelper;

public class GraphActivity extends BaseActivity {

    public CoordinatorLayout getCoordinatorLayout() {
        return (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
    }

    public FloatingActionButton getFloatActionButton() {
        return floatActionButton;
    }

    @Bind(R.id.floatActionButton)
    FloatingActionButton floatActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_small);
        ButterKnife.bind(this);
        setToolBar(false);
        floatActionButton.setVisibility(View.GONE);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, new GraphActivityFragment(), "TAG_FRAGMENT");
        ft.commitAllowingStateLoss();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.showChange).setVisible(false);
        menu.findItem(R.id.showChange).setIcon(FontIconHelper.getFontDrawable(this, MaterialIcons.md_attach_money, 36, R.color.white));
        menu.findItem(R.id.showChange).setTitle(isShowChange ? getString(R.string.showChange) : getString(R.string.showChangePercentage));
        return true;
    }

    boolean isShowChange = true;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.showChange:
                isShowChange = !isShowChange;
                EventBus.getDefault().post(new DisplayChangeEvent(isShowChange));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
