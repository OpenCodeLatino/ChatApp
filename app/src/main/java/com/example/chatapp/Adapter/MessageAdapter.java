package com.example.chatapp.Adapter;


import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.chatapp.Messages.MessageContract;
import com.example.chatapp.Model.Message;
import com.example.chatapp.R;
import com.example.chatapp.Utils;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Message> mMessages = new ArrayList<>();
    private static final int SENT_MESSAGE_TEXT = 0;
    private static final int SENT_MESSAGE_IMAGE = 1;
    private static final int RECEIVED_MESSAGE_TEXT = 2;
    private static final int RECEIVED_MESSAGE_IMAGE = 3;
    private Context mContext;
    private MessageContract.MessageListener mMessageListener;

    public MessageAdapter(Context mContext, MessageContract.MessageListener mMessageListener) {
        this.mContext = mContext;
        this.mMessageListener = mMessageListener;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == SENT_MESSAGE_TEXT) {
            View itemView = LayoutInflater.from(mContext).inflate(R.layout.list_item_messages_text_right, parent, false);
            return new SentMessageTextViewHolder(itemView);
        } else if (viewType == SENT_MESSAGE_IMAGE) {
            View itemView = LayoutInflater.from(mContext).inflate(R.layout.list_item_messages_image_right, parent, false);
            return new SentMessageImageViewHolder(itemView);
        } else if (viewType == RECEIVED_MESSAGE_TEXT) {
            View itemView = LayoutInflater.from(mContext).inflate(R.layout.list_item_messages_text_left, parent, false);
            return new ReceivedMessageTextViewHolder(itemView);
        } else {
            View itemView = LayoutInflater.from(mContext).inflate(R.layout.list_item_messages_image_left, parent, false);
            return new ReceivedMessageImageViewHolder(itemView);
        }
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (getItemViewType(position) == SENT_MESSAGE_TEXT) {
            ((SentMessageTextViewHolder) holder).setSentMessageText(mMessages.get(position));
        } else if (getItemViewType(position) == SENT_MESSAGE_IMAGE) {
            ((SentMessageImageViewHolder) holder).setSentMessageImage(mMessages.get(position));
        } else if (getItemViewType(position) == RECEIVED_MESSAGE_TEXT) {
            ((ReceivedMessageTextViewHolder) holder).setReceivedMessageText(mMessages.get(position));
        } else {
            ((ReceivedMessageImageViewHolder) holder).setReceivedMessageImage(mMessages.get(position));
        }
    }


    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    public void addMessages(Message message) {
        mMessages.add(message);
        notifyItemChanged(mMessages.size() - 1);
    }

    public class SentMessageTextViewHolder extends RecyclerView.ViewHolder {

        TextView time;
        TextView message;

        SentMessageTextViewHolder(@NonNull View itemView) {
            super(itemView);

            time = itemView.findViewById(R.id.time);
            message = itemView.findViewById(R.id.message_body);
        }

        private void setSentMessageText(Message msg) {
            time.setText(Utils.convertTime(msg.getTimestamp()));
            message.setText(msg.getMessage());
        }
    }

    public class SentMessageImageViewHolder extends RecyclerView.ViewHolder {

        TextView time;
        ImageView messageImage;

        SentMessageImageViewHolder(@NonNull View itemView) {
            super(itemView);

            time = itemView.findViewById(R.id.time);
            messageImage = itemView.findViewById(R.id.image_body);
        }

        private void setSentMessageImage(final Message msg) {
            time.setText(Utils.convertTime(msg.getTimestamp()));
            Glide.with(mContext).asBitmap()
                    .load(msg.getImage())
                    .into(messageImage);
            messageImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mMessageListener.onImageClick(msg.getImage());

                }
            });
        }
    }

    public class ReceivedMessageTextViewHolder extends RecyclerView.ViewHolder {

        TextView time;
        TextView name;
        TextView message;
        CircleImageView avatar;


        ReceivedMessageTextViewHolder(@NonNull View itemView) {
            super(itemView);

            avatar = itemView.findViewById(R.id.avatar);
            time = itemView.findViewById(R.id.time);
            name = itemView.findViewById(R.id.name);
            message = itemView.findViewById(R.id.message_body);
        }

        private void setReceivedMessageText(Message msg) {
            time.setText(Utils.convertTime(msg.getTimestamp()));
            name.setText(msg.getSenderName());
            message.setText(msg.getMessage());
            Uri avatarUri = Uri.parse(msg.getAvatar());
            Glide.with(mContext).load(avatarUri).into(avatar);
        }
    }

    public class ReceivedMessageImageViewHolder extends RecyclerView.ViewHolder {

        TextView time;
        TextView name;
        ImageView messageImage;
        CircleImageView avatar;

        ReceivedMessageImageViewHolder(@NonNull View itemView) {
            super(itemView);

            avatar = itemView.findViewById(R.id.avatar);
            time = itemView.findViewById(R.id.time);
            name = itemView.findViewById(R.id.name);
            messageImage = itemView.findViewById(R.id.image_body);
        }

        private void setReceivedMessageImage(final Message msg) {
            time.setText(Utils.convertTime(msg.getTimestamp()));
            name.setText(msg.getSenderName());
            Glide.with(mContext).asBitmap()
                    .load(msg.getImage())
                    .into(messageImage);
            Uri avatarUri = Uri.parse(msg.getAvatar());
            Glide.with(mContext).load(avatarUri).into(avatar);

            messageImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mMessageListener.onImageClick(msg.getImage());
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (FirebaseAuth.getInstance().getUid().equalsIgnoreCase(mMessages.get(position).getSenderUid()) && mMessages.get(position).getMessageType().equalsIgnoreCase("text")) {
            return SENT_MESSAGE_TEXT;
        } else if (FirebaseAuth.getInstance().getUid().equalsIgnoreCase(mMessages.get(position).getSenderUid()) && mMessages.get(position).getMessageType().equalsIgnoreCase("image")) {
            return SENT_MESSAGE_IMAGE;
        } else if (!FirebaseAuth.getInstance().getUid().equalsIgnoreCase(mMessages.get(position).getSenderUid()) && mMessages.get(position).getMessageType().equalsIgnoreCase("text")) {
            return RECEIVED_MESSAGE_TEXT;
        } else {
            return RECEIVED_MESSAGE_IMAGE;
        }
    }
}