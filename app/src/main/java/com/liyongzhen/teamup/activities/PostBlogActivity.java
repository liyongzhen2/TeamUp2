package com.liyongzhen.teamup.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.liyongzhen.teamup.R;
import com.liyongzhen.teamup.models.BlogModel;
import com.liyongzhen.teamup.utils.CommonUtil;
import com.liyongzhen.teamup.utils.Constants;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

public class PostBlogActivity extends AppCompatActivity {

    private String blogType;

    private ImageButton pickPhoto, postBlog;
    private ImageView blogImage;
    private EditText blogContent;
    private ProgressBar mProgressBar;

    private Uri imageUri;
    private int imageWidth, imageHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_blog);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        blogType = getIntent().getStringExtra(Constants.EXTRA_POSTTYPE);

        mProgressBar = (ProgressBar) findViewById(R.id.progress);
        mProgressBar.setVisibility(View.GONE);
        postBlog = (ImageButton) findViewById(R.id.postBlog);
        pickPhoto = (ImageButton) findViewById(R.id.pickPhoto);
        blogImage = (ImageView) findViewById(R.id.blogImage);
        blogContent = (EditText) findViewById(R.id.blogContent);

        postBlog.setOnClickListener(mOnClickListener);
        pickPhoto.setOnClickListener(mOnClickListener);
    }

    private void postBlog() {
        mProgressBar.setVisibility(View.VISIBLE);
        if (imageUri != null)
            uploadImage();
        else
            updateFirebase("");
    }

    private void uploadImage() {

        imageWidth = blogImage.getWidth();
        imageHeight = blogImage.getHeight();

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        String imageName = String.valueOf(System.currentTimeMillis());
        StorageReference screenRef = storageRef.child("post_images").child(imageName);

        File f = null;
        try {
            f = CommonUtil.getFileRezie(new URI(imageUri.toString()));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        UploadTask uploadTask = screenRef.putFile(Uri.parse(f.toURI().toString()));
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(getBaseContext(), "Uploading error" + exception.toString(), Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                updateFirebase(downloadUrl.toString());
            }
        });
    }

    private void updateFirebase(String imageUri) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("posts").child("posts");
        DatabaseReference childRef = myRef.push();

        String fromId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String postId = childRef.getKey();

        String blogText = blogContent.getText().toString();
        long time = CommonUtil.getUTCSecond();

        BlogModel model = new BlogModel(fromId, postId, blogText, time, imageUri, imageWidth, imageHeight);
        DatabaseReference upRef = database.getReference().child("user-posts").child("posts").child(fromId).child(postId);
        upRef.setValue(1);
        childRef.setValue(model, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError == null) {
                    finish();
                }
            }
        });
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.postBlog:
                    postBlog();
                    break;
                case R.id.pickPhoto:
                    CropImage.activity()
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .setActivityTitle("Take Image")
                            .setCropShape(CropImageView.CropShape.RECTANGLE)
                            .setScaleType(CropImageView.ScaleType.FIT_CENTER)
                            .start(PostBlogActivity.this);
                    break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    if (result.getUri() != null) {
                        imageUri = result.getUri();
                        Picasso.with(this).load(imageUri)
                                .into(blogImage);
                    } else {
                        Log.w("fail", "File URI is null");
                    }
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Toast.makeText(this, "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
