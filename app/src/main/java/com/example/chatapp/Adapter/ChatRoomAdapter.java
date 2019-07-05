package com.example.chatapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.chatapp.ChatRoom.ChatRoomContract;
import com.example.chatapp.Model.ChatRoom;
import com.example.chatapp.R;

import java.util.ArrayList;
import java.util.List;


public class ChatRoomAdapter extends RecyclerView.Adapter<ChatRoomAdapter.ChatRoomViewHolder> {

    private List<ChatRoom> mChatRooms = new ArrayList<>();
    private ChatRoomContract.ChatRoomListener mChatRoomListener;

    public ChatRoomAdapter(ChatRoomContract.ChatRoomListener chatRoomListener) {
        this.mChatRoomListener = chatRoomListener;
    }

    @NonNull
    @Override
    public ChatRoomAdapter.ChatRoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_chatrooms, parent, false);
        return new ChatRoomViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatRoomAdapter.ChatRoomViewHolder holder, final int position) {
        holder.mRoomTitle.setText(mChatRooms.get(position).getTitle());
        holder.mRoomDescription.setText(mChatRooms.get(position).getDescription());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mChatRoomListener.onChatRoomClicked(mChatRooms.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        System.out.println(mChatRooms.size());
        return mChatRooms.size();
    }

    public void clear() {
        mChatRooms.clear();
    }

    public void addChatRooms(ChatRoom chatRoom) {
        mChatRooms.add(chatRoom);
        notifyItemChanged(mChatRooms.size() - 1);
    }

    static class ChatRoomViewHolder extends RecyclerView.ViewHolder {

        private TextView mRoomTitle;
        private TextView mRoomDescription;

        ChatRoomViewHolder(@NonNull View itemView) {
            super(itemView);

            mRoomTitle = itemView.findViewById(R.id.roomTitle);
            mRoomDescription = itemView.findViewById(R.id.roomDescription);
        }
    }
}
