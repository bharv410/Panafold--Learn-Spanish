package com.panafold.main.datamodel;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class LocalDBHelper extends SQLiteOpenHelper {
	 
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "wordsManager";
 
    private static final String TABLE_WORDS = "reviewwords";
 
    private static final String KEY_ENGLISH = "english";
    private static final String KEY_TIMESTAMP = "timestamp";
    private static final String KEY_REVIEW = "review";
    
    
 
    public LocalDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_WORDS_TABLE = "CREATE TABLE " + TABLE_WORDS + "("
                + KEY_ENGLISH + " TEXT PRIMARY KEY," 
                + KEY_REVIEW + " INTEGER," 
                + KEY_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP"+ ")";
        db.execSQL(CREATE_WORDS_TABLE);
    }
 
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WORDS);
 
        // Create tables again
        onCreate(db);
    }
 
    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */
 
    // Adding new contact
    public void addWord(Word curWord) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(KEY_ENGLISH, curWord.getEnglish()); 
        values.put(KEY_REVIEW, 0);
        
        // Inserting Row
        db.insert(TABLE_WORDS, null, values);
        //db.close(); // Closing database connection
    }
 
    // Getting single word
    ReviewWord getWord(String eng) {
        SQLiteDatabase db = this.getReadableDatabase();
 
        Cursor cursor = db.query(TABLE_WORDS, new String[] { KEY_ENGLISH,
        		KEY_REVIEW, KEY_TIMESTAMP}, KEY_ENGLISH + "=?",
                new String[] { String.valueOf(eng) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        ReviewWord curWord = new ReviewWord(cursor.getString(0), cursor.getInt(1), cursor.getString(2));
        // return contact
        return curWord;
    }
     
    public void setAsSeen(String sameEnglish) {
        SQLiteDatabase db = this.getWritableDatabase();
        
        
    ContentValues args = new ContentValues();
    args.put(KEY_REVIEW, 1);

    db.update(TABLE_WORDS, args, KEY_ENGLISH+"="+sameEnglish, null);
}
    public boolean deleteTitle(String name) 
	{SQLiteDatabase db = this.getWritableDatabase();
	    return db.delete(TABLE_WORDS, KEY_ENGLISH + "='" + name+"'", null) > 0;
	}
    // Getting All Contacts
    public List<ReviewWord> getReviewWords() {
        List<ReviewWord> wordList = new ArrayList<ReviewWord>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_WORDS;
 
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
 
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
            	ReviewWord curWord = new ReviewWord(cursor.getString(0), cursor.getInt(1), cursor.getString(2));
            	System.out.println("Grabbing "+curWord.getEnglish() +" from db. timestamp is "+curWord.getTimeStamp());
                		wordList.add(curWord);
                
            	} while (cursor.moveToNext());
        }
        // return contact list
        return wordList;
    }

 
    // Getting contacts Count
    public int getWordCount() {
        String countQuery = "SELECT  * FROM " + TABLE_WORDS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
 
        // return count
        return cursor.getCount();
    }
 
}