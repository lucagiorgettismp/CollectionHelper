package com.lucagiorgetti.surprix.views;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lucagiorgetti.surprix.R;
import com.lucagiorgetti.surprix.adapters.OnboardingAdapter;

public class OnboardActivity extends AppCompatActivity {
    private ViewPager mSlideViewPager;
    private LinearLayout mDotsLayout;
    private int slideCount = 5;
    private int currentPage;

    private Button nextButton;
    private Button backButton;

    private TextView[] mDots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.onboarding_main);

        mSlideViewPager = findViewById(R.id.slideViewPager);
        mDotsLayout = findViewById(R.id.dotsLayout);

        nextButton = findViewById(R.id.next);
        backButton = findViewById(R.id.previous);

        OnboardingAdapter sliderAdapter = new OnboardingAdapter(this, slideCount);
        mSlideViewPager.setAdapter(sliderAdapter);
        addDotsIndicator(0);

        mSlideViewPager.addOnPageChangeListener(viewListener);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPage < (mDots.length - 1)) {
                    mSlideViewPager.setCurrentItem(currentPage + 1);
                } else {
                    finish();
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSlideViewPager.setCurrentItem(currentPage - 1);
            }
        });
    }

    public void addDotsIndicator(int position) {
        mDots = new TextView[slideCount];
        mDotsLayout.removeAllViews();
        for (int i = 0; i < slideCount; i++) {
            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226"));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(getResources().getColor(R.color.colorTransparentWhite));

            mDotsLayout.addView(mDots[i]);
        }

        if (mDots.length > 0) {
            mDots[position].setTextColor(getResources().getColor(R.color.colorWhite));
        }
    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDotsIndicator(position);
            currentPage = position;

            if (currentPage == 0) {
                backButton.setEnabled(false);
                nextButton.setEnabled(true);
                backButton.setVisibility(View.INVISIBLE);

                nextButton.setText(getResources().getString(R.string.next));
                backButton.setText(getResources().getString(R.string.back));
            } else if (currentPage == mDots.length - 1) {
                backButton.setEnabled(true);
                nextButton.setEnabled(true);
                backButton.setVisibility(View.VISIBLE);

                nextButton.setText(getResources().getString(R.string.finish));
                backButton.setText(getResources().getString(R.string.back));
            } else {
                backButton.setEnabled(true);
                nextButton.setEnabled(true);
                backButton.setVisibility(View.VISIBLE);

                nextButton.setText(getResources().getString(R.string.next));
                backButton.setText(getResources().getString(R.string.back));
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    @Override
    public void onBackPressed() {
    }
}
