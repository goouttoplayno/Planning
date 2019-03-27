package com.example.planning;

import java.util.ArrayList;
import java.util.HashMap;

import com.example.SQL.DataBaseHelper;
import com.example.nation.InfoHandler;
import com.example.socialplanning.R;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.app.PendingIntent;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;

public class DateList extends Activity {
	private ListView mylist = null;
	private Button addbtn = null;
	private Button wancheng;
	private Button weiwancheng;
	private ImageView xiala;
	private PopupWindow popupwindow;
	private Button backbtn;
	private Button deleteallbtn;
	private Button addclock;

	private ProgressDialog p_bar;
	private int i = 0;
	private int w;
	DataBaseHelper dbHelper;
	SQLiteDatabase db;
	MyApp myapp;

	Builder deleteall;

	private Handler searchhandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if (msg.what == InfoHandler.OVER) {
				// mylistview.setAdapter(simpleAdapter);
				if (w == 1) {
					getFinishNameList();
				} else if (w == 2) {
					getUnfinishNameList();
				}
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.headerbutton);
		myapp = (MyApp) getApplication();
		w = myapp.getNum();
		deleteall = new Builder(DateList.this, R.style.dialog);
		dbHelper = new DataBaseHelper(this);
		deleteallbtn = (Button) findViewById(R.id.deletelist);
		xiala = (ImageView) findViewById(R.id.xiala);
		mylist = (ListView) findViewById(R.id.myListView);
		addbtn = (Button) findViewById(R.id.add);
		backbtn = (Button) findViewById(R.id.datelistback);
		addclock = (Button) findViewById(R.id.addclock);
		addclock.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(DateList.this, SetCall.class);
				startActivity(intent);
			}
		});
		backbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		xiala.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				switch (v.getId()) {
				case R.id.xiala:
					if (popupwindow != null && popupwindow.isShowing()) {
						popupwindow.dismiss();
						return;
					} else {
						initmPopupWindowView();
						popupwindow.showAsDropDown(v, 0, 5);
					}
					break;
				default:
					break;
				}
			}
		});
		final String Routes[] = { "清除所有事项", "清除未规划完成事项", "清除已规划完成事项" };
		deleteallbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				new AlertDialog.Builder(DateList.this, R.style.dialog)
						.setItems(Routes,
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											final int id) {
										new AlertDialog.Builder(DateList.this,
												R.style.dialog)
												.setTitle("提示")
												.setMessage("确定删除？")
												.setPositiveButton(
														"确定",
														new DialogInterface.OnClickListener() {

															@Override
															public void onClick(
																	DialogInterface dialog,
																	int which) {
																// TODO
																// Auto-generated
																// method stub

																p_bar = ProgressDialog
																		.show(DateList.this,
																				null,
																				"正在删除...");
																new Thread(
																		new Runnable() {

																			@Override
																			public void run() {
																				// TODO
																				// Auto-generated
																				// method
																				// stub
																				deleteList(id);
																			}
																		})
																		.start();
																p_bar.dismiss();

															}

														})
												.setNegativeButton("取消", null)
												.show();
									}
								}).create().show();

			}
		});
		if (w == 1) {
			getFinishNameList();
		} else if (w == 2) {
			getUnfinishNameList();
		}

		mylist.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(final AdapterView<?> adapterview,
					View view, final int id, long where) {
				// TODO Auto-generated method stub
				String name2 = null;
				db = dbHelper.getReadableDatabase();
				SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
				qb.setTables("datanamelist");
				Cursor cursor;
				if (w == 1) {
					cursor = qb.query(db, null, "end=?",
							new String[] { "yes" }, null, null, null);
				} else {
					cursor = qb.query(db, null, "end=?", new String[] { "no" },
							null, null, null);
				}

				if (cursor.moveToPosition(id)) {
					name2 = cursor.getString(cursor.getColumnIndex("name"));
					Log.i("weiwancheng", "name" + name2);
					Log.i("weiwancheng", w + "");
				}
				Intent intent = new Intent();
				// Log.i("xiugailiucheng", name2);
				intent.putExtra("itemname", name2);
				intent.setClass(DateList.this, LookOverItem.class);
				DateList.this.startActivity(intent);
			}
		});

		mylist.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(final AdapterView<?> adatperview,
					final View view, final int id, long where) {
				// TODO Auto-generated method stub

				new AlertDialog.Builder(DateList.this, R.style.dialog)
						.setTitle("操作")
						.setPositiveButton("删除日程",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										deleteData(
												dbHelper.getWritableDatabase(),
												id);
										if (w == 1) {
											getFinishNameList();
										} else {
											getUnfinishNameList();
										}
									}
								})
						.setNegativeButton("修改名称",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										Intent intent = new Intent();
										intent.putExtra("caozuo", "xiugai");
										intent.putExtra(
												"name",
												getName(dbHelper
														.getReadableDatabase(),
														id));
										intent.putExtra(
												"didian",
												getDiDian(dbHelper
														.getReadableDatabase(),
														id));
										intent.putExtra(
												"id",
												""
														+ getId(dbHelper
																.getReadableDatabase(),
																id));
										Log.i("nameid", id + "");
										intent.setClass(DateList.this,
												SetNameAndDate.class);
										DateList.this.startActivity(intent);
									}
								}).show();

				return false;
			}
		});

		addbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra("editor", "add");
				intent.setClass(DateList.this, AddItem.class);
				DateList.this.startActivity(intent);
			}
		});

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub

		w = myapp.getNum();
		if (w == 1) {
			getFinishNameList();
		} else if (w == 2) {
			getUnfinishNameList();
		}

		super.onResume();

	}

	private void deleteData(SQLiteDatabase db, int id) {
		// TODO Auto-generated method stub
		String name2 = null;
		int clock = 0;
		db = dbHelper.getReadableDatabase();
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables("datanamelist");
		Cursor cursor = null;

		if (w == 1) {
			cursor = qb.query(db, null, "end=?", new String[] { "yes" }, null,
					null, null);
		} else if (w == 2) {
			cursor = qb.query(db, null, "end=?", new String[] { "no" }, null,
					null, null);
		}
		if (cursor.moveToPosition(id)) {
			name2 = cursor.getString(cursor.getColumnIndex("name"));
			clock = Integer.parseInt(cursor.getString(cursor
					.getColumnIndex("clock")));
		}
		if (clock == 1) {
			for (int a = 0; a <= 20; a++) {
				Intent intent = new Intent(DateList.this, CallAlarm.class);
				PendingIntent pt = PendingIntent.getBroadcast(DateList.this, a,
						intent, 0);

				// AlarmManager中删除闹钟
				AlarmManager am;
				am = (AlarmManager) getSystemService(ALARM_SERVICE);
				am.cancel(pt);

			}
		}
		db.delete("datanamelist", "name=?", new String[] { name2.toString() });
		db.execSQL("delete from liucheng where belongto='" + name2 + "'");
		i = 0;
		cursor.close();
		db.close();
	}

	private String getName(SQLiteDatabase db, int id) {
		// TODO Auto-generated method stub
		String name2 = null;
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables("datanamelist");
		Cursor cursor = null;
		if (w == 1) {
			cursor = qb.query(db, null, "end=?", new String[] { "yes" }, null,
					null, null);
		} else if (w == 2) {
			cursor = qb.query(db, null, "end=?", new String[] { "no" }, null,
					null, null);
		}
		if (cursor.moveToPosition(id)) {
			name2 = cursor.getString(cursor.getColumnIndex("name"));

		}
		cursor.close();
		db.close();
		return name2;
	}

	private String getId(SQLiteDatabase db, int id) {
		// TODO Auto-generated method stub
		String name2 = null;
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables("datanamelist");
		Cursor cursor = null;
		if (w == 1) {
			cursor = qb.query(db, null, "end=?", new String[] { "yes" }, null,
					null, null);
		} else if (w == 2) {
			cursor = qb.query(db, null, "end=?", new String[] { "no" }, null,
					null, null);
		}
		if (cursor.moveToPosition(id)) {
			name2 = cursor.getString(cursor.getColumnIndex("name"));

		}
		cursor = db.rawQuery("select * from datanamelist where name='" + name2
				+ "'", null);
		cursor.moveToNext();
		name2 = cursor.getInt(cursor.getColumnIndex("_id")) + "";
		cursor.close();
		db.close();
		return name2;
	}

	private String getDiDian(SQLiteDatabase db, int id) {
		String name = "";
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables("datanamelist");
		Cursor cursor = null;
		if (w == 1) {
			cursor = qb.query(db, null, "end=?", new String[] { "yes" }, null,
					null, null);
		} else if (w == 2) {
			cursor = qb.query(db, null, "end=?", new String[] { "no" }, null,
					null, null);
		}
		if (cursor.moveToPosition(id)) {
			if (cursor.isNull(cursor.getColumnIndex("didian"))) {
				cursor.close();
				db.close();
				return "";
			} else {
				name = cursor.getString(cursor.getColumnIndex("didian"));
			}

		}
		cursor.close();
		db.close();
		return name;
	}

	public void getFinishNameList() {

		ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		db = dbHelper.getReadableDatabase();
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables("datanamelist");
		Cursor cursor = qb.query(db, null, "end=?", new String[] { "yes" },
				null, null, null);

		if (cursor.moveToFirst()) {
			do {
				String date = cursor.getString(2);
				int first = date.indexOf("f");
				int second = date.indexOf("s");

				int year = Integer.parseInt(date.substring(0, first));
				Log.i("lookover", year + "");

				int month = Integer.parseInt(date.substring(first + 1, second));
				// Log.i("lookover", this.month+"");
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
				setIcon(i, map);
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
		mylist.setAdapter(adapter);

		// Log.i("firstitem", "ghjgjhgjh");
		cursor.close();
		db.close();
	}

	public void getUnfinishNameList() {

		ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		db = dbHelper.getReadableDatabase();
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables("datanamelist");
		Cursor cursor = qb.query(db, null, "end=?", new String[] { "no" },
				null, null, null);
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
				setIcon(i, map);
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
		mylist.setAdapter(adapter);

		cursor.close();
		db.close();
		i = 0;
	}

	public boolean haveOrNot(SQLiteDatabase db, String name) {

		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables("datanamelist");
		Cursor cursor = qb.query(db, null, null, null, null, null, null);
		if (cursor.moveToFirst()) {

			do {
				if (cursor.getString(cursor.getColumnIndex("name")).toString()
						.equals(name.toString())) {
					// Log.i("datename",
					// cursor.getString(cursor.getColumnIndex("name")));
					return true;
				}
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();
		return false;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (dbHelper != null) {
			dbHelper.close();
		}
	}

	private void deleteList(int id) {
		// TODO Auto-generated method stub

		String end = null;
		String name = "";

		db = dbHelper.getReadableDatabase();
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables("datanamelist");
		Cursor cursor = null;
		if (id == 0) {
			cursor = qb.query(db, null, " clock=?", new String[] { "1" }, null,
					null, null);
			end = "";
		} else if (id == 1) {
			cursor = qb.query(db, null, "end=?and clock=?", new String[] {
					"no", "1" }, null, null, null);
			end = "no";
		} else if (id == 2) {
			cursor = qb.query(db, null, "end=? and clock=?", new String[] {
					"yes", "1" }, null, null, null);
			end = "yes";
		}
		if (!cursor.moveToFirst()) {
			for (int a = 0; a <= 20; a++) {
				Intent intent = new Intent(DateList.this, CallAlarm.class);
				PendingIntent pt = PendingIntent.getBroadcast(DateList.this, a,
						intent, 0);

				// AlarmManager中删除闹钟
				AlarmManager am;
				am = (AlarmManager) getSystemService(ALARM_SERVICE);
				am.cancel(pt);

			}
		}
		cursor = null;
		cursor = db.rawQuery("select * from datanamelist where end='" + end
				+ "'", null);
		Log.i("cursorcount", cursor.getCount() + "");
		if (cursor.getCount() < 1) {
			while (cursor.moveToNext()) {
				name = cursor.getString(cursor.getColumnIndex("name"));
				db.execSQL("delete from liucheng where belongto = '" + name
						+ "'");
			}
		}
		if (end.equals("") || end == null) {
			db.execSQL("delete from datanamelist");
		} else {
			db.execSQL("delete from datanamelist where end='" + end + "'");
		}
		cursor.close();
		db.close();

		Message msg = new Message();
		msg.what = InfoHandler.OVER;

		searchhandler.sendMessage(msg);

	}

	private void setIcon(int i, HashMap<String, Object> map) {
		if (i == 1) {
			map.put("friendIcon", R.drawable.icon1);
		} else if (i == 2) {
			map.put("friendIcon", R.drawable.icon2);
		} else if (i == 3) {
			map.put("friendIcon", R.drawable.icon3);
		} else if (i == 4) {
			map.put("friendIcon", R.drawable.icon4);
		} else {
			map.put("friendIcon", R.drawable.icon5);
		}
	}

	public void initmPopupWindowView() {

		// // 获取自定义布局文件pop.xml的视图
		View customView = getLayoutInflater().inflate(R.layout.popview_item,
				null, false);
		// 创建PopupWindow实例,200,150分别是宽度和高度
		popupwindow = new PopupWindow(customView, 250, 280);
		// 设置动画效果 [R.style.AnimationFade 是自己事先定义好的]
		popupwindow.setAnimationStyle(R.style.AnimationFade);
		// 自定义view添加触摸事件
		customView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (popupwindow != null && popupwindow.isShowing()) {
					popupwindow.dismiss();
					popupwindow = null;
				}
				return false;
			}

		});

		/** 在这里可以实现自定义视图的功能 */

		wancheng = (Button) customView.findViewById(R.id.wancheng);
		weiwancheng = (Button) customView.findViewById(R.id.weiwancheng);
		wancheng.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// weiwancheng.setClickable(true);
				// wancheng.setClickable(false);
				getFinishNameList();
				// wancheng.setBackgroundResource(R.drawable.buttonon);
				// weiwancheng.setBackgroundResource(R.drawable.button_down);
				w = 1;
				myapp.setNum(w);
				popupwindow.dismiss();
			}
		});
		weiwancheng.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// wancheng.setClickable(true);
				// weiwancheng.setClickable(false);
				getUnfinishNameList();
				// weiwancheng.setBackgroundResource(R.drawable.buttonon);
				// wancheng.setBackgroundResource(R.drawable.button_down);
				w = 2;
				myapp.setNum(w);
				popupwindow.dismiss();
			}
		});
	}

}
