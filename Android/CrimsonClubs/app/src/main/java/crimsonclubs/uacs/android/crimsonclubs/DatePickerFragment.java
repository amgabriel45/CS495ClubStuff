package crimsonclubs.uacs.android.crimsonclubs;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

import hirondelle.date4j.DateTime;
import mehdi.sakout.fancybuttons.FancyButton;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{
    /*
    public int bigYear;
    public int bigMonth;
    public int bigDay;
    */

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user

        FancyButton btn = (FancyButton)getActivity().findViewById(R.id.btnStartDate);
        btn.setText(String.valueOf(year) + "-" + String.valueOf(month) + "-" + String.valueOf(day));
        showTimePickerDialog(view);
    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        //newFragment.show(main.getSupportFragmentManager(), "timePicker");
        newFragment.show(getFragmentManager(), "timePicker");
    }

    /*
    public String getDateFromDialog(int year, int month, int day) {

        String date = "";
        date.concat(String.valueOf(month) + "/")
                .concat(String.valueOf(day) + "/")
                .concat(String.valueOf(year) + "/");

        return date;


    }
    */
}
