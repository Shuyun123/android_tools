package com.example.anumbrella.viewpager;

/**
 * Created by anumbrella on 15/10/5.
 * <p/>
 * 头像适配器接口
 * <p/>
 * 来源于Jake Wharton,查看源码,覆写并添加中文注释
 */
public interface IconPagerAdapter {

    /**
     *
     * 获取图片页面在适配器中的资源id地址
     * @param index
     * @return
     */
    int getIconResId(int index);


    /**
     * 数据总数
     *
     * @return
     */
    int getCount();
}
