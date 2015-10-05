/*
 * Copyright (C) 2011 The Android Open Source Project
 * Copyright (C) 2011 Jake Wharton
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
