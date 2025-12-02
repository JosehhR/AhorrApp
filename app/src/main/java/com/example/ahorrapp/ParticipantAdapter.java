package com.example.ahorrapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ParticipantAdapter extends RecyclerView.Adapter<ParticipantAdapter.ParticipantViewHolder> {

    private final List<String> participants;
    private final OnParticipantRemoveListener listener;

    public interface OnParticipantRemoveListener {
        void onParticipantRemoved(int position);
    }

    public ParticipantAdapter(List<String> participants, OnParticipantRemoveListener listener) {
        this.participants = participants;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ParticipantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_participant, parent, false);
        return new ParticipantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ParticipantViewHolder holder, int position) {
        String participant = participants.get(position);
        holder.participantNameTextView.setText(participant);
        holder.removeParticipantButton.setOnClickListener(v -> listener.onParticipantRemoved(position));
    }

    @Override
    public int getItemCount() {
        return participants.size();
    }

    static class ParticipantViewHolder extends RecyclerView.ViewHolder {
        TextView participantNameTextView;
        ImageButton removeParticipantButton;

        public ParticipantViewHolder(@NonNull View itemView) {
            super(itemView);
            participantNameTextView = itemView.findViewById(R.id.participantNameTextView);
            removeParticipantButton = itemView.findViewById(R.id.removeParticipantButton);
        }
    }
}
