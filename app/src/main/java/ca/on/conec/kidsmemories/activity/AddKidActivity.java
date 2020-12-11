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
import ca.on.conec.kidsmemories.adapter.KidsListAdapter;
import ca.on.conec.kidsmemories.db.KidsDAO;
import ca.on.conec.kidsmemories.model.Kids;


public class AddKidActivity extends AppCompatActivity {
    static final int SEND_CAM_PERMISSION_REQUEST_CODE = 1;
    static final int INPUT_FILE_REQUEST_CODE = 2;

    private String[] genderList = {"M", "F", "X"};
    private String[] provinceList = {"AB", "BC", "MB", "NB", "NL", "NT", "NS", "NU", "ON", "PE", "QC", "SK", "YT"};

    // DataBase Helper Class for saving grades
    private KidsDAO dbh;

    // Declare UI instance variables
    private ImageView mImgPhoto;
    private EditText mEdtPhotoPath;
    private Button mBtnCamera;
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
        mBtnCamera = (Button)findViewById(R.id.btnPhoto);
        mEdtFirstName = (EditText)findViewById(R.id.edtFirstName);
        mEdtLastName = (EditText)findViewById(R.id.edtLastName);
        mEdtDateOfBirth = (EditText)findViewById(R.id.edtDateOfBirth);
        mEdtNickName = (EditText)findViewById(R.id.edtNickName);
        mBtnSave = (Button)findViewById(R.id.btnSave);
        mBtnDelete = (Button)findViewById(R.id.btnDelete);
        mBtnCancel = (Button)findViewById(R.id.btnCancel);

        mBtnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try
                {
                    if (!checkPermission(Manifest.permission.CAMERA))
                    {
                        ActivityCompat.requestPermissions(AddKidActivity.this, new String[] {Manifest.permission.CAMERA}, SEND_CAM_PERMISSION_REQUEST_CODE);
                    }
                    getKidImage();
                }
                catch (Exception e)
                {
                    Toast.makeText(AddKidActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        mSpnGender = (Spinner) findViewById(R.id.spnGender);
        ArrayAdapter<String> adpGender = new ArrayAdapter<String>(this, R.layout.spinner_item, genderList);
        adpGender.setDropDownViewResource(R.layout.spinner_item);
        mSpnGender.setAdapter(adpGender);

        mSpnGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mGender = genderList[position];
                //Toast.makeText(AddKidActivity.this, "Selected Gender: " + mGender, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mSpnProvince = (Spinner) findViewById(R.id.spnProvince);
        ArrayAdapter<String> adpProvince = new ArrayAdapter<String>(this, R.layout.spinner_item, provinceList);
        adpProvince.setDropDownViewResource(R.layout.spinner_item);
        mSpnProvince.setAdapter(adpProvince);

        mSpnProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mProvinceCode = provinceList[position];
                //Toast.makeText(AddKidActivity.this, "Selected Province: " + mProvinceCode, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mEdtDateOfBirth.setInputType(InputType.TYPE_NULL);
        mEdtDateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cal = Calendar.getInstance();
                int dayJoin = cal.get(Calendar.DAY_OF_MONTH);
                int monthJoin = cal.get(Calendar.MONTH);
                int yearJoin = cal.get(Calendar.YEAR);
                mDateOfBirthPicker = new DatePickerDialog(AddKidActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        mEdtDateOfBirth.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
                    }
                }, yearJoin, monthJoin, dayJoin);

