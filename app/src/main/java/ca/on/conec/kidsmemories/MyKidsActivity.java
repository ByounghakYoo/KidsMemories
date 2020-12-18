/**
 * FileName : MyKidsActivity
 * Purpose : Kid Activity include Post, immunization info and gallery
 * Revision History :
 *          Created by Byunghak Yoo (Henry) 2020.12.10
 */


package ca.on.conec.kidsmemories;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import ca.on.conec.kidsmemories.fragment.AlbumFragment;
import ca.on.conec.kidsmemories.fragment.ImmunizationFragment;
import ca.on.conec.kidsmemories.fragment.PostFragment;


public class MyKidsActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    Fragment fragment;
    int kidId;

    /**
     * Create Activity
     * Attach Post fragment and bottom Navigation
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_kids);

        // Set UI instance variables
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // Get KidId from previous activiry
        kidId = getIntent().getIntExtra("KID_ID", 0);

        Log.d("info" , ">>>" + kidId);

        fragment = new PostFragment();

        Bundle bundle = new Bundle();
        bundle.putInt("KID_ID", kidId);
        fragment.setArguments(bundle);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_layout , fragment);
        transaction.commit();
        transaction.addToBackStack(null);

        // Click bottom navigation and move those fragment.
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {

                    case R.id.bottom_tab_post:
                        fragment = new PostFragment();
                        break;
                    case R.id.bottom_tab_immunization:
                        fragment = new ImmunizationFragment();
                        break;
                    case R.id.bottom_tab_album:
                        fragment = new AlbumFragment();
                        break;
                    case R.id.bottom_tab_home:
                        // Go to Main Activity
                        startActivity(new Intent(MyKidsActivity.this , MainActivity.class));
                        break;
                }

                if(fragment != null) {
                    // Send KidId to fragments
                    // Use: int kidId = getArguments().getInt("KID_ID");
                    Log.d("info" , ">>>>>>>>>> frag click : "+kidId);
                    Bundle bundle = new Bundle();
                    bundle.putInt("KID_ID", kidId);
                    fragment.setArguments(bundle);

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