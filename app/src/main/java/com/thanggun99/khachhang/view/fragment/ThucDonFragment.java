package com.thanggun99.khachhang.view.fragment;

import android.annotation.SuppressLint;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.thanggun99.khachhang.R;
import com.thanggun99.khachhang.adapter.MonAdapter;
import com.thanggun99.khachhang.adapter.NhomMonAdapter;
import com.thanggun99.khachhang.model.entity.Mon;
import com.thanggun99.khachhang.presenter.KhachHangPresenter;
import com.thanggun99.khachhang.util.Utils;
import com.thanggun99.khachhang.view.dialog.OrderMonDialog;

import java.util.ArrayList;


@SuppressLint("ValidFragment")
public class ThucDonFragment extends BaseFragment implements KhachHangPresenter.ThucDonView,
        View.OnClickListener {

    private KhachHangPresenter khachHangPresenter;

    private RecyclerView nhomMonListView;
    private RecyclerView monListView;

    private NhomMonAdapter nhomMonAdapter;
    private MonAdapter monAdapter;
    private OrderMonDialog orderMonDialog;

    public ThucDonFragment(KhachHangPresenter khachHangPresenter) {
        super(R.layout.fragment_thuc_don);
        this.khachHangPresenter = khachHangPresenter;
    }

    @Override
    public void findViews(View view) {
        monListView = (RecyclerView) view.findViewById(R.id.list_mon);
        nhomMonListView = (RecyclerView) view.findViewById(R.id.list_nhom_mon);

    }

    @Override
    public void initComponents() {
        orderMonDialog = new OrderMonDialog(getContext(), khachHangPresenter);

    }

    @Override
    public void setEvents() {
        nhomMonListView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        monListView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        khachHangPresenter.setThucDonView(this);
        khachHangPresenter.loadThucDonList();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            default:
                break;
        }
    }


    @Override
    public void showRequireLoginNotify() {
        Utils.notifi(Utils.getStringByRes(R.string.ban_can_dang_nhap_de_goi_mon));
    }

    @Override
    public void showOrderMonDialog(Mon mon) {
        orderMonDialog.setContent(mon);
    }

    @Override
    public void showThucDon() {
        if (nhomMonAdapter == null) {
            nhomMonAdapter = new NhomMonAdapter(getContext(), khachHangPresenter);
            monAdapter = new MonAdapter(getContext(), khachHangPresenter);

            nhomMonListView.setAdapter(nhomMonAdapter);
            monListView.setAdapter(monAdapter);
        }

    }

    @Override
    public void notifyChangeListThucDon(ArrayList<Mon> listMonByMaLoai) {
        monListView.scrollToPosition(0);
        monAdapter.changeData(listMonByMaLoai);
    }

    @Override
    public void showErrorGetThucDonFail() {
        Utils.notifi(Utils.getStringByRes(R.string.tai_danh_sach_thuc_don_that_bai));
    }
}
