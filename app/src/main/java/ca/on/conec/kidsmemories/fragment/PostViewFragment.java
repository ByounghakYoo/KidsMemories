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
import android.graphics.drawable.LevelListDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.text.Spanned;
import android.util.Base64;
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

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import ca.on.conec.kidsmemories.MyKidsActivity;
import ca.on.conec.kidsmemories.R;
import ca.on.conec.kidsmemories.activity.WritePostActivity;
import ca.on.conec.kidsmemories.adapter.PostListAdapter;
import ca.on.conec.kidsmemories.db.PostDAO;
import ca.on.conec.kidsmemories.entity.Post;

public class PostViewFragment extends Fragment{

    private PostDAO dao;
    TextView txtTitle , txtContent , txtWriteDate;

    int postId , kidId;

    public PostViewFragment() {
        // Required empty public constructor
    }


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
        display.getRealSize(size);

        if(post != null) {
            txtTitle.setText(post.getTitle());

            Spanned content = Html.fromHtml(post.getContent(), new Html.ImageGetter() {
                @Override
                public Drawable getDrawable(String source) {
                    Bitmap bitmap = BitmapFactory.decodeFile(source);
                    Drawable drawable = new BitmapDrawable(getResources(), bitmap);
                    drawable.setBounds(0,0,drawable.getIntrinsicWidth() , drawable.getIntrinsicHeight());
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.post_write_actionbar, menu);
        menu.setGroupVisible(R.id.saveGroup , false);
        menu.setGroupVisible(R.id.updateGroup , true);
        menu.setGroupVisible(R.id.deleteGroup , true);
    }


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


    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if ((requestCode == 10010) && (resultCode == Activity.RESULT_OK)) {

            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.detach(this).attach(this).commit();
        }
    }
}