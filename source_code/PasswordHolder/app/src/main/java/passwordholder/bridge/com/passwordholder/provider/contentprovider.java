package passwordholder.bridge.com.passwordholder.provider;



import java.util.HashMap;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;


import passwordholder.bridge.com.passwordholder.Utils.PLog;

public class contentprovider extends ContentProvider{
	private static final String TAG = "contentProvider";
	public static final int INCOMING_ACCOUNT_COLLECTION_URI_INDICATOR =1;
	public static final int INCOMING_ACCOUNT_SINGLE_URI_INDICATOR =2;
	private static HashMap<String, String> sAccountProjectionMap;

	static
	{
		sAccountProjectionMap = new HashMap<String, String>();
		sAccountProjectionMap.put(ProviderMetadata.accountTableMetaData._ID, ProviderMetadata.accountTableMetaData._ID);
		sAccountProjectionMap.put(ProviderMetadata.accountTableMetaData.accountId, ProviderMetadata.accountTableMetaData.accountId);
		sAccountProjectionMap.put(ProviderMetadata.accountTableMetaData.accountName, ProviderMetadata.accountTableMetaData.accountName);
		sAccountProjectionMap.put(ProviderMetadata.accountTableMetaData.accountUsername, ProviderMetadata.accountTableMetaData.accountUsername);
		sAccountProjectionMap.put(ProviderMetadata.accountTableMetaData.accountPassword, ProviderMetadata.accountTableMetaData.accountPassword);
		sAccountProjectionMap.put(ProviderMetadata.accountTableMetaData.accountNotes, ProviderMetadata.accountTableMetaData.accountNotes);
		sAccountProjectionMap.put(ProviderMetadata.accountTableMetaData.timeStamp, ProviderMetadata.accountTableMetaData.timeStamp);


	}

	private static final UriMatcher sUriMatcher;

	static {
		sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		sUriMatcher.addURI(ProviderMetadata.AUTHORITY, "account", INCOMING_ACCOUNT_COLLECTION_URI_INDICATOR);
		sUriMatcher.addURI(ProviderMetadata.AUTHORITY, "account/#",
				INCOMING_ACCOUNT_SINGLE_URI_INDICATOR);
	}

	private Dbhelper mOpenHelper;
	@Override
	public int delete(Uri uri, String where, String[] whereArgs) {
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		int count;

		switch (sUriMatcher.match(uri)) {
			case INCOMING_ACCOUNT_COLLECTION_URI_INDICATOR:
				count = db.delete(ProviderMetadata.accountTableMetaData.TABLE_NAME,
						where, whereArgs);
				break;
			case INCOMING_ACCOUNT_SINGLE_URI_INDICATOR:
				String rowId = uri.getPathSegments().get(1);
				count = db.delete(ProviderMetadata.accountTableMetaData.TABLE_NAME,
						ProviderMetadata.accountTableMetaData._ID + "=" + rowId
								+ (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""),
						whereArgs);
				break;


			default:
				throw new IllegalArgumentException("Unknown URI " + uri);
		}//switch
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}


	@Override
	public int bulkInsert(Uri uri, ContentValues[] values) {

		int numInserted = 0;

		String table="";
		int match=sUriMatcher.match(uri);

		if(match==INCOMING_ACCOUNT_COLLECTION_URI_INDICATOR)
		{
			table= ProviderMetadata.accountTableMetaData.TABLE_NAME;
		}
		else if(match==INCOMING_ACCOUNT_SINGLE_URI_INDICATOR)
		{
			table= ProviderMetadata.accountTableMetaData.TABLE_NAME;
		}
		else{

			throw new IllegalArgumentException("Unknown Uri"+uri)	;
		}
		SQLiteDatabase sqlDB = mOpenHelper.getWritableDatabase();
		sqlDB.beginTransaction();
		try {
			for (ContentValues cv : values) {
				long newID = sqlDB.insertOrThrow(table, null, cv);
				if (newID <= 0) {
					throw new SQLException("Failed to insert row into " + uri);
				}
			}
			sqlDB.setTransactionSuccessful();
			getContext().getContentResolver().notifyChange(uri, null);
			numInserted = values.length;
		} finally {
			sqlDB.endTransaction();
		}
		PLog.e("numinserted:"+ numInserted);
		return numInserted;

	}

