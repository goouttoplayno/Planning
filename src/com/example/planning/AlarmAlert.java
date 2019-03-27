package com.example.planning;

import com.example.socialplanning.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * 实际跳出闹钟的dialog
 * 
 * @author CxLong
 * 
 */
public class AlarmAlert extends Activity {
	music m = new music(this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		m.PlayMusic();
		new AlertDialog.Builder(AlarmAlert.this, R.style.dialog);
		new AlertDialog.Builder(AlarmAlert.this, R.style.dialog).setTitle("提醒")
				.setMessage("您制定的流程开始了")
				.setNegativeButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						m.FreeMusic();
						AlarmAlert.this.finish();
					}
				}).show();
	}

}
