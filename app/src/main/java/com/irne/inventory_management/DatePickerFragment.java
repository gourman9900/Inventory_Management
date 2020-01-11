package com.irne.inventory_management;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.DateFormat;
import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{

    int year,month,date;
    EditText storage_mdate,storage_MaintainanceDate;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int date = c.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(getActivity(),this,year,month,date);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_barcode,container,false);
        storage_mdate = (EditText) view.findViewById(R.id.storage_mdate);
        storage_MaintainanceDate = (EditText) view.findViewById(R.id.storage_MaintainanceDate);
        return view;
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR,year);
        c.set(Calendar.MONTH,month);
        c.set(Calendar.DATE,date);
        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());



        //Boolean ismdate = getArguments().getBoolean("ismdate");

            storage_mdate.setText(currentDateString);
    }


}
