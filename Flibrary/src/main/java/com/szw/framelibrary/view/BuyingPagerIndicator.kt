package com.szw.framelibrary.view

import android.content.Context
import android.graphics.*
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.szw.framelibrary.R


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
class BuyingPagerIndicator @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : FrameLayout(context, attrs, defStyle) {

    private val mPaint: Paint//绘制指针的画笔
    private lateinit var mPath: Path//绘制指针的path路径
    private var mTabsWidth: Int = 0//每个标题tab的宽度
    private lateinit var mPager: ViewPager//关联的ViewPager
    lateinit var mTabsContainer: LinearLayout//各个tab的容器
    private var mTabCount: Int = 0//tab的总个数
    private var mDefaultTabLayoutParams: LinearLayout.LayoutParams? = null
    private val mPageListener = PageListener()//跟ViewPager关联的Listener
    private var mSelectedPosition: Int = 0//当前ViewPager被选中的位置
    private var mLastScrollX: Int = 0//tabContainer最后x方向滚动到的位置
    private val mScroller: Scroller//用来做平滑滚动效果
    private var mDelegatePageListener: ViewPager.OnPageChangeListener? = null//页面监听器

    init {
        setWillNotDraw(false)
        // 获得自定义属性，tab的数量
        val a = context.obtainStyledAttributes(attrs,
                R.styleable.BuyingPagerIndicator)
        val indicatorColor = a.getColor(R.styleable.BuyingPagerIndicator_indicator_color,
                Color.parseColor("#ff0000"))
        a.recycle()

        // 初始化画笔
        mPaint = Paint()
        mPaint.isAntiAlias = true
        mPaint.color = indicatorColor
        mPaint.style = Paint.Style.FILL
        mPaint.pathEffect = CornerPathEffect(3f)

        mScroller = Scroller(context)
        initContainer(context)
    }

    fun setOnPageChangeListener(listener: ViewPager.OnPageChangeListener) {
        this.mDelegatePageListener = listener
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        //初始化tab的宽度
        mTabsWidth = w / TABVISIBLECOUNT
        updateTab()
        // 初始化指针
        initIndicator()
    }

    //更新tab的大小
    private fun updateTab() {
        val childCount = mTabsContainer.childCount
        for (i in 0 until childCount) {
            val childAt = mTabsContainer.getChildAt(i)
            val layoutParams = LinearLayout.LayoutParams(mTabsWidth,
                    ViewGroup.LayoutParams.MATCH_PARENT)
            if (i == 0) {
                //第一个tab，设置左间距
                Log.d(TAG, "updateTab: //第一个tab，设置左间距")
                layoutParams.leftMargin = TABVISIBLECOUNT / 2 * mTabsWidth
            } else if (i == childCount - 1) {
                //最后一个tab，设置右间距
                layoutParams.rightMargin = TABVISIBLECOUNT / 2 * mTabsWidth
            }
            childAt.layoutParams = layoutParams
            Log.d(TAG, "updateTab: width" + mTabsWidth)
        }
    }

    //在dispatchDraw中去绘制indicator
    override fun dispatchDraw(canvas: Canvas) {
        canvas.save()
        // 画笔平移到正确的位置
        val transX = TABVISIBLECOUNT / 2 * mTabsWidth
        canvas.translate(transX.toFloat(), 0f)
        canvas.drawPath(mPath, mPaint)
        canvas.restore()
        super.dispatchDraw(canvas)
    }

    override fun onDraw(canvas: Canvas) {
        if (isInEditMode) {
            return
        }
        // draw 设置item选中效果
        for (i in 0 until mTabsContainer.childCount) {
            val tab = mTabsContainer.getChildAt(i)
            tab.isSelected = i == mSelectedPosition
        }
        super.onDraw(canvas)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthSize = View.MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = View.MeasureSpec.getMode(heightMeasureSpec)
        var heightSize = View.MeasureSpec.getSize(heightMeasureSpec)
        //如果高为wrap_content，设置高度为45dp
        if (heightMode == View.MeasureSpec.AT_MOST) {
            heightSize = dp2px(45)
        }
        setMeasuredDimension(widthSize, heightSize)

    }

