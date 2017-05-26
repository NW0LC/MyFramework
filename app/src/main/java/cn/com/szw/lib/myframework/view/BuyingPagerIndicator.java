package cn.com.szw.lib.myframework.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;

import cn.com.exz.beefrog.R;

/*
 * ============================================================
 * Editor: MuMuXuan(6511631@qq.com)
 *  
 * Time: 2016-08-24 12:05 
 * 
 * Description: 仿淘宝抢购页面的ViewPager的tab指针
 *
 * how to use:设置标题时需要在Adapter的getTitle中设置为 04:00#即将入场 这样的格式
 * （两个字符串用“#”来分割）
 *
 * Version: 1.0
 * ============================================================
 */
public class BuyingPagerIndicator extends FrameLayout {
    public static final int TABVISIBLECOUNT = 5;//用户可见的tab数量
    private static final String TAG = "BuyingPagerIndicator";

    private Paint mPaint;//绘制指针的画笔
    private Path mPath;//绘制指针的path路径
    private int mTabsWidth;//每个标题tab的宽度
    private ViewPager mPager;//关联的ViewPager
    private LinearLayout mTabsContainer;//各个tab的容器
    private int mTabCount;//tab的总个数
    private LinearLayout.LayoutParams mDefaultTabLayoutParams;
    private final PageListener mPageListener = new PageListener();//跟ViewPager关联的Listener
    private int mSelectedPosition;//当前ViewPager被选中的位置
    private int mLastScrollX;//tabContainer最后x方向滚动到的位置
    private Scroller mScroller;//用来做平滑滚动效果
    private ViewPager.OnPageChangeListener mDelegatePageListener;//页面监听器

    public BuyingPagerIndicator(Context context) {
        this(context, null);
    }

    public BuyingPagerIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BuyingPagerIndicator(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setWillNotDraw(false);
        // 获得自定义属性，tab的数量
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.BuyingPagerIndicator);
        int indicatorColor = a.getColor(R.styleable.BuyingPagerIndicator_indicator_color,
                Color.parseColor("#ff0000"));
        a.recycle();

        // 初始化画笔
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(indicatorColor);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setPathEffect(new CornerPathEffect(3));

        mScroller = new Scroller(context);
        initContainer(context);
    }

    public void setOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
        this.mDelegatePageListener = listener;
    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //初始化tab的宽度
        mTabsWidth = w / TABVISIBLECOUNT;
        updateTab();
        // 初始化指针
        initIndicator();
    }

    //更新tab的大小
    private void updateTab() {
        int childCount = mTabsContainer.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = mTabsContainer.getChildAt(i);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(mTabsWidth,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            if (i == 0) {
                //第一个tab，设置左间距
                Log.d(TAG, "updateTab: //第一个tab，设置左间距");
                layoutParams.leftMargin = (TABVISIBLECOUNT / 2) * mTabsWidth;
            } else if (i == childCount - 1) {
                //最后一个tab，设置右间距
                layoutParams.rightMargin = (TABVISIBLECOUNT / 2) * mTabsWidth;
            }
            childAt.setLayoutParams(layoutParams);
            Log.d(TAG, "updateTab: width" + mTabsWidth);
        }
    }

    //在dispatchDraw中去绘制indicator
    @Override
    protected void dispatchDraw(Canvas canvas) {
        canvas.save();
        // 画笔平移到正确的位置
        int transX = (TABVISIBLECOUNT / 2) * mTabsWidth;
        canvas.translate(transX, 0);
        canvas.drawPath(mPath, mPaint);
        canvas.restore();
        super.dispatchDraw(canvas);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (isInEditMode()) {
            return;
        }
        // draw 设置item选中效果
        for (int i = 0; i < mTabsContainer.getChildCount(); ++i) {
            View tab = mTabsContainer.getChildAt(i);
            tab.setSelected(i == mSelectedPosition);
        }
        super.onDraw(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        //如果高为wrap_content，设置高度为45dp
        if (heightMode == MeasureSpec.AT_MOST) {
            heightSize = dp2px(45);
        }
        setMeasuredDimension(widthSize, heightSize);

    }

    //初始化指针的形状
    private void initIndicator() {
        mPath = new Path();
        mPath.moveTo(0, 0);
        mPath.lineTo(mTabsWidth, 0);
        mPath.lineTo(mTabsWidth, getHeight());
        mPath.lineTo(0, getHeight());
        mPath.close();
    }

    //绑定ViewPager
    public void setViewPager(ViewPager pager) {
        this.mPager = pager;
        if (mPager.getAdapter() == null) {
            throw new IllegalStateException("ViewPager does not have adapter instance.");
        }
        //设置监听器
        mPager.addOnPageChangeListener(mPageListener);
        //刷新
        notifyDataSetChanged();
    }


    private void initContainer(Context context) {
        mTabsContainer = new LinearLayout(context) ;
        mTabsContainer.setOrientation(LinearLayout.HORIZONTAL);
        //这里mTabContainer的宽度是明显大于父控件的，所以宽度设置为Wrap而不是Match
        mTabsContainer.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
        mDefaultTabLayoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        addView(mTabsContainer);
    }

    public void notifyDataSetChanged() {

        mTabsContainer.removeAllViews();
        mTabCount = mPager.getAdapter().getCount();

        for (int i = 0; i < mTabCount; i++) {
            String title = mPager.getAdapter().getPageTitle(i).toString();
            String[] split = title.split("#");
            View tab = generateTab(split);
            addTab(i, tab);
        }
        //手动调起Listener中的选中方法
        mPageListener.onPageSelected(mPager.getCurrentItem());
    }


    private View generateTab(String[] strs) {
        View tab = View.inflate(getContext(), R.layout.item_rush_tab, null);
        TextView tv_item_tab_time = (TextView) tab.findViewById(R.id.tv_item_tab_time);
        TextView tv_item_tab_desc = (TextView) tab.findViewById(R.id.tv_item_tab_desc);
        tv_item_tab_desc.setMaxEms(5);
        if (strs.length > 1) {
            tv_item_tab_time.setText(strs[0]);
            tv_item_tab_desc.setText(strs[1]);
        }

        return tab;
    }

    private void addTab(final int position, View tab) {
        tab.setFocusable(true);
        tab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mPager.setCurrentItem(position);
            }
        });
        mTabsContainer.addView(tab, position, mDefaultTabLayoutParams);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                //在拦截方法的down事件中设置初始值才能拿到，如果是onTouch中拿就晚了
                startX = (int) ev.getX();
                mStartScrollX = mLastScrollX;
                break;
            case MotionEvent.ACTION_MOVE:
                int newX = (int) ev.getX();
                int dx = Math.abs(newX - startX);
                startX = newX;
                if (dx >  1) {
                    //拦截触摸事件
                    Log.d(TAG, "onInterceptHoverEvent: true");
                    return true;
                }
                break;
            default:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    int startX = 0;
    int mStartScrollX = 0;//x方向上的刚开始的位移量
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //1 判断事件类型
        int action = event.getAction();
        int currentItem = mPager.getCurrentItem();
        int left = mTabsContainer.getChildAt(mTabCount - 3).getLeft();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                //2 为down时，记录下x
