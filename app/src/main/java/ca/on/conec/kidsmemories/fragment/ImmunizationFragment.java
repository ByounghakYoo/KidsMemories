package ca.on.conec.kidsmemories.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import ca.on.conec.kidsmemories.MainActivity;
import ca.on.conec.kidsmemories.R;

/**
 * create an instance of this fragment.
 */
public class ImmunizationFragment extends Fragment {
    int kidId;
    Bundle para;

    //Constructor
    public ImmunizationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_immunization, container, false);

        Button btnCalendar;
        Button btnList;
        Button btnHistory;
        Button btnSchedules;
        Button btnScheduleList;

        btnCalendar = (Button) v.findViewById(R.id.btnCalendar);
        btnList = (Button) v.findViewById(R.id.btnList);
        btnHistory = (Button) v.findViewById(R.id.btnHistory);
        btnSchedules = (Button) v.findViewById(R.id.btnSchedule);
        btnScheduleList = (Button) v.findViewById(R.id.btnScheduleList);

        // Get kidId
        kidId = getArguments().getInt("KID_ID");
        para = new Bundle();
        para.putInt("KID_ID", kidId);

        // Perform when calendar button is clicked
        btnCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalendarFragment fragment  = new CalendarFragment();
                fragment.setArguments(para);
                loadFragment(fragment);
            }
        });

        // Perform when site button is clicked
        btnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new ListFragment());
            }
        });

        // Perform when my history button is clicked
        btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HistoryFragment fragment  = new HistoryFragment();
                fragment.setArguments(para);
                loadFragment(fragment);
            }
        });

        // Perform when immunization schedules(calendar) button is clicked
        btnSchedules.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VaccinationScheduleFragment fragment  = new VaccinationScheduleFragment();
                fragment.setArguments(para);
                loadFragment(fragment);
            }
        });

        // Perform when immunization schedule(list) button is clicked
        btnScheduleList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScheduleListFragment fragment  = new ScheduleListFragment();
                fragment.setArguments(para);
                loadFragment(fragment);
            }
        });

        return v;
    }

    //Perform the transaction
    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}