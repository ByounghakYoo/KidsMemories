/*
 * FileName : WritePostActivity
 * Purpose : Write and update form activity
 * Revision History
 *          Created by Byounghak Yoo (Henry) 2020.12.10
 *
 */
package ca.on.conec.kidsmemories.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.github.irshulx.Editor;
import com.github.irshulx.EditorListener;
import com.github.irshulx.models.EditorTextStyle;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import ca.on.conec.kidsmemories.R;
import ca.on.conec.kidsmemories.db.PostDAO;
import ca.on.conec.kidsmemories.entity.Post;

public class WritePostActivity extends AppCompatActivity {

    EditText title;
    Editor editor;
    String writeActionType;
    int kidId , postId;

    /**
     * create life cycle
     * @param savedInstanceState
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_post);

        Intent intent = getIntent();
        writeActionType = intent.getStringExtra("WRITE_ACTION_TYPE");
        kidId = intent.getIntExtra("KID_ID" , 0);
        postId = intent.getIntExtra("POST_ID" , 0);

        editor = findViewById(R.id.postContent);
        title = findViewById(R.id.postTitle);
        setEditorBtnSetup();



        if(writeActionType != null && writeActionType.equals("UPDATE")) {
            PostDAO postDAO = new PostDAO(this);
            Post post = postDAO.getPostView(postId , kidId);

            if(post != null) {
                title.setText(post.getTitle());
                editor.render(post.getContent());
            }

            ActionBar actionBar = getSupportActionBar();

        } else {
            editor.render();
        }

        final android.os.Handler handler = new android.os.Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                title.setFocusable(true);
                title.setFocusableInTouchMode(true);
                title.requestFocus();
            }
        }, 50);



        editor.setEditorListener(new EditorListener() {
            @Override
            public void onTextChanged(EditText editText, Editable text) {
                // Toast.makeText(EditorTestActivity.this, text, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onUpload(Bitmap image, String uuid) {
                Toast.makeText(WritePostActivity.this, uuid, Toast.LENGTH_LONG).show();

                /*
                Base 64 Enconding, problem db storage upper 1M
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream .toByteArray();

                String encoded = Base64.encodeToString(byteArray , Base64.DEFAULT);
                */
                File filePath = getApplicationContext().getFilesDir();
                File file = new File(filePath, getEditorImageFileName());

                try (FileOutputStream out = new FileOutputStream(file))
                {
                    image.compress(Bitmap.CompressFormat.PNG, 50, out);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                String absolutePath = file.getAbsolutePath();

                editor.onImageUploadComplete(absolutePath, uuid);
            }

            @Override
            public View onRenderMacro(String name, Map<String, Object> props, int index) {
                View view = null;
                return view;
            }

        });

    }

    /**
     * generate image file name
     * @return image file name
     */
    private String getEditorImageFileName() {
        Date date = new Date();
        String timeStr = new SimpleDateFormat("yyyyMMddHHmmss").format(date);
        return "Kid_" + kidId + "_" + timeStr + ".png";
    }

    /**
     * option emnu
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.post_write_actionbar, menu);

        menu.setGroupVisible(R.id.saveGroup , true);
        menu.setGroupVisible(R.id.updateGroup , false);
        menu.setGroupVisible(R.id.deleteGroup , false);
        return true;
    }


    /**
     * Option Item Clicked
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.postSave :

                String titleString = title.getText().toString();
                String contentAsHTML = editor.getContentAsHTML();

                if(writeActionType != null && writeActionType.equals("INSERT")) {
                    // Save Post.
                    SavePost(titleString ,contentAsHTML);

                } else if(writeActionType != null && writeActionType.equals("UPDATE")) {
                    UpdatePost(titleString ,contentAsHTML);
                }
                finish();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    /**
     * Insert Post
     * @param title title
     * @param content content
     */
    public void SavePost(String title, String content) {

        Post post = new Post();
        post.setTitle(title);
        post.setContent(content);
        post.setKidId(kidId);

        PostDAO postDAO = new PostDAO(this);
        boolean result = postDAO.createPost(post);

        if(result) {
            Toast.makeText(this, "Record added successfully.", Toast.LENGTH_SHORT).show();
            setResult(Activity.RESULT_OK);
        } else {
            Toast.makeText(this, "Record does not added.", Toast.LENGTH_SHORT).show();
        }

        finish();
    }

    /**
     * Update Post
     * @param title title
     * @param content content
     */
    private void UpdatePost(String title, String content) {
        Post post = new Post();
        post.setTitle(title);
        post.setContent(content);
        post.setKidId(kidId);
        post.setId(postId);

        PostDAO postDAO = new PostDAO(this);
        boolean result = postDAO.modifyPost(post);

        if(result) {
            Toast.makeText(this, "Record modified successfully.", Toast.LENGTH_SHORT).show();
            setResult(Activity.RESULT_OK);
        } else {
            Toast.makeText(this, "Record does not modified.", Toast.LENGTH_SHORT).show();
        }

        finish();

    }

    /**
     * Editor Button assign
     */
    public void setEditorBtnSetup() {

        findViewById(R.id.action_h1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.updateTextStyle(EditorTextStyle.H1);
            }
        });

        findViewById(R.id.action_h2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.updateTextStyle(EditorTextStyle.H2);
            }
        });

        findViewById(R.id.action_h3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.updateTextStyle(EditorTextStyle.H3);
            }
        });

        findViewById(R.id.action_bold).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.updateTextStyle(EditorTextStyle.BOLD);
            }
        });

        findViewById(R.id.action_Italic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.updateTextStyle(EditorTextStyle.ITALIC);
            }
        });

        findViewById(R.id.action_indent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.updateTextStyle(EditorTextStyle.INDENT);
            }
        });

        findViewById(R.id.action_outdent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.updateTextStyle(EditorTextStyle.OUTDENT);
            }
        });

        findViewById(R.id.action_bulleted).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.insertList(false);
            }
        });

        findViewById(R.id.action_color).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.updateTextColor("#FF3333");
            }
        });

        findViewById(R.id.action_unordered_numbered).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.insertList(true);
            }
        });

        findViewById(R.id.action_hr).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.insertDivider();
            }
        });

        findViewById(R.id.action_insert_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.openImagePicker();
            }
        });

        findViewById(R.id.action_insert_link).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.insertLink();
            }
        });


        findViewById(R.id.action_erase).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.clearAllContents();
            }
        });

        findViewById(R.id.action_blockquote).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.updateTextStyle(EditorTextStyle.BLOCKQUOTE);
            }
        });

    }

    /**
     * Activity result for upload photo
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == editor.PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                editor.insertImage(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            // editor.RestoreState();
        }
    }

}