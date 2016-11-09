package com.collectioncar.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.app.Notification;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.collectioncar.Jpush.ExampleUtil;
import com.collectioncar.R;
import com.collectioncar.adapter.GengxinAdapter;
import com.collectioncar.baseapp.AppManager;
import com.collectioncar.baseapp.BaseAct;
import com.collectioncar.baseapp.TabEntity;
import com.collectioncar.dialog.XuanZeDialog;
import com.collectioncar.framgent.CareMainFragment;
import com.collectioncar.framgent.NewsMainFragment;
import com.collectioncar.framgent.PhotosMainFragment;
import com.collectioncar.framgent.VideoMainFragment;
import com.collectioncar.update.UpdateManager;
import com.collectioncar.view.HorizontalProgressBarDiglog;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.BasicPushNotificationBuilder;
import cn.jpush.android.api.CustomPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;


/**
 * des:主界面
 * Created by xsf
 * on 2016.09.15:32
 */
public class MainTabActivity extends BaseAct {


    @Bind(R.id.fl_body)
    FrameLayout flBody;
    @Bind(R.id.tab_layout)
    CommonTabLayout tabLayout;
    private String[] mTitles = {"首页", "美女", "视频", "关注"};
    private int[] mIconUnselectIds = {
            R.drawable.ic_home_normal, R.drawable.ic_girl_normal, R.drawable.ic_video_normal, R.drawable.ic_care_normal};
    private int[] mIconSelectIds = {
            R.drawable.ic_home_selected, R.drawable.ic_girl_selected, R.drawable.ic_video_selected, R.drawable.ic_care_selected};
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();

    private NewsMainFragment newsMainFragment;
    private PhotosMainFragment photosMainFragment;
    private VideoMainFragment videoMainFragment;
    private CareMainFragment careMainFragment;
    private static int tabLayoutHeight;

    private static Boolean isExit = false;

