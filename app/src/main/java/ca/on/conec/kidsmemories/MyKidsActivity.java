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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_kids);

        // Set UI instance variables
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        fragment = new PostFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_layout , fragment);
        transaction.commit();
        transaction.addToBackStack(null);

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

    }

}