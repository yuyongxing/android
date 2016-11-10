package com.collectioncar.framgent;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.collectioncar.R;
import com.collectioncar.activity.PicupAct;
import com.collectioncar.baseapp.BaseFragment;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/10/20.
 */

public class PhotosMainFragment extends BaseFragment {
    @Bind(R.id.title)
    RelativeLayout title;
    @Bind(R.id.imgup)
    ImageView imgup;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.frament_picup, null);


        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.title, R.id.imgup})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title:
                break;
            case R.id.imgup:
                Intent intent=new Intent();
                intent.setClass(getActivity(),PicupAct.class);
                startActivity(intent);
                break;
        }
    }
}
