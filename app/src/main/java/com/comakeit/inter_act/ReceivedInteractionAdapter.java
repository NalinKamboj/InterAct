package com.comakeit.inter_act;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

public class ReceivedInteractionAdapter extends RecyclerView.Adapter<ReceivedInteractionAdapter.MyViewHolder> {
    private List<Interaction> mInteractionList;
    public Context mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView fromEmail, eventName, message, iaContext, interactionDate;
        public LinearLayout mLinearLayout, mBottomLinearLayout, mExpandingLayout;

        public MyViewHolder(View view){
            super(view);
//            mExpandingLayout = view.findViewById(R.id.interaction_row_collapsing_linear_layout);
            iaContext = view.findViewById(R.id.interaction_row_context_text_view);
            interactionDate = view.findViewById(R.id.interaction_row_date);
            mLinearLayout = view.findViewById(R.id.received_interaction_row_layout);
            fromEmail = view.findViewById(R.id.interaction_row_from_text_view);
            eventName = view.findViewById(R.id.interaction_row_event_text_view);
            message = view.findViewById(R.id.interaction_row_description_text_view);
            mBottomLinearLayout = view.findViewById(R.id.interaction_row_bottom_bar_linear_layout);
        }
    }

    public ReceivedInteractionAdapter(Context context, List<Interaction> interactionList) {
        this.mInteractionList = interactionList;
        this.mContext = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.received_interaction_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Interaction interaction = mInteractionList.get(position);
        holder.fromEmail.setText(interaction.getFromUserEmail());
        holder.eventName.setText(interaction.getEventName());
        holder.message.setText(interaction.getDescription());
        holder.iaContext.setText(interaction.getContext());
        String iaDateString = interaction.getIACalendar().get(Calendar.DAY_OF_MONTH) + "-" + interaction.getIACalendar().get(Calendar.MONTH) + "-" +
                + interaction.getIACalendar().get(Calendar.YEAR) + " " + interaction.getIACalendar().get(Calendar.HOUR_OF_DAY) + ":" +
                interaction.getIACalendar().get(Calendar.MINUTE);
        holder.interactionDate.setText(iaDateString);

        if(interaction.getIAType() == 1){
            holder.mLinearLayout.setBackground(mContext.getDrawable(R.drawable.rounded_corner_green));
            holder.mBottomLinearLayout.setBackground(mContext.getDrawable(R.drawable.rounded_bottom_green));
        }
    }

    @Override
    public int getItemCount() {
        return mInteractionList.size();
    }
}
