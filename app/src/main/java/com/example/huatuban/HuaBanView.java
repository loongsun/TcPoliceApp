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


/**ʵ�ֻ��幦�ܵ�View*/
public class HuaBanView extends View {

	/**����λͼ*/
	private Bitmap cacheBitmap;
	/**����λͼ�Ļ���*/
	private Canvas cacheCanvas;
	/**���廭��*/
	private Paint paint;
	/**ʵ�ʻ���*/
	private Paint BitmapPaint;
	/**�����������·��*/
	private Path path;
	/**������*/
	private int height;
	/**������*/
	private int width;
	
	/**������һ�λ��Ƶ��յ������*/
	private float pX;
	/**������һ�λ��Ƶ��յ�������*/
	private float pY;
	
	/**���ʳ�ʼ��ɫ*/
	private int paintColor = Color.BLACK;
	/**��״״̬*/
	private static Paint.Style paintStyle = Paint.Style.STROKE;
	/**���ʴ�ϸ*/
	private static int paintWidth = 3;
	
	private Canvas canvas;
	
	
	/**��ȡViewʵ�ʿ�ߵķ���*/
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
	
	/**���»�����ɫ*/
	public void setColor(int color){
		paintColor = color;
		updatePaint();
	}
	
	/**���û��ʴ�ϸ*/
	public void setPaintWidth(int width){
		paintWidth = width;
		updatePaint();
	}
	
	public static final int PEN = 1;
	public static final int PAIL = 2;
	
	/**���û�����ʽ*/
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
	
	/**��ջ���*/
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
