package cn.com.szw.lib.myframework.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ProgressBar;

/*****
 *  
 * @author lixiaodaoaaa 
 *         http://weibo.com/lixiaodaoaaa 
 *         http://t.qq.com/lixiaodaoaaa 
 *  
 */  
public class TextProgressBar extends ProgressBar
{  
    private String str;  
    private Paint mPaint;
  
    public TextProgressBar(Context context)
    {  
        super(context);  
        initText();  
    }  
  
    public TextProgressBar(Context context, AttributeSet attrs, int defStyle)
    {  
        super(context, attrs, defStyle);  
        initText();  
    }  
  
    public TextProgressBar(Context context, AttributeSet attrs)  
    {  
        super(context, attrs);  
        initText();  
    }  
  
    @Override  
    public void setProgress(int progress)  
    {  
        setText(progress);  
        super.setProgress(progress);  
  
    }  
  
    @Override  
    protected synchronized void onDraw(Canvas canvas)
    {  
        super.onDraw(canvas);  
        Rect rect = new Rect();
        this.mPaint.getTextBounds(this.str, 0, this.str.length(), rect);  
        int x = (getWidth() / 2) - rect.centerX();// 让现实的字体处于中心位置;;  
        int y = (getHeight() / 2) - rect.centerY();// 让显示的字体处于中心位置;;  
        canvas.drawText(this.str, x, y, this.mPaint);  
    }  
  
    // 初始化，画笔  
    private void initText()  
    {  
        this.mPaint = new Paint();  
        this.mPaint.setAntiAlias(true);// 设置抗锯齿;;;;  
        this.mPaint.setColor(Color.WHITE);
        this.mPaint.setTextSize(30);
    }  
  
    // 设置文字内容  
    private void setText(int progress)  
    {  
        int i = (int) ((progress * 1.0f / this.getMax()) * 100);  
        this.str = String.valueOf(i) + "%";  
    }  
}  