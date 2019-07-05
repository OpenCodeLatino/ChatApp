package com.example.chatapp.Messages;


import android.net.Uri;

import com.example.chatapp.Model.Message;

public class MessagePresenter implements MessageContract.Presenter {

    private MessageInteractor mMessageInteractor;
    private MessageContract.View mMessageView;

    MessagePresenter(MessageContract.View messageView) {
        this.mMessageView = messageView;
        mMessageInteractor = new MessageInteractor(this);
    }

    @Override
    public void creatTexteMessage(String message, String uid) {
        if(message.trim().isEmpty()) {
            mMessageView.onMessageSentFailed();
        } else {
            mMessageInteractor.pushMessageToFirebsae(message, uid);
            mMessageView.onMessageSentSuccess();
        }
    }

    @Override
    public void createImageMessage(Uri imageUri, String uid) {
        mMessageInteractor.pushImagesToFirebase(imageUri, uid);
    }

    @Override
    public void showMessages(Message message) {
        mMessageView.showMessages(message);
    }

    @Override
    public void getMessages(String uid) {
        mMessageInteractor.getChatRoomMessages(uid);
    }

    @Override
    public void onCameraClick() {
        mMessageView.checkPermissions();
    }

    @Override
    public void onGalleryClick() {
        mMessageView.openGallery();
    }

    @Override
    public void cameraIntent() {
        mMessageView.openCamera();
    }


    void openMessages(String imageUri) {
        mMessageView.showImageFullScreenUI(imageUri);
    }
}
