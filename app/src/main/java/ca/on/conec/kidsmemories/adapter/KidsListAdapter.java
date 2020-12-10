package ca.on.conec.kidsmemories.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ca.on.conec.kidsmemories.R;
import ca.on.conec.kidsmemories.model.Kids;

/**
 * List Adapter Class for RecyclerView
 */
public class KidsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    // List variable for manipulating data
    private List<Kids> mList;

    // Constructor with List data input
    public KidsListAdapter(List<Kids> grade, Context context)
    {
        super();
        mList = grade;
    }

    // ViewHolder for looking list data
    class ViewHolder extends RecyclerView.ViewHolder
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
        }
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
        Uri imgUri = Uri.parse(kidAdapter.getPhotoURI());
        ((ViewHolder)holder).mImgKid.setImageURI(imgUri);
    }

    @Override
    public int getItemCount()
    {
        return mList.size();
    }
}
