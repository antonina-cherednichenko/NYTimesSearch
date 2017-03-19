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

import org.parceler.Parcels;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import codepath.com.nytimessearch.R;
import codepath.com.nytimessearch.models.FilterData;


public class FilterSearchDialog extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    @BindView(R.id.etDate) EditText etDate;
    @BindView(R.id.spOrder) Spinner spOrder;
    @BindView(R.id.cbArts) CheckBox cbArts;
    @BindView(R.id.cbFashion) CheckBox cbFashion;
    @BindView(R.id.cbSports) CheckBox cbSports;
    @BindView(R.id.btnSave) Button btnSave;

    private static SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy");
    private static final String FILTER_VALUE = "filter";
    private FilterData filter;


    public FilterSearchDialog() {

    }

    public static FilterSearchDialog newInstance(FilterData filter) {
        FilterSearchDialog dialog = new FilterSearchDialog();
        Bundle args = new Bundle();
        args.putParcelable(FILTER_VALUE, Parcels.wrap(filter));
        dialog.setArguments(args);
        return dialog;
    }


    public interface FilteredSearchListener {
        void filterResults(FilterData filter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_fragment_filter_search, container);
        ButterKnife.bind(this, rootView);
        getDialog().setTitle(R.string.filter_dialog_name);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        spOrder = (Spinner) view.findViewById(R.id.spOrder);
        // etDate = (EditText) view.findViewById(etDate);
//        cbArts = (CheckBox) view.findViewById(R.id.cbArts);
//        cbFashion = (CheckBox) view.findViewById(R.id.cbFashion);
//        cbSports = (CheckBox) view.findViewById(R.id.cbSports);
//        btnSave = (Button) view.findViewById(btnSave);

        filter = Parcels.unwrap(getArguments().getParcelable(FILTER_VALUE));

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.search_order, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spOrder.setAdapter(adapter);

        //set selected item in spinner
        for (int i = 0; i < adapter.getCount(); i++) {
            String item = (String) adapter.getItem(i);
            if (item.equals(filter.getOrder())) {
                spOrder.setSelection(i);
                break;
            }
        }

        //Set current date to EditText field
        if (filter.getCal() == null) {
            filter.setCal(Calendar.getInstance());
        }

        Calendar cal = filter.getCal();
        String date = sdf.format(cal.getTime());
        etDate.setText(date);

        //set topics from filter
        cbArts.setChecked(filter.isArts());
        cbSports.setChecked(filter.isSports());
        cbFashion.setChecked(filter.isFashion());


        etDate.setOnClickListener(v -> {
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog dp = new DatePickerDialog(getActivity(), FilterSearchDialog.this, year, month, day);
            dp.show();

        });

        btnSave.setOnClickListener(v -> {
            FilteredSearchListener listener = (FilteredSearchListener) getActivity();
            String order = (String) spOrder.getSelectedItem();
            FilterData filter1 = new FilterData(cal, order);

            if (cbArts.isChecked()) {
                filter1.setArts(true);
            }

            if (cbSports.isChecked()) {
                filter1.setSports(true);
            }

            if (cbFashion.isChecked()) {
                filter1.setFashion(true);
            }

            listener.filterResults(filter1);
            dismiss();

        });

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        filter.getCal().set(year, month, dayOfMonth);
        String date = sdf.format(filter.getCal().getTime());
        etDate.setText(date);
    }

}
