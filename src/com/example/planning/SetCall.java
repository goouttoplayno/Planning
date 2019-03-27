package com.example.planning;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import com.example.SQL.DataBaseHelper;
import com.example.socialplanning.R;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class SetCall extends Activity {
	private Button clockback;
	private ListView clocklist;

	private DataBaseHelper dbHelper;
	private SQLiteDatabase db;
	private Calendar calendar = Calendar.getInstance();

	private int i = 0;
	private boolean isnull = false;
	private String dataname;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.clockview);

		dbHelper = new DataBaseHelper(this);
		final Builder ad = new Builder(SetCall.this, R.style.dialog);
		final Builder ad2 = new Builder(SetCall.this, R.style.dialog);

		clockback = (Button) findViewById(R.id.clockcancel);
		clocklist = (ListView) findViewById(R.id.clocklist);
		clockback.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		getNameList();
		clocklist.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, final View arg1,
					final int arg2, long arg3) {
				// TODO Auto-generated method stub
				db = dbHelper.getReadableDatabase();
				Log.i("listwhere", arg2 + "   " + arg3);
				SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
				qb.setTables("datanamelist");
				Cursor cursor = qb
						.query(db, null, null, null, null, null, null);
				cursor.moveToPosition(arg2);
				String a = cursor.getString(cursor.getColumnIndex("clock"));
				if (a.equals("0")) {
					ad.setTitle("设置提醒")
							.setMessage(
									"设置提醒后将会按照您当前日程的每个流程的预计开始时间进行提醒，确定开启提醒(设置后将会覆盖原有提醒)？")
							.setPositiveButton("确定",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											// TODO Auto-generated method stub

											setClock(arg2);
										}
									}).setNegativeButton("取消", null).show();
				} else {
					ad2.setTitle("设置提醒")
							.setMessage("取消后将不再提醒，确定取消？")
							.setPositiveButton("确定",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface arg0, int arg1) {
											// TODO Auto-generated method stub
											cancelCall();
										}
									}).setNegativeButton("取消", null).show();
				}
				cursor.close();
				db.close();
			}

		});
	}

	public void getNameList() {

		ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		db = dbHelper.getReadableDatabase();
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables("datanamelist");
		Cursor cursor = qb.query(db, null, null, null, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				String date = cursor.getString(2);
				int first = date.indexOf("f");
				int second = date.indexOf("s");

				int year = Integer.parseInt(date.substring(0, first));
				int month = Integer.parseInt(date.substring(first + 1, second));
				int day = Integer.parseInt(date.substring(second + 1));

				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("name", cursor.getString(1));
				if (cursor.getString(cursor.getColumnIndex("didian"))
						.equals("")
						|| cursor.getString(cursor.getColumnIndex("didian")) == null) {
					map.put("where", "执行于：未指定地点");
				} else {
					map.put("where",
							"执行于："
									+ cursor.getString(cursor
											.getColumnIndex("didian")) + " ");
				}
				map.put("date", year + "年" + month + "月" + day + "日");
				setIcon(cursor.getString(cursor.getColumnIndex("clock")), map);
				i++;
				if (i >= 4)
					i = 0;
				list.add(map);

			} while (cursor.moveToNext());
		}
		SimpleAdapter adapter = new SimpleAdapter(this, list, R.layout.text,
				new String[] { "friendIcon", "name", "where", "date" },
				new int[] { R.id.headimage, R.id.nametext, R.id.wheretext,
						R.id.datetext });
		adapter.notifyDataSetChanged();
		clocklist.setAdapter(adapter);

		cursor.close();
		db.close();
		i = 0;
	}

	private void setIcon(String i, HashMap<String, Object> map) {
		if (i.equals("1")) {
			map.put("friendIcon", R.drawable.clock);
		} else {
			map.put("friendIcon", R.drawable.clockcancel);
		}
	}

	private void setClock(int id) {
		db = dbHelper.getWritableDatabase();
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables("datanamelist");
		Cursor cursor = null;
		cursor = qb.query(db, null, null, null, null, null, null);
		cursor.moveToPosition(id);
		dataname = cursor.getString(cursor.getColumnIndex("name"));
//		setCall();
		
		
		
		for (int a = 0; a <= 20; a++) {
			Intent intent = new Intent(SetCall.this, CallAlarm.class);
			PendingIntent pt = PendingIntent.getBroadcast(SetCall.this, a,
					intent, 0);

			// AlarmManager中删除闹钟
			AlarmManager am;
			am = (AlarmManager) getSystemService(ALARM_SERVICE);
			am.cancel(pt);
		}
		db = dbHelper.getReadableDatabase();
		cursor = db.rawQuery("select * from liucheng where belongto = '"
				+ dataname + "'", null);
		if (cursor.moveToFirst()) {
			int i = 0;
			do {

				String s = cursor.getString(cursor.getColumnIndex("start"));

				calendar.setTimeInMillis(System.currentTimeMillis());
				calendar.set(Calendar.HOUR_OF_DAY,
						Integer.parseInt(s.substring(0, s.indexOf("f"))));
				calendar.set(Calendar.MINUTE,
						Integer.parseInt(s.substring(s.indexOf("f") + 1)));

				calendar.set(Calendar.SECOND, 0);
				calendar.set(Calendar.MILLISECOND, 0);
				// 指定闹钟时间到了响起
				Intent intent = new Intent(SetCall.this, CallAlarm.class);
				PendingIntent pdIntent = PendingIntent.getBroadcast(
						SetCall.this, i, intent, 0);

				AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
				/**
				 * AlarmManager.RTC_WAKEUP 在系统休眠的时候同样运行
				 * 以set()设置的PendingIntent只会运行一次
				 */
				am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
						pdIntent);
				i++;
			} while (cursor.moveToNext());
		} else {
			Toast.makeText(SetCall.this, "您未对该日程设置任何流程", 3000).show();
			isnull = true;
		}
		
		
		if (!isnull) {
			db.execSQL("update datanamelist set clock='0' ");
			db.execSQL("update datanamelist set clock='1' where name='"
					+ dataname + "'");
		}
		cursor.close();
		db.close();
		getNameList();
	}

	private void setCall() {
		// TODO Auto-generated method stub
		SQLiteDatabase db;
		for (int a = 0; a <= 20; a++) {
			Intent intent = new Intent(SetCall.this, CallAlarm.class);
			PendingIntent pt = PendingIntent.getBroadcast(SetCall.this, a,
					intent, 0);

			// AlarmManager中删除闹钟
			AlarmManager am;
			am = (AlarmManager) getSystemService(ALARM_SERVICE);
			am.cancel(pt);
		}
		db = dbHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from liucheng where belongto = '"
				+ dataname + "'", null);
		if (cursor.moveToFirst()) {
			int i = 0;
			do {

				String s = cursor.getString(cursor.getColumnIndex("start"));

				calendar.setTimeInMillis(System.currentTimeMillis());
				calendar.set(Calendar.HOUR_OF_DAY,
						Integer.parseInt(s.substring(0, s.indexOf("f"))));
				calendar.set(Calendar.MINUTE,
						Integer.parseInt(s.substring(s.indexOf("f") + 1)));

				calendar.set(Calendar.SECOND, 0);
				calendar.set(Calendar.MILLISECOND, 0);
				// 指定闹钟时间到了响起
				Intent intent = new Intent(SetCall.this, CallAlarm.class);
				PendingIntent pdIntent = PendingIntent.getBroadcast(
						SetCall.this, i, intent, 0);

				AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
				/**
				 * AlarmManager.RTC_WAKEUP 在系统休眠的时候同样运行
				 * 以set()设置的PendingIntent只会运行一次
				 */
				am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
						pdIntent);
				i++;
			} while (cursor.moveToNext());
		} else {
			Toast.makeText(SetCall.this, "您未对该日程设置任何流程", 3000).show();
			isnull = true;
		}
		cursor.close();
		db.close();
	}

	private void cancelCall() {
		db = dbHelper.getWritableDatabase();
		db.execSQL("update datanamelist set clock='0' ");
		Cursor cursor = db.rawQuery("select * from liucheng where belongto = '"
				+ dataname + "'", null);
		cursor.moveToNext();
		int a = 0;
		do {
			Intent intent = new Intent(SetCall.this, CallAlarm.class);
			PendingIntent pt = PendingIntent.getBroadcast(SetCall.this, a,
					intent, 0);

			// AlarmManager中删除闹钟
			AlarmManager am;
			am = (AlarmManager) getSystemService(ALARM_SERVICE);
			am.cancel(pt);
			a++;
		} while (cursor.moveToNext());
		cursor.close();
		getNameList();
		db.close();
	}
}
