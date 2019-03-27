package com.example.planning;

import com.example.SQL.DataBaseHelper;
import com.example.socialplanning.R;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.TimePicker;

public class EditorScan extends Activity {

	private TextView name;
	private TextView content;
	private TextView waring;
	private Button back;
	private CheckBox read;
	private TimePicker starttimepicker;
	private TimePicker endtimepicker;

	private String liuchengname = "";
	private String belongto = "";

	DataBaseHelper dbhelper;
	SQLiteDatabase db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lookover);

		dbhelper = new DataBaseHelper(this);
		Intent intent = getIntent();
		liuchengname = intent.getStringExtra("liuchengname");
		belongto = intent.getStringExtra("belongto");

		name = (TextView) findViewById(R.id.lgetitembname);
		content = (TextView) findViewById(R.id.lgetitembneirong);
		waring = (TextView) findViewById(R.id.litemzhuyi);
		read = (CheckBox) findViewById(R.id.biaoji);

		starttimepicker = (TimePicker) findViewById(R.id.lookovertimePicker);
		starttimepicker.setClickable(false);
		starttimepicker.setIs24HourView(true);
		starttimepicker.setFocusable(false);
		starttimepicker.setEnabled(false);

		endtimepicker = (TimePicker) findViewById(R.id.lendtimePicker);
		endtimepicker.setClickable(false);
		endtimepicker.setIs24HourView(true);
		endtimepicker.setFocusable(false);
		endtimepicker.setEnabled(false);

		read.setChecked(getChecked());

		read.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				db = dbhelper.getWritableDatabase();
				if (isChecked) {
					db.execSQL("update  liucheng set wancheng = 1 where belongto='"
							+ belongto
							+ "' and liuchengname='"
							+ liuchengname
							+ "'");
				} else
					db.execSQL("update  liucheng set wancheng = 0 where belongto='"
							+ belongto
							+ "' and liuchengname='"
							+ liuchengname
							+ "'");
				db.close();

			}
		});

		back = (Button) findViewById(R.id.lcancel);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		name.setText(liuchengname);
		Log.i("editorscan", liuchengname);
		db = dbhelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from liucheng where belongto='"
				+ belongto + "' and liuchengname='" + liuchengname + "'", null);
		while (cursor.moveToNext()) {
			if (!cursor.isNull(cursor.getColumnIndex("itemcontent"))) {
				content.setText(cursor.getString(cursor
						.getColumnIndex("itemcontent")));
			}
			if (!cursor.isNull(cursor.getColumnIndex("waring"))) {
				waring.setText(cursor.getString(cursor.getColumnIndex("waring")));
			}
			if (!cursor.isNull(cursor.getColumnIndex("start"))) {
				String s = cursor.getString(cursor.getColumnIndex("start"));
				starttimepicker.setCurrentHour(Integer.parseInt(s.substring(0,
						s.indexOf("f"))));
				starttimepicker.setCurrentMinute(Integer.parseInt(s.substring(s
						.indexOf("f") + 1)));
			}
			if (!cursor.isNull(cursor.getColumnIndex("end"))) {
				String s = cursor.getString(cursor.getColumnIndex("end"));
				endtimepicker.setCurrentHour(Integer.parseInt(s.substring(0,
						s.indexOf("f"))));
				endtimepicker.setCurrentMinute(Integer.parseInt(s.substring(s
						.indexOf("f") + 1)));
			}

		}
		cursor.close();
		db.close();

	}

	private boolean getChecked() {
		db = dbhelper.getReadableDatabase();
		int check;
		Cursor cursor = db.rawQuery("select * from liucheng where belongto='"
				+ belongto + "' and liuchengname='" + liuchengname + "'", null);
		cursor.moveToNext();
		check = cursor.getInt(cursor.getColumnIndex("wancheng"));
		if (check == 0)
			return false;
		else
			return true;
	}

}