	@Override
	public String getType(Uri uri) {
		switch (sUriMatcher.match(uri)) {
			case INCOMING_ACCOUNT_COLLECTION_URI_INDICATOR:
				return ProviderMetadata.accountTableMetaData.CONTENT_TYPE;
			case INCOMING_ACCOUNT_SINGLE_URI_INDICATOR:
				return ProviderMetadata.accountTableMetaData.CONTENT_ITEM_TYPE;

			default:
				throw new IllegalArgumentException("Unknown URI " + uri);
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues initialValues) {

		String table="";
		int match=sUriMatcher.match(uri);

		if(match==INCOMING_ACCOUNT_COLLECTION_URI_INDICATOR)
		{
			table= ProviderMetadata.accountTableMetaData.TABLE_NAME;
		}
		else if(match==INCOMING_ACCOUNT_SINGLE_URI_INDICATOR)
		{
			table= ProviderMetadata.accountTableMetaData.TABLE_NAME;
		}
		else{

			throw new IllegalArgumentException("Unknown Uri"+uri)	;
		}

		SQLiteDatabase db=mOpenHelper.getWritableDatabase();
		long rowId=db.replace(table, null, initialValues);
		if(rowId>0){
			Uri inserteduri=ContentUris.withAppendedId(uri, rowId)	;
			getContext().getContentResolver().notifyChange(uri, null);
			return inserteduri;
		}
		return null;
	}


	@Override
	public boolean onCreate() {
		PLog.d("main onCreate called");
		mOpenHelper = new Dbhelper(getContext());
		return true;
	}


	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
						String[] selectionArgs, String sortOrder) {

		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		switch (sUriMatcher.match(uri)) {
			case INCOMING_ACCOUNT_COLLECTION_URI_INDICATOR:
				qb.setTables(ProviderMetadata.accountTableMetaData.TABLE_NAME);
				qb.setProjectionMap(sAccountProjectionMap);
				break;
			case INCOMING_ACCOUNT_SINGLE_URI_INDICATOR:
				qb.setTables(ProviderMetadata.accountTableMetaData.TABLE_NAME);
				qb.setProjectionMap(sAccountProjectionMap);
				qb.appendWhere(ProviderMetadata.accountTableMetaData._ID + "="
						+ uri.getPathSegments().get(1));
				break;

			default:
				throw new IllegalArgumentException("Unknown URI " + uri);
		}
		// If no sort order is specified use the default
		String orderBy;
		if (TextUtils.isEmpty(sortOrder)) {
			orderBy = sortOrder;
		} else {
			orderBy = sortOrder;
		}
		// Get the database and run the query
		SQLiteDatabase db = mOpenHelper.getReadableDatabase();
		Cursor c = qb.query(db, projection, selection,
				selectionArgs, null, null, orderBy);
		int i = c.getCount();
		// Tell the cursor what uri to watch,
		// so it knows when its source data changes
		c.setNotificationUri(getContext().getContentResolver(), uri);
		return c;


	}


	@Override
	public int update(Uri uri, ContentValues values, String where,
					  String[] whereArgs) {
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		int count;
		switch (sUriMatcher.match(uri)) {
			case INCOMING_ACCOUNT_COLLECTION_URI_INDICATOR:
				count = db.update(ProviderMetadata.accountTableMetaData.TABLE_NAME,
						values, where, whereArgs);
				break;
			case INCOMING_ACCOUNT_SINGLE_URI_INDICATOR:
				String rowId = uri.getPathSegments().get(1);
				count = db.update(ProviderMetadata.accountTableMetaData.TABLE_NAME,
						values, ProviderMetadata.accountTableMetaData._ID + "=" + rowId
								+ (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""),
						whereArgs);
				break;

			default:
				throw new IllegalArgumentException("Unknown URI " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}


}
