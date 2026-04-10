package com.example.personaleventplanner;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventRepository {

    private EventDao eventDao;
    private LiveData<List<Event>> allEvents;

    // ✅ Background thread executor
    private static final ExecutorService databaseExecutor =
            Executors.newSingleThreadExecutor();

    public EventRepository(Application application) {
        EventDatabase database = EventDatabase.getInstance(application);
        eventDao = database.eventDao();
        allEvents = eventDao.getAllEvents();
    }

    // ✅ Insert
    public void insert(Event event) {
        databaseExecutor.execute(() -> eventDao.insert(event));
    }

    // ✅ Update
    public void update(Event event) {
        databaseExecutor.execute(() -> eventDao.update(event));
    }

    // ✅ Delete
    public void delete(Event event) {
        databaseExecutor.execute(() -> eventDao.delete(event));
    }

    // ✅ Get all events
    public LiveData<List<Event>> getAllEvents() {
        return allEvents;
    }
}