//                startX = (int) event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                //3 为move时，让container去scroll对应的x位移
                int newX = (int) event.getX();
                int dx = newX - startX;
//                mTabsContainer.scrollTo(mLastScrollX-newX, 0);
                //设置边界检测
                if (mLastScrollX - dx <= 0) {
                    return false;
                } else if (mLastScrollX - dx >= left) {
                    return false;
                }
                Log.d(TAG, "onTouchEvent: dX " + newX + " mLastScrollX: " + mLastScrollX);
//                mScroller.startScroll(mLastScrollX, 0, mLastScrollX - dx, 0);
//                invalidate();
                mLastScrollX -= dx;
                mTabsContainer.scrollBy(-dx,0);
                startX = newX;
                break;
            case MotionEvent.ACTION_UP:
                //4 为up时，检测位置，如果container没有吻合红色标记，则去scroll，回退到应该处于的位置
                int remainX = mLastScrollX % mTabsWidth;
                int moveX;
                if (remainX > mTabsWidth / 2) {
                    //超过了宽度的一半,向前再滚动，选中前面的tab
                    moveX = - remainX + mTabsWidth;
                } else {
                    moveX= - remainX;
                }
                mScroller.startScroll(mLastScrollX, 0, moveX, 0);
                invalidate();
                mLastScrollX = mLastScrollX + moveX;
//                    mTabsContainer.scrollTo(mLastScrollX,0);
                //同时，让ViewPager选中对应的currentItem
                currentItem = currentItem+(mLastScrollX-mStartScrollX)/mTabsWidth;
                Log.d(TAG, "onTouchEvent: mLastScrollX-startScrollX "+(mLastScrollX-mStartScrollX));
                mPager.setCurrentItem(currentItem);
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            int currX = mScroller.getCurrX();
            int currY = mScroller.getCurrY();
            mTabsContainer.scrollTo(currX, currY);
            invalidate();
        }
    }

    public int dp2px(int dp) {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        float density = displayMetrics.scaledDensity;
        int px = (int) (dp * density + 0.5f);
        Log.d(TAG, "dp2px: " + px);
        return px;
    }

    private class PageListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            scrollToChild(position, (int) (positionOffset * mTabsContainer.getChildAt(position).getWidth()));

            if (mDelegatePageListener != null) {
                mDelegatePageListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
//            if (state == ViewPager.SCROLL_STATE_IDLE) {
//                scrollToChild(pager.getCurrentItem(), 0);
//            }
            if (mDelegatePageListener != null) {
                mDelegatePageListener.onPageScrollStateChanged(state);
            }
        }

        @Override
        public void onPageSelected(int position) {
            if (mDelegatePageListener != null) {
                mDelegatePageListener.onPageSelected(position);
            }
            mSelectedPosition = position;
            updateTabTextSize(position);
            invalidate();
        }
    }

    private void scrollToChild(int position, int offset) {
        if (mTabCount == 0) {
            return;
        }
        int newScrollX = mTabsContainer.getChildAt(position).getLeft() + offset -
                mTabsWidth * (TABVISIBLECOUNT / 2);
        Log.d(TAG, "scrollToChild: newScrollX :" + newScrollX);
        if (newScrollX != mLastScrollX) {
            mLastScrollX = newScrollX;
            mTabsContainer.scrollTo(newScrollX, 0);
        }
    }

    //更新tab上的字体大小  默认字体大小为16sp和20sp两种，这里偷懒没有做自定义属性
    private void updateTabTextSize(int selectedPosition) {
        for (int i = 0; i < mTabsContainer.getChildCount(); ++i) {
            View tab = mTabsContainer.getChildAt(i);
            if (tab instanceof RelativeLayout) {
                TextView tv_item_tab_time = (TextView) tab.findViewById(R.id.tv_item_tab_time);
                tv_item_tab_time.setTextSize(TypedValue.COMPLEX_UNIT_DIP, i == selectedPosition ? 20 : 16);
            }
        }
    }
}