    /****************
      检查更新*/
    private Handler handler = new Handler();
    private GengxinAdapter adaptergengxin;
    private List<String> listgengxin = new ArrayList<String>(); //存储更新提示的提示语
    private HorizontalProgressBarDiglog updateProgressDialog;
    private UpdateManager updateMan;
    private XuanZeDialog checkUpdate;
    private XuanZeDialog.Dialogcallback checkupdatecallback = new XuanZeDialog.Dialogcallback() {
        @Override
        public void paizhao() {
            //更新
            //显示下载进度条
            updateProgressDialog = new HorizontalProgressBarDiglog(MainTabActivity.this);
						/*updateProgressDialog
								.setMessage(getText(R.string.dialog_downloading_msg));
						updateProgressDialog.setIndeterminate(false);
						updateProgressDialog
								.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

						updateProgressDialog.setMax(100);*/
            updateProgressDialog.setProgress(0);
            updateProgressDialog.show();
				/*	updateProgressDialog.setCanceledOnTouchOutside(false);
					updateProgressDialog.setCancelable(true);*/
            //进行下载
            updateMan.downloadPackage();
        }

        @Override
        public void tuku() {
            //暂不更新
        }
    };
    //查看版本是否需要更新
    private void gengxin() {


        PackageInfo pInfo=null;


        try {
            pInfo = MainTabActivity.this.getPackageManager().getPackageInfo(
                    MainTabActivity.this.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if(pInfo!=null){
            updateMan = new UpdateManager(this, appUpdateCb);
            handler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    updateMan.checkUpdate();

                }
            }, 500);


        }

    }

    //自动更新回调函数
    UpdateManager.UpdateCallback appUpdateCb = new UpdateManager.UpdateCallback() {

        //进度条数值的变化,用来显示已经下载完成了多少
        @Override
        public void downloadProgressChanged(int progress) {
            if (updateProgressDialog != null) {
                updateProgressDialog.setProgress(progress);
            }

        }


        //下载完成
        @Override
        public void downloadCompleted(Boolean sucess, CharSequence errorMsg) {
            //关闭进度显示条
            if (updateProgressDialog != null
                    ) {
                updateProgressDialog.dismiss();
            }
            if (sucess) {
                //更新成功
			  	/*SharedPreferences sp =getApplication().getSharedPreferences("shoucijinru", Context.MODE_PRIVATE);
			  	sp.edit().putBoolean("first", false).commit();*/
                updateMan.update();


            } else {
                checkUpdate.setTitleText(getText(R.string.dialog_error_title).toString());
                checkUpdate.setContentText(getText(R.string.dialog_downfailed_msg).toString());
                checkUpdate.setLeftChoose(getText(R.string.dialog_downfailed_msg22).toString());
                checkUpdate.setRightChoose(getText(R.string.dialog_downfailed_btnnext).toString());
                checkUpdate.setCancelInvisible();
                checkUpdate.setIcon(R.drawable.icon_warning);
                checkUpdate.show();

            }
        }
        @Override
        public void downloadCanceled() {

        }

        //检查是否需要更新回调hasUpdate为true则表示有新的版本
        @Override
        public void checkUpdateCompleted(Boolean hasUpdate,
                                         CharSequence updateInfo) {
            if (hasUpdate) {


                final AlertDialog builder  = new AlertDialog.Builder(MainTabActivity.this).create();
                builder.show();
                builder.getWindow().setContentView(R.layout.baididialog);

                builder.setMessage( getText(R.string.dialog_update_msg).toString()+ updateInfo);

                    builder.setTitle(getText(R.string.dialog_update_title));
                    if(builder == null){
                    return;
                }
                builder.setCanceledOnTouchOutside(false);
                builder.getWindow().findViewById(R.id.quxiao_text)
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                builder.dismiss();

                            }
                        });

                TextView banebnhao = (TextView)builder.getWindow().findViewById(R.id.banebnhao);
                banebnhao.setText(updateInfo);


                ListView gengxinlist = (ListView)builder.getWindow().findViewById(R.id.gengxinlist);
                if(listgengxin.size() <=0){
                    listgengxin.add(0, "1.增强版本的稳定性");
                    listgengxin.add(1, "2.更好的用户交互");
                }


                adaptergengxin = new GengxinAdapter(MainTabActivity.this, listgengxin);
                gengxinlist.setAdapter(adaptergengxin);


                //点击更新
                builder.getWindow().findViewById(R.id.gengxin)
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                builder.dismiss();

                                //显示下载进度条
                                updateProgressDialog = new HorizontalProgressBarDiglog(
                                        MainTabActivity.this);

                                updateProgressDialog.setProgress(0);
                                updateProgressDialog.show();

                                updateMan.downloadPackage();
                            }
                        });


            }else{
                 }

        }
    };

    /****************
     检查更新*/
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tab);
        ButterKnife.bind(this);

        initTab();
        //初始化frament
        initFragment(savedInstanceState);
        tabLayout.measure(0, 0);
        tabLayoutHeight = tabLayout.getMeasuredHeight();
        //监听底部framegent显示或隐藏
       /* mRxManager.on(AppConstant.MENU_SHOW_HIDE, new Action1<Boolean>() {

            @Override
            public void call(Boolean hideOrShow) {
                startAnimation(hideOrShow);
            }
        });*/


        //JPushInterface.init(getApplicationContext());

        String udid =  ExampleUtil.getImei(getApplicationContext(), "");
        String appKey = ExampleUtil.getAppKey(getApplicationContext());
        String deviceId = ExampleUtil.getDeviceId(getApplicationContext());
        String versionName =  ExampleUtil.GetVersion(getApplicationContext());
        //获取Registratio 推送以这个为标识符
        String rid = JPushInterface.getRegistrationID(getApplicationContext());

        CustomPushNotificationBuilder builder = new CustomPushNotificationBuilder(MainTabActivity.this,R.layout.customer_notitfication_layout,R.id.icon, R.id.title, R.id.text);

        builder.statusBarDrawable= R.drawable.ic_launcher;//最顶层状态栏小图标

        builder.layoutIconDrawable=R.drawable.ic_launcher;  //下拉状态时显示的通知图标.

        JPushInterface.setPushNotificationBuilder(2, builder);
        JPushInterface.setDefaultPushNotificationBuilder(builder); //设置该对话框为默认.自定义消息:**
        //初始化

        /*//停止推送
       // JPushInterface.stopPush(getApplicationContext());
        //开启推送
        // 	JPushInterface.resumePush(getApplicationContext());*/
        //检查版本更新
        gengxin();


    }

    /**
     * 初始化tab
     */
    private void initTab() {
        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }
        tabLayout.setTabData(mTabEntities);
        //点击监听
        tabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                SwitchTo(position);
            }

            @Override
            public void onTabReselect(int position) {
            }
        });
    }

    /**
     * 初始化碎片
     */
    private void initFragment(Bundle savedInstanceState) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        int currentTabPosition = 0;
        if (savedInstanceState != null) {
            newsMainFragment = (NewsMainFragment) getSupportFragmentManager().findFragmentByTag("newsMainFragment");
            photosMainFragment = (PhotosMainFragment) getSupportFragmentManager().findFragmentByTag("photosMainFragment");
            videoMainFragment = (VideoMainFragment) getSupportFragmentManager().findFragmentByTag("videoMainFragment");
            careMainFragment = (CareMainFragment) getSupportFragmentManager().findFragmentByTag("careMainFragment");
            //currentTabPosition = savedInstanceState.getInt(AppConstant.HOME_CURRENT_TAB_POSITION);
        } else {
            newsMainFragment = new NewsMainFragment();
            photosMainFragment = new PhotosMainFragment();
            videoMainFragment = new VideoMainFragment();
            careMainFragment = new CareMainFragment();

            transaction.add(R.id.fl_body, newsMainFragment, "newsMainFragment");
            transaction.add(R.id.fl_body, photosMainFragment, "photosMainFragment");
            transaction.add(R.id.fl_body, videoMainFragment, "videoMainFragment");
            transaction.add(R.id.fl_body, careMainFragment, "careMainFragment");
        }
        transaction.commit();
        SwitchTo(currentTabPosition);
        tabLayout.setCurrentTab(currentTabPosition);
    }

    /**
     * 切换
     */
    private void SwitchTo(int position) {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch (position) {
            //首页
            case 0:
                transaction.hide(photosMainFragment);
                transaction.hide(videoMainFragment);
                transaction.hide(careMainFragment);
                transaction.show(newsMainFragment);
                transaction.commitAllowingStateLoss();
                break;
            //美女
            case 1:
                transaction.hide(newsMainFragment);
                transaction.hide(videoMainFragment);
                transaction.hide(careMainFragment);
                transaction.show(photosMainFragment);
                transaction.commitAllowingStateLoss();
                break;
            //视频
            case 2:
                transaction.hide(newsMainFragment);
                transaction.hide(photosMainFragment);
                transaction.hide(careMainFragment);
                transaction.show(videoMainFragment);
                transaction.commitAllowingStateLoss();
                break;
            //关注
            case 3:
                transaction.hide(newsMainFragment);
                transaction.hide(photosMainFragment);
                transaction.hide(videoMainFragment);
                transaction.show(careMainFragment);
                transaction.commitAllowingStateLoss();
                break;
            default:
                break;
        }
    }



    /**
     * 监听全屏视频时返回键
     */
    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }

    /**
     * 监听返回键
     *
     * @param keyCode
     * @param event
     * @return
     */
  /*  @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(false);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }*/
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitBy2Click(); // 调用双击退出函数
        } else if (keyCode == KeyEvent.KEYCODE_MENU) {
            return false;
        }
        return false;
    }

    private void exitBy2Click() {
        Timer tExit = null;
        if (isExit == false) {
            isExit = true;// 准备退出
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false; // 取消退出
                }
            }, 2000);// 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

        } else {
//            MyApplication.getInstance().setUseInfoVo(null);
//            finish();
           // exitToHome();
            AppManager.getAppManager().AppExit(this,true);
        }
    }
    @Override
    public void onResume() {
        super.onResume();

    }
    public void onPause() {
        super.onPause();

    }
    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @OnClick({R.id.fl_body, R.id.tab_layout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fl_body:
                break;
            case R.id.tab_layout:
                break;
        }
    }
}
