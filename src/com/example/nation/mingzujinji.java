package com.example.nation;

import android.R.integer;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.example.SQL.DataBaseHelper;
import com.example.nationstyle.FlipHorizontalLayoutActivity;
import com.example.nationstyle.FlipViewController;
import com.example.socialplanning.R;

public class mingzujinji extends Activity {
	DataBaseHelper dbHelper;
	SQLiteDatabase db;
	String id;
	TextView myTextView;
	TextView head;
	Button backbtn;
	private String[] names = new String[] { "阿昌族", "白族", "保安族 ", "布朗族 ", "布依族",
			"朝鲜族 ", "达斡尔族 ", "傣族", "德昂族 ", "东乡族 ", "侗族 ", "独龙族", "俄罗斯族",
			"鄂伦春族 ", "鄂温克族", "高山族 ", "仡佬族 ", "哈尼族 ", "哈萨克族 ", "汉族 ", "赫哲族 ",
			"回族 ", "基诺族 ", "京族 ", "景颇族 ", "柯尔克孜族", "拉祜族 ", "黎族", "僳僳族 ",
			"珞巴族 ", "满族", "毛南族", "门巴族 ", "蒙古族", "苗族", "仫佬族", "纳西族 ", "怒族 ",
			"普米族 ", "羌族", "撒拉族 ", "畲族 ", "水族", "塔吉克族", "塔塔尔族 ", "土家族 ", "土族",
			"佤族 ", "维吾尔族", "乌孜别克族 ", "锡伯族 ", "瑶族 ", "彝族 ", "裕固族 ", "藏族", "壮族" };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.jinji);
		head = (TextView) findViewById(R.id.jinjilotv);
		Intent intent = getIntent();
		id = intent.getStringExtra("id");
		dbHelper = new DataBaseHelper(this);
		db = dbHelper.getReadableDatabase();
		backbtn = (Button) findViewById(R.id.jinjiback);
		Cursor cursor = db
				.rawQuery("select* from nation where _id=" + id, null);
		cursor.moveToNext();
		myTextView = (TextView) findViewById(R.id.zwqtw);
		myTextView.setTextSize(20);
		myTextView.setText(cursor.getString(cursor.getColumnIndex("jinji")));
		cursor.close();
		db.close();

		int w = 0;
		for (int i = 0; i <= 55; i++) {
			if (Integer.parseInt(id) == i) {
				w = i;
				break;
			}
		}
		head.setText(names[w]);
		backbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra("id", id + "");
				intent.setClass(
						mingzujinji.this,
						com.example.nationstyle.FlipHorizontalLayoutActivity.class);
				mingzujinji.this.startActivity(intent);
				FlipViewController.where = Integer.parseInt(id);
				finish();
			}
		});
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {

		KeyEvent key = new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BACK);
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent();
			intent.putExtra("id", id + "");
			FlipViewController.where = Integer.parseInt(id);
			intent.setClass(mingzujinji.this,
					com.example.nationstyle.FlipHorizontalLayoutActivity.class);
			mingzujinji.this.startActivity(intent);
			finish();
		}

		return super.onKeyDown(key.getKeyCode(), key);
	}
}
