package ca.on.conec.kidsmemories.fragment;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.TreeSet;

import ca.on.conec.kidsmemories.DatabaseHelper;
import ca.on.conec.kidsmemories.DisplayVaccinationDate;
import ca.on.conec.kidsmemories.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ScheduleListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScheduleListFragment extends Fragment {
    DatabaseHelper dbh;
    String pCode;
    TextView scheduleList;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ScheduleListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ScheduleListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ScheduleListFragment newInstance(String param1, String param2) {
        ScheduleListFragment fragment = new ScheduleListFragment();
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
        View v = inflater.inflate(R.layout.fragment_schedule_list, container, false);
        String[] provinceList = {"Alberta", "British Columbia", "Manitoba", "New Brunswick",
                "Newfoundland and Labrador", "Northwest Territories", "Nova Scotia",
                "Nunavut", "Ontario", "Prince Edward Island", "Quebec",
                "Saskatchewan", "Yukon"};

        final String[] provinceCode = {"AB", "BC", "MB", "NB", "NL", "NT", "NS", "NU", "ON", "PE", "QC", "SK", "YT"};
        Spinner spinProvince;

        // Create an instance of the database handler class
        dbh = new DatabaseHelper(getContext());

        scheduleList = (TextView) v.findViewById(R.id.textSchedule);
        spinProvince = (Spinner) v.findViewById(R.id.spinner);
        // Create the instance of ArrayAdapter having the list of province
        ArrayAdapter adpProvince = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,provinceList );
        // set a simple layout resource file for each item of the spinner
        adpProvince.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching size adapter to spinner
        spinProvince.setAdapter(adpProvince);

        spinProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pCode = provinceCode[position];
                String data = RetrieveDate(pCode);
                scheduleList.setText("");
                scheduleList.setText(data);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return v;
    }

    private String RetrieveDate(String provinceCode) {
        StringBuffer bfr = new StringBuffer();
        Cursor cursor1 = dbh.RetrieveVaccinationData(provinceCode);
        if(cursor1.getCount() > 0) {
            if (cursor1.moveToFirst()) {
                do {
                    // Append rows to StringBuffer to show records
                    String vaccines = cursor1.getString(2);
                    int first = cursor1.getInt(3);
                    int second = cursor1.getInt(4);
                    int third = cursor1.getInt(5);
                    int fourth = cursor1.getInt(6);
                    int fifth = cursor1.getInt(7);

                    String period = "";
                    if (first != 0) period = Integer.toString(first);
                    if (second != 0) period = period + ", " + Integer.toString(second);
                    if (third != 0) period = period + ", " + Integer.toString(third);
                    if (fourth != 0) period = period + ", " + Integer.toString(fourth);
                    if (fifth != 0) period = period + ", " + Integer.toString(fifth);

                    bfr.append(" • " + vaccines + " : " + period + " months \n");
                } while (cursor1.moveToNext());
            }
        }
        return bfr.toString();
    }

}