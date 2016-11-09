package com.collectioncar.util;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/*
 * 图像工具类
 */
public class ImageUtil {
	public static int displaywidth = 480;
	public static int displayheight = 800;
	public static int displaypixels = displaywidth * displayheight;


	public static Bitmap drawableToBitmap(Drawable drawable) {
		Config config;
		if (drawable.getOpacity() != PixelFormat.OPAQUE) {
			config = Config.ARGB_8888;
		} else {
			config = Config.RGB_565;
		}
		Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
				drawable.getIntrinsicHeight(), config);
		Canvas canvas = new Canvas(bitmap);
		// canvas.setBitmap(bitmap);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
				drawable.getIntrinsicHeight());
		drawable.draw(canvas);
		return bitmap;
	}



	public static Bitmap compressImageFromFile(String srcPath) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		newOpts.inJustDecodeBounds = true;//只读边,不读内容
		Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);

		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		float hh = 800f;//
		float ww = 480f;//
		int be = 1;
		if (w > h && w > ww) {
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;//设置采样率

		newOpts.inInputShareable = true;//。当系统内存不够时候图片自动被回收

		try {
			bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		} catch (OutOfMemoryError e) {
			// TODO: handle exception
		}


		return bitmap;
	}

	private Bitmap compressBmpFromBmp(Bitmap image) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int options = 100;
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		while (baos.toByteArray().length / 1024 > 100) {
			baos.reset();
			options -= 10;
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);
		return bitmap;
	}


	public static Bitmap getBitmapFromFile(String dst, int width, int height) {
		if (dst !=null && !dst.equals("")) {
			BitmapFactory.Options opts = null;
			if (width > 0 && height > 0) {
				opts = new BitmapFactory.Options();
				opts.inJustDecodeBounds = true;
				BitmapFactory.decodeFile(dst, opts);

				int max = Math.max(opts.outHeight, opts.outWidth);
				if(max<3000){
					opts.inSampleSize=1;
				}else{
					// 计算图片缩放比例
					final int minSideLength = Math.min(width, height);
					opts.inSampleSize = computeSampleSize(opts, minSideLength,
							width * height/2);
				}

				opts.inJustDecodeBounds = false;
				opts.inInputShareable = true;
				opts.inPurgeable = true;
			}
			try {

				return BitmapFactory.decodeFile(dst, opts);
			} catch (OutOfMemoryError e) {

				e.printStackTrace();
			}
		/*	 BitmapFactory.Options opts = new BitmapFactory.Options();
			 opts.inJustDecodeBounds = true;
			 opts.inPreferredConfig = Bitmap.Config.RGB_565;
			 Bitmap bitmap = BitmapFactory.decodeFile(dst, opts);
			 int picHeight = opts.outHeight;
			 int picWidth = opts.outWidth;


			 //http://blog.csdn.net/xiaoyaovsxin/article/details/8446698计算图片占用的内存 不能超过8*1024
			 int picsize=  ((picHeight)*(picWidth)*16)/(8*1024);
			 //isSampleSize是表示对图片的缩放程度，比如值为2图片的宽度和高度都变为以前的1/2
			 Log.e("tupianchucun", "bytecount:" +"picsize:"+picsize);  ;

			 opts.inSampleSize = 1;
			 //根据占用内存来进行缩放
			 if(picsize>2*1024){
				 opts.inSampleSize=(int)(picsize/(2*1024));
			 }
			 opts.inJustDecodeBounds = false;
			 bitmap = BitmapFactory.decodeFile(dst, opts);
			 return bitmap;*/

		}
		return null;
	}

	/**
	 * 得到 图片旋转 的角度
	 *
	 * @param filepath
	 * @return
	 */

	//http://my.oschina.net/techstan/blog/142592
	public static int getExifOrientation(String filepath) {
		int degree = 0;
		ExifInterface exif = null;
		try {
			exif = new ExifInterface(filepath);
		} catch (IOException ex) {
			Log.e("test", "cannot read exif", ex);
		}
		if (exif != null) {
			int orientation = exif.getAttributeInt(
					ExifInterface.TAG_ORIENTATION, -1);
			if (orientation != -1) {
				switch (orientation) {
					case ExifInterface.ORIENTATION_ROTATE_90:
						degree = 90;
						break;
					case ExifInterface.ORIENTATION_ROTATE_180:
						degree = 180;
						break;
					case ExifInterface.ORIENTATION_ROTATE_270:
						degree = 270;
						break;
				}
			}
		}
		return degree;
	}


	/****
	 * 处理图片bitmap size exceeds VM budget （Out Of Memory 内存溢出）
	 */
	public static int computeSampleSize(BitmapFactory.Options options,
										int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength,
				maxNumOfPixels);
		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}
		return roundedSize;
	}

	public static int computeInitialSampleSize(BitmapFactory.Options options,
											   int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;
		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
				.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
				Math.floor(w / minSideLength), Math.floor(h / minSideLength));
		if (upperBound < lowerBound) {
			return lowerBound;
		}
		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}
	private static byte[] getBytesOfBitMap(String imgUrl){
		try {
			URL url = new URL(imgUrl);
			HttpURLConnection conn=(HttpURLConnection)url.openConnection();
			//设置超时时间为6000毫秒，conn.setConnectionTiem(0);表示没有时间限制
			conn.setConnectTimeout(6000);
			//连接设置获得数据流
			conn.setDoInput(true);
			//不使用缓存
			conn.setUseCaches(false);
			InputStream in = conn.getInputStream();
			return readStream(in);
		} catch (IOException e) {
			Log.e("error",""+e);
		} catch (Exception e) {
			Log.e("error",""+e);
			e.printStackTrace();
		}
		return null;
	}
	private static byte[] readStream(InputStream inStream) throws Exception{
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[2048];
		int len = 0;
		while( (len=inStream.read(buffer)) != -1){
			outStream.write(buffer, 0, len);
		}
		outStream.close();
		inStream.close();
		return outStream.toByteArray();
	}
	public static Bitmap loadTypeURL(String imagePath)  {
		//从网络下载图片
		byte[] datas = getBytesOfBitMap(imagePath);
		if(datas != null){

			BitmapFactory.Options opts = new BitmapFactory.Options();

			opts.inJustDecodeBounds = true;
			Bitmap bitmap = BitmapFactory.decodeByteArray(datas, 0, datas.length, opts);

			// int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

			final int height = opts.outHeight;
			final int width = opts.outWidth;
			int inSampleSize = 1;
			//图片源和实际图片的高度宽度对比

			if (width > height && width > 100) {//如果宽度大的话根据宽度固定大小缩放
				inSampleSize = opts.outWidth / 100;
			} else if (width < height && height > 100) {//如果高度高的话根据宽度固定大小缩放
				inSampleSize = opts.outHeight / 100;
			}
			if (inSampleSize <= 0)
				inSampleSize = 1;

			opts.inSampleSize =inSampleSize;



			opts.inJustDecodeBounds = false;
			opts.inInputShareable = true;
			opts.inPurgeable = true;
			try {
				bitmap = BitmapFactory.decodeByteArray(datas, 0, datas.length);
			} catch (OutOfMemoryError e) {
				// TODO: handle exception
			}


			return bitmap;
		}  else{
			return null;
		}

	}


	public static Drawable getDrawable(String url) {
		Bitmap bmp = null;
		try {
			bmp = getBitmap(url, displaypixels, true);
		} catch (MalformedURLException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
		if (bmp == null) {
			return null;
		}
		Drawable drawable = new BitmapDrawable(bmp);
		return drawable;
	}
	/**
	 * 通过URL获得网上图片。如:http://www.xxxxxx.com/xx.jpg
	 * */
	public static Bitmap getBitmap(String url, int displaypixels, Boolean isBig)
			throws IOException {
		Bitmap bmp = null;
		BitmapFactory.Options opts = new BitmapFactory.Options();
		InputStream stream = new URL(url).openStream();
		byte[] bytes = getBytes(stream);
		// 这3句是处理图片溢出的begin( 如果不需要处理溢出直接 opts.inSampleSize=1;)
		opts.inJustDecodeBounds = true;
		BitmapFactory.decodeByteArray(bytes, 0, bytes.length, opts);
		opts.inSampleSize = computeSampleSize(opts, -1, displaypixels);
		// end
		opts.inJustDecodeBounds = false;
		bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, opts);

		//判断照片本身的横竖，对照片进行选择操作
		int angle= ImageUtil.getExifOrientation(url);
		if(angle!=0){
			//如果照片出现了 旋转 那么 就更改旋转度数
			Matrix matrix = new Matrix();
			matrix.postRotate(angle);
			bmp = Bitmap.createBitmap(bmp,
					0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
		}
		return bmp;
	}
	/**
	 * 数据流转成btyle[]数组
	 * */
	public static byte[] getBytes(InputStream is) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] b = new byte[2048];
		int len = 0;
		try {
			while ((len = is.read(b, 0, 2048)) != -1) {
				baos.write(b, 0, len);
				baos.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] bytes = baos.toByteArray();
		return bytes;
	}
}
