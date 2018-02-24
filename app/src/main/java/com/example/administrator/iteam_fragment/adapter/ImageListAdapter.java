package com.example.administrator.iteam_fragment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.administrator.iteam_fragment.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jarvis on 04/11/2017.
 */

@SuppressWarnings("ALL")
public class ImageListAdapter extends BaseAdapter {

    private Context context;
    private List<String> urls = new ArrayList<>();

    public ImageListAdapter(Context context, List<String> urls) {
        this.context = context;
        this.urls = urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

    @Override
    public int getCount() {
        return urls.size();
    }

    @Override
    public String getItem(int i) {
        return urls.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder vh = null;
        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.pic_item,null);
            vh = new ViewHolder();
            vh.imageView = (ImageView) view.findViewById(R.id.pic_Item);
            view.setTag(vh);
        }
        vh = (ViewHolder) view.getTag();
        if(urls!=null && urls.size()>0){
            Glide.with(context).load(urls.get(i)).centerCrop().into(vh.imageView);
        }
        return view;
    }

    class ViewHolder{
        ImageView imageView;
    }
}