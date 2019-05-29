package com.example.myapplication.activitycontrol;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.models.Message;

import java.util.TreeMap;


// this will show how we will show our messages in the list view
public class MessageState extends BaseAdapter {
    public TreeMap<Integer, Message> messages = new TreeMap<Integer, Message>();

    Context context;

    public MessageState(Context context) {
        this.context = context;
    }

    public void add(Integer index, Message message) {
        this.messages.put(index, message);
        notifyDataSetChanged(); // to render the list we need to notify
    }

    public void addLast(Message message) {
        this.messages.put(0,message);
        notifyDataSetChanged(); // to render the list we need to notify
    }

    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public Object getItem(int i) {
        return messages.get(i);
    }

    //get element of that index number
    @Override
    public long getItemId(int i) {
        return i;
    }

    // This is the backbone of the class, it handles the creation of single ListView row (chat bubble)
    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {

        MessageViewHolder holder = new MessageViewHolder();
        LayoutInflater messageInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        Message message = messages.get(i);

        try {
            // this message was sent by us so let's create a basic chat bubble on the right
            if (message.belongsToCurrentUser()) {
                convertView = messageInflater.inflate(R.layout.my_message, null);
                holder.messageBody = (TextView) convertView.findViewById(R.id.message_body);
                convertView.setTag(holder);
                holder.messageBody.setText(message.getText());
            } else {
                convertView = messageInflater.inflate(R.layout.other_message, null);
                holder.messageBody = (TextView) convertView.findViewById(R.id.message_body);
                holder.name = (TextView) convertView.findViewById(R.id.name);
                convertView.setTag(holder);

                holder.name.setText(message.getUsername());
                holder.messageBody.setText(message.getText());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return convertView;
    }

      class MessageViewHolder {
        public TextView name;
        public TextView messageBody;
    }
}
