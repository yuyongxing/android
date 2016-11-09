package com.collectioncar.update;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import com.collectioncar.URL.SystemConstant;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/*
 * 自动更新，更新管理
 */
public class UpdateManager {

	private String curVersion;	//当前版本号
	private String newVersion;	//新版本号
	private int curVersionCode;
	private int newVersionCode;
	private String updateInfo;	//更新信息
	private UpdateCallback callback;	//回调
	private Context ctx;	//上下文

	private int progress;	//进度条
	private Boolean hasNewVersion;	//是否有新版本
	private Boolean canceled;	//取消更新

	// 存放更新APK文件的路径
	public static final String UPDATE_DOWNURL = SystemConstant.HTTP_SERVICE_URL+"resource/android/chexiaoke.apk";
	// 存放更新APK文件相应的版本说明路径
	public static final String UPDATE_CHECKURL = SystemConstant.HTTP_SERVICE_URL+"resource/android/newappversion.txt";
	public static final String UPDATE_APKNAME = "chexingapp.apk";
	public static final String UPDATE_SAVENAME = "chexingapp.apk";
	private static final int UPDATE_CHECKCOMPLETED = 1;
	private static final int UPDATE_DOWNLOADING = 2;
	private static final int UPDATE_DOWNLOAD_ERROR = 3;
	private static final int UPDATE_DOWNLOAD_COMPLETED = 4;
	private static final int UPDATE_DOWNLOAD_CANCELED = 5;

	// 从服务器上下载apk存放文件夹
	private String savefolder = "/chexingapp/";

	public UpdateManager(Context context, UpdateCallback updateCallback) {
		ctx = context;
		callback = updateCallback;
		canceled = false;
		getCurVersion();//得到版本号等信息
	}

	public String getNewVersionName() {
		return newVersion;
	}

	public String getUpdateInfo() {
		return updateInfo;
	}

	//获取当前的版本号
	private void getCurVersion() {
		try {
			PackageInfo pInfo = ctx.getPackageManager().getPackageInfo(
					ctx.getPackageName(), 0);
			curVersion = pInfo.versionName;
			curVersionCode = pInfo.versionCode;
		} catch (NameNotFoundException e) {
			curVersion = "1.1.1000";
			curVersionCode = 111000;
		}

	}

	/*
	 *  检测版本更新内容
	 */
	public void checkUpdate() {
		hasNewVersion = false;

		new Thread() {
			@Override
			public void run() {
				try {
					String verjson = NetHelper.httpStringGet(UPDATE_CHECKURL);//获取线上的版本信息
					JSONArray array = new JSONArray(verjson);

					if (array.length() > 0) {
						JSONObject obj = array.getJSONObject(0);
						try {
							//获取最新版本的versioncode和versionname
							newVersionCode = Integer.parseInt(obj
									.getString("verCode"));
							newVersion = obj.getString("verName");
							updateInfo = "";
							if (newVersionCode > curVersionCode) {
								hasNewVersion = true;
							}
						} catch (Exception e) {
							newVersionCode = -1;
							newVersion = "";
							updateInfo = "";

						}
					}
				} catch (Exception e) {
					//检查是否需要更新
				}
				updateHandler.sendEmptyMessage(UPDATE_CHECKCOMPLETED);
			}
		}.start();

	}

	/*
	 * 更新
	 */
	//已经下载完最新的apk包了，进行更新安装操作
	public void update() {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setDataAndType(
				Uri.fromFile(new File(Environment.getExternalStorageDirectory(), UPDATE_SAVENAME)),
				"application/vnd.android.package-archive");
		ctx.startActivity(intent);
	}

	/*
	 * 下载数据包
	 */
	public void downloadPackage() {
		new Thread() {
			@Override
			public void run() {
				try {
					//最新的android下载包地址
					URL url = new URL(UPDATE_DOWNURL);

					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
					conn.connect();
					int length = conn.getContentLength();
					InputStream is = conn.getInputStream();

					File ApkFile = new File(Environment.getExternalStorageDirectory(), UPDATE_SAVENAME);
					File savefolderFile = new File(savefolder);
					if (!savefolderFile.exists()) {
						savefolderFile.mkdir();
					}
					if (ApkFile.exists()) {

						ApkFile.delete();
					}

					FileOutputStream fos = new FileOutputStream(ApkFile);

					int count = 0;
					byte buf[] = new byte[512];

					do {

						int numread = is.read(buf);
						count += numread;
						progress = (int) (((float) count / length) * 100);
						//显示下载进度
						updateHandler.sendMessage(updateHandler
								.obtainMessage(UPDATE_DOWNLOADING));
						if (numread <= 0) {
							//下载完成
							updateHandler
									.sendEmptyMessage(UPDATE_DOWNLOAD_COMPLETED);
							break;
						}
						fos.write(buf, 0, numread);
					} while (!canceled);
					if (canceled) {
						updateHandler
								.sendEmptyMessage(UPDATE_DOWNLOAD_CANCELED);
					}
					fos.close();
					is.close();
				} catch (MalformedURLException e) {
					e.printStackTrace();

					updateHandler.sendMessage(updateHandler.obtainMessage(
							UPDATE_DOWNLOAD_ERROR, e.getMessage()));
				} catch (IOException e) {
					e.printStackTrace();

					updateHandler.sendMessage(updateHandler.obtainMessage(
							UPDATE_DOWNLOAD_ERROR, e.getMessage()));
				}

			}
		}.start();
	}

	/*
	 * 取消下载
	 */
	public void cancelDownload() {
		canceled = true;
	}

	/*
	 *  事件处理器
	 */
	Handler updateHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {
				//检查是否需要更新
				case UPDATE_CHECKCOMPLETED:

					callback.checkUpdateCompleted(hasNewVersion, newVersion);
					break;
				//显示下载进度
				case UPDATE_DOWNLOADING:

					callback.downloadProgressChanged(progress);
					break;

				//下载出错异常了
				case UPDATE_DOWNLOAD_ERROR:

					callback.downloadCompleted(false, msg.obj.toString());
					break;

				//下载完成
				case UPDATE_DOWNLOAD_COMPLETED:

					callback.downloadCompleted(true, "");
					break;
				//取消下载
				case UPDATE_DOWNLOAD_CANCELED:

					callback.downloadCanceled();
				default:
					break;
			}
		}
	};

	/*
	 *  更新回调
	 */
	public interface UpdateCallback {
		void checkUpdateCompleted(Boolean hasUpdate,
								  CharSequence updateInfo);

		void downloadProgressChanged(int progress);

		void downloadCanceled();

		void downloadCompleted(Boolean sucess, CharSequence errorMsg);
	}

	public	boolean gethasNewVersion(){
		return hasNewVersion;
	}

}
