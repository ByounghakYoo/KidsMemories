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
 * A simple {@link Fragment} subclass.
 * Use the {@link ImmunizationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ImmunizationFragment extends Fragment {
    int kidId;
    Bundle para;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ImmunizationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ImmunizationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ImmunizationFragment newInstance(String param1, String param2) {
        ImmunizationFragment fragment = new ImmunizationFragment();
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

        btnCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalendarFragment fragment  = new CalendarFragment();
                fragment.setArguments(para);
                loadFragment(fragment);
                loadFragment(fragment);
            }
        });

        btnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new ListFragment());
            }
        });

        btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HistoryFragment fragment  = new HistoryFragment();
                fragment.setArguments(para);
                loadFragment(fragment);
            }
        });

        btnSchedules.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VaccinationScheduleFragment fragment  = new VaccinationScheduleFragment();
                fragment.setArguments(para);
                loadFragment(fragment);
            }
        });

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

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}