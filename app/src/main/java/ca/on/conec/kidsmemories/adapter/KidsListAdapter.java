/* KidsListAdapter.java
   KidsMemories: List Adapter for RecyclerView
    Revision History
        Yi Phyo Hong, 2020.12.10: Created
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
import ca.on.conec.kidsmemories.entity.Kids;

/**
 * List Adapter Class for RecyclerView
 */
public class KidsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    // List variable for manipulating data
    private List<Kids> mList;
    private static onClickListner onClickListner;

    /**
     * Constructor with List data input
     * @param kids kids data
     * @param context context
     */
    public KidsListAdapter(List<Kids> kids, Context context)
    {
        super();
        mList = kids;
    }

    /**
     *  ViewHolder for looking list data
     */
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener
    {
        public TextView mTxtFirstName;
        public TextView mTxtLastName;
        public TextView mTxtDateOfBirth;
        public ImageView mImgKid;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);

            // Set UI instance variables
            mTxtFirstName = (TextView) itemView.findViewById(R.id.txtFName);
            mTxtLastName = (TextView) itemView.findViewById(R.id.txtLName);
            mTxtDateOfBirth = (TextView) itemView.findViewById(R.id.txtDateOfBitrh);
            mImgKid = (ImageView) itemView.findViewById(R.id.imgKid);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        // Click Event: Set clicked position, call user defined onClick Function
        @Override
        public void onClick(View v) {
            onClickListner.onItemClick(getAdapterPosition(), v);
        }

        // Long Click Event: Set clicked position, call user defined onLongClick Function
        @Override
        public boolean onLongClick(View v) {
            onClickListner.onItemLongClick(getAdapterPosition(), v);
            return true;
        }
    }

    /**
     * Set User defined onClickListner to this class
     * @param onClickListner interface for processing click events
     */
    public void setOnItemClickListener(onClickListner onClickListner) {
        KidsListAdapter.onClickListner = onClickListner;
    }

    /**
     * Interface for click event procedures
     */
    public interface onClickListner {
        void onItemClick(int position, View v);
        void onItemLongClick(int position, View v);
    }

    /**
     *  Connecting ViewHolder to layout
     * @param parent parent Viewgroup
     * @param viewType view type
     * @return created ViewHolder
     */
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        // Inflating layout for recycler view layout and Create Viewholder
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.kid_record_layout, parent, false);
        return new ViewHolder(v);
    }

    /**
     * Loading data to layout (selected position)
     * @param holder viewholder
     * @param position data position
     */
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position)
    {
        Kids kidAdapter = mList.get(position);

        // Set view text data
        ((ViewHolder)holder).mTxtFirstName.setText(String.valueOf(kidAdapter.getFirstName()));
        ((ViewHolder)holder).mTxtLastName.setText(String.valueOf(kidAdapter.getLastName()));
        ((ViewHolder)holder).mTxtDateOfBirth.setText(kidAdapter.getDateOfBirth());

        // If kid data has a photo, display it.
        // If not, display an avatar corresponding to gender
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
        }
    }

    /**
     * Get current number of kids data
     * @return Data count
     */
    @Override
    public int getItemCount()
    {
        return mList.size();
    }

    /**
     * Get kid ID coressponding to position
     * @param position selected position
     * @return kidId
     */
    public int getKidId(int position)
    {
        return mList.get(position).getKidId();
    }
}
