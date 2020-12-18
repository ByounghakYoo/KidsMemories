/* MainActivity.java
   KidsMemories: Main Activity (Kids List)
    Revision History
        Byounghak Yoo, 2020.12.04: Created
        Yi Phyo Hong, 2020.12.10: Modified
*/
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

    // DataBase Helper Class for querying kids DB
    private KidsDAO kDBHelper;

    // Declare UI instance variables
    private ImageView mImgTop;
    FloatingActionButton mFabAdd;

    // Recycler View variables
    private RecyclerView mRecyclerView;
    private List<Kids> mList = new ArrayList<>();
    private KidsListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set UI instance variables
        mImgTop = (ImageView)findViewById(R.id.imgLogo);
        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerViewKids);
        mFabAdd = findViewById(R.id.fabAdd);

        mImgTop.setImageResource(R.drawable.kids_memories_logo);

        // Floating Button (+): Go to Activity for adding new kid data
        mFabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AddKidActivity.class));
            }
        });

        // Get kids list
        getKids();
        // binding an adapter for Recycler view
        bindAdapter();

        // Get External Storage Permission for saving photo file
        try
        {
            if (!checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE))
            {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_FILE_PERMISSION_REQUEST_CODE);
            }
        }
        catch (Exception e)
        {
            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Search corresponding to user inputs, and then Add kids list to recycler view data
     */
    private void getKids()
    {
        // DataBase Helper Class for querying kids
        kDBHelper = new KidsDAO(this);
        Cursor cursor;

        // Get cursor for getting a list
        cursor = kDBHelper.getKids("");

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
     * This method for binding an adapter for RecyclerView
     */
    private void bindAdapter()
    {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new KidsListAdapter(mList, this);

        // Set Click event to Recycler view
        mAdapter.setOnItemClickListener(new KidsListAdapter.onClickListner() {
            @Override
            public void onItemClick(int position, View v) {
                Intent intent;

                if (v.getId() == R.id.btnEditKid) {
                    // Go to Activity for adding a new kid
                    intent = new Intent(MainActivity.this , AddKidActivity.class);
                } else {
                    // Go to Next Activity (3 Fragments)
                    intent = new Intent(MainActivity.this , MyKidsActivity.class);
                }

                intent.putExtra("KID_ID", mAdapter.getKidId(position));
                startActivity(intent);
            }
        });

        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    /**
     * Check Permission is granted or not
     * @param permission permission for checking
     * @return granted flag
     */
    private boolean checkPermission(String permission)
    {
        int check = ContextCompat.checkSelfPermission(this, permission);
        return (check == PackageManager.PERMISSION_GRANTED);
    }

    /**
     * OnStop State: Remove Recycler View Adpater for preventing memory leak
     */
    @Override
    protected void onStop()
    {
        // Prevent Memory Leak
        mRecyclerView.setAdapter(null);
        super.onStop();
    }

    /**
     * OnRestart State: Re-Binding Recycler View Adapter
     */
    @Override
    protected void onRestart() {
        // Re-Bind Recycler View Adapter because it is removed onStop()
        bindAdapter();
        super.onRestart();
    }
}