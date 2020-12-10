package ca.on.conec.kidsmemories;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import ca.on.conec.kidsmemories.activity.AddKidActivity;
import ca.on.conec.kidsmemories.adapter.KidsListAdapter;
import ca.on.conec.kidsmemories.fragment.AlbumFragment;
import ca.on.conec.kidsmemories.fragment.ImmunizationFragment;
import ca.on.conec.kidsmemories.fragment.PostFragment;
import ca.on.conec.kidsmemories.model.Kids;
import ca.on.conec.kidsmemories.util.KidsDBHelper;

public class MyKidsActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    Fragment fragment;

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
        setContentView(R.layout.activity_my_kids);

        // Set UI instance variables
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        mImgTop = (ImageView)findViewById(R.id.imgMyKids);
        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerViewKids);
        FloatingActionButton mFabAdd = findViewById(R.id.fabAdd);

//        fragment = new PostFragment();
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.replace(R.id.main_layout , fragment);
//        transaction.commit();
//        transaction.addToBackStack(null);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {

                    case R.id.bottom_tab1 :
                        fragment = new PostFragment();
                        break;
                    case R.id.bottom_tab2 :
                        fragment = new ImmunizationFragment();
                        break;
                    case R.id.bottom_tab3 :
                        fragment = new AlbumFragment();
                        break;
                }

                if(fragment != null) {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.main_layout, fragment);
                    transaction.commit();
                    transaction.addToBackStack(null);

                    return true;
                }

                return false;
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