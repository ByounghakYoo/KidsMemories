package ca.on.conec.kidsmemories.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
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
    // List variable for album data
    //Constructor
    public  AlbumListAdapter(List<Album> list, Context context){
        super();
        mList = list;
    }
        //
    class  ViewHolder extends  RecyclerView.ViewHolder
    {
        public ImageView image1;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image1 = (ImageView) itemView.findViewById(R.id.image1);
            // List variable for album

        }
    }

    @NonNull
    @Override
    //
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.album_layout, parent,false);
        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
        //implement to bind data and hold
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        // show the album
        Album itemAdapter = mList.get(position);
        String item1 = itemAdapter.getImgPath();


        Log.i("info" , ">>>>>>>>>>>" + item1);

        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inSampleSize = 10;

        Bitmap bitmap1 = BitmapFactory.decodeFile(item1 , opt);


        ((ViewHolder) holder).image1.setImageBitmap(bitmap1);

    }
        //count image list
    @Override
    public int getItemCount() {
        return mList.size();
    }

}
