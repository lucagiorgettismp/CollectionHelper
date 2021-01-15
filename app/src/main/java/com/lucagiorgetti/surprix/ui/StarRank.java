package com.lucagiorgetti.surprix.ui;


import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.lucagiorgetti.surprix.R;

public class StarRank extends LinearLayout {
    ImageView star1On;
    ImageView star1Off;
    ImageView star2On;
    ImageView star2Off;
    ImageView star3On;
    ImageView star3Off;

    public StarRank(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.star_rank, this);

        star1On = findViewById(R.id.star_1_on);
        star1Off = findViewById(R.id.star_1_off);
        star2On = findViewById(R.id.star_2_on);
        star2Off = findViewById(R.id.star_2_off);
        star3On = findViewById(R.id.star_3_on);
        star3Off = findViewById(R.id.star_3_off);
    }

    public void setValue(@Nullable Integer value) {
        if (value != null) {
            switch (value) {
                case 1:
                    star1On.setVisibility(View.VISIBLE);
                    star1Off.setVisibility(View.GONE);
                    break;
                case 2:
                    star1On.setVisibility(View.VISIBLE);
                    star2On.setVisibility(View.VISIBLE);
                    star1Off.setVisibility(View.GONE);
                    star2Off.setVisibility(View.GONE);
                    break;
                case 3:
                    star1On.setVisibility(View.VISIBLE);
                    star2On.setVisibility(View.VISIBLE);
                    star3On.setVisibility(View.VISIBLE);
                    star1Off.setVisibility(View.GONE);
                    star2Off.setVisibility(View.GONE);
                    star3Off.setVisibility(View.GONE);
                    break;
            }
        }
    }
}
