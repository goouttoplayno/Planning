package com.example.ui;

import com.example.socialplanning.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.SurfaceHolder.Callback;

public class MySurfaceView extends SurfaceView implements Callback, Runnable {

    private static final String TAG = "MySerfaceView";
    private Context mContext;
    private SurfaceHolder surfaceHolder;
    private boolean flag = false;// 线程标识
    private Bitmap bitmap_bg;// 背景图
    private Bitmap newbm;
    private int actNum = 2;

    private float mSurfaceWindth, mSurfaceHeight;// 屏幕宽高

    private int mBitposX;// 图片的位置

    private Canvas mCanvas;

    private Thread thread;

    private int id;

    // 背景移动状态
    private enum State {
        LEFT, RIGHT
    }

    // 默认为向左
    private State state = State.LEFT;

    private final int BITMAP_STEP = 40;// 背景画布移动步伐.

    public MySurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.i(TAG, "MySerfaceView");
        flag = true;
        this.mContext = context;
        setFocusable(true);
        setFocusableInTouchMode(true);
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
    }

    /***
     * 进行绘制.
     */
    private void onDoDraw() {
        Log.i(TAG, "onDoDraw");
        drawBG();
        updateBG();
    }

    /***
     * 更新背景.
     */
    public void updateBG() {
        Log.i(TAG, "updateBG");
        /** 图片滚动效果 **/
        switch (state) {
            case LEFT:
                mBitposX -= BITMAP_STEP;// 画布左移
                break;
            case RIGHT:
                mBitposX += BITMAP_STEP;// 画布右移
                break;

            default:
                break;
        }
        if (mBitposX <= -mSurfaceWindth) {
            state = State.RIGHT;
        }
        if (mBitposX >= 0) {
            state = State.LEFT;
        }
    }

    /***
     * 绘制背景
     */
    public void drawBG() {
        Log.i(TAG, "drawBG");
        try {
            mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);// 清屏幕.
            mCanvas.drawBitmap(newbm, mBitposX, 0, null);// 绘制当前屏幕背景
        } catch (NullPointerException e) {
            // TODO: handle exception

        }

    }

    @Override
    public void run() {
        Log.i(TAG, "run");
        while (flag) {
            if (-mBitposX <= mSurfaceWindth * id / (actNum - 1) - BITMAP_STEP) {
                state = State.LEFT;

            } else if (-mBitposX >= mSurfaceWindth * id / (actNum - 1) + BITMAP_STEP) {
                state = State.RIGHT;

            } else {
                if (id == 0) {
                    state = State.LEFT;
                } else if (id == 1) {
                    state = State.RIGHT;
                }
                flag = false;
            }
            synchronized (surfaceHolder) {
                mCanvas = surfaceHolder.lockCanvas();
                onDoDraw();
                surfaceHolder.unlockCanvasAndPost(mCanvas);
                try {
                    Thread.sleep(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public void setActNum(int actNum) {
        this.actNum = actNum;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.i(TAG, "surfaceCreated");
        mSurfaceHeight = getHeight();
        /***
         * 将图片缩放到屏幕的3/2倍.
         */
        bitmap_bg = BitmapFactory.decodeResource(this.getContext()
                .getResources(), R.drawable.background);

        int wantw = getWidth();
        int wanth = getHeight();
        float rat = bitmap_bg.getWidth() / bitmap_bg.getHeight();
        int w = (Float.valueOf(rat * wanth)).intValue();

        newbm = Bitmap.createScaledBitmap(bitmap_bg, w, wanth, true);

        mSurfaceWindth = newbm.getWidth() - getWidth();
        flag = true;
        synchronized (surfaceHolder) {
            mCanvas = surfaceHolder.lockCanvas();
            onDoDraw();
            surfaceHolder.unlockCanvasAndPost(mCanvas);
        }

        Log.i("create", mBitposX + "");

    }

    public void movePic(int id) {
        Log.i(TAG, "movePic");
        this.id = id;
        flag = true;
        thread = new Thread(this);
        thread.start();

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        Log.i(TAG, "surfaceChanged");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        flag = false;
    }

}
