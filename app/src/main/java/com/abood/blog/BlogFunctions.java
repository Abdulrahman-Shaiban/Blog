package com.abood.blog;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;


public class BlogFunctions extends AppCompatActivity {

    private static BlogFunctions blogFunctions;
    private Context bContext;
    private SQLiteDatabase bDatabase;
    ArrayList<Posts> mPosts;

    private BlogFunctions(Context context) {

        bContext = context.getApplicationContext();
        bDatabase = new DBManager(bContext).getWritableDatabase();

    }

    public static BlogFunctions getInstance(Context context){

        if(blogFunctions == null){
            blogFunctions = new BlogFunctions(context);
        }
        return blogFunctions;
    }


    public void addPost(Posts post) {

        ContentValues values = getContentValues(post);
        bDatabase.insert("posts", null, values);

    }

    public void addAnswer(Answers answer) {

        ContentValues values = getContentValues(answer);
        bDatabase.insert("answers", null, values);

    }

    public void deletePost(Posts post) {
        String uuidString = post.getpId().toString();
        bDatabase.delete("posts","pid" + " = ?",
                new String[] { uuidString });
    }

    public void deletePoxst(Posts post) {
        String uuidString = post.getpId().toString();
        bDatabase.delete("posts","pid = " + uuidString, null);
    }

//    public Posts getPost() {
//
//        String pId = getString(getColumnIndex("pid"));
//        String title = getString(getColumnIndex(CrimeDbSchema.CrimeTable.Cols.TITLE));
//        long date = getLong(getColumnIndex(CrimeDbSchema.CrimeTable.Cols.DATE));
//        int isSolved = getInt(getColumnIndex(CrimeDbSchema.CrimeTable.Cols.SOLVED));
//        String suspect = getString(getColumnIndex(CrimeDbSchema.CrimeTable.Cols.SUSPECT));
//
//        Posts post = new Posts();
//        post.setcTitle(title);
//        post.setcDate(new Date(date));
//        post.setcSolved(isSolved != 0);
//        post.setcSuspect(suspect);
//
//        return post;
//    }
//
//    public ArrayList<Posts> getCrimes() {
//
//        ArrayList<Posts> crimes = new ArrayList<>();
//        CrimeCursorWrapper cursor = queryCrimes(null, null);
//        try {
//            cursor.moveToFirst();
//            while (!cursor.isAfterLast()) {
//                crimes.add(cursor.getCrime());
//                cursor.moveToNext();
//            }
//        } finally {
//            cursor.close();
//        }
//        return crimes;
//
//
//    }


    private static ContentValues getContentValues(Posts post) {
        ContentValues values = new ContentValues();

        values.put("id", post.getpId());
        values.put("user", post.getpUser());
        values.put("date", post.getpDate());
        values.put("type", post.getpType());
        values.put("question", post.getpQuestion());
        values.put("action", post.getpAction());
        values.put("image", post.getpImage());

        return values;
    }


    private static ContentValues getContentValues(Answers answer) {
        ContentValues values = new ContentValues();

        values.put("aid", answer.getaId());
        values.put("user", answer.getaUser());
        values.put("date", answer.getaDate());
        values.put("content", answer.getaContent());
        values.put("pid", answer.getpId());

        return values;
    }

}
