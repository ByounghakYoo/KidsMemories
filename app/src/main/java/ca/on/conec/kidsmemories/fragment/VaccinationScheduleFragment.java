package ca.on.conec.kidsmemories.fragment;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

import ca.on.conec.kidsmemories.DatabaseHelper;
import ca.on.conec.kidsmemories.DisplayVaccinationDate;
import ca.on.conec.kidsmemories.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VaccinationScheduleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VaccinationScheduleFragment extends Fragment {
    String pCode;
    DatabaseHelper dbh;
    MaterialCalendarView calendarView;
    DatePickerDialog dobPicker;
    EditText edtDob;
    Spinner spinProvince;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public VaccinationScheduleFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment VaccinationScheduleFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static VaccinationScheduleFragment newInstance(String param1, String param2) {
        VaccinationScheduleFragment fragment = new VaccinationScheduleFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_vaccination_schedule, container, false);

        String[] provinceList = {"Alberta", "British Columbia", "Manitoba", "New Brunswick",
                "Newfoundland and Labrador", "Northwest Territories", "Nova Scotia",
                "Nunavut", "Ontario", "Prince Edward Island", "Quebec",
                "Saskatchewan", "Yukon"};

        final String[] provinceCode = {"AB", "BC", "MB", "NB", "NL", "NT", "NS", "NU", "ON", "PE", "QC", "SK", "YT"};

        dbh = new DatabaseHelper(getContext());
        calendarView = (MaterialCalendarView) v.findViewById(R.id.mcalendarView);
        spinProvince = (Spinner) v.findViewById(R.id.spinner);
        edtDob = (EditText) v.findViewById(R.id.editTextBirthday);

        // Create the instance of ArrayAdapter having the list of province
        ArrayAdapter adpProvince = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,provinceList );
        // set a simple layout resource file for each item of the spinner
        adpProvince.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching size adapter to spinner
        spinProvince.setAdapter(adpProvince);
        spinProvince.setEnabled(false);

        spinProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pCode = provinceCode[position];
                spinProvince.setEnabled(true);
                ReTrieveData(pCode, calendarView);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        edtDob.setInputType(InputType.TYPE_NULL);
        edtDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cal = Calendar.getInstance();
                int dayBirth = cal.get(Calendar.DAY_OF_MONTH);
                int monthBirth = cal.get(Calendar.MONTH);
                int yearBirth = cal.get(Calendar.YEAR);

                dobPicker = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String date = String.format("%02d", dayOfMonth)+"/"+String.format("%02d", (month+1))+"/"+String.format("%04d", year);
                       // edtDob.setText(dayOfMonth+"/"+(month+1)+"/"+year);
                        edtDob.setText(date);
                    }
                },yearBirth, monthBirth, dayBirth);
                dobPicker.show();
            }
        });

        edtDob.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                ReTrieveData(pCode, calendarView);
            }
        });
        return v;
    }

    private void ReTrieveData(String pCode, MaterialCalendarView calendarView) {
        Cursor cursor1 = dbh.RetrieveVaccinationData(pCode);
        List<CalendarDay> list;
        List<Integer> month;
        if(cursor1.getCount() > 0){
            month = new ArrayList<>();
            list = new ArrayList<CalendarDay>();
            if(cursor1.moveToFirst()){
                do{
                    // Append rows to StringBuffer to show records
                    String vaccines = cursor1.getString(1);
                    int first = cursor1.getInt(2);
                    int second = cursor1.getInt(3);
                    int third = cursor1.getInt(4);
                    int fourth = cursor1.getInt(5);
                    int fifth = cursor1.getInt(6);

                    if(first != 0 ) month.add(first);
                    if(second != 0) month.add(second);
                    if(third != 0) month.add(third);
                    if(fourth != 0) month.add(fourth);
                    if(fifth != 0) month.add(fifth);

                }while(cursor1.moveToNext());
            }

            TreeSet<Integer> treeSet = new TreeSet<Integer>(month);
            month.clear();
            month = new ArrayList<>(treeSet);

            //String birthday = "12102020";
            String birthday = edtDob.getText().toString();

            int day_b = 0,month_b = 0,year_b = 0;
            if(birthday.isEmpty() || birthday == null){
                Calendar cal = Calendar.getInstance();
                day_b = cal.get(Calendar.DAY_OF_MONTH);
                month_b = cal.get(Calendar.MONTH);
                year_b = cal.get(Calendar.YEAR);
            }else{
                day_b = Integer.parseInt(birthday.substring(0,2));
                month_b = Integer.parseInt(birthday.substring(3,5));
                year_b = Integer.parseInt(birthday.substring(6));
            }

            Collections.sort(month);
            Calendar cal_s = Calendar.getInstance();
            Calendar cal_e = Calendar.getInstance();

            for (Integer object: month) {
                cal_s.set(year_b, month_b, day_b);
                cal_e.set(year_b, month_b, day_b);
                cal_s.add(cal_s.MONTH, object-1);
                cal_e.add(cal_e.MONTH, object);
                while(cal_s.compareTo( cal_e ) !=1){
                    CalendarDay calendarDay = CalendarDay.from(cal_s);
                    list.add(calendarDay);
                    cal_s.add(cal_s.DAY_OF_MONTH,1);
                }
            }
            calendarView.addDecorator(new DisplayVaccinationDate(Color.BLUE, list));
        }
    }
}