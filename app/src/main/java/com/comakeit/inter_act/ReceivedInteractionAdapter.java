package com.comakeit.inter_act;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.comakeit.inter_act.Activities.InteractionDetailActivity;

import java.util.List;

public class ReceivedInteractionAdapter extends RecyclerView.Adapter<ReceivedInteractionAdapter.MyViewHolder> {
    private List<Interaction> mInteractionList;
    private Context mContext;
    private int type;       // 0 - received or 1 - sent

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView userName, eventName, message, iaContext, interactionDate;
        LinearLayout mLinearLayout, mBottomLinearLayout;

        MyViewHolder(View view){
            super(view);
            iaContext = view.findViewById(R.id.interaction_row_context_text_view);
            interactionDate = view.findViewById(R.id.interaction_row_date);
            mLinearLayout = view.findViewById(R.id.received_interaction_row_layout);
            userName = view.findViewById(R.id.interaction_row_from_text_view);
            eventName = view.findViewById(R.id.interaction_row_event_text_view);
            message = view.findViewById(R.id.interaction_row_description_text_view);
            mBottomLinearLayout = view.findViewById(R.id.interaction_row_bottom_bar_linear_layout);
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
//        String iaDateString = interaction.getIACalendar().get(Calendar.DAY_OF_MONTH) + "-" + interaction.getIACalendar().get(Calendar.MONTH) + "-" +
//                + interaction.getIACalendar().get(Calendar.YEAR) + " " + interaction.getIACalendar().get(Calendar.HOUR_OF_DAY) + ":" +
//                interaction.getIACalendar().get(Calendar.MINUTE);
        holder.interactionDate.setText(interaction.getEventDate());
//
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
            holder.mLinearLayout.setBackground(mContext.getDrawable(R.drawable.rounded_corner_green));
            holder.mBottomLinearLayout.setBackground(mContext.getDrawable(R.drawable.rounded_bottom_green));
//        } else {
//            String parts[] = interaction.getDescription().split("Suggestion:");
//            holder.message.setText(parts[0].trim());
        }
    }

    @Override
    public int getItemCount() {
        return mInteractionList.size();
    }
}
