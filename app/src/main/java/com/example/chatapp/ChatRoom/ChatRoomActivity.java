package com.example.chatapp.ChatRoom;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.chatapp.Adapter.ChatRoomAdapter;
import com.example.chatapp.Messages.MessageActivity;
import com.example.chatapp.Model.ChatRoom;
import com.example.chatapp.R;
import com.example.chatapp.Utils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

public class ChatRoomActivity extends AppCompatActivity implements ChatRoomContract.View {

    private EditText titleEditText;
    private EditText descriptionEditText;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ChatRoomPresenter chatRoomPresenter;
    private ChatRoomAdapter mChatRoomAdapter;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatroom);
        chatRoomPresenter = new ChatRoomPresenter(this);
        mRecyclerView = findViewById(R.id.chatRoomRecyclerView);
        mSwipeRefreshLayout = findViewById(R.id.swipeRefresh);
        mChatRoomAdapter = new ChatRoomAdapter(roomListener);
        mRecyclerView.setAdapter(mChatRoomAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(layoutManager);
        chatRoomPresenter.subscribe();
        swipeRefresh();
    }

    public void addChatRoom(View view) {
        //FirebaseAuth.getInstance().signOut();

        //Show alertdialog when creating new room
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_dialog, null);
        titleEditText = dialogView.findViewById(R.id.edit_title);
        descriptionEditText = dialogView.findViewById(R.id.edit_description);
        dialogBuilder.setTitle("New Chatroom");

        dialogBuilder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        })
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String roomTitle = titleEditText.getText().toString();
                        String roomDescription = descriptionEditText.getText().toString();
                        chatRoomPresenter.createRoom(roomTitle, roomDescription);
                    }
                });


        dialogBuilder.setView(dialogView);
        dialogBuilder.show();
    }

    @Override
    public void showAddedChatRooms(ChatRoom chatRoom) {
        mChatRoomAdapter.addChatRooms(chatRoom);
    }

    //Go to Message activity
    @Override
    public void showMessagesUi(String uid) {
        Intent intent = new Intent(getApplicationContext(), MessageActivity.class);
        intent.putExtra("chatroomuid", uid);
        startActivity(intent);
    }

    //Swipe down to get newly created chat rooms
    private void swipeRefresh() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mChatRoomAdapter.clear();
                chatRoomPresenter.subscribe();
                mChatRoomAdapter.notifyDataSetChanged();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

    }

    @Override
    public void onChatRoomCreatedSuccess() {
        Utils.showMessage(this, "Success");
    }

    @Override
    public void onChatRoomCreatedFailed() {
        Utils.showMessage(this, "Failed");
    }


    ChatRoomContract.ChatRoomListener roomListener = new ChatRoomContract.ChatRoomListener() {
        @Override
        public void onChatRoomClicked(ChatRoom clickedChatRoom) {
            chatRoomPresenter.openMessages(clickedChatRoom);
        }
    };
}