/**
 * FileName : PostFragment
 * Purpose : Related to Post list Fragment
 * Revision History :
 *          Created by Byunghak Yoo (Henry) 2020.12.10
 */
package ca.on.conec.kidsmemories.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import ca.on.conec.kidsmemories.MyKidsActivity;
import ca.on.conec.kidsmemories.R;
import ca.on.conec.kidsmemories.activity.AddKidActivity;
import ca.on.conec.kidsmemories.activity.WritePostActivity;
import ca.on.conec.kidsmemories.adapter.KidsListAdapter;
import ca.on.conec.kidsmemories.adapter.PostListAdapter;
import ca.on.conec.kidsmemories.db.PostDAO;
import ca.on.conec.kidsmemories.entity.Post;

public class PostFragment extends Fragment {

    private ImageView listImgView;
    private RecyclerView recyclerView;
    private List<Post> postList = new ArrayList<Post>();
    private PostListAdapter postListAdapter;
    private Fragment viewFragment;
    private PostDAO dao;
    private int kidId;

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

        kidId = getArguments().getInt("KID_ID");

        FloatingActionButton addPostBtn = v.findViewById(R.id.addPost);

        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        dao = new PostDAO(getActivity());
        postList = dao.getPostList(kidId);

        /*
        for(int i = 0; i < postList.size(); i++) {
            Post post = postList.get(i);
            if(!"".equals(post.getPhotoLink()) && post.getPhotoLink() != null) {
                byte[] bytePlainOrg = Base64.decode(post.getPhotoLink(), 0);
                ByteArrayInputStream inStream = new ByteArrayInputStream(bytePlainOrg);
                Bitmap bitmap = BitmapFactory.decodeStream(inStream);

                post.setBitMap(bitmap);
            }
        }
        */


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

                /*
                Fragment initFragment = new PostWriteFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.main_layout , initFragment);
                transaction.addToBackStack(null);
                transaction.commit();
                */

                Intent intent = new Intent(view.getContext() , WritePostActivity.class);
                intent.putExtra("KID_ID", kidId);
                intent.putExtra("WRITE_ACTION_TYPE" , "INSERT");

                startActivityForResult(intent , 10001);

            }
        });

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ((requestCode == 10001) && (resultCode == Activity.RESULT_OK)) {

            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.detach(this).attach(this).commit();
        }

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



        postListAdapter.setOnItemClickListener(new PostListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                // View Fragment
                viewFragment = new PostViewFragment();

                Bundle param = new Bundle();
                param.putInt("POST_ID" , postList.get(pos).getId());
                param.putInt("KID_ID" , postList.get(pos).getKidId());
                viewFragment.setArguments(param);

                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.main_layout , viewFragment);
                transaction.commit();
                transaction.addToBackStack(null);

            }
        });

    }

    @Override
    public void onDestroyView() {
        if(recyclerView != null) {
            recyclerView.setAdapter(null);
        }
        super.onDestroyView();
    }


}