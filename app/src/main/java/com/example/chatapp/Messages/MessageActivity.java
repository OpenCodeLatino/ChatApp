package com.example.chatapp.Messages;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.chatapp.Adapter.MessageAdapter;
import com.example.chatapp.ChatRoom.ChatRoomActivity;
import com.example.chatapp.ImageFullScreenActivity;
import com.example.chatapp.Model.Message;
import com.example.chatapp.R;
import com.example.chatapp.Utils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MessageActivity extends AppCompatActivity implements MessageContract.View {

    MessagePresenter mMessagePresenter;
    String mChatRoomUid;
    RecyclerView mRecyclerView;
    MessageAdapter mMessageAdapter;
    EditText mMessageText;
    ImageButton mSendButton;
    ImageButton mGalleryButton;
    ImageButton mCameraButton;
    static final int IMAGE_REQUEST = 1;
    static final int CAMERA_REQUEST = 2;
    Uri imageUri;
    String mCurrentPhotoPath;
    File photoFile = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        mMessagePresenter = new MessagePresenter(this);
        mRecyclerView = findViewById(R.id.messageRecyclerView);
        mMessageText = findViewById(R.id.edittext_chat_message);
        mSendButton = findViewById(R.id.button_send_message);
        mGalleryButton = findViewById(R.id.button_gallery);
        mCameraButton = findViewById(R.id.button_camera);
        mMessageAdapter = new MessageAdapter(this, imageListener);
        mRecyclerView.setAdapter(mMessageAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mChatRoomUid = getIntent().getStringExtra("chatroomuid");
        mMessagePresenter.getMessages(mChatRoomUid);
        sendMessageButton();
        galleryButton();
        cameraButton();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_REQUEST) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mMessagePresenter.cameraIntent();
            }
        }
    }

    public void sendMessageButton() {
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMessagePresenter.creatTexteMessage(mMessageText.getText().toString(), mChatRoomUid);
                mMessageText.getText().clear();
            }
        });
    }


    @Override
    public void galleryButton() {
        mGalleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMessagePresenter.onGalleryClick();
            }
        });
    }

    @Override
    public void cameraButton() {
        mCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMessagePresenter.onCameraClick();
            }
        });
    }

    @Override
    public void showImageFullScreenUI(String imageUri) {
        Intent intent = new Intent(this, ImageFullScreenActivity.class);
        intent.putExtra("imageUri", imageUri);
        startActivity(intent);
    }

    @Override
    public void checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA},
                        CAMERA_REQUEST);
            } else {
                mMessagePresenter.cameraIntent();
            }
        }
    }

    @Override
    public void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_REQUEST);
    }

    @Override
    public void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".fileprovider", photoFile);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Gallery result
        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            imageUri = data.getData();
            mMessagePresenter.createImageMessage(imageUri, mChatRoomUid);
        }

        //Camera result
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            imageUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".fileprovider", photoFile);
            mMessagePresenter.createImageMessage(imageUri, mChatRoomUid);
        }
    }

    @Override
    public void showMessages(Message message) {
        mMessageAdapter.addMessages(message);
        mRecyclerView.scrollToPosition(mMessageAdapter.getItemCount() - 1);
    }

    MessageContract.MessageListener imageListener = new MessageContract.MessageListener() {
        @Override
        public void onImageClick(String imageUri) {
            mMessagePresenter.openMessages(imageUri);
        }
    };

    @Override
    public void onMessageSentSuccess() {
        Utils.setIntent(this, ChatRoomActivity.class);
    }

    @Override
    public void onMessageSentFailed() {
        Utils.showMessage(this, getString(R.string.login_failed));
    }
}
