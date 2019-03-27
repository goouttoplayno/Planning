package com.example.planning;

import com.example.SQL.DataBaseHelper;
import com.example.socialplanning.R;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils.TruncateAt;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class LookOverItem extends Activity {
	private LinearLayout linearlayout;
	private TextView tv;
	private Button back;
	private Button xiugai;
	private String belongto = "";
	DataBaseHelper dbhelper;
	SQLiteDatabase db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lookoveritem);
		belongto = this.getIntent().getExtras().getString("itemname");

		tv = (TextView) findViewById(R.id.lotv);
		tv.setText(belongto);
		linearlayout = (LinearLayout) findViewById(R.id.lopaint);
		xiugai = (Button) findViewById(R.id.lxiugai);
		xiugai.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra("editor", "xiugai");
				intent.putExtra("belongto", belongto);
				intent.setClass(LookOverItem.this, AddItem.class);
				LookOverItem.this.startActivity(intent);
			}
		});
		back = (Button) findViewById(R.id.locancelbtn);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		dbhelper = new DataBaseHelper(this);
		addLiuCheng();
	}

	private void addLiuCheng() {
		db = dbhelper.getReadableDatabase();
		String str = "select * from liucheng where belongto='" + this.belongto
				+ "';";
		final Cursor cursor = db.rawQuery(str, null);
		Log.i("lookover", "outwhile");
		while (cursor.moveToNext()) {
			String leixing = cursor.getString(cursor.getColumnIndex("leixing"));
			String wancheng = cursor.getString(cursor
					.getColumnIndex("wancheng"));
			// int wancheng=0;
			final TextView as = new TextView(LookOverItem.this);
			as.setTextSize(20);
			as.setEllipsize(TruncateAt.END);
			as.setMaxEms(8);
			as.setMaxLines(2);
			as.setGravity(Gravity.CENTER_HORIZONTAL);
			Log.i("wanchengleme", wancheng);
			LinearLayout.LayoutParams lParams = new LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			if (leixing.startsWith("start")) {
				if (wancheng.equals("1")) {
					as.setBackgroundResource(R.drawable.start);
				} else {
					as.setBackgroundResource(R.drawable.yidustart);
				}
			} else if (leixing.startsWith("normal")) {
				if (wancheng.equals("1"))
					as.setBackgroundResource(R.drawable.normal);
				else
					as.setBackgroundResource(R.drawable.yidunormal);
			} else if (leixing.startsWith("important")) {
				if (wancheng.equals("1"))
					as.setBackgroundResource(R.drawable.important);
				else
					as.setBackgroundResource(R.drawable.yiduimportant);
			} else if (leixing.startsWith("end")) {
				if (wancheng.equals("1"))
					as.setBackgroundResource(R.drawable.endtool);
				else
					as.setBackgroundResource(R.drawable.yiduendtool);
			}
			final String str1 = cursor.getString(
					cursor.getColumnIndex("liuchengname")).toString();
			as.setText(str1);
			Log.i("lookover", str1);
			as.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub

					Intent intent = new Intent();
					intent.putExtra("belongto", belongto);
					intent.putExtra("liuchengname", str1);
					intent.setClass(LookOverItem.this, EditorScan.class);
					LookOverItem.this.startActivity(intent);

				}
			});
			linearlayout.addView(as, lParams);
		}

		cursor.close();
		db.close();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		linearlayout.removeAllViews();
		addLiuCheng();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		// linearlayout.removeAllViews();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.add(0, Menu.FIRST, 0, "全部标记为已完成").setIcon(
				R.drawable.menu_deleteall);
		menu.add(0, Menu.FIRST + 1, 1, "全部标记为未完成");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case Menu.FIRST:
			setDoneOrNot(1);
			break;
		case Menu.FIRST + 1:
			setDoneOrNot(2);
			break;
		}

		return false;
	}

	private void setDoneOrNot(int i) {
		// TODO Auto-generated method stub
		db = dbhelper.getWritableDatabase();
		if (i == 1) {
			db.execSQL("update  liucheng set wancheng = 1 where belongto='"
					+ belongto + "'");
		} else if (i == 2)
			db.execSQL("update  liucheng set wancheng = 0 where belongto='"
					+ belongto + "'");
		db.close();
		linearlayout.removeAllViews();
		addLiuCheng();
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void onOptionsMenuClosed(Menu menu) {
		// TODO Auto-generated method stub
		super.onOptionsMenuClosed(menu);
	}
}
