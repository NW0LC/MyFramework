package cn.com.szw.lib.myframework.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatRadioButton;
import android.util.AttributeSet;

public class DrawableCenterButton extends AppCompatRadioButton {

    public DrawableCenterButton(Context context) {
        super(context);
    }

    public DrawableCenterButton(Context context, AttributeSet attrs,
                                int defStyle) {
        super(context, attrs, defStyle);
    }

    public DrawableCenterButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        Drawable[] drawables = getCompoundDrawables();
        Drawable drawableRight = drawables[2];
        if (drawableRight != null) {

            float textWidth = getPaint().measureText(getText().toString());//文字的宽度
            int drawablePadding = getCompoundDrawablePadding();//文字与drawable之间的宽度
            int drawableWidth;
            drawableWidth = drawableRight.getIntrinsicWidth();//drawable的宽度
            float bodyWidth = textWidth + drawableWidth + drawablePadding;//文字加图片加间距的宽度
            setPadding(0, 0, (int)(getWidth() - bodyWidth), 0);//距离右边 控件总宽度，减去内容宽度，，，，将内容放到最左边
            canvas.translate((getWidth() - bodyWidth) / 2, 0);//把当前画布的原点移到 总宽度 减去内容/2 的位置开始画， 就是居中的
        }
        super.onDraw(canvas);
    }
}