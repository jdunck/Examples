package com.examples.CPExamples;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

public class PhrasesProvider extends ContentProvider {

    public static final String PROVIDER_NAME = 
        "com.examples.provider.Phrases";
     
     public static final Uri CONTENT_URI = 
        Uri.parse("content://"+ PROVIDER_NAME + "/phrases");
     
     public static final String _ID = "_id";
     public static final String TITLE = "title";
     
     private static final int PHRASES = 1;
     private static final int PHRASE_ID = 2;   
         
     private static final UriMatcher uriMatcher;
     static{
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "phrases", PHRASES);
        uriMatcher.addURI(PROVIDER_NAME, "phrases/#", PHRASE_ID);      
     }
	
     //---for database use---
     private SQLiteDatabase phrasesDB;
     private static final String DATABASE_NAME = "Phrases";
     private static final String DATABASE_TABLE = "shortpharse";
     private static final int DATABASE_VERSION = 1;
     private static final String DATABASE_CREATE =
           "create table " + DATABASE_TABLE + 
           " (_id integer primary key autoincrement, "
           + "title text not null);";
     
     private static class DatabaseHelper extends SQLiteOpenHelper 
     {
        DatabaseHelper(Context context) {
           super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) 
        {
           db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, 
        int newVersion) {
           Log.w("Content provider database", 
                "Upgrading database from version " + 
                oldVersion + " to " + newVersion + 
                ", which will destroy all old data");
           db.execSQL("DROP TABLE IF EXISTS titles");
           onCreate(db);
        }
     }      
     
     @Override
     public String getType(Uri uri) {
        switch (uriMatcher.match(uri)){
           //---get all books---
           case PHRASES:
              return "vnd.android.cursor.dir/vnd.examples.phrases ";
           //---get a particular book---
           case PHRASE_ID:                
              return "vnd.android.cursor.item/vnd.examples.phrases ";
           default:
              throw new IllegalArgumentException("Unsupported URI: " + uri);        
        }   
     }
     
	@Override
	public int delete(Uri arg0, String arg1, String[] arg2) {
	      // arg0 = uri 
	      // arg1 = selection
	      // arg2 = selectionArgs
	      int count=0;
	      switch (uriMatcher.match(arg0)){
	         case PHRASES:
	            count = phrasesDB.delete(
	               DATABASE_TABLE,
	               arg1, 
	               arg2);
	            break;
	         case PHRASE_ID:
	            String id = arg0.getPathSegments().get(1);
	            count = phrasesDB.delete(
	               DATABASE_TABLE,                        
	               _ID + " = " + id + 
	               (!TextUtils.isEmpty(arg1) ? " AND (" + 
	               arg1 + ')' : ""), 
	               arg2);
	            break;
	         default: throw new IllegalArgumentException(
	            "Unknown URI " + arg0);    
	      }       
	      getContext().getContentResolver().notifyChange(arg0, null);
	      return count; 
	      
	      
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
	      long rowID = phrasesDB.insert(
	    	         DATABASE_TABLE, "", values);
	    	           
	    	      //---if added successfully---
	    	      if (rowID>0)
	    	      {
	    	         Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
	    	         getContext().getContentResolver().notifyChange(_uri, null);    
	    	         return _uri;                
	    	      }        
	    	      throw new SQLException("Failed to insert row into " + uri);
	}

	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
	      Context context = getContext();
	      DatabaseHelper dbHelper = new DatabaseHelper(context);
	      phrasesDB = dbHelper.getWritableDatabase();
	      return (phrasesDB == null)? false:true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		// TODO Auto-generated method stub
	      SQLiteQueryBuilder sqlBuilder = new SQLiteQueryBuilder();
	      sqlBuilder.setTables(DATABASE_TABLE);
	       
	      if (uriMatcher.match(uri) == PHRASE_ID)
	         //---if getting a particular book---
	         sqlBuilder.appendWhere(
	            _ID + " = " + uri.getPathSegments().get(1));                
	       
	      if (sortOrder==null || sortOrder=="")
	         sortOrder = TITLE;
	   
	      Cursor c = sqlBuilder.query(
	         phrasesDB, 
	         projection, 
	         selection, 
	         selectionArgs, 
	         null, 
	         null, 
	         sortOrder);
	   
	      //---register to watch a content URI for changes---
	      c.setNotificationUri(getContext().getContentResolver(), uri);
	      return c;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		int count = 0;
	      switch (uriMatcher.match(uri)){
	         case PHRASES:
	            count = phrasesDB.update(
	               DATABASE_TABLE, 
	               values,
	               selection, 
	               selectionArgs);
	            break;
	         case PHRASE_ID:                
	            count = phrasesDB.update(
	               DATABASE_TABLE, 
	               values,
	               _ID + " = " + uri.getPathSegments().get(1) + 
	               (!TextUtils.isEmpty(selection) ? " AND (" + 
	                  selection + ')' : ""), 
	                selectionArgs);
	            break;
	         default: throw new IllegalArgumentException(
	            "Unknown URI " + uri);    
	      }       
	      getContext().getContentResolver().notifyChange(uri, null);
	      return count;
	}

}
