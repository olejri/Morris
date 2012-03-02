package morris.gui;


import java.util.ArrayList;

import morris.help.LogHelp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;


public class BoardView extends View {
	
	int xLeft = 0;
	int yTop = 0;
	int xRight = 470;
	int yBottom = 470;
	int viewWidth = 0;
    int viewHeight = 0;
	LogHelp l = new LogHelp();
	
	

	public BoardView(Context context) {
		super(context);	
	}

	public BoardView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}



	@Override
	protected void onDraw(Canvas canvas) {
		drawBoard(canvas);
		

	}
	protected void onSizeChanged(int xNew, int yNew, int xOld, int yOld){
        super.onSizeChanged(xNew, yNew, xOld, yOld);
        viewWidth = xNew;
        viewHeight = yNew;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		//This method is overridden in both local and network-class
		return true;
	}
	
	
	public void drawBoard(Canvas canvas){
		Paint p = getPaint(Color.BLACK);
		Rect r = new Rect(xLeft, yTop, viewWidth-1, viewWidth);
		l.Out("Test"+ p.getStrokeWidth());
		Log.i("LOGHELP", "Width" + viewWidth + "Height" + viewHeight);
		Log.i("LOGHELP", ""+p.getStrokeWidth());
		canvas.drawRect(r, p);
		
		
		
		
		
	}
	
	public Rect makeRect(){
		Rect r = new Rect();
		return r;
	}
	
	public Paint getPaint(int color){
		Paint p = new Paint();
		p.setStyle(Style.STROKE);
		p.setColor(color);
		return p;
		
	}

	

}


