/**
 * FileName : PostListAdapter
 * Purpose : For recycler view.
 * Revision History :
 *          Creted by Byounghak Yoo (Henry) 2020.12.10
 */

package ca.on.conec.kidsmemories.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
    private OnItemClickListener pListener;

    /**
     * assign postAdapterList;
     * @param list list using recycler view.
     * @param context
     */
    public PostListAdapter(List<Post> list , Context context) {
        super();
        postAdapterList = list;
    }


    /**
     * View Holder Constructor
     */
    class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView listImgView;
        public TextView title , content, writeDate ;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            listImgView = (ImageView) itemView.findViewById(R.id.photo_link);
            title = (TextView) itemView.findViewById(R.id.post_title);
            content = (TextView) itemView.findViewById(R.id.post_content);
            writeDate = (TextView) itemView.findViewById(R.id.post_writedate);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION) {
                        if (pListener != null) {
                            pListener.onItemClick(v, pos);
                        }
                    }
                }
            });

        }
    }

    /**
     * VIew Holder Create Layout
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_data_record_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    /**
     * Binding Adapter to View Holder
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Post itemAdapter = postAdapterList.get(position);

        if (itemAdapter.getPhotoLink().isEmpty()) {
            ((ViewHolder) holder).listImgView.setImageBitmap(null);
        } else {
            Bitmap bitmap = BitmapFactory.decodeFile(itemAdapter.getPhotoLink());
            ((ViewHolder) holder).listImgView.setImageBitmap(bitmap);
        }

        ((ViewHolder) holder).title.setText(itemAdapter.getTitle());
        ((ViewHolder) holder).content.setText(itemAdapter.getContent());
        ((ViewHolder) holder).writeDate.setText(String.valueOf(itemAdapter.getWriteDate()));

    }


    /**
     * View Holder Item Count
     * @return
     */
    @Override
    public int getItemCount() {
        return postAdapterList.size();
    }

    /**
     * Item List Click Event Listener interface
     */
    public interface OnItemClickListener {
        void onItemClick(View v, int pos);
    }

    /**
     * Setter Item Click Listener
     * @param listener
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.pListener = listener;
    }

}


