package com.panafold.main.datamodel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SqlLiteDbHelper extends SQLiteOpenHelper {

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;
	private static String DATABASE_PATH;
	// Database Name
	private static final String DATABASE_NAME = "spanishdb";
	// Contacts table name
	private static final String TABLE_WORDS = "spanishtable";
	private SQLiteDatabase db;

	// Contacts Table Columns names
//	private static final String KEY_ID = "id";
	private static final String KEY_ENGLISH = "english";
	private static final String KEY_SPANISH = "spanish";
	private static final String KEY_UNLOCK = "unlock";
	private static final String KEY_ENGPHRASE = "englishPhrase";
	private static final String KEY_SPANPHRASE = "spanishPhrase";
	private static final String KEY_IMAGENAME = "image";
	private static final String KEY_TIMESTAMP = "timestamp";
	private static final String KEY_ATTRIBUTION = "attribution";

	Context ctx;

	public SqlLiteDbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		ctx = context;
		//set database path name
		DATABASE_PATH=ctx.getDatabasePath(DATABASE_NAME).getPath();
		System.out.println(DATABASE_PATH);
	}

	// Getting single word
	public Word getWord(String eng) {
		db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_WORDS, new String[] { KEY_ENGLISH,KEY_SPANISH,
				KEY_ENGPHRASE, KEY_SPANPHRASE, KEY_IMAGENAME, KEY_ATTRIBUTION,
				}, KEY_ENGLISH + "=?",
				new String[] { String.valueOf(eng) }, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();
		Word curWord = new Word(cursor.getString(1), cursor.getString(0),
				cursor.getString(5), cursor.getString(3), cursor.getString(2),
				cursor.getString(4));
		// return contact
		return curWord;
	}

	public List<Word> getAllWords() {
		List<Word> wordList = new ArrayList<Word>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_WORDS;

		db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Word curWord = new Word(cursor.getString(1), cursor.getString(0),
						cursor.getString(5), cursor.getString(3), cursor.getString(2),
						cursor.getString(4));
				// Adding contact to list
				wordList.add(curWord);
			} while (cursor.moveToNext());
		}
			    cursor.close();
		// return contact list
		return wordList;
	}

	public void CopyDataBaseFromAsset() throws IOException {
		InputStream in = ctx.getAssets().open("databases/spanishdb.sqlite");
		Log.e("sample", "Starting copying");
		String outputFileName = DATABASE_PATH;
		File databaseFile = new File(DATABASE_PATH);
		// check if databases folder exists, if not create one and its
		// subfolders
		if (!databaseFile.exists()) {
			databaseFile.getParentFile().mkdir();
		}

		OutputStream out = new FileOutputStream(outputFileName);

		byte[] buffer = new byte[1024];
		int length;

		while ((length = in.read(buffer)) > 0) {
			out.write(buffer, 0, length);
		}
		Log.e("sample", "Completed");
		out.flush();
		out.close();
		in.close();

	}
	
	

	public void openDataBase() throws SQLException {
		String path = DATABASE_PATH;
		db = SQLiteDatabase.openDatabase(path, null,
				SQLiteDatabase.NO_LOCALIZED_COLLATORS
						| SQLiteDatabase.CREATE_IF_NECESSARY);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}
}