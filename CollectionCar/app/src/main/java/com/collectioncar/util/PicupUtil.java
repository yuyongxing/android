package com.collectioncar.util;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

import com.collectioncar.galleryselect.XiangCeActivity;
import com.collectioncar.picup.ImageUpFile;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2016/10/25.
 */

public class PicupUtil {
    public Context context;
    public  static String picdown = Environment
            .getExternalStorageDirectory()
            + File.separator
            + "collectioncar"
            + File.separator;
    /* 拍照的照片存储位置 */
    public static final File PHOTO_DIR = new File(picdown
            + "PICJIETU");
    public String picName = "";// 用户拍照后保存的图片名称
    /* 用来标识请求照相功能的activity */
    public int CAMERA_WITH_DATA = 3023;
    /* 用来标识请求gallery的activity */
    public  int PHOTO_PICKED_WITH_DATA = 3021;
    public PicupUtil(Context context) {
        this.context=context;

    }
    public String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(date) + ".png";
    }

    public void doTakePhoto(Activity act) {
        try {
            PHOTO_DIR.mkdirs();// 创建照片的存储目录
            Intent imageCaptureIntent = new Intent(
                    MediaStore.ACTION_IMAGE_CAPTURE);
            // 重新分解图片名称将-:的符号去掉
            picName = getPhotoFileName();
            picName = picName.replace("-", "");
            picName = picName.replace(":", "");
            File out = new File(PHOTO_DIR, picName);
            Uri uri = Uri.fromFile(out);
            imageCaptureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            imageCaptureIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
            // startActivityForResult(imageCaptureIntent, CAMERA_WITH_DATA);

            // 把一个Activity转换成一个View
            // getParent().startActivityForResult(imageCaptureIntent,CAMERA_WITH_DATA);
            act.startActivityForResult(imageCaptureIntent, CAMERA_WITH_DATA);
            // View view = w.getDecorView();
            // 把View添加大ActivityGroup中
            // GroupSurveyMain.group.setContentView(view);

        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "doTakePhoto：e=" + e, Toast.LENGTH_LONG)
                    .show();
        }
    }
    // 请求Gallery程序
    public void doPickPhotoFromGallery(Activity act) {
        try {
            //Intent intent = new Intent(FaCheActivity.this, FaBuMultiPhotoSelectActivity.class);
            Intent intent = new Intent(context, XiangCeActivity.class);
            String name = "Seed";
            intent.putExtra("Name", name);
            act.startActivityForResult(intent, PHOTO_PICKED_WITH_DATA);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "无法打开相册", Toast.LENGTH_LONG).show();
        }
    }
}
