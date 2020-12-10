package ca.on.conec.kidsmemories.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import ca.on.conec.kidsmemories.MyKidsActivity;
import ca.on.conec.kidsmemories.R;
import ca.on.conec.kidsmemories.model.Kids;
import ca.on.conec.kidsmemories.util.KidsDBHelper;

public class AddKidActivity extends AppCompatActivity {
    // DataBase Helper Class for saving grades
    private KidsDBHelper dbh;
    private Button mBtnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_kid);

        mBtnAdd = (Button)findViewById(R.id.btnSave);

        final Intent intent1 = new Intent(this, MyKidsActivity.class);

        mBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbh = new KidsDBHelper(getApplicationContext());

                // Save inputted grade data
                Kids kid = new Kids(0, "Harry", "Porter", "2010-01-20", "M", "Nick", "ON", "/pic/pic1.png");
                Boolean insertStat = dbh.insertKid(kid);

                // Toast message for user (success or not)
                if (insertStat == true)
                {
                    Toast.makeText(getApplicationContext(), "Record inserted successfully", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Record not inserted", Toast.LENGTH_LONG).show();
                }

                startActivity(intent1);
            }
        });
    }
}