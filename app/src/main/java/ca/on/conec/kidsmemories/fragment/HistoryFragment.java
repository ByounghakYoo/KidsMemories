package ca.on.conec.kidsmemories.fragment;

import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Calendar;

import ca.on.conec.kidsmemories.DatabaseHelper;
import ca.on.conec.kidsmemories.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HistoryFragment extends Fragment {
    DatabaseHelper dbh;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HistoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HistoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HistoryFragment newInstance(String param1, String param2) {
        HistoryFragment fragment = new HistoryFragment();
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
        View v = inflater.inflate(R.layout.fragment_history, container, false);

        // Create an instance of the database handler class
        dbh = new DatabaseHelper(getContext());
        TextView myHistory;
        StringBuffer bfr = new StringBuffer();

        myHistory = (TextView) v.findViewById(R.id.textHistory);

        Cursor cursor = dbh.ViewData("", "History");
        if(cursor.getCount() > 0){
            if(cursor.moveToFirst()){
                do{
                    // Append rows to StringBuffer to show records
                    String memoDate = cursor.getString(1);
                    String day = memoDate.substring(0,2);
                    String month = memoDate.substring(2,4);
                    String year = memoDate.substring(4);

                    String memo = cursor.getString(2);
                    bfr.append(" • " + day + "-" + month + "-" + year + " : " + memo + "\n");
                }while(cursor.moveToNext());
            }
        }

        myHistory.setText(bfr.toString());
        return v;
    }
}