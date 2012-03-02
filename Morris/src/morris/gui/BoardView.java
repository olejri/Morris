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

	int xLeft = 10;
	int yTop = 10;
	int xRight = 470;
	int yBottom = 470;
	int viewWidth = 0;
	int viewHeight = 0;
	LogHelp l = new LogHelp();
	ArrayList<Point> pointList = new ArrayList<Point>();



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
		xRight = viewWidth;
		yBottom = viewWidth;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		//This method is overridden in both local and network-class
		return true;
	}


	public void drawBoard(Canvas canvas){
		//board calc
		int calcValue = (viewWidth-21)/6;
		int secondRect = calcValue;
		int thirdRect = calcValue*2;
		
		Paint p = getPaint();
		//Rect r = new Rect(xLeft, yTop, viewWidth-11, viewWidth-10);
		xRight = xRight-11;
		yBottom = yBottom-10;
		
		//first Rect
		drawRect(xLeft, yTop, xRight, yBottom, canvas, p);
		//second Rect
		drawRect(xLeft+secondRect, yTop+secondRect, xRight-secondRect, yBottom-secondRect, canvas, p);
		//third Rect
		drawRect(xLeft+thirdRect, yTop+thirdRect, xRight-thirdRect, yBottom-thirdRect, canvas, p);
		//first Line
		drawLine(xLeft, yBottom/2, xLeft+thirdRect, yBottom/2, canvas, p);
		//second Line
		drawLine(xRight-thirdRect, yBottom/2, xRight, yBottom/2, canvas, p);
		//third Line
		drawLine(xRight/2, yTop, xRight/2, yTop+thirdRect, canvas, p);
		//4th line
		drawLine(xRight/2, yBottom-thirdRect, xRight/2, yBottom, canvas, p);
		
		
		p.setStyle(Style.FILL);
		drawCircle(10, 10 , canvas, p);






	}

	public void drawRect(int xLeft, int yTop, int xRight, int yBottom, Canvas canvas, Paint p){
		Rect r = new Rect(xLeft, yTop, xRight, yBottom);
		canvas.drawRect(r, p);
	}

	public void drawCircle(int x, int y, Canvas canvas, Paint p){
		canvas.drawCircle(x, y, 10, p);
		
	}
	
	public void drawLine(int startX, int startY, int stopX, int stopY, Canvas canvas, Paint p){
		canvas.drawLine(startX, startY, stopX, stopY, p);
	}
	

	public Paint getPaint(){
		Paint p = new Paint();
		p.setStyle(Style.STROKE);
		p.setColor(Color.BLACK);
		p.setStrokeWidth(6);
		return p;

	}
	
	public void drawPoints(Canvas canvas , Paint p){
		
		
	}



}


