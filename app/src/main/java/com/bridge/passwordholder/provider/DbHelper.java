package com.bridge.passwordholder.provider;
/**
 *
 */


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.bridge.passwordholder.Utils.PLog;


class DbHelper extends SQLiteOpenHelper{
	public DbHelper(Context context) {
		super(context, ProviderMetadata.DATABASE_NAME, null, ProviderMetadata.DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " + ProviderMetadata.accountTableMetaData.TABLE_NAME+ " ("
		+ ProviderMetadata.accountTableMetaData._ID + " INTEGER PRIMARY KEY,"
		+ ProviderMetadata.accountTableMetaData.accountId + " TEXT, "
		+ ProviderMetadata.accountTableMetaData.accountName + " TEXT, "
		+ ProviderMetadata.accountTableMetaData.accountUsername + " TEXT, "
		+ ProviderMetadata.accountTableMetaData.accountPassword + " TEXT, "
		+ ProviderMetadata.accountTableMetaData.accountNotes + " TEXT ,"
		+ ProviderMetadata.accountTableMetaData.timeStamp + " TEXT"

	    + ");");

	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		PLog.w( "Upgrading database from version "
				+ oldVersion + " to "
				+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + ProviderMetadata.accountTableMetaData.TABLE_NAME);
		onCreate(db);
	}
}
