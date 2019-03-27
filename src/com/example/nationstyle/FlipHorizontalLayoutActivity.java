/*
Copyright 2012 Aphid Mobile

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
 
   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

package com.example.nationstyle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.example.nationstyle.TravelAdapter;
import com.example.socialplanning.R;

public class FlipHorizontalLayoutActivity extends Activity {

	private FlipViewController flipView;
	private int where;

	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		where = Integer.parseInt(intent.getStringExtra("id"));
		flipView = new FlipViewController(this, FlipViewController.HORIZONTAL);
		flipView.setAdapter(new TravelAdapter(this, where));
		LayoutInflater factory = LayoutInflater
				.from(FlipHorizontalLayoutActivity.this);
		final View view = factory.inflate(R.layout.jianjie, null);
		final Button backbtn = (Button) view.findViewById(R.id.jianjieback);
		backbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		setContentView(flipView);
		flipView.setOnViewFlipListener(new FlipViewController.ViewFlipListener() {

			@Override
			public void onViewFlipped(View view, int position) {
				if (position == 0) {
					Toast.makeText(view.getContext(), "已到达第一页",
							Toast.LENGTH_SHORT).show();
				} else if (position == 55) {
					Toast.makeText(view.getContext(), "已到达最后一页",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		flipView.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		flipView.onPause();
		finish();
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {

		KeyEvent key = new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BACK);
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent();
			intent.setClass(FlipHorizontalLayoutActivity.this,
					com.example.nation.listViewActivity.class);
			FlipHorizontalLayoutActivity.this.startActivity(intent);
			finish();
		}

		return super.onKeyDown(key.getKeyCode(), key);
	}
}
