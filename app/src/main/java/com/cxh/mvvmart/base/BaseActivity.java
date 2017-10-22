package com.cxh.mvvmart.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.cxh.mvvmart.R;
import com.cxh.mvvmart.manager.ActivityManager;
import com.cxh.mvvmart.ui.widget.autolayout.AutoCardView;
import com.cxh.mvvmart.ui.widget.autolayout.AutoConstraintLayout;
import com.cxh.mvvmart.ui.widget.autolayout.AutoRadioGroup;
import com.cxh.mvvmart.ui.widget.autolayout.AutoScrollView;
import com.cxh.mvvmart.ui.widget.autolayout.AutoTabLayout;
import com.cxh.mvvmart.ui.widget.autolayout.AutoToolbar;
import com.fingdo.statelayout.StateLayout;
import com.socks.library.KLog;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.zhy.autolayout.AutoFrameLayout;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasFragmentInjector;
import dagger.android.support.HasSupportFragmentInjector;

/**
 * @author Hai (haigod7[at]gmail[dot]com)
 *         2017/3/6
 */
public abstract class BaseActivity extends RxAppCompatActivity implements StateLayout.OnViewRefreshListener,
        HasFragmentInjector, HasSupportFragmentInjector {

    private static final String LAYOUT_LINEARLAYOUT = "LinearLayout";
    private static final String LAYOUT_FRAMELAYOUT = "FrameLayout";
    private static final String LAYOUT_RELATIVELAYOUT = "RelativeLayout";
    private static final String LAYOUT_SCROLLVIEW = "ScrollView";
    private static final String LAYOUT_RADIOGROUP = "RadioGroup";
    private static final String LAYOUT_CARDVIEW = "android.support.v7.widget.CardView";
    private static final String LAYOUT_TOOLBAR = "android.support.v7.widget.Toolbar";
    private static final String LAYOUT_TABLAYOUT = "android.support.design.widget.TabLayout";
    private static final String LAYOUT_CONSTRAINTLAYOUT = "android.support.constraint.ConstraintLayout";

    private StateLayout stateLayout;
    public Toolbar toolbar;
    public TextView toolbarTitle;

    @Inject
    DispatchingAndroidInjector<Fragment> supportFragmentInjector;
    @Inject
    DispatchingAndroidInjector<android.app.Fragment> frameworkFragmentInjector;

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return supportFragmentInjector;
    }

    @Override
    public AndroidInjector<android.app.Fragment> fragmentInjector() {
        return frameworkFragmentInjector;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (isInject())  AndroidInjection.inject(this);

        super.onCreate(savedInstanceState);

        dataBindingView();

        ActivityManager.getInstance().pushOneActivity(this);

        if (isUseDefaultToolbar()) setupToolbar();

        if (isUseEventBus()) EventBus.getDefault().register(this);

        Bundle extras = getIntent().getExtras();
        if (null != extras) getBundleExtras(extras);

        setupStateLayout();

        initViewsAndEvents();
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        View view = null;
        if (name.equals(LAYOUT_FRAMELAYOUT)) view = new AutoFrameLayout(context, attrs);

        if (name.equals(LAYOUT_LINEARLAYOUT)) view = new AutoLinearLayout(context, attrs);

        if (name.equals(LAYOUT_RELATIVELAYOUT)) view = new AutoRelativeLayout(context, attrs);

        if (name.equals(LAYOUT_CARDVIEW)) view = new AutoCardView(context, attrs);

        if (name.equals(LAYOUT_TOOLBAR)) view = new AutoToolbar(context, attrs);

        if (name.equals(LAYOUT_RADIOGROUP)) view = new AutoRadioGroup(context, attrs);

        if (name.equals(LAYOUT_SCROLLVIEW)) view = new AutoScrollView(context, attrs);

        if (name.equals(LAYOUT_TABLAYOUT)) view = new AutoTabLayout(context, attrs);

        if (name.equals(LAYOUT_CONSTRAINTLAYOUT)) view = new AutoConstraintLayout(context, attrs);

        if (view != null) return view;

        return super.onCreateView(name, context, attrs);
    }

    @Override
    protected void onDestroy() {
        if (isUseEventBus()) EventBus.getDefault().unregister(this);
        super.onDestroy();
        ActivityManager.getInstance().popOneActivity(this);
    }

    private void setupToolbar() {
        toolbar = findViewById(R.id.toolbar);
        toolbarTitle = findViewById(R.id.toolbarTitle);
        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (null != supportActionBar) {
            supportActionBar.setDisplayHomeAsUpEnabled(displayHomeAsUpEnabled());
            supportActionBar.setDisplayShowTitleEnabled(false);
        }
    }

    private void setupStateLayout() {
        stateLayout = findViewById(R.id.stateLayout);
        if (null != stateLayout) {
            stateLayout.setUseAnimation(false);
            stateLayout.setTipText(5, getString(R.string.statelayout_loading));
            stateLayout.setRefreshListener(this);
            stateLayout.showLoadingView();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void refreshClick() {
        stateLayout.showLoadingView();
        refreshState();
    }

    @Override
    public void loginClick() {

    }

    @Subscribe
    public void onEvent(String event) {
        KLog.e(event);
    }

    public void showLoadingView() {
        stateLayout.showLoadingView();
    }

    public void showContentView() {
        stateLayout.showContentView();
    }

    public void showErrorView() {
        stateLayout.showErrorView();
    }

    public void showTimeoutView() {
        stateLayout.showTimeoutView();
    }

    protected void pushPage(Class<?> clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }

    protected void pushPage(Class<?> clazz, Bundle bundle) {
        Intent intent = new Intent(this, clazz);
        if (null != bundle) intent.putExtras(bundle);
        startActivity(intent);
    }

    protected  void pushPageThenKill(Class<?> clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
        finish();
    }

    protected void pushPageThenKill(Class<?> clazz, Bundle bundle) {
        Intent intent = new Intent(this, clazz);
        if (null != bundle) intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

    protected void pushPageForResult(Class<?> clazz, int requestCode) {
        Intent intent = new Intent(this, clazz);
        startActivityForResult(intent, requestCode);
    }

    protected  void pushPageForResult(Class<?> clazz, int requestCode, Bundle bundle) {
        Intent intent = new Intent(this, clazz);
        if (null != bundle) intent.putExtras(bundle);
        startActivityForResult(intent, requestCode);
    }

    protected void showSnackbar(View v, String msg) {
        if (!TextUtils.isEmpty(msg)) Snackbar.make(v, msg, Snackbar.LENGTH_SHORT).show();
    }

    protected void getBundleExtras(Bundle extras) {
    }

    protected boolean isInject() {
        return true;
    }

    protected boolean isUseDefaultToolbar() {
        return true;
    }

    protected boolean isUseEventBus() {
        return false;
    }

    protected boolean displayHomeAsUpEnabled() {
        return true;
    }

    protected abstract void dataBindingView();

    protected abstract void initViewsAndEvents();

    protected abstract void refreshState();

}
