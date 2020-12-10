package ca.on.conec.kidsmemories;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
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
import ca.on.conec.kidsmemories.model.Kids;
import ca.on.conec.kidsmemories.util.KidsDBHelper;

public class MainActivity extends AppCompatActivity {
// YH comment
    // YJ comment
    // HJ comment

    Button button;
    Intent intent;

    // DataBase Helper Class for querying grades
    private KidsDBHelper dbh;

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

        mImgTop = (ImageView)findViewById(R.id.imgMyKids);
        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerViewKids);
        FloatingActionButton mFabAdd = findViewById(R.id.fabAdd);
        button = findViewById(R.id.goMyKids);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                intent = new Intent(v.getContext() , MyKidsActivity.class);
                startActivity(intent);
            }
        });

        final Intent intent1 = new Intent(this, AddKidActivity.class);
        mFabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent1);
            }
        });
        getKids();
        bindAdapter();
    }

    /**
     * Search corresponding to user inputs, and then Display a result (list)
     */
    private void getKids() {
        // DataBase Helper Class for querying grades
        dbh = new KidsDBHelper(this);
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
                // Set found grades to RecyclerView
                do {
                    kidObj = new Kids();
                    kidObj.setKidId(cursor.getInt(0));
                    kidObj.setFirstName(cursor.getString(1));
                    kidObj.setLastName(cursor.getString(2));
                    kidObj.setDateOfBirth(cursor.getString(3));
                    kidObj.setGender(cursor.getString(4));
                    kidObj.setNickName(cursor.getString(5));
                    kidObj.setProvinceCode(cursor.getString(6));
                    kidObj.setPhotoURI(cursor.getString(7));
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
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }
}