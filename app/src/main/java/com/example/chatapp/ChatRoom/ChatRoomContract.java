package com.example.chatapp.ChatRoom;


import com.example.chatapp.Model.ChatRoom;

public interface ChatRoomContract {

    interface View {
        void onChatRoomCreatedSuccess();

        void onChatRoomCreatedFailed();

        void showAddedChatRooms(ChatRoom chatRoom);

        void showMessagesUi(String uid);
    }

    interface Presenter {
        void createRoom(String roomName, String roomDescription);

        void showChatRooms(ChatRoom chatRoom);

        void subscribe();

        void openMessages(ChatRoom chatRoom);

    }

    interface Interactor {
        void pushChatRoomToFirebase(String roomName, String roomDescription);

        void getAllChatRooms();
    }

    interface ChatRoomListener {
        void onChatRoomClicked(ChatRoom clickedChatRoom);
    }
}
