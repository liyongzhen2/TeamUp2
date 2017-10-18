package com.liyongzhen.teamup.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.liyongzhen.teamup.R;
import com.liyongzhen.teamup.models.User;
import com.liyongzhen.teamup.utils.CommonValuables;
import com.liyongzhen.teamup.utils.UiUtil;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;

public class ProfileActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener , View.OnClickListener{

    private ProgressBar mProgressBar;
    private User user;

    private ImageView imageViewPhoto;
    private TextView textViewBirthday;
    private EditText editTextFirstName, editTextLastName;
    private RadioGroup radioGroupGender;
    private CheckBox checkBox1, checkBox2,checkBox3,checkBox4;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Profile");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        findViewById(R.id.button_save).setOnClickListener(this);

        imageViewPhoto = (ImageView) findViewById(R.id.imageViewPhoto);
        textViewBirthday = (TextView) findViewById(R.id.textViewBithday);
        editTextFirstName = (EditText) findViewById(R.id.editText_profile_firstname);
        editTextLastName = (EditText) findViewById(R.id.editText_profile_lastname);
        radioGroupGender = (RadioGroup) findViewById(R.id.radio_group);

        checkBox1 = (CheckBox) findViewById(R.id.checkBox_experience1);
        checkBox2 = (CheckBox) findViewById(R.id.checkBox_experience2);
        checkBox3 = (CheckBox) findViewById(R.id.checkBox_experience3);
        checkBox4 = (CheckBox) findViewById(R.id.checkBox_experience4);
        spinner = (Spinner) findViewById(R.id.spinner_sport);

        mProgressBar = (ProgressBar) findViewById(R.id.progress);
        mProgressBar.setVisibility(View.GONE);
        setUserInfo();
    }

    private void setUserInfo() {
        Log.e(CommonValuables.TAG, "set UserInfor");
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase.getInstance().getReference("users").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                if (user != null){
                    Log.e(CommonValuables.TAG, "set UserInfor != null");
                    editTextFirstName.setText(user.getFirstname());
                    editTextLastName.setText(user.getLastname());
                    textViewBirthday.setText(user.getDateofBirth());
                    ((RadioButton)radioGroupGender.getChildAt(user.getGender())).setChecked(true);
                    checkBox1.setChecked(user.getExperience1());
                    checkBox2.setChecked(user.getExperience2());
                    checkBox3.setChecked(user.getExperience3());
                    checkBox4.setChecked(user.getExperience4());
                    spinner.setSelection(user.getChooseSport());

                    if (user.getPhotoUrl().isEmpty())
                        imageViewPhoto.setImageResource(R.drawable.photo_default);
                    else
                        Picasso.with(ProfileActivity.this).load(user.getPhotoUrl()).into(imageViewPhoto);
                    Log.d(CommonValuables.TAG, user.getPhotoUrl());
                    Log.e(CommonValuables.TAG, user.getPhotoUrl());

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void onClickPhoto(View view){
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setActivityTitle("Take Image")
                .setCropShape(CropImageView.CropShape.OVAL)
                .setAspectRatio(1,1)
                .setShowCropOverlay(true)
                .setScaleType(CropImageView.ScaleType.FIT_CENTER)
                .start(ProfileActivity.this);
    }

    public void onClickDatePicker(View view){
        String str = textViewBirthday.getText().toString();
        int[] intDate = CommonValuables.convertDateStringToInt(str);
        DatePickerDialog dialog = new DatePickerDialog(this, this, intDate[0], intDate[1], intDate[2]);
        dialog.show();
    }

    private void attemptUpdateProfile() {
        mProgressBar.setVisibility(View.VISIBLE);
        // photo upload
        imageViewPhoto.setDrawingCacheEnabled(true);
        imageViewPhoto.buildDrawingCache();
        Bitmap bitmap = imageViewPhoto.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        String uID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference mountainsRef = storageRef.child("user_images").child(uID+".jpg");
        UploadTask uploadTask = mountainsRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.

                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                String firstName = editTextFirstName.getText().toString();
                String lastName = editTextLastName.getText().toString();
                String birthday = textViewBirthday.getText().toString();
                int gender = 0;
                if(radioGroupGender.getCheckedRadioButtonId() == R.id.radio_female)
                    gender = 1;
                boolean check1 = checkBox1.isChecked();
                boolean check2 = checkBox2.isChecked();
                boolean check3 = checkBox3.isChecked();
                boolean check4 = checkBox4.isChecked();
                int sport = spinner.getSelectedItemPosition();
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                User updatedUser = new User(firstName  , lastName  , gender, birthday,
                        1, sport, downloadUrl.toString(), check1, check2, check3, check4);
                FirebaseDatabase.getInstance().getReference("users").child(userId).setValue(updatedUser)
                        .addOnCompleteListener(ProfileActivity.this, new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                mProgressBar.setVisibility(View.GONE);
                                UiUtil.showMessageDialog(ProfileActivity.this, "Nice", "Profile updated successfully", "OK");
                            }
                        })
                        .addOnFailureListener(ProfileActivity.this, new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                mProgressBar.setVisibility(View.GONE);
                                UiUtil.showMessageDialog(ProfileActivity.this, "Oops!", "Something went wrong, please try again later", "OK");
                            }
                        });
            }
        });
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        Log.d(CommonValuables.TAG, i + ":" + i1 + ":"+ i2);
        String str = i+"/"+ i1+"/"+i2;
        textViewBirthday.setText(str);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    if (result.getUri() != null) {
                        imageViewPhoto.setImageURI(result.getUri());
//                        Picasso.with(ProfileActivity.this).load(result.getUri()).into(imageViewPhoto);
                    } else {
                        Log.d(CommonValuables.TAG, "File URI is null");
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

    @Override
    public void onClick(View view){
        if(view.getId() == R.id.button_save){
            attemptUpdateProfile();
        }
    }
}
