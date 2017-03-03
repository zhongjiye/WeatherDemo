package com.demo.weather.bean;

import java.util.List;

/**
 * Created by zhongjy on 2017/2/27.
 */

public class Advert implements Cloneable {

    private String content;
    private List<String> imgUrlList;
    private int type;

    public Advert(String content, List<String> imgUrlList, int type) {
        this.content = content;
        this.imgUrlList = imgUrlList;
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<String> getImgUrlList() {
        return imgUrlList;
    }

    public void setImgUrlList(List<String> imgUrlList) {
        this.imgUrlList = imgUrlList;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
