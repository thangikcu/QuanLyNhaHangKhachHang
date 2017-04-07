package com.thanggun99.khachhang.view.fragment;


import android.annotation.SuppressLint;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.thanggun99.khachhang.R;
import com.thanggun99.khachhang.adapter.TabsAdapter;
import com.thanggun99.khachhang.presenter.KhachHangPresenter;
import com.thanggun99.khachhang.util.Utils;

import java.util.ArrayList;

import butterknife.BindView;

@SuppressLint("ValidFragment")
public class HomeFragment extends BaseFragment implements KhachHangPresenter.HomeView {
    @BindView(R.id.tabs)
    TabLayout tabs;
    @BindView(R.id.view_pg)
    ViewPager viewPg;
    private KhachHangPresenter khachHangPresenter;
    private TabsAdapter tabsAdapter;
    private ThucDonFragment thucDonFragment;
    private TinTucFragment tinTucFragment;

    public HomeFragment(KhachHangPresenter khachHangPresenter) {
        super(R.layout.fragment_home);
        this.khachHangPresenter = khachHangPresenter;
    }

    @Override
    public void setEvents() {
        khachHangPresenter.setHomeView(this);
        viewPg.setAdapter(tabsAdapter);
        tabs.setupWithViewPager(viewPg);

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 1) {
                    Utils.showLog("load tin tuc");
                    khachHangPresenter.loadTinTucList();
                } else if (tab.getPosition() == 0) {
                    Utils.showLog("load thuc don");
                    khachHangPresenter.loadThucDonList();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    @Override
    public void initComponents() {
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(thucDonFragment = new ThucDonFragment(khachHangPresenter));
        fragments.add(tinTucFragment = new TinTucFragment(khachHangPresenter));
        tabsAdapter = new TabsAdapter(getActivity().getSupportFragmentManager(), fragments);

    }

    @Override
    public void showTabThucDon() {
        viewPg.setCurrentItem(0);
    }

}
