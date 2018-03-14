package com.bridge.passwordholder.provider;

import android.net.Uri;
import android.provider.BaseColumns;

public class ProviderMetadata {
	public static final String AUTHORITY = "com.bridge.passwordholder";
	public static final String DATABASE_NAME = "passwordholder.db";
	public static final int DATABASE_VERSION = 1;

	public ProviderMetadata() {}
	public static final class accountTableMetaData implements BaseColumns
	{
		private accountTableMetaData() {}
		public static final String TABLE_NAME = "account";
		//uri and MIME type definitions
		public static final Uri CONTENT_URI =Uri.parse("content://" + AUTHORITY + "/account");

		public static final String CONTENT_TYPE ="vnd.android.cursor.dir/com.bridge.offers";

		public static final String CONTENT_ITEM_TYPE ="vnd.android.cursor.item/com.bridge.offers";

		//Additional Columns start here.
		///string type
		public static final String accountId = "accountId";
		//string type
		public static final String accountName = "accountName";
		//string type
		public static final String accountUsername = "accountUsername";
		//string type
		public static final String accountPassword = "accountPassword";
		//string type
		public static final String accountNotes = "accountNotes";
		public static final String timeStamp = "timeStamp";


	}

}
