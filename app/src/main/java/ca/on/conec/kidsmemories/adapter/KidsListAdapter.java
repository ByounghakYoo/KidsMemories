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
import ca.on.conec.kidsmemories.entity.Kids;

/**
 * List Adapter Class for RecyclerView
 */
public class KidsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    // List variable for manipulating data
    private List<Kids> mList;
    private static onClickListner onclicklistner;

    // Constructor with List data input
    public KidsListAdapter(List<Kids> kids, Context context)
    {
        super();
        mList = kids;
    }

    // ViewHolder for looking list data
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener
    {
        public TextView mTxtName;
        public TextView mTxtDateOfBirth;
        public ImageView mImgKid;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            mTxtName = (TextView) itemView.findViewById(R.id.txtName);
            mTxtDateOfBirth = (TextView) itemView.findViewById(R.id.txtDateOfBitrh);
            mImgKid = (ImageView) itemView.findViewById(R.id.imgKid);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onclicklistner.onItemClick(getAdapterPosition(), v);
        }

        @Override
        public boolean onLongClick(View v) {
            onclicklistner.onItemLongClick(getAdapterPosition(), v);
            return true;
        }
    }

    public void setOnItemClickListener(onClickListner onclicklistner) {
        KidsListAdapter.onclicklistner = onclicklistner;
    }

    public interface onClickListner {
        void onItemClick(int position, View v);
        void onItemLongClick(int position, View v);
    }

    // Connecting ViewHolder to layout
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.kid_record_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    // Loading data to layout (selected position)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position)
    {
        Kids kidAdapter = mList.get(position);
        ((ViewHolder)holder).mTxtName.setText(String.valueOf(kidAdapter.getFirstName() + " " + kidAdapter.getLastName()));
        ((ViewHolder)holder).mTxtDateOfBirth.setText(kidAdapter.getDateOfBirth());
        if (!kidAdapter.getPhotoPath().isEmpty())
        {
            Bitmap bitmap = BitmapFactory.decodeFile(kidAdapter.getPhotoPath());
            ((ViewHolder)holder).mImgKid.setImageBitmap(bitmap);
        }
        else
        {
            if (kidAdapter.getGender().equals("F"))
            {
                ((ViewHolder)holder).mImgKid.setImageResource(R.drawable.avatar_f);
            }
            else if (kidAdapter.getGender().equals("M"))
            {
                ((ViewHolder)holder).mImgKid.setImageResource(R.drawable.avatar_m);
            }
            else
            {
                ((ViewHolder)holder).mImgKid.setImageResource(R.drawable.avatar_x);
            }
        }
    }

    @Override
    public int getItemCount()
    {
        return mList.size();
    }

    public int getKidId(int position)
    {
        return mList.get(position).getKidId();
    }
}
