package com.collectioncar.baseapp;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.collectioncar.R;
import com.collectioncar.dialog.XuanZeDialog;
import com.collectioncar.util.ToastUitl;
import com.collectioncar.widget.StatusBarCompat;

import butterknife.ButterKnife;
import cn.jpush.android.api.JPushInterface;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
/**
 * Created by Administrator on 2016/10/19.
 */

public class BaseAct extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        doBeforeSetcontentView();

        //ButterKnife.bind(this);
       /* mContext = this;
        mPresenter = TUtil.getT(this, 0);
        mModel=TUtil.getT(this,1);
        if(mPresenter!=null){
            mPresenter.mContext=this;
        }
        this.initPresenter();
        this.initView();*/

        setNetWork = new XuanZeDialog(this);
        setNetWork.setDialogCallback(setnetworkcallback);
    }
    @Override
    public void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
    }
    @Override
    public void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }
    /**
     * 设置layout前配置
     */
    private void doBeforeSetcontentView() {

        // 把actvity放到application栈中管理
        AppManager.getAppManager().addActivity(this);
        // 无标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 设置竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // 默认着色状态栏
        //SetStatusBarColor();
        SetTranslanteBar();
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
        Context context = getApplicationContext();
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
    public void setNetwork() {

        setNetWork.setTitleText("网络状态");
        setNetWork.setContentText("当前网络不可用，是否设置网络");
        setNetWork.setLeftChoose("设置");
        setNetWork.setRightChoose("取消");
        setNetWork.setCancelInvisible();
        setNetWork.setIcon(R.drawable.icon_warning);
        setNetWork.show();
    }

    /**
     * 着色状态栏（4.4以上系统有效）
     */
    protected void SetStatusBarColor(){
        StatusBarCompat.setStatusBarColor(this, ContextCompat.getColor(this, R.color.main_color));
    }
    /**
     * 着色状态栏（4.4以上系统有效）
     */
    protected void SetStatusBarColor(int color){
        StatusBarCompat.setStatusBarColor(this,color);
    }
    /**
     * 沉浸状态栏（4.4以上系统有效）
     */
    protected void SetTranslanteBar(){
        StatusBarCompat.translucentStatusBar(this);
    }
    /**
     * 网络访问错误提醒
     */
    public void showNetErrorTip() {
        ToastUitl.showToastWithImg(getText(R.string.net_error).toString(),R.drawable.ic_wifi_off);
    }
    public void showNetErrorTip(String error) {
        ToastUitl.showToastWithImg(error,R.drawable.ic_wifi_off);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
       /* if (mPresenter != null)
            mPresenter.onDestroy();
        mRxManager.clear();*/
        ButterKnife.unbind(this);
        AppManager.getAppManager().finishActivity(this);

    }

}
