package com.mdp.fypapp.Adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mdp.fypapp.Date.DateToString;
import com.mdp.fypapp.Model.Chat;
import com.mdp.fypapp.R;

import org.apache.http.client.utils.DateUtils;

import java.util.List;
public class ChatAdapter extends BaseAdapter {
    private List<Chat> list;
    public ChatAdapter(List<Chat> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.isEmpty() ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        Chat chatMessage = list.get(position);
        // receive：0，send：1
        if (chatMessage.getType() == Chat.Type.INCOUNT) {
            return 0;
        }
        return 1;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Chat chatMessage = list.get(position);
        if (convertView == null) {
            ViewHolder viewHolder = null;
            // load
            if (getItemViewType(position) == 0) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.chat_item_left, null);
                viewHolder = new ViewHolder();
                viewHolder.time = (TextView) convertView
                        .findViewById(R.id.left_time);
                viewHolder.message = (TextView) convertView
                        .findViewById(R.id.left_message);
            } else {
                convertView = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.chat_item_right, null);
                viewHolder = new ViewHolder();
                viewHolder.time = (TextView) convertView
                        .findViewById(R.id.right_time);
                viewHolder.message = (TextView) convertView
                        .findViewById(R.id.right_message);
            }
            convertView.setTag(viewHolder);
        }

        // set data
        ViewHolder vh = (ViewHolder) convertView.getTag();
        vh.time.setText(DateToString.dateToString(chatMessage.getDate(), 0));
        vh.message.setText(chatMessage.getMessage());
        return convertView;
    }

    private class ViewHolder {
        private TextView time, message;
    }
}
