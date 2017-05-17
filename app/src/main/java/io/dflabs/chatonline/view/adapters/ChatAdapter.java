package io.dflabs.chatonline.view.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import io.dflabs.chatonline.R;
import io.dflabs.chatonline.model.pojos.Message;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by dflabs on 5/11/17.
 * ChatOnline
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {
    private final Context context;
    private RealmList<Message> messages = new RealmList<>();

    public ChatAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_chat, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ChatViewHolder holder, int position) {
        Message message = messages.get(position);
        holder.messageTextView.setText(message.message);
        holder.usernameTextView.setText(message.username);
    }

    @Override
    public int getItemCount() {
        return this.messages.size();
    }

    public void update(RealmList<Message> results) {
        this.messages = results;
        notifyDataSetChanged();
    }

    class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView usernameTextView;
        TextView messageTextView;

        ChatViewHolder(View itemView) {
            super(itemView);
            usernameTextView = (TextView) itemView.findViewById(R.id.item_chat_username);
            messageTextView = (TextView) itemView.findViewById(R.id.item_chat_message);
        }
    }
}
