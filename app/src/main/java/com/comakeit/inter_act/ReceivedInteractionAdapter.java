package com.comakeit.inter_act;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.comakeit.inter_act.Activities.InteractionDetailActivity;

import java.util.Calendar;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class ReceivedInteractionAdapter extends RecyclerView.Adapter<ReceivedInteractionAdapter.MyViewHolder> {
    private List<Interaction> mInteractionList;
    private Context mContext;
    private int FLAG;       // 0 - received or 1 - sent
    private ImageView mStarImageView1, mStarImageView2, mStarImageView3;
    private LottieAnimationView mLottieStarView1, mLottieStarView2, mLottieStarView3;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView userName, eventName, message, iaContext, interactionDate;
        LinearLayout mLinearLayout, mBottomLinearLayout;
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

            //Get star ImageViews for Sent InterActions
            mStarImageView1 = view.findViewById(R.id.rating_star_1);
            mStarImageView2 = view.findViewById(R.id.rating_star_2);
            mStarImageView3 = view.findViewById(R.id.rating_star_3);

            //Get enclosing lin-layouts
            starLayout1 = view.findViewById(R.id.rating_star_layout_1);
            starLayout2 = view.findViewById(R.id.rating_star_layout_2);
            starLayout3 = view.findViewById(R.id.rating_star_layout_3);

            //Animation views will only be created if it is a received Interaction
            if(FLAG == 0){
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

                //set listeners on each star view
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

                        sendRating(1, getAdapterPosition());
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

                        sendRating(2, getAdapterPosition());
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

                        sendRating(3, getAdapterPosition());
                    }
                });
            } else {
                mLottieStarView1.setVisibility(View.GONE);
                mLottieStarView2.setVisibility(View.GONE);
                mLottieStarView3.setVisibility(View.GONE);
            }

        }
    }

    public ReceivedInteractionAdapter(Context context, List<Interaction> interactionList, int type) {
        this.mInteractionList = interactionList;
        this.mContext = context;
        this.FLAG = type;
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
            String name = (FLAG == 0) ? GeneralUser.sUserHashMap.get(interaction.getFromUserId()).getFirstName() + " "
                    + GeneralUser.sUserHashMap.get(interaction.getFromUserId()).getLastName() : GeneralUser.sUserHashMap.get(interaction.getToUserId()).getFirstName() + " "
                    + GeneralUser.sUserHashMap.get(interaction.getToUserId()).getLastName();
            holder.userName.setText(name);
        }
        holder.eventName.setText(Utilities.toCamelCase(interaction.getEventName()));
        holder.message.setText(interaction.getObservation().trim());
        holder.iaContext.setText(Utilities.toCamelCase(interaction.getContext()));
        holder.interactionDate.setText(interaction.getEventDate());

        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), InteractionDetailActivity.class);
                Log.e("RECV ADAPTER", "INTERACTION ID - " + interaction.getInteractionID());
                intent.putExtra("parcel_interaction", interaction);
                mContext.getApplicationContext().startActivity(intent);
            }
        });
//
        if(interaction.getType() == 1){
//            holder.mLinearLayout.setBackground(mContext.getDrawable(R.drawable.rounded_corner_green));
            holder.mBottomLinearLayout.setBackground(mContext.getDrawable(R.drawable.rounded_bottom_green));
        }

//        TODO Enable this code when Server is updated to support ratings...
        if(FLAG == 1){
            Log.i("IA ADAPTER", "ID - " + interaction.getInteractionID() + "Rating  -  " + interaction.getRating());
            switch (interaction.getRating()){
                case 1:
                    mStarImageView1.setVisibility(View.GONE);
                    mStarImageView2.setVisibility(View.GONE);
                    mStarImageView3.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    mStarImageView1.setVisibility(View.GONE);
                    mStarImageView2.setVisibility(View.VISIBLE);
                    mStarImageView3.setVisibility(View.VISIBLE);
                    break;
                case 3:
                    mStarImageView1.setVisibility(View.VISIBLE);
                    mStarImageView2.setVisibility(View.VISIBLE);
                    mStarImageView3.setVisibility(View.VISIBLE);
                    break;
            }
        } else {
            switch (interaction.getRating()){
                case 1:
                    mLottieStarView1.setProgress(1f);
                    break;
                case 2:
                    mLottieStarView1.setProgress(1f);
                    mLottieStarView2.setProgress(1f);
                    break;
                case 3:
                    mLottieStarView1.setProgress(1f);
                    mLottieStarView2.setProgress(1f);
                    mLottieStarView3.setProgress(1f);
                    break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return mInteractionList.size();
    }

    private void sendRating(int rating, int position){
        Interaction interaction = mInteractionList.get(position);
        interaction.setRating(rating);
        interaction.setAcknowledgementDate(Calendar.getInstance().getTime());

        Bundle bundle = new Bundle();
        bundle.putString("MAIN_URL", mContext.getResources().getString(R.string.app_base_url));
        bundle.putParcelable("interaction", interaction);
        UpdateInteractionAsync updateInteractionAsync = new UpdateInteractionAsync(new UpdateInteractionAsync.UpdateInterface() {
            @Override
            public void processFinish(int response) {
                switch (response){
                    case 200:
                        Toasty.success(mContext, "Rating Sent!", Toast.LENGTH_SHORT, true).show();
                        break;
                    case 204:
                        Toasty.success(mContext, "Rating Sent!", Toast.LENGTH_SHORT, true).show();
                        break;
                    case 500:
                        Toasty.error(mContext, "Rating not sent - Internal Server Error", Toast.LENGTH_LONG, true).show();
                        break;
                    default:
                        Toasty.error(mContext.getApplicationContext(), "Rating not sent - Unknown Error (" + response + ")", Toast.LENGTH_SHORT, true).show();
                        break;
                }
            }
        });
        updateInteractionAsync.execute(bundle);
    }
}
