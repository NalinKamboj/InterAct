package com.comakeit.inter_act;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.comakeit.inter_act.Activities.InteractionDetailActivity;

import java.util.List;

public class ReceivedInteractionAdapter extends RecyclerView.Adapter<ReceivedInteractionAdapter.MyViewHolder> {
    private List<Interaction> mInteractionList;
    private Context mContext;
    private int type;       // 0 - received or 1 - sent

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView userName, eventName, message, iaContext, interactionDate;
        LinearLayout mLinearLayout, mBottomLinearLayout;
        LottieAnimationView mLottieStarView1, mLottieStarView2, mLottieStarView3;
        LinearLayout starLayout1, starLayout2, starLayout3;
        ValueAnimator mValueAnimator1, mValueAnimator2, mValueAnimator3;

        MyViewHolder(View view){
            super(view);
            iaContext = view.findViewById(R.id.interaction_row_context_text_view);
            interactionDate = view.findViewById(R.id.interaction_row_date);
            mLinearLayout = view.findViewById(R.id.received_interaction_row_layout);
            userName = view.findViewById(R.id.interaction_row_from_text_view);
            eventName = view.findViewById(R.id.interaction_row_event_text_view);
            message = view.findViewById(R.id.interaction_row_description_text_view);
            mBottomLinearLayout = view.findViewById(R.id.interaction_row_bottom_bar_linear_layout);

            ///Get lottie animation views
            mLottieStarView1 = view.findViewById(R.id.rating_lottie_star_1);
            mLottieStarView2 = view.findViewById(R.id.rating_lottie_star_2);
            mLottieStarView3 = view.findViewById(R.id.rating_lottie_star_3);

            //Get enclosing lin-layouts
            starLayout1 = view.findViewById(R.id.rating_star_layout_1);
            starLayout2 = view.findViewById(R.id.rating_star_layout_2);
            starLayout3 = view.findViewById(R.id.rating_star_layout_3);

            //Create animators for all Lottie Animations
            mValueAnimator1 = ValueAnimator.ofFloat(0f, 1f).setDuration(1500);
            mValueAnimator2 = ValueAnimator.ofFloat(0f, 1f).setDuration(1500);
            mValueAnimator3 = ValueAnimator.ofFloat(0f, 1f).setDuration(1500);
            //Create animation update methods
            mValueAnimator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {  //Star 1
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    mLottieStarView1.setProgress((Float) valueAnimator.getAnimatedValue());
                }
            });
            mValueAnimator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {  //Star 2
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    mLottieStarView2.setProgress((Float) valueAnimator.getAnimatedValue());
                }
            });
            mValueAnimator3.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {  //Star 3
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    mLottieStarView3.setProgress((Float) valueAnimator.getAnimatedValue());
                }
            });

//        set listeners on each star view
            starLayout1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Pausing is essential to set progress to zero in case an animation is already under way while being clicked
                    mValueAnimator2.pause();
                    mValueAnimator3.pause();
                    mLottieStarView2.setProgress(0f);
                    mLottieStarView3.setProgress(0f);

                    if(mLottieStarView1.getProgress() != 1f)
                        mValueAnimator1.start();
                }
            });
            starLayout2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mLottieStarView2.getProgress() != 1f)
                        mValueAnimator2.start();
                    if(mLottieStarView1.getProgress() != 1f)
                        mValueAnimator1.start();

                    mValueAnimator3.pause();
                    mLottieStarView3.setProgress(0f);
                }
            });
            starLayout3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mLottieStarView1.getProgress() != 1f)
                        mValueAnimator1.start();
                    if(mLottieStarView2.getProgress() != 1f)
                        mValueAnimator2.start();
                    if(mLottieStarView3.getProgress() != 1f)
                        mValueAnimator3.start();
                }
            });
        }
    }

    public ReceivedInteractionAdapter(Context context, List<Interaction> interactionList, int type) {
        this.mInteractionList = interactionList;
        this.mContext = context;
        this.type = type;
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.received_interaction_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        final Interaction interaction = mInteractionList.get(position);
        if(interaction.isAnonymous())
            holder.userName.setText("Anonymous");
        else{
            String name = (type == 0) ? GeneralUser.sUserHashMap.get(interaction.getFromUserId()).getFirstName() + " "
                    + GeneralUser.sUserHashMap.get(interaction.getFromUserId()).getLastName() : GeneralUser.sUserHashMap.get(interaction.getToUserId()).getFirstName() + " "
                    + GeneralUser.sUserHashMap.get(interaction.getToUserId()).getLastName();
            holder.userName.setText(name);
        }
        holder.eventName.setText(interaction.getEventName());
        holder.message.setText(interaction.getObservation().trim());
        holder.iaContext.setText(interaction.getContext());
        holder.interactionDate.setText(interaction.getEventDate());

        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), InteractionDetailActivity.class);
                intent.putExtra("parcel_interaction", interaction);
                mContext.getApplicationContext().startActivity(intent);
            }
        });
//
        if(interaction.getType() == 1){
//            holder.mLinearLayout.setBackground(mContext.getDrawable(R.drawable.rounded_corner_green));
            holder.mBottomLinearLayout.setBackground(mContext.getDrawable(R.drawable.rounded_bottom_green));
        }
    }

    @Override
    public int getItemCount() {
        return mInteractionList.size();
    }
}
