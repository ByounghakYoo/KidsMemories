package ca.on.conec.kidsmemories.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ca.on.conec.kidsmemories.R;
import ca.on.conec.kidsmemories.entity.Album;

public class AlbumListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Album> mList;

    public  AlbumListAdapter(List<Album> list, Context context){
        super();
        mList = list;
    }

    class  ViewHolder extends  RecyclerView.ViewHolder
    {
        public ImageView image1;
        public ImageView image2;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image1 = (ImageView) itemView.findViewById(R.id.image1);
            image2 = (ImageView) itemView.findViewById(R.id.image2);

        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.album_layout, parent,false);
        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        Album itemAdapter = mList.get(position);
        String item1 = itemAdapter.getImage1();
        String item2 = itemAdapter.getImage1();
        Bitmap bitmap1 = BitmapFactory.decodeFile( item1);
        Bitmap bitmap2 = BitmapFactory.decodeFile( item2);

        ((ViewHolder) holder).image1.setImageBitmap(bitmap1);

        ((ViewHolder) holder).image2.setImageBitmap(bitmap2);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


}
