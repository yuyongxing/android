// (c)2016 Flipboard Inc, All Rights Reserved.

package com.collectioncar.model;

import java.io.Serializable;

public class CarListModel implements Serializable{
    public String title;

    private String id;
    private String []smallPicList;
    private String[] bigPicList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String[] getBigPicList() {
        return bigPicList;
    }

    public void setBigPicList(String[] bigPicList) {
        this.bigPicList = bigPicList;
    }

    public String[] getSmallPicList() {
        return smallPicList;
    }

    public void setSmallPicList(String[] smallPicList) {
        this.smallPicList = smallPicList;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
