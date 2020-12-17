/* AddKidActivity.java
   KidsMemories: Activity for adding a new kid data
    Revision History
        Yi Phyo Hong, 2020.12.10: Created
*/
package ca.on.conec.kidsmemories.activity;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import ca.on.conec.kidsmemories.MainActivity;
import ca.on.conec.kidsmemories.R;
import ca.on.conec.kidsmemories.db.KidsDAO;
import ca.on.conec.kidsmemories.entity.Kids;

/**
 * Activity class for adding a new kid
 */
public class AddKidActivity extends AppCompatActivity {

    static final int SEND_CAM_PERMISSION_REQUEST_CODE = 1;
    static final int INPUT_FILE_REQUEST_CODE = 2;

    // predefined gender List for kids data
    private String[] genderList = {"M", "F"};
    // predefined province List for kids data
    private String[] provinceList =
            {"AB", "BC", "MB", "NB", "NL", "NT", "NS", "NU", "ON", "PE", "QC", "SK", "YT"};

    // DataBase Helper Class for saving kids
    private KidsDAO kDBHelper;

    // Declare UI instance variables
    private ImageView mImgPhoto;
    private EditText mEdtPhotoPath;
    private Button mBtnPhoto;
    private EditText mEdtFirstName;
    private EditText mEdtLastName;
    private DatePickerDialog mDateOfBirthPicker;
    private EditText mEdtDateOfBirth;
    private Spinner mSpnGender;
    private EditText mEdtNickName;
    private Spinner mSpnProvince;
    private Button mBtnSave;
    private Button mBtnDelete;
    private Button mBtnCancel;

