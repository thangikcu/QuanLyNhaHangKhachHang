package com.thanggun99.khachhang.view.activity;

import android.app.ProgressDialog;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.thanggun99.khachhang.R;
import com.thanggun99.khachhang.model.entity.KhachHang;
import com.thanggun99.khachhang.presenter.KhachHangPresenter;
import com.thanggun99.khachhang.service.ConnectChangeBroadcastReceiver;
import com.thanggun99.khachhang.service.MyBroadcastReceiver;
import com.thanggun99.khachhang.service.MyFirebaseMessagingService;
import com.thanggun99.khachhang.util.Utils;
import com.thanggun99.khachhang.view.dialog.ChangePasswordDialog;
import com.thanggun99.khachhang.view.dialog.NotifiDialog;
import com.thanggun99.khachhang.view.fragment.AboutFragment;
import com.thanggun99.khachhang.view.fragment.FeedbackFragment;
import com.thanggun99.khachhang.view.fragment.HomeFragment;
import com.thanggun99.khachhang.view.fragment.LoginFragment;
import com.thanggun99.khachhang.view.fragment.MyProfileFragment;
import com.thanggun99.khachhang.view.fragment.SettingFragment;
import com.thanggun99.khachhang.view.fragment.ThongTinPhucVuFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements KhachHangPresenter.MainView, PopupMenu.OnMenuItemClickListener, NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    private ActionBarDrawerToggle toggle;
    private LinearLayout lnLogin;
    private TextView tvUsername, tvFullname, tvDangNhap;
    private IntentFilter intentFilter;
    private PopupMenu popupMenu;
    private ImageButton btnArrowDown;

    private KhachHangPresenter khachHangPresenter;


    //fragments
    private Fragment fragmentIsShow;
    private HomeFragment homeFragment;
    private FeedbackFragment feedbackFragment;
    private SettingFragment settingFragment;
    private AboutFragment aboutFragment;
    private ThongTinPhucVuFragment thongTinPhucVuFragment;
    private MyProfileFragment myProfileFragment;
    private LoginFragment loginFragment;

    private ProgressDialog progressDialog;
    private MyBroadcastReceiver myBroadcastReceiver;
    private NotifiDialog notifiDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        findViews();
        initComponets();
        setEvents();
        khachHangPresenter.loginAuto();

    }

    private void initComponets() {
        khachHangPresenter = new KhachHangPresenter(this);

        notifiDialog = new NotifiDialog(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(Utils.getStringByRes(R.string.loading));
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);

        //initFragment
        loginFragment = new LoginFragment(khachHangPresenter);
        homeFragment = new HomeFragment(khachHangPresenter);

        popupMenu = new PopupMenu(this, btnArrowDown);
        popupMenu.getMenuInflater().inflate(R.menu.drop_menu, popupMenu.getMenu());
        toggle = new ActionBarDrawerToggle(this,
                drawerLayout, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        myBroadcastReceiver = new MyBroadcastReceiver(khachHangPresenter);
        intentFilter = new IntentFilter();

    }

    private void setEvents() {
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        popupMenu.setOnMenuItemClickListener(this);
        btnArrowDown.setOnClickListener(this);
        navView.setNavigationItemSelectedListener(this);
        tvDangNhap.setOnClickListener(this);

        showNavigationOnUnLogin();

        intentFilter.addAction(ConnectChangeBroadcastReceiver.CONNECT_AVAIlABLE);
        intentFilter.addAction(ConnectChangeBroadcastReceiver.CONNECT_FAIL);
        intentFilter.addAction(MyFirebaseMessagingService.LOGOUT_ACTION);
        intentFilter.addAction(MyFirebaseMessagingService.NOTIFI_ACTION);
        intentFilter.addAction(MyFirebaseMessagingService.UPDATE_DAT_BAN_ACTION);
        intentFilter.addAction(MyFirebaseMessagingService.HUY_DAT_BAN_ACTION);
        intentFilter.addAction(MyFirebaseMessagingService.TAO_HOA_DON_MOI_ACTION);
        intentFilter.addAction(MyFirebaseMessagingService.GIAM_GIA_HOA_DON_ACTION);
        intentFilter.addAction(MyFirebaseMessagingService.ORDER_MON_ACTION);
        intentFilter.addAction(MyFirebaseMessagingService.TINH_TIEN_HOA_DON_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(myBroadcastReceiver,
                intentFilter);
    }

    private void findViews() {
        lnLogin = ButterKnife.findById(navView.getHeaderView(0), R.id.ln_login);
        tvFullname = ButterKnife.findById(navView.getHeaderView(0), R.id.tv_full_name);
        tvUsername = ButterKnife.findById(navView.getHeaderView(0), R.id.tv_username);
        tvDangNhap = ButterKnife.findById(navView.getHeaderView(0), R.id.tv_login);
        btnArrowDown = ButterKnife.findById(navView.getHeaderView(0), R.id.btn_arrow_down);

    }

    @Override
    public void showLoginFragment() {
        if (loginFragment == null) {
            Utils.showLog("login frm null");
            loginFragment = new LoginFragment(khachHangPresenter);
        }
        fillFrame(loginFragment, 0);
    }

    @Override
    public void showHomeFragment() {
        if (homeFragment == null) {
            homeFragment = new HomeFragment(khachHangPresenter);
        }
        fillFrame(homeFragment, R.id.btn_home);
    }

    @Override
    public void showThongTinPhucVuFragment() {
        if (thongTinPhucVuFragment == null) {

            thongTinPhucVuFragment = new ThongTinPhucVuFragment(khachHangPresenter);
        }

        fillFrame(thongTinPhucVuFragment, R.id.btn_thong_tin_phuc_vu);
    }

    @Override
    public void showTenKhachHang(String hoTen) {
        tvFullname.setText(hoTen);
    }

    @Override
    public void showProgress() {
        progressDialog.show();
    }

    @Override
    public void hideProgress() {
        progressDialog.dismiss();
    }

    private void fillFrame(final Fragment fragment, int id) {
        if (fragment.isVisible()) return;

        if (id != 0) {
            MenuItem menuItem = navView.getMenu().findItem(id);
            menuItem.setChecked(true);
            String title = menuItem.getTitle().toString();
            toolbar.setTitle(title);
        } else if (id == 0) {
            toolbar.setTitle(Utils.getStringByRes(R.string.dang_nhap));
        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (fragmentIsShow != null && fragmentIsShow.isVisible()) transaction.hide(fragmentIsShow);
        if (fragment.isAdded()) {
            transaction.show(fragment);
        } else {
            Utils.showLog("add new fragment" + fragment.getClass().getSimpleName());
            transaction.add(R.id.frame, fragment);
        }
        transaction.commitAllowingStateLoss();
        fragmentIsShow = fragment;

    }

    public void showNavigationOnUnLogin() {
        lnLogin.setVisibility(View.GONE);
        tvDangNhap.setVisibility(View.VISIBLE);
        navView.getMenu().getItem(1).setVisible(false);
        navView.getMenu().getItem(2).setVisible(false);
    }

    public void showNavigationOnLogin() {
        lnLogin.setVisibility(View.VISIBLE);
        tvDangNhap.setVisibility(View.GONE);
        navView.getMenu().getItem(1).setVisible(true);
        navView.getMenu().getItem(2).setVisible(true);
    }

    @Override
    public void showNotify(String message) {
        notifiDialog.notifi(message);
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(myBroadcastReceiver);

        if (progressDialog != null) {
            progressDialog.cancel();
        }
        if (notifiDialog != null) {
            notifiDialog.cancel();
        }
        khachHangPresenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_arrow_down:
                popupMenu.show();
                break;
            case R.id.tv_login:
                drawerLayout.closeDrawer(GravityCompat.START);
                if (loginFragment == null) loginFragment = new LoginFragment(khachHangPresenter);
                fillFrame(loginFragment, 0);
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if (fragmentIsShow != null && !(fragmentIsShow instanceof HomeFragment)) {

                showHomeFragment();
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    public void showViewOnlogin(KhachHang khachHang) {
        showHomeFragment();

        tvUsername.setText(khachHang.getTenDangNhap());
        showTenKhachHang(String.format(Utils.getStringByRes(R.string.ho_ten_khach_hang), khachHang.getTenKhachHang()));
        showNavigationOnLogin();
    }

    @Override
    public void showViewOnUnlogin() {
        tvUsername.setText("");
        tvFullname.setText("");
        drawerLayout.closeDrawer(GravityCompat.START);
        popupMenu.dismiss();
        showLoginFragment();
        showNavigationOnUnLogin();
    }

    @Override
    public void clearFragments() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();

        if (fragments.size() > 0) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            for (Fragment fragment : fragments) {
                if (fragment != null) {

                    Utils.showLog("xoa " + fragment.getClass().getName());
                    transaction.remove(fragment);
                }
            }

            transaction.commit();
        }

        notifiDialog.cancel();
        fragmentIsShow = null;
        thongTinPhucVuFragment = null;
        myProfileFragment = null;
        loginFragment = null;
        homeFragment = null;
        aboutFragment = null;
        settingFragment = null;
        feedbackFragment = null;
    }

    @Override
    public void showOtherLogin() {
        Utils.notifi(Utils.getStringByRes(R.string.other_people_login));
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btn_logout:
                khachHangPresenter.logout();
                return true;
            case R.id.btn_change_password:
                ChangePasswordDialog changePasswordDialog = new ChangePasswordDialog(this, khachHangPresenter);
                changePasswordDialog.show();
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btn_home:
                if (homeFragment == null) homeFragment = new HomeFragment(khachHangPresenter);
                fillFrame(homeFragment, R.id.btn_home);
                break;
            case R.id.btn_gop_y:
                if (feedbackFragment == null) {
                    feedbackFragment = new FeedbackFragment(khachHangPresenter);
                }
                fillFrame(feedbackFragment, R.id.btn_gop_y);
                break;
            case R.id.btn_settings:
                if (settingFragment == null) {
                    settingFragment = new SettingFragment();
                }
                fillFrame(settingFragment, R.id.btn_settings);
                break;
            case R.id.btn_about:
                if (aboutFragment == null) {
                    aboutFragment = new AboutFragment();
                }
                fillFrame(aboutFragment, R.id.btn_about);
                break;
            case R.id.btn_thong_tin_phuc_vu:
                if (thongTinPhucVuFragment == null) {
                    thongTinPhucVuFragment = new ThongTinPhucVuFragment(khachHangPresenter);
                }
                fillFrame(thongTinPhucVuFragment, R.id.btn_thong_tin_phuc_vu);
                break;
            case R.id.btn_my_profile:
                if (myProfileFragment == null) {
                    myProfileFragment = new MyProfileFragment(khachHangPresenter);
                }
                fillFrame(myProfileFragment, R.id.btn_my_profile);
                break;
            default:
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
