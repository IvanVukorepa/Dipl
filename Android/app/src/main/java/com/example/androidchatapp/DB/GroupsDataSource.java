package com.example.androidchatapp.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.androidchatapp.Models.Group;
import com.example.androidchatapp.Services.Message;

import java.util.ArrayList;

public class GroupsDataSource {
    private SQLiteDatabase database;
    private DBHelper dbHelper;

    public GroupsDataSource(Context context){
        dbHelper = new DBHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close(){
        database.close();
    }

    public void addGroupToDB(String groupName, String userName, String date){
        ContentValues values = new ContentValues();

        values.put("groupName", groupName);
        values.put("userName", userName);
        values.put("datetime", date);

        database.insert("Groups", null, values);
    }

    public ArrayList<Group> getAllGroupsForUser(String userName){
        ArrayList<Group> groups = new ArrayList<>();

        Cursor cursor = database.rawQuery("select * from Groups where userName=" + "\"" + userName + "\"", null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()){
            Group group = new Group();

            group.setId(cursor.getInt(0));
            group.setUserName(cursor.getString(1));
            group.setGroupName(cursor.getString(2));
            group.setDate(cursor.getString(3));
            groups.add(group);
            cursor.moveToNext();
        }

        cursor.close();
        return groups;
    }

    public void updateGroupData(String username, String group, String time){
        ContentValues values = new ContentValues();

        values.put("userName", username);
        values.put("groupName", group);
        values.put("datetime", time);
        String[] columns = new String[]{"groupName"};
        String[] whereArgs = new String[]{group, username};
        Cursor cursor = database.query("Groups", columns, "groupName = ? and userName = ?", whereArgs, null, null, "groupName");
        if (cursor.getCount() > 0){
            database.update("Groups", values, "groupName = ? and " + "userName = ?", whereArgs);
        }else{
            database.insert("Groups", null, values);
        }
        cursor.close();
    }
}
