package com.uitl;

import java.util.ArrayList;

import android.content.ContentProviderOperation;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
import android.util.Log;

public class SystemDataContact {

	// 根据名字删除系统通讯录中的联系人数据
	public static void delContact(Context context, String name) {
		Cursor cursor = context.getContentResolver().query(Data.CONTENT_URI,
				new String[] { Data.RAW_CONTACT_ID },
				ContactsContract.Contacts.DISPLAY_NAME + "=?",
				new String[] { name }, null);

		ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

		if (cursor.moveToFirst()) {
			do {
				long Id = cursor.getLong(cursor
						.getColumnIndex(Data.RAW_CONTACT_ID));

				ops.add(ContentProviderOperation
						.newDelete(
								ContentUris.withAppendedId(
										RawContacts.CONTENT_URI, Id)).build());
				try {
					context.getContentResolver().applyBatch(
							ContactsContract.AUTHORITY, ops);
				} catch (Exception e) {
				}
			} while (cursor.moveToNext());
			cursor.close();
		}
	}

	// 根据oldname更新系统通讯录中联系人的信息
	public static void updateContact(Context context, String oldname,
			String name, String phone) {
		try {
			Cursor cursor = context.getContentResolver().query(
					Data.CONTENT_URI, new String[] { Data.RAW_CONTACT_ID },
					ContactsContract.Contacts.DISPLAY_NAME + "=?",
					new String[] { oldname }, null);
			cursor.moveToFirst();
			String _id = cursor.getString(cursor
					.getColumnIndex(Data.RAW_CONTACT_ID));
			cursor.close();
			String where = ContactsContract.Data.RAW_CONTACT_ID + " = ? AND "
					+ Phone.TYPE + " = ?";
			// ContactsContract.Data.RAW_CONTACT_ID + " = ? AND " + Phone.TYPE + " = ?";
			ContentValues values = new ContentValues();
			values.put(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE);
			values.put(Phone.NUMBER, phone);
			values.put(Phone.TYPE, Phone.TYPE_MOBILE);
			context.getContentResolver().update(Data.CONTENT_URI, values,
					where,
					new String[] { _id, Integer.toString(Phone.TYPE_MOBILE) });
		} catch (Exception e) {
			Log.i("miss", e.toString());
		}
	}

	// 添加联系人
	public static void addContact(Context context, String name, String phone) {

		// 首先插入空值，再得到rawContactsId ，用于下面插值
		ContentValues values = new ContentValues();
		// insert a null value
		Uri rawContactUri = context.getContentResolver().insert(
				RawContacts.CONTENT_URI, values);
		long rawContactsId = ContentUris.parseId(rawContactUri);

		// 往刚才的空记录中插入姓名
		values.clear();
		// A reference to the _ID that this data belongs to
		values.put(StructuredName.RAW_CONTACT_ID, rawContactsId);
		// "CONTENT_ITEM_TYPE" MIME type used when storing this in data table
		values.put(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE);
		// The name that should be used to display the contact.
		values.put(StructuredName.DISPLAY_NAME, name);
		// insert the real values
		context.getContentResolver().insert(Data.CONTENT_URI, values);
		// 插入电话
		values.clear();
		values.put(Phone.RAW_CONTACT_ID, rawContactsId);
		// String "Data.MIMETYPE":The MIME type of the item represented by this
		// row
		// String "CONTENT_ITEM_TYPE": MIME type used when storing this in data
		// table.
		values.put(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE);
		values.put(Phone.TYPE, Phone.TYPE_MOBILE);
		values.put(Phone.NUMBER, phone);
		context.getContentResolver().insert(Data.CONTENT_URI, values);
	}

	// 删除系统通讯录所有联系人
	public static void DelAllContact(Context context) {
		context.getContentResolver().delete(Data.CONTENT_URI, null, null);
	}
}
