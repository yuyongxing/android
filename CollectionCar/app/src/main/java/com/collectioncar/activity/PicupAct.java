package com.collectioncar.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.collectioncar.R;
import com.collectioncar.URL.HxServiceUrl;
import com.collectioncar.baseapp.BaseAct;
import com.collectioncar.dialog.XuanZeDialog;
import com.collectioncar.picup.ImageUpFile;
import com.collectioncar.util.PicupUtil;
import com.yanzhenjie.permission.AndPermission;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/10/25.
 */

public class PicupAct extends BaseAct {

    PicupUtil picupUtil = new PicupUtil(PicupAct.this);
    ImageUpFile imageupfile;
    ImageUpFile.AddUrl addurl = new ImageUpFile.AddUrl() {

        @Override
        public void addurl(String filePath, String relativePath) {

            relativePath = relativePath.substring(1, relativePath.length() - 1);
            Log.e("relativePath", "" + relativePath);

        }
    };


    //ImageUpFile imgupfile=new ImageUpFile(PicupAct.this,addurl);
    XuanZeDialog xuanZeDialog;
    XuanZeDialog.Dialogcallback dialogcallback = new XuanZeDialog.Dialogcallback() {

        @Override
        public void paizhao() {

            String status = Environment
                    .getExternalStorageState();
            if (status
                    .equals(Environment.MEDIA_MOUNTED)) {// 判断是否有SD卡
                picupUtil.doTakePhoto(PicupAct.this);
            } else {
                Toast.makeText(PicupAct.this, "没有SD卡",
                        Toast.LENGTH_LONG).show();
            }
        }


        @Override
        public void tuku() {

            if(Build.VERSION.SDK_INT >= 23){
                AndPermission.with(PicupAct.this)
                        .requestCode(1001)
                        .permission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .send();
            }else {
                picupUtil.doPickPhotoFromGallery(PicupAct.this);// 从相册中去获取
            }

        }
    };
    @Bind(R.id.dianjishangchuan)
    TextView dianjishangchuan;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.picup_xml);
        ButterKnife.bind(this);
        imageupfile = new ImageUpFile(PicupAct.this, addurl);
        xuanZeDialog = new XuanZeDialog(PicupAct.this);
        xuanZeDialog.setDialogCallback(dialogcallback);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            try {
                String userSelectPath = picupUtil.PHOTO_DIR.getPath() + "/" + picupUtil.picName;

           /*     imgupfile.uploadImg(picuplist, "http://upload.hx2car.com/mobile/upload.htm");
                picuplist=null;*/
                imageupfile.uploadMethod(HxServiceUrl.uploadHost, userSelectPath, null);
            } catch (Exception e) {
                System.out.println("CAMERA_WITH_DATA:e=" + e.getMessage());
            }
        } else if (requestCode == picupUtil.PHOTO_PICKED_WITH_DATA) {
            try {

                Bundle b=data.getExtras();
                ArrayList<String> selectedItems =b.getStringArrayList("fileurls");

                if(selectedItems!=null){
                    for(int i=0;i<selectedItems.size();i++){
                        String fu=selectedItems.get(i);
                        if(fu!=null){

                          /*  if (!imageAdapter2.containsUrl(fu)) {
                                imageAdapter2.add(fu);
                                tupianshangchuan2 = true;
                                if(	rl_gallerylayout.getVisibility()== View.GONE){
                                    rl_gallerylayout.setVisibility(View.VISIBLE);
                                    gif2.setVisibility(View.VISIBLE);
                                }*/

                                imageupfile.uploadMethod(HxServiceUrl.uploadHost, fu, null);
                            }
//							}

                        }
                    }
                    Toast.makeText(PicupAct.this, "已选择 "+(selectedItems.size())+" 张图片", Toast.LENGTH_SHORT).show();



            } catch (Exception e) {
                System.out.println("CAMERA_WITH_DATA:e=" + e.getMessage());
            }
        }

    }

    @Override
    public void onStart() {
        super.onStart();


    }

    @Override
    public void onStop() {
        super.onStop();


    }

    @OnClick(R.id.dianjishangchuan)
    public void onClick() {
        xuanZeDialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent intent = new Intent();
        intent.putExtra("filename", picupUtil.picdown + "PICJIETU");
        intent.setAction("android.intent.action.delete");
        PicupAct.this.sendBroadcast(intent);
    }
}
