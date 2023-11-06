package ir.adak.Vect.UI.CustomViews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.Scroller;

import androidx.viewpager.widget.ViewPager;

import java.lang.reflect.Field;

public class MyViewPager extends ViewPager {

    boolean canSwipe = false;

    public MyViewPager(Context context) {
        super(context);
        setMyScroller(context);
    }

    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        setMyScroller(context);
    }

    private void setMyScroller(Context context) {
        try {
            Class viewpager = ViewPager.class;

            Field scroller = viewpager.getDeclaredField("mScroller");
            scroller.setAccessible(true);
            scroller.set(this, new MyScroller(context, new DecelerateInterpolator()));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    class MyScroller extends Scroller {

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, 350);
        }

        public MyScroller(Context context) {
            super(context);
        }

        public MyScroller(Context context, Interpolator interpolator) {
            super(context, interpolator);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        View child = getChildAt(getCurrentItem());
        if (child != null) {
            child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            int h = child.getMeasuredHeight();
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(h, MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void canSwipe(boolean canSwipe) {
        this.canSwipe = canSwipe;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (this.canSwipe) {
            return super.onTouchEvent(event);
        }

        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (this.canSwipe) {
            return super.onInterceptTouchEvent(event);
        }

        return false;
    }
}