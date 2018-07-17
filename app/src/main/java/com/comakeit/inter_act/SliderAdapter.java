package com.comakeit.inter_act;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by nalin on 3/22/2018.
 */

public class SliderAdapter extends PagerAdapter {
    Context mContext;
    LayoutInflater mLayoutInflater;

    public SliderAdapter(Context context){
        this.mContext = context;
    }

    //Arrays
    public int[] slide_images = {
            R.drawable.ic_interact_logo,
            R.drawable.ic_feedback,
            R.drawable.ic_correct
    };


    public String[] slide_headings = {
            "Welcome to InterAct",
            "Promote Growth with Feedback!",
            "Promote Synergy with Appreciations!"
    };

    public String[] slide_descriptions = {
            "This is an amazing app intro statement!",
            "This message will probably explain how feedback works in InterAct",
            "This message will probably talk about appreciations. Meh. Who knows?"
    };

    @Override
    public int getCount() {
        return slide_headings.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    public Object instantiateItem(ViewGroup container, int position){
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
        View view = mLayoutInflater.inflate(R.layout.slide_layout, container, false);

        ImageView slideImageView = view.findViewById(R.id.slide_image_view);
        TextView slideHeading = view.findViewById(R.id.slide_heading);
        TextView slideDescription = view.findViewById(R.id.slide_description);

        slideImageView.setImageResource(slide_images[position]);
        slideHeading.setText(slide_headings[position]);
        slideDescription.setText(slide_descriptions[position]);

        if(position == 0){
            slideImageView.setScaleX(2);
            slideImageView.setScaleY(2);
        }

        container.addView(view);

        return view;
    }

    public void destroyItem(ViewGroup container, int position, Object object){
        container.removeView((RelativeLayout) object);
    }
}
