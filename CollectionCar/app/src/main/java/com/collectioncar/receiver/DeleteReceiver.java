package com.collectioncar.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import java.io.File;

/**
 * Created by Administrator on 2016/6/30.
 */
public class DeleteReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String newDir2  = intent.getStringExtra("filename");
        if(newDir2!=null){
            File file= new File(newDir2);
            boolean success = deleteDir(file);
            if(success){
                Uri localUri = Uri.fromFile(file);
                Intent localIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, localUri);
                context.sendBroadcast(localIntent);
            }
            Toast.makeText(context,"删除成功"+success,Toast.LENGTH_SHORT).show();
        }

    }
    private  boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            //递归删除目录中的子目录下
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }
}
