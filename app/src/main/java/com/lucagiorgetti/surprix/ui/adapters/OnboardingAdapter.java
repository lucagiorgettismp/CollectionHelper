package com.lucagiorgetti.surprix.ui.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lucagiorgetti.surprix.R;

/**
 * Created by Luca on 27/02/2018.
 */

public class OnboardingAdapter extends PagerAdapter {

    private Context mContext;

    private int numSlide;

    public OnboardingAdapter(Context mContext, int slideCount) {
        this.mContext = mContext;
        this.numSlide = slideCount;
    }

    @Override
    public int getCount() {
        return numSlide;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        int pos = position + 1;
        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = null;
        TextView title;
        TextView content;
        ImageView imageView;

        switch (pos) {
            case 1:
                view = layoutInflater.inflate(R.layout.onboarding_default, container, false);
                title = view.findViewById(R.id.title);
                content = view.findViewById(R.id.content);
                imageView = view.findViewById(R.id.image);
                title.setText(R.string.catalog);
                content.setText(R.string.onboarding_catalog_content);
                imageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.onboard_catalog));
                break;
            case 2:
                view = layoutInflater.inflate(R.layout.onboarding_adding, container, false);
                title = view.findViewById(R.id.title);
                title.setText(R.string.create_lists);
                break;
            case 3:
                view = layoutInflater.inflate(R.layout.onboarding_exchange, container, false);
                title = view.findViewById(R.id.title);
                title.setText(R.string.make_exchange);
                break;
            case 4:
                view = layoutInflater.inflate(R.layout.onboarding_default, container, false);
                title = view.findViewById(R.id.title);
                content = view.findViewById(R.id.content);
                imageView = view.findViewById(R.id.image);
                title.setText(R.string.search);
                content.setText(R.string.onboarding_search_content);
                imageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.onboard_search));
                break;
            case 5:
                view = layoutInflater.inflate(R.layout.onboarding_default, container, false);
                title = view.findViewById(R.id.title);
                content = view.findViewById(R.id.content);
                imageView = view.findViewById(R.id.image);
                title.setText(R.string.menu);
                content.setText(R.string.onboarding_menu_content);
                imageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.onboard_menu));
                break;
        }
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout) object);
    }
}
