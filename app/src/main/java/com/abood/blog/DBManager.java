package com.abood.blog;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBManager extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "blog.db";
    private static final int VERSION = 1;

    public DBManager(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table posts (p_id integer primary key, p_user varchar not null, " +
                "p_date varchar not null, p_type varchar not null,p_question varchar not null," +
                "p_action boolean not null,p_image varchar not null)");

        db.execSQL("create table answers (a_id integer primary key, a_user varchar not null, " +
                "a_date varchar not null, a_content varchar not null,p_id integer not null)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
