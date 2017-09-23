package com.szw.framelibrary.view.timer;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SizeUtils;
import com.szw.framelibrary.R;

import java.util.HashMap;
import java.util.Map;


public class TimerView extends LinearLayout {
    private static final int START_TIME = 0X001;
    private static final String ZERO_FILL = "0";

    private final long M_FOR_S = 30 * 24 * 60 * 60;
    private final long DAY_FOR_S = 24 * 60 * 60;
    private final long HOUR_FOR_S = 60 * 60;
    private final long MIN_FOR_S = 60;
    //计时器开关
    private boolean isStart = false;
    //view 对象池
    private Map<Byte, TimeItemView> viewPool = new HashMap<>();
    private final byte DAY_TAG = 0x04;
    private final byte HOUR_TAG = 0x03;
    private final byte MIN_TAG = 0x02;
    private final byte SECOND_TAG = 0x01;

    private long mSeconds;
    private Context context;

    public void setOnTimerListener(OnTimerListener onTimerListener) {
        this.onTimerListener = onTimerListener;
    }

    private OnTimerListener onTimerListener;

    public long getmSeconds() {
        return mSeconds;
    }

    public TimerView(Context context) {
        super(context);
        init(context);
    }

    public TimerView(Context context, AttributeSet attr) {
        super(context, attr);
        init(context);
    }


    public void init(Context context) {
        this.context = context;
        setOrientation(HORIZONTAL);
    }


    private void show(long seconds) {
        long countTimer = seconds;
        if (seconds > M_FOR_S) {
            Log.e("log","暂不支持大于月的计时");
            return;
        }
        if (seconds > DAY_FOR_S) {
            //存在天
            if (viewPool.containsKey(DAY_TAG)) {
                TimeItemView timeItemView = viewPool.get(DAY_TAG);
                timeItemView.setNumText((countTimer / DAY_FOR_S));
            } else {
                TimeItemView timeItemView = new TimeItemView(context);
                timeItemView.setNumText((countTimer / DAY_FOR_S));
                timeItemView.setUnitText((DAY_TAG));
                addView(timeItemView);
                viewPool.put(DAY_TAG, timeItemView);
            }
            countTimer -= (countTimer / DAY_FOR_S) * DAY_FOR_S;
        } else {
            if (viewPool.get(DAY_TAG) != null && seconds == DAY_FOR_S ) {
                removeView(viewPool.get(DAY_TAG));
            }
        }
//        if (seconds > HOUR_FOR_S) {
        //存在时
        if (viewPool.containsKey(HOUR_TAG)) {
            TimeItemView timeItemView = viewPool.get(HOUR_TAG);
            timeItemView.setNumText((countTimer / HOUR_FOR_S));
        } else {
            TimeItemView timeItemView = new TimeItemView(context);
            timeItemView.setNumText((countTimer / HOUR_FOR_S));
            timeItemView.setUnitText((HOUR_TAG));
            this.addView(timeItemView);
            viewPool.put(HOUR_TAG, timeItemView);
        }
        if (seconds > HOUR_FOR_S)//改
            countTimer -= (countTimer / HOUR_FOR_S) * HOUR_FOR_S;
//        } else {
//            if (viewPool.get(HOUR_TAG) != null && seconds == HOUR_FOR_S) {
//                removeView(viewPool.get(HOUR_TAG));
//            }
//        }
//        if (seconds > MIN_FOR_S) {
//            //存在分
        if (viewPool.containsKey(MIN_TAG)) {
            TimeItemView timeItemView = viewPool.get(MIN_TAG);
            timeItemView.setNumText((countTimer / MIN_FOR_S));
        } else {
            TimeItemView timeItemView = new TimeItemView(context);
            timeItemView.setNumText((countTimer / MIN_FOR_S));
            timeItemView.setUnitText((MIN_TAG));
            this.addView(timeItemView);
            viewPool.put(MIN_TAG, timeItemView);
        }
        if (seconds > MIN_FOR_S)//改
            countTimer -= (countTimer / MIN_FOR_S) * MIN_FOR_S;
//        } else {
//            if (viewPool.get(MIN_TAG) != null && seconds == MIN_FOR_S) {
//                removeView(viewPool.get(MIN_TAG));
//            }
//        }
//        if (seconds > 0) {
        //存在S
        if (viewPool.containsKey(SECOND_TAG)) {
            TimeItemView timeItemView = viewPool.get(SECOND_TAG);
            timeItemView.setNumText((countTimer));
        } else {
            TimeItemView timeItemView = new TimeItemView(context);
            timeItemView.setNumText((countTimer));
            timeItemView.setUnitText((SECOND_TAG));
            this.addView(timeItemView);
            viewPool.put(SECOND_TAG, timeItemView);
        }
//        }else {
            if (viewPool.get(SECOND_TAG) != null && seconds == 0) {
//                removeView(viewPool.get(MIN_TAG));
                //回调
                if (onTimerListener != null){
                    onTimerListener.timeOver();
                }
            }
//        }

    }


