package com.shui.jing.xiao.you.ui.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.io.Serializable;

/**
 * Created by dengshengjin on 15/4/20.
 */
@Table(name = "t_zhanbu")
public class ZhanBuModel extends Model implements Serializable, Comparable<ZhanBuModel> {

    private static final long serialVersionUID = 2182318760380672963L;
    @Column(name = "zhanBuId")
    public long zhanBuId;

    @Column(name = "name")
    public String name;

    @Column(name = "desc")
    public String desc;

    @Column(name = "drawable")
    public String drawable;

    @Column(name = "createTime")
    public long createTime;

    @Column(name = "updateTime")
    public long updateTime;

    @Column(name = "type")
    public int type;//1 塔罗 2 水晶

    @Override
    public int compareTo(ZhanBuModel another) {
        return ((Long) createTime).compareTo((Long) another.createTime);
    }
}
