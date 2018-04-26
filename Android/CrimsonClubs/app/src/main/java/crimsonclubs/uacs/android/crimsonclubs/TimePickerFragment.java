package crimsonclubs.uacs.android.crimsonclubs;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import java.util.Calendar;

import mehdi.sakout.fancybuttons.FancyButton;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // Do something with the time chosen by the user
        FancyButton btn = (FancyButton)getActivity().findViewById(R.id.btnStartDate);
        String old = btn.getText().toString();

        String minHand, hourHand;
        if (minute < 10)
            minHand = "0" + String.valueOf(minute);
        else
            minHand = String.valueOf(minute);

        if (hourOfDay < 10)
            hourHand = "0" + String.valueOf(hourOfDay);
        else
            hourHand = String.valueOf(hourOfDay);

        old += "T" + hourHand + ":" + minHand + ":00";
        btn.setText(old);
    }

}
