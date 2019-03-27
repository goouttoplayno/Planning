package com.example.nationstyle;

import java.util.ArrayList;
import java.util.List;

import com.example.SQL.DataBaseHelper;
import com.example.planning.MyApp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Travels {
	// private static MyApp myapp = new MyApp();
	// static public Context context = myapp.getMyContext();
	public static final List<Data> IMG_DESCRIPTIONS = new ArrayList<Data>();
	// private static DataBaseHelper dbhelper = new DataBaseHelper(context);
	// private static SQLiteDatabase db = dbhelper.getReadableDatabase();
	// private static Cursor cursor = db.rawQuery("select * from nation ",
	// null);

	static {

		for (int i = 0; i <= 55; i++) {

			// cursor.moveToNext();
			// String s = cursor.getString(cursor.getColumnIndex("jianjie"));
			Travels.IMG_DESCRIPTIONS.add(new Travels.Data(i));
		}
		// cursor.close();
		// db.close();
	}

	public static final class Data {

		// public final String minzujianjie;
		public final int id;

		private Data(
		// String minzujianjie,
				int id) {
			// this.minzujianjie = minzujianjie;
			this.id = id;
		}
	}
}
