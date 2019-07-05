package com.example.chatapp.ChatRoom;

import com.example.chatapp.Model.ChatRoom;

public class ChatRoomPresenter implements ChatRoomContract.Presenter {

    private ChatRoomInteractor mChatRoomInteractor;
    private ChatRoomContract.View mChatRoomView;

    ChatRoomPresenter(ChatRoomContract.View chatRoomView) {
        this.mChatRoomView = chatRoomView;
        mChatRoomInteractor = new ChatRoomInteractor(this);
    }

    @Override
    public void createRoom(String roomName, String roomDescription) {
        if (roomName.isEmpty()) {
            mChatRoomView.onChatRoomCreatedFailed();
        } else if (roomDescription.isEmpty()) {
            mChatRoomView.onChatRoomCreatedFailed();
        } else {
            mChatRoomInteractor.pushChatRoomToFirebase(roomName, roomDescription);
            mChatRoomView.onChatRoomCreatedSuccess();
        }
    }

    @Override
    public void showChatRooms(ChatRoom chatRoom) {
        mChatRoomView.showAddedChatRooms(chatRoom);
    }


    @Override
    public void subscribe() {
        mChatRoomInteractor.getAllChatRooms();
    }

    @Override
    public void openMessages(ChatRoom chatRoom) {
        mChatRoomView.showMessagesUi(chatRoom.getUid());
    }
}