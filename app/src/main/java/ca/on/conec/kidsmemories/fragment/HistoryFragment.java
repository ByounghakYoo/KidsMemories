package ca.on.conec.kidsmemories.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Calendar;

import ca.on.conec.kidsmemories.MainActivity;
import ca.on.conec.kidsmemories.R;
import ca.on.conec.kidsmemories.db.ImmunizationDAO;

/**
 * create an instance of this fragment.
 */
public class HistoryFragment extends Fragment {
    ImmunizationDAO dbh;
    int mKidId;

    // Constructor
    public HistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_history, container, false);

        // Create an instance of the database handler class
        dbh = new ImmunizationDAO(getContext());

        TextView myHistory;
        StringBuffer bfr = new StringBuffer();

        myHistory = (TextView) v.findViewById(R.id.textHistory);

        // Save the selected kidId in the initialization screen
        mKidId = getArguments().getInt("KID_ID");

        // Retrieve for memo data corresponding to kidId
        Cursor cursor = dbh.ViewData("", "History", mKidId);
        if(cursor.getCount() > 0){
            if(cursor.moveToFirst()){
                do{
                    // Append rows to StringBuffer to show records
                    String memoDate = cursor.getString(1);
                    String memo = cursor.getString(2);
                    bfr.append(" • " + memoDate + " : " + memo + "\n");
                }while(cursor.moveToNext());
            }
        }

        myHistory.setText(bfr.toString());
        return v;
    }
}