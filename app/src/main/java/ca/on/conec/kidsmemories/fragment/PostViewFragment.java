/*
 * FileName : PostViewFragment
 * Purpose : Related to Post detail Fragment
 * Revision History :
 *          Created by Byunghak Yoo (Henry) 2020.12.15
 */
package ca.on.conec.kidsmemories.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import ca.on.conec.kidsmemories.MyKidsActivity;
import ca.on.conec.kidsmemories.R;
import ca.on.conec.kidsmemories.activity.WritePostActivity;
import ca.on.conec.kidsmemories.db.PostDAO;
import ca.on.conec.kidsmemories.entity.Post;

public class PostViewFragment extends Fragment{

    private PostDAO dao;
    TextView txtTitle , txtContent , txtWriteDate;

    int postId , kidId;

    /**
     * Constructor
     */
    public PostViewFragment() {
        // Required empty public constructor
    }


    /**
     * when the create the fragment
     * @param inflater inflater
     * @param container container
     * @param savedInstanceState  savedInstantState
     * @return view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_post_view , container , false);

        txtTitle = v.findViewById(R.id.txtTitle);
        txtContent = v.findViewById(R.id.txtContent);
        txtWriteDate = v.findViewById(R.id.txtWriteDate);

        postId = getArguments().getInt("POST_ID");
        kidId = getArguments().getInt("KID_ID");

        dao = new PostDAO(getActivity());
        Post post = dao.getPostView(postId , kidId);

        Display display = getActivity().getWindowManager().getDefaultDisplay();
        final Point size = new Point();
        display.getSize(size);

        Log.i("info" , size.x + "");
        Log.i("info" , size.y + "");

        if(post != null) {
            txtTitle.setText(post.getTitle());

            Spanned content = Html.fromHtml(post.getContent(), new Html.ImageGetter() {
                @Override
                public Drawable getDrawable(String source) {

                    BitmapFactory.Options opt = new BitmapFactory.Options();
                    opt.inSampleSize = 10;

                    Bitmap bitmap = BitmapFactory.decodeFile(source, opt);

                    Drawable drawable = new BitmapDrawable(getResources(), bitmap);
                    int realImgWidth = drawable.getIntrinsicWidth();
                    int realImgHeight = drawable.getIntrinsicHeight();

                    int displayWidth = size.x;
                    float ratio = (float) displayWidth / (float) realImgWidth;

                    int ratioHeight = (int) (realImgHeight * ratio);

                    drawable.setBounds(0, 0, displayWidth, ratioHeight);

                    return drawable;
                }
            }, null);

            txtContent.setText(content);
            txtWriteDate.setText(post.getWriteDate());
        }


        ActionBar actionBar = ((MyKidsActivity)getActivity()).getSupportActionBar();
        setHasOptionsMenu(true);

        return v;

    }

    /**
     * Menu Draw
     * Modify and delete Button draw
     * @param menu menu item in xml
     * @param inflater inflater
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.post_write_actionbar, menu);
        menu.setGroupVisible(R.id.saveGroup , false);
        menu.setGroupVisible(R.id.updateGroup , true);
        menu.setGroupVisible(R.id.deleteGroup , true);
    }


    /**
     * Modify or Delete click Action
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.postUpdate :
                // go to write activity for update

                Intent intent = new Intent(getContext() , WritePostActivity.class);
                intent.putExtra("KID_ID", kidId);
                intent.putExtra("POST_ID", postId);
                intent.putExtra("WRITE_ACTION_TYPE" , "UPDATE");
                startActivityForResult(intent , 10010);

                return true;
            case R.id.postDelete :
                // delete post and then go to list page;
                // Delete
                // Confirm and if 'Yes' , delete post

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Delete")
                        .setMessage("Are you sure do you want to delete this post?")
                        .setCancelable(true)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deletePost(postId);
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Delete Post method
     * @param postId
     */
    private void deletePost(int postId) {
            Post post = new Post();
            post.setId(postId);

            PostDAO postDAO = new PostDAO(getActivity());
            boolean result = postDAO.deletePost(post);

            if(result) {
                Toast.makeText(getActivity(), "Record deleted successfully.", Toast.LENGTH_SHORT).show();

                // Move to List page

                Fragment fragment = new PostFragment();

                Bundle bundle = new Bundle();
                bundle.putInt("KID_ID", kidId);
                fragment.setArguments(bundle);

                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.main_layout , fragment);
                transaction.commit();

            } else {
                Toast.makeText(getActivity(), "Record does not deleted.", Toast.LENGTH_SHORT).show();
            }

    }


    /**
     * Update Activity Result.
     * When update complete, detach fragment and attach
     * because display new content
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if ((requestCode == 10010) && (resultCode == Activity.RESULT_OK)) {

            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.detach(this).attach(this).commit();
        }
    }
}