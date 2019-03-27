package com.example.planning;

import com.example.nationstyle.FlipViewController;

import android.app.Application;

public class MyApp extends Application {
	private FlipViewController fvc;
	private int i;
	private int where;

	public void setNum(int i) {
		this.i = i;
	}

	public int getNum() {
		return this.i;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		i = 1;
	}

	@SuppressWarnings("static-access")
	public void setWhere(int where) {
		this.where = where;
		this.fvc.where = where;
	}

	public int getWhere() {
		return this.where;
	}

}
