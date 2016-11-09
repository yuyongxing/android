package com.collectioncar.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.collectioncar.R;


/**
 * Created by Administrator on 2016/7/29.
 */
public class HorizontalProgressBarDiglog {
    Context context;
    Dialog dialog;
    HorizontalProgressBar progressBarWithNumber;
    public HorizontalProgressBarDiglog(Context con) {
        this.context = con;

        dialog = new Dialog(context, R.style.mydialog);
        dialog.setContentView(R.layout.horiprogress_xml);

        Window dialogWindow = dialog.getWindow();
        dialogWindow.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = WindowManager.LayoutParams.FILL_PARENT;
        dialogWindow.setAttributes(lp);
        // WindowManager.LayoutParams lp = dialogWindow.getAttributes();

        dialogWindow.setGravity(Gravity.CENTER);

        dialog.setCanceledOnTouchOutside(false);
        //   loadingtext = (TextView) dialog.findViewById(R.id.loadingtext);
        progressBarWithNumber=(HorizontalProgressBar) dialog.findViewById(R.id.id_progressbar);

    }
    public void setProgress(int i) {
        progressBarWithNumber.setProgress(i);
    }
    public void dismiss() {
        dialog.dismiss();
    }
    public void show() {
        dialog.show();
    }

}
