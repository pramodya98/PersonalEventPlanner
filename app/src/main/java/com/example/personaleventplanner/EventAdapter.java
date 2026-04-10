package com.example.personaleventplanner;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventHolder> {

    private List<Event> events = new ArrayList<>();

    // ✅ Click listener
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Event event);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public EventHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_item, parent, false);
        return new EventHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull EventHolder holder, int position) {
        Event currentEvent = events.get(position);

        holder.textViewTitle.setText(currentEvent.getTitle());
        holder.textViewCategory.setText("Category: " + currentEvent.getCategory());
        holder.textViewLocation.setText("Location: " + currentEvent.getLocation());

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault());
        String formattedDate = sdf.format(new Date(currentEvent.getDateTime()));
        holder.textViewDateTime.setText("Date: " + formattedDate);
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public void setEvents(List<Event> events) {
        this.events = events;
        notifyDataSetChanged();
    }

    // ✅ Needed for delete
    public Event getEventAt(int position) {
        return events.get(position);
    }

    class EventHolder extends RecyclerView.ViewHolder {

        private TextView textViewTitle;
        private TextView textViewCategory;
        private TextView textViewLocation;
        private TextView textViewDateTime;

        public EventHolder(@NonNull View itemView) {
            super(itemView);

            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewCategory = itemView.findViewById(R.id.textViewCategory);
            textViewLocation = itemView.findViewById(R.id.textViewLocation);
            textViewDateTime = itemView.findViewById(R.id.textViewDateTime);

            // ✅ Click handling
            itemView.setOnClickListener(v -> {
                if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                    listener.onItemClick(events.get(getAdapterPosition()));
                }
            });
        }
    }
}