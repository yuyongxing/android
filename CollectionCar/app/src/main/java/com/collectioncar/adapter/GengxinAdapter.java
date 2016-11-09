package com.collectioncar.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.collectioncar.R;

import java.util.List;

/**
 * Created by Administrator on 2016/11/8.
 */

public class GengxinAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private List<String> listpaixu;




    public GengxinAdapter(Context context, List<String> list) {
        this.inflater = LayoutInflater.from(context);
        listpaixu = list;
    }




    @Override
    public int getCount() {
        return listpaixu.size();
    }



    @Override
    public Object getItem(int position) {
        return listpaixu.get(position);
    }



    @Override
    public long getItemId(int position) {
        return position;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.car_gengxin_list_item,
                    null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.alpha_paixu);//名字
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String paixu = listpaixu.get(position);
        holder.name.setText(paixu);

        return convertView;
    }

    private class ViewHolder {
        TextView name;
    }




}
