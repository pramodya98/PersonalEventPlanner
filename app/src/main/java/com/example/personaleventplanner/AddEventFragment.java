package com.example.personaleventplanner;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddEventFragment extends Fragment {

    private EditText editTextTitle, editTextCategory, editTextLocation;
    private TextView textViewSelectedDateTime;
    private long selectedDateTimeMillis = -1;
    private EventViewModel eventViewModel;

    // ✅ To check if updating
    private int eventId = -1;

    public AddEventFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_event, container, false);

        editTextTitle = view.findViewById(R.id.editTextTitle);
        editTextCategory = view.findViewById(R.id.editTextCategory);
        editTextLocation = view.findViewById(R.id.editTextLocation);
        textViewSelectedDateTime = view.findViewById(R.id.textViewSelectedDateTime);

        Button buttonPickDateTime = view.findViewById(R.id.buttonPickDateTime);
        Button buttonSaveEvent = view.findViewById(R.id.buttonSaveEvent);

        eventViewModel = new ViewModelProvider(requireActivity()).get(EventViewModel.class);

        // ✅ RECEIVE DATA FOR UPDATE
        Bundle args = getArguments();
        if (args != null) {
            eventId = args.getInt("id");

            editTextTitle.setText(args.getString("title"));
            editTextCategory.setText(args.getString("category"));
            editTextLocation.setText(args.getString("location"));

            selectedDateTimeMillis = args.getLong("dateTime");

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault());
            textViewSelectedDateTime.setText(sdf.format(selectedDateTimeMillis));
        }

        buttonPickDateTime.setOnClickListener(v -> showDateTimePicker());
        buttonSaveEvent.setOnClickListener(v -> saveEvent());

        return view;
    }

    private void showDateTimePicker() {
        Calendar calendar = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                (view, year, month, dayOfMonth) -> {
                    Calendar selectedCalendar = Calendar.getInstance();
                    selectedCalendar.set(year, month, dayOfMonth);

                    TimePickerDialog timePickerDialog = new TimePickerDialog(
                            requireContext(),
                            (timeView, hourOfDay, minute) -> {
                                selectedCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                selectedCalendar.set(Calendar.MINUTE, minute);
                                selectedCalendar.set(Calendar.SECOND, 0);

                                selectedDateTimeMillis = selectedCalendar.getTimeInMillis();

                                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault());
                                textViewSelectedDateTime.setText(sdf.format(selectedCalendar.getTime()));
                            },
                            calendar.get(Calendar.HOUR_OF_DAY),
                            calendar.get(Calendar.MINUTE),
                            false
                    );
                    timePickerDialog.show();
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        datePickerDialog.show();
    }

    private void saveEvent() {
        String title = editTextTitle.getText().toString().trim();
        String category = editTextCategory.getText().toString().trim();
        String location = editTextLocation.getText().toString().trim();

        if (TextUtils.isEmpty(title)) {
            Toast.makeText(getContext(), "Title cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedDateTimeMillis == -1) {
            Toast.makeText(getContext(), "Please select date and time", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedDateTimeMillis < System.currentTimeMillis()) {
            Toast.makeText(getContext(), "Past dates are not allowed", Toast.LENGTH_SHORT).show();
            return;
        }

        Event event = new Event(title, category, location, selectedDateTimeMillis);

        if (eventId != -1) {
            // ✅ UPDATE
            event.setId(eventId);
            eventViewModel.update(event);
            Toast.makeText(getContext(), "Event updated", Toast.LENGTH_SHORT).show();
        } else {
            // ✅ INSERT
            eventViewModel.insert(event);
            Toast.makeText(getContext(), "Event saved", Toast.LENGTH_SHORT).show();
        }

        // Reset fields
        editTextTitle.setText("");
        editTextCategory.setText("");
        editTextLocation.setText("");
        textViewSelectedDateTime.setText("No date selected");
        selectedDateTimeMillis = -1;
        eventId = -1;
    }
}