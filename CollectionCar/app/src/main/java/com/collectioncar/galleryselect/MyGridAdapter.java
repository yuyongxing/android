package com.collectioncar.galleryselect;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.collectioncar.R;


import java.util.List;

public class MyGridAdapter extends BaseAdapter {
	private List<ImageBean> beans;
	private GridView mGridView;
	int width=0,height=0;
	LayoutInflater inflater;
	Context c;
	public MyGridAdapter(Context context,List<ImageBean> beans,GridView gridView) {
		this.beans = beans;
		this.mGridView=gridView;
		inflater=LayoutInflater.from(context);
		c=context;
	}

	@Override
	public int getCount() {
		return beans.size();
	}

	@Override
	public Object getItem(int position) {
		return beans.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageBean bean=beans.get(position);
		String path=bean.getTopImagePath();
		final ViewHolder viewHolder;
		if(convertView==null){
			viewHolder=new ViewHolder();
			convertView=inflater.inflate(R.layout.item_gridview, null);
			viewHolder.image=(MyImageView) convertView.findViewById(R.id.group_image);
			viewHolder.tv1=(TextView) convertView.findViewById(R.id.group_count);
			viewHolder.tv2=(TextView) convertView.findViewById(R.id.group_title);
			viewHolder.image.setOnMeasureListener(new MyImageView.OnMeasureListener() {
				@Override  
                public void onMeasureSize(int width, int height) {  
					MyGridAdapter.this.width=width; 
					MyGridAdapter.this.height=height;
                }  
            });  
			convertView.setTag(viewHolder);
		}else{
			viewHolder=(ViewHolder) convertView.getTag();
			viewHolder.image.setImageResource(R.mipmap.friends_sends_pictures_no);
		}	
		
	/*	viewHolder.image.setTag(path);

		Bitmap bitmap=ImageLoader.getInstance().loadImage(path, width,height, new OnCallBackListener() {

			@Override
			public void setOnCallBackListener(Bitmap bitmap, String path) {
				ImageView mImageView = (ImageView) mGridView.findViewWithTag(path);
				if(bitmap != null && mImageView != null){
					if(bitmap!=null){
				      	int  src_w = bitmap.getWidth();
						  int  src_h = bitmap.getHeight();
						  if(src_w>src_h){
							  try {
								bitmap = Bitmap.createBitmap(bitmap, (src_w-src_h)/2, 0,src_h , src_h); 
						} catch (OutOfMemoryError e) {
							
						}
						
						  }else if(src_h>src_w){
							  try {
								bitmap = Bitmap.createBitmap(bitmap, 0, (src_h-src_w)/2,src_w, src_w); 
							} catch (OutOfMemoryError e) {
								
							}
						
					  }
					}
					mImageView.setImageBitmap(bitmap);
				}
			}
		});
		if(bitmap!=null){
			viewHolder.image.setImageBitmap(bitmap);
		}else{
			viewHolder.image.setImageResource(R.drawable.friends_sends_pictures_no);
		}*/
		Glide
				.with(c)
				.load(path)
				.override(400, 300)
				.diskCacheStrategy(DiskCacheStrategy.ALL)
			.into(viewHolder.image);
/*		Picasso.with(c)
				.load(path)
				.fit()
				.centerCrop()
				.into(viewHolder.image);*/
		viewHolder.tv1.setText(bean.getImageCounts()+"");
		viewHolder.tv2.setText(bean.getFolderName());
		return convertView;
	}
	public static class ViewHolder{
		public MyImageView image;
		public TextView tv1,tv2;
	}
}
