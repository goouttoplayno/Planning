package com.example.planning;

import java.util.Calendar;

import com.example.SQL.DataBaseHelper;
import com.example.socialplanning.R;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;
import android.widget.Toast;

public class SetNameAndDate extends Activity {

	private DatePicker datePicker;
	private EditText nameet;
	private EditText whereet;
	private Button savebtn;
	private Button cancelbtn;
	// private TextView tv;

	private DataBaseHelper dbhelper;
	SQLiteDatabase db;

	int nyear;
	int month;
	int day;
	String id;
	boolean isEnd;
	String caozuo;
	String name;
	String didian = "";
	Calendar calendar = Calendar.getInstance();
	Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setnameanddate);

		intent = getIntent();
		caozuo = intent.getStringExtra("caozuo");
		isEnd = intent.getBooleanExtra("isend", true);// true未结束 false 结束

		datePicker = (DatePicker) findViewById(R.id.datePicker1);
		nameet = (EditText) findViewById(R.id.editText1);
		whereet = (EditText) findViewById(R.id.sndwhere);
		savebtn = (Button) findViewById(R.id.sndsave);
		cancelbtn = (Button) findViewById(R.id.sndcancel);
		final int mHours = calendar.get(Calendar.HOUR_OF_DAY);
		final int mMinute = calendar.get(Calendar.MINUTE);
		dbhelper = new DataBaseHelper(this);

		if (!caozuo.equals("xiugai")) {
			nyear = calendar.get(Calendar.YEAR);
			month = calendar.get(Calendar.MONTH);
			day = calendar.get(Calendar.DAY_OF_MONTH);

			Log.i("lookover", nyear + month + day + "')");
		} else {
			name = intent.getStringExtra("name");
			nameet.setText(name);
			id = intent.getStringExtra("id").toString();
			if (!(intent.getStringExtra("didian").equals("") || intent
					.getStringExtra("didian") == null)) {
				didian = intent.getStringExtra("didian");
				whereet.setText(didian);
			}
			setDate();
		}

		datePicker.init(nyear, month, day, new OnDateChangedListener() {

			@Override
			public void onDateChanged(DatePicker view, int year,
					int monthOfYear, int dayOfMonth) {
				// TODO Auto-generated method stub
				nyear = year;
				month = monthOfYear;
				day = dayOfMonth;

			}
		});
		savebtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				if (!caozuo.equals("xiugai")) {
					String s = "";
					if (nameet.getText().toString().equals("")) {
						Toast.makeText(SetNameAndDate.this, "请设置名称",
								Toast.LENGTH_LONG).show();
					} else {
						s = nameet.getText().toString();
						if (s.contains("'") || getDiDian().contains("'")) {
							Toast.makeText(SetNameAndDate.this,
									"请勿在编辑框中包含 ' 字符", Toast.LENGTH_SHORT)
									.show();
						} else {
							if (haveOrNot(dbhelper.getReadableDatabase(), s)) {
								Toast.makeText(SetNameAndDate.this,
										"该日程已存在，请更换名称", Toast.LENGTH_LONG)
										.show();
							} else {
								db = dbhelper.getWritableDatabase();
								Cursor cursor = db.rawQuery(
										"select * from linshi", null);
								if (cursor.moveToFirst()) {
									do {
										if (cursor.isNull(cursor
												.getColumnIndex("start")))
											db.execSQL("update linshi set start = '"
													+ mHours
													+ "f"
													+ mMinute
													+ "'");
										if (cursor.isNull(cursor
												.getColumnIndex("end")))
											db.execSQL("update linshi set end = '"
													+ mHours
													+ "f"
													+ mMinute
													+ "'");

									} while (cursor.moveToNext());
								}
								db.execSQL("insert into datanamelist(name,date,didian) values('"
										+ s
										+ "','"
										+ nyear
										+ "f"
										+ (month + 1)
										+ "s"
										+ day
										+ "','"
										+ getDiDian()
										+ "')");
								db.execSQL("update linshi set belongto='" + s
										+ "';");
								db.execSQL("insert into liucheng ('liuchengname','leixing','itemcontent','waring','clock','start','end','belongto') select liuchengname,leixing,itemcontent,waring,clock,start,end,belongto from linshi");

								db.execSQL("delete from linshi");
								if (isEnd)// true未结束 false 结束
								{
									db.execSQL("update datanamelist set end='no' where name='"
											+ s + "' ");
								} else {
									db.execSQL("update datanamelist set end='yes' where name='"
											+ s + "' ");
								}
								db.close();

								Toast.makeText(SetNameAndDate.this, "已保存",
										Toast.LENGTH_LONG).show();
								AddItem.itemid = 1;
								finish();

							}
						}
					}
				} else {
					String s = "";
					if (nameet.getText().toString().equals("")) {
						Toast.makeText(SetNameAndDate.this, "请设置名称",
								Toast.LENGTH_LONG).show();
					} else {
						s = nameet.getText().toString();
						if (s.contains("'") || getDiDian().contains("'")) {
							Toast.makeText(SetNameAndDate.this,
									"请勿在编辑框中包含 ' 字符", Toast.LENGTH_SHORT)
									.show();
						} else {
							if (haveOrNot(dbhelper.getReadableDatabase(), s)) {
								Toast.makeText(SetNameAndDate.this,
										"该日程已存在，请更换名称", Toast.LENGTH_LONG)
										.show();
							} else {
								db = dbhelper.getWritableDatabase();
								db.execSQL("update datanamelist set name='" + s
										+ "',date='" + nyear + "f"
										+ (month + 1) + "s" + day
										+ "',didian='" + getDiDian()
										+ "' where name='" + name + "'");
								db.close();
								Toast.makeText(SetNameAndDate.this, "已保存",
										Toast.LENGTH_LONG).show();
								finish();
							}
						}
					}
				}
			}
		});
		cancelbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (!caozuo.equals("xiugai")) {
					Intent intent = new Intent();
					intent.putExtra("editor", "add");
					intent.setClass(SetNameAndDate.this, AddItem.class);
					SetNameAndDate.this.startActivity(intent);
				}
				finish();
			}
		});
	}

	public boolean haveOrNot(SQLiteDatabase db, String name) {

		Cursor cursor;
		if (caozuo.equals("xiugai")) {
			cursor = db.rawQuery(
					"select * from datanamelist where _id not in (" + id + ")"
					// " and end='"++"" +
					, null);
		} else {
			cursor = db.rawQuery("select * from datanamelist ", null);
		}
		if (cursor.moveToFirst()) {

			do {
				if (cursor.getString(cursor.getColumnIndex("name")).toString()
						.equals(name.toString())) {
					return true;
				}
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();
		return false;
	}

	public void setDate() {
		db = dbhelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from datanamelist where name='"
				+ this.name + "';", null);
		cursor.moveToNext();
		String s = cursor.getString(cursor.getColumnIndex("date"));
		int first = s.indexOf("f");
		int second = s.indexOf("s");
		this.nyear = Integer.parseInt(s.substring(0, first));
		this.month = Integer.parseInt(s.substring(first + 1, second)) - 1;
		this.day = Integer.parseInt(s.substring(second + 1));
		cursor.close();
		db.close();
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {

		KeyEvent key = new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BACK);
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (!this.caozuo.equals("xiugai")) {
				Intent intent = new Intent();
				intent.putExtra("editor", "add");
				intent.setClass(SetNameAndDate.this, AddItem.class);
				SetNameAndDate.this.startActivity(intent);
			}
			finish();

		}
		Log.i("lookover", "onkeydown");
		return super.onKeyDown(key.getKeyCode(), key);
	}

	private String getDiDian() {
		if (whereet.getText().equals("")) {
			return "";
		} else {
			return whereet.getText().toString();
		}

	}
}
