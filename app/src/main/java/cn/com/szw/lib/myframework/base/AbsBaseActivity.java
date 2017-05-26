package cn.com.szw.lib.myframework.base;

import android.os.Bundle;

/**
 * Created by 史忠文
 * on 2017/3/20.
 */

interface AbsBaseActivity {
    /**
     * 视图，组件,数据的初始化
     */
    void init() throws Exception;
    void init(Bundle savedInstanceState);
    /**
     * 初始化电量条和状态栏
     */
    void initBar();
    boolean initToolbar();
    /**
     * 设置布局id
     */
    int setInflateId();
}
