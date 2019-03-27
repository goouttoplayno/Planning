package com.example.SQL;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class GetJianJie {
	private DataBaseHelper dbhelper;
	private SQLiteDatabase db;
	private Context context;

	public GetJianJie(Context context) {
		this.context = context;
	}

	private void dbOpen() {
		dbhelper = new DataBaseHelper(context);
		db = dbhelper.getReadableDatabase();
	}

	private void dbClose() {
		db.close();
	}

	public String getJianJie(int id) {

		String jianjie = "";
		dbOpen();
		Cursor cursor = db.rawQuery("select * from nation where _id=" + id,
				null);
		cursor.moveToNext();
		jianjie = cursor.getString(cursor.getColumnIndex("jianjie"));
		cursor.close();
		dbClose();
		return jianjie;
	}

	public void insertFromMoban(int id) {
		String belongto = "";
		switch (id) {
		case 0:
			belongto = "yuehui";
			break;
		case 1:
			belongto = "jubanzangli";
			break;
		case 2:
			belongto = "canjiamianshi";
			break;
		default:
			if (id == 100)
				belongto = "canjiahunli";
			else if (id == 101)
				belongto = "jubanhunli";
			break;
		}
		Log.i("mobanbtn", belongto + "");
		dbOpen();
		db = dbhelper.getWritableDatabase();
		db.execSQL("insert into linshi('liuchengname','leixing','itemcontent','belongto','start') select liuchengname,leixing,itemcontent,belongto,start from moban where belongto ='"
				+ belongto + "'");

		dbClose();
	}
}
