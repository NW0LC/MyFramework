package com.szw.framelibrary.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.support.v7.widget.AppCompatRadioButton
import android.util.AttributeSet

class DrawableCenterButton : AppCompatRadioButton {

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet,
                defStyle: Int) : super(context, attrs, defStyle) {
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}


    override fun onDraw(canvas: Canvas) {
        val drawables = compoundDrawables
        val drawableRight = drawables[2]
        if (drawableRight != null) {

            val textWidth = paint.measureText(text.toString())//文字的宽度
            val drawablePadding = compoundDrawablePadding//文字与drawable之间的宽度
            val drawableWidth: Int
            drawableWidth = drawableRight.intrinsicWidth//drawable的宽度
            val bodyWidth = textWidth + drawableWidth.toFloat() + drawablePadding.toFloat()//文字加图片加间距的宽度
            setPadding(0, 0, (width - bodyWidth).toInt(), 0)//距离右边 控件总宽度，减去内容宽度，，，，将内容放到最左边
            canvas.translate((width - bodyWidth) / 2, 0f)//把当前画布的原点移到 总宽度 减去内容/2 的位置开始画， 就是居中的
        }
        super.onDraw(canvas)
    }
}