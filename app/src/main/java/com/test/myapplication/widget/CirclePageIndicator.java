package com.test.myapplication.widget;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.StateListDrawable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

import com.test.myapplication.R;

public class CirclePageIndicator extends View {

    private static final boolean DEBUG_MODE = false;

    private static final int[] STATE_NORMAL = new int[]{};
    private static final int[] STATE_SELECTED = new int[]{android.R.attr.state_selected};

    private StateListDrawable mIndicatorDrawable;
    private ViewPager mPager;
    private int mItemsMargin;
    private int mItemWidth;
    private int mItemHeight;
    private int mVisibleItemCount;
    private ViewPager.OnPageChangeListener mPageChangeListener;

    public CirclePageIndicator(Context context) {
        super(context);
        init();
    }

    public CirclePageIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CirclePageIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public CirclePageIndicator(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public void setPager(ViewPager pager) {
        mPager = pager;
        mPager.getAdapter().registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                requestLayout();
            }
        });
        mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (mPageChangeListener != null) {
                    mPageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
                }
            }

            @Override
            public void onPageSelected(int position) {
                if (mPageChangeListener != null) {
                    mPageChangeListener.onPageSelected(position);
                }
                invalidate();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (mPageChangeListener != null) {
                    mPageChangeListener.onPageScrollStateChanged(state);
                }
            }
        });
        requestLayout();
    }

    public void setOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
        mPageChangeListener = listener;
    }

    private void init() {
        mIndicatorDrawable = (StateListDrawable) getResources().getDrawable(R.drawable.circle_indicator);
        mItemsMargin = getResources().getDimensionPixelSize(R.dimen.circle_indicator_items_margin);
        mItemWidth = mIndicatorDrawable.getIntrinsicWidth();
        mItemHeight = mIndicatorDrawable.getIntrinsicHeight();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int maxWidth = MeasureSpec.getSize(widthMeasureSpec);
        int count = mPager == null ? 0 : getPageCount();
        int maxCount = (int) Math.floor((maxWidth + mItemsMargin) / (mItemWidth + mItemsMargin));
        mVisibleItemCount = Math.min(count, maxCount);
        int width = mVisibleItemCount == 0 ? 0 : mItemWidth * mVisibleItemCount + mItemsMargin * (mVisibleItemCount - 1);
        setMeasuredDimension(Math.min(width, maxWidth), mItemHeight);
    }

    private int getPageCount() {
        return mPager.getAdapter().getCount();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (DEBUG_MODE) {
            Paint paint = new Paint();
            paint.setColor(Color.RED);
            canvas.drawRect(0, 0, 1000, 1000, paint);
        }
        if (mPager == null) {
            return;
        }
        int selectedItem = getSelectedItem();
        int x = 0;
        for (int item = 0; item < mVisibleItemCount; item++) {
            mIndicatorDrawable.setState(item == selectedItem ? STATE_SELECTED : STATE_NORMAL);
            mIndicatorDrawable.setBounds(x, 0, x + mItemWidth, mItemHeight);
            mIndicatorDrawable.draw(canvas);
            x += mItemWidth + mItemsMargin;
        }
    }

    private int getSelectedItem() {
        int currentPage = mPager.getCurrentItem();
        if (getPageCount() == mVisibleItemCount) {
            return currentPage;
        }
        int middle = Math.max(0, mVisibleItemCount / 2 - 1);
        int rightItemCount = mVisibleItemCount - middle - 1;
        if (currentPage <= middle) {
            return currentPage;
        }
        if (currentPage >= getPageCount() - 1 - rightItemCount) {
            int diff = getPageCount() - mVisibleItemCount;
            return currentPage - diff;
        }
        return middle;
    }
}
