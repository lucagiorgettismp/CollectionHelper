package com.lucagiorgetti.surprix.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
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

public class SliderAdapter extends PagerAdapter {

    private Context mContext;

    private int numSlide;

    public SliderAdapter(Context mContext, int slideCount) {
        this.mContext = mContext;
        this.numSlide = slideCount;
    }

    @Override
    public int getCount() {
        return numSlide;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        int pos = position + 1;
        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = null;
        TextView title;
        TextView content;
        ImageView imageView;
        switch (pos){
            case 1:
                view = layoutInflater.inflate(R.layout.default_slide_layout, container, false);
                title = (TextView) view.findViewById(R.id.title);
                content = (TextView) view.findViewById(R.id.content);
                imageView = (ImageView) view.findViewById(R.id.image);
                title.setText(R.string.catalog);
                content.setText(R.string.onboarding_catalog_content);
                imageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.onboard_catalog));
                break;
            case 2:
                view = layoutInflater.inflate(R.layout.adding_slide_layout, container, false);
                title= (TextView) view.findViewById(R.id.title);
                title.setText(R.string.create_lists);
                break;
            case 3:
                view = layoutInflater.inflate(R.layout.change_slide_layout, container, false);
                title = (TextView) view.findViewById(R.id.title);
                title.setText(R.string.make_exchange);
                break;
            case 4:
                view = layoutInflater.inflate(R.layout.default_slide_layout, container, false);
                title = (TextView) view.findViewById(R.id.title);
                content = (TextView) view.findViewById(R.id.content);
                imageView =(ImageView) view.findViewById(R.id.image);
                title.setText(R.string.search);
                content.setText(R.string.onboarding_search_content);
                imageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.onboard_search));
                break;
            case 5:
                view = layoutInflater.inflate(R.layout.default_slide_layout, container, false);
                title = (TextView) view.findViewById(R.id.title);
                content = (TextView) view.findViewById(R.id.content);
                imageView = (ImageView) view.findViewById(R.id.image);
                title.setText(R.string.menu);
                content.setText(R.string.onboarding_menu_content);
                imageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.onboard_menu));
                break;
        }
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout)object);
    }
}
