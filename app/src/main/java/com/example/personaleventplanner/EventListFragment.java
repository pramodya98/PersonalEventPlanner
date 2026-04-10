package com.example.personaleventplanner;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class EventListFragment extends Fragment {

    private EventViewModel eventViewModel;

    public EventListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_event_list, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewEvents);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        EventAdapter adapter = new EventAdapter();
        recyclerView.setAdapter(adapter);

        eventViewModel = new ViewModelProvider(requireActivity()).get(EventViewModel.class);
        eventViewModel.getAllEvents().observe(getViewLifecycleOwner(), adapter::setEvents);

        // ✅ DELETE (Swipe)
        ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView,
                                  RecyclerView.ViewHolder viewHolder,
                                  RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                Event event = adapter.getEventAt(viewHolder.getAdapterPosition());
                eventViewModel.delete(event);

                Toast.makeText(getContext(), "Event deleted", Toast.LENGTH_SHORT).show();
            }
        };

        new ItemTouchHelper(callback).attachToRecyclerView(recyclerView);

        // ✅ UPDATE (Click)
        adapter.setOnItemClickListener(event -> {
            Bundle bundle = new Bundle();
            bundle.putInt("id", event.getId());
            bundle.putString("title", event.getTitle());
            bundle.putString("category", event.getCategory());
            bundle.putString("location", event.getLocation());
            bundle.putLong("dateTime", event.getDateTime());

            AddEventFragment fragment = new AddEventFragment();
            fragment.setArguments(bundle);

            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
        });

        return view;
    }
}