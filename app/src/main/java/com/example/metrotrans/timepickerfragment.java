package com.example.metrotrans;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class timepickerfragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Calendar C = Calendar.getInstance();
        int hour = C.get(Calendar.HOUR_OF_DAY);
        int Min = C.get(Calendar.MINUTE);

         return new TimePickerDialog(getActivity(), (TimePickerDialog.OnTimeSetListener)getActivity(),hour, Min, DateFormat.is24HourFormat(getActivity()));
    }
}
