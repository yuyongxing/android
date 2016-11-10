package com.collectioncar.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.aspsine.irecyclerview.universaladapter.ViewHolderHelper;
import com.aspsine.irecyclerview.universaladapter.recyclerview.MultiItemRecycleViewAdapter;
import com.aspsine.irecyclerview.universaladapter.recyclerview.MultiItemTypeSupport;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.collectioncar.R;
import com.collectioncar.model.CarListModel;
import com.jaydenxiao.common.imagePager.BigImagePagerActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/21.
 */

public class CheShangQuanAdapter  extends MultiItemRecycleViewAdapter<CarListModel> {
   private Activity act;

    public Activity getAct() {
        return act;
    }

    public void setAct(Activity act) {
        this.act = act;
    }

    public CheShangQuanAdapter(Context context, final List<CarListModel> datas)
    {
        super(context, datas, new MultiItemTypeSupport<CarListModel>()
        {

            @Override
            public int getLayoutId(int type) {

                    return R.layout.cheshangquan;

            }

            @Override
            public int getItemViewType(int position, CarListModel carListModel) {
                return 0;
            }


        });
    }
    @Override
    public void convert(ViewHolderHelper holder, CarListModel carListModel) {
        setItemValues(holder, carListModel,getPosition(holder));
    }
    private void setItemValues(final ViewHolderHelper holder, final CarListModel carListModel, final int position) {
        String title = carListModel.getTitle();
        if(title!=null){
            holder.setText(R.id.description,title);
        }else{
            holder.setText(R.id.description,"");
        }

        final String []SmallPicList= carListModel.getSmallPicList();
        /*if(SmallPicList!=null&&SmallPicList.length>0){
            setpicvalues(holder,SmallPicList,position);
        }else{
            holder.getView(R.id.piclin).setVisibility(View.GONE);

        }*/
        if(SmallPicList!=null&&SmallPicList.length>0){
            holder.setSmallImageUrl(R.id.cheshangtouxiang,SmallPicList[0]);
        }else{
            holder.setImageResource(R.id.cheshangtouxiang,R.mipmap.touxiang);
        }
        Log.e("piclist",""+SmallPicList.length+"position:"+position);

     if(SmallPicList!=null&&SmallPicList.length==1){
         holder.setVisible(R.id.onepic,true);
         holder.setVisible(R.id.mygridview,false);

        // holder.getView(R.id.onepic).setVisibility(View.VISIBLE);
         holder.setImageUrl(R.id.onepic,SmallPicList[0]);
     }else if(SmallPicList!=null&&SmallPicList.length>1){
         holder.setVisible(R.id.onepic,false);
         holder.setVisible(R.id.mygridview,true);
         GirdViewAdapter gridViewAdapter=new GirdViewAdapter(mContext);
         gridViewAdapter.add(SmallPicList);
         (( com.collectioncar.view.MyGridView)holder.getView(R.id.mygridview)).setAdapter(gridViewAdapter);
     }else{
         holder.setVisible(R.id.onepic,false);
         holder.setVisible(R.id.mygridview,false);
     }
        holder.getView(R.id.onepic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BigImagePagerActivity.startImagePagerActivity(act,SmallPicList[0],position);
            }
        });
        holder.getView(R.id.cheshangtouxiang).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(SmallPicList!=null&&SmallPicList.length>0){
                    BigImagePagerActivity.startImagePagerActivity(act,SmallPicList[0],position);
                }

            }
        });


    }


    public class GirdViewAdapter extends BaseAdapter {
        //适配器
        public final class GirdViewAViewHolder {
            public ImageView img;
        }
        private LayoutInflater mInflater;

        ArrayList piclist=new ArrayList();
        public void removeall(){
            piclist.clear();
           // notifyDataSetChanged();
        }
        public void add(String []SmallPicList){
            for(int i=0;i<SmallPicList.length;i++){
                piclist.add(SmallPicList[i]);
            }

            //notifyDataSetChanged();
        }
        public GirdViewAdapter(Context context) {
            this.mInflater = LayoutInflater.from(context);

        }


        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            if (piclist != null && piclist.size() > 0) {

                return piclist.size();
            } else {

                return 0;
            }


        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return arg0;
        }

        @Override
        public long getItemId(int arg0) {
            // TODO Auto-generated method stub
            return arg0;
        }


        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // 显示优化（只要之前显示过的就可以不再再次从布局文件读取，直接从缓存中读取——ViewHolder的作用）
            // 其实是setTag和getTag中Tag的作用

            GirdViewAViewHolder holder = null;
            if (convertView == null) {// 如果是第一次显示该页面(要记得保存到viewholder中供下次直接从缓存中调用)

                holder = new GirdViewAViewHolder();

                convertView = mInflater.inflate(R.layout.cheshangquanpicitem, null);
                // 以下为保存这一屏的内容，供下次回到这一屏的时候直接refresh，而不用重读布局文件

                holder.img = (ImageView) convertView.findViewById(R.id.img);

                convertView.setTag(holder);

            } else {// 如果之前已经显示过该页面，则用viewholder中的缓存直接刷屏

                holder = (GirdViewAViewHolder) convertView.getTag();
            }

          //  holder.img.set(xuqiulist.get(position));
         /*   Glide.with(CheShangQuanAdapter.this.mContext).load(piclist.get(position))
                    .load(piclist.get(position))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.img);*/

                      Glide.with(holder.img.getContext()).load(piclist.get(position)).asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(com.jaydenxiao.common.R.drawable.ic_image_loading)
                    .error(com.jaydenxiao.common.R.drawable.ic_empty_picture)
                    .override(100,100)
                    .into(holder.img);


            holder.img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    BigImagePagerActivity.startImagePagerActivity(act,piclist,position);
                }
            });


            return convertView;
        }
    }
}
