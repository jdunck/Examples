package com.example;

import java.util.HashMap;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.LiveFolders;
import android.text.TextUtils;

public class BlurtProvider extends ContentProvider 
{
	//private static final String TAG="<ApplicationProvider>";
	//content uri parts
	private static final String SCHEME="content://";
	private static final String AUTHORITY="com.gtug.example.blurts";
	public static final Uri CONTENT_URI = Uri.parse(String.format("%s%s/buzz",SCHEME,AUTHORITY));
	public static final Uri LIVE_URI = Uri.parse(String.format("%s%s/live",SCHEME,AUTHORITY));
	//db stuff
	private static final String DB_NAME = "blurts.db";
	private static final int DB_VERSION=1;
	
	private SQLiteDatabase data;
	
	//set up matcher
	private static final int SINGLE_MATCH=1;
	private static final int MULTIPLE_MATCH=2;
	private static final int LIVE_MATCH=3;
	
	private static final HashMap<String, String> LiveProjection;
	private static final UriMatcher matcher;
	
	static
	{
		LiveProjection = new HashMap<String,String>();
		LiveProjection.put(LiveFolders._ID, BlurtColumns._ID + " AS " + LiveFolders._ID);
		LiveProjection.put(LiveFolders.NAME, BlurtColumns.TITLE + " AS " + LiveFolders.NAME);
		LiveProjection.put(LiveFolders.DESCRIPTION, BlurtColumns.DETAIL + " AS " + LiveFolders.DESCRIPTION);
		
		matcher = new UriMatcher(UriMatcher.NO_MATCH);
		matcher.addURI(AUTHORITY, "buzz/#", SINGLE_MATCH);
		matcher.addURI(AUTHORITY, "buzz", MULTIPLE_MATCH);
		matcher.addURI(AUTHORITY,"live",LIVE_MATCH);
	}
	
	
	
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) 
	{
		int which = matcher.match(uri);
		int count=0;
		switch(which)
		{
		case SINGLE_MATCH:
			long id=ContentUris.parseId(uri);
			if(!TextUtils.isEmpty(selection))
			{
				count = data.delete(BlurtColumns.TABLE_NAME, String.format("%s=%s AND (%s)",BlurtColumns._ID,id,selection), selectionArgs);
			}
			else
			{
				count = data.delete(BlurtColumns.TABLE_NAME, String.format("%s=%s",BlurtColumns._ID,id), selectionArgs);
			}
			break;
		case MULTIPLE_MATCH:
			count = data.delete(BlurtColumns.TABLE_NAME, selection, selectionArgs);
			break;
		default: throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
		if(count > 0)
		{
			getContext().getContentResolver().notifyChange(CONTENT_URI, null);
		}
		return count;
	}

	@Override
	public String getType(Uri uri) 
	{
		int which = matcher.match(uri);
		String val = "";
		switch(which)
		{
		case SINGLE_MATCH:
			val = "vnd.android.cursor.item/vnd.gtug.blurts";
			break;
		case MULTIPLE_MATCH:
			val = "vnd.android.cursor.dir/vnd.gtug.blurts";
			break;
		default: throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
		return val;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) 
	{
		Uri retval = null;
		int which  = matcher.match(uri);

		switch(which)
		{
		case MULTIPLE_MATCH:
			long id = data.insert(BlurtColumns.TABLE_NAME, "", values);
			if(id > -1)
			{
				retval = ContentUris.withAppendedId(CONTENT_URI, id);
				getContext().getContentResolver().notifyChange(CONTENT_URI, null);
			}
			break;
		default: throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
		return retval;
	}

	@Override
	public boolean onCreate() 
	{
		DbHelper helper = new DbHelper(getContext(),DB_NAME,null,DB_VERSION);
		this.data = helper.getWritableDatabase();
		return (this.data == null)?false:true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) 
	{
		int which = matcher.match(uri);
		SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
		Cursor cursor = null;
		switch(which)
		{
		case SINGLE_MATCH:
			long id = ContentUris.parseId(uri);
			builder.setTables(BlurtColumns.TABLE_NAME);
			builder.appendWhere(String.format("%s=%s",BlurtColumns._ID,id));
			cursor = builder.query(data, projection, selection, selectionArgs, null, null, sortOrder);
			break;
		case MULTIPLE_MATCH:
			builder.setTables(BlurtColumns.TABLE_NAME);
			cursor = builder.query(data, projection, selection, selectionArgs, null, null, sortOrder);
			break;
		case LIVE_MATCH:
			builder.setTables(BlurtColumns.TABLE_NAME);
			builder.setProjectionMap(LiveProjection);
			cursor = builder.query(data, projection, selection, selectionArgs, null, null, sortOrder);
			break;
		default: throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
		return cursor;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) 
	{
		int which=matcher.match(uri);
		int count=0;
		switch(which)
		{
		case SINGLE_MATCH:
			long id = ContentUris.parseId(uri);
			String where = null;
			if(id > -1)
			{
				if(TextUtils.isEmpty(selection))
				{
					where = String.format("%s=%s",BlurtColumns._ID,id);
				}
				else
				{
					where = String.format("%s=%s AND (%s)",BlurtColumns._ID,id,selection);
				}
				count = data.update(BlurtColumns.TABLE_NAME, values, where, selectionArgs);
				if(count > 0)
				{
					getContext().getContentResolver().notifyChange(CONTENT_URI, null);
					getContext().getContentResolver().notifyChange(uri, null);
				}
			}
			break;
		case MULTIPLE_MATCH:
			count = data.update(BlurtColumns.TABLE_NAME, values, selection, selectionArgs);
			if(count > 0)
			{
				getContext().getContentResolver().notifyChange(CONTENT_URI, null);
			}
			break;
		}
		return count;
	}
}