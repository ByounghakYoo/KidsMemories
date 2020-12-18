package ca.on.conec.kidsmemories.fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ca.on.conec.kidsmemories.R;
import ca.on.conec.kidsmemories.adapter.AlbumListAdapter;
import ca.on.conec.kidsmemories.db.PostDAO;
import ca.on.conec.kidsmemories.entity.Album;
import ca.on.conec.kidsmemories.entity.Post;

//create an instance of this fragment
public class AlbumFragment extends Fragment {
    // from of date
    EditText edtFromdate;
    EditText edtTodate;     // end of date
    int ssdate;
    int sedate;
    DatePickerDialog datePicker;
    int kidId;              // kid id
    private PostDAO dao;
    ArrayList<Post> postArrayList;
    RecyclerView mrecyclerView;
    AlbumListAdapter albumListAdapter;

    // Rename and change types of parameters
    private List<Album> mList = new ArrayList<>();
    private List<String> date = new ArrayList<>();

    public AlbumFragment() {
        // Required empty public constructor
    }

    // Inflate the layout for this fragment
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

       View v =inflater.inflate(R.layout.fragment_album, container, false);

        Button btnRetrieve;
        // Retrieve action of button click
        edtFromdate = (EditText)v.findViewById(R.id.editTextFromdate);
        edtTodate = (EditText)v.findViewById(R.id.editTextTodate);
        btnRetrieve = (Button)v.findViewById(R.id.button);
        mrecyclerView = (RecyclerView)v.findViewById(R.id.recyclerView) ;
        // Get kidId
        kidId = getArguments().getInt("KID_ID");

        dao = new PostDAO(getActivity());
        postArrayList = new ArrayList<>();
        postArrayList = dao.getPostList(kidId);

        //set on the start of date
        edtFromdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get start of date
                final Calendar cal = Calendar.getInstance();
                int day = cal.get(Calendar.DAY_OF_MONTH);
                int month = cal.get(Calendar.MONTH);
                int year = cal.get(Calendar.YEAR);
                // show date picker
                datePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        edtFromdate.setText(year+"-"+(month+1)+"-"+dayOfMonth);
                        ssdate = year+(month+1)+dayOfMonth;
                    }
                },year, month, day);

                datePicker.show();

            }
        });
        // set on the end of date
        edtTodate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get end of date
                final Calendar cal = Calendar.getInstance();
                int day = cal.get(Calendar.DAY_OF_MONTH);
                int month = cal.get(Calendar.MONTH);
                int year = cal.get(Calendar.YEAR);
                //show date picker
                datePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        edtTodate.setText(year+"-"+(month+1)+"-"+dayOfMonth);
                        sedate = year+(month+1)+dayOfMonth;
                    }
                },year, month, day);

                datePicker.show();

            }
        });
        // get the album list from database
        btnRetrieve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayList<Album> postImgList = dao.getPostImgList(kidId , edtFromdate.getText().toString() , edtTodate.getText().toString());

                mList = postImgList;  


                if(postImgList == null) {
                    mrecyclerView.setAdapter(null);
                } else {
                    bindAdapter();
                }

            }
        });
        return v;
    }

    /**
     * binding recyclerView.
     */
    private void bindAdapter() {
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext() , 2);

        mrecyclerView.setLayoutManager(layoutManager);
        albumListAdapter = new AlbumListAdapter(mList, getContext());
        mrecyclerView.setAdapter(albumListAdapter);
        albumListAdapter.notifyDataSetChanged();
    }
   // after activity destroy view
    @Override
    public void onDestroyView() {
        if(mrecyclerView != null) {
            mrecyclerView.setAdapter(null);
        }
        super.onDestroyView();
    }



}