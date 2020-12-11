package ca.on.conec.kidsmemories.fragment;

import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.Calendar;
import java.util.List;

import ca.on.conec.kidsmemories.DatabaseHelper;
import ca.on.conec.kidsmemories.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CalendarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalendarFragment extends Fragment {
    int year, month, day;
    Boolean insertState;
    String memoDate;
    DatabaseHelper dbh;
    EditText edtMemo;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CalendarFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CalendarFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CalendarFragment newInstance(String param1, String param2) {
        CalendarFragment fragment = new CalendarFragment();
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
        View v = inflater.inflate(R.layout.fragment_calendar, container, false);

        final CalendarView calendarView;
        Button btnSave;
        Button btnDelete;

        Calendar currentdate = Calendar.getInstance();
        year = currentdate.get(Calendar.YEAR);
        month = currentdate.get(Calendar.MONTH) + 1;
        day = currentdate.get(Calendar.DAY_OF_MONTH);

        calendarView = (CalendarView) v.findViewById(R.id.calendarView);
        edtMemo = (EditText) v.findViewById(R.id.editMemo);
        btnSave = (Button) v.findViewById(R.id.btnSave);
        btnDelete = (Button) v.findViewById(R.id.btnDelete);

        // Create an instance of the database handler class
        dbh = new DatabaseHelper(getContext());
        edtMemo.getText().clear();

        memoDate = String.format("%02d", day)+String.format("%02d", month)+String.format("%04d", year);
        Cursor cursor = dbh.ViewData(memoDate,"Calendar");
        if(cursor.getCount() > 0) {
            edtMemo.setText(cursor.getString(2));

        }

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                edtMemo.getText().clear();
                year = year;
                month = month+1;
                day = dayOfMonth;
                memoDate = String.format("%02d", day)+String.format("%02d", month)+String.format("%04d", year);
                Cursor cursor = dbh.ViewData(memoDate,"Calendar");
                if(cursor.getCount() > 0){
                    edtMemo.setText(cursor.getString(2));
                }
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String memo = edtMemo.getText().toString();
                insertState = dbh.Insert(memoDate, memo);
                if(insertState == true){
                    Toast.makeText(getContext(), "Memo was saved",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getContext(), "Memo was not saved",Toast.LENGTH_LONG).show();
                }
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertState = dbh.DeleteData(memoDate);
                if(insertState == true){
                    edtMemo.getText().clear();
                    Toast.makeText(getContext(), "Memo was deleted",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getContext(), "Memo was not deleted",Toast.LENGTH_LONG).show();
                }
            }
        });

        return v;
    }
}