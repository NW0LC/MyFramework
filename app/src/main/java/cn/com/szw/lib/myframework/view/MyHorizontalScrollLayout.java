package cn.com.szw.lib.myframework.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;

/**
 * Created by 史忠文 on 2017/1/6.
 */

public class MyHorizontalScrollLayout extends HorizontalScrollView {
    public MyHorizontalScrollLayout(Context context) {
        super(context);
    }
    public MyHorizontalScrollLayout(Context context, AttributeSet attrs,
                                    int defStyle) {
        super(context, attrs, defStyle);
    }
    public MyHorizontalScrollLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    private ScrollViewListener scrollViewListener = null;
    public void setOnScrollViewListener(ScrollViewListener scrollViewListener) {
        this.scrollViewListener = scrollViewListener;
    }
    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        super.onScrollChanged(x, y, oldx, oldy);
        if (scrollViewListener != null) {
            scrollViewListener.onScrollChanged(this, x, y, oldx, oldy);
        }
    }


    public interface ScrollViewListener {
        void onScrollChanged(MyHorizontalScrollLayout scrollView, int x, int y,
                             int oldx, int oldy);
    }
}
