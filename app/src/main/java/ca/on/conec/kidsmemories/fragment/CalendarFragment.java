package ca.on.conec.kidsmemories.fragment;

import android.content.Intent;
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
import ca.on.conec.kidsmemories.MainActivity;
import ca.on.conec.kidsmemories.R;
import ca.on.conec.kidsmemories.activity.AddKidActivity;

/**
 * create an instance of this fragment.
 */
public class CalendarFragment extends Fragment {
    int year, month, day;
    Boolean insertState;
    String memoDate;
    DatabaseHelper dbh;
    EditText edtMemo;
    int mKidId;

     // Constructor
    public CalendarFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_calendar, container, false);

        final CalendarView calendarView;
        Button btnSave;
        Button btnDelete;

        // Creating a calendar
        Calendar currentdate = Calendar.getInstance();
        year = currentdate.get(Calendar.YEAR);
        month = currentdate.get(Calendar.MONTH) + 1;
        day = currentdate.get(Calendar.DAY_OF_MONTH);

        calendarView = (CalendarView) v.findViewById(R.id.calendarView);
        edtMemo = (EditText) v.findViewById(R.id.editMemo);
        btnSave = (Button) v.findViewById(R.id.btnSave);
        btnDelete = (Button) v.findViewById(R.id.btnDelete);

        // Save the selected kidId in the initialization screen
        mKidId = getArguments().getInt("KID_ID");

        // Create an instance of the database handler class
        dbh = new DatabaseHelper(getContext());
        edtMemo.getText().clear();

        memoDate = String.format("%04d", year) + '-' + String.format("%02d", month) + '-' + String.format("%02d", day);
        // Retrieve the memo data of the corresponding date for kidid
        RetrieveMemoData(memoDate);

        // Called upon change of the selected day
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                edtMemo.getText().clear();
                year = year;
                month = month+1;
                day = dayOfMonth;

                memoDate = String.format("%04d", year) + '-' + String.format("%02d", month) + '-' + String.format("%02d", day);
                // Retrieve the memo data of the corresponding date for kidid
                RetrieveMemoData(memoDate);
            }
        });

        // Perform when Save button is clicked
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String memo = edtMemo.getText().toString();
                insertState = dbh.Insert(memoDate, memo,mKidId);
                if(insertState == true){
                    Toast.makeText(getContext(), "Memo was saved",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getContext(), "Memo was not saved",Toast.LENGTH_LONG).show();
                }
            }
        });

        // Perform when Delete button is clicked
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertState = dbh.DeleteData(memoDate, mKidId);
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

    //Retrieve the memo data of the corresponding date for kidid
    private void RetrieveMemoData(String memoDate) {
        Cursor cursor = dbh.ViewData(memoDate,"Calendar", mKidId);
        if(cursor.getCount() > 0) {
            edtMemo.setText(cursor.getString(2));
        }
    }
}