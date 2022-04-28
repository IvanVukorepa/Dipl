package com.example.androidchatapp.main_screen;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.androidchatapp.R;
import com.squareup.picasso.Picasso;

public class ChatsListAdapter extends BaseAdapter {
    @Override
    public int getCount() {
        return ChatListDataStorage.chats.size();
    }

    @Override
    public Object getItem(int position) {
        return ChatListDataStorage.chats.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private Context myContext;
    private LayoutInflater mInflater;

    public ChatsListAdapter(Context context) {
        myContext = context;
        mInflater = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Log.e("afaf", "asefgwagweagweargrsedagrsedgrthse");
        if (view == null) {
            view = mInflater.inflate(R.layout.chat_list_iten, viewGroup, false);
        }

        final TextView ChatName = (TextView) view.findViewById(R.id.ChatName);
        ImageView imageTmb = (ImageView) view.findViewById(R.id.image_tmb);
        final String chatInstance = ChatListDataStorage.chats.get(i);

        ChatName.setText(chatInstance);
        //Picasso.get().load(myContext.getString(R.string.baseURL) + sport.getImageUrl()).into(imageTmb);

        return view;
    }
}
