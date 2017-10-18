package com.liyongzhen.teamup.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.liyongzhen.teamup.R;
import com.liyongzhen.teamup.adapters.ChatAdapter;
import com.liyongzhen.teamup.models.ChatModel;
import com.liyongzhen.teamup.models.User;
import com.liyongzhen.teamup.utils.CommonUtil;
import com.liyongzhen.teamup.utils.Constants;
import com.liyongzhen.teamup.utils.UiUtil;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class ChatRoomActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView mRecyclerView;
    private ImageView pickerView, sendView;
    private EditText messageET;
    private User friend;

    private ChatAdapter adapter;
    private ArrayList<ChatModel> chatData;
    private long lastTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        lastTime = CommonUtil.getUTCSecond() - 3600 * 24 * 7;

        String fId = getIntent().getStringExtra(Constants.EXTRA_USERID);

        pickerView = (ImageView) findViewById(R.id.pickPhoto);
        sendView = (ImageView) findViewById(R.id.sendBtn);
        pickerView.setOnClickListener(mOnClickListener);
        sendView.setOnClickListener(mOnClickListener);

        messageET = (EditText) findViewById(R.id.messageET);
        mRecyclerView = (RecyclerView) findViewById(R.id.chatList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
//        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        chatData = new ArrayList<>();
        adapter = new ChatAdapter(this, chatData);
        mRecyclerView.setAdapter(adapter);

        setTitleWithUserName(fId);
    }

    private void setTitleWithUserName(String id) {
        Query query = FirebaseDatabase.getInstance().getReference().child("users").child(id);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                friend = dataSnapshot.getValue(User.class);
                if (friend != null) {
                    toolbar.setTitle(friend.getUsername());
                    friend.setuId(dataSnapshot.getKey());
                    adapter.setFriendUrl(friend.getPhotoUrl());

                    fetchChatData();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void fetchChatData() {
        String uId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Query query = FirebaseDatabase.getInstance().getReference().child("user-messages").child(uId).child(friend.getuId());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot item : dataSnapshot.getChildren()) {
                    FirebaseDatabase.getInstance().getReference().child("messages").child(item.getKey()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            ChatModel model = dataSnapshot.getValue(ChatModel.class);
                            if (model.getTimestamp() > lastTime) {
                                chatData.add(model);
                                lastTime = model.getTimestamp();
                                adapter.notifyItemInserted(chatData.size() - 1);
                                mRecyclerView.scrollToPosition(chatData.size() -1);
                            } else {

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.pickPhoto:
                    CropImage.activity()
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .setActivityTitle("Take Image")
                            .setCropShape(CropImageView.CropShape.RECTANGLE)
                            .setScaleType(CropImageView.ScaleType.FIT_CENTER)
                            .start(ChatRoomActivity.this);
                    break;
                case R.id.sendBtn:
                    String chatContent = messageET.getText().toString();
                    if (chatContent.isEmpty())
                        return;
                    sendChat(chatContent, "");
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
                        uploadImage(result.getUri());
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

    private void uploadImage(Uri url) {

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        String imageName = String.valueOf(System.currentTimeMillis());
        StorageReference screenRef = storageRef.child("message_images").child(imageName);

        File f = null;
        try {
            f = CommonUtil.getFileRezie(new URI(url.toString()));
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
                sendChat("", downloadUrl.toString());
            }
        });
    }

    private void sendChat(String chatContent, String imageUrl) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("messages");
        DatabaseReference childRef = myRef.push();
        String chatKey = childRef.getKey();
        String fromId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String toId = friend.getuId();

        long time = CommonUtil.getUTCSecond();
        int imageWidth, imageHeight;
        if (imageUrl.isEmpty()) {
            imageHeight = imageWidth = 0;
        } else {
            imageHeight = imageWidth = 300;
        }

        ChatModel model = new ChatModel(fromId, toId, chatContent, time, imageUrl, imageWidth, imageHeight);
        DatabaseReference fromRef = database.getReference().child("user-messages").child(fromId).child(toId).child(chatKey);
        fromRef.setValue(1);
        DatabaseReference toRef = database.getReference().child("user-messages").child(toId).child(fromId).child(chatKey);
        toRef.setValue(1);

        childRef.setValue(model, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError == null) {
                    messageET.setText("");
                    UiUtil.hideKeyboard(ChatRoomActivity.this, messageET);
                    mRecyclerView.scrollToPosition(chatData.size() - 1);
                }
            }
        });
    }

}
