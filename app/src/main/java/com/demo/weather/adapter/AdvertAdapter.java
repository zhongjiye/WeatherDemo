package com.demo.weather.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.demo.weather.R;
import com.demo.weather.bean.Advert;
import com.demo.weather.bean.Weather;
import com.demo.weather.util.DateUtil;
import com.demo.weather.util.WeatherUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;

import java.util.ArrayList;

/**
 * Created by zhongjy on 2017/2/23.
 */

public class AdvertAdapter extends BaseAdapter {

    private ArrayList<Advert> list;
    private LayoutInflater inflater;
    private DisplayImageOptions options;
    private ImageLoader imageLoader;

    public AdvertAdapter(Context context, ArrayList<Advert> list) {
        this.list = list;
        inflater = LayoutInflater.from(context);
        imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .showStubImage(R.mipmap.icon)          // image在加载过程中，显示的图片
                .showImageForEmptyUri(R.mipmap.icon)  // empty URI时显示的图片
                .showImageOnFail(R.mipmap.icon)       // 不是图片文件 显示图片
                .resetViewBeforeLoading(false)  // default
                .delayBeforeLoading(1000)
                .cacheInMemory(true)           // default 不缓存至内存
                .cacheOnDisc(true)             // default 不缓存至手机SDCard
                .build();
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Advert getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        Advert advert = list.get(position);
        return advert.getType();
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);
        SingleAdvertViewHolder singleAdvertViewHolder = null;
        MultiAdvertViewHolder multiAdvertViewHolder = null;
        if (convertView == null) {
            switch (type) {
                case 0:
                    singleAdvertViewHolder = new SingleAdvertViewHolder();
                    convertView = inflater.inflate(R.layout.item_single_advert, null);
                    singleAdvertViewHolder.advertImg = (ImageView) convertView.findViewById(R.id.iv_advert_img);
                    singleAdvertViewHolder.advertContent = (TextView) convertView.findViewById(R.id.tv_advert_content);
                    convertView.setTag(singleAdvertViewHolder);
                    break;
                case 1:
                    multiAdvertViewHolder = new MultiAdvertViewHolder();
                    convertView = inflater.inflate(R.layout.item_multittem_advert, null);
                    multiAdvertViewHolder.advertImg1 = (ImageView) convertView.findViewById(R.id.iv_advert_img1);
                    multiAdvertViewHolder.advertImg2 = (ImageView) convertView.findViewById(R.id.iv_advert_img2);
                    multiAdvertViewHolder.advertImg3 = (ImageView) convertView.findViewById(R.id.iv_advert_img3);
                    multiAdvertViewHolder.advertContent = (TextView) convertView.findViewById(R.id.tv_advert_content);
                    convertView.setTag(multiAdvertViewHolder);
                    break;
            }
        } else {
            switch (type) {
                case 0:
                    singleAdvertViewHolder = (SingleAdvertViewHolder) convertView.getTag();
                    break;
                case 1:
                    multiAdvertViewHolder = (MultiAdvertViewHolder) convertView.getTag();
                    break;
            }
        }

        Advert advert = list.get(position);
        switch (type) {
            case 0:
                ImageAware imageAware = new ImageViewAware(singleAdvertViewHolder.advertImg, false);
                imageLoader.displayImage(advert.getImgUrlList().get(0), imageAware, options);
                singleAdvertViewHolder.advertContent.setText(advert.getContent());
                break;
            case 1:
                multiAdvertViewHolder.advertContent.setText(advert.getContent());
                ImageAware imageAware1 = new ImageViewAware(multiAdvertViewHolder.advertImg1, false);
                ImageAware imageAware2 = new ImageViewAware(multiAdvertViewHolder.advertImg2, false);
                ImageAware imageAware3 = new ImageViewAware(multiAdvertViewHolder.advertImg3, false);
                imageLoader.displayImage(advert.getImgUrlList().get(0), imageAware1, options);
                imageLoader.displayImage(advert.getImgUrlList().get(1), imageAware2, options);
                imageLoader.displayImage(advert.getImgUrlList().get(2), imageAware3, options);
                break;
        }
        return convertView;
    }

    private class SingleAdvertViewHolder {
        private TextView advertContent;
        private ImageView advertImg;
    }

    private class MultiAdvertViewHolder {
        private TextView advertContent;
        private ImageView advertImg1, advertImg2, advertImg3;
    }

}
