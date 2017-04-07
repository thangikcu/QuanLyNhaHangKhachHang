package com.thanggun99.khachhang.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.thanggun99.khachhang.R;
import com.thanggun99.khachhang.model.entity.KhachHang;
import com.thanggun99.khachhang.presenter.KhachHangPresenter;
import com.thanggun99.khachhang.util.Utils;

/**
 * Created by Thanggun99 on 07/04/2017.
 */

public class MyBroadcastReceiver extends BroadcastReceiver {
    private KhachHangPresenter khachHangPresenter;

    public MyBroadcastReceiver(KhachHangPresenter khachHangPresenter) {

        this.khachHangPresenter = khachHangPresenter;
    }

    public MyBroadcastReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        switch (intent.getAction()) {
            case ConnectChangeBroadcastReceiver.CONNECT_AVAIlABLE:
                if (khachHangPresenter != null) {
                    khachHangPresenter.reLoadDatas();
                }
                break;
            case ConnectChangeBroadcastReceiver.CONNECT_FAIL:
                if (khachHangPresenter != null) {
                    khachHangPresenter.showConnectToServerFailDialog();
                }
                break;
            case MyFirebaseMessagingService.NOTIFI_ACTION:
                break;
            case MyFirebaseMessagingService.LOGOUT_ACTION:

                if (khachHangPresenter.checkLogin()) {

                    khachHangPresenter.onOtherLogin();
                    khachHangPresenter.logout();
                }
                break;
            case MyFirebaseMessagingService.UPDATE_DAT_BAN_ACTION:
                if (khachHangPresenter != null) {
                    KhachHang khachHangUpdate = (KhachHang) intent.getSerializableExtra(MyFirebaseMessagingService.KHACH_HANG);

                    khachHangPresenter.updateThongTinKhachHangService(khachHangUpdate);

                    Utils.showNotify(Utils.getStringByRes(R.string.thong_bao),
                            Utils.getStringByRes(R.string.thong_tin_cua_ban_da_duoc_cap_nhat));
                }
                break;
            case MyFirebaseMessagingService.HUY_DAT_BAN_ACTION:
                if (khachHangPresenter != null) {
                    khachHangPresenter.deleteDatBanService();

                    Utils.showNotify(Utils.getStringByRes(R.string.thong_bao),
                            Utils.getStringByRes(R.string.da_huy_dat_ban));
                }
                break;
            case MyFirebaseMessagingService.TAO_HOA_DON_MOI_ACTION:
                if (khachHangPresenter != null) {
                    khachHangPresenter.taoHoaDonMoiService();

                    Utils.showNotify(Utils.getStringByRes(R.string.thong_bao),
                            Utils.getStringByRes(R.string.da_tao_hoa_don_moi));
                }
                break;
            case MyFirebaseMessagingService.GIAM_GIA_HOA_DON_ACTION:
                if (khachHangPresenter != null) {
                    int giamGia = intent.getIntExtra(MyFirebaseMessagingService.GIAM_GIA, 0);

                    khachHangPresenter.giamGiaHoaDonMoiService(giamGia);

                    Utils.showNotify(Utils.getStringByRes(R.string.thong_bao),
                            Utils.getStringByRes(R.string.ban_duoc_giam_gia) + " " + giamGia + "%");
                }
                break;
            case MyFirebaseMessagingService.ORDER_MON_ACTION:
                if (khachHangPresenter != null) {

                    khachHangPresenter.orderMonService();

                    Utils.showNotify(Utils.getStringByRes(R.string.thong_bao),
                            Utils.getStringByRes(R.string.da_them_mon));
                }
                break;
            case MyFirebaseMessagingService.TINH_TIEN_HOA_DON_ACTION:
                if (khachHangPresenter != null) {

                    khachHangPresenter.tinhTienHoaDonService();

                    Utils.showNotify(Utils.getStringByRes(R.string.thong_bao),
                            Utils.getStringByRes(R.string.da_tinh_tien));
                }
                break;
            default:
                break;
        }
    }
}