    //初始化指针的形状
    private fun initIndicator() {
        mPath = Path()
        mPath.moveTo(0f, 0f)
        mPath.lineTo(mTabsWidth.toFloat(), 0f)
        mPath.lineTo(mTabsWidth.toFloat(), height.toFloat())
        mPath.lineTo(0f, height.toFloat())
        mPath.close()
    }

    //绑定ViewPager
    fun setViewPager(pager: ViewPager) {
        this.mPager = pager
        if (mPager.adapter == null) {
            throw IllegalStateException("ViewPager does not have adapter instance.")
        }
        //设置监听器
        mPager.addOnPageChangeListener(mPageListener)
        //刷新
        notifyDataSetChanged()
    }


    private fun initContainer(context: Context) {
        mTabsContainer = LinearLayout(context)
        mTabsContainer.orientation = LinearLayout.HORIZONTAL
        //这里mTabContainer的宽度是明显大于父控件的，所以宽度设置为Wrap而不是Match
        mTabsContainer.layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.MATCH_PARENT)
        mDefaultTabLayoutParams = LinearLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.MATCH_PARENT)
        addView(mTabsContainer)
    }

    private fun notifyDataSetChanged() {

        mTabsContainer.removeAllViews()
        mTabCount = mPager.adapter.count

        for (i in 0 until mTabCount) {
            val title = mPager.adapter.getPageTitle(i).toString()
            val split = title.split("#".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val tab = generateTab(split)
            addTab(i, tab)
        }
        //手动调起Listener中的选中方法
        mPageListener.onPageSelected(mPager.currentItem)
    }


    private fun generateTab(strs: Array<String>): View {
        val tab = View.inflate(context, R.layout.item_rush_tab, null)
        val tv_item_tab_time = tab.findViewById<View>(R.id.tv_item_tab_time) as TextView
        val tv_item_tab_desc = tab.findViewById<View>(R.id.tv_item_tab_desc) as TextView
        tv_item_tab_desc.maxEms = 5
        if (strs.size > 1) {
            tv_item_tab_time.text = strs[0]
            tv_item_tab_desc.text = strs[1]
        }

        return tab
    }

    private fun addTab(position: Int, tab: View) {
        tab.isFocusable = true
        tab.setOnClickListener { mPager.currentItem = position }
        mTabsContainer.addView(tab, position, mDefaultTabLayoutParams)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        val action = ev.action
        when (action) {
            MotionEvent.ACTION_DOWN -> {
                //在拦截方法的down事件中设置初始值才能拿到，如果是onTouch中拿就晚了
                startX = ev.x.toInt()
                mStartScrollX = mLastScrollX
            }
            MotionEvent.ACTION_MOVE -> {
                val newX = ev.x.toInt()
                val dx = Math.abs(newX - startX)
                startX = newX
                if (dx > 1) {
                    //拦截触摸事件
                    Log.d(TAG, "onInterceptHoverEvent: true")
                    return true
                }
            }
            else -> {
            }
        }
        return super.onInterceptTouchEvent(ev)
    }

    private var startX = 0
    private var mStartScrollX = 0//x方向上的刚开始的位移量
    override fun onTouchEvent(event: MotionEvent): Boolean {
        //1 判断事件类型
        val action = event.action
        var currentItem = mPager.currentItem
        val left = mTabsContainer.getChildAt(mTabCount - 3).left
        when (action) {
            MotionEvent.ACTION_DOWN -> {
            }
            MotionEvent.ACTION_MOVE -> {
                //3 为move时，让container去scroll对应的x位移
                val newX = event.x.toInt()
                val dx = newX - startX
                //                mTabsContainer.scrollTo(mLastScrollX-newX, 0);
                //设置边界检测
                if (mLastScrollX - dx <= 0) {
                    return false
                } else if (mLastScrollX - dx >= left) {
                    return false
                }
                Log.d(TAG, "onTouchEvent: dX $newX mLastScrollX: $mLastScrollX")
                //                mScroller.startScroll(mLastScrollX, 0, mLastScrollX - dx, 0);
                //                invalidate();
                mLastScrollX -= dx
                mTabsContainer.scrollBy(-dx, 0)
                startX = newX
            }
            MotionEvent.ACTION_UP -> {
                //4 为up时，检测位置，如果container没有吻合红色标记，则去scroll，回退到应该处于的位置
                val remainX = mLastScrollX % mTabsWidth
                val moveX: Int
                moveX = if (remainX > mTabsWidth / 2) {
                    //超过了宽度的一半,向前再滚动，选中前面的tab
                    -remainX + mTabsWidth
                } else {
                    -remainX
                }
                mScroller.startScroll(mLastScrollX, 0, moveX, 0)
                invalidate()
                mLastScrollX += moveX
                //                    mTabsContainer.scrollTo(mLastScrollX,0);
                //同时，让ViewPager选中对应的currentItem
                currentItem += (mLastScrollX - mStartScrollX) / mTabsWidth
                Log.d(TAG, "onTouchEvent: mLastScrollX-startScrollX " + (mLastScrollX - mStartScrollX))
                mPager.currentItem = currentItem
            }
            else -> {
            }
        }//2 为down时，记录下x
        //                startX = (int) event.getX();
        return true
    }

    override fun computeScroll() {
        if (mScroller.computeScrollOffset()) {
            val currX = mScroller.currX
            val currY = mScroller.currY
            mTabsContainer.scrollTo(currX, currY)
            invalidate()
        }
    }

    private fun dp2px(dp: Int): Int {
        val displayMetrics = context.resources.displayMetrics
        val density = displayMetrics.scaledDensity
        val px = (dp * density + 0.5f).toInt()
        Log.d(TAG, "dp2px: " + px)
        return px
    }

    private inner class PageListener : ViewPager.OnPageChangeListener {

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            scrollToChild(position, (positionOffset * mTabsContainer.getChildAt(position).width).toInt())

            if (mDelegatePageListener != null) {
                mDelegatePageListener?.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }
        }

        override fun onPageScrollStateChanged(state: Int) {
            //            if (state == ViewPager.SCROLL_STATE_IDLE) {
            //                scrollToChild(pager.getCurrentItem(), 0);
            //            }
            if (mDelegatePageListener != null) {
                mDelegatePageListener?.onPageScrollStateChanged(state)
            }
        }

        override fun onPageSelected(position: Int) {
            if (mDelegatePageListener != null) {
                mDelegatePageListener?.onPageSelected(position)
            }
            mSelectedPosition = position
            updateTabTextSize(position)
            invalidate()
        }
    }

    private fun scrollToChild(position: Int, offset: Int) {
        if (mTabCount == 0) {
            return
        }
        val newScrollX = mTabsContainer.getChildAt(position).left + offset - mTabsWidth * (TABVISIBLECOUNT / 2)
        Log.d(TAG, "scrollToChild: newScrollX :" + newScrollX)
        if (newScrollX != mLastScrollX) {
            mLastScrollX = newScrollX
            mTabsContainer.scrollTo(newScrollX, 0)
        }
    }

    //更新tab上的字体大小  默认字体大小为16sp和20sp两种，这里偷懒没有做自定义属性
    private fun updateTabTextSize(selectedPosition: Int) {
        for (i in 0 until mTabsContainer.childCount) {
            val tab = mTabsContainer.getChildAt(i)
            if (tab is RelativeLayout) {
                val tv_item_tab_time = tab.findViewById<View>(R.id.tv_item_tab_time) as TextView
                tv_item_tab_time.setTextSize(TypedValue.COMPLEX_UNIT_DIP, (if (i == selectedPosition) 20 else 16).toFloat())
            }
        }
    }

    companion object {
        val TABVISIBLECOUNT = 5//用户可见的tab数量
        private val TAG = "BuyingPagerIndicator"
    }
}
