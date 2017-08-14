package com.example.huatuban;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;


/**实现画板功能的View*/
public class HuaBanView extends View {

	/**缓冲位图*/
	private Bitmap cacheBitmap;
	/**缓冲位图的画板*/
	private Canvas cacheCanvas;
	/**缓冲画笔*/
	private Paint paint;
	/**实际画笔*/
	private Paint BitmapPaint;
	/**保存绘制曲线路径*/
	private Path path;
	/**画布高*/
	private int height;
	/**画布宽*/
	private int width;
	
	/**保存上一次绘制的终点横坐标*/
	private float pX;
	/**保存上一次绘制的终点纵坐标*/
	private float pY;
	
	/**画笔初始颜色*/
	private int paintColor = Color.BLACK;
	/**线状状态*/
	private static Paint.Style paintStyle = Paint.Style.STROKE;
	/**画笔粗细*/
	private static int paintWidth = 3;
	
	private Canvas canvas;
	
	
	/**获取View实际宽高的方法*/
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		height = h;
		width = w;
		init();
	}
	
	public void init(){
		cacheBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		cacheCanvas = new Canvas(cacheBitmap);
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		path = new Path();
		BitmapPaint = new Paint();
		updatePaint();
	}
	
	private void updatePaint(){
		paint.setColor(paintColor);
		paint.setStyle(paintStyle);
		paint.setStrokeWidth(paintWidth);
	}
	
	public HuaBanView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public HuaBanView(Context context){
		super(context);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getAction();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			path.moveTo(event.getX(), event.getY());
			pX = event.getX();
			pY = event.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			path.quadTo(pX, pY, event.getX(), event.getY());
			pX = event.getX();
			pY = event.getY();
			break;
		case MotionEvent.ACTION_UP:
			cacheCanvas.drawPath(path, paint);
			path.reset();
			break;
		}
		invalidate();
		
		return true;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		this.canvas = canvas;
	
		BitmapPaint = new Paint();
		canvas.drawBitmap(cacheBitmap, 0,0, BitmapPaint);
		canvas.drawPath(path, paint);
		
	}
	
	/**更新画笔颜色*/
	public void setColor(int color){
		paintColor = color;
		updatePaint();
	}
	
	/**设置画笔粗细*/
	public void setPaintWidth(int width){
		paintWidth = width;
		updatePaint();
	}
	
	public static final int PEN = 1;
	public static final int PAIL = 2;
	
	/**设置画笔样式*/
	public void setStyle(int style){
		switch(style){
		case PEN:
			paintStyle = Paint.Style.STROKE;
			break;
		case PAIL:
			paintStyle = Paint.Style.FILL;
			break;
		}
		updatePaint();
	}
	
	/**清空画布*/
	public void clearScreen(){
		if(canvas != null){
			Log.e("e", "1");
		//	Paint backPaint = new Paint();
//			Log.e("e", "1");
//			backPaint.setColor(Color.WHITE);
//			Log.e("e", "1"+width+height);
//			backPaint.setStrokeWidth(1);
//			canvas.drawRect(new Rect(0, 0, width, height), backPaint);
//			Log.e("e", "1");
//			cacheCanvas.drawRect(new Rect(0, 0, width, height), backPaint);
//			Log.e("e", "1");
//			
//			Paint backPaint = new Paint();
//			backPaint.setStrokeWidth(1);
//			backPaint.setXfermode(new PorterDuffXfermode(android.graphics.PorterDuff.Mode.CLEAR));
//			canvas.drawPaint(backPaint);
//			backPaint.setXfermode(new PorterDuffXfermode(Mode.SRC));
//			canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR); 
			cacheBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
			cacheCanvas = new Canvas(cacheBitmap);
			paint = new Paint(Paint.ANTI_ALIAS_FLAG);
			path = new Path();
			BitmapPaint = new Paint();
			updatePaint();
		}
		invalidate();
	}
	
}
