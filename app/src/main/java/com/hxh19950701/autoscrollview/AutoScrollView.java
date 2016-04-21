package com.hxh19950701.autoscrollview;


import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ScrollView;


/**
 * Depiction:Control to scroll up and down to refresh the view
 * <p/>
 * Author: hxh19950701
 * <p/>
 * Create Date：2016/4/21
 * <p/>
 * Modify:
 * <p/>
 *
 * @version 1.0
 * @since 1.0
 */
public class AutoScrollView extends ScrollView {
    private final Handler handler = new Handler();
    private long duration = 50;
    private boolean isScrolled = false;
    private int currentIndex = 0;
    private long period = 2000;
    private int currentY = -1;
    private double x;
    private double y;

    /**
     * @param context
     */
    public AutoScrollView(Context context) {
        this(context, null);
    }

    /**
     * @param context
     * @param attrs
     */
    public AutoScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * @param context
     * @param attrs
     * @param defStyle
     */
    public AutoScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public boolean onTouchEvent(MotionEvent event) {
        int Action = event.getAction();
        switch (Action) {
            case MotionEvent.ACTION_DOWN:
                x = event.getX();
                y = event.getY();
                setScrolled(false);
                break;
            case MotionEvent.ACTION_MOVE:
                double moveY = event.getY() - y;
                double moveX = event.getX() - x;
                if ((moveY > 20 || moveY < -20) && (moveX < 50 || moveX > -50) && getParent() != null) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                break;
            case MotionEvent.ACTION_UP:
                new DelayedThread().start();
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent p_event) {
        Log.d("test", "onInterceptTouchEvent");
        return true;
    }

    /**
     * Whether current judgment for rolling state
     *
     * @return the isScrolled
     */
    public boolean isScrolled() {
        return isScrolled;
    }

    /**
     * et to open or close automatically scrolling function
     *
     * @param isScrolled true:open false:close
     */
    public void setScrolled(boolean isScrolled) {
        this.isScrolled = isScrolled;
        handler.postDelayed(new AutoScrollThread(), duration);
    }

    /**
     * Gets the current scroll to the end of the pause time, unit: ms
     *
     * @return the period
     */
    public long getPeriod() {
        return period;
    }

    /**
     * Set the current scroll to the end of the pause time, unit: ms
     *
     * @param period the period to set
     */
    public void setPeriod(long period) {
        this.period = period;
    }

    /**
     * Gets the scroll speed, the current unit: ms, the smaller the value, the faster.
     *
     * @return the speed
     */
    public long getSpeed() {
        return duration;
    }

    /**
     * Setting the rolling speed, the current unit: ms, the smaller the value, the faster.
     *
     * @param speed the duration to set
     */
    public void setSpeed(long speed) {
        this.duration = speed;
    }

    int threadCount = 0;

    private class DelayedThread extends Thread {
        public void run() {
            ++threadCount;
            try {
                sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            currentIndex = getScrollY();
            if (!isScrolled && threadCount == 1) {
                setScrolled(true);
            }
            --threadCount;
        }
    }

    private class AutoScrollThread implements Runnable {
        public void run() {
            if (isScrolled) {
                if (currentY == getScrollY()) {
                    try {
                        Thread.sleep(period);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    currentIndex = 0;
                    scrollTo(0, 0);
                    handler.postDelayed(this, period);
                } else {
                    currentY = getScrollY();
                    handler.postDelayed(this, duration);
                    scrollTo(0, ++currentIndex);
                }
            }
        }
    }
}