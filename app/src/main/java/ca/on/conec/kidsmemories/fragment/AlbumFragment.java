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

    public AlbumFragment() {
        // Required empty public constructor
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

                ArrayList<Album> postImgList = dao.getPostImgList(kidId , edtFromdate.getText().toString() , edtTodate.getText().toString());

                mList = postImgList;  
                /*
                if(postImgList != null) {
                    Log.d("info" , "KKKKKKKKKKK");
                    for (int i = 0; i < postImgList.size(); i++) {
                        String link = postImgList.get(i);
                        Log.i("info", ">>>" + link);
                    }
                }

                 */

                /*
                for(Post object : postArrayList ) {
                    String swdate = object.getWriteDate().toString();
                    int wdate = Integer.parseInt(swdate.substring(0,4))+Integer.parseInt(swdate.substring(5,7)) + Integer.parseInt(swdate.substring(8,10));
                    if (wdate >= ssdate && wdate <= sedate) {
                        if(object.getPhotoLink() != null && !"".equals(object.getPhotoLink())) {
                            date.add(object.getPhotoLink());
                        }
                    }
                }


                Album album = new Album();
                int count = 0;
                for (String link : date) {
                    Log.d("info" , ">>>>>" + link);
                    if (count % 2 == 0) {
                        album = new Album();
                        album.setImage1(link);
                        count++;
                    } else {
                        album.setImage2(link);
                        count++;
                    }
                    if (date.size() != count && count % 2 == 0) {
                        mList.add(album);
                    } else if (date.size() == count) {
                        mList.add(album);
                    }
                }
                */

                if(postImgList == null) {
                    mrecyclerView.setAdapter(null);
                } else {
                    bindAdapter();
                }
                /*
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
                mrecyclerView.setLayoutManager(layoutManager);

                albumListAdapter = new AlbumListAdapter(mList, getContext());

                mrecyclerView.setAdapter(albumListAdapter);
                albumListAdapter.notifyDataSetChanged();
                */
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

    @Override
    public void onDestroyView() {
        if(mrecyclerView != null) {
            mrecyclerView.setAdapter(null);
        }
        super.onDestroyView();
    }



}