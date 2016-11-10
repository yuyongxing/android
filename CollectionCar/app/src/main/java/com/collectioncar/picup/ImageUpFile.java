package com.collectioncar.picup;


import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.collectioncar.util.FileSizeUtil;
import com.collectioncar.util.ImageUtil;
import com.collectioncar.util.JsonUtil;
import com.collectioncar.util.PicupUtil;
import com.google.gson.JsonObject;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ImageUpFile {

	AddUrl addurl;

	public boolean upfilefinish=true;


	public interface AddUrl {
		void addurl(String filePath, String relativePath);
	}

	HttpUtils http = new HttpUtils();

	Context context;
	public  final File PHOTO_DIR = new File(PicupUtil.picdown
			+ "PICJIETU");
	public ImageUpFile(Context context, AddUrl addurl) {
		this.context=context;
		this.addurl=addurl;
	}
	/**
	 * 保存文件
	 * @param
	 * @param
	 * @throws IOException
	 */

	public String getPhotoFileName() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		return dateFormat.format(date) + ".jpg";
	}
	public File saveFile(Bitmap bm, String fileName) throws IOException {
		PHOTO_DIR.mkdirs();// 创建照片的存储目录
		String	picName = getPhotoFileName();
		picName = picName.replace("-", "");
		picName = picName.replace(":", "");

		fileName=fileName.replace("/", "");
		fileName=fileName.substring(0, fileName.length() - 4);
		fileName=fileName.trim();
//		Log.e("picName",""+fileName+picName);

		File myCaptureFile = new File(PHOTO_DIR, fileName+".jpg");

		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
		bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
		bos.flush();
		bos.close();
		return myCaptureFile;
	}

	public  void uploadMethod(final String uploadHost,final String filePath ,final TextView baifenbi) {
		final RequestParams params=new RequestParams();
//		params.addHeader("name", "value");
//		params.addQueryStringParameter("name", "value");
//		params.addBodyParameter("msg","AB");
		//	File file=new File(filePath);
		double picrealsize= FileSizeUtil.getFileOrFilesSize(filePath,FileSizeUtil.SIZETYPE_KB);
		Log.e("filePath",filePath);
		File file= null;
		if(picrealsize>400){

		/*	BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
			opts.inPreferredConfig = Bitmap.Config.RGB_565;
			opts.inPurgeable = true;// 同时设置才会有效
			opts.inInputShareable = true;//。当系统内存不够时候图片自动被回收
			Bitmap image = BitmapFactory.decodeFile(filePath, opts);

			int picHeight = opts.outHeight;
			int picWidth = opts.outWidth;
			int picsize=  ((picHeight)*(picWidth)*16)/(8*1024);
			//isSampleSize是表示对图片的缩放程度，比如值为2图片的宽度和高度都变为以前的1/2


			opts.inSampleSize = 1;
			//根据占用内存来进行缩放
			if(picsize>4*1024){
				opts.inSampleSize=picsize/(4*1024);
			}
			//opts.inSampleSize=1;
			opts.inJustDecodeBounds = false;
			image = BitmapFactory.decodeFile(filePath, opts);*/

			Bitmap image= ImageUtil.getBitmapFromFile(filePath, 1600, 1200);

			try {
				file = saveFile(image,filePath);
				// file=new File(filePath);


			} catch (Exception e) {
				e.printStackTrace();
			}

		}else{
			file=new File(filePath);
		}

		params.addBodyParameter("name", "file");
		params.addBodyParameter("file", file);
		Log.e("filePath",""+file);
		new Handler().post(new Runnable() {

			@Override
			public void run() {
				http.send(HttpRequest.HttpMethod.POST, uploadHost, params,new RequestCallBack<String>() {
					@Override
					public void onStart() {
//                    msgTextview.setText("conn...");
					}



					@Override
					public void onLoading(long total, long current,boolean isUploading) {
						if (isUploading) {
							upfilefinish=false;


						} else {
//
						}
						NumberFormat numberFormat = NumberFormat.getInstance();
						if((float) current / (float) total * 100>0){
							String result = numberFormat.format((float) current / (float) total * 100);
							if(baifenbi!=null){

								baifenbi.setText(result + "%");
								Toast.makeText(context, result, 1).show();
							}

						}else{


						}

					/*  String result = numberFormat.format((float) current / (float) total * 100);

					  Toast.makeText(context, result, Toast.LENGTH_SHORT).show();*/


					}
					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
//                    msgTextview.setText("reply: " + responseInfo.result);
						Toast.makeText(context, "uploaddone", 1).show();
						upfilefinish=true;
						String response=responseInfo.result;
						// Log.e("filePath",response);
						if(response!=null&&response.length()>0){
							JsonObject jsonobject = JsonUtil.jsonToGoogleJsonObject(response);//将数据转化成google形式的JsonObject
							if(jsonobject.has("relativePath")&&jsonobject.get("relativePath").toString().length()>0){

								String relativePath=jsonobject.get("relativePath").toString();

								addurl.addurl(filePath, relativePath);


							}
						}



					}
					@Override
					public void onFailure(HttpException error, String msg) {
//                    msgTextview.setText(error.getExceptionCode() + ":" + msg);
						Log.e("error",""+error);
						Toast.makeText(context, "上传失败"+msg, 1).show();
					}
				});

			}
		});

	}

}
