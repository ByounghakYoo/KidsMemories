package ca.on.conec.kidsmemories.fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AlbumFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AlbumFragment extends Fragment {
    EditText edtFromdate;
    EditText edtTodate;
    int ssdate;
    int sedate;
    DatePickerDialog datePicker;
    int kidId;
    private PostDAO dao;
    ArrayList<Post> postArrayList;
    RecyclerView mrecyclerView;
    AlbumListAdapter albumListAdapter;


    private List<Album> mList = new ArrayList<>();
    private List<String> date = new ArrayList<>();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AlbumFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AlbumFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AlbumFragment newInstance(String param1, String param2) {
        AlbumFragment fragment = new AlbumFragment();
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
       View v =inflater.inflate(R.layout.fragment_album, container, false);

        Button btnRetrieve;

        edtFromdate = (EditText)v.findViewById(R.id.editTextFromdate);
        edtTodate = (EditText)v.findViewById(R.id.editTextTodate);
        btnRetrieve = (Button)v.findViewById(R.id.button);
        mrecyclerView = (RecyclerView)v.findViewById(R.id.recyclerView) ;
        // Get kidId
        kidId = getArguments().getInt("KID_ID");

        dao = new PostDAO(getActivity());
        postArrayList = new ArrayList<>();
        postArrayList = dao.getPostList(kidId);


        edtFromdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar cal = Calendar.getInstance();
                int day = cal.get(Calendar.DAY_OF_MONTH);
                int month = cal.get(Calendar.MONTH);
                int year = cal.get(Calendar.YEAR);

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

        edtTodate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar cal = Calendar.getInstance();
                int day = cal.get(Calendar.DAY_OF_MONTH);
                int month = cal.get(Calendar.MONTH);
                int year = cal.get(Calendar.YEAR);

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

        btnRetrieve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for(Post object : postArrayList ) {
                    String swdate = object.getWriteDate().toString();
                    int wdate = Integer.parseInt(swdate.substring(0,4))+Integer.parseInt(swdate.substring(5,7)) + Integer.parseInt(swdate.substring(8,10));
                    if (wdate >= ssdate && wdate <= sedate) {
                        date.add(object.getPhotoLink());
                    }
                }

                int count = 0;
                for (String link : date) {
                    Album album = new Album();
                    if (count % 2 == 0) {
                        album.setImage1(link);
                        count++;
                    } else {
                        album.setImage2(link);
                        count++;
                    }
                    if (date.size() != count - 1 && count % 2 == 0) {
                        mList.add(album);
                    } else if (date.size() == count - 1) {
                        mList.add(album);
                    }
                }
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
                mrecyclerView.setLayoutManager(layoutManager);

                albumListAdapter = new AlbumListAdapter(mList, getContext());

                mrecyclerView.setAdapter(albumListAdapter);
                albumListAdapter.notifyDataSetChanged();

            }
        });
        return v;
    }
}