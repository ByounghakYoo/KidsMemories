package ca.on.conec.kidsmemories;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import ca.on.conec.kidsmemories.activity.AddKidActivity;
import ca.on.conec.kidsmemories.adapter.KidsListAdapter;
import ca.on.conec.kidsmemories.db.KidsDAO;
import ca.on.conec.kidsmemories.entity.Kids;

public class MainActivity extends AppCompatActivity {
    final int WRITE_EXTERNAL_FILE_PERMISSION_REQUEST_CODE = 3;

    // DataBase Helper Class for querying grades
    private KidsDAO dbh;

    // Declare UI instance variables
    private RecyclerView mRecyclerView;
    private List<Kids> mList = new ArrayList<>();
    private KidsListAdapter mAdapter;
    private ImageView mImgTop;
    FloatingActionButton mFabAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set UI instance variables
        mImgTop = (ImageView)findViewById(R.id.imgLogo);
        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerViewKids);
        FloatingActionButton mFabAdd = findViewById(R.id.fabAdd);

        mImgTop.setImageResource(R.drawable.kids_memories_logo);

        mFabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), AddKidActivity.class));
            }
        });
        getKids();
        bindAdapter();

        // Get External Storage Permission
        try
        {
            if (!checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE))
            {
                ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_FILE_PERMISSION_REQUEST_CODE);
            }
        }
        catch (Exception e)
        {
            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        // Will be deleted
        Button button = findViewById(R.id.goMyKids);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext() , MyKidsActivity.class);
                intent.putExtra("KID_ID", 1);
                startActivity(intent);
            }
        });
    }

    /**
     * Search corresponding to user inputs, and then Display a result (list)
     */
    private void getKids() {
        // DataBase Helper Class for querying grades
        dbh = new KidsDAO(this);
        Cursor cursor;

        // Get cursor for getting a list
        cursor = dbh.getKids("");

        Kids kidObj = new Kids();
        mList.clear();

        if (cursor.getCount() == 0)
        {
            Toast.makeText(this, "No Record", Toast.LENGTH_LONG).show();
        }
        else
        {
            if (cursor.moveToFirst())
            {
                // Set found kids to RecyclerView
                do {
                    kidObj = new Kids();
                    kidObj.setKidId(cursor.getInt(0));
                    kidObj.setFirstName(cursor.getString(1));
                    kidObj.setLastName(cursor.getString(2));
                    kidObj.setDateOfBirth(cursor.getString(3));
                    kidObj.setGender(cursor.getString(4));
                    kidObj.setNickName(cursor.getString(5));
                    kidObj.setProvinceCode(cursor.getString(6));
                    kidObj.setPhotoPath(cursor.getString(7));
                    mList.add(kidObj);
                } while (cursor.moveToNext());
            }
        }
    }

    /**
     * This method for setting RecyclerView
     */
    private void bindAdapter()
    {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new KidsListAdapter(mList, this);

        mAdapter.setOnItemClickListener(new KidsListAdapter.onClickListner() {
            @Override
            public void onItemClick(int position, View v) {
                //Snackbar.make(v, "On item click "+position, Snackbar.LENGTH_LONG).show();
                Intent intent = new Intent(v.getContext() , MyKidsActivity.class);
                intent.putExtra("KID_ID", mAdapter.getKidId(position));
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(int position, View v) {
                //Snackbar.make(v, "On item longclick  "+position, Snackbar.LENGTH_LONG).show();
                Intent intent = new Intent(v.getContext() , AddKidActivity.class);
                intent.putExtra("KID_ID", mAdapter.getKidId(position));
                startActivity(intent);
            }
        });


        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    private boolean checkPermission(String permission) {
        int check = ContextCompat.checkSelfPermission(this, permission);
        return (check == PackageManager.PERMISSION_GRANTED);
    }
}