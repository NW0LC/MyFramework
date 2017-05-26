package cn.com.szw.lib.myframework.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class ScrollViewPager extends ViewPager {
    private static boolean isCanScroll = true;

    public ScrollViewPager(Context context) {
        super(context);
    }

    public ScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setScanScroll(boolean isCanScroll) {
        this.isCanScroll = isCanScroll;
    }


    @Override
    public void scrollTo(int x, int y) {
//        if (isCanScroll) {
            super.scrollTo(x, y);
//        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent arg0){
/* return false;//super.onTouchEvent(arg0); */
        if (!isCanScroll)
            return false;
        else
            return super.onTouchEvent(arg0);
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0){
        if (!isCanScroll)
            return false;
        else
            return super.onInterceptTouchEvent(arg0);
    }
    @Override
    public void setCurrentItem(int item, boolean smoothScroll){
        super.setCurrentItem(item, smoothScroll);
    }
    @Override
    public void setCurrentItem(int item){
        super.setCurrentItem(item);
    }
}
