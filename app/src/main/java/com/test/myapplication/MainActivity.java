package com.test.myapplication;

import android.os.Handler;
import android.os.Looper;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.test.myapplication.client.MockApiClient;
import com.test.myapplication.model.Photo;
import com.test.myapplication.widget.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends AppCompatActivity {

    private static final long PAGE_CHANGE_INTERVAL = 6 * DateUtils.SECOND_IN_MILLIS;
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    @InjectView(R.id.pager)
    ViewPager mPager;
    @InjectView(R.id.pageIndicator)
    CirclePageIndicator mPageIndicator;
    private NewsAdapter mAdapter;

    private Runnable mTimerTask = new Runnable() {
        @Override
        public void run() {
            int nextPage = (mPager.getCurrentItem() + 1) % mPager.getAdapter().getCount();
            mPager.setCurrentItem(nextPage, true);
            scheduleTimer();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        mAdapter = new NewsAdapter();
        mPager.setAdapter(mAdapter);
        mPageIndicator.setPager(mPager);
        mPageIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                scheduleTimer();
            }

            @Override
            public void onPageSelected(int position) {
                scheduleTimer();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void scheduleTimer() {
        mHandler.removeCallbacks(mTimerTask);
        if (mAdapter.getCount() > 0) {
            mHandler.postDelayed(mTimerTask, PAGE_CHANGE_INTERVAL);
        }
    }


    public class NewsAdapter extends PagerAdapter {

        private final List<Photo> mItems = MockApiClient.getPhotos();


        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            final Photo photo = mItems.get(position);
            View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_news_home_preview, container, false);
            TextView titleView = ButterKnife.findById(view, R.id.title);
            ImageView imageView = ButterKnife.findById(view, R.id.image);
            imageView.requestLayout();
            titleView.setText(photo.getText());
            Picasso.with(getApplicationContext()).load(photo.getUrl()).into(imageView);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object view) {
            container.removeView((View) view);
        }

        @Override
        public int getCount() {
            return mItems.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        /**
         * {@inheritDoc}
         * Seems that this method is called only after notifyDataSetChanged to determine whether items changed.
         * Returning here POSITION_NONE to force ViewPager to reload views after notifyDataSetChanged
         */
        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

    }
}
