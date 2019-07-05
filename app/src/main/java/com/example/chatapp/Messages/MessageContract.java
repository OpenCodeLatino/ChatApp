package com.example.chatapp.Messages;

import android.net.Uri;

import com.example.chatapp.Model.Message;

public interface MessageContract {

    interface View {
        void onMessageSentSuccess();

        void onMessageSentFailed();

        void showMessages(Message message);

        void openGallery();

        void openCamera();

        void galleryButton();

        void cameraButton();

        void showImageFullScreenUI(String imageUri);

        void checkPermissions();
    }

    interface Presenter {
        void creatTexteMessage(String message, String uid);

        void createImageMessage(Uri imageUri, String uid);

        void showMessages(Message message);

        void getMessages(String uid);

        void onCameraClick();

        void onGalleryClick();

        void cameraIntent();
    }

    interface Interactor {
        void pushMessageToFirebsae(String message, String uid);

        void pushImagesToFirebase(Uri imageUri, String uid);

        void getChatRoomMessages(String uid);
            }

    interface MessageListener{
        void onImageClick(String imageUri);
    }


}
