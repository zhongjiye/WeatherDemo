package com.demo.weather.adapter;

import com.demo.weather.R;
import com.demo.weather.cusview.flowlayout.FlowLayout;
import com.demo.weather.cusview.flowlayout.TagAdapter;
import com.demo.weather.cusview.flowlayout.TagFlowLayout;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 选择国家适配器
 */
public class CountryAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<String> indexList;
    private Map<String, List<String>> countryMap;
    private OnTagClickListener mOnTagClickListener;


    public CountryAdapter(Context context, List<String> indexList, Map<String, List<String>>
        countryMap, OnTagClickListener tagClickListener) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mOnTagClickListener = tagClickListener;
        this.indexList = indexList;
        this.countryMap = countryMap;
    }


    @Override
    public int getCount() {
        return countryMap.size();
    }

    @Override
    public Object getItem(int position) {
        return countryMap.get(indexList.get(position));
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        MyViewHolder myViewHolder;
        if (convertView == null) {
            myViewHolder = new MyViewHolder();
            convertView = mInflater.inflate(R.layout.item_select_country, null);
            myViewHolder.countryIndexTxt = (TextView) convertView.findViewById(R.id.tv_country_index);
            myViewHolder.flowLayout = (TagFlowLayout) convertView.findViewById(R.id.cus_flowlayout);
            convertView.setTag(myViewHolder);
        } else {
            myViewHolder = (MyViewHolder) convertView.getTag();
        }
        String countryIndex = indexList.get(position);
        myViewHolder.countryIndexTxt.setText(countryIndex.toUpperCase());
        final List<String> list = countryMap.get(countryIndex);
        TagAdapter tagAdapter = new TagAdapter(list) {
            @Override
            public View getView(FlowLayout parent, int position, Object o) {
                TextView tv = (TextView) mInflater.inflate(R.layout.tv, parent, false);
                tv.setText(o.toString());
                return tv;
            }
        };
        myViewHolder.flowLayout.setAdapter(tagAdapter);
        myViewHolder.flowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                if (mOnTagClickListener != null) {
                    mOnTagClickListener.onTagClik(((TextView) view).getText().toString());
                }
                return false;
            }
        });
        tagAdapter.notifyDataChanged();


        return convertView;
    }

    private class MyViewHolder {
        private TextView countryIndexTxt;
        private TagFlowLayout flowLayout;
    }

    public interface OnTagClickListener {
        void onTagClik(String name);
    }

}
