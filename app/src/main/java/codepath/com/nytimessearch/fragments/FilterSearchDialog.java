package codepath.com.nytimessearch.fragments;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import codepath.com.nytimessearch.R;
import codepath.com.nytimessearch.models.FilterData;


public class FilterSearchDialog extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    EditText etDate;
    Spinner spOrder;
    CheckBox cbArts;
    CheckBox cbFashion;
    CheckBox cbSports;
    Button btnSave;

    private static SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy");

    private Calendar cal = Calendar.getInstance();


    public FilterSearchDialog() {

    }

    public static FilterSearchDialog newInstance() {
        FilterSearchDialog dialog = new FilterSearchDialog();
        return dialog;
    }


    public interface FilteredSearchListener {
        void filterResults(FilterData filter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_fragment_filter_search, container);
        getDialog().setTitle(R.string.filter_dialog_name);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        spOrder = (Spinner) view.findViewById(R.id.spOrder);
        etDate = (EditText) view.findViewById(R.id.etDate);
        cbArts = (CheckBox) view.findViewById(R.id.cbArts);
        cbFashion = (CheckBox) view.findViewById(R.id.cbFashion);
        cbSports = (CheckBox) view.findViewById(R.id.cbSports);
        btnSave = (Button) view.findViewById(R.id.btnSave);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.search_order, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spOrder.setAdapter(adapter);

        //Set current date to EditText field
        String date = sdf.format(cal.getTime());
        etDate.setText(date);

        etDate.setOnClickListener(new EditText.OnClickListener() {
            @Override
            public void onClick(View v) {
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dp = new DatePickerDialog(getActivity(), FilterSearchDialog.this, year, month, day);
                dp.show();

            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FilteredSearchListener listener = (FilteredSearchListener) getActivity();
                String order = (String) spOrder.getSelectedItem();
                FilterData filter = new FilterData(cal, FilterData.Order.valueOf(order.toUpperCase()));

                if (cbArts.isChecked()) {
                    filter.setArts(true);
                }

                if (cbSports.isChecked()) {
                    filter.setSports(true);
                }

                if (cbFashion.isChecked()) {
                    filter.setFashion(true);
                }

                listener.filterResults(filter);
                dismiss();

            }
        });

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        cal.set(year, month, dayOfMonth);
        String date = sdf.format(cal.getTime());
        etDate.setText(date);
    }

}