    /**
     * 开始计时
     */
//    private int mposition=-1;
    public void startTiming(long seconds,int position) {
        this.mSeconds = seconds;
//        if (mposition==position) {
//            return;
//        }
//        else {
//            mposition=position;
//        }
        if (!isStart){
            isStart = true;
            handler.sendEmptyMessageDelayed(START_TIME, 1000);}
    }

    /**
     * 结束计时
     */
    public void shopTiming() {
        removeAllViews();
        isStart = false;
        viewPool.clear();
    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == START_TIME) {
                if (mSeconds == 0) {
                    isStart = false;
                }
                show(mSeconds--);
                if (isStart) {
                    handler.sendEmptyMessageDelayed(START_TIME, 1000);
                }
            }
        }
    };

    class TimeItemView extends LinearLayout {
        private Context context;
        private TextView numTv;
        private TextView unitTv;

        TimeItemView(Context context) {
            super(context);
            this.context = context;
            setOrientation(HORIZONTAL);
            addView(getNumTv());
            addView(getUnitTv());
        }

        public void setNumText(long num) {
            if (num < 10) {
                getNumTv().setText(String.format("%s%s", ZERO_FILL, num));
            } else {
                getNumTv().setText(String.valueOf(num));
            }
        }

        public void setUnitText(byte tag) {
            switch (tag) {//改过
                case DAY_TAG:
                    getUnitTv().setText(" : ");
                    getNumTv().setBackground(ContextCompat.getDrawable(context, R.drawable.time_black_line));
                    getNumTv().setPadding(SizeUtils.dp2px(3),SizeUtils.dp2px(2),SizeUtils.dp2px(3),SizeUtils.dp2px(2));
                    getNumTv().setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                    break;
                case HOUR_TAG:
                    getUnitTv().setText(" : ");
                    getNumTv().setBackground(ContextCompat.getDrawable(context,R.drawable.time_black_line));
                    getNumTv().setPadding(SizeUtils.dp2px(3),SizeUtils.dp2px(2),SizeUtils.dp2px(3),SizeUtils.dp2px(2));
                    getNumTv().setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                    break;
                case MIN_TAG:
                    getUnitTv().setText(" : ");
                    getNumTv().setBackground(ContextCompat.getDrawable(context,R.drawable.time_black_line));
                    getNumTv().setPadding(SizeUtils.dp2px(3),SizeUtils.dp2px(2),SizeUtils.dp2px(3),SizeUtils.dp2px(2));
                    getNumTv().setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                    break;
                case SECOND_TAG:
                    getUnitTv().setText("");
                    getNumTv().setBackground(ContextCompat.getDrawable(context,R.drawable.time_black_line));
                    getNumTv().setPadding(SizeUtils.dp2px(3),SizeUtils.dp2px(2),SizeUtils.dp2px(3),SizeUtils.dp2px(2));
                    getNumTv().setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
//                    getNumTv().setTextColor(ContextCompat.getColor(context,R.color.red_de1a19));
                    break;
            }
        }

        private TextView getNumTv() {
            if (numTv == null) {
                numTv = new TextView(context);
                numTv.setTextColor(Color.parseColor("#FF333333"));
                numTv.setBackgroundColor(Color.WHITE);
                numTv.setTextSize(12);
            }
            return numTv;
        }

        private TextView getUnitTv() {
            if (unitTv == null) {
                unitTv = new TextView(context);
                unitTv.setTextColor(Color.parseColor("#FF333333"));

            }
            return unitTv;
        }
    }

    public interface OnTimerListener {
        void timeOver();
    }

}