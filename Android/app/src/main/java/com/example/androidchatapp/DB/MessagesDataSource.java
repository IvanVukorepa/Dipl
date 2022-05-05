package com.example.androidchatapp.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.androidchatapp.Services.Message;

import java.util.ArrayList;
import java.util.List;

public class MessagesDataSource {

    private SQLiteDatabase database;
    private DBHelper dbHelper;

    public MessagesDataSource(Context context){
        dbHelper = new DBHelper(context);
    }

    public void open() throws SQLException{
        database = dbHelper.getWritableDatabase();
    }

    public void close(){
        database.close();
    }

    public void addMessageToDB(String user, String group, String content){
        ContentValues values = new ContentValues();

        values.put("chatName", group);
        values.put("username", user);
        values.put("messageContent", content);

        database.insert("Messages", null, values);
    }

    public ArrayList<MessageDatabse> getAllMessagesForChat(String chatName){
        ArrayList<MessageDatabse> messages = new ArrayList<>();

        Cursor cursor = database.rawQuery("select * from messages where chatName=" + "\"" + chatName + "\"", null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()){
            MessageDatabse message = new MessageDatabse();

            message.setId(cursor.getInt(0));
            message.setChatName(cursor.getString(1));
            message.setUsername(cursor.getString(2));
            message.setMessageContent(cursor.getString(3));
            messages.add(message);
            cursor.moveToNext();
        }

        cursor.close();
        return messages;
    }
}
