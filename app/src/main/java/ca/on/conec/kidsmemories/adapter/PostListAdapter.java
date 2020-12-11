/**
 * FileName : PostListAdapter
 * Purpose : For recycler view.
 * Revision History :
 *          Creted by Byounghak Yoo (Henry) 2020.12.10
 */

package ca.on.conec.kidsmemories.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ca.on.conec.kidsmemories.R;
import ca.on.conec.kidsmemories.entity.Post;

public class PostListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private List<Post> postAdapterList;


    /**
     *
     * @param list list using recycler view.
     * @param context
     */
    public PostListAdapter(List<Post> list , Context context) {
        super();
        postAdapterList = list;
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView listImgView;
        public TextView title , content, writeDate ;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            listImgView = (ImageView) itemView.findViewById(R.id.photo_link);
            title = (TextView) itemView.findViewById(R.id.post_title);
            content = (TextView) itemView.findViewById(R.id.post_content);
            writeDate = (TextView) itemView.findViewById(R.id.post_writedate);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_data_record_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Post itemAdapter = postAdapterList.get(position);
        //((ViewHolder) holder).listImgView.setImageResource(itemAdapter.getPhotoLink());
        ((ViewHolder) holder).title.setText(itemAdapter.getTitle());
        ((ViewHolder) holder).content.setText(itemAdapter.getContent());
        ((ViewHolder) holder).writeDate.setText(String.valueOf(itemAdapter.getWriteDate()));
    }


    @Override
    public int getItemCount() {
        return postAdapterList.size();
    }
}


