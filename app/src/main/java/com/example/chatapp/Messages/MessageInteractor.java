package com.example.chatapp.Messages;


import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.chatapp.Model.Message;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

public class MessageInteractor implements MessageContract.Interactor {

    private DatabaseReference mMessageReference;
    private DatabaseReference mChatRoomReference;
    private StorageReference mStorageRef;
    private Message mMessage;
    private FirebaseAuth mAuth;
    private MessagePresenter mMessagePresenter;

    MessageInteractor(MessagePresenter messagePresenter) {
        mMessageReference = FirebaseDatabase.getInstance().getReference("messages");
        mChatRoomReference = FirebaseDatabase.getInstance().getReference("chatrooms");
        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        mAuth = FirebaseAuth.getInstance();
        this.mMessagePresenter = messagePresenter;
    }

    @Override
    public void pushMessageToFirebsae(String message, String uid) {
        mMessage = new Message();
        long messageTime = System.currentTimeMillis();

        mMessage.setSenderName(mAuth.getCurrentUser().getDisplayName());
        mMessage.setSenderUid(mAuth.getCurrentUser().getUid());
        mMessage.setAvatar(mAuth.getCurrentUser().getPhotoUrl().toString());
        mMessage.setMessage(message);
        mMessage.setTimestamp(messageTime);
        mMessage.setMessageType("text");
        mMessageReference.child(uid).push().setValue(mMessage);
        mChatRoomReference.child(uid).child("lastMessageTime").setValue(messageTime);
    }

    @Override
    public void pushImagesToFirebase(Uri imageUri, final String uid) {
        String pushId = mMessageReference.push().getKey();
        final StorageReference fileReference = mStorageRef.child("message_images").child(pushId + ".jpg");

        StorageTask<UploadTask.TaskSnapshot> uploadTask = fileReference.putFile(imageUri);
        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                return fileReference.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    String mUri = downloadUri.toString();
                    mMessage = new Message();
                    long messageTime = System.currentTimeMillis();

                    mMessage.setSenderName(mAuth.getCurrentUser().getDisplayName());
                    mMessage.setSenderUid(mAuth.getCurrentUser().getUid());
                    mMessage.setAvatar(mAuth.getCurrentUser().getPhotoUrl().toString());
                    mMessage.setTimestamp(messageTime);
                    mMessage.setMessageType("image");
                    mMessage.setImage(mUri);
                    mMessageReference.child(uid).push().setValue(mMessage);
                    mChatRoomReference.child(uid).child("lastMessageTime").setValue(messageTime);
                } else {

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });
    }

    @Override
    public void getChatRoomMessages(String uid) {
        Query mMessageQuery = mMessageReference.child(uid).limitToLast(50);

        mMessageQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                mMessage = dataSnapshot.getValue(Message.class);
                mMessagePresenter.showMessages(mMessage);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