    // Member variables
    private int mKidId;
    private String mGender;
    private String mProvinceCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_kid);

        // Set UI instance variables
        mImgPhoto = (ImageView)findViewById(R.id.imagePhoto);
        mEdtPhotoPath = (EditText)findViewById(R.id.edtPhotoPath);
        mBtnPhoto = (Button)findViewById(R.id.btnPhoto);
        mEdtFirstName = (EditText)findViewById(R.id.edtFirstName);
        mEdtLastName = (EditText)findViewById(R.id.edtLastName);
        mEdtDateOfBirth = (EditText)findViewById(R.id.edtDateOfBirth);
        mEdtNickName = (EditText)findViewById(R.id.edtNickName);
        mBtnSave = (Button)findViewById(R.id.btnSave);
        mBtnDelete = (Button)findViewById(R.id.btnDelete);
        mBtnCancel = (Button)findViewById(R.id.btnCancel);

        // Camera button Click: Get a photo from Camera or File Explorer
        mBtnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try
                {
                    if (!checkPermission(Manifest.permission.CAMERA))
                    {
                        ActivityCompat.requestPermissions(AddKidActivity.this,
                                new String[] {Manifest.permission.CAMERA}, SEND_CAM_PERMISSION_REQUEST_CODE);
                    }
                    getKidImage();
                }
                catch (Exception e)
                {
                    Toast.makeText(AddKidActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Set Spinner for Gender input
        mSpnGender = (Spinner) findViewById(R.id.spnGender);
        ArrayAdapter<String> adpGender = new ArrayAdapter<String>(this, R.layout.spinner_item, genderList);
        adpGender.setDropDownViewResource(R.layout.spinner_item);
        mSpnGender.setAdapter(adpGender);

        // If click spinner item, Keep it to member variable
        mSpnGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mGender = mSpnGender.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(AddKidActivity.this, "Please, select gender"
                        + mProvinceCode, Toast.LENGTH_SHORT).show();
            }
        });

        // Set Spinner for Province input
        mSpnProvince = (Spinner) findViewById(R.id.spnProvince);
        ArrayAdapter<String> adpProvince = new ArrayAdapter<String>(this, R.layout.spinner_item, provinceList);
        adpProvince.setDropDownViewResource(R.layout.spinner_item);
        mSpnProvince.setAdapter(adpProvince);

        // If click spinner item, Keep it to member variable
        mSpnProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mProvinceCode = mSpnProvince.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(AddKidActivity.this, "Please, select province"
                        + mProvinceCode, Toast.LENGTH_SHORT).show();
            }
        });

        // If click DOB text input window, call DatePicker
        mEdtDateOfBirth.setInputType(InputType.TYPE_NULL);
        mEdtDateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Date
                final Calendar cal = Calendar.getInstance();
                int dayJoin = cal.get(Calendar.DAY_OF_MONTH);
                int monthJoin = cal.get(Calendar.MONTH);
                int yearJoin = cal.get(Calendar.YEAR);

                // Call DatePicker for DOB input
                mDateOfBirthPicker = new DatePickerDialog(AddKidActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        mEdtDateOfBirth.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
                    }
                }, yearJoin, monthJoin, dayJoin);
                mDateOfBirthPicker.show();
            }
        });

        // Save button: Save current kid data, and go back to kids list (previous activity)
        mBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kDBHelper = new KidsDAO(AddKidActivity.this);

                // Save kid data
                Kids kid = new Kids(mKidId, mEdtFirstName.getText().toString(),
                        mEdtLastName.getText().toString(),
                        mEdtDateOfBirth.getText().toString(),
                        mGender, mEdtNickName.getText().toString(),
                        mProvinceCode, mEdtPhotoPath.getText().toString());

                boolean insertStat;
                // If new kid data, insert it
                // If not, update it
                if (mKidId == 0)
                {
                    insertStat = kDBHelper.insertKid(kid);
                }
                else
                {
                    insertStat = kDBHelper.updateKid(kid);
                }

                // Toast message for user (success or not)
                if (insertStat == true)
                {
                    Toast.makeText(getApplicationContext(), "Kid added/updated successfully",
                            Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Kid not added/updated",
                            Toast.LENGTH_LONG).show();
                }

                // Go to kids list menu
                startActivity(new Intent(AddKidActivity.this, MainActivity.class));
            }
        });

        // Delete Button: delete current kid data
        mBtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kDBHelper = new KidsDAO(AddKidActivity.this);

                boolean insertStat = kDBHelper.deleteKid(mKidId);

                // Toast message for user (success or not)
                if (insertStat == true)
                {
                    Toast.makeText(getApplicationContext(), "Kid added/updated successfully",
                            Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Kid not added/updated",
                            Toast.LENGTH_LONG).show();
                }

                startActivity(new Intent(AddKidActivity.this, MainActivity.class));
            }
        });

        // Cancel Button: Go back to kids list
        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddKidActivity.this, MainActivity.class));
            }
        });

        // Get selected kid ID from previous activity
        mKidId = getIntent().getIntExtra("KID_ID", 0);

        // If kid id exists, get kids data from DB, and enable Delete button
        // If not, disable delete button
        if (mKidId != 0)
        {
            getKid(mKidId);
            mBtnDelete.setVisibility(View.VISIBLE);
        }
        else
        {
            mBtnDelete.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * Search corresponding to Kid ID, and then Display a result
     */
    private void getKid(int kidId)
    {
        // DataBase Helper Class for querying kids data
        kDBHelper = new KidsDAO(this);
        Cursor cursor;

        // Get cursor for getting a list
        cursor = kDBHelper.getKids("WHERE kid_id = " + kidId);

        Kids kidObj = new Kids();

        // If no data, Display Error message
        if (cursor.getCount() == 0)
        {
            Toast.makeText(this, "No Record", Toast.LENGTH_LONG).show();
        }
        else
        {
            if (cursor.moveToFirst())
            {
                // Set found kid data to View UI
                mEdtFirstName.setText(cursor.getString(1));
                mEdtLastName.setText(cursor.getString(2));
                mEdtDateOfBirth.setText(cursor.getString(3));
                for (int position = 0; position < genderList.length; position++)
                {
                    if (genderList[position].equals(cursor.getString(4)))
                    {
                        mSpnGender.setSelection(position);
                    }
                }
                mEdtNickName.setText(cursor.getString(5));
                for (int position = 0; position < provinceList.length; position++)
                {
                    if (provinceList[position].equals(cursor.getString(6)))
                    {
                        mSpnProvince.setSelection(position);
                    }
                }
                mEdtPhotoPath.setText(cursor.getString(7));
                Bitmap bitmap = BitmapFactory.decodeFile(mEdtPhotoPath.getText().toString());
                mImgPhoto.setImageBitmap(bitmap);
            }
        }
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
     * Photo button: Get result from Camera, Gallery, or Photos, Save photo path to DB
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK)
        {
            // If it returns multiple item from storage
            if (data.getClipData() != null)
            {
                ClipData clipData = data.getClipData();
                Uri[] results = new Uri[clipData.getItemCount()];

                // Save last item, but just one photo can be selected now
                for (int i = 0; i < clipData.getItemCount(); i++)
                {
                    ClipData.Item item = clipData.getItemAt(i);
                    Uri imgUri = item.getUri();

                    // Save path and display image
                    String imgPath = getPathFromUri(imgUri);
                    mEdtPhotoPath.setText(imgPath);
                    Bitmap bitmap = BitmapFactory.decodeFile(imgPath);
                    mImgPhoto.setImageBitmap(bitmap);
                }
            }
            else if (data.getClipData() == null && data.getDataString() != null)
            {
                // Single item selected
                String dataString = data.getDataString();
                if (dataString != null)
                {
                    Uri imgUri = Uri.parse(dataString);
                    // Save path and display image
                    String imgPath = getPathFromUri(imgUri);
                    mEdtPhotoPath.setText(imgPath);
                    Bitmap bitmap = BitmapFactory.decodeFile(imgPath);
                    mImgPhoto.setImageBitmap(bitmap);
                }

            }
            else
            {
                // Photo from Camera
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                mImgPhoto.setImageBitmap(photo);

                // Save photo to a file, save path to DB
                File filePath = getApplicationContext().getFilesDir();
                File file = new File(filePath, getNewFileName());
                try (FileOutputStream out = new FileOutputStream(file))
                {
                    photo.compress(Bitmap.CompressFormat.PNG, 100, out);
                    mEdtPhotoPath.setText(file.getPath());
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Get file name created by time basis
     * @return new file name
     */
    private String getNewFileName()
    {
        Date currentTime = Calendar.getInstance().getTime();
        return "MyKidProfile" + currentTime.toString();
    }

    /**
     * Get a real photo path from URI
     * @param uri URI for converting
     * @return path string
     */
    private String getPathFromUri(Uri uri)
    {
        // Get cursor correspondign to URI
        ContentResolver contentResolver = getApplicationContext().getContentResolver();
        Cursor cursor = contentResolver.query(uri, new String[] {MediaStore.MediaColumns.DATA},
                null, null, null );

        // Get real path
        String path = null;
        if (cursor != null)
        {
            int dataIndex = cursor.getColumnIndex(MediaStore.MediaColumns.DATA);
            if (dataIndex >= 0 && cursor.moveToFirst())
            {
                path = cursor.getString(dataIndex);
            }
            cursor.close();
        }

        return path;
    }

    /**
     * Get Photo from Camera, Gallery, Photos
     */
    private void getKidImage()
    {
        Intent[] intentArray;

        // Set Camera Intent
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null)
        {
            if (takePictureIntent != null)
            {
                intentArray = new Intent[]{ takePictureIntent };
            }
            else
            {
                intentArray = new Intent[0];
            }
        }
        else
        {
            intentArray = new Intent[0];
        }

        // Get file Intent (Gallery, Photos, ...) from storage
        Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
        contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
        contentSelectionIntent.setType("image/*");

        // Chooser: Camera or File
        Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
        chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);
        chooserIntent.putExtra(Intent.EXTRA_TITLE, "Select a Photo");

        startActivityForResult(chooserIntent, INPUT_FILE_REQUEST_CODE);
    }
}