                mDateOfBirthPicker.show();
            }
        });


        final Intent intent = new Intent(this, MainActivity.class);
        mBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbh = new KidsDAO(AddKidActivity.this);

                // Save inputted kid data
                Kids kid = new Kids(mKidId, mEdtFirstName.getText().toString(), mEdtLastName.getText().toString(),
                        mEdtDateOfBirth.getText().toString(), mGender, mEdtNickName.getText().toString(),
                        mProvinceCode, mEdtPhotoPath.getText().toString());

                Boolean insertStat;
                if (mKidId == 0)
                {
                    insertStat = dbh.insertKid(kid);
                }
                else
                {
                    insertStat = dbh.updateKid(kid);
                }

                // Toast message for user (success or not)
                if (insertStat == true)
                {
                    Toast.makeText(getApplicationContext(), "Kid added/updated successfully", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Kid not added/updated", Toast.LENGTH_LONG).show();
                }

                startActivity(intent);
            }
        });

        mBtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbh = new KidsDAO(AddKidActivity.this);

                Boolean insertStat = dbh.deleteKid(mKidId);

                // Toast message for user (success or not)
                if (insertStat == true)
                {
                    Toast.makeText(getApplicationContext(), "Kid added/updated successfully", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Kid not added/updated", Toast.LENGTH_LONG).show();
                }

                startActivity(intent);
            }
        });

        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent);
            }
        });

        mKidId = getIntent().getIntExtra("KID_ID", 0);
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
    private void getKid(int kidId) {
        // DataBase Helper Class for querying grades
        dbh = new KidsDAO(this);
        Cursor cursor;

        // Get cursor for getting a list
        cursor = dbh.getKids("WHERE kid_id = " + kidId);

        Kids kidObj = new Kids();

        if (cursor.getCount() == 0)
        {
            Toast.makeText(this, "No Record", Toast.LENGTH_LONG).show();
        }
        else
        {
            if (cursor.moveToFirst())
            {
                // Set found kid to View UI
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

    private boolean checkPermission(String permission) {
        int check = ContextCompat.checkSelfPermission(this, permission);
        return (check == PackageManager.PERMISSION_GRANTED);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK)
        {
            // If it returns multiple item from storage
            if (data.getClipData() != null)
            {
                ClipData mClipData = data.getClipData();
                Uri[] results = new Uri[mClipData.getItemCount()];

                for (int i = 0; i < mClipData.getItemCount(); i++)
                {
                    ClipData.Item item = mClipData.getItemAt(i);
                    Uri imgUri = item.getUri();

                    // save path and display image
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
                    // save path and display image
                    String imgPath = getPathFromUri(imgUri);
                    mEdtPhotoPath.setText(imgPath);
                    Bitmap bitmap = BitmapFactory.decodeFile(imgPath);
                    mImgPhoto.setImageBitmap(bitmap);
                }

            }
            else
            {
                //Camera
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                mImgPhoto.setImageBitmap(photo);

                File filePath = getApplicationContext().getFilesDir();
                File file = new File(filePath, getNewFileName());
                try (FileOutputStream out = new FileOutputStream(file))
                {
                    photo.compress(Bitmap.CompressFormat.PNG, 100, out);
                    // PNG is a lossless format, the compression factor (100) is ignored
                    mEdtPhotoPath.setText(file.getPath());
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    private String getNewFileName()
    {
        Date currentTime = Calendar.getInstance().getTime();
        return "MyKidProfile" + currentTime.toString();
    }

    private String getPathFromUri(Uri uri) {
        ContentResolver contentResolver = getApplicationContext().getContentResolver();
        Cursor cursor = contentResolver.query( uri, new String[] { MediaStore.MediaColumns.DATA }, null, null, null );
        String path = null;
        if ( cursor != null )
        {
            int dataIdx = cursor.getColumnIndex( MediaStore.MediaColumns.DATA );
            if (dataIdx>=0&&cursor.moveToFirst())
                path = cursor.getString( dataIdx );
            cursor.close();
        }
        return path;
    }

    public void getKidImage() {
        Intent[] intentArray;

        // Camera
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

        // Get file (Gallery, Photos, ...) from storage
        Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
        contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
        contentSelectionIntent.setType("image/*");

        // To select multiple images from the storage
        //contentSelectionIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);

        // To select any kind of file or attachment such as image, files etc.
        //contentSelectionIntent.setType("*//*");

        // Chooser: Camera or File
        Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
        chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);
        chooserIntent.putExtra(Intent.EXTRA_TITLE, "Select a Photo");

        startActivityForResult(chooserIntent, INPUT_FILE_REQUEST_CODE);
    }
}