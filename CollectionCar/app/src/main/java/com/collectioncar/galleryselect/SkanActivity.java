package com.collectioncar.galleryselect;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.GridView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.collectioncar.R;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class SkanActivity extends Activity {
	private GridView mGridView;
	private ArrayList<String> lists;
	private LayoutInflater inflate;
	private int viewWidth=0,viewHeight=0;
	private MyAdapter adapter;
	private ArrayList<String> strs=new ArrayList<String>();
	
	private TextView back;
	private Button submit;
	private int position;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_image_activity);
		lists=(ArrayList<String>) getIntent().getExtras().get("data");
		Collections.reverse(lists);
		initView();

	}
	private void initView() {
		back=(TextView)findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
			finish();	
			}
		});
		submit=(Button)findViewById(R.id.submit);
		submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent=new Intent();
				intent.putExtra("data", strs);
				setResult(200,intent);
				finish();

			}
		});
		mGridView=(GridView) findViewById(R.id.child_grid);
		inflate=LayoutInflater.from(SkanActivity.this);
		adapter=new MyAdapter();
		mGridView.setAdapter(adapter);
	}
	
/*	@Override
	public void onBackPressed() {
		Intent intent=new Intent();
		intent.putExtra("data", strs);
		setResult(200,intent);
		finish();
		super.onBackPressed();
	}*/
	private class MyAdapter extends BaseAdapter{
		  HashMap<Integer,Integer> map = new HashMap();
		@Override
		public int getCount() {
			return lists.size();
		}
		@Override
		public Object getItem(int position) {
			return lists.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			final String path=lists.get(position);
			ViewHolder viewHolder=null;
			if(convertView==null){
				viewHolder=new ViewHolder();
				convertView=inflate.inflate(R.layout.grid_child_item, null);
				viewHolder.image=(MyImageView) convertView.findViewById(R.id.child_image);
				viewHolder.check=(CheckBox) convertView.findViewById(R.id.child_checkbox);
				convertView.setTag(viewHolder);
				viewHolder.image.setOnMeasureListener(new MyImageView.OnMeasureListener() {
					@Override
					public void onMeasureSize(int width, int height) {
						viewWidth = width;
						viewHeight = height;
					}
				});
			}else{
				viewHolder=(ViewHolder) convertView.getTag();
				viewHolder.image.setImageResource(R.mipmap.friends_sends_pictures_no);
			}
	/*		viewHolder.image.setTag(path);
			Bitmap bitmap=ImageLoader.getInstance().loadImage(path, viewWidth, viewHeight, new OnCallBackListener() {
				@Override
				public void setOnCallBackListener(Bitmap bitmap, String url) {
					ImageView image=(ImageView) mGridView.findViewWithTag(url);
					if(image!=null&&bitmap!=null){
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

						image.setImageBitmap(bitmap);
					}
				}
			});
			if(bitmap!=null){
				viewHolder.image.setImageBitmap(bitmap);
			}else{
				viewHolder.image.setImageResource(R.drawable.friends_sends_pictures_no);
			}*/
			/*imageloadmanagerwusd.setImageView(imageLoadManagerwusdcard.IMAGE_LOAD_TYPE.FILE_PATH, path,
					viewHolder.image, 800, 600);*/

			Glide
					.with(SkanActivity.this)
					.load(path)
					.diskCacheStrategy(DiskCacheStrategy.ALL)
					.into(viewHolder.image);
			viewHolder.check.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if (isChecked) {
						if (!strs.contains(path)) {
							strs.add(path);
							map.put(position, position);
						}

					} else {
						if (strs.contains(path)) {
							strs.remove(path);
							map.remove(position);
						}

					}
				}
			});
		/*	final ViewHolder finalViewHolder = viewHolder;
			viewHolder.image.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if(finalViewHolder.check.isChecked()){
						finalViewHolder.check.setChecked(false);
						if (strs.contains(path)) {
							strs.remove(path);
							map.remove(position);
						}
					}else{
						finalViewHolder.check.setChecked(true);
						if (!strs.contains(path)) {
							strs.add(path);
							map.put(position, position);
						}
					}
				}
			});*/
			if(map.containsKey(position)){
				viewHolder.check.setChecked(true);
			
			}else{
				viewHolder.check.setChecked(false);
				
			}
			return convertView;
			
		}		
	}
	private static class ViewHolder{
		public MyImageView image;
		public CheckBox check;
	}
	
	
}
