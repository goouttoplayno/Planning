package com.example.ui;

import com.example.SQL.DataBaseHelper;
import com.example.nation.listViewActivity;
import com.example.planning.DateList;
import com.example.socialplanning.R;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private static final String TAG = "MainActivity";
	// private Button option;
	RelativeLayout relativeLayout;
	private TextView title;
	private int a = 0;
	boolean isExit;
	DataBaseHelper dbhelper;
	SQLiteDatabase db;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// option = (Button) findViewById(R.id.operation);
		relativeLayout = (RelativeLayout) findViewById(R.id.mainView);
		title = (TextView) findViewById(R.id.title);

		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);

		int screenWidth = dm.widthPixels;

		dbhelper = new DataBaseHelper(this);
		Typeface typeface = Typeface.createFromAsset(getAssets(),
				"fonts/TTGangBiF.ttf");
		title.setTypeface(typeface);

		CoverFlow cf = new CoverFlow(this);
		final Integer[] mImageIds = { R.drawable.a2,
				R.drawable.a4 };
		final Class<?>[] target = { DateList.class,
				listViewActivity.class};

		final MySurfaceView mySurfaceView = (MySurfaceView) findViewById(R.id.msv);
		mySurfaceView.setActNum(target.length);
		CoverFlowImageAdapter imageAdapter = new CoverFlowImageAdapter(this,
				mImageIds, target, screenWidth);

		cf.setAdapter(imageAdapter);
		cf.setSelection(0, true);
		cf.setAnimationDuration(1000);
		cf.setLayoutParams(new ViewGroup.MarginLayoutParams(
				ViewGroup.LayoutParams.FILL_PARENT,
				ViewGroup.LayoutParams.FILL_PARENT));

		// ��Ӽ�����

		cf.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				Log.i("covers", arg2 + "");
				if (a == arg2) {
					intent.setClass(MainActivity.this, target[arg2]);
					startActivity(intent);
				}
			}
		});

		cf.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				Log.i(TAG, "arg2:" + arg2 + ", arg3:" + arg3);
				a = arg2;
				mySurfaceView.movePic((int) arg3);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});
		// ��
		relativeLayout.addView(cf);

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exit();
			return false;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}

	public void exit() {
		if (!isExit) {
			isExit = true;
			Toast.makeText(getApplicationContext(), "�ٰ�һ���˳�����",
					Toast.LENGTH_SHORT).show();
			mHandler.sendEmptyMessageDelayed(0, 2000);
		} else {
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_HOME);
			startActivity(intent);
			db = dbhelper.getWritableDatabase();
			db.execSQL("delete from linshi");
			db.close();
			System.exit(0);

		}
	}

	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			isExit = false;
		}

	};
}
