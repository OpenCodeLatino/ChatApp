package com.example.chatapp.ChatRoom;

import androidx.annotation.NonNull;
import com.example.chatapp.Model.ChatRoom;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class ChatRoomInteractor implements ChatRoomContract.Interactor {

    private DatabaseReference mChatRoomReference;
    private ChatRoom chatRoom;
    private ChatRoomPresenter chatRoomPresenter;


    ChatRoomInteractor(ChatRoomPresenter chatRoomPresenter) {
        mChatRoomReference = FirebaseDatabase.getInstance().getReference("chatrooms");
        chatRoom = new ChatRoom();
        this.chatRoomPresenter = chatRoomPresenter;
    }

    @Override
    public void pushChatRoomToFirebase(String roomName, String roomDescription) {
        chatRoom.setTitle(roomName);
        chatRoom.setDescription(roomDescription);
        chatRoom.setUid(mChatRoomReference.push().getKey());
        mChatRoomReference.child(chatRoom.getUid()).setValue(chatRoom);
    }

    @Override
    public void getAllChatRooms() {
        mChatRoomReference.orderByChild("lastMessageTime").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    chatRoom = ds.getValue(ChatRoom.class);
                    chatRoomPresenter.showChatRooms(chatRoom);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}

