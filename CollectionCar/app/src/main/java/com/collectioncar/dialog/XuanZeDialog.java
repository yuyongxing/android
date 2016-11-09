package com.collectioncar.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.collectioncar.R;


/**
 * Created by Administrator on 2016/4/28.
 */
public class XuanZeDialog {
    Context context;
   // Dialogcallback dialogcallback;
    Dialog dialog;
    ImageView iv_icon;
    TextView paizhao,xiangce;
    TextView content,title;
    RelativeLayout quxiaore;
    Dialogcallback dialogcallback;
    public XuanZeDialog(Context con) {
        this.context = con;
        dialog = new Dialog(context, R.style.mydialog);
        dialog.setContentView(R.layout.xuanzelayout);

        Window dialogWindow = dialog.getWindow();
        dialogWindow.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = WindowManager.LayoutParams.FILL_PARENT;
        dialogWindow.setAttributes(lp);
        // WindowManager.LayoutParams lp = dialogWindow.getAttributes();

        dialogWindow.setGravity(Gravity.CENTER);

        dialog.setCanceledOnTouchOutside(true);
        iv_icon = (ImageView) dialog.findViewById(R.id.iv_icon);
        content = (TextView) dialog.findViewById(R.id.title2);
        quxiaore = (RelativeLayout) dialog.findViewById(R.id.quxiaore);
        title = (TextView) dialog.findViewById(R.id.title1);
        paizhao = (TextView)dialog.findViewById(R.id.paizhao);
        paizhao.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                dismiss();

                dialogcallback.paizhao();
            }
        });
        xiangce = (TextView)dialog.findViewById(R.id.xiangce);
        xiangce.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                dismiss();

                dialogcallback.tuku();
            }
        });
        quxiaore=(RelativeLayout)dialog.findViewById(R.id.quxiaore);
        quxiaore.setVisibility(View.VISIBLE);
        quxiaore.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                dismiss();


            }
        });
    }

    public void setIcon(int id){
        iv_icon.setBackgroundResource(id);
    }

    public void setContentText(String text){
        content.setText(text);
    }
    public void setTitleText(String text){
        title.setText(text);
    }
    public void setCancelInvisible(){
        quxiaore.setVisibility(View.INVISIBLE);
    }
    public void setLeftChoose(String text){
        paizhao.setText(text);
    }
    public void setRightChoose(String text){
        xiangce.setText(text);
    }
    public void setDialogCallback(Dialogcallback dialogcallback) {
        this.dialogcallback = dialogcallback;
    }
    public interface Dialogcallback {
        void paizhao();
        void tuku();
    }

    public void show() {

        dialog.show();

    }

    public void hide() {
        dialog.hide();
    }

    public void dismiss() {
        dialog.dismiss();
    }
}
