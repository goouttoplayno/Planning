package com.example.planning;

import java.util.Calendar;

import com.example.SQL.DataBaseHelper;
import com.example.socialplanning.R;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;
import android.widget.Toast;

public class EditorAdd extends Activity {
	private TextView liuchengnameview;
	private EditText itemcontent;
	private EditText waring;
	private Button savebtn;
	private Button cancelbtn;
	private boolean save = false;
	private String liuchengname = "";
	private TimePicker starttimepicker;
	private TimePicker endtimepicker;
	Calendar calendar = Calendar.getInstance();

	private int shour;
	private int sminute;
	private int ehour;
	private int eminute;

	private DataBaseHelper dbhelper;
	SQLiteDatabase db;
	Builder ad;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.editor);

		dbhelper = new DataBaseHelper(this);
		Intent intent = getIntent();
		liuchengname = intent.getStringExtra("liuchengname");
		ad = new Builder(EditorAdd.this, R.style.dialog);
		liuchengnameview = (TextView) findViewById(R.id.getitembname);
		itemcontent = (EditText) findViewById(R.id.getitembneirong);
		waring = (EditText) findViewById(R.id.getitembzhuyi);
		savebtn = (Button) findViewById(R.id.save);
		cancelbtn = (Button) findViewById(R.id.cancel);
		starttimepicker = (TimePicker) findViewById(R.id.starttimePicker);
		endtimepicker = (TimePicker) findViewById(R.id.endtimePicker);
		starttimepicker.setIs24HourView(true);
		endtimepicker.setIs24HourView(true);
		getTime(liuchengname);
		liuchengnameview.setText(liuchengname);

		savebtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String a = "", b = "";
				if (!(itemcontent.getText().toString().equals("") && itemcontent
						.getText().toString() == null)) {

					a = itemcontent.getText().toString();

				}
				if (!(waring.getText().toString().equals("") && waring
						.getText().toString() == null)) {
					b = waring.getText().toString();
				}
				if (a.contains("'") || b.contains("'")) {
					Toast.makeText(EditorAdd.this, "请勿在编辑框中包含 ' 字符",
							Toast.LENGTH_SHORT).show();
				} else {
					setLiuCheng(dbhelper.getWritableDatabase(), liuchengname,
							a, b);
					a = "";
					b = "";
					save = true;
					Toast.makeText(EditorAdd.this, "已保存", Toast.LENGTH_SHORT)
							.show();

					Log.i("timess", "update linshi set waring ='" + waring
							+ "',itemcontent='" + itemcontent + "',start ='"
							+ shour + "f" + sminute + "',end = '" + ehour + "f"
							+ eminute + "' where liuchengname='" + liuchengname
							+ "';");

					finish();
				}
			}
		});
		cancelbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (!save) {
					ad.setTitle("返回")
							.setMessage("您还未保存，确认放弃修改？")
							.setPositiveButton("确定",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											// TODO Auto-generated method stub
											finish();
										}
									})
							.setNegativeButton("取消",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											// TODO Auto-generated method stub

										}
									}).show();
				} else {
					finish();
				}
			}
		});

		starttimepicker.setOnTimeChangedListener(new OnTimeChangedListener() {

			@Override
			public void onTimeChanged(TimePicker arg0, int arg1, int arg2) {
				// TODO Auto-generated method stub
				shour = arg1;
				sminute = arg2;
			}
		});
		endtimepicker.setOnTimeChangedListener(new OnTimeChangedListener() {

			@Override
			public void onTimeChanged(TimePicker arg0, int arg1, int arg2) {
				// TODO Auto-generated method stub
				ehour = arg1;
				eminute = arg2;
			}
		});
		isOrNot();
	}

	private void getTime(String liuchengname) {
		// TODO Auto-generated method stub
		db = dbhelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from linshi where liuchengname='"
				+ liuchengname + "'", null);
		cursor.moveToNext();

		if (!cursor.isNull(cursor.getColumnIndex("start"))) {
			String s = cursor.getString(cursor.getColumnIndex("start"));
			starttimepicker.setCurrentHour(Integer.parseInt(s.substring(0,
					s.indexOf("f"))));
			starttimepicker.setCurrentMinute(Integer.parseInt(s.substring(s
					.indexOf("f") + 1)));
		} else {
			shour = calendar.get(Calendar.HOUR);
			sminute = calendar.get(Calendar.MINUTE);
			starttimepicker.setCurrentHour(shour);
			starttimepicker.setCurrentMinute(sminute);
		}
		if (!cursor.isNull(cursor.getColumnIndex("end"))) {
			String s = cursor.getString(cursor.getColumnIndex("end"));
			endtimepicker.setCurrentHour(Integer.parseInt(s.substring(0,
					s.indexOf("f"))));
			endtimepicker.setCurrentMinute(Integer.parseInt(s.substring(s
					.indexOf("f") + 1)));
		}
	}

	public void setLiuCheng(SQLiteDatabase db, String liuchengname,
			String itemcontent, String waring) {// 传入liuchegnname
		// 和belongto进行判断
		// itemcontent和waring插入

		db.execSQL("update linshi set waring ='" + waring + "',itemcontent='"
				+ itemcontent + "',start ='" + shour + "f" + sminute
				+ "',end = '" + ehour + "f" + eminute
				+ "' where liuchengname='" + liuchengname + "';");
		// cursor.close();
		db.close();
	}

	public void isOrNot() {
		db = dbhelper.getReadableDatabase();
		String str = "select * from linshi where liuchengname = '"
				+ this.liuchengname + "';";
		Cursor cursor = db.rawQuery(str, null);
		if (cursor.moveToFirst()) {
			if (cursor.isNull(cursor.getColumnIndex("itemcontent"))) {
				itemcontent.setText("");
			} else {
				itemcontent.setText(cursor.getString(cursor
						.getColumnIndex("itemcontent")));
			}
			if (cursor.isNull(cursor.getColumnIndex("waring"))) {
				waring.setText("");
			} else {
				waring.setText(cursor.getString(cursor.getColumnIndex("waring")));
			}

			if (!cursor.isNull(cursor.getColumnIndex("start"))) {
				String s = cursor.getString(cursor.getColumnIndex("start"));
				starttimepicker.setCurrentHour(Integer.parseInt(s.substring(0,
						s.indexOf("f"))));
				shour = Integer.parseInt(s.substring(0, s.indexOf("f")));
				starttimepicker.setCurrentMinute(Integer.parseInt(s.substring(s
						.indexOf("f") + 1)));
				sminute = Integer.parseInt(s.substring(s.indexOf("f") + 1));
			}
			if (!cursor.isNull(cursor.getColumnIndex("end"))) {
				String s = cursor.getString(cursor.getColumnIndex("end"));
				endtimepicker.setCurrentHour(Integer.parseInt(s.substring(0,
						s.indexOf("f"))));
				ehour = Integer.parseInt(s.substring(0, s.indexOf("f")));
				endtimepicker.setCurrentMinute(Integer.parseInt(s.substring(s
						.indexOf("f") + 1)));
				eminute = Integer.parseInt(s.substring(s.indexOf("f") + 1));
			}
		}

		cursor.close();
		db.close();
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {

		KeyEvent key = new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BACK);
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (!save) {
				ad.setTitle("返回")
						.setMessage("您还未保存，确认放弃修改？")
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										finish();
									}
								});
				ad.setNegativeButton("取消",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub

							}
						}).show();
			} else {
				finish();
			}
		}

		return super.onKeyDown(key.getKeyCode(), key);
	}
}
