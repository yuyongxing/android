package com.collectioncar.baseapp;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.aspsine.irecyclerview.animation.ScaleInAnimation;
import com.collectioncar.R;
import com.collectioncar.adapter.CheShangQuanAdapter;
import com.collectioncar.dialog.XuanZeDialog;
import com.collectioncar.util.ToastUitl;

import butterknife.ButterKnife;
import rx.Subscription;

/**
 * Created by Administrator on 2016/10/20.
 */

public class BaseFragment extends Fragment {
    //protected Subscription subscription;
    /**
     * 网络访问错误提醒
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.)


        setNetWork = new XuanZeDialog(getContext());
        setNetWork.setDialogCallback(setnetworkcallback);

    }

    public void showNetErrorTip() {
        ToastUitl.showToastWithImg(getText(R.string.net_error).toString(),R.drawable.ic_wifi_off);
    }

    public void showNetErrorTip(String error) {
        ToastUitl.showToastWithImg(error,R.drawable.ic_wifi_off);
    }
    public void setNetwork() {

        setNetWork.setTitleText("网络状态");
        setNetWork.setContentText("当前网络不可用，是否设置网络");
        setNetWork.setLeftChoose("设置");
        setNetWork.setRightChoose("取消");
        setNetWork.setCancelInvisible();
        setNetWork.setIcon(R.drawable.icon_warning);
        setNetWork.show();
    }
    //设置网络
    XuanZeDialog setNetWork;
    XuanZeDialog.Dialogcallback setnetworkcallback = new XuanZeDialog.Dialogcallback() {
        @Override
        public void paizhao() {
            //设置
            Intent intent = null;
            // 判断手机系统的版本 即API大于10 就是3.0或以上版本
            if (android.os.Build.VERSION.SDK_INT > 10) {
                intent = new Intent(
                        android.provider.Settings.ACTION_WIRELESS_SETTINGS);
            } else {
                intent = new Intent();
                ComponentName component = new ComponentName(
                        "com.android.settings",
                        "com.android.settings.WirelessSettings");
                intent.setComponent(component);
                intent.setAction("android.intent.action.VIEW");
            }
            startActivity(intent);
        }

        @Override
        public void tuku() {
            //取消
        }
    };
    // NETWORK
    public boolean isNetworkAvailable() {
        Context context = getContext();
        ConnectivityManager connect = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connect == null) {
            return false;
        } else// get all network info
        {
            NetworkInfo[] info = connect.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
      //  unsubscribe();
    }

 /*   protected void unsubscribe() {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }*/

}
