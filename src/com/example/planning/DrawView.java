package com.example.planning;

import com.example.socialplanning.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;
import android.view.View;

public class DrawView extends View {
	public float currentX;
	public float currentY;
	public Bitmap bitmap;
	Context mContext;
	public int num;

	int image[] = { R.drawable.starttool, R.drawable.normal_move,
			R.drawable.important_move, R.drawable.endtool };

	public DrawView(Context context) {
		super(context);
		mContext = context;
		// currentX = x;
		// currentY = y;
	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		bitmap = BitmapFactory.decodeResource(mContext.getResources(),
				image[num]);
		Log.i("setnum", "ondraw");
		// bitmap.
		canvas.drawBitmap(bitmap, currentX, currentY, null);
		destory();
	}

	public void SetNum(int num) {
		this.num = num;
		Log.i("setnum", num + "");
	}

	public void destory() {
		if (bitmap != null || (!bitmap.isRecycled())) {
			bitmap.recycle();
			bitmap = null;

		}
	}
}
