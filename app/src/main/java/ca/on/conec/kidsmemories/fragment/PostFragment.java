/**
 * FileName : PostFragment
 * Purpose : Related to Post list Fragment
 * Revision History :
 *          Created by Byunghak Yoo (Henry) 2020.12.10
 */
package ca.on.conec.kidsmemories.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import ca.on.conec.kidsmemories.R;
import ca.on.conec.kidsmemories.adapter.PostListAdapter;
import ca.on.conec.kidsmemories.db.PostDAO;
import ca.on.conec.kidsmemories.entity.Post;

public class PostFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<Post> postList = new ArrayList<Post>();
    private PostListAdapter postListAdapter;

    private PostDAO dao;

    /**
     * Constructor
     */
    public PostFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_post , container , false);

        FloatingActionButton addPostBtn = v.findViewById(R.id.addPost);

        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        dao = new PostDAO(getActivity());
        postList = dao.getPostList(1);


        if(postList.size() == 0) {
            Toast.makeText(getContext() , "No Records" , Toast.LENGTH_SHORT ).show();
        } else {
            bindAdapter();
        }

        dao.close();

        addPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Change Fragment Writing Post

                Fragment initFragment = new PostWriteFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.main_layout , initFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return v;

    }

    /**
     * binding recyclerView.
     */
    private void bindAdapter() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        postListAdapter = new PostListAdapter(postList , getContext());
        recyclerView.setAdapter(postListAdapter);
        postListAdapter.notifyDataSetChanged();

    }